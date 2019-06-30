package cmcc01;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;

import com.google.gson.Gson;

import cmcc.hbase.dao.HBaseDAO;
import cmcc.hbase.dao.impl.HBaseDAOImp;
import tools.DateFmt;

public class OneCellDropNumServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	private String dtime() {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR);// 小时
		int minute = cal.get(Calendar.MINUTE);// 分
		int second = cal.get(Calendar.SECOND);// 秒
		double time = (minute * 60 + second) / 3600.0;
		double stime = hour + time;
		System.out.println(stime);
		String ntime = new DecimalFormat("#.00").format(stime); // 保留一位小数
		return ntime;
	}

	private HBaseDAO hBaseDAO = new HBaseDAOImp();
	Gson gson = new Gson();

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String cell_num = request.getParameter("cell_num");
		String flag = request.getParameter("flag");

		response.setContentType("text/plain;charset=utf-8");

		PrintWriter out = response.getWriter();
		// 取当前
		String today = DateFmt.getCountDate(null, DateFmt.date_short);

		List list = new ArrayList();
		if ("first".equals(flag)) {
			// HBaseDAOImp中修改ZooKeeper设置
			// 如果是first，就去cell_monitor_table表中找 rowkey=29448-000001_2016-08-10
			// 的所有列，列名是cf:201608101042等
			Result rs = hBaseDAO.getOneRowAndMultiColumn("cell_monitor_table", cell_num + "_" + today,
					DateFmt.getCols());
			for (Cell cell : rs.rawCells()) {
				System.out.println("RowName:" + new String(CellUtil.cloneRow(cell)) + " ");
				System.out.println("Timetamp:" + cell.getTimestamp() + " ");
				System.out.println("column Family:" + new String(CellUtil.cloneFamily(cell)) + " ");
				System.out.println("row Name:" + new String(CellUtil.cloneQualifier(cell)) + " ");
				System.out.println("value:" + new String(CellUtil.cloneValue(cell)) + " ");
				list.add(new String(CellUtil.cloneValue(cell)));
			}

		} else {
			// 去当前时间的值
			Result rs = hBaseDAO.getOneRowAndMultiColumn("cell_monitor_table", cell_num + "_" + today,
					new String[] { DateFmt.getCountDate(null, DateFmt.date_minute) });
			for (Cell cell : rs.rawCells()) {
				System.out.println("RowName:" + new String(CellUtil.cloneRow(cell)) + " ");
				System.out.println("Timetamp:" + cell.getTimestamp() + " ");
				System.out.println("column Family:" + new String(CellUtil.cloneFamily(cell)) + " ");
				System.out.println("row Name:" + new String(CellUtil.cloneQualifier(cell)) + " ");
				System.out.println("value:" + new String(CellUtil.cloneValue(cell)) + " ");
				list.add(new String(CellUtil.cloneValue(cell)));
			}
		}
		out.print(gson.toJson(list));
		out.flush();
		out.close();
	}

	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);// 获取年份
		int month = cal.get(Calendar.MONTH);// 获取月份
		int day = cal.get(Calendar.DATE);// 获取日
		int hour = cal.get(Calendar.HOUR);// 小时
		int minute = cal.get(Calendar.MINUTE);// 分
		int second = cal.get(Calendar.SECOND);// 秒

		System.out.println(year + "--" + (month + 1) + "---" + day + "--" + hour + "--" + minute);
		// double time = (minute*60+second)/3600.0;
		// double stime = hour+time;
		// System.out.println(stime);
		// String ntime = new DecimalFormat("#.00").format(stime); //保留一位小数
		//// System.out.println(ntime);
		// System.out.println((59*60+20)/3600.0);
		// System.out.println((30*60+30)/3600.0);
	}
}
