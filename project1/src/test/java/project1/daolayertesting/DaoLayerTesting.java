package project1.daolayertesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project1.dao.implementations.UserAccountDaoImpl;
import project1.models.UserAccount;

class DaoLayerTesting {
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

	@Test
	void test() {
		UserAccount testUser = accountDao.retrieveAccountByUsername("username");
		if(testUser == null)
			fail("returned null instead of UserAccount object");
		else
			assertEquals(testUser.getUsername(), "username");
	}

}
