package Demo;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.*;
public class SignUp extends JFrame {
	private JPanel contentPane;
	private JTextField phoneNumtf;
	private JPasswordField passwordField;
	private JTextField nametf;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SignUp frame = new SignUp();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public SignUp() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 298);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel phoneNumlb = new JLabel("Phone number");
		phoneNumlb.setFont(new Font("Arial", Font.PLAIN, 16));
		phoneNumlb.setHorizontalAlignment(SwingConstants.RIGHT);
		phoneNumlb.setBounds(56, 44, 107, 24);
		contentPane.add(phoneNumlb);
		
		JLabel passwordlb = new JLabel("Password");
		passwordlb.setHorizontalAlignment(SwingConstants.RIGHT);
		passwordlb.setFont(new Font("Arial", Font.PLAIN, 16));
		passwordlb.setBounds(56, 78, 107, 24);
		contentPane.add(passwordlb);
		
		phoneNumtf = new JTextField();
		phoneNumtf.setFont(new Font("Arial", Font.PLAIN, 16));
		phoneNumtf.setBounds(173, 44, 200, 24);
		contentPane.add(phoneNumtf);
		phoneNumtf.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(173, 78, 200, 24);
		contentPane.add(passwordField);
		
		JLabel errorlb = new JLabel("Phone number");
		errorlb.setForeground(Color.RED);
		errorlb.setHorizontalAlignment(SwingConstants.CENTER);
		errorlb.setFont(new Font("Arial", Font.PLAIN, 16));
		errorlb.setBounds(56, 146, 317, 24);
		contentPane.add(errorlb);
		
		JButton okbtn = new JButton("OK");
		okbtn.setFont(new Font("Arial", Font.BOLD, 16));
		okbtn.setBounds(56, 180, 147, 32);
		contentPane.add(okbtn);
		
		JButton signUpbtn = new JButton("Sign up");
		signUpbtn.setFont(new Font("Arial", Font.BOLD, 16));
		signUpbtn.setBounds(226, 180, 147, 32);
		contentPane.add(signUpbtn);
		
		JLabel namelb = new JLabel("Name");
		namelb.setHorizontalAlignment(SwingConstants.RIGHT);
		namelb.setFont(new Font("Arial", Font.PLAIN, 16));
		namelb.setBounds(56, 112, 107, 24);
		contentPane.add(namelb);
		
		nametf = new JTextField();
		nametf.setFont(new Font("Arial", Font.PLAIN, 16));
		nametf.setColumns(10);
		nametf.setBounds(173, 112, 200, 24);
		contentPane.add(nametf);
	}
}