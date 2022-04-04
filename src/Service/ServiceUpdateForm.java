package Service;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
class ServiceUpdateForm extends JFrame implements ActionListener {
	JLabel namelb;
	JTextField name;
	JLabel numberkeylb;
	JTextField numberkey;
	JLabel errorlb;
	JLabel errordetails;
	JButton ok;
	JButton cancel;
	Service serv;
	public ServiceUpdateForm(String str, Service srv, String na, String nk) {
		super(str);
		serv = srv;
		Container cont = this.getContentPane();
		cont.setLayout(new GridLayout(4,2));
		namelb = new JLabel("Service name");
		name = new JTextField(na);
		cont.add(namelb);
		cont.add(name);
		numberkeylb = new JLabel("Number key");
		numberkey = new JTextField(nk);
		if (str.equals("Edit form")) numberkey.setEditable(false);
		else numberkey.setEditable(true);
		cont.add(numberkeylb);
		cont.add(numberkey);
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
		this.setSize(250,150);
		this.setLocation(625,300);
		this.setVisible(true);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK")) insertSV();
		else this.dispose();
	}
	public void insertSV() {
		if (name.getText().equals("") || numberkey.getText().equals("")) {
			errorlb.setText("Error");
			errordetails.setText("empty value");
			errorlb.setForeground(Color.RED);
			errordetails.setForeground(Color.RED);
			errorlb.setVisible(true);
			errordetails.setVisible(true);
		} else {
			try {
				String nk = numberkey.getText();
				String na = name.getText();
				String sql = "";
				if (this.getTitle().equals("Insert form")) {
					sql = "insert into DICHVU values (N'" + na + "','" + nk + "')";
				} else {
					sql = "update DICHVU set TenDichVu = N'" + na + "' where BamPhim = '" + nk + "'";
				}
				serv.stmt.executeUpdate(sql);
				serv.reload();
				serv.model.fireTableDataChanged();
				this.dispose();		
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}