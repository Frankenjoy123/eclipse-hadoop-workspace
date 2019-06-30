package com.sxt.storm.drpc;


import org.apache.thrift7.TException;

import backtype.storm.generated.DRPCExecutionException;
import backtype.storm.utils.DRPCClient;

public class MyDRPCclient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		DRPCClient client = new DRPCClient("node01", 3772);
		
		try {
			String result = client.execute("exclamation", "11,22");
			
			System.out.println(result);
		} catch (TException e) {
			e.printStackTrace();
		} catch (DRPCExecutionException e) {
			e.printStackTrace();
		} 
	}
}
