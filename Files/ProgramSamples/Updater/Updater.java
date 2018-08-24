import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

public class Updater {

	private int updaterVersion;
	
	boolean checkUpdate(int version) throws IOException {
		
		URL website = new URL("http://burghporter31415.x10host.com/Messaging%20System%20Updater/versionPKG.txt");
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		FileOutputStream fos = new FileOutputStream("C:\\MessagingSystem\\versionPKG.txt");
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		
		FileInputStream os = new FileInputStream("C:\\MessagingSystem\\versionPKG.txt");
		updaterVersion = Character.getNumericValue(os.read());
		if(version != updaterVersion) {return true;}
		return false;
		
	}
	
	void update() throws IOException {
		
		JOptionPane.showMessageDialog(null, "The Program needs to Update ... Press OK to Start.");
				
		File file = new File("C:\\MessagingSystem\\version.txt");

		FileOutputStream os = new FileOutputStream(file);
		os.write(updaterVersion+48);
		os.close();
		
		URL website = new URL("http://burghporter31415.x10host.com/Messaging%20System%20Updater/MessagingSystem.jar");
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		FileOutputStream fos = new FileOutputStream("C:\\MessagingSystem\\MessagingSystem.jar");
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		
		
		website = new URL("http://burghporter31415.x10host.com/Messaging%20System%20Updater/MessagingSystem.jar");
		rbc = Channels.newChannel(website.openStream());
		File temp = new File(System.getenv("APPDATA") + "\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs");
		temp.mkdir();
		fos = new FileOutputStream(System.getenv("APPDATA") + "\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\MessagingSystem.jar");
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		
		website = new URL("http://burghporter31415.x10host.com/Messaging%20System%20Updater/MessagingSystemLaunch.jar");
		rbc = Channels.newChannel(website.openStream());
		fos = new FileOutputStream(System.getProperty("user.home") + "\\Desktop\\MessagingSystemLaunch.jar");
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			  @Override
			  public void run() {
				JOptionPane.showMessageDialog(null, "Update Complete... Press OK to Restart. If \n the app doesn't restart, just start it yourself.");
		        try {
					Process p = Runtime
					        .getRuntime()
					        .exec("rundll32 url.dll,FileProtocolHandler c:\\MessagingSystem\\MessagingSystem.jar");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
				System.exit(0);
			  }
		}, 7*1000);		
	}
	
}
