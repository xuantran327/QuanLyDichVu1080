package QuanLyDichVu1080;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
public class ReadServer extends Thread {
	public static Socket serSocket;
	public static ArrayList data;
	public ReadServer(Socket serSocket) {
		this.serSocket = serSocket;
	}
	public void run() {
		try {
			ObjectInputStream ois = new ObjectInputStream(serSocket.getInputStream());
			while (true) {
				/* String sms = dis.readUTF();
				if(sms.contains("exit")) {
					Server.listSK.remove(socket);
					System.out.println("Đã ngắt kết nối với " + socket);
					dis.close();
					socket.close();
					continue; //Ngắt kết nối rồi
				}
				for (Socket item : Server.listSK) {
					if(item.getPort() != socket.getPort()) {
						DataOutputStream dos = new DataOutputStream(item.getOutputStream());
						dos.writeUTF(sms);
					}
				}
				System.out.println(sms); */
				data = (ArrayList)ois.readObject();
			}
		} catch (Exception e) {
			try {
				serSocket.close();
			} catch (IOException ex) {
				System.out.println("Ngắt kết nối Server");
			}
		}
	}
}
