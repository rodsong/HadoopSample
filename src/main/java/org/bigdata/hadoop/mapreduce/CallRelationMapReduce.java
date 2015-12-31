package org.bigdata.hadoop.mapreduce;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 有一批通话记录用户A拨打用户B的记录（call_detail.txt文件）
 * 要求做一个倒排索引，生成给用户B拨打电话的所有A用户，用|分割
 *
 * @author songyanfei
 * @version 1.0
 * @date 2015年12月28日 added
 * <p/>
 * 1. 首先上传call_detail.txt 到hdfs文件系统
 * hdfs dfs -put ./call_detail.txt
 * 2. 设置程序运行
 * 输入参数hdfs://rsong:9000/user/rsong/call_detail.txt output12
 */
public class CallRelationMapReduce {
    private static final Log LOG = LogFactory.getLog(CallRelationMapReduce.class);

    enum Counter {
        LINE_SKIP
    }

    public static class MyMap extends Mapper<LongWritable, Text, Text, Text> {
        public void map(LongWritable key, Text value, Context context) throws IOException {
            String line = value.toString();

            LOG.info("Mapper key:" + key.toString());
            try {
                String[] lineSplit = line.split(" ");
                String anum = lineSplit[0];
                String bnum = lineSplit[1];
                context.write(new Text(bnum.trim()), new Text(anum.trim()));
            } catch (InterruptedException e) {
                context.getCounter(Counter.LINE_SKIP).increment(1);//出错行计数器+1
                e.printStackTrace();
                return;
            }

        }
    }

    public static class MyReduce extends Reducer<Text, Text, Text, Text> {
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            String valueString = "";
            String out = "";
            for (Text value : values) {
                valueString = value.toString();
                out = out + valueString +"\t";
            }

            LOG.info("Reduce line:" + key.toString() + "\t" + out);
            context.write(key, new Text(out));
        }
    }


    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.err.println("Usage: MaxTemperature <input path> <output path>");
            System.exit(-1);
        }

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Call Relation");
        job.setJarByClass(CallRelationMapReduce.class);
        job.setMapperClass(MyMap.class);
        job.setCombinerClass(MyReduce.class);
        job.setReducerClass(MyReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
