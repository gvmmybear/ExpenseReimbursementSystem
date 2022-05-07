package project1.daolayertesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project1.dao.implementations.UserAccountDaoImpl;
import project1.models.UserAccount;

class AccountDaoTesting {
	static String url = "jdbc:h2:./myH2FolderTest/myMockH2Database";
	static String username = "mockuser";
	static String password = "mockpw";
	static UserAccountDaoImpl accountDao;
	

	@BeforeEach
	void setUp() throws Exception {
		accountDao = new UserAccountDaoImpl();
		accountDao.setUrl(url);
		accountDao.setUser(username);
		accountDao.setPassword(password);
		accountDao.h2InitDao();
	}

	@AfterEach
	void tearDown() throws Exception {
		accountDao.h2DestroyDao();
	}

	// successful test returns generic user account with all fields equal
	@Test
	void test() {
		UserAccount testUser = accountDao.retrieveAccountByUsername("username");
		assertEquals(testUser.getUsername(), "username");
		assertEquals(testUser.getPassword(), "password");
		assertEquals(testUser.getFirstName(), "John");
		assertEquals(testUser.getLastName(), "Doe");
		assertEquals(testUser.getEmail(), "email@emailaddress.com");	
	}
	
	// incorrect user-name should return a null value for the UserAccount object.
	@Test
	void test2() {
		UserAccount testUser = accountDao.retrieveAccountByUsername("incorrectUser");
		assertEquals(testUser, null);
	}
}
