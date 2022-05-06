package project1.frontcontroller;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * @author Christian Castro
 * Date Last Modified: 05/04/2022
 */
@WebServlet(name="MasterServlet", urlPatterns={"/home/*"})
public class MasterServlet extends HttpServlet{
	
	// serial ID 
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		HttpSession session = req.getSession(false);
		if(session == null) {
			resp.sendRedirect("http://localhost:8080/project1/login");				
		}else {
			MainDispatcher.virtualRouter(req, resp);
		}
	}
}
