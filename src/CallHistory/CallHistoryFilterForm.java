package CallHistory;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import Login.Login;
class CallHistoryFilterForm extends JFrame implements ActionListener {
	JLabel attributelb, valuelb;
	JComboBox attribute, value;
	JButton ok, cancel;
	CallHistory callHis;
	String sql = "";
	Login lg;
	public CallHistoryFilterForm(String str, CallHistory ch) {
		super(str);
		callHis = ch;
		Container cont = this.getContentPane();
		attributelb = new JLabel("Filter by");
		attribute = new JComboBox();
		attribute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					value.removeAllItems();
					if (lg.accrts.equals("Admin")) sql = "select distinct " + attribute.getSelectedItem() + " from LICHSUCUOCGOI";
					else sql = "select distinct [" + attribute.getSelectedItem() + "] from LICHSUCUOCGOI_KHACHHANG where [Điện thoại gọi] = '" + lg.acct + "'";
					ResultSet rst = callHis.stmt.executeQuery(sql);
					while (rst.next()) value.addItem(rst.getString(1));
					rst.close();		
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		});
		cont.add(attributelb);
		cont.add(attribute);
		valuelb = new JLabel("Value");
		value = new JComboBox();
		cont.add(valuelb);
		cont.add(value);
		ok = new JButton("OK");
		ok.addActionListener(this);
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		cont.add(ok);
		cont.add(cancel);
		this.setLayout(new GridLayout(3,2));
		this.setSize(250,120);
		this.setLocation(1010,350);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		try  {
			if (lg.accrts.equals("Admin")) sql = "select * from LICHSUCUOCGOI";
			else sql = "select * from LICHSUCUOCGOI_KHACHHANG where [Điện thoại gọi] = '" + lg.acct + "'";
			ResultSet rst = callHis.stmt.executeQuery(sql);
			ResultSetMetaData rstmeta = rst.getMetaData();
			int num_column = rstmeta.getColumnCount();
			for (int i=1; i<=num_column; i++) {
				String label = rstmeta.getColumnLabel(i);
				if (!label.equals("Điện thoại gọi")) attribute.addItem(label);
			}
			rst.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK")) {
			filterAC();
		}
		else {
			callHis.reload();
			callHis.model.fireTableDataChanged();
			this.dispose();
		}
	}
	public void filterAC() {
		try {
			callHis.vTitle.clear();
			callHis.vData.clear();
			if (lg.accrts.equals("Admin")) sql = "select * from LICHSUCUOCGOI where " + attribute.getSelectedItem() + " = N'" + value.getSelectedItem() + "' order by ThoiGian desc";
			else sql = "select * from LICHSUCUOCGOI_KHACHHANG where [" + attribute.getSelectedItem() + "] = N'" + value.getSelectedItem() 
					+ "' and [Điện thoại gọi] = '" + lg.acct + "' order by [Thời gian] desc";
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
