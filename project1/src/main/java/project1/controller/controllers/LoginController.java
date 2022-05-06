package project1.controller.controllers;

import java.io.IOException;
import project1.models.UserAccount;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import project1.servicelayer.LoginService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Christian Castro
 * Date Last Edited: 05/04/2022
 */
public class LoginController {
	
	/**
	 * Verifies login credentials by comparison with values stored in the database.
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void verifyLogin(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		String path = null;
		
		// gets user name and password parameters from HTML form then calls on 
		// helper method in LoginService to verify valid entry.
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		System.out.println(username + " " + password);
		
		// returns UserAccount object if user name and password matched.
		UserAccount user = LoginService.verifyCredentials(username, password);
		
		// if login was verified then HTTP session attributes are set from the
		// user's information from the database. 
		if(user != null) {
			HttpSession session = req.getSession();
			session.setAttribute("username", user.getUsername());
			session.setAttribute("firstName", user.getFirstName());
			session.setAttribute("lastName", user.getLastName());
			session.setAttribute("userId", user.getUserId());
			session.setAttribute("userRoleId", user.getUserRoleId());
			
			if(user.getUserRoleId() == 4) {
				path = "http://localhost:8080/project1/home/manager";
			}else {
				path = "http://localhost:8080/project1/home";
			}
			resp.sendRedirect(path);
		}else {
			path = "/index.html";
			System.out.println("client login failed");
			req.getRequestDispatcher(path).forward(req, resp);
		}
		return;
	}
}
