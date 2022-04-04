package Account;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
class AccountUpdateForm extends JFrame implements ActionListener {
	JLabel phonenumlb,passwordlb,acctrightslb,namelb,errorlb,errordetails;
	JTextField phonenum,name;
	JPasswordField password;
	JComboBox acctrights;
	JButton ok,cancel;
	Account acct;
	public AccountUpdateForm(String str, Account acc, String pn, String pw, String ar, String na) {
		super(str);
		acct = acc;
		Container cont = this.getContentPane();
		cont.setLayout(new GridLayout(6,2));
		phonenumlb = new JLabel("Phone number");
		phonenum = new JTextField(pn);
		if (str.equals("Edit form")) phonenum.setEditable(false);
		else phonenum.setEditable(true);
		cont.add(phonenumlb);
		cont.add(phonenum);
		passwordlb = new JLabel("Password");
		password = new JPasswordField(pw);
		cont.add(passwordlb);
		cont.add(password);
		acctrightslb = new JLabel("Access rights");
		acctrights = new JComboBox();
		acctrights.addItem("Admin");
		acctrights.addItem("Customer");
		if (str.equals("Edit form")) acctrights.setSelectedItem(ar);
		cont.add(acctrightslb);
		cont.add(acctrights);
		namelb = new JLabel("Name");
		name = new JTextField(na);
		cont.add(namelb);
		cont.add(name);
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
		this.setSize(250,210);
		this.setLocation(625,300);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK")) insertAC();
		else this.dispose();
	}
	
	/* Thêm thông tin vào bảng hoặc sửa thông tin đã có sẵn trong bảng */
	public void insertAC() {
		if (phonenum.getText().equals("") || password.getText().equals("") || name.getText().equals("")) {
			errorlb.setText("Error");
			errordetails.setText("empty value");
			errorlb.setForeground(Color.RED);
			errordetails.setForeground(Color.RED);
			errorlb.setVisible(true);
			errordetails.setVisible(true);
		} else {
			try {
				String pn = phonenum.getText();
				String pw = password.getText();
				String ars = acctrights.getSelectedItem().toString();
				String na = name.getText();
				String sql = "";
				if (this.getTitle().equals("Insert form")) {
					sql = "insert into TAIKHOAN values ('" + pn + "','" + pw + "','" + ars + "',N'" + na + "')";
				} else {
					sql = "update TAIKHOAN set MatKhau = '" + pw + "', LoaiTaiKhoan = '" + ars + "', HoTen = N'" + na + "' where DienThoai = '" + pn + "'";
				}
				acct.stmt.executeUpdate(sql);
				acct.reload();
				acct.model.fireTableDataChanged();
				this.dispose();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
