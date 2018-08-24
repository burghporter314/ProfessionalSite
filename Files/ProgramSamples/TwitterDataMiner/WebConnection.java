import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebConnection {
	
	private URL url;
	private ArrayList<String> key;
	private ArrayList<Double> data;
	private ArrayList<String> languages = new ArrayList<String>();
	private ArrayList<String> languageAcronyms = new ArrayList<String>();
	
	public WebConnection(URL url) {
		this.url = url;
	}
	
	private String getSource(URL url) {
		try {
			URLConnection connection = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "UTF-8"));
			String inputLine;
			StringBuilder builder = new StringBuilder();
			while ((inputLine = in.readLine()) != null) 
				builder.append(inputLine);
			in.close();
			return builder.toString();
		} catch (IOException e) {return "";}
		
	}
	
	private void getLanguages(URL url) throws MalformedURLException {

		String z = getSource(url);
		while(z.contains("<a href=")) {
			z = z.substring(z.indexOf("<a href="), z.length());
			z = z.replaceFirst("a href=","");
			languageAcronyms.add(z.substring(z.indexOf("\"")+1,z.indexOf("/")));
			languages.add(z.substring(z.indexOf(">")+1,z.indexOf("</a>")));
		}
	}
	
	protected void getUsers() {
		String s = getSource(this.url);
		this.key = new ArrayList<String>();
		this.data = new ArrayList<Double>();
		Pattern regex = Pattern.compile("(\\d+(?:\\.\\d+)?)");
		Matcher matcher = null;
		double currentNum = 0, prevNum = 0;
		while(s.contains("<a href=")) {
			s = s.substring(s.indexOf("<a href="), s.length());
			s = s.replaceFirst("a href=","");
			key.add(s.substring(21,s.indexOf("><img")-1));
			matcher = regex.matcher(s.substring(21,600));
			
			while(matcher.find()) { 
				if(matcher.group(1).contains(".")) {
					currentNum = Double.parseDouble(matcher.group(1));
					if(currentNum != prevNum) {
						prevNum = currentNum;
						data.add(currentNum);
					}
				}
			}
			
		}
		while(key.size() > data.size()-1) { key.remove(key.size()-1); }
		data.remove(data.size()-1);
	}
	
	protected ArrayList<String> returnLanguages(URL url) throws MalformedURLException {
		getLanguages(url);
		return this.languages;
	}
	
	protected ArrayList<String> returnLanguageAcronyms() { return this.languageAcronyms; }
	
	protected ArrayList<String> returnKey() {
		return this.key;
	}
	
	protected ArrayList<Double> returnData() {
		return this.data;
	}
}
	
