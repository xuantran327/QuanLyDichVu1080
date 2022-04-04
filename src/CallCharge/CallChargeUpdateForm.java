package CallCharge;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
class CallChargeUpdateForm extends JFrame implements ActionListener {
	JLabel callChargeIDlb, callChargeNamelb, unitPricelb, errorlb, errordetails;
	JTextField callChargeID, callChargeName, unitPrice;
	JButton ok, cancel;
	CallCharge callChrg;
	public CallChargeUpdateForm(String str, CallCharge cc, String id, String na, float up) {
		super(str);
		callChrg = cc;
		Container cont = this.getContentPane();
		cont.setLayout(new GridLayout(5,2));
		callChargeIDlb = new JLabel("Call charge ID");
		callChargeID = new JTextField(id);
		cont.add(callChargeIDlb);
		cont.add(callChargeID);
		if (str.equals("Edit form")) callChargeID.setEditable(false);
		else callChargeID.setEditable(true);
		callChargeNamelb = new JLabel("Call charge name");
		callChargeName = new JTextField(na);
		cont.add(callChargeNamelb);
		cont.add(callChargeName);
		unitPricelb = new JLabel("Unit price (dong(s) per minute)");
		unitPrice = new JTextField(String.valueOf(up));
		cont.add(unitPricelb);
		cont.add(unitPrice);
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
		this.setSize(380,180);
		this.setLocation(560,300);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK")) insertCC();
		else this.dispose();
	}
	public void insertCC() {
		if (callChargeID.getText().equals("") || callChargeName.getText().equals("")) {
			errorlb.setText("Error");
			errordetails.setText("empty value");
			errorlb.setForeground(Color.RED);
			errordetails.setForeground(Color.RED);
			errorlb.setVisible(true);
			errordetails.setVisible(true);
		} else {
			try {
				String id = callChargeID.getText();
				String na = callChargeName.getText();
				String up = unitPrice.getText();
				String sql = "";
				if (this.getTitle().equals("Insert form")) {
					sql = "insert into GIACUOC values ('" + id + "',N'" + na + "'," + up + ")";
				} else {
					sql = "update GIACUOC set TenCuoc = N'" + na + "', DonGia = " + up + " where MaCuoc = '" + id + "'";
				}
				callChrg.stmt.executeUpdate(sql);
				callChrg.reload();
				callChrg.model.fireTableDataChanged();
				this.dispose();		
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
