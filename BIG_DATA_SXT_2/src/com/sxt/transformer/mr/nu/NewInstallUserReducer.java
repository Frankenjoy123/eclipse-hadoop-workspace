package com.sxt.transformer.mr.nu;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Reducer;

import com.sxt.transformer.model.dim.StatsUserDimension;
import com.sxt.transformer.model.value.map.TimeOutputValue;
import com.sxt.transformer.model.value.reduce.MapWritableValue;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.mapreduce.Reducer;
import com.sxt.common.KpiType;

public class NewInstallUserReducer extends Reducer<StatsUserDimension, TimeOutputValue, StatsUserDimension, MapWritableValue>{
	
    private MapWritableValue outputValue = new MapWritableValue();
    private Set<String> unique = new HashSet<String>();

	@Override
	protected void reduce(StatsUserDimension key, Iterable<TimeOutputValue> values,
			Reducer<StatsUserDimension, TimeOutputValue, StatsUserDimension, MapWritableValue>.Context context)
			throws IOException, InterruptedException {
		this.unique.clear();
		
		
		for(TimeOutputValue value : values) {
			this.unique.add(value.getId());
		
		}
		
		MapWritable map = new MapWritable();
		map.put(new IntWritable(-1), new IntWritable(unique.size()));
		outputValue.setValue(map);
		
		String kpiName  = key.getStatsCommon().getKpi().getKpiName();
		
		if (KpiType.NEW_INSTALL_USER.name.equals(kpiName)) {
			outputValue.setKpi(KpiType.NEW_INSTALL_USER);
		}else if (KpiType.BROWSER_NEW_INSTALL_USER.name.equals(kpiName)) {
			
			outputValue.setKpi(KpiType.BROWSER_NEW_INSTALL_USER);
		}
		
		context.write(key, outputValue);
		
		
		
	}
	
	

}
