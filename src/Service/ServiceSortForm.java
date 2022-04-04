package Service;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import Login.Login;
class ServiceSortForm extends JFrame implements ActionListener, ItemListener {
	JPanel selectpnl, buttonpnl;
	JLabel errordetails;
	JRadioButton ascRdo, descRdo;
	ButtonGroup sortGr;
	JButton ok, cancel;
	Service serv;
	Login lg;
	public ServiceSortForm(String str, Service srv) {
		super(str);
		serv = srv;
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
			sortSV();
		}
		else {
			serv.reload();
			serv.model.fireTableDataChanged();
			this.dispose();
		}
	}
	public void itemStateChanged(ItemEvent e) {}
	public void sortSV() {
		try {
			String sql = "";
			if (!ascRdo.isSelected() && !descRdo.isSelected()) {
				errordetails.setText("Error: no sort selected");
				errordetails.setForeground(Color.RED);
				errordetails.setVisible(true);
			} else {
				if (lg.accrts.equals("Admin")) sql = "select * from DICHVU";
				else sql = "select * from DICHVU_KHACHHANG";
				sql += " order by [" + (String)serv.vTitle.elementAt(serv.selectedcolumn) + "]";
				if (descRdo.isSelected()) sql += " desc";
				serv.vTitle.clear();
				serv.vData.clear();
				errordetails.setVisible(false);
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
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	} 
}