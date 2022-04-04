package QuanLyDichVu1080;
import java.sql.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import Login.Login;
import Account.Account;
import Service.Service;
import CallHistory.CallHistory;
import CallCharge.CallCharge;
import java.awt.Image.*;
public class QuanLyDichVu1080 extends JFrame implements ActionListener {
	Container cont = this.getContentPane();
	MenuBar bar;
	Menu view,other;
	MenuItem account,service,callHistory,callCharge,logout;
	JPanel pnl;
	JLabel imagelb;
	Login lg;
	Service Serv;
	Account Acct;
	CallHistory callHis;
	CallCharge callChrg;
	QuanLyDichVu1080 qldv;
	int num;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new QuanLyDichVu1080("1080 Service Management",false,true,false,false,false,false,false,0);
		/* Biến Login.accrts trả giá trị null, vì nút OK của hộp thoại Log in chưa được nhấn (đây là bước đầu tiên để có thể thiết lập giá trị của biến đó)
		Giao diện của hộp thoại QLDV khi tiến hành đăng nhập là chung cho Customer và Admin */
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}
	public QuanLyDichVu1080(String str, boolean b, boolean l, boolean srv, boolean acc, boolean ch, boolean cc, boolean ars, int num) {
		super(str);
		this.num = num;
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			// Tạo giao diện trống chỉ có MenuBar sau khi đăng nhập thành công 
			pnl = new JPanel();
			bar = new MenuBar();
			if (b == true) {
				this.setMenuBar(bar);
			} else {
			// Tạo giao diện trống chỉ có nút Log in khi chưa đăng nhập
				cont.setLayout(new FlowLayout());
				JButton login = new JButton("Log in");
				login.addActionListener(this);
				cont.add(login);
			}
			// Tạo Menu
			view = new Menu("View");
			account = new MenuItem("Account");
			account.addActionListener(this);
			service = new MenuItem("Service");
			service.addActionListener(this);
			callHistory = new MenuItem("Call history");
			callHistory.addActionListener(this);
			callCharge = new MenuItem("Call charge");
			callCharge.addActionListener(this);
			other = new Menu("Other");
			logout = new MenuItem("Log out");
			logout.addActionListener(this);
			other.add(logout);
			view.add(service);
			view.add(account);
			view.add(callHistory);
			view.add(callCharge);
			bar.add(view);
			bar.add(other);
			// Tạo giao diện có Panel tương ứng khi nhấn vào MenuItem tương ứng (có phân loại giao diện theo quyền truy cập)
			Serv = new Service(ars);
			if (srv) {
				if (ars) {
					Serv.Apnl.setVisible(true);
					cont.add(Serv.Apnl);
				} else {
					Serv.Cpnl.setVisible(true);
					cont.add(Serv.Cpnl);			
				}
			}
			Acct = new Account(ars);
			if (acc) {
				if (ars) {
					Acct.Apnl.setVisible(true);
					cont.add(Acct.Apnl);
				} else {
					Acct.Cpnl.setVisible(true);
					cont.add(Acct.Cpnl);			
				}
			}
			callHis = new CallHistory(ars);
			if (ch) {
				if (ars) {
					callHis.Apnl.setVisible(true);
					cont.add(callHis.Apnl);
				} else {
					callHis.Cpnl.setVisible(true);
					cont.add(callHis.Cpnl);			
				}
			}
			callChrg = new CallCharge(ars);
			if (cc) {
				if (ars) {
					callChrg.Apnl.setVisible(true);
					cont.add(callChrg.Apnl);
				} else {
					callChrg.Cpnl.setVisible(true);
					cont.add(callChrg.Cpnl);			
				}
			}
			if (str.equals("1080 Service Management - Account - Customer")) {
				this.setSize(380,260);
				this.setLocation(590,250);
			} else if (str.endsWith("Account - Admin") || str.endsWith("Call history - Admin")) {
				this.setSize(500,580);
				this.setLocation(500,150);
			} else {
				this.setSize(500,535);
				this.setLocation(500,150);
			}
			this.setResizable(false);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			// Thêm hình ảnh khi giao diện trống chỉ có MenuBar
			if (b && !l && !srv && !acc && !ch && !cc && !ars) {
				try {
					BufferedImage image = ImageIO.read(new File("11082272_1549057022011098_2819549587323593238_o.jpg"));
		            int x = this.getSize().width;
		            int y = this.getSize().height;
		            int ix = image.getWidth();
		            int iy = image.getHeight();
		            int dx = 0;
		            int dy = 0;
		            if(x/y > ix/iy){
		                dy = y;
		                dx = dy*ix/iy;
		            } else {
		                dx = x;
		                dy = dx*iy/ix;
		            }
		            ImageIcon icon = new ImageIcon(image.getScaledInstance(dx, dy, BufferedImage.SCALE_SMOOTH));
		            imagelb = new JLabel("",icon,JLabel.CENTER);
		            imagelb.setVisible(true);
		            cont.add(imagelb);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}		
			}
			this.setVisible(true);
			// Mặc định khi mở ứng dụng sẽ hiển thị hộp thoại Log in
			if (l == true) lg = new Login("Log in",this,"OK","Sign up",num);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Service")) {
			this.dispose();
			if (lg.accrts.equals("Admin")) {
				qldv = new QuanLyDichVu1080("1080 Service Management - Service - Admin",true,false,true,false,false,false,true,num);
				qldv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			} else {
				qldv = new QuanLyDichVu1080("1080 Service Management - Service - Customer",true,false,true,false,false,false,false,num);
				qldv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		}
		if (e.getActionCommand().equals("Account")) {
			this.dispose();
			if (lg.accrts.equals("Admin")) {
				qldv = new QuanLyDichVu1080("1080 Service Management - Account - Admin",true,false,false,true,false,false,true,num);
				qldv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			} else {
				qldv = new QuanLyDichVu1080("1080 Service Management - Account - Customer",true,false,false,true,false,false,false,num);
				qldv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		}
		if (e.getActionCommand().equals("Call history")) {
			this.dispose();
			if (lg.accrts.equals("Admin")) {
				qldv = new QuanLyDichVu1080("1080 Service Management - Call history - Admin",true,false,false,false,true,false,true,num);
				qldv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			} else {
				qldv = new QuanLyDichVu1080("1080 Service Management - Call history - Customer",true,false,false,false,true,false,false,num);
				qldv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		}
		if (e.getActionCommand().equals("Call charge")) {
			this.dispose();
			if (lg.accrts.equals("Admin")) {
				qldv = new QuanLyDichVu1080("1080 Service Management - Call charge - Admin",true,false,false,false,false,true,true,num);
				qldv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			} else {
				qldv = new QuanLyDichVu1080("1080 Service Management - Call charge - Customer",true,false,false,false,false,true,false,num);
				qldv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		}
		if (e.getActionCommand().equals("Log out")) {
			this.dispose();
			qldv = new QuanLyDichVu1080("1080 Service Management",false,true,false,false,false,false,false,num);
			qldv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		if (e.getActionCommand().equals("Log in"))
			new Login("Log in",this,"OK","Sign up",num);
	}
}