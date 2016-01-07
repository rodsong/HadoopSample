========================
Hadoop2.7.1 earning and using
========================

maven project.

mvn clean install.

1. doc
2. learning and using hadoop.

linux env
  hostname is rsong (ip:10.128.2.150)


3. org.bigdata.hadoop.hdfs.MainTest.java
   protocol:hdfs read hadoop file.

   if run this demo on windows environment,you may encounter as following error.
   Failed to locate the winutils binary in the hadoop binary path
   java.io.IOException: Could not locate executable null\bin\winutils.exe in the Hadoop binaries.

   you should be setup hadoop_home in windows env.
   1).download winutils on windows from github.com.
      https://github.com/srccodes/hadoop-common-2.2.0-bin zip

   2).set user's env on windows
      HADOOP_HOME = D:\hadoop-common-2.2.0-bin

4.FileSystemTest.java  HDFS Java API DEMO.

5. 安装hadoop相关软件请参apache官方安装文档，因为yarn和MPv1已经有很多改动，网上的安装教程一般是过时的。