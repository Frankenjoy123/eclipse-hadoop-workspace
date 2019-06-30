package tools;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateFmt {

	public static final String date_long = "yyyy-MM-dd HH:mm:ss";
	public static final String date_short = "yyyy-MM-dd";
	public static final String date_minute = "yyyyMMddHHmm";

	public static SimpleDateFormat sdf = new SimpleDateFormat(date_short);

	public static String getCountDate(String date, String patton) {
		SimpleDateFormat sdf = new SimpleDateFormat(patton);
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			try {
				cal.setTime(sdf.parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return sdf.format(cal.getTime());
	}

	public static String getCountDate(String date, String patton, int step) {
		SimpleDateFormat sdf = new SimpleDateFormat(patton);
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			try {
				cal.setTime(sdf.parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		cal.add(Calendar.DAY_OF_MONTH, step);
		return sdf.format(cal.getTime());
	}

	public static Date parseDate(String dateStr) throws Exception {
		return sdf.parse(dateStr);
	}

	/**
	 * 返回一天所有分钟
	 * 
	 * @return
	 */
	public static String[] getCols() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);// 获取年份
		int month = cal.get(Calendar.MONTH);// 获取月份
		int day = cal.get(Calendar.DATE);// 获取日
		int hour = cal.get(Calendar.HOUR);// 小时
		int minute = cal.get(Calendar.MINUTE);// 分
		int second = cal.get(Calendar.SECOND);// 秒
		List<String> list = new ArrayList<String>();
		NumberFormat nf = new DecimalFormat("00");
		// {201504080000,}
		for (int i = 0; i < 24; i++) {
			for (int j = 0; j < 60; j++) {
				list.add(year + "" + nf.format(month + 1) + "" + nf.format(day) + nf.format(i) + "" + nf.format(j));
				// list.add(year+""+nf.format(
				// month+1)+""+nf.format(day)+nf.format(i)+""+"30");
			}
		}

		// list.add(year+""+nf.format(
		// month+1)+""+nf.format(day)+nf.format(23)+""+"59");
		// System.out.println(year+"--"+(month +
		// 1)+"---"+day+"--"+hour+"--"+minute);
		return list.toArray(new String[0]);
	}

	public static void main(String[] args) throws Exception {

		// System.out.println(DateFmt.getCountDate("2015-03-01 14:13:14",
		// DateFmt.date_short));
		// System.out.println(parseDate("2015-04-02").after(parseDate("2014-04-04")));
		System.out.println(getCols());
		String[] s = getCols();
		for (int i = 0; i < s.length; i++) {
			String string = s[i];
			System.out.println("DateFmt.main()===" + string);
		}
	}

}
