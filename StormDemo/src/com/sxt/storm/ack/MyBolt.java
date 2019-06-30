package com.sxt.storm.ack;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class MyBolt implements IRichBolt {

	private static final long serialVersionUID = 1L;

	OutputCollector collector = null;
	@Override
	public void cleanup() {

	}
	int num = 0;
	String valueString = null;
	@Override
	public void execute(Tuple input) {
		try {
			valueString = input.getStringByField("log") ;
			
			if(valueString != null) {
				num ++ ;
				System.err.println(Thread.currentThread().getName()+"   lines  :"+num +"   session_id:"+valueString.split("\t")[1]);
			}
			collector.emit(input, new Values(valueString));
//			collector.emit(new Values(valueString));
			collector.ack(input);
			Thread.sleep(2000);
		} catch (Exception e) {
			collector.fail(input);
			e.printStackTrace();
		}
		
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector ;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("session_id")) ;
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
