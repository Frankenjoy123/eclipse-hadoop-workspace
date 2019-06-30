package topo;

import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.ITuple;
import backtype.storm.tuple.Values;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.storm.hbase.bolt.mapper.HBaseValueMapper;

import java.util.ArrayList;
import java.util.List;

public class CommonHbaseValueMapper implements HBaseValueMapper {

    @Override
    public List<Values> toValues(ITuple tuple, Result result) throws Exception {
        List<Values> values = new ArrayList<Values>();
        Cell[] cells = result.rawCells();
        for(Cell cell : cells) {
            Values value = new Values (Bytes.toString(CellUtil.cloneQualifier(cell)), Bytes.toLong(CellUtil.cloneValue(cell)));
            values.add(value);
        }
        return values;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("columnName","columnValue"));
    }

}