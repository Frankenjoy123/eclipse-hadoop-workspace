package com.aa.storm.wordsum;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

public class Test {

	public static void main(String[] args) {
		
		TopologyBuilder tBuilder = new TopologyBuilder();
		tBuilder.setSpout("wsSpout", new WsSpout());
		tBuilder.setBolt("wdBolt", new WsBolt())
		.shuffleGrouping("wsSpout");
		
		LocalCluster lc =  new LocalCluster();
		lc.submitTopology("wordsum", new Config(), tBuilder.createTopology());
		
		
	}

}
