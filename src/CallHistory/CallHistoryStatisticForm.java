package CallHistory;
import java.awt.Container;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Login.Login;
class CallHistoryStatisticForm extends JFrame implements ActionListener {
	Connection conn;
	Statement stmt;
	ResultSet rst;
	Vector vData = new Vector();
	Vector vTitle = new Vector();
	JScrollPane tableResult;
	DefaultTableModel model;
	JTable tb;
	JLabel selectlb;
	JComboBox select;
	JButton ok,cancel;
	CallHistory callHis;
	Login lg;
	JPanel tablepnl,selectpnl,buttonpnl;
	JLabel totallb;
	JTextField total;
	public CallHistoryStatisticForm(String str, CallHistory ch) {
		super(str);
		callHis = ch;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-U2H3L4F\\SQLEXPRESS:1433;databaseName=DICHVU1080;integratedSecurity=true");
			stmt = conn.createStatement();
			Container cont = this.getContentPane();
			tablepnl = new JPanel();
			selectpnl = new JPanel();
			buttonpnl = new JPanel();
			selectlb = new JLabel("Select year");
			select = new JComboBox();
			String sql = "select distinct year(ThoiGian) from LICHSUCUOCGOI";
			if (lg.accrts.equals("Customer")) sql += " where DienThoaiGoi = '" + lg.acct + "'";
			totallb = new JLabel("Total");
			total = new JTextField(15);
			total.setEditable(false);
			ResultSet rst = callHis.stmt.executeQuery(sql);
			ResultSetMetaData rstmeta = rst.getMetaData();
			int num_column = rstmeta.getColumnCount();
			while (rst.next()) select.addItem(rst.getString(1));
			rst.close();
			selectpnl.add(selectlb);
			selectpnl.add(select);
			selectpnl.add(totallb);
			selectpnl.add(total);
			ok = new JButton("OK");
			ok.addActionListener(this);
			cancel = new JButton("Cancel");
			cancel.addActionListener(this);
			buttonpnl.add(ok);
			buttonpnl.add(cancel);
			reload();
			model = new DefaultTableModel(vData,vTitle);
			tb = new JTable(model);
			tableResult = new JScrollPane(tb);
			tableResult.setVisible(true);
			selectpnl.setVisible(true);
			buttonpnl.setVisible(true);
			cont.add(tableResult,"North");
			cont.add(selectpnl);
			cont.add(buttonpnl,"South");
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
			total.setText("");
			String sql = "";
			if (lg.accrts.equals("Admin")) sql = "select year(ThoiGian) 'Nam', month(ThoiGian) 'Thang', sum(ThoiLuongThuc/60*DonGia) 'TongCuocPhi' ";
			else sql = "select year(ThoiGian) N'Năm', month(ThoiGian) N'Tháng', sum(ThoiLuongThuc/60*DonGia) N'Tổng cước phí' ";
			sql += "from LICHSUCUOCGOI L inner join "
					+ "TAIKHOAN_MACUOC T on L.DienThoaiGoi = T.DienThoai inner join "
					+ "GIACUOC G on T.MaCuoc = G.MaCuoc";
			if (lg.accrts.equals("Customer")) sql += " where L.DienThoaiGoi = '" + lg.acct + "'";
			sql += " group by year(ThoiGian), month(ThoiGian) order by year(ThoiGian), month(ThoiGian)";
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
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK")) {
			statistic();
		}
		else {
			reload();
			model.fireTableDataChanged();
		}
	}
	public void statistic() {
		try {
			vTitle.clear();
			vData.clear();
			String sql = "";
			if (lg.accrts.equals("Admin")) sql = "select year(ThoiGian) 'Nam', month(ThoiGian) 'Thang', sum(ThoiLuongThuc/60*DonGia) 'TongCuocPhi' ";
			else sql = "select year(ThoiGian) N'Năm', month(ThoiGian) N'Tháng', sum(ThoiLuongThuc/60*DonGia) N'Tổng cước phí' ";
			sql += "from LICHSUCUOCGOI L inner join "
					+ "TAIKHOAN_MACUOC T on L.DienThoaiGoi = T.DienThoai inner join "
					+ "GIACUOC G on T.MaCuoc = G.MaCuoc where year(ThoiGian) = " + select.getSelectedItem();
			if (lg.accrts.equals("Customer")) sql += " and L.DienThoaiGoi = '" + lg.acct + "'";
			sql += " group by year(ThoiGian), month(ThoiGian) order by year(ThoiGian), month(ThoiGian)";
			ResultSet rst = stmt.executeQuery(sql);
			ResultSetMetaData rstmeta = rst.getMetaData();
			int num_column = rstmeta.getColumnCount();
			for (int i=1; i<=num_column; i++) vTitle.add(rstmeta.getColumnLabel(i));
			while (rst.next()) {
				Vector row = new Vector(num_column);
				for (int i=1; i<=num_column; i++) row.add(rst.getString(i));
				vData.add(row);
			}
			model.fireTableDataChanged();
			sql = "select sum(ThoiLuongThuc/60*DonGia)"
					+ " from LICHSUCUOCGOI L inner join"
					+ "	TAIKHOAN_MACUOC T on L.DienThoaiGoi = T.DienThoai inner join"
					+ "	GIACUOC G on T.MaCuoc = G.MaCuoc"
					+ " where year(ThoiGian) = " + select.getSelectedItem().toString();
			if (lg.accrts.equals("Customer")) sql += " and DienThoaiGoi = '" + lg.acct + "'";
			rst = callHis.stmt.executeQuery(sql);
			while (rst.next()) {
				total.setText(rst.getString(1));
			}
			rst.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
