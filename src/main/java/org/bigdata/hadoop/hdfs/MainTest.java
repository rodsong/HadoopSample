package org.bigdata.hadoop.hdfs;

import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.io.IOUtils;

import java.io.InputStream;
import java.net.URL;

/**
 * hdfs:// 协议
 *
 * @author songyanfei
 * @version 1.0
 */
public class MainTest {

    static final String HDFS_PATH="hdfs://10.128.2.150:9000/user/rsong/samp.txt";

    public static void main(String[] args) throws Exception {
        URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
        final URL url = new URL(HDFS_PATH);
        final InputStream in = url.openStream();
        IOUtils.copyBytes(in, System.out, 1024, true);
    }
}
