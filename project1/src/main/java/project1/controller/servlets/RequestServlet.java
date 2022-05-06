package project1.controller.servlets;

import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import project1.models.RequestQueryObject;
import project1.models.ReimbursementRequest;
import project1.dao.implementations.RequestDaoImpl;
import project1.servicelayer.RequestQueryServiceLayer;
import project1.dao.implementations.ReimbStatusDaoImpl;

/**
 * @author Christian Castro
 * Date Last Modified: 05/04/2022
 */
@WebServlet(name="RequestServlet", urlPatterns={"/viewrequests", "/submitrequest", "/updaterequest"})
public class RequestServlet extends HttpServlet{

	// Class Fields
	RequestDaoImpl dao = new RequestDaoImpl();
	ReimbStatusDaoImpl statusDao = new ReimbStatusDaoImpl();
	
	/**
	 * doGet for retrieving all requests from the database based on the account type 
	 * that is currently logged in.
	 */
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		HttpSession session = req.getSession(false);
		if(session != null) {
			int userId = (int) session.getAttribute("userId");
			int userRoleId = (int) session.getAttribute("userRoleId");
			List<RequestQueryObject> requests = RequestQueryServiceLayer.getRequestsByUserRole(userId, userRoleId);
			resp.setContentType("application/json");
			PrintWriter printer = resp.getWriter();
			printer.write(new ObjectMapper().writeValueAsString(requests));
		}
	}
	
	/**
	 * doPost method for submitting and adding a new request to the database.
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		HttpSession session = req.getSession(false);
		if (session != null) {
			ObjectMapper mapper = new ObjectMapper();
			ReimbursementRequest request = mapper.readValue(req.getInputStream(), ReimbursementRequest.class);
			request.setReqAuthor((int)session.getAttribute("userId"));
			int reimbId = dao.insertReimbReq(request);
			RequestQueryObject fetchNewReq = dao.retrieveRequestQueryObjectById(reimbId);
			PrintWriter printer = resp.getWriter();
			printer.write(new ObjectMapper().writeValueAsString(fetchNewReq));			
		}
	}
	
	/**
	 * doPut method for updating a request object in the database.
	 */
	@Override
	public void doPut(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException{
		HttpSession session = req.getSession(false);
		if(session != null) {
			ReimbursementRequest request = new ReimbursementRequest();
			ObjectMapper mapper = new ObjectMapper();
			RequestQueryObject updateReq = mapper.readValue(req.getInputStream(), RequestQueryObject.class);
			String reqStatus = updateReq.getReqStatus();
			request.setReqId(updateReq.getReqId());
			request.setReqResolver((int)session.getAttribute("userId"));
			request.setReqStatusId(updateReq.getReqStatusId());
			int statusId = request.getReqStatusId();
			dao.updateReimbReq(request, reqStatus);
			statusDao.updateReimbStatus(statusId, reqStatus);
			RequestQueryObject fetchNewReq = dao.retrieveManagerQueryObjectById(request.getReqId());
			PrintWriter printer = resp.getWriter();
			printer.write(new ObjectMapper().writeValueAsString(fetchNewReq));
		}
	}
}
