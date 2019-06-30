package com.sxt.storm.bolt;

import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class WsplitBolt extends BaseRichBolt {

	OutputCollector collector;

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		
		this.collector = collector;

	}

	@Override
	public void execute(Tuple input) {
		String line = input.getString(0);//获取一行数据
		
		//split切割
		String[] words = line.split(" ");
		
		//遍历输出
		for (String word : words) {
			
			List w = new Values(word);
			this.collector.emit(w);
			
		}

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
		declarer.declare(new Fields("w"));

	}

}
