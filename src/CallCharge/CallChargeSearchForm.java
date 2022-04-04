package CallCharge;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import Login.Login;
class CallChargeSearchForm extends JFrame implements ActionListener {
	JLabel searchlb;
	JTextField search;
	JButton ok,cancel;
	JLabel errorlb,errordetails;
	CallCharge callChrg;
	Login lg;
	public CallChargeSearchForm(String str, CallCharge cc) {
		super(str);
		callChrg = cc;
		Container cont = this.getContentPane();
		cont.setLayout(new GridLayout(3,2));
		searchlb = new JLabel("Search");
		search = new JTextField(100);
		cont.add(searchlb);
		cont.add(search);
		errorlb = new JLabel("");
		errordetails = new JLabel("");
		errorlb.setVisible(false);
		errordetails.setVisible(false);
		cont.add(errorlb);
		cont.add(errordetails);
		ok = new JButton("OK");
		ok.addActionListener(this);
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		cont.add(ok);
		cont.add(cancel);
		this.setSize(250,120);
		this.setLocation(1010,350);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK")) {
			if (search.getText().equals("")) {
				errorlb.setText("Error");
				errordetails.setText("empty value");
				errorlb.setForeground(Color.RED);
				errordetails.setForeground(Color.RED);
				errorlb.setVisible(true);
				errordetails.setVisible(true);
			} else {
				errorlb.setVisible(false);
				errordetails.setVisible(false);
				searchCC();
			}
		}
		else {
			callChrg.reload();
			callChrg.model.fireTableDataChanged();
			this.dispose();
		}
	}
	public void searchCC() {
		try {
			callChrg.vTitle.clear();
			callChrg.vData.clear();
			String sql = "";
			if (lg.accrts.equals("Admin")) {
				sql = "select * from GIACUOC where ";
				sql += "MaCuoc like N'%" + search.getText() + "%' or TenCuoc like N'%" + search.getText() + "%' or DonGia like N'%" + search.getText() + "%'";
			}
			else {
				sql = "select * from GIACUOC_KHACHHANG where ";
				sql += "[Mã cước] like N'%" + search.getText() + "%' or [Tên cước] like N'%" + search.getText() + "%' or [Đơn giá] like N'%" + search.getText() + "%'"; 
			}
			ResultSet rst = callChrg.stmt.executeQuery(sql);
			ResultSetMetaData rstmeta = rst.getMetaData();
			int num_column = rstmeta.getColumnCount();
			for (int i=1; i<=num_column; i++) callChrg.vTitle.add(rstmeta.getColumnLabel(i));
			while (rst.next()) {
				Vector row = new Vector(num_column);
				for (int i=1; i<=num_column; i++) row.add(rst.getString(i));
				callChrg.vData.add(row);
			}
			callChrg.model.fireTableDataChanged();
			rst.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}