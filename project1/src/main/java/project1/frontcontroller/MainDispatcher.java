package project1.frontcontroller;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import project1.controller.controllers.HomeController;


/**
 * @author Christian Castro
 * Date Last Modified: 05/04/2022
 */
public class MainDispatcher {

	/**
	 * Virtual Router for MasterServlet; routes user to either manager page or employee page
	 * after login has been verified and HTTP session is in use.
	 * @param req HttpServletRequest Object
	 * @param resp HttpServletResponse Object
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void virtualRouter(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		HttpSession session = req.getSession(false);
		if(session!=null) {
			switch((int)session.getAttribute("userRoleId")) {
			case 4:
				HomeController.managerPage(req, resp);
				break;
			default:
				HomeController.homePage(req, resp);
			}			
		}
	}
}
