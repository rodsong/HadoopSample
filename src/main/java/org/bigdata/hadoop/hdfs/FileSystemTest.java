package org.bigdata.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.io.IOUtils;

import java.io.*;
import java.net.URI;
import java.util.Arrays;

/**
 * HDFS java api， SystetmFile
 *
 * @author songyanfei
 * @version 1.0
 * @date 2015年12月24日 added
 *
 * **********************************************************************
 * I install hadoop on my laptop(hostname: rsong & ip :10.128.2.150)
 * **********************************************************************
 */


public class FileSystemTest {
    static final String HDFS_PATH = "hdfs://10.128.2.150:9000";
    static final String DIR_PATH = "/test2";
    static final String FILE_PATH = "/test2/f100.txt";


    public static void main(String[] args) throws Exception {
        //new FileSystemTest().uploadFile();
        //new FileSystemTest().testCreate();
        //new FileSystemTest().readTest();
        //new FileSystemTest().testExists();
        //new FileSystemTest().testFileBlockLocation();
        //new FileSystemTest().testGetHostName();
        //new FileSystemTest().testRename();
        //new FileSystemTest().testDel();
        new FileSystemTest().testAppend();
    }

    // 上传本地文件到HDFS
    private void uploadFile() throws Exception {
        FileSystem fileSystem = FileSystem.get(new URI(HDFS_PATH), new Configuration());
        fileSystem.mkdirs(new Path(DIR_PATH));
        FSDataOutputStream outputStream = fileSystem.create(new Path(FILE_PATH));
        FileInputStream in = new FileInputStream("test.txt");
        IOUtils.copyBytes(in, outputStream, 1024, true);
    }

    // 创建HDFS文件
    private void testCreate() throws Exception {
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(new URI(HDFS_PATH), conf);

        byte[] buff = "hello world!".getBytes();

        //默认在hdfs的 /user/<username>/hello.txt
        Path dst = new Path("hello.txt");
        FSDataOutputStream outputStream = null;
        try {
            outputStream = hdfs.create(dst);
            outputStream.write(buff, 0, buff.length);
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }

        FileStatus files[] = hdfs.listStatus(dst);
        for (FileStatus file : files) {
            System.out.println(file.getPath());
        }

        hdfs.close();
    }

    //读取文件
    private void readTest() throws Exception {
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(new URI(HDFS_PATH), conf);
        Path dst = new Path("hello.txt");
        FSDataInputStream in = hdfs.open(dst);
        IOUtils.copyBytes(in, System.out, 50, false);
        System.out.println();
    }


    // 查看HDFS文件是否存在
    private void testExists() throws Exception {
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(new URI(HDFS_PATH), conf);
        Path dst = new Path("hello.txt");
        boolean ok = hdfs.exists(dst);
        if (ok) {
            System.out.println(ok ? "文件存在" : "文件不存在");
            readTest();
        } else {
            System.out.println("文件不存在");
        }
    }

    //todo append method
    private void testAppend() throws Exception {
        String hdfs_path = "hdfs://10.128.2.150:9000/user/songyanfei/hello.txt";//文件路径
        String inpath = "doc/hadoop_install.txt";
        FileSystem fs = null;
        try {
            Configuration conf = new Configuration();
            fs = FileSystem.get(new URI(HDFS_PATH), conf);
            InputStream in = new BufferedInputStream(new FileInputStream(inpath));
            OutputStream out = fs.append(new Path(hdfs_path));
            IOUtils.copyBytes(in, out, 4096, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 重命名HDFS文件
    //my username is songyanfei.
    private void testRename() throws Exception {
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(new URI(HDFS_PATH), conf);
        Path dst = new Path(HDFS_PATH + "/user/songyanfei/");

        Path frpath = new Path("hello.txt");
        Path topath = new Path("hello2.txt");

        hdfs.rename(frpath, topath);
        FileStatus files[] = hdfs.listStatus(dst);
        for (FileStatus file : files) {
            System.out.println(file.getPath());
        }
    }

    // 刪除HDFS文件
    private void testDel() throws Exception {
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(new URI(HDFS_PATH), conf);
        Path dst = new Path(HDFS_PATH + "/user/songyanfei/");
        Path topath = new Path("hello.txt");

        boolean ok = hdfs.delete(topath, false);
        System.out.println(ok ? "删除成功" : "删除失败");

        FileStatus files[] = hdfs.listStatus(dst);
        for (FileStatus file : files) {
            System.out.println(file.getPath());
        }
    }

    //


    // 查看某个文件在HDFS集群的位置
    private void testFileBlockLocation() throws Exception {
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(new URI(HDFS_PATH), conf);
        Path dst = new Path("hello.txt");

        FileStatus fileStatus = hdfs.getFileStatus(dst);
        BlockLocation[] blockLocations = hdfs.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
        for (BlockLocation block : blockLocations) {
            System.out.println(Arrays.toString(block.getHosts()) + "\t" + Arrays.toString(block.getNames()));
        }
    }

    // 获取HDFS集群上所有节点名称
    private void testGetHostName() throws Exception {
        Configuration conf = new Configuration();
        DistributedFileSystem hdfs = (DistributedFileSystem) FileSystem.get(new URI(HDFS_PATH), conf);
        DatanodeInfo[] dataNodeStats = hdfs.getDataNodeStats();
        for (DatanodeInfo dataNode : dataNodeStats) {
            System.out.println(dataNode.getHostName() + "\t" + dataNode.getName());
        }
    }
}
