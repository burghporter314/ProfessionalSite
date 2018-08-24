/**
 * Credit to: https://www.youtube.com/watch?v=HE6ZHSuHcu0
 * http://www.freesqldatabase.com/account/
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ServerSQL {
	
	static Connection conn; 
	
	public ServerSQL() throws Exception {
		conn = getConnection();
	}
	
	public static ArrayList<String> get(boolean isName) throws Exception {
		
		ArrayList<String> arr = null;
		
		try {

			PreparedStatement statement = conn.prepareStatement("SELECT name,message FROM accesstable");
			
			ResultSet result = statement.executeQuery();
			
			arr = new ArrayList<String>();
			if(isName) 
				while(result.next()) { arr.add(result.getString("name")); }
			else
				while(result.next()) { arr.add(result.getString("message")); }
			
		} catch (Exception e) { System.out.println(e); }
		return arr;
	}
	
	public static void post(String name, String message) throws Exception {
		
		try {
			createTable();
			PreparedStatement posted = conn.prepareStatement("INSERT INTO accesstable (name, message) VALUES ('" + name + "','" + message + "')");
			posted.executeUpdate();
		} catch(Exception e) { System.out.println(e); }
		finally { }
	}
	
	public static void deleteUser(String user) throws Exception {
		try {
			String command = "DELETE FROM accesstable WHERE name='"+user+"'";
			
			PreparedStatement create = conn.prepareStatement(command);
			create.executeUpdate();
		} catch(Exception e) { System.out.println(e); }
		finally { }
	}
	
	public static void deleteTable() throws Exception {
		try {
			String command = "DROP TABLE accesstable";
			
			PreparedStatement create = conn.prepareStatement(command);
			create.executeUpdate();
		} catch(Exception e) { System.out.println(e); }
		finally { }
	}
	
	public static void createTable() throws Exception {
		try {
			String command = "CREATE TABLE IF NOT EXISTS "
					+ "accesstable(id int NOT NULL AUTO_INCREMENT, "
					+ "name varchar(3000), message varchar(3000), PRIMARY KEY(id))";
			
			PreparedStatement create = conn.prepareStatement(command);
			create.executeUpdate();
		} catch(Exception e) { System.out.println(e); }
		finally { }
	}
	
	public static Connection getConnection() throws Exception {
		
		try {
			String driver = "com.mysql.jdbc.Driver";
			//String url = "jdbc:mysql://localhost:3306/javaserver"; // change local host to ip address of connecting server
			//String url = "jdbc:mysql://sql9.freemysqlhosting.net:3306/sql9173556";
			String url = "jdbc:mysql://sql9.freesqldatabase.com:3306/sql9174757";
			Class.forName(driver);
			
			conn = DriverManager.getConnection(url, "sql9174757", "ugQQ8chwyD");
			return conn;
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return null;
	}
	
}
