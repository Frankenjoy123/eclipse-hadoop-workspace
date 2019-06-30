package topo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.hadoop.hbase.client.Durability;
import org.apache.storm.hbase.bolt.mapper.HBaseValueMapper;
import org.apache.storm.hbase.trident.mapper.SimpleTridentHBaseMapper;
import org.apache.storm.hbase.trident.mapper.TridentHBaseMapper;
import org.apache.storm.hbase.trident.state.HBaseState;
import org.apache.storm.hbase.trident.state.HBaseStateFactory;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import storm.kafka.trident.TransactionalTridentKafkaSpout;
import storm.kafka.trident.TridentKafkaConfig;
import storm.trident.Stream;
import storm.trident.TridentTopology;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.operation.builtin.FirstN;
import storm.trident.state.StateFactory;
import storm.trident.tuple.TridentTuple;
import tools.DateFmt;

/**
 * Created by root on 2016/4/13 0013.
 */
public class TridentOneCellMonintorTopology {

	public static class CellFilter extends BaseFunction {
		private static final long serialVersionUID = 1L;
		// 通话总数
		Map<String, Long> cellCountMap = new HashMap<String, Long>();
		// 掉话数
		Map<String, Long> cellDropCountMap = new HashMap<String, Long>();
		long beginTime = System.currentTimeMillis();
		long endTime = 0;
		String todayStr = null;

		@Override
		public void execute(TridentTuple tuple, TridentCollector collector) {
			if (tuple == null) {
				return;
			}
			String line = tuple.getString(0);
			String dateStr = null;
			String cellNum = null;
			String dropNum = null;
			try {
				String[] str = line.split("\\t");
				// 发出的数据 时间,小区编号，掉话状态
				dateStr = DateFmt.getCountDate(str[2], DateFmt.date_short);
				cellNum = str[1];
				dropNum = str[3];
			} catch (Exception e) {
				e.printStackTrace();
			}

			todayStr = DateFmt.getCountDate(null, DateFmt.date_short);
			// 跨天的处理
			if (todayStr != dateStr && todayStr.compareTo(dateStr) < 0) {
				cellCountMap.clear();
				cellDropCountMap.clear();
			}

			Long cellAll = cellCountMap.get(cellNum);
			if (cellAll == null) {
				cellAll = 0L;
			}

			cellAll++;
			cellCountMap.put(cellNum, cellAll);
			Long cellDropAll = cellDropCountMap.get(cellNum);
			int t = Integer.parseInt(dropNum);
			if (t > 0) {
				if (cellDropAll == null) {
					cellDropAll = 0L;
				}
				cellDropAll++;
				cellDropCountMap.put(cellNum, cellDropAll);
			}
			// 每隔五秒统计一次掉话率
			endTime = System.currentTimeMillis();
			if (endTime - beginTime >= 5000) {
				String today_minute = DateFmt.getCountDate(null, DateFmt.date_minute);
				String[] arr = this.getAxsi();
				long dropnum = cellDropCountMap.containsKey(cellNum) ? cellDropCountMap.get(cellNum) : 0;
				double drop_rate = dropnum / Double.valueOf(cellCountMap.get(cellNum));

				collector.emit(new Values(drop_rate, cellNum + "_" + todayStr, today_minute));
				// collector.emit(new
				// Values(cellNum+"_"+todayStr,"{"+"time_title:\""+arr[0]+"\",xAxis:"+
				// arr[1]+",call_num:"+cellCountMap.get(cellNum)+",call_drop_num:"+drop_rate+"}"));
				beginTime = System.currentTimeMillis();
			}

		}

		// 获取X坐标
		public String[] getAxsi() {
			Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			int sec = c.get(Calendar.SECOND);
			// 总秒数
			int curSecNum = hour * 3600 + minute * 60 + sec;

			Double xValue = (double) curSecNum / 3600;
			// 时间，数值
			String[] end = { hour + ":" + minute, xValue.toString() };
			return end;
		}
	}

	public static void main(String[] args) {
		// config kafka spout
		String topicName = "mylog_cmcc";

		// create TransactionalTridentKafkaSpout
		ZkHosts zkHosts = new ZkHosts("192.168.57.4:2181,192.168.57.5:2181,192.168.57.6:2181");
		TridentKafkaConfig tridentKafkaConfig = new TridentKafkaConfig(zkHosts, topicName,
				UUID.randomUUID().toString());
		List<String> zkServers = new ArrayList<String>();
		System.out.println(zkHosts.brokerZkStr);
		for (String host : zkHosts.brokerZkStr.split(",")) {
			zkServers.add(host.split(":")[0]);
		}

		tridentKafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
		tridentKafkaConfig.forceFromStart = false;
		TransactionalTridentKafkaSpout tridentKafkaSpout = new TransactionalTridentKafkaSpout(tridentKafkaConfig);

		TridentHBaseMapper tridentHBaseMapper = new SimpleTridentHBaseMapper().withColumnFamily("cf")
				.withColumnFields(new Fields("today_minute", "drop_rate"))
				// .withCounterFields(new Fields("cell_num"))
				.withRowKeyField("cell_num");

		HBaseValueMapper rowToStormValueMapper = new CommonHbaseValueMapper();
		// create 'cell_monitor_drop_rate','cf'
		// 这里的hdfs://chenkl/hbase是hbase在hdfs的root路径
		HBaseState.Options options = new HBaseState.Options().withConfigKey("hdfs://chenkl/hbase")
				.withDurability(Durability.SYNC_WAL).withMapper(tridentHBaseMapper)
				.withRowToStormValueMapper(rowToStormValueMapper).withTableName("cell_monitor_drop_rate");

		StateFactory factory = new HBaseStateFactory(options);

		TridentTopology topology = new TridentTopology();
		// 这里可以和上面的代码组合统计某个基站的掉话率变化，这里注释了，但是存储的时候需要改变一下不能用SimpleTridentHBaseMapper存储了
		// Stream stream = topology.newStream(topicName, tridentKafkaSpout)
		// .each(new Fields("str"), new CellFilter(), new Fields("drop_rate",
		// "cell_num", "date_rate")).toStream();
		// stream.partitionPersist(factory,new Fields("cell_num","drop_rate",
		// "date_rate"),new HBaseUpdater(),new Fields());

		Stream stream = topology.newStream(topicName, tridentKafkaSpout)
				.each(new Fields("str"), new CellFilter(), new Fields("drop_rate", "cell_num", "today_minute"))
				.groupBy(new Fields("today_minute")).aggregate(new Fields("drop_rate", "cell_num"),
						new FirstN.FirstNSortedAgg(10, "drop_rate", false), new Fields("drop_rate", "cell_num"))
				.toStream();
		stream.partitionPersist(factory, new Fields("cell_num", "drop_rate", "today_minute"), new HBaseUpdater(),
				new Fields());

		Config conf = new Config();
		conf.setDebug(true);
		conf.setNumWorkers(3);
		// StormSubmitter.submitTopologyWithProgressBar(topoName, conf,
		// topology.build());
		LocalCluster localCluster = new LocalCluster();
		localCluster.submitTopology(topicName, conf, topology.build());
	}
}
