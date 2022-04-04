package Account;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
class AccountSearchForm extends JFrame implements ActionListener {
	JLabel searchlb;
	JTextField search;
	JButton ok,cancel;
	JLabel errorlb,errordetails;
	Account acct;
	public AccountSearchForm(String str, Account acc) {
		super(str);
		acct = acc;
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
				searchAC();
			}
		}
		else {
			acct.reload();
			acct.model.fireTableDataChanged();
			this.dispose();
		}
	}
	public void searchAC() {
		try {
			acct.vTitle.clear();
			acct.vData.clear();
			String sql = "select * from TAIKHOAN_MACUOC where DienThoai like N'%" + search.getText() + "%' or MatKhau like N'%" + search.getText() 
					+ "%' or LoaiTaiKhoan like N'%" + search.getText() + "%' or HoTen like N'%" + search.getText() + "%' or MaCuoc like N'%" + search.getText() + "%'";
			ResultSet rst = acct.stmt.executeQuery(sql);
			ResultSetMetaData rstmeta = rst.getMetaData();
			int num_column = rstmeta.getColumnCount();
			for (int i=1; i<=num_column; i++) acct.vTitle.add(rstmeta.getColumnLabel(i));
			while (rst.next()) {
				Vector row = new Vector(num_column);
				for (int i=1; i<=num_column; i++) row.add(rst.getString(i));
				acct.vData.add(row);
			}
			acct.model.fireTableDataChanged();
			rst.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}