package com.sxt.hbase;

import java.io.IOException;

import org.apache.commons.compress.compressors.pack200.Pack200CompressorInputStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hdfs.server.namenode.status_jsp;
import org.apache.hadoop.yarn.security.AdminACLsManager;
import org.junit.Before;
import org.junit.Test;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;

public class HBaseDemo {
	
	private static HBaseAdmin admin;
	
	private static String tableName = "phone";
	
	public static void main(String[] args) {
//		createTable()
		
//		insertTable();
		
		getTable();

	}
	
	private static void insertTable() {
		
		try {
			Configuration conf = new Configuration();
			conf.set("hbase.zookeeper.quorum", "node02,node03,node04");
			
			HTable hTable = new HTable(conf, tableName);
			
			Put put = new Put("1111".getBytes());
			put.add("cf".getBytes(), "name".getBytes(), "zhangsan".getBytes());
			put.add("cf".getBytes(), "age".getBytes(), "12".getBytes());
			put.add("cf".getBytes(), "sex".getBytes(), "boy".getBytes());
			hTable.put(put);
			
			if (admin != null) {
				admin.close();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private static void getTable() {
		
		try {
			Configuration conf = new Configuration();
			conf.set("hbase.zookeeper.quorum", "node02,node03,node04");
			
			HTable hTable = new HTable(conf, tableName);
			
			Get get = new Get("1111".getBytes());
			// define return columns in family
			get.addColumn("cf".getBytes(), "name".getBytes());
			get.addColumn("cf".getBytes(), "age".getBytes());
			get.addColumn("cf".getBytes(), "sex".getBytes());

			Result result = hTable.get(get);
			Cell cell = result.getColumnLatestCell("cf".getBytes(), "name".getBytes());
			Cell cell2 = result.getColumnLatestCell("cf".getBytes(), "age".getBytes());
			Cell cell3 = result.getColumnLatestCell("cf".getBytes(), "sex".getBytes());
//			System.out.println(new String( cell.getValue()));
			System.out.println(new String(CellUtil.cloneValue(cell)));
			System.out.println(new String(CellUtil.cloneValue(cell2)));
			System.out.println(new String(CellUtil.cloneValue(cell3)));
			
			Scan scan = new Scan();
			scan.setStartRow("1111".getBytes());
			scan.setStopRow("1111".getBytes());
//			hTable
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private static void createTable() {
		
		try {
			Configuration conf = new Configuration();
			conf.set("hbase.zookeeper.quorum", "node02,node03,node04");
			admin = new HBaseAdmin(conf);
			
			HTableDescriptor desc = new HTableDescriptor(tableName);
			HColumnDescriptor columnDescriptor = new HColumnDescriptor("cf");
			desc.addFamily(columnDescriptor);
			admin.createTable(desc);
			
			if (admin != null) {
				admin.close();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	

	
	
	
	
//	@Before
//	public void before() throws Exception {
//		Configuration conf = new Configuration();
//		conf.set("hbase.zookeeper.quorum", "node02,node03,node04");
//		admin = new HBaseAdmin(conf);
//	}
//	
//	@Test
//	public void createTable() throws Exception {
//		HTableDescriptor desc = new HTableDescriptor(tableName);
//		HColumnDescriptor columnDescriptor = new HColumnDescriptor("cf");
//		desc.addFamily(columnDescriptor);
//		admin.createTable(desc);
//	}
//	
//	
//	@Test
//	public void after() throws Exception {
//		if (admin != null) {
//			admin.close();
//		}
//	}
}
