package Login;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import QuanLyDichVu1080.QuanLyDichVu1080;
import QuanLyDichVu1080.ReadClient;
import QuanLyDichVu1080.WriteClient;
import QuanLyDichVu1080.Client;
public class Login extends JFrame implements ActionListener {
	Container cont;
	JLabel phoneNumberlb;
	public static JTextField phoneNumber;
	JLabel passwordlb;
	public static JPasswordField password;
	JLabel accesslb;
	JComboBox access;
	JLabel namelb;
	public static JTextField name;
	JLabel errorlb;
	JLabel errordetails;
	JButton btn1, btn2;
	QuanLyDichVu1080 qldv;
	Connection conn;
	Statement stmt;
	ResultSet rst;
	String sql;
	public static String accrts;
	public static String acct;
	int num;
	public Login(String str, QuanLyDichVu1080 qldv, String bt1, String bt2, int num) {
		super(str);
		this.qldv = qldv;
		this.num = num;
		//this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		cont = this.getContentPane();
		if (str.equals("Log in")) cont.setLayout(new GridLayout(5,2));
		else cont.setLayout(new GridLayout(6,2));
		phoneNumberlb = new JLabel("Phone number");
		phoneNumber = new JTextField("");
		cont.add(phoneNumberlb);
		cont.add(phoneNumber);
		passwordlb = new JLabel("Password");
		password = new JPasswordField("");
		cont.add(passwordlb);
		cont.add(password);
		accesslb = new JLabel("Access rights");
		access = new JComboBox();
		access.addItem("Admin");
		access.addItem("Customer");
		if (str.equals("Sign up")) {
			access.setSelectedItem("Customer");
			access.setEnabled(false);
		}
		cont.add(accesslb);
		cont.add(access);
		errorlb = new JLabel("");
		errordetails = new JLabel("");
		if (str.equals("Sign up")) {
			namelb = new JLabel("Name");
			name = new JTextField("");
			cont.add(namelb);
			cont.add(name);
		}
		errorlb.setVisible(false);
		errordetails.setVisible(false);
		cont.add(errorlb);
		cont.add(errordetails);
		btn1 = new JButton(bt1);
		btn2 = new JButton(bt2);
		cont.add(btn1);
		cont.add(btn2);
		btn1.addActionListener(this);
		btn2.addActionListener(this);
		if (str.equals("Log in")) this.setSize(230,180);
		else this.setSize(230,210);
		this.setLocation(635,300);
		this.setVisible(true);
		this.setResizable(false);
		if (str.equals("Sign up")) this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK")) {
			if (this.getTitle().equals("Log in")) {
				if (phoneNumber.getText().equals("") || password.getText().equals("")) {
					errorlb.setText("Error");
					errordetails.setText("empty value");
					errorlb.setForeground(Color.RED);
					errordetails.setForeground(Color.RED);
					errorlb.setVisible(true);
					errordetails.setVisible(true);
				}
				else if (search(phoneNumber.getText(),password.getText(),access.getSelectedItem().toString())) {
					acct = phoneNumber.getText();
					accrts = access.getSelectedItem().toString();
					/* Nếu biến acct và accrts không phải là biến static thì khi đăng nhập thành công, hộp thoại Log in sẽ tắt (chưa kết thúc toàn bộ chương trình QLDV)
					Máy tính sẽ giải phóng 2 biến trên, nên không có căn cứ để:
					- Hiển thị giao diện hộp thoại QLDV tương với chế độ người dùng (biến accrts)
					- Hiển thị thông tin bên trong hộp thoại QLDV tương ứng với người dùng (biến acct) */
					this.dispose();
					qldv.dispose();
					qldv = new QuanLyDichVu1080("1080 Service Management",true,false,false,false,false,false,false,num);
					qldv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				} else {
					errorlb.setText("Error");
					errordetails.setText("no valid");
					errorlb.setForeground(Color.RED);
					errordetails.setForeground(Color.RED);
					errorlb.setVisible(true);
					errordetails.setVisible(true);
				}
			} else {
				/* if (phoneNumber.getText().equals("") || password.getText().equals("") || name.getText().equals("") ) {
					errorlb.setText("Error");
					errordetails.setText("empty value");
					errorlb.setForeground(Color.RED);
					errordetails.setForeground(Color.RED);
					errorlb.setVisible(true);
					errordetails.setVisible(true);
				}
				else if (search(phoneNumber.getText(),password.getText(),access.getSelectedItem().toString())) {
					errorlb.setText("Error");
					errordetails.setText("account exist");
					errorlb.setForeground(Color.RED);
					errordetails.setForeground(Color.RED);
					errorlb.setVisible(true);
					errordetails.setVisible(true);
				} else {
					insertPN();		//insert phone number
					this.dispose();
				} */
				/* WriteClient write = new WriteClient(Client.client,this,e);
				write.start();
				ReadClient read = new ReadClient(Client.client,this,e);
				read.start(); */
				WriteClient.fr = this;
				WriteClient.ae = e;
				if (Client.string.equals("Sign-up successful!")) {
					this.dispose();
				} else {
					errorlb.setText("Error");
					errordetails.setText(Client.string);
					errorlb.setForeground(Color.RED);
					errordetails.setForeground(Color.RED);
					errorlb.setVisible(true);
					errordetails.setVisible(true);
				}
			}
		} else if (e.getActionCommand().equals("Sign up"))
			new Login("Sign up",qldv,"OK","Cancel",num);
		else this.dispose();
	}
	public boolean search(String str1, String str2, String str3) {
		boolean result = false;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-U2H3L4F\\SQLEXPRESS:1433;databaseName=DICHVU1080;integratedSecurity=true");
			stmt = conn.createStatement();		//Định nghĩa phương thức và thuộc tính, cho phép thực thi câu lệnh SQL tĩnh
			String sql = "SELECT DienThoai, MatKhau, LoaiTaiKhoan FROM TAIKHOAN WHERE DienThoai = '" + str1 + "'";
			ResultSet rst = stmt.executeQuery(sql);
			if (this.getTitle().equals("Log in")) {
				while (rst.next()) {
					if (str1.equals(rst.getString(1)) && str2.equals(rst.getString(2)) && str3.equals(rst.getString(3)))
						result = true;
				}
				rst.close();
			}
			else {
				while (rst.next()) {
					if (str1.equals(rst.getString(1))) result = true;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return result;
	}
	public void insertPN() {
		try {
			String pn = phoneNumber.getText();
			String pw = password.getText();
			String ar = access.getSelectedItem().toString();
			String na = name.getText();
			String sql = "insert into TAIKHOAN values ('" + pn + "','" + pw + "','" + ar + "',N'" + na + "')";
			stmt.executeUpdate(sql);
			this.dispose();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
