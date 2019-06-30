package com.sxt.storm.transactional;

import java.math.BigInteger;

import backtype.storm.transactional.ITransactionalSpout;
import backtype.storm.utils.Utils;

public class MyCoordinator implements ITransactionalSpout.Coordinator<MyMeta>{

	// batch中消息条数
	public static int BATCH_NUM = 10 ;

	/**
	 * 在initializeTransaction前执行
	 * 确认数据源是否已经准备就绪，可以读取数据
	 * 返回值 true、false
	 */
	@Override
	public boolean isReady() {
		Utils.sleep(2000);
		return true;
	}

	/**
	 * txid：事务序列号
	 * prevMetadata：之前事务的元数据（如果第一次启动事务，则为null）
	 * 返回值：当前事务的元数据
	 */
	@Override
	public MyMeta initializeTransaction(BigInteger txid, MyMeta prevMetadata) {
		long beginPoint = 0;
		if (prevMetadata == null) {
			beginPoint = 0 ;
		}else {
			beginPoint = prevMetadata.getBeginPoint() + prevMetadata.getNum() ;
		}
		
		MyMeta meta = new MyMeta() ;
		meta.setBeginPoint(beginPoint);
		meta.setNum(BATCH_NUM);
		System.err.println("启动一个事务："+meta.toString());
		return meta;
	}
	
	@Override
	public void close() {
		
	}
}