package project1.controller.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Christian Castro
 * Date Last Modified: 05/04/2022
 */
@WebServlet(name="AccountServlet", urlPatterns={"/useraccount"})
public class AccountServlet extends HttpServlet{

	// serial ID
	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		HttpSession session = req.getSession(false);
		if(session != null) {
			int userRoleId = (int)session.getAttribute("userRoleId");
			resp.setContentType("application/json");
			PrintWriter printer = resp.getWriter();
			printer.write(new ObjectMapper().writeValueAsString(userRoleId));
		}
	}
}
