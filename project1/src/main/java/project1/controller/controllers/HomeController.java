package project1.controller.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Christian Castro
 * Date Last Modified: 05/04/2022
 */
public class HomeController {
	
	/**
	 * Secondary dispatcher/router for forwarding user to correct home page
	 * after login validation.
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void homeRouter(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		switch(req.getRequestURI()) {
		case "/project1/home":
			homePage(req, resp);
			break;
		case "/project1/home/manager":
			managerPage(req, resp);
			break;
		default:
			System.out.println("Client Request Error");
		}
	}
	
	/**
	 * Forwards user to employee web-page after login validation
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void homePage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		RequestDispatcher redis = req.getRequestDispatcher("/resources/html/home.html");
		redis.forward(req, resp);

	}
	
	/**
	 * Forwards user to manager web-page after login and user credential validation.
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void managerPage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		RequestDispatcher redis = req.getRequestDispatcher("/resources/html/manager.html");
		redis.forward(req, resp);

	}
	
}
