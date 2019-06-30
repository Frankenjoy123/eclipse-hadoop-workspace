package com.sxt.storm.bolt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class WcountBolt extends BaseRichBolt {

	Map<String, Integer> map = new HashMap<>();

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {

	}

	/**
	 * sxt nihao hello welcome ....
	 * 
	 */
	@Override
	public void execute(Tuple input) {

		// 接收单词
		String word = input.getStringByField("w");
		int count = 1;

		// 统计 使用map
		// 逻辑判断： 如果第一次获取该词，则录入map，否则更新出现次数
		if (map.containsKey(word)) {

			count = map.get(word) + 1;

		}
		
		map.put(word, count);
		
		//输出每个单词及其出现的次数
		System.out.println(word+"--------------------------"+count);
			
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {

	}

}
