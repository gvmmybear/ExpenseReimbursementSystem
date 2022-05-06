package project1.dao.interfaces;

import project1.models.UserAccount;


public interface UserAccountDao {
	public UserAccount retrieveAccountByUsername(String username);
	public void h2InitDao();
	public void h2DestroyDao();
}
