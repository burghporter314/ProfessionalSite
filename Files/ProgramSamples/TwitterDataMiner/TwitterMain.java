import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import twitter4j.TwitterException;


/**
 * 
 * @author Dylan Porter
 * 10,000 Words per Author
 * 10 Authors
 * Multiple Languages
 * KiSwahilii
 */

public class TwitterMain {

	public static void main(String[] args) throws InterruptedException, IOException, TwitterException {
		
		WebConnection connect = new WebConnection(new URL("http://indigenoustweets.com/"));
		ArrayList<String> languages = connect.returnLanguages(new URL("http://indigenoustweets.com/"));
		ArrayList<String> acronyms = connect.returnLanguageAcronyms();
		/**
		for(int i = 71; i < acronyms.size(); i++)
			initiateSequence("http://indigenoustweets.com/" + acronyms.get(i) + "/", acronyms.get(i), languages.get(i));
		**/
		initiateSequence("http://indigenoustweets.com/sw/", "sw", "Kiswahili");

	}
	
	static void initiateSequence(String link, String language, String languageName) throws InterruptedException, IOException, TwitterException {
		System.out.println("Language: " + languageName);
		int limit = 0;
		
		WebConnection connect = new WebConnection(new URL(link));
		connect.getUsers();
		ArrayList<String> key = connect.returnKey(), requestedUsers = new ArrayList<String>();
		ArrayList<Double> data = connect.returnData();
		connect.returnLanguages(new URL("http://indigenoustweets.com/eu/"));

		for(int i = 0; i < data.size(); i++) {
			if(data.get(i) > 90) { requestedUsers.add(key.get(i)); limit++;}
			if(limit > 10) { break; }
		}
		
		String userInput="", fileDir;
		
		fileDir = System.getProperty("user.home") + "\\Desktop\\evlfiles";
		new File(fileDir).mkdir();
		
		fileDir = System.getProperty("user.home") + "\\Desktop\\evlfiles\\" + language + " (" + languageName + ")" + "\\";
		new File(fileDir).mkdir();
		
		String[] files = new String[requestedUsers.size()];
		for(int i = 0; i < files.length; i++) { files[i] = fileDir+requestedUsers.get(i) + ".txt"; }
		
		AccessRequest ar = new AccessRequest("gHa4ZdffaVDctsvexU80umVBB",
					"s400dOMEJck8Iewq9Z59u5M76Kk2sPQZwM0k8bh9wNzyjuM9X3",
					""
					+ "854341369682423808-nemiZ8yiGe3PGDcLkUfutxZgybsKe2i",
					"Lc3kvxUHBtOJf7tPEq4R4uVL21sZokcoeUFvdlFMpR84B", fileDir); //e3ddef7ac961f5fdec8b6c51991c428d

		ArrayList<String> languageList = new ArrayList<String>();
		for(int i = 0; i < files.length; i++) {
			ar.gatherTweets(requestedUsers.get(i), language);
			ar.writeTweetsToFile(files[i]);
			for(String s : ar.languages) { if(!languageList.contains(s)) { languageList.add(s);}}
		}
		/**
		languageList.remove(language);
		files = new String[languageList.size()];
		for(int i = 0; i < languageList.size(); i++) { 
			new File(fileDir + languageList.get(i) + "\\").mkdir();
			files[i] = fileDir + languageList.get(i) + "\\";
		}
		
		for(int x = 0; x < languageList.size(); x++) {
			System.out.println("Switching to: " + languageList.get(x));
			for(int i = 0 ; i < requestedUsers.size(); i++) {
				ar.gatherTweets(requestedUsers.get(i), languageList.get(x));
				ar.writeTweetsToFile(files[x] + requestedUsers.get(i) + ".txt");
			}
		}
		**/
	}
}

