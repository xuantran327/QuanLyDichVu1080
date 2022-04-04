package CallHistory;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import Login.Login;
class CallHistorySearchForm extends JFrame implements ActionListener  {
	JLabel searchlb;
	JTextField search;
	JButton ok,cancel;
	JLabel errorlb,errordetails;
	CallHistory callHis;
	Login lg;
	public CallHistorySearchForm(String str, CallHistory ch) {
		super(str);
		callHis = ch;
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
				searchCH();
			}
		}
		else {
			callHis.reload();
			callHis.model.fireTableDataChanged();
			this.dispose();
		}

	}
	public void searchCH() {
		try {
			callHis.vTitle.clear();
			callHis.vData.clear();
			String sql = "";
			if (lg.accrts.equals("Admin")) {
				sql = "select * from LICHSUCUOCGOI where";
				sql += " (ThoiGian like N'%" + search.getText() + "%' or DienThoaiGoi like N'" + search.getText() 
				+ "%' or BamPhim like N'%" + search.getText() + "%' or TrangThai like N'%" + search.getText() 
				+ "%' or convert(nvarchar,TongThoiLuong) like N'%" + search.getText() + "%' or convert(nvarchar,ThoiLuongThuc) like N'%" + search.getText() + "%') order by ThoiGian desc";
			} else {
				sql = "select * from LICHSUCUOCGOI_KHACHHANG where [Điện thoại gọi] = '" + lg.acct + "' and";
				sql += " ([Thời gian] like N'%" + search.getText() + "%' or [Điện thoại gọi] like N'" + search.getText() 
				+ "%' or [Bấm phím] like N'%" + search.getText() + "%' or [Trạng thái] like N'%" + search.getText() 
				+ "%' or convert(nvarchar,[Tổng thời lượng]) like N'%" + search.getText() + "%' or convert(nvarchar,[Thời lượng thực]) like N'%" + search.getText() + "%') order by [Thời gian] desc";
			}
			ResultSet rst = callHis.stmt.executeQuery(sql);
			ResultSetMetaData rstmeta = rst.getMetaData();
			int num_column = rstmeta.getColumnCount();
			for (int i=1; i<=num_column; i++) callHis.vTitle.add(rstmeta.getColumnLabel(i));
			while (rst.next()) {
				Vector row = new Vector(num_column);
				for (int i=1; i<=num_column; i++) row.add(rst.getString(i));
				callHis.vData.add(row);
			}
			callHis.model.fireTableDataChanged();
			rst.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
