package QuanLyDichVu1080;
import java.io.*;
import java.net.Socket;
import java.sql.*;
import Connect.Connect;
public class WriteServer extends Thread {
	public void run() {
		ObjectOutputStream oos = null;
		while (true) {
			try {
				for (Socket item : Server.listSK) {
					if (item.getPort() == ReadServer.serSocket.getPort()) {
						oos = new ObjectOutputStream(item.getOutputStream());
						if ((int)ReadServer.data.get(0) == 0) {
							if (((String)ReadServer.data.get(1)).equals("") || ((String)ReadServer.data.get(2)).equals("") || ((String)ReadServer.data.get(4)).equals("")) {
								oos.writeObject((Object)"You must fill in all required fields!");
							} else if (search((String)ReadServer.data.get(1),(String)ReadServer.data.get(2),(String)ReadServer.data.get(3))) {
								oos.writeObject((Object)"This account has already existed!");
							} else {
								oos.writeObject((Object)"Sign-up successful!");
								//insert();
							}
						}
					}
					//dos.writeUTF("Server: " + sms);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	public boolean search(String str1, String str2, String str3) {
		boolean result = false;
		try {
			Connect.Connect();
			String sql = "SELECT DienThoai, MatKhau, LoaiTaiKhoan FROM TAIKHOAN WHERE DienThoai = '" + str1 + "'";
			ResultSet rst = Connect.stmt.executeQuery(sql);
			if ((int)ReadServer.data.get(0) == 1) {
				while (rst.next()) {
					if (str1.equals(rst.getString(1)) && str2.equals(rst.getString(2)) && str3.equals(rst.getString(3))) {
						result = true;
						break;
					}
				}
				rst.close();
			}
			else {
				while (rst.next()) {
					if (str1.equals(rst.getString(1))) {
						result = true;
						break;
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return result;
	}
	public void insert() {
		try {
			String pn = (String)ReadServer.data.get(1);
			String pw = (String)ReadServer.data.get(2);
			String ar = (String)ReadServer.data.get(3);
			String na = (String)ReadServer.data.get(4);
			String sql = "insert into TAIKHOAN values ('" + pn + "','" + pw + "','" + ar + "',N'" + na + "')";
			Connect.stmt.executeUpdate(sql);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
