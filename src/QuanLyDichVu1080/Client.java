package QuanLyDichVu1080;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.JFrame;
public class Client {
	private InetAddress host;
	private int port;
	public static Socket client;
	public static ArrayList data;
	public static Vector vData, vTitle;
	public static ArrayList<Vector> table;
	public static String string;
	public Client(InetAddress host, int port) {
		this.host = host;
		this.port = port;
	}
	private void execute() throws IOException {
		client = new Socket(host,port);
		ReadClient read = new ReadClient(client);
		read.start();
		WriteClient write = new WriteClient(client);
		write.start();
	}
	public static void main(String[] args) throws IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new QuanLyDichVu1080("1080 Service Management",false,true,false,false,false,false,false,0);
		/* Biến Login.accrts trả giá trị null, vì nút OK của hộp thoại Log in chưa được nhấn (đây là bước đầu tiên để có thể thiết lập giá trị của biến đó)
		Giao diện của hộp thoại QLDV khi tiến hành đăng nhập là chung cho Customer và Admin */
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
		Client client = new Client(InetAddress.getLocalHost(), 15797);
		System.out.println(InetAddress.getLocalHost());
		client.execute();

	}
}
