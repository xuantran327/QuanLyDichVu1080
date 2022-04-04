package QuanLyDichVu1080;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.Socket;
import java.util.*;
import javax.swing.JFrame;
public class ReadClient extends Thread {
	public static Socket cltSocket;
	public ReadClient(Socket cltSocket) {
		this.cltSocket = cltSocket;
	}
	public void run() {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(cltSocket.getInputStream());
			while (true) {
				if ((int)ReadServer.data.get(0) == 0) {
					Client.string = ois.readUTF();
				} else {
					Client.table = (ArrayList<Vector>) ois.readObject();
					Client.vTitle = Client.table.get(0);
					Client.vData = Client.table.get(1);
				}
			}
		} catch (Exception e) {
			try {
				ois.close();
				cltSocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}