package Service;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import Login.Login;
class ServiceSearchForm extends JFrame implements ActionListener {
	JLabel searchlb;
	JTextField search;
	JButton ok,cancel;
	JLabel errorlb,errordetails;
	Service serv;
	Login lg;
	public ServiceSearchForm(String str, Service srv) {
		super(str);
		serv = srv;
		Container cont = this.getContentPane();
		cont.setLayout(new GridLayout(3,2));
		searchlb = new JLabel("Search");
		search = new JTextField("");
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
				searchSV();
			}
		} else {
			serv.reload();
			serv.model.fireTableDataChanged();
			this.dispose();
		}
	}
	public void searchSV() {
		try {
			serv.vTitle.clear();
			serv.vData.clear();
			String sql = "";
			if (lg.accrts.equals("Admin")) {
				sql = "select * from DICHVU where ";
				sql += "TenDichVu like N'%" + search.getText() + "%' or BamPhim like N'%" + search.getText() + "%'";
			}
			else {
				sql = "select * from DICHVU_KHACHHANG where ";
				sql += "[Tên dịch vụ] like N'%" + search.getText() + "%' or [Bấm phím] like N'%" + search.getText() + "%'";
			}
			ResultSet rst = serv.stmt.executeQuery(sql);
			ResultSetMetaData rstmeta = rst.getMetaData();
			int num_column = rstmeta.getColumnCount();
			for (int i=1; i<=num_column; i++) serv.vTitle.add(rstmeta.getColumnLabel(i));
			while (rst.next()) {
				Vector row = new Vector(num_column);
				for (int i=1; i<=num_column; i++) row.add(rst.getString(i));
				serv.vData.add(row);
			}
			serv.model.fireTableDataChanged();
			rst.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}