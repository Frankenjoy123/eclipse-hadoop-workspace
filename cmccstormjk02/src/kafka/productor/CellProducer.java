/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kafka.productor;

import java.util.Properties;
import java.util.Random;

import backtype.storm.utils.Utils;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import tools.DateFmt;

/***
 * 模拟发送数据到kafka中
 * 
 * @author hadoop
 *
 */
public class CellProducer extends Thread {

	// bin/kafka-topics.sh --create --zookeeper localhost:2181
	// --replication-factor 3 --partitions 5 --topic cmcccdr
	private final kafka.javaapi.producer.Producer<Integer, String> producer;
	private final String topic;
	private final Properties props = new Properties();

	public CellProducer(String topic) {
		props.put("serializer.class", "kafka.serializer.StringEncoder");// 字符串消息
		props.put("metadata.broker.list", KafkaProperties.broker_list);
		producer = new kafka.javaapi.producer.Producer<Integer, String>(new ProducerConfig(props));
		this.topic = topic;
	}

	/*
	 * public void run() { // order_id,order_amt,create_time,province_id Random
	 * random = new Random(); String[] cell_num = { "29448-37062",
	 * "29448-51331", "29448-51331","29448-51333", "29448-51343" }; String[]
	 * drop_num = { "0","1","2"};//掉话1(信号断断续续) 断话2(完全断开)
	 * 
	 * // Producer.java // record_time, imei, cell,
	 * ph_num,call_num,drop_num,duration,drop_rate,net_type,erl // 2011-06-28
	 * 14:24:59.867,356966,29448-37062,0,0,0,0,0,G,0 // 2011-06-28
	 * 14:24:59.867,352024,29448-51331,0,0,0,0,0,G,0 // 2011-06-28
	 * 14:24:59.867,353736,29448-51331,0,0,0,0,0,G,0 // 2011-06-28
	 * 14:24:59.867,353736,29448-51333,0,0,0,0,0,G,0 // 2011-06-28
	 * 14:24:59.867,351545,29448-51333,0,0,0,0,0,G,0 // 2011-06-28
	 * 14:24:59.867,353736,29448-51343,1,0,0,8,0,G,0 int i =0 ; NumberFormat nf
	 * = new DecimalFormat("000000"); while(true) { i ++ ; // String messageStr
	 * = i+"\t"+cell_num[random.nextInt(cell_num.length)]+"\t"+DateFmt.
	 * getCountDate(null,
	 * DateFmt.date_long)+"\t"+drop_num[random.nextInt(drop_num.length)] ;
	 * String testStr = nf.format(random.nextInt(10)+1);
	 * 
	 * String messageStr =
	 * i+"\t"+("29448-"+testStr)+"\t"+DateFmt.getCountDate(null,
	 * DateFmt.date_long)+"\t"+drop_num[random.nextInt(drop_num.length)] ;
	 * 
	 * System.out.println("product:"+messageStr); producer.send(new
	 * KeyedMessage<Integer, String>(topic, messageStr)); Utils.sleep(1000) ; //
	 * if (i==500) { // break; // } }
	 * 
	 * }
	 */
	public void run() {
		Random random = new Random();
		String[] cell_num = { "29448-37062", "29448-51331", "29448-51331", "29448-51333", "29448-51343" };
		// 正常0； 掉话1(信号断断续续)； 断话2(完全断开)
		String[] drop_num = { "0", "1", "2" };
		int i = 0;
		while (true) {
			i++;
			String testStr = String.format("%06d", random.nextInt(10) + 1);

			// messageStr: 2494 29448-000003 2016-01-05 10:25:17 1
			//
			String messageStr = i + "\t" + ("29448-" + testStr) + "\t" + DateFmt.getCountDate(null, DateFmt.date_long)
					+ "\t" + drop_num[random.nextInt(drop_num.length)];
			System.out.println("product:" + messageStr);
			producer.send(new KeyedMessage<Integer, String>(topic, messageStr));
			Utils.sleep(1000);
			// if(i == 500) {
			// break;
			// }
		}
	}

	public static void main(String[] args) {
		// topic设置
		CellProducer producerThread = new CellProducer(KafkaProperties.Cell_Topic);

		// 启动线程生成数据
		producerThread.start();

	}
}
