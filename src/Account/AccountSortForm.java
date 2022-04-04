package Account;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
class AccountSortForm extends JFrame implements ActionListener, ItemListener {
	JPanel choicepnl, buttonpnl;
	JLabel errordetails;
	JRadioButton ascRdo, descRdo;
	ButtonGroup sortGr;
	JButton ok, cancel;
	Account acct;
	public AccountSortForm(String str, Account acc) {
		super(str);
		acct = acc;
		Container cont = this.getContentPane();
		choicepnl = new JPanel(new GridLayout(1,2));
		ascRdo = new JRadioButton("Asc");
		ascRdo.setHorizontalAlignment(JRadioButton.CENTER);
		ascRdo.addItemListener(this);
		descRdo = new JRadioButton("Desc");
		descRdo.setHorizontalAlignment(JRadioButton.CENTER);
		descRdo.addItemListener(this);
		sortGr = new ButtonGroup();
		sortGr.add(ascRdo);
		sortGr.add(descRdo);
		choicepnl.add(ascRdo);
		choicepnl.add(descRdo);
		cont.add(choicepnl,"North");
		errordetails = new JLabel("");
		errordetails.setHorizontalAlignment(JLabel.CENTER);
		errordetails.setVisible(false);
		cont.add(errordetails);
		buttonpnl = new JPanel();
		ok = new JButton("OK");
		ok.addActionListener(this);
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		buttonpnl.add(ok);
		buttonpnl.add(cancel);
		buttonpnl.setVisible(true);
		cont.add(buttonpnl,"South");
		this.setSize(330,120);
		this.setLocation(1010,350);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK")) {
			sortAC();
		}
		else {
			acct.reload();
			acct.model.fireTableDataChanged();
			this.dispose();
		}
	}
	public void itemStateChanged(ItemEvent e) {}
	public void sortAC() {
		try {
			String sql = "";
			if (!ascRdo.isSelected() && !descRdo.isSelected()) {
				errordetails.setText("Error: no sort selected");
				errordetails.setForeground(Color.RED);
				errordetails.setVisible(true);
			} else {
				sql = "select * from TAIKHOAN_MACUOC order by " + (String)acct.vTitle.elementAt(acct.selectedcolumn);
				if (descRdo.isSelected()) sql += " desc";
				acct.vTitle.clear();
				acct.vData.clear();
				errordetails.setVisible(false);
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
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	} 
}
