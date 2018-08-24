//TODO https://stackoverflow.com/questions/13145942/creating-a-shortcut-file-from-java

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class ProgramSystem extends JFrame{
	
	final static int version = 2;
	static int convertNum, convertNum2, convertNum3;
	JPanel panel;
	JButton submit;
	static boolean abort = true;
	static String topMessage = "", name = "";
	static JScrollPane scroller, scrollerOnline, scrollerPrivate;
	static ArrayList<String> names, messages;
	static int p, p2, p3, q, q2, q3, n, sigmaN, d, e, timerVariable = 0;
	static JLabel display, info, usersOnline, privateDisp;
	static JTextField input;
	static ServerSQL request;
	static RSA ct;
	
	public ProgramSystem() throws Exception {

		display = new JLabel("");
		display.setBounds(0,0,382,350);
		display.setBackground(new Color(250,250,250));
		display.setOpaque(true);
		display.setHorizontalAlignment(0);
		display.setFont(new Font("Serif", Font.PLAIN, 24));
		
		info = new JLabel("Enter Message Below");
		info.setBounds(25,410, 382, 40);
		info.setFont(new Font("Serif", Font.PLAIN, 22));
		info.setHorizontalAlignment(0);
		info.setOpaque(true);
		info.setBackground(new Color(250,250,250));

		privateDisp = new JLabel("");
		privateDisp.setBounds(300,0,382,350);
		privateDisp.setBackground(new Color(250,250,250));
		privateDisp.setFont(new Font("Serif", Font.PLAIN, 24));
		privateDisp.setHorizontalAlignment(0);
		privateDisp.setOpaque(true);
		
		usersOnline = new JLabel("");
		usersOnline.setBounds(300,0,382,350);
		usersOnline.setBackground(new Color(250,250,250));
		usersOnline.setFont(new Font("Serif", Font.PLAIN, 24));
		usersOnline.setHorizontalAlignment(0);
		usersOnline.setOpaque(true);
		
		input = new JTextField();
		input.setBounds(30, 477, 370, 40);
		input.setBackground(new Color(250,250,250));
		input.setFont(new Font("Serif", Font.PLAIN, 20));
		
		submit = new JButton("Submit");
		submit.setBounds(120,540,202,50);
		submit.setBackground(new Color(250,250,250));

		submit.setFont(new Font("Serif", Font.PLAIN, 22));
		
		scroller = new JScrollPane(display,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroller.setViewportView(display);
		scroller.setBounds(0,0,432,350);
		scroller.getVerticalScrollBar().setUnitIncrement(30);
		
		scrollerPrivate = new JScrollPane(privateDisp,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollerPrivate.setViewportView(privateDisp);
		scrollerPrivate.setBounds(431,0,432,350);
		scrollerPrivate.getVerticalScrollBar().setUnitIncrement(30);
		
		scrollerOnline = new JScrollPane(usersOnline,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollerOnline.setViewportView(usersOnline);
		scrollerOnline.setBounds(430,348,433,350);
		scrollerOnline.getVerticalScrollBar().setUnitIncrement(30);

		panel = new JPanel();
		panel.setLayout(null);
		panel.add(input);
		panel.add(submit);
		panel.add(scroller);
		panel.add(scrollerPrivate);
		panel.add(scrollerOnline);
		panel.setBackground(new Color(250,250,250));


		panel.add(info);
		//setSize(387,600);
		setSize(868,731);
		
		setTitle("Dylan's Messaging System");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		this.getContentPane().add(panel);
		setVisible(true);
		SwingUtilities.getRootPane(submit).setDefaultButton(submit);

		request.createTable();
		displayMessages();
		
		submit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent E) {
				try { abort = true; submitPost(); displayMessages();} catch (Exception e) {}
			}
			
		});
		
		addWindowListener(new WindowAdapter() {
		   public void windowClosing(WindowEvent evt) {
			   try { deleteOnline(); } catch (Exception e) {}
		   }
		});
		
	}

	static String encryptTriple(String message) throws Exception {
		
	    n = ct.returnN(p,q); sigmaN = ct.returnSigma(p, q);
		d = ct.returnD(sigmaN); e = ct.returnE(d, sigmaN);
		String encryptedMSG = ct.writeBytes(message, e, n, convertNum);
	    n = ct.returnN(p2,q2); sigmaN = ct.returnSigma(p2, q2);
		d = ct.returnD(sigmaN); e = ct.returnE(d, sigmaN);
		
		encryptedMSG = ct.writeBytes(encryptedMSG, e, n, convertNum2);

	    n = ct.returnN(p3,q3); sigmaN = ct.returnSigma(p3, q3);
		d = ct.returnD(sigmaN); e = ct.returnE(d, sigmaN);
		
		encryptedMSG = ct.writeBytes(encryptedMSG, e, n, convertNum3);

		return encryptedMSG;
	}
	
	static String decryptTriple(String message) {
		
	    n = ct.returnN(p3,q3); sigmaN = ct.returnSigma(p3, q3);
		d = ct.returnD(sigmaN); e = ct.returnE(d, sigmaN);
		
		String decryptedMSG = ct.decodeMessage(message, d, n, convertNum3);
		
	    n = ct.returnN(p2,q2); sigmaN = ct.returnSigma(p2, q2);
		d = ct.returnD(sigmaN); e = ct.returnE(d, sigmaN);
		
		decryptedMSG = ct.decodeMessage(decryptedMSG, d, n, convertNum2);
		
	    n = ct.returnN(p,q); sigmaN = ct.returnSigma(p, q);
		d = ct.returnD(sigmaN); e = ct.returnE(d, sigmaN);
		decryptedMSG = ct.decodeMessage(decryptedMSG, d, n, convertNum);

		decryptedMSG = ct.finalMessage(decryptedMSG);
		return decryptedMSG;
	}
	
	public static void submitPost() throws IOException, Exception {
		
		String s = input.getText();
		if(input.getText().equals("DROP")) { 
			request.deleteTable(); request.createTable(); ct.postBytes("System", encryptTriple(ct.writeMessage("Type INFO for Available Commands!")));
		} else if(input.getText().contains("NAME")) {
			if(!names.contains(s.substring(5)) && !names.contains("~" + s.substring(5))) {changeName(s.substring(5));}
			else { JOptionPane.showMessageDialog(null, "Can't change name: Belongs to another User!"); }
		} else if(input.getText().equals("DELETE ME")) {
			request.deleteUser(name); 
		} else if(input.getText().equals("DELETE PRIVATE")) {
			request.deleteUser("~" + name);
		} else if(input.getText().contains("PRIVATE")) {
			s = s.substring(s.indexOf(" ") + 1);
			String privateName = s.substring(0, s.indexOf(" "));
			String privateMessage = s.substring(s.indexOf(" ") + 1);
			ct.postBytes("~" + privateName, encryptTriple(ct.writeMessage(name + ": " + privateMessage)));
		} else if(input.getText().contains("LOWCONF")) {
			ct.postBytes("Confidential Message", encryptTriple(ct.writeMessage(input.getText().replaceAll("LOWCONF", ""))));
		} else if(input.getText().contains("HIGHCONF")) {
			ct.postBytes("Highly Confidential Message", encryptTriple(ct.writeMessage(input.getText().replaceAll("HIGHCONF", ""))));
			timerVariable = 0;
		} else if(input.getText().equals("INFO")) {
			JOptionPane.showMessageDialog(null,  "DELETE ME - delete your messages from the Queue\n"
				+ "DELETE PRIVATE - Delete your private messages\n"
				+ "NAME name - Change you display name\n"
				+ "PRIVATE name message - Send a private message to name (Case Sensitive)\n"
				+ "CHANGEP - Change P Variable\n"
				+ "CHANGEP2 - Change P2 Variable\n"
				+ "CHANGEP3 - Change P3 Variable\n"
				+ "CHANGEQ - Change Q Variable\n"
				+ "CHANGEQ2 - Change Q2 Variable\n"
				+ "CHANGEQ3 - Change Q3 Variable\n"
				+ "CHANGEFREQ - Change number of layers for encryption"
				+ "CHANGEFREQ2 - Change number of layers for encryption 2"
				+ "CHANGEFREQ3 - Change number of layers for encryption 3");
		} else if(input.getText().equals("CHANGEP")) {
			p = Integer.parseInt(JOptionPane.showInputDialog("Enter new 'P' value."));
		} else if(input.getText().equals("CHANGEQ")) {
			q = Integer.parseInt(JOptionPane.showInputDialog("Enter new 'Q' value."));
		} else if(input.getText().equals("CHANGEP2")) {
			p2 = Integer.parseInt(JOptionPane.showInputDialog("Enter new 'P2' value."));
		} else if(input.getText().equals("CHANGEQ2")) {
			q2 = Integer.parseInt(JOptionPane.showInputDialog("Enter new 'Q2' value."));
		} else if(input.getText().equals("CHANGEP3")) {
			p3 = Integer.parseInt(JOptionPane.showInputDialog("Enter new 'P3' value."));
		} else if(input.getText().equals("CHANGEQ3")) {
			q3 = Integer.parseInt(JOptionPane.showInputDialog("Enter new 'Q3' value."));
		} else if(input.getText().equals("CHANGEFREQ")) {
			convertNum = Integer.parseInt(JOptionPane.showInputDialog("Enter new number for layers of encryption."));
		} else if(input.getText().equals("CHANGEFREQ2")) {
			convertNum2 = Integer.parseInt(JOptionPane.showInputDialog("Enter new number for layers of encryption 2."));
		} else if(input.getText().equals("CHANGEFREQ3")) {
			convertNum3 = Integer.parseInt(JOptionPane.showInputDialog("Enter new number for layers of encryption 3."));
		} else {ct.postBytes(name, encryptTriple(ct.writeMessage(input.getText())));}
		
	    n = ct.returnN(p,q); sigmaN = ct.returnSigma(p, q);
		d = ct.returnD(sigmaN); e = ct.returnE(d, sigmaN);
		
		display.setText("");
		input.setText("");
		displayMessages();
	}
	
	public static void submitOnline() throws IOException, Exception { 
		if(!names.contains(name+"ONLINE")) { ct.postBytes(name + "ONLINE", encryptTriple(ct.writeMessage(""))); }
	}
	
	public static void deleteOnline() throws Exception { request.deleteUser(name + "ONLINE"); 
		request.deleteUser("Confidential Message");
		request.deleteUser("Highly Confidential Message");
	}
	
	public static void displayMessages() {
		
		try { names = request.get(true); messages = request.get(false);} catch (Exception e) {}
				
		if(names.size() == 0) {return;}
		
		if(names.get(names.size()-1).equals("System")) { abort = true; }
		
		String text = "<html><div style='overflow:scroll; width:280px; '>";
		String privateText = "<html><div style='overflow:scroll; width:280px; '>";
		String users = "<html><div style='overflow:scroll; width:280px; '>";
		
		for(int i = names.size()-1; i >= 0; i--) {
			if(!names.get(i).contains("ONLINE")) {
				if(names.get(i).substring(0,1).equals("~") && names.get(i).contains(name)) {
					privateText += "<p style='margin:0px; max-width: 100px; margin-bottom:0px;'><span style='color:#a85346'>" 
						+ "Private Message" +"</span>" + " - " + decryptTriple(messages.get(i))
							+ "</p>";
				} else if (!names.get(i).substring(0,1).equals("~")) {
					text += "<p style='margin:0px; max-width: 100px; margin-bottom:4px;'><span style='color:#326d87'>" 
						+ names.get(i) +"</span>" + " - " + decryptTriple(messages.get(i))
							+ "</p>";
				}
			} else {
				users += "<p style='margin:8px; max-width: 100px; margin-bottom:0px;text-align:center'><span style='color:#326d87'>" 
						+ names.get(i).replace("ONLINE", "") + "</span> " + " is now online"
							+ "</p>";
			}
		}

		if(abort) { topMessage = messages.get(names.size()-1); }

		if(!messages.get(names.size()-1).equals(topMessage) && !abort) { sound(); topMessage = messages.get(names.size()-1); }
		display.setText(text + "</div></html>");
		privateDisp.setText(privateText + "</div></html>");
		usersOnline.setText(users + "</div></html>");
		abort = false;

	}
	
	public static void sound() {
		Toolkit.getDefaultToolkit().beep();
	}
	
	public static void createDir() throws IOException {
		File file = new File("C:\\MessagingSystem");
		if(!file.exists()) { file.mkdir(); }
		file = new File("C:\\MessagingSystem\\name.txt");
		if(!file.exists()) {
			file.createNewFile();
			String name = JOptionPane.showInputDialog("Enter Name:");
			FileOutputStream os = new FileOutputStream(file);
			char[] chars = name.toCharArray();
			for(char s : chars) {os.write(s); }
			os.close();
		} else {return;}
		
	}
	
	static void changeName(String newName) throws Exception {
		deleteOnline();
		FileOutputStream os = new FileOutputStream("C:\\MessagingSystem\\name.txt");
		char[] n = newName.toCharArray();
		for(char s : n) { os.write(s); }
		os.close();
		name = newName;
		submitOnline();
		return;
	}
	
	static void setName() {
		try {
			FileInputStream is = new FileInputStream("C:\\MessagingSystem\\name.txt");
			int n;
			String readName = "";
			while((n = is.read()) != -1) {
				readName += Character.toString((char)n);
			}
			name = readName;
		} catch (IOException e) {}
	}
	
	static void writeVersion() throws IOException {
		File file = new File("C:\\MessagingSystem\\version.txt");
		FileOutputStream os = new FileOutputStream(file);
		if(!file.exists()) { file.createNewFile(); }
		os.write(version+48);
		os.close();
	}
	
	public static void main(String[] args) throws Exception {
					
		createDir();
		setName();
		writeVersion();
		
		p = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter variable 'P'"));
		q = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter variable 'Q'"));
		p2 = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter variable 'P2'"));
		q2 = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter variable 'Q2'"));
		p3 = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter variable 'P3'"));
		q3 = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter variable 'Q3'"));
		convertNum = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter number of layers for encryption 1"));
		convertNum2 = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter number of layers for encryption 2"));
		convertNum3 = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter number of layers for encryption 3"));

		Updater updater = new Updater();
		File file = new File("C:\\MessagingSystem\\MessagingSystem.jar");
		if(updater.checkUpdate(version) || !file.exists()) {
			updater.update();
		}
		
		file = new File(System.getProperty("user.home") + "\\Desktop\\MessagingSystemLaunch.jar");
		if(!file.exists()) {
			updater.update();
		}
		
		request = new ServerSQL();
		request.deleteUser("Confidential Message");
		request.deleteUser("Highly Confidential Message");
		ct = new RSA();

	    n = ct.returnN(p,q); sigmaN = ct.returnSigma(p, q);
		d = ct.returnD(sigmaN); e = ct.returnE(d, sigmaN);
		ProgramSystem ps = new ProgramSystem();
				
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() { @Override public void run() { displayMessages(); try {
			if(timerVariable == 3) { request.deleteUser("Highly Confidential Message"); }
			if(timerVariable++ == 100) {timerVariable = 4;};
			submitOnline();
		} catch (Exception e) { } } }, 3*1000, 3*1000);
		
	}
	
}
