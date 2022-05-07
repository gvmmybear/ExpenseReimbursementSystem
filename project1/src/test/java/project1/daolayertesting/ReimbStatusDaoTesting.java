package project1.daolayertesting;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project1.dao.implementations.ReimbStatusDaoImpl;


class ReimbStatusDaoTesting {
	static String url = "jdbc:h2:./myH2FolderTest/myMockH2Database";
	static String username = "mockuser";
	static String password = "mockpw";
	static ReimbStatusDaoImpl statusDao;
	static int testStatusId;

	@BeforeEach
	void setUp() throws Exception {
		System.out.println("-------------------------------------");
		statusDao = new ReimbStatusDaoImpl(url, username, password);
		statusDao.h2InitDao();
		System.out.println("H2 Reimbursement Status Table has been created!");
		System.out.println("-------------------------------------");
	}

	@AfterEach
	void tearDown() throws Exception {
		System.out.println("-------------------------------------");
		statusDao.h2DestroyDao();
		System.out.println("H2 Table has been dropped after test!");
		System.out.println("-------------------------------------");
	}

	@Test
	void testInsert() {
		System.out.println("insert test has started");
		// insert will return -1 if insert was unsuccessful.
		testStatusId = statusDao.insertReimbStatus();
		System.out.println("Insert returned a new status id of " + testStatusId);
		assertNotEquals(testStatusId, -1);
		System.out.println("insert test finished");
	}
	
	@Test
	void testRetrieve() {
		System.out.println("retrieve test has started");
		// executes insert then immediately calls on retrieve to get retrieveById.
		testStatusId = statusDao.insertReimbStatus();
		String reimbStatus = statusDao.retrieveReimbStatusById(testStatusId);
		assertEquals(reimbStatus , "Pending");
		System.out.println("retrieve test finished");
	}
	
	@Test void testUpdate() {
		System.out.println("updated test has started");
		// inserts new status with "Pending"
		testStatusId = statusDao.insertReimbStatus();
		// updates newly insert status with new status "Approved"
		String newStatus = "Approved";
		statusDao.updateReimbStatus(testStatusId, newStatus);
		// checks that update was successful!
		String reimbStatus = statusDao.retrieveReimbStatusById(testStatusId);
		assertEquals(reimbStatus, "Approved");
		System.out.println("updated test finished");
	}
}
