package org.bigdata.hadoop.mapreduce.mapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;
  
/**
123456798676231190101234567986762311901012345679867623119010123456798676231190101234561+00121534567890356
123456798676231190101234567986762311901012345679867623119010123456798676231190101234562+01122934567890456
123456798676231190201234567986762311901012345679867623119010123456798676231190101234562+02120234567893456
123456798676231190401234567986762311901012345679867623119010123456798676231190101234561+00321234567803456
123456798676231190101234567986762311902012345679867623119010123456798676231190101234561+00429234567903456
123456798676231190501234567986762311902012345679867623119010123456798676231190101234561+01021134568903456
123456798676231190201234567986762311902012345679867623119010123456798676231190101234561+01124234578903456
123456798676231190301234567986762311905012345679867623119010123456798676231190101234561+04121234678903456
123456798676231190301234567986762311905012345679867623119010123456798676231190101234561+00821235678903456
*/

/**
1901    112
1902    212
1903    412
1904    32
1905    102
 */
public class TestMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
         private static final int MISSING = 9999;
         private static final Log LOG = LogFactory.getLog(TestMapper.class);
  
          public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output,Reporter reporter)
               throws IOException {

              LOG.info("KEY:" + key.toString());
              LOG.info("VALUE:"+value.toString());

             String line = value.toString();
             String year = line.substring(15, 19);
             int airTemperature;
             if (line.charAt(87) == '+') { // parseInt doesn't like leading plus signs
               airTemperature = Integer.parseInt(line.substring(88, 92));
             } else {
               airTemperature = Integer.parseInt(line.substring(87, 92));
             }
             String quality = line.substring(92, 93);
             if (airTemperature != MISSING && quality.matches("[012459]")) {
               output.collect(new Text(year), new IntWritable(airTemperature));
             }
           }
}
