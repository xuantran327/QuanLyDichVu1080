package Account;
import java.awt.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
class AccountStatisticForm extends JFrame {
	Connection conn;
	Statement stmt;
	ResultSet rst;
	Vector vData = new Vector();
	Vector vTitle = new Vector();
	JScrollPane tableResult;
	DefaultTableModel model;
	JTable tb;
	Account acct;
	String sql = "";
	public AccountStatisticForm(String str, Account acc) {
		super(str);
		acct = acc;
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
			this.setLayout(new GridLayout(1,2));
			this.setSize(480,450);
			this.setLocation(510,200);
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
			sql = "select G.MaCuoc, TenCuoc, count(DienThoai) 'SoLuongTaiKhoan' "
					+ "from TAIKHOAN_MACUOC T inner join "
					+ "GIACUOC G on T.MaCuoc = G.MaCuoc "
					+ "group by G.MaCuoc, TenCuoc";
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
