package Account;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
class AccountFilterForm extends JFrame implements ActionListener {
	JLabel attributelb, valuelb;
	JComboBox attribute, value;
	JButton ok, cancel;
	Account acct;
	String sql = "";
	public AccountFilterForm(String str, Account acc) {
		super(str);
		acct = acc;
		Container cont = this.getContentPane();
		attributelb = new JLabel("Filter by");
		attribute = new JComboBox();
		attribute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					value.removeAllItems();
					sql = "select distinct " + attribute.getSelectedItem() + " from TAIKHOAN_MACUOC";
					ResultSet rst = acct.stmt.executeQuery(sql);
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
			sql = "select * from TAIKHOAN_MACUOC";
			ResultSet rst = acct.stmt.executeQuery(sql);
			ResultSetMetaData rstmeta = rst.getMetaData();
			int num_column = rstmeta.getColumnCount();
			for (int i=1; i<=num_column; i++) {
				String label = rstmeta.getColumnLabel(i);
				attribute.addItem(label);
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
			acct.reload();
			acct.model.fireTableDataChanged();
			this.dispose();
		}
	}
	/* Lọc thông tin với thuộc tính và giá trị đã chọn trong ComboBox */
	public void filterAC() {
		try {
			acct.vTitle.clear();
			acct.vData.clear();
			sql = "select * from TAIKHOAN_MACUOC where " + attribute.getSelectedItem() + " = N'" + value.getSelectedItem() + "'";
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
