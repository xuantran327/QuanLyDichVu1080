package CallCharge;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Login.Login;
public class CallCharge extends JFrame implements ActionListener, MouseListener {
	Connection conn;
	Statement stmt;
	ResultSet rst;
	Vector vData = new Vector();
	Vector vTitle = new Vector();
	JScrollPane tableResult;
	DefaultTableModel model;
	JTable tb = new JTable();
	JButton edit,delete,insert,search,sort;
	public JPanel Apnl,Cpnl,btpnl;
	int selectedrow = 0;
	int selectedcolumn = 0;
	Login lg;
	public CallCharge(boolean cc) {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-U2H3L4F\\SQLEXPRESS:1433;databaseName=DICHVU1080;integratedSecurity=true");
			stmt = conn.createStatement();
			Apnl = new JPanel();
			Cpnl = new JPanel();
			btpnl = new JPanel();
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
			if (cc) {
				btpnl.add(edit);
				btpnl.add(delete);
				btpnl.add(insert);
			}
			btpnl.add(search);
			btpnl.add(sort);
			reload();
			model = new DefaultTableModel(vData,vTitle);
			tb = new JTable(model);
			tb.setRowSelectionAllowed(true);
			tb.setColumnSelectionAllowed(true);
			tb.addMouseListener(this);
			tableResult = new JScrollPane(tb);
			if (cc) {
				Apnl.add(tableResult,"North");
				Apnl.add(btpnl,"South");
			} else {
				Cpnl.add(tableResult,"North");
				Cpnl.add(btpnl,"South");
			}
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
	public void reload() {
		try {
			vTitle.clear();
			vData.clear();
			String sql = "";
			if (lg.accrts.equals("Admin")) sql = "select * from GIACUOC";
			else sql = "select * from GIACUOC_KHACHHANG";
			ResultSet rst = stmt.executeQuery(sql);
			ResultSetMetaData rstmeta = rst.getMetaData();
			int num_column = rstmeta.getColumnCount();
			for (int i=1; i<=num_column; i++) vTitle.add(rstmeta.getColumnLabel(i));
			while (rst.next()) {
				Vector row = new Vector(num_column);
				for (int i=1; i<=num_column; i++) row.add(rst.getString(i));
				vData.add(row);
			}
			rst.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Delete")) delete();
		if (e.getActionCommand().equals("Insert")) 
			new CallChargeUpdateForm("Insert form",this,"","",0);
		if (e.getActionCommand().equals("Edit")) {
			Vector callChrg = (Vector) vData.elementAt(selectedrow);
			new CallChargeUpdateForm("Edit form",this,(String)callChrg.elementAt(0),(String)callChrg.elementAt(1),Float.parseFloat((String)callChrg.elementAt(2)));
		}
		if (e.getActionCommand().equals("Search")) 
			new CallChargeSearchForm("Search form",this);
		if (e.getActionCommand().equals("Sort"))
			new CallChargeSortForm("Sort form",this);
	}
	public void mouseClicked(MouseEvent e) {
		selectedrow = tb.getSelectedRow();
		selectedcolumn = tb.getSelectedColumn();
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void delete() {
		try {
			Vector callChrg = (Vector) vData.elementAt(selectedrow);
			String sql = "Delete from GIACUOC where MaCuoc = '" + (String)callChrg.elementAt(0) + "'";
			stmt.executeUpdate(sql);
			vData.remove(selectedrow);
			model.fireTableDataChanged();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
