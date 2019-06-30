package com.sxt.storm.transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.transactional.ITransactionalSpout;
import backtype.storm.tuple.Fields;

public class MyTxSpout implements ITransactionalSpout<MyMeta> {

	/**
	 * 数据源
	 */
	Map<Long, String> dbMap = null;

	public MyTxSpout() {
		Random random = new Random();
		dbMap = new HashMap<Long, String>();
		String[] hosts = { "www.taobao.com" };
		String[] session_id = { 
				"ABYH6Y4V4SCVXTG6DPB4VH9U123", 
				"XXYH6YCGFJYERTT834R52FDXV9U34",
				"BBYH61456FGHHJ7JL89RG5VV9UYU7", 
				"CYYH6Y2345GHI899OFG4V9U567", 
				"VVVYH6Y4V4SFXZ56JIPDPB4V678" 
			};
		String[] time = { 
				"2017-02-21 08:40:50", 
				"2017-02-21 08:40:51", 
				"2017-02-21 08:40:52", 
				"2017-02-21 08:40:53",
				"2017-02-21 09:40:49", 
				"2017-02-21 10:40:49", 
				"2017-02-21 11:40:49", 
				"2017-02-21 12:40:49"
			};

		for (long i = 0; i < 100; i++) {
			dbMap.put(i, hosts[0] + "\t" + session_id[random.nextInt(5)] + "\t" + time[random.nextInt(8)]);
		}
	}

	private static final long serialVersionUID = 1L;

	@Override
	public backtype.storm.transactional.ITransactionalSpout.Coordinator<MyMeta> getCoordinator(Map conf,
			TopologyContext context) {
		return new MyCoordinator();
	}

	@Override
	public backtype.storm.transactional.ITransactionalSpout.Emitter<MyMeta> getEmitter(Map conf,
			TopologyContext context) {
		return new MyEmitter(dbMap);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tx", "log"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
