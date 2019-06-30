package cmcc.hbase.dao.impl;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class SimpleHBase {

	public static void main(String[] args) {
		Configuration configuration = HBaseConfiguration.create();
		String tableName = "student";
		createTable(configuration, tableName);
		// addData(configuration, tableName);
		// getData(configuration, tableName);
		// getAllData(configuration, tableName);
		// deleteDate(configuration, tableName);
		// dropTable(configuration, tableName);

	}

	/**
	 * create a new Table
	 * 
	 * @param configuration
	 *            Configuration
	 * @param tableName
	 *            String,the new Table's name
	 */
	public static void createTable(Configuration configuration, String tableName) {
		HBaseAdmin admin;
		try {
			admin = new HBaseAdmin(configuration);
			if (admin.tableExists(tableName)) {
				admin.disableTable(tableName);
				admin.deleteTable(tableName);
				System.out.println(tableName + "is exist ,delete ......");
			}

			HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
			tableDescriptor.addFamily(new HColumnDescriptor("info"));
			tableDescriptor.addFamily(new HColumnDescriptor("address"));
			admin.createTable(tableDescriptor);
			System.out.println("end create table");
		} catch (MasterNotRunningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Delete the existing table
	 * 
	 * @param configuration
	 *            Configuration
	 * @param tableName
	 *            String,Table's name
	 */
	public static void dropTable(Configuration configuration, String tableName) {
		HBaseAdmin admin;
		try {
			admin = new HBaseAdmin(configuration);
			if (admin.tableExists(tableName)) {
				admin.disableTable(tableName);
				admin.deleteTable(tableName);
				System.out.println(tableName + "delete success!");
			} else {
				System.out.println(tableName + "Table does not exist!");
			}
		} catch (MasterNotRunningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * insert a data
	 * 
	 * @param configuration
	 *            Configuration
	 * @param tableName
	 *            String,Table's name
	 */
	public static void addData(Configuration configuration, String tableName) {
		HBaseAdmin admin;
		try {
			admin = new HBaseAdmin(configuration);
			if (admin.tableExists(tableName)) {
				HTable table = new HTable(configuration, tableName);
				Put put = new Put(Bytes.toBytes("zhangsan"));
				put.add(Bytes.toBytes("info"), Bytes.toBytes("age"), Bytes.toBytes("28"));
				table.put(put);
				System.out.println("add success!");
			} else {
				System.out.println(tableName + "Table does not exist!");
			}
		} catch (MasterNotRunningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Delete a data
	 * 
	 * @param configuration
	 *            Configuration
	 * @param tableName
	 *            String,Table's name
	 */
	public static void deleteDate(Configuration configuration, String tableName) {
		HBaseAdmin admin;
		try {
			admin = new HBaseAdmin(configuration);
			if (admin.tableExists(tableName)) {
				HTable table = new HTable(configuration, tableName);
				Delete delete = new Delete(Bytes.toBytes("zhangsan"));
				table.delete(delete);
				System.out.println("delete success!");
			} else {
				System.out.println("Table does not exist!");
			}
		} catch (MasterNotRunningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * get a data
	 * 
	 * @param configuration
	 *            Configuration
	 * @param tableName
	 *            String,Table's name
	 */
	public static void getData(Configuration configuration, String tableName) {
		HTable table;
		try {
			table = new HTable(configuration, tableName);
			Get get = new Get(Bytes.toBytes("zhangsan"));
			Result result = table.get(get);

			for (Cell cell : result.rawCells()) {
				System.out.println("RowName:" + new String(CellUtil.cloneRow(cell)) + " ");
				System.out.println("Timetamp:" + cell.getTimestamp() + " ");
				System.out.println("column Family:" + new String(CellUtil.cloneFamily(cell)) + " ");
				System.out.println("row Name:" + new String(CellUtil.cloneQualifier(cell)) + " ");
				System.out.println("value:" + new String(CellUtil.cloneValue(cell)) + " ");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * insert all data
	 * 
	 * @param configuration
	 *            Configuration
	 * @param tableName
	 *            String,Table's name
	 */
	public static void getAllData(Configuration configuration, String tableName) {
		HTable table;
		try {
			table = new HTable(configuration, tableName);
			Scan scan = new Scan();
			ResultScanner results = table.getScanner(scan);
			for (Result result : results) {
				for (Cell cell : result.rawCells()) {
					System.out.println("RowName:" + new String(CellUtil.cloneRow(cell)) + " ");
					System.out.println("Timetamp:" + cell.getTimestamp() + " ");
					System.out.println("column Family:" + new String(CellUtil.cloneFamily(cell)) + " ");
					System.out.println("row Name:" + new String(CellUtil.cloneQualifier(cell)) + " ");
					System.out.println("value:" + new String(CellUtil.cloneValue(cell)) + " ");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}