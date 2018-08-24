//github.com/detectlanguage/detectlanguage-java
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

//Create a Cursor -- Java access to Server
public class AccessRequest {

	ConfigurationBuilder cb;
	File userFile = null;
	Twitter twitter; 
	ArrayList<Status> statuses;
	ArrayList<String> languages;
	ST<String,Integer> database = new ST<>();
	String lang, user, fileDir, previousBuffer = "";
	ArrayList<Long> idList = new ArrayList<Long>();
	public AccessRequest(String c1, String c2, String c3, String c4, String fileDir)  {
		
		cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey(c1)
		  .setOAuthConsumerSecret(c2)
		  .setOAuthAccessToken(c3)
		  .setOAuthAccessTokenSecret(c4);
	    TwitterFactory tf = new TwitterFactory(cb.build());
	    twitter = tf.getInstance();
		this.fileDir=fileDir;
		
	}
	
	
	protected ArrayList<Status> gatherTweets(String account, String lang) throws InterruptedException {
		
		this.lang = lang;
	    int pageno = 1;
	    this.user = account;
	    statuses = new ArrayList<Status>();
	    
	    while (true) {
          int size = statuses.size(); 
          Paging page = new Paging(pageno++, 100);
          try {
        	  statuses.addAll(twitter.getUserTimeline(user, page));
          } catch(TwitterException e) {
        	  System.out.println("Sleeping for 7 mins...");
        	  Thread.sleep(420000); 
          }
          if (statuses.size() == size) break;
	    }
	    return statuses;
	}
	
	/**
	 * TODO Write To File 
	 * @throws APIError 
	 * @throws IOException 
	 * @throws LangDetectException 
	 */
	protected void writeTweetsToFile(String fileAddress) throws IOException {
		languages = new ArrayList<>();
		int lines = 0;
		
		System.out.println("User: " + this.user);
		userFile = new File(fileAddress);
		File subFile = null;
		if(!createFile(userFile)) { System.err.print("Failed to create file."); }
		
        for (Status st:statuses) {
        	
        	if(lines++ > 2000) {break;}
        	String s = st.getText();
        	String lang = st.getLang();

        	if(!languages.contains(lang)) { 
        		languages.add(lang);
        	}
        	
        	/** **/
        	subFile = new File(fileDir + lang + "\\");
        	subFile.mkdir();
        	subFile = new File(fileDir+lang+"\\" + this.user+".txt");
        	if(!subFile.exists()) {subFile.createNewFile();}
        	
    		if(!database.contains(lang)) {
    			database.put(lang, 0);
    		} 
    		
    		if(!st.isRetweet()) {
    			int prevNum = database.get(lang);
    			database.put(lang, ++prevNum);
	        	writeToFile(s, subFile, st.getId(), true);
	        	idList.add(st.getId());
    		}
        }
        
        int maxEl=0, currentEl = 0;
        String strEl = "", message = "";
        for(String s : languages) { 
        	currentEl = database.get(s);
        	database.put(s, 0);
        	message = s + " ("+currentEl+" hits) ";
        	writeToFile(message, userFile, 0, false);
        	if(currentEl > maxEl) {maxEl = currentEl; strEl = s;}
        }
        message = "Most hit language: ("+ strEl + ") hits: (" + maxEl + ") ";
        writeToFile(message, userFile, 0, false);
        languages = new ArrayList<>();
	}
	
	/**
	private String getModifiedBuffer(String s) throws APIError  {
    	String[] badChars = {":",",","!","\\.","\\?","-","[0-9]+",">","<","\n","'","\"","\\\\","%","/", "\\|", "\\[", "\\]","\\;"};
    	String[] badSequences = { "#[^\\s]", "RT[^\\s]*", "@[^\\s]*", "\\([^\\s]*","\\)[^\\s]*" };
    	
    	for(String st : badChars) { s = s.replaceAll(st, ""); }
    	for(String sequence: badSequences) { s = s.replaceAll(sequence, ""); }
    	s = s.replace("  ", " ");
    	s = s.trim();
    	try {
    		s = s.substring(s.indexOf(" "), s.length());
    		s = s.substring(0, s.lastIndexOf(" "));
    	}
    	catch(StringIndexOutOfBoundsException e) {}
    	
    	String split[] = s.split(" ");

    	String seq = "", tempSeq = "";
    	try {
	    	for(int i = 3; i < split.length; i+=4) {
	    		tempSeq = split[i] + " " + split[i-1] + " " + split[i-2] + " " + split[i-3] + " ";
	    		results = DetectLanguage.detect(tempSeq);
	    		if(results.get(0).language.equals(this.lang)) {
	    			seq += tempSeq;
	    		}
	    	}
    	} catch(IndexOutOfBoundsException e) {}
    	
    	results = DetectLanguage.detect(s);
    	try {
    		if(results.get(0).language.equals(this.lang)) { System.out.println(s); return s; }
        	else {return "";}
    	} catch(IndexOutOfBoundsException e) { return ""; }

    	return s;
	}
	*/
	
	private boolean createFile(File file) {
		try { if(!file.exists()) { file.createNewFile(); } }
		catch (IOException ex) { return false; }
		return true;
	}
	
	/**
	 * TODO
	 * @param buffer
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	
	private boolean writeToFile(String buffer, File file, long id, boolean restricted) throws IOException  {

		if(idList.contains(id) && restricted) {return false; }
		
		Scanner in = new Scanner(new FileReader(file));
		ArrayList<String> everything = new ArrayList<String>();
		while(in.hasNext()) { everything.add(in.nextLine()); }
		in.close();
		
		try {
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			for(String s : everything) {bw.write(s); bw.newLine(); }
			bw.write(buffer);
			bw.close();
		} catch (IOException e) { return false; }
		return true;
	}
}
