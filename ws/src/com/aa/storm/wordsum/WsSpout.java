package com.aa.storm.wordsum;

import java.util.List;
import java.util.Map;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

/**
 * 数据累加 1+2+3+4
 * @author zhouxiaowu
 *
 */
public class WsSpout extends BaseRichSpout{

	private Map conf;
	private TopologyContext context;
	private SpoutOutputCollector collector;
	int i=0;

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.conf = conf;
		this.context = context;
		this.collector = collector;
		
	}

	/**
	 * 不断被线程调用，发送数据
	 */
	@Override
	public void nextTuple() {
		i++;	
		List tuple = new Values(i);
		this.collector.emit(tuple);
		
		System.out.println("spout -----" + tuple);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("n"));
	}

}
