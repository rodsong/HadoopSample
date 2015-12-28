package org.bigdata.hadoop.mapreduce;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.bigdata.hadoop.mapreduce.mapper.TestMapper;
import org.bigdata.hadoop.mapreduce.reducer.TestReducer;

import java.io.IOException;

/**
 * MapReduce需要导成jar包，用 hadoop jar 命令执行.
 *
 *
 * 运行samp.txt 文件，气温demo数据
 */
public class TestHadoopMapRed {

	public static void main(String[] args) throws IOException {

		if (args.length != 2) {
			System.err.println("Usage: MaxTemperature <input path> <output path>");
			System.exit(-1);
		}
		JobConf job = new JobConf(TestHadoopMapRed.class);
		job.setJobName("test Max temperature");
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.setMapperClass(TestMapper.class);  //
		job.setReducerClass(TestReducer.class); //输入key对应maper的输出key类型，输入value对应mapper的输出value
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		JobClient.runJob(job);
	}

}