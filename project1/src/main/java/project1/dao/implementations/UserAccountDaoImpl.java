package project1.dao.implementations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lombok.Data;
import project1.dao.interfaces.UserAccountDao;
import project1.models.UserAccount;

@Data public class UserAccountDaoImpl implements UserAccountDao{
	public String url = "jdbc:postgresql://"+System.getenv("MY_DB_ENDPOINT");
	public String user = System.getenv("MY_DB_USERNAME");
	public String password = System.getenv("MY_DB_PASSWORD");
	
	// RETRIEVES USER ACCOUNT DATA BY USERNAME 
	@Override
	public UserAccount retrieveAccountByUsername(String username) {
		try(Connection conn = DriverManager.getConnection(url, user, password)){
			String sqlStatement = "SELECT * FROM ers_users WHERE ers_username = ?";
			PreparedStatement ps = conn.prepareStatement(sqlStatement);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) 
				return makeTempAccount(rs);
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Something went wrong retrieving account information!");
		}
		return null;
	}	
	
	/** Private class method for making a temporary account object by using the
	 * ResultSet from an SQL statement. 
	 * @param rs
	 * @return
	 */
	private static UserAccount makeTempAccount(ResultSet rs) {
		UserAccount tempAccount = new UserAccount();
		try {
			tempAccount.setUserId(rs.getInt("ers_users_id"));
			tempAccount.setUsername(rs.getString("ers_username"));
			tempAccount.setPassword(rs.getString("ers_password"));
			tempAccount.setFirstName(rs.getString("user_first_name"));
			tempAccount.setLastName(rs.getString("user_last_name"));
			tempAccount.setEmail(rs.getString("user_email"));
			tempAccount.setUserRoleId(rs.getInt("user_role_id"));
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("something failed when making temporary account!");
		}
		return tempAccount;
	}
	
	static { 
	      try {
	          Class.forName("org.postgresql.Driver");
	      }catch(ClassNotFoundException e) {
	          e.printStackTrace();
	          System.out.println("Static block has failed!");
	      }
	}

	@Override
	public void h2InitDao() {
		try(Connection conn = DriverManager.getConnection(url, user, password)){
			System.out.println("h2Init called");
			String sqlStatement = "CREATE TABLE ers_users (\r\n"
					+ "	ERS_USERS_ID SERIAL\r\n"
					+ "	, ERS_USERNAME VARCHAR(50) UNIQUE\r\n"
					+ "	, ERS_PASSWORD VARCHAR(50) NOT NULL\r\n"
					+ "	, USER_FIRST_NAME VARCHAR(100)\r\n"
					+ "	, USER_LAST_NAME VARCHAR(100)\r\n"
					+ "	, USER_EMAIL VARCHAR(150) UNIQUE \r\n"
					+ "	, USER_ROLE_ID INTEGER\r\n"
					+ "	, PRIMARY KEY(ERS_USERS_ID)\r\n"
					+ ");";
			Statement statement = conn.createStatement();
			statement.execute(sqlStatement);
			
			String sqlInsert = "INSERT INTO ers_users(\r\n"
					+ "	ers_username, \r\n"
					+ "	ers_password, \r\n"
					+ "	user_first_name, \r\n"
					+ "	user_last_name, \r\n"
					+ "	user_email, \r\n"
					+ "	user_role_id) VALUES (\r\n"
					+ "	'username',\r\n"
					+ "	'password',\r\n"
					+ "	'John',\r\n"
					+ "	'Doe',\r\n"
					+ "	'email@emailaddress.com',\r\n"
					+ "	1);";
			PreparedStatement ps = conn.prepareStatement(sqlInsert);
			int i = ps.executeUpdate();
			System.out.println(i);
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Something went wrong with h2Init()!");
		}
	}

	@Override
	public void h2DestroyDao() {
		try(Connection conn = DriverManager.getConnection(url, user, password)){
		 System.out.println("h2Destroy called");
         String sql= "DROP TABLE ERS_USERS;";
         Statement statement = conn.createStatement();
         statement.execute(sql);
     }catch(SQLException e) {
         e.printStackTrace();
         System.out.println("something went wrong with h2Destroy()");
     }


	}
}
