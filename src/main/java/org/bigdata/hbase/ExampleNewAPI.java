package org.bigdata.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

public class ExampleNewAPI {

    private static final String TABLE_NAME = "my_test";
    private static final String CF_DEFAULT = "dcf";

    public static void createOrOverwrite(Admin admin, HTableDescriptor table) throws IOException {
        if (admin.tableExists(table.getTableName())) {
            admin.disableTable(table.getTableName());
            admin.deleteTable(table.getTableName());
        }
        admin.createTable(table);
    }

    public static void createSchemaTables(Configuration config) throws IOException {
        try (Connection connection = ConnectionFactory.createConnection(config);
             Admin admin = connection.getAdmin()) {

            HTableDescriptor table = new HTableDescriptor(TableName.valueOf(TABLE_NAME));
            table.addFamily(new HColumnDescriptor(CF_DEFAULT));

            System.out.println("Creating table... ");
            createOrOverwrite(admin, table);
            System.out.println(" Done.");
        }
    }

    public static void modifySchema(Configuration config) throws IOException {
        try (Connection connection = ConnectionFactory.createConnection(config);
             Admin admin = connection.getAdmin()) {

            TableName tableName = TableName.valueOf(TABLE_NAME);
            if (!admin.tableExists(tableName)) {
                System.out.println("Table does not exist.");
                System.exit(-1);
            }

            // Update existing table
//            HColumnDescriptor newColumn = new HColumnDescriptor("newcf");
//            newColumn.setCompactionCompressionType(Algorithm.GZ);
//            newColumn.setMaxVersions(HConstants.ALL_VERSIONS);
//            admin.addColumn(tableName, newColumn);

            // Disable an existing table
            admin.disableTable(tableName);
            // Delete an existing column family
            admin.deleteColumn(tableName, CF_DEFAULT.getBytes("UTF-8"));
            // Delete a table (Need to be disabled first)
            //admin.deleteTable(tableName);
        }
    }

    public static void main(String... args) throws IOException {
        Configuration config = HBaseConfiguration.create();

        //Add any necessary configuration files (hbase-site.xml, core-site.xml)
        config.addResource("hbase-site.xml");
        config.addResource("core-site.xml");
        //createSchemaTables(config);
        modifySchema(config);
    }
}