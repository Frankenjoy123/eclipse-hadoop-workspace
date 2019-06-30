package cmcc.hbase.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.PrefixFilter;

import cmcc.constant.Constants;
import cmcc.hbase.dao.HBaseDAO;

public class HBaseDAOImp implements HBaseDAO {

	HConnection hTablePool = null;
	static Configuration conf = null;

	public HBaseDAOImp() {
		conf = new Configuration();
		// ZooKeeper连接
		String zk_list = Constants.HBASE_ZOOKEEPER_LIST;
		conf.set("hbase.zookeeper.quorum", zk_list);
		try {
			hTablePool = HConnectionManager.createConnection(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void save(Put put, String tableName) {
		// TODO Auto-generated method stub
		HTableInterface table = null;
		try {
			table = hTablePool.getTable(tableName);
			table.put(put);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				table.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void insert(String tableName, String rowKey, String family, String quailifer, String value) {
		// TODO Auto-generated method stub
		HTableInterface table = null;
		try {
			table = hTablePool.getTable(tableName);
			Put put = new Put(rowKey.getBytes());
			put.add(family.getBytes(), quailifer.getBytes(), value.getBytes());
			table.put(put);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				table.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void insert(String tableName, String rowKey, String family, String quailifer[], String value[]) {
		HTableInterface table = null;
		try {
			table = hTablePool.getTable(tableName);
			Put put = new Put(rowKey.getBytes());
			// 批量添加
			for (int i = 0; i < quailifer.length; i++) {
				String col = quailifer[i];
				String val = value[i];
				put.add(family.getBytes(), col.getBytes(), val.getBytes());
			}
			table.put(put);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				table.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void save(List<Put> Put, String tableName) {
		// TODO Auto-generated method stub
		HTableInterface table = null;
		try {
			table = hTablePool.getTable(tableName);
			table.put(Put);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				table.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public Result getOneRow(String tableName, String rowKey) {
		// TODO Auto-generated method stub
		HTableInterface table = null;
		Result rsResult = null;
		try {
			table = hTablePool.getTable(tableName);
			Get get = new Get(rowKey.getBytes());
			rsResult = table.get(get);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				table.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return rsResult;
	}

	@Override
	public List<Result> getRows(String tableName, String rowKeyLike) {
		// TODO Auto-generated method stub
		HTableInterface table = null;
		List<Result> list = null;
		try {
			table = hTablePool.getTable(tableName);
			PrefixFilter filter = new PrefixFilter(rowKeyLike.getBytes());
			Scan scan = new Scan();
			scan.setFilter(filter);
			ResultScanner scanner = table.getScanner(scan);
			list = new ArrayList<Result>();
			for (Result rs : scanner) {
				list.add(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				table.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public List<Result> getRows(String tableName, String rowKeyLike, String cols[]) {
		// TODO Auto-generated method stub
		HTableInterface table = null;
		List<Result> list = null;
		try {
			table = hTablePool.getTable(tableName);
			PrefixFilter filter = new PrefixFilter(rowKeyLike.getBytes());
			Scan scan = new Scan();
			for (int i = 0; i < cols.length; i++) {
				scan.addColumn("cf".getBytes(), cols[i].getBytes());
			}
			scan.setFilter(filter);
			ResultScanner scanner = table.getScanner(scan);
			list = new ArrayList<Result>();
			for (Result rs : scanner) {
				list.add(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				table.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public List<Result> getRows(String tableName, String startRow, String stopRow) {
		HTableInterface table = null;
		List<Result> list = null;
		try {
			table = hTablePool.getTable(tableName);
			Scan scan = new Scan();
			scan.setStartRow(startRow.getBytes());
			scan.setStopRow(stopRow.getBytes());
			ResultScanner scanner = table.getScanner(scan);
			list = new ArrayList<Result>();
			for (Result rsResult : scanner) {
				list.add(rsResult);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				table.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public void deleteRecords(String tableName, String rowKeyLike) {
		HTableInterface table = null;
		try {
			table = hTablePool.getTable(tableName);
			PrefixFilter filter = new PrefixFilter(rowKeyLike.getBytes());
			Scan scan = new Scan();
			scan.setFilter(filter);
			ResultScanner scanner = table.getScanner(scan);
			List<Delete> list = new ArrayList<Delete>();
			for (Result rs : scanner) {
				Delete del = new Delete(rs.getRow());
				list.add(del);
			}
			table.delete(list);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				table.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void createTable(String tableName, String[] columnFamilys) {
		try {
			// admin 对象
			HBaseAdmin admin = new HBaseAdmin(conf);
			if (admin.tableExists(tableName)) {
				System.err.println("此表，已存在！");
			} else {
				HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tableName));

				for (String columnFamily : columnFamilys) {
					tableDesc.addFamily(new HColumnDescriptor(columnFamily));
				}

				admin.createTable(tableDesc);
				System.err.println("建表成功!");

			}
			admin.close();// 关闭释放资源
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
	 * 删除一个表
	 * 
	 * @param tableName
	 *            删除的表名
	 */
	public void deleteTable(String tableName) {
		try {
			HBaseAdmin admin = new HBaseAdmin(conf);
			if (admin.tableExists(tableName)) {
				admin.disableTable(tableName);// 禁用表
				admin.deleteTable(tableName);// 删除表
				System.err.println("删除表成功!");
			} else {
				System.err.println("删除的表不存在！");
			}
			admin.close();
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
	 * 查询表中所有行
	 * 
	 * @param tablename
	 */
	public void scaner(String tablename) {
		try {
			HTable table = new HTable(conf, tablename);
			Scan s = new Scan();
			ResultScanner rs = table.getScanner(s);
			for (Result r : rs) {
				KeyValue[] kv = r.raw();
				for (int i = 0; i < kv.length; i++) {
					System.out.print(new String(kv[i].getRow()) + "");
					System.out.print(new String(kv[i].getFamily()) + ":");
					System.out.print(new String(kv[i].getQualifier()) + "");
					System.out.print(kv[i].getTimestamp() + "");
					System.out.println(new String(kv[i].getValue()));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		HBaseDAO dao = new HBaseDAOImp();

		// 创建表
		// String tableName="test";
		// String cfs[] = {"cf"};
		// dao.createTable(tableName,cfs);

		// 存入一条数据
		// Put put = new Put("bjsxt".getBytes());
		// put.add("cf".getBytes(), "name".getBytes(), "cai10".getBytes()) ;
		// dao.save(put, "test") ;

		// 插入多列数据
		// Put put = new Put("bjsxt".getBytes());
		// List<Put> list = new ArrayList<Put>();
		// put.add("cf".getBytes(), "addr".getBytes(), "shanghai1".getBytes()) ;
		// put.add("cf".getBytes(), "age".getBytes(), "30".getBytes()) ;
		// put.add("cf".getBytes(), "tel".getBytes(), "13889891818".getBytes())
		// ;
		// list.add(put) ;
		// dao.save(list, "test");

		// 插入单行数据
		// dao.insert("test", "testrow", "cf", "age", "35") ;
		// dao.insert("test", "testrow", "cf", "cardid", "12312312335") ;
		// dao.insert("test", "testrow", "cf", "tel", "13512312345") ;

		List<Result> list = dao.getRows("test", "testrow", new String[] { "age" });
		for (Result rs : list) {
			for (Cell cell : rs.rawCells()) {
				System.out.println("RowName:" + new String(CellUtil.cloneRow(cell)) + " ");
				System.out.println("Timetamp:" + cell.getTimestamp() + " ");
				System.out.println("column Family:" + new String(CellUtil.cloneFamily(cell)) + " ");
				System.out.println("row Name:" + new String(CellUtil.cloneQualifier(cell)) + " ");
				System.out.println("value:" + new String(CellUtil.cloneValue(cell)) + " ");
			}
		}

		Result rs = dao.getOneRow("test", "testrow");
		System.out.println(new String(rs.getValue("cf".getBytes(), "age".getBytes())));

	}

}
