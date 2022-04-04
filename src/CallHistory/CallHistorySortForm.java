package CallHistory;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import Login.Login;
class CallHistorySortForm extends JFrame implements ActionListener, ItemListener {
	JPanel selectpnl, buttonpnl;
	JLabel errordetails;
	JRadioButton ascRdo, descRdo;
	ButtonGroup sortGr;
	JButton ok, cancel;
	CallHistory callHis;
	Login lg;
	public CallHistorySortForm(String str, CallHistory ch) {
		super(str);
		callHis = ch;
		Container cont = this.getContentPane();
		selectpnl = new JPanel(new GridLayout(1,2));
		ascRdo = new JRadioButton("Asc");
		ascRdo.setHorizontalAlignment(JRadioButton.CENTER);
		ascRdo.addItemListener(this);
		descRdo = new JRadioButton("Desc");
		descRdo.setHorizontalAlignment(JRadioButton.CENTER);
		descRdo.addItemListener(this);
		sortGr = new ButtonGroup();
		sortGr.add(ascRdo);
		sortGr.add(descRdo);
		selectpnl.add(ascRdo);
		selectpnl.add(descRdo);
		cont.add(selectpnl,"North");
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
			sortCH();
		}
		else {
			callHis.reload();
			callHis.model.fireTableDataChanged();
			this.dispose();
		}
	}
	public void itemStateChanged(ItemEvent e) {}
	public void sortCH() {
		try {
			String sql = "";
			if (!ascRdo.isSelected() && !descRdo.isSelected()) {
				errordetails.setText("Error: no sort selected");
				errordetails.setForeground(Color.RED);
				errordetails.setVisible(true);
			} else {
				if (lg.accrts.equals("Admin")) sql = "select * from LICHSUCUOCGOI";
				else sql = "select * from LICHSUCUOCGOI_KHACHHANG where [Điện thoại gọi] = '" + lg.acct + "'";
				sql += " order by [" + (String)callHis.vTitle.elementAt(callHis.selectedcolumn) + "]";
				if (descRdo.isSelected()) sql += " desc";
				callHis.vTitle.clear();
				callHis.vData.clear();
				errordetails.setVisible(false);
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
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	} 
}
