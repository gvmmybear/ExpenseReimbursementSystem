package project1.servicelayer;

import project1.models.UserAccount;
import project1.dao.implementations.UserAccountDaoImpl;


/**
 * @author Christian Castro
 * Date Last Edited: 05/04/2022
 */
public class LoginService {

	/**
	 * Verifies the user's input from login page with database values. 
	 * @param username A String containing the user name
	 * @param password A String containing the password
	 * @return UserAccount object containing all user's attributes if parameters match values
	 * from the database or null if user name and/or password do not match the database .
	 */
	public static UserAccount verifyCredentials(String username, String password) {
		UserAccountDaoImpl accountDao = new UserAccountDaoImpl();
		UserAccount account = accountDao.retrieveAccountByUsername(username);
		if(account != null) {
			String checkPW = account.getPassword();
			if(password.equals(checkPW)) 
				return account;
		}
		return null;
	}
}
