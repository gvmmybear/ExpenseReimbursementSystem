package project1.dao.interfaces;

import java.util.List;
import project1.models.RequestQueryObject;
import project1.models.ReimbursementRequest;

public interface RequestDao {

	public int insertReimbReq(ReimbursementRequest myRequest);
	public boolean updateReimbReq(ReimbursementRequest myRequest, String newStatus);
	public RequestQueryObject retrieveRequestQueryObjectById(int id);
	public RequestQueryObject retrieveManagerQueryObjectById(int id);
	public List<RequestQueryObject> retrieveTicketsByStatus(String queryType);
	public List<RequestQueryObject> retrieveTicketsByUserId(int userId);
	public List<ReimbursementRequest> retrieveAllReq();
}
