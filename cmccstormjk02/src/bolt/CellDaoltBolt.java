package bolt;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import cmcc.hbase.dao.HBaseDAO;
import cmcc.hbase.dao.impl.HBaseDAOImp;
import tools.DateFmt;

public class CellDaoltBolt implements IBasicBolt {

	private static final long serialVersionUID = 1L;

	HBaseDAO dao = null;

	long beginTime = System.currentTimeMillis();
	long endTime = 0;

	// 通话总数
	Map<String, Long> cellCountMap = new HashMap<String, Long>();
	// 掉话数
	Map<String, Long> cellDropCountMap = new HashMap<String, Long>();

	String todayStr = null;

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		// input为2016-01-05,29448-000003,1
		if (input != null) {
			String dateStr = input.getString(0);
			String cellNum = input.getString(1);
			String dropNum = input.getString(2);

			// 判断是否是当天，不是当天 就清除map 避免内存过大
			// 基站数目 大概5-10万（北京市）
			// http://bbs.c114.net/thread-793707-1-1.html
			todayStr = DateFmt.getCountDate(null, DateFmt.date_short);

			// 跨天的处理，大于当天的数据来了，就清空两个map
			// 思考： 如果程序崩溃了，map清零了，如果不出问题，一直做同一个cellid的累加
			// 这个逻辑不好，应该换成一个线程定期的清除map数据，而不是这里判断
			if (todayStr != dateStr && todayStr.compareTo(dateStr) < 0) {
				cellCountMap.clear();
				cellDropCountMap.clear();
			}

			// 当前cellid的通话数统计
			Long cellAll = cellCountMap.get(cellNum);
			if (cellAll == null) {
				cellAll = 0L;
			}
			cellCountMap.put(cellNum, ++cellAll);

			// 掉话数统计，大于0就是掉话
			Long cellDropAll = cellDropCountMap.get(cellNum);
			int t = Integer.parseInt(dropNum);
			if (t > 0) {
				if (cellDropAll == null) {
					cellDropAll = 0L;
				}
				cellDropCountMap.put(cellNum, ++cellDropAll);
			}

			// 1.定时写库.为了防止写库过于频繁 这里间隔一段时间写一次
			// 2.也可以检测map里面数据size 写数据到 hbase
			// 3.自己可以设计一些思路 ，当然 采用redis 也不错
			// 4.采用tick定时存储也是一个思路
			endTime = System.currentTimeMillis();

			// flume+kafka 集成
			// 当前掉话数
			// 1.每小时掉话数目
			// 2.每小时 通话数据
			// 3.每小时 掉话率
			// 4.昨天的历史轨迹
			// 5.同比去年今天的轨迹（如果有数据）

			// hbase 按列存储的数据（）
			// 10万
			// rowkey cellnum+ day
			if (endTime - beginTime >= 5000) {
				// 5s 写一次库
				if (cellCountMap.size() > 0 && cellDropCountMap.size() > 0) {
					// x轴，相对于小时的偏移量，格式为 时：分，数值 数值是时间的偏移
					String arr[] = this.getAxsi();

					// 当前日期
					String today = DateFmt.getCountDate(null, DateFmt.date_short);
					// 当前分钟
					String today_minute = DateFmt.getCountDate(null, DateFmt.date_minute);

					// cellCountMap为通话数据的map
					Set<String> keys = cellCountMap.keySet();
					for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
						
						String key_cellnum = (String) iterator.next();
						
						System.out.println("key_cellnum: " + key_cellnum + "***" 
								+ arr[0] + "---" 
								+ arr[1] + "---"
								+ cellCountMap.get(key_cellnum) + "----" 
								+ cellDropCountMap.get(key_cellnum));
						
						//写入HBase数据，样例： {time_title:"10:45",xAxis:10.759722222222223,call_num:140,call_drop_num:91}
						
						dao.insert("cell_monitor_table", 
								key_cellnum + "_" + today, 
								"cf", 
								new String[] { today_minute },
								new String[] { "{" + "time_title:\"" + arr[0] + "\",xAxis:" + arr[1] + ",call_num:"
										+ cellCountMap.get(key_cellnum) + ",call_drop_num:" + cellDropCountMap.get(key_cellnum) + "}" }
								);
					}
				}
				// 需要重置初始时间
				beginTime = System.currentTimeMillis();
			}
		}
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		// TODO Auto-generated method stub
		dao = new HBaseDAOImp();
		Calendar calendar = Calendar.getInstance();
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	// 获取X坐标，就是当前时间的坐标，小时是单位
	public String[] getAxsi() {
		// 取当前时间
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int sec = c.get(Calendar.SECOND);
		// 总秒数
		int curSecNum = hour * 3600 + minute * 60 + sec;

		// (12*3600+30*60+0)/3600=12.5
		Double xValue = (double) curSecNum / 3600;
		// 时：分，数值 数值是时间的偏移
		String[] end = { hour + ":" + minute, xValue.toString() };
		return end;
	}

	@Override
	public void cleanup() {
	}
}