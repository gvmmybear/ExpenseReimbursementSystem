package project1.controller.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import project1.controller.controllers.LoginController;

/**
 * @author Christian Castro
 * Date Last Modified: 05/04/2022
 */
@WebServlet(name="LogoutServlet", urlPatterns={"/logout", "/resources"})
public class LogoutServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		HttpSession session = req.getSession();
		session.invalidate();
		String myPath = "/index.html";
		req.getRequestDispatcher(myPath).forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		HttpSession session = req.getSession();
		session.invalidate();
		String myPath = "/index.html";
		req.getRequestDispatcher(myPath).forward(req, resp);
		
	}
}
