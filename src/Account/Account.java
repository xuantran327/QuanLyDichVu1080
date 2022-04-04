package Account;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import Login.Login;
public class Account extends JFrame implements ActionListener, MouseListener {
	Connection conn;
	Statement stmt;
	ResultSet rst;
	Vector vData = new Vector();
	Vector vTitle = new Vector();
	JScrollPane tableResult;
	DefaultTableModel model;
	JTable tb;
	JLabel phonenumlb,passwordlb,acctrightslb,namelb,callChrgIDlb,errorlb,errordetails;
	JTextField phonenum,name,address,callChrgID;
	JPasswordField password;
	JComboBox acctrights;
	JButton edit,delete,insert,search,sort,filter,statistic;
	JButton ok,cancel;
	public JPanel Apnl,Cpnl;
	JPanel CInfopnl,btpnl1,btpnl2;
	int selectedrow = 0;
	int selectedcolumn = 0;
	Login lg;
	String pn,pw,ars,na,ccID;
	public Account(boolean acc) {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-U2H3L4F\\SQLEXPRESS:1433;databaseName=DICHVU1080;integratedSecurity=true");
			stmt = conn.createStatement(); 
			Apnl = new JPanel();
			Cpnl = new JPanel();
			btpnl1 = new JPanel();
			btpnl2 = new JPanel();
			edit = new JButton("Edit");
			edit.addActionListener(this);
			delete = new JButton("Delete");
			delete.addActionListener(this);
			insert = new JButton("Insert");
			insert.addActionListener(this);
			search = new JButton("Search");
			search.addActionListener(this);
			sort = new JButton("Sort");
			sort.addActionListener(this);
			filter = new JButton("Filter");
			filter.addActionListener(this);
			statistic = new JButton("Statistic");
			statistic.addActionListener(this);
			ok = new JButton("OK");
			ok.addActionListener(this);
			cancel = new JButton("Cancel");
			cancel.addActionListener(this);
			btpnl1.add(edit);
			// Giao diện Customer có các nút Edit, OK và Cancel
			if (!acc) {
				btpnl1.add(ok);
				btpnl1.add(cancel);
			}
			// Giao diện Admin gồm bảng với thông tin về các tài khoản và các nút Edit, Insert, Delete, Search, Sort, Filter và Statistic
			if (acc) {
				btpnl1.add(insert);
				btpnl1.add(delete);
				btpnl1.add(search);
				btpnl1.add(sort);
				btpnl1.add(filter);
				btpnl2.add(statistic);
				reload();
				model = new DefaultTableModel(vData,vTitle);
				tb = new JTable(model);
				tb.setRowSelectionAllowed(true);
				tb.setColumnSelectionAllowed(true);
				tb.addMouseListener(this);
				tableResult = new JScrollPane(tb);
				Apnl.add(tableResult,"North");
				Apnl.add(btpnl1,"South");
				Apnl.add(btpnl2,"South");
			// Giao diện Customer chỉ hiển thị thông tin của tài khoản vừa đăng nhập
			} else {
				CInfopnl = new JPanel(new GridLayout(6,2));
				phonenumlb = new JLabel("Phone number");
				phonenum = new JTextField("");
				CInfopnl.add(phonenumlb);
				CInfopnl.add(phonenum);
				passwordlb = new JLabel("Password");
				password = new JPasswordField("");
				CInfopnl.add(passwordlb);
				CInfopnl.add(password);
				acctrightslb = new JLabel("Access rights");
				acctrights = new JComboBox();
				acctrights.addItem("Admin");
				acctrights.addItem("Customer");
				CInfopnl.add(acctrightslb);
				CInfopnl.add(acctrights);
				namelb = new JLabel("Name");
				name = new JTextField("");
				CInfopnl.add(namelb);
				CInfopnl.add(name);
				callChrgIDlb = new JLabel("Call charge ID");
				callChrgID = new JTextField("");
				CInfopnl.add(callChrgIDlb);
				CInfopnl.add(callChrgID);
				// Thông tin về tài khoản bị khóa cho đến khi nhấn vào nút Edit
				phonenum.setEditable(false);
				password.setEditable(false);
				acctrights.setEnabled(false);
				name.setEditable(false);
				callChrgID.setEditable(false);
				errorlb = new JLabel("");
				errordetails = new JLabel("");
				errorlb.setVisible(false);
				errordetails.setVisible(false);
				ok.setEnabled(false);
				cancel.setEnabled(false);
				CInfopnl.add(errorlb);
				CInfopnl.add(errordetails);
				Cpnl.add(CInfopnl);
				Cpnl.add(btpnl1);
				reload();
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* Hiển thị toàn bộ thông tin có trong bảng (đối với giao diện Admin) hoặc thông tin của tài khoản vừa đăng nhập (đối với giao diện Customer) */
	public void reload() {
		try {
			if (lg.accrts.equals("Admin")) {
				vTitle.clear();
				vData.clear();
				ResultSet rst = stmt.executeQuery("select * from TAIKHOAN_MACUOC");
				ResultSetMetaData rstmeta = rst.getMetaData();
				int num_column = rstmeta.getColumnCount();
				for (int i=1; i<=num_column; i++) vTitle.add(rstmeta.getColumnLabel(i));
				while (rst.next()) {
					Vector row = new Vector(num_column);
					for (int i=1; i<=num_column; i++) row.add(rst.getString(i));
					vData.add(row);
				}
				rst.close();
			} else {
				ResultSet rst = stmt.executeQuery("select * from TAIKHOAN_MACUOC where DienThoai = '" + lg.acct + "'");
				while (rst.next()) {
					phonenum.setText(rst.getString(1));
					password.setText(rst.getString(2));
					acctrights.setSelectedItem(rst.getString(3));
					name.setText(rst.getString(4));
					callChrgID.setText(rst.getString(5));
					// Lưu thông tin tài khoản vào các biến phục vụ việc chỉnh sửa thông tin
					pn = phonenum.getText();
					pw = password.getText();
					ars = acctrights.getSelectedItem().toString();
					na = name.getText();
					ccID = callChrgID.getText();
				}
				rst.close();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Delete")) delete();
		if (e.getActionCommand().equals("Insert"))
			new AccountUpdateForm("Insert form",this,"","","","");
		if (e.getActionCommand().equals("Edit")) {
			if (lg.accrts.equals("Admin")) {
				Vector acct = (Vector) vData.elementAt(selectedrow);
				new AccountUpdateForm("Edit form",this,(String)acct.elementAt(0),(String)acct.elementAt(1),(String)acct.elementAt(2),(String)acct.elementAt(3));
			} else OnButton();
		}
		if (e.getActionCommand().equals("Search"))
			new AccountSearchForm("Search form",this);
		if (e.getActionCommand().equals("Sort"))
			new AccountSortForm("Sort form",this);
		if (e.getActionCommand().equals("Filter"))
			new AccountFilterForm("Filter form",this);
		if (e.getActionCommand().equals("Statistic"))
			new AccountStatisticForm("Statistical table",this);
		if (e.getActionCommand().equals("OK")) {
			if (password.getText().equals("") || name.getText().equals("")) {
				errorlb.setText("Error");
				errordetails.setText("empty value");
				errorlb.setForeground(Color.RED);
				errordetails.setForeground(Color.RED);
				errorlb.setVisible(true);
				errordetails.setVisible(true);
			} else {
				try {
					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
					conn = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-U2H3L4F\\SQLEXPRESS:1433;databaseName=DICHVU1080;integratedSecurity=true");
					stmt = conn.createStatement();
					String pn = phonenum.getText();
					String pw = password.getText();
					String ars = acctrights.getSelectedItem().toString();
					String na = name.getText();
					String sql = "update TAIKHOAN set MatKhau = '" + pw + "', LoaiTaiKhoan = '" + ars + "', HoTen = N'" + na + "' where DienThoai = '" + pn + "'";
					stmt.executeUpdate(sql);
					reload();
					OffButton();
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		}
		if (e.getActionCommand().equals("Cancel")) {
			password.setText(pw);
			name.setText(na);
			OffButton();
		}
	}
	/* Cho phép nhập thông tin sau khi nhấp chuột vào nút Edit */
	public void OnButton() {
		password.setEditable(true);
		name.setEditable(true);
		ok.setEnabled(true);
		cancel.setEnabled(true);
		edit.setEnabled(false);
	}
	/* Khóa thông tin sau khi nhấp chuột vào nút OK hoặc Cancel */
	public void OffButton() {
		errorlb.setVisible(false);
		errordetails.setVisible(false);
		password.setEditable(false);
		name.setEditable(false);
		ok.setEnabled(false);
		cancel.setEnabled(false);
		edit.setEnabled(true);
	}
	public void mouseClicked(MouseEvent e) {
		selectedrow = tb.getSelectedRow();
		selectedcolumn = tb.getSelectedColumn();
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	/* Xóa hàng được chọn */
	public void delete() {
		try {
			Vector acc = (Vector) vData.elementAt(selectedrow);
			String sql = "Delete from TAIKHOAN_MACUOC where DienThoai = '" + (String)acc.elementAt(0) + "'";
			stmt.executeUpdate(sql);
			vData.remove(selectedrow);
			model.fireTableDataChanged();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}	
}

