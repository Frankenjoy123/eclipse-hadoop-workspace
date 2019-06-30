package topo;

import java.util.ArrayList;
import java.util.List;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import bolt.CellDaoltBolt;
import bolt.CellFilterBolt;
import cmcc.constant.Constants;
import kafka.productor.KafkaProperties;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

public class KafkaOneCellMonintorTopology {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		TopologyBuilder builder = new TopologyBuilder();

		ZkHosts zkHosts = new ZkHosts(Constants.KAFKA_ZOOKEEPER_LIST);
		SpoutConfig spoutConfig = new SpoutConfig(zkHosts, 
				"mylog_cmcc", 
				"/MyKafka", // 偏移量offset的根目录
				"MyTrack"); // 对应一个应用
		List<String> zkServers = new ArrayList<String>();
		System.out.println(zkHosts.brokerZkStr);
		for (String host : zkHosts.brokerZkStr.split(",")) {
			zkServers.add(host.split(":")[0]);
		}

		spoutConfig.zkServers = zkServers;
		spoutConfig.zkPort = 2181;
		// 是否从头开始消费
		spoutConfig.forceFromStart = false; 
		spoutConfig.socketTimeoutMs = 60 * 1000;
		// String
		spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme()); 

		builder.setSpout("spout", new KafkaSpout(spoutConfig), 3);
		builder.setBolt("cellBolt", new CellFilterBolt(), 3).shuffleGrouping("spout");
		builder.setBolt("CellDaoltBolt", new CellDaoltBolt(), 5)
				.fieldsGrouping("cellBolt", new Fields("cell_num"));

		
		Config conf = new Config();
		conf.setDebug(false);
		conf.setNumWorkers(5);
		if (args.length > 0) {
			try {
				StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
			} catch (AlreadyAliveException e) {
				e.printStackTrace();
			} catch (InvalidTopologyException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Local running");
			LocalCluster localCluster = new LocalCluster();
			localCluster.submitTopology("mytopology", conf, builder.createTopology());
		}

	}

}
