package com.sxt.transformer.mr.nu;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.MultipleColumnPrefixFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TestTableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.google.common.collect.Lists;
import com.sun.org.apache.regexp.internal.recompile;
import com.sxt.common.EventLogConstants;
import com.sxt.common.EventLogConstants.EventEnum;
import com.sxt.common.GlobalConstants;
import com.sxt.transformer.model.dim.StatsUserDimension;
import com.sxt.transformer.model.value.map.TimeOutputValue;
import com.sxt.transformer.model.value.reduce.MapWritableValue;
import com.sxt.transformer.mr.TransformerOutputFormat;
import com.sxt.util.TimeUtil;


public class NewInstallUserRunner implements Tool{
	
	Configuration conf = null;
	
	public static void main(String[] args) throws Exception {
		ToolRunner.run(new Configuration(), new NewInstallUserRunner(), args);	
	}
	
	@Override
	public Configuration getConf() {
		return this.conf;
	}

	@Override
	public void setConf(Configuration conf) {
		conf.set("fs.defaultFS", "hdfs://node01:8020");
		conf.set("hbase.zookeeper.quorum", "node02,node03,node04");
		conf.addResource("output-collector.xml");
		conf.addResource("query-mapping.xml");
		conf.addResource("transformer-env.xml");
		this.conf = HBaseConfiguration.create(conf);
		
	}

	@Override
	public int run(String[] args) throws Exception {
        Configuration conf = this.getConf();
        // 处理参数
        this.processArgs(conf, args);

        Job job = Job.getInstance(conf, "new_install_user");

        job.setJarByClass(NewInstallUserRunner.class);
        // 本地运行
        TableMapReduceUtil.initTableMapperJob(
        		initScans(job), 
        		NewInstallUserMapper.class, 
        		StatsUserDimension.class, 
        		TimeOutputValue.class, 
        		job, 
        		false);
        // 集群运行：本地提交和打包(jar)提交
//         TableMapReduceUtil.initTableMapperJob(initScans(job), NewInstallUserMapper.class, StatsUserDimension.class, TimeOutputValue.class, job);
        job.setReducerClass(NewInstallUserReducer.class);
        job.setOutputKeyClass(StatsUserDimension.class);
        job.setOutputValueClass(MapWritableValue.class);
//        job.setInputFormatClass(KeyValueTextInputFormat.class);
        job.setOutputFormatClass(TransformerOutputFormat.class);
        if (job.waitForCompletion(true)) {
            // 执行成功, 需要计算总用户
//            this.calculateTotalUsers(conf);
            return 0;
        } else {
            return -1;
        }
	
	
	}
	
    /**
     * 处理参数
     * 
     * @param conf
     * @param args
     */
    private void processArgs(Configuration conf, String[] args) {
        String date = null;
        for (int i = 0; i < args.length; i++) {
            if ("-d".equals(args[i])) {
                if (i + 1 < args.length) {
                    date = args[++i];
                    break;
                }
            }
        }

        // 要求date格式为: yyyy-MM-dd
        if (StringUtils.isBlank(date) || !TimeUtil.isValidateRunningDate(date)) {
            // date是一个无效时间数据
            date = TimeUtil.getYesterday(); // 默认时间是昨天
        }
        System.out.println("----------------------" + date);
        conf.set(GlobalConstants.RUNNING_DATE_PARAMES, date);
    }
	
    /**
     * 初始化scan集合
     * 
     * @param job
     * @return
     */
    private List<Scan> initScans(Job job) {
        // 时间戳+....
        Configuration conf = job.getConfiguration();
        // 获取运行时间: yyyy-MM-dd
        String date = conf.get(GlobalConstants.RUNNING_DATE_PARAMES);
        long startDate = TimeUtil.parseString2Long(date);
        long endDate = startDate + GlobalConstants.DAY_OF_MILLISECONDS;

        Scan scan = new Scan();
        // 定义hbase扫描的开始rowkey和结束rowkey
        scan.setStartRow(Bytes.toBytes("" + startDate));
        scan.setStopRow(Bytes.toBytes("" + endDate));
        
        FilterList filterList = new FilterList();
        // 过滤数据，只分析launch事件
        filterList.addFilter(new SingleColumnValueFilter(Bytes.toBytes(EventLogConstants.EVENT_LOGS_FAMILY_NAME),
        		Bytes.toBytes(EventLogConstants.LOG_COLUMN_NAME_EVENT_NAME), 
        		CompareOp.EQUAL, Bytes.toBytes(EventEnum.LAUNCH.alias)));
        // 定义mapper中需要获取的列名
        String[] columns = new String[] {
        			EventLogConstants.LOG_COLUMN_NAME_EVENT_NAME, 
        			EventLogConstants.LOG_COLUMN_NAME_UUID, 
        			EventLogConstants.LOG_COLUMN_NAME_SERVER_TIME, 
        			EventLogConstants.LOG_COLUMN_NAME_PLATFORM, 
        			EventLogConstants.LOG_COLUMN_NAME_BROWSER_NAME, 
        			EventLogConstants.LOG_COLUMN_NAME_BROWSER_VERSION };
//        scan.addColumn(family, qualifier)
        filterList.addFilter(this.getColumnFilter(columns));

        scan.setAttribute(Scan.SCAN_ATTRIBUTES_TABLE_NAME, Bytes.toBytes(EventLogConstants.HBASE_NAME_EVENT_LOGS));
        scan.setFilter(filterList);
        return Lists.newArrayList(scan);
    }
    
    /**
     * 获取这个列名过滤的column
     * 
     * @param columns
     * @return
     */
    private Filter getColumnFilter(String[] columns) {
        int length = columns.length;
        byte[][] filter = new byte[length][];
        for (int i = 0; i < length; i++) {
            filter[i] = Bytes.toBytes(columns[i]);
        }
        return new MultipleColumnPrefixFilter(filter);
    }

}
