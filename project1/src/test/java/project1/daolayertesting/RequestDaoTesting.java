package project1.daolayertesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import project1.dao.implementations.RequestDaoImpl;
import project1.models.ReimbursementRequest;

class RequestDaoTesting {
	static String url = "jdbc:h2:./myH2FolderTest/myMockH2Database";
	static String username = "mockuser";
	static String password = "mockpw";
	static RequestDaoImpl requestDao;
	static ReimbursementRequest testRequest;
	static String testDescript = "This is a test description for unit testing purposes";

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		System.out.println("RequestDao testing has started initialzing!");
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		System.out.println("RequestDao testing has finished!");
	}

	@BeforeEach
	void setUp() throws Exception {
		System.out.println("-------------------------------------");
		requestDao = new RequestDaoImpl(url, username, password);
		requestDao.h2InitDao();
		testRequest = new ReimbursementRequest();
		testRequest.setReqAmount(9.99);
		testRequest.setReqDescription(testDescript);
		testRequest.setReqAuthor(1);
		testRequest.setReqTypeId(1);
		System.out.println("H2 Reimbursement Requests table has been created!");
		System.out.println("-------------------------------------");
	}

	@AfterEach
	void tearDown() throws Exception {
		System.out.println("-------------------------------------");
		requestDao.h2DestroyDao();
		System.out.println("H2 Reimbursement Requests table has been destroyed!");
		System.out.println("-------------------------------------");
	}

	@Test
	void testInsertReimbReq() {
		System.out.println("insert testing started");
		// insertReimbReq() returns -1 if failed insert so we check that method
		// has NOT returned -1.
		int requestId = requestDao.insertReimbReq(testRequest);
		assertNotEquals(requestId, -1);
		System.out.println("insert testing finished");
	}

	@Test
	void testUpdateRequests() {
		System.out.println("update testing started");
		int reqId = requestDao.insertReimbReq(testRequest);
		testRequest.setReqId(reqId);
		boolean result = requestDao.updateReimbReq(testRequest, "Approved");
		assertEquals(result, true);
		System.out.println("update testing finished");
	}
	
	@Test 
	void testRetrieveAllRequests() {
		System.out.println("Retrieve all requests testing started!");
		System.out.println("inserting multiple requests...");
		int n = 5;
		for(int index = 0; index < n; index++) {
			requestDao.insertReimbReq(testRequest);			
		}
		
		System.out.println("\nRetrieving requests...");
		List<ReimbursementRequest> reqList = requestDao.retrieveAllReq();
		for(ReimbursementRequest temp: reqList) {
			System.out.println(temp.toString());
		}
		assertEquals(reqList.size(), n);
		
		System.out.println("Retrieve all requests testing finished!");
	}
}
