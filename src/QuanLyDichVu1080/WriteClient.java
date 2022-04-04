package QuanLyDichVu1080;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JFrame;
import Login.Login;
public class WriteClient extends Thread {
	public static Socket cltSocket;
	public static JFrame fr;
	public static ActionEvent ae;
	public WriteClient(Socket cltSocket) {
		this.cltSocket = cltSocket;
	}
	public void run() {
		ObjectOutputStream oos = null;
		Client.data = new ArrayList();
		try {
			oos = new ObjectOutputStream(cltSocket.getOutputStream());
			if (fr.getTitle().equals("Sign up") && ae.getActionCommand().equals("OK")) {
				Client.data.add(0);
				Client.data.add(Login.phoneNumber.getText());
				Client.data.add(Login.password.getText());
				Client.data.add("Customer");
				Client.data.add(Login.name.getText());
			}
			oos.writeObject((Object)Client.data);
		} catch (Exception e) {
			try {
				oos.close();
				cltSocket.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
