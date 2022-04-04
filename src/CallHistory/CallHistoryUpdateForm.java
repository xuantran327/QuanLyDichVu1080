package CallHistory;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import CallHistory.CallHistory;
class CallHistoryUpdateForm extends JFrame implements ActionListener {	
	JLabel datetimelb,phonenumlb,numkeylb,statuslb,totalDurationlb,realDurationlb,errorlb,errordetails;
	JTextField datetime,phonenum,numkey,totalDuration,realDuration;
	JComboBox status;
	JButton ok,cancel;
	CallHistory callHis;
	public CallHistoryUpdateForm(String str, CallHistory ch, String dt, String pn, String nk, String stt, float td, float rd) {
		super(str);
		callHis = ch;
		Container cont = this.getContentPane();
		cont.setLayout(new GridLayout(8,2));
		datetimelb = new JLabel("Date time");
		datetime = new JTextField(dt);
		if (str.equals("Edit form")) datetime.setEditable(false);
		else datetime.setEditable(true);
		cont.add(datetimelb);
		cont.add(datetime);
		phonenumlb = new JLabel("Phone number");
		phonenum = new JTextField(pn);
		cont.add(phonenumlb);
		cont.add(phonenum);
		numkeylb = new JLabel("Number key");
		numkey = new JTextField(nk);
		cont.add(numkeylb);
		cont.add(numkey);
		statuslb = new JLabel("Status");
		status = new JComboBox();
		status.addItem("Busy");;
		status.addItem("Answered");
		status.addItem("NoAnswered");
		if (str.equals("Edit form")) status.setSelectedItem(stt);
		cont.add(statuslb);
		cont.add(status);
		totalDurationlb = new JLabel("Total duration (second(s))");
		totalDuration = new JTextField(String.valueOf(td));
		cont.add(totalDurationlb);
		cont.add(totalDuration);
		realDurationlb = new JLabel("Real duration (second(s))");
		realDuration = new JTextField(String.valueOf(rd));
		cont.add(realDurationlb);
		cont.add(realDuration);
		errorlb = new JLabel("");
		errordetails = new JLabel("");
		errorlb.setVisible(false);
		errordetails.setVisible(false);
		cont.add(errorlb);
		cont.add(errordetails);
		ok = new JButton("OK");
		cancel = new JButton("Cancel");
		cont.add(ok);
		cont.add(cancel);
		ok.addActionListener(this);
		cancel.addActionListener(this);
		this.setSize(320,300);
		this.setLocation(590,265);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK")) insertCH();
		else this.dispose();
	}
	public void insertCH() {
		if (datetime.getText().equals("") || phonenum.getText().equals("") || numkey.getText().equals("")) {
			errorlb.setText("Error");
			errordetails.setText("empty value");
			errorlb.setForeground(Color.RED);
			errordetails.setForeground(Color.RED);
			errorlb.setVisible(true);
			errordetails.setVisible(true);
		} else {
			try {
				String dt = datetime.getText();
				String pn = phonenum.getText();
				String nk = numkey.getText();
				String stt = status.getSelectedItem().toString();
				String td = totalDuration.getText();
				String rd = realDuration.getText();
				String sql = "";
				if (this.getTitle().equals("Insert form")) {
					sql = "insert into LICHSUCUOCGOI values ('" + dt + "','" + pn + "','" + nk + "','" + stt + "'," + td + "," + rd + ")";
				} else {
					sql = "update LICHSUCUOCGOI set DienThoaiGoi = '" + pn + "', BamPhim = '" + nk + "', TrangThai = '" + stt 
							+ "', TongThoiLuong = " + td + ", ThoiLuongThuc = " + rd + " where ThoiGian = '" + dt + "'";
				}
				callHis.stmt.executeUpdate(sql);
				callHis.reload();
				callHis.model.fireTableDataChanged();
				this.dispose();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
