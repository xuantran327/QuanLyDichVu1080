package QuanLyDichVu1080;
import java.awt.EventQueue;
import java.net.*;
import java.util.ArrayList;
import java.util.Vector;
import java.io.*;
import javax.swing.JFrame;
public class Server {
	private int port;
	public static ArrayList<Socket> listSK;
	public static ArrayList data;
	public static Vector vData, vTitle;
	public static ArrayList<Vector> table;
	public Server(int port) {
		this.port = port;
	}
	private void execute() throws IOException {
		ServerSocket serSocket = new ServerSocket(port);
		WriteServer write = new WriteServer();
		write.start();
		while (true) {
			Socket socket = serSocket.accept();
			Server.listSK.add(socket);
			ReadServer read = new ReadServer(socket);
			read.start();
		}
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new QuanLyDichVu1080("1080 Service Management",false,true,false,false,false,false,false,1);
		/* Biến Login.accrts trả giá trị null, vì nút OK của hộp thoại Log in chưa được nhấn (đây là bước đầu tiên để có thể thiết lập giá trị của biến đó)
		Giao diện của hộp thoại QLDV khi tiến hành đăng nhập là chung cho Customer và Admin */
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
		Server.listSK = new ArrayList<>();
		Server server = new Server(15797);
		try {
			server.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}