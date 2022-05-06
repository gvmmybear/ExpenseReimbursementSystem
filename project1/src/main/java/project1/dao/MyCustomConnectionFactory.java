package project1.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import lombok.Data;

@Data public class MyCustomConnectionFactory {
	public static String url = "jdbc:postgresql://"+System.getenv("MY_DB_ENDPOINT");
	public static String username = System.getenv("MY_DB_USERNAME");
	public static String password = System.getenv("MY_DB_PASSWORD");
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}
	
	static { 
	      try {
	          Class.forName("org.postgresql.Driver");
	      }catch(ClassNotFoundException e) {
	          e.printStackTrace();
	          System.out.println("Static block has failed!");
	      }
	}
}
