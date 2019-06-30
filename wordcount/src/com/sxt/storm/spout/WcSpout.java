package com.sxt.storm.spout;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.hadoop.util.StringUtils;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class WcSpout extends BaseRichSpout {

	 SpoutOutputCollector collector;
	 String[] text = {
			 
			 "nihao sxt hello",
			 "sxt welcome ok",
			 "nihao sxt bye"
			 
	 };
	 
	 Random r = new Random();
	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		
		this.collector = collector;

	}

	/**
	 * 以行为单位发送字符串
	 */
	@Override
	public void nextTuple() {
		
		
		List line = new Values(text[r.nextInt(text.length)]);
		this.collector.emit(line);//发送数据行
		System.out.println("spout-------------------"+line);
		Utils.sleep(1000);
		

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
		declarer.declare(new Fields("line"));

	}

}
