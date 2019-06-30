package bolt;

import java.util.Map;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import tools.DateFmt;

public class CellFilterBolt implements IBasicBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		String logString = input.getString(0);
		try {
			if (input != null) {
				String arr[] = logString.split("\\t");
				// messageStr格式：消息编号\t小区编号\t时间\t状态
				// 例：   2494  29448-000003  2016-01-05 10:25:17  1
				// DateFmt.date_short是yyyy-MM-dd，把2016-01-05 10:25:17格式化2016-01-05
				// 发出的数据格式： 时间, 小区编号, 掉话状态 
				collector.emit(new Values(DateFmt.getCountDate(arr[2], DateFmt.date_short), arr[1], arr[3]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("date", "cell_num", "drop_num"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
	}

	@Override
	public void prepare(Map map, TopologyContext arg1) {
		// TODO Auto-generated method stub
	}

}
