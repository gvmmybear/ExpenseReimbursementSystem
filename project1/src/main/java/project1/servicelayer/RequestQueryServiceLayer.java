package project1.servicelayer;

import java.util.List;
import project1.models.RequestQueryObject;
import project1.dao.implementations.RequestDaoImpl;

/**
 * @author Christian Castro
 * Date Last Edited: 05/04/2022
 */
public class RequestQueryServiceLayer {

	// static DAO implementation object for class methods
	private static RequestDaoImpl requestDao = new RequestDaoImpl();
	
	/**
	 * Gets list of RequestQueryObjects by user's job role. If manager role type, then
	 * all requests are returned, if employee type, then only that user's requests are
	 * returned. 
	 * @param userId Int value that matches the user's ID stored in the database
	 * @param userRoleId Int value which references the database reference table for user roles.
	 * @return A list of RequestQueryObjects
	 */
	public static List<RequestQueryObject> getRequestsByUserRole(int userId, int userRoleId){
		if(userRoleId == 4) {
			return requestDao.managerQuery();
		}else {
			return requestDao.retrieveTicketsByUserId(userId);
		}
	}
}
