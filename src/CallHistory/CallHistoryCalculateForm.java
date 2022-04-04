package CallHistory;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Login.Login;
class CallHistoryCalculateForm extends JFrame {
	Connection conn;
	Statement stmt;
	ResultSet rst;
	Vector vData = new Vector();
	Vector vTitle = new Vector();
	JScrollPane tableResult;
	DefaultTableModel model;
	JTable tb = new JTable();
	CallHistory callHis;
	String sql="";
	Login lg;
	public CallHistoryCalculateForm(String str, CallHistory ch) {
		super(str);
		callHis = ch;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-U2H3L4F\\SQLEXPRESS:1433;databaseName=DICHVU1080;integratedSecurity=true");
			stmt = conn.createStatement();
			Container cont = this.getContentPane();
			reload();
			model = new DefaultTableModel(vData,vTitle);
			tb = new JTable(model);
			tableResult = new JScrollPane(tb);
			cont.add(tableResult);
			this.setSize(500,535);
			this.setLocation(500,170);
			this.setVisible(true);
			this.setResizable(false);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void reload() {
		try {
			vTitle.clear();
			vData.clear();
			if (lg.accrts.equals("Admin")) sql = "select * from TINHCUOC order by ThoiGian desc";
			else sql = "select * from TINHCUOC_KHACHHANG where [Điện thoại gọi] = '" + lg.acct + "' order by [Thời gian] desc";
			ResultSet rst = stmt.executeQuery(sql);
			ResultSetMetaData rstmeta = rst.getMetaData();
			int num_column = rstmeta.getColumnCount();
			for (int i=1; i<=num_column; i++) vTitle.add(rstmeta.getColumnLabel(i));
			while (rst.next()) {
				Vector row = new Vector(num_column);
				for (int i=1; i<=num_column; i++) row.add(rst.getString(i));
				vData.add(row);
			}
			rst.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
