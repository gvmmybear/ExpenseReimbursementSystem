package project1.ServiceLayerTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import project1.dao.implementations.UserAccountDaoImpl;
import project1.models.UserAccount;
import project1.servicelayer.LoginService;

class LoginServiceTesting {
	
	LoginService service =  new LoginService();
	UserAccount testAccount = new UserAccount();
	UserAccountDaoImpl mockAccountDao = Mockito.mock(UserAccountDaoImpl.class);
	
	
	@BeforeEach
	void setUp() throws Exception {
		service.setAccountDao(mockAccountDao);
	}

	@Test
	void test() {
		testAccount.setUsername("username");
		testAccount.setPassword("password");
		testAccount.setFirstName("John");
		testAccount.setLastName("Doe");
		testAccount.setEmail("email@emailaddress.com");
		testAccount.setUserRoleId(1);
	
		// set up mock DAO
		when(mockAccountDao.retrieveAccountByUsername("username")).thenReturn(testAccount);
		
		// conducts verify login test
		UserAccount test = service.verifyCredentials("username", "password");
		
		// checks for null first when asserts method is returning correct user data
		if(test == null)
			fail("Verify credentials returned null when a UserAccount should have been returned");
		else {
			assertEquals(testAccount.toString(), test.toString());
		}
	}
}
