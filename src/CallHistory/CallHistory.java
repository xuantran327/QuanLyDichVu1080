package CallHistory;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Login.Login;
public class CallHistory extends JFrame implements ActionListener, MouseListener {
	Connection conn;
	Statement stmt;
	ResultSet rst;
	Vector vData = new Vector();
	Vector vTitle = new Vector();
	JScrollPane tableResult;
	DefaultTableModel model;
	JTable tb;
	JButton edit,delete,insert,search,sort,filter,calculate,statistic;
	JButton ok,cancel;
	Login lg;
	public JPanel Apnl,Cpnl,btpnl1,btpnl2;
	int selectedrow = 0;
	int selectedcolumn = 0;
	public CallHistory(boolean ch) {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-U2H3L4F\\SQLEXPRESS:1433;databaseName=DICHVU1080;integratedSecurity=true");
			stmt = conn.createStatement();
			Apnl = new JPanel();
			Cpnl = new JPanel();
			btpnl1 = new JPanel();
			btpnl2 = new JPanel();
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
			filter = new JButton("Filter");
			filter.addActionListener(this);
			calculate = new JButton("Calculate");
			calculate.addActionListener(this);
			statistic = new JButton("Statistic");
			statistic.addActionListener(this);
			if (ch) {
				btpnl1.add(edit);
				btpnl1.add(delete);
				btpnl1.add(insert);
			}
			btpnl1.add(search);
			btpnl1.add(sort);
			btpnl1.add(filter);
			if (ch) {
				btpnl2.add(calculate);
				btpnl2.add(statistic);
			} else {
				btpnl1.add(calculate);
				btpnl1.add(statistic);
			}
			reload();
			model = new DefaultTableModel(vData,vTitle);
			tb = new JTable(model);
			tb.setRowSelectionAllowed(true);
			tb.setColumnSelectionAllowed(true);
			tb.addMouseListener(this);
			tableResult = new JScrollPane(tb);
			if (ch) {
				Apnl.add(tableResult,"North");
				Apnl.add(btpnl1,"South");
				Apnl.add(btpnl2,"South");
			} else {
				Cpnl.add(tableResult,"North");
				Cpnl.add(btpnl1,"South");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void reload() {
		try {
			vTitle.clear();
			vData.clear();
			String sql = "";
			if (lg.accrts.equals("Admin")) sql = "select * from LICHSUCUOCGOI order by ThoiGian desc";
			else sql = "select * from LICHSUCUOCGOI_KHACHHANG where [Điện thoại gọi] = '" + lg.acct + "' order by [Thời gian] desc";
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
			new CallHistoryUpdateForm("Insert form",this,"","","","",0,0);
		if (e.getActionCommand().equals("Edit")) {
			Vector ch = (Vector) vData.elementAt(selectedrow);
			new CallHistoryUpdateForm("Edit form",this,(String)ch.elementAt(0),(String)ch.elementAt(1),(String)ch.elementAt(2),(String)ch.elementAt(3),
					Float.parseFloat((String)ch.elementAt(4)),Float.parseFloat((String)ch.elementAt(5)));
		}
		if (e.getActionCommand().equals("Search")) 
			new CallHistorySearchForm("Search form",this);
		if (e.getActionCommand().equals("Sort"))
			new CallHistorySortForm("Sort form",this);
		if (e.getActionCommand().equals("Filter"))
			new CallHistoryFilterForm("Filter form",this);
		if (e.getActionCommand().equals("Calculate"))
			new CallHistoryCalculateForm("Cost-per-Call calculation",this);
		if (e.getActionCommand().equals("Statistic"))
			new CallHistoryStatisticForm("Statistical table",this);
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
			Vector ch = (Vector) vData.elementAt(selectedrow);
			String sql = "Delete from LICHSUCUOCGOI where ThoiGian = '" + (String)ch.elementAt(0) + "'";
			stmt.executeUpdate(sql);
			vData.remove(selectedrow);
			model.fireTableDataChanged();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
