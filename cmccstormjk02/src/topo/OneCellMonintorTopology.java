package  topo;

import kafka.productor.KafkaProperties;
import spout.LogSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import bolt.CellDaoltBolt;
import bolt.CellFilterBolt;

public class OneCellMonintorTopology {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("spout", new LogSpout(KafkaProperties.Cell_Topic), 3);
		builder.setBolt("cellBolt", new CellFilterBolt(), 3).shuffleGrouping("spout");
		builder.setBolt("CellDaoltBolt", new CellDaoltBolt() , 5).fieldsGrouping("cellBolt", new Fields("cell_num")) ;
		
		Config conf = new Config() ;
//		conf.setDebug(true);
		conf.setDebug(false);
		conf.setNumWorkers(10);
		if (args.length > 0) {
			try {
				StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
			} catch (AlreadyAliveException e) {
				e.printStackTrace();
			} catch (InvalidTopologyException e) {
				e.printStackTrace();
			}
		}else {
			LocalCluster localCluster = new LocalCluster();
			localCluster.submitTopology("mytopology", conf, builder.createTopology());
		}
		
		
	}

}
