package project1.dao.implementations;

import java.util.List;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import project1.dao.interfaces.RequestDao;
import project1.models.RequestQueryObject;
import project1.models.ReimbursementRequest;
import project1.dao.MyCustomConnectionFactory;

public class RequestDaoImpl implements RequestDao{
	// reimbursement status DAO 
	static ReimbStatusDaoImpl statusDao = new ReimbStatusDaoImpl();
	
	// Creates new reimbursement request
	@Override 
	public int insertReimbReq(ReimbursementRequest myRequest) {
		try(Connection conn = MyCustomConnectionFactory.getConnection()){
			// SQL insert statement 
			String sqlStatement = sqlInsertStatement();
			int statusId = generateStatusId();
			PreparedStatement ps = conn.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
			ps.setDouble(1, myRequest.getReqAmount());
			ps.setString(2, myRequest.getReqDescription());
			ps.setInt(3, myRequest.getReqAuthor());
			ps.setInt(4, statusId);
			ps.setInt(5, myRequest.getReqTypeId());
			// checks if new insert was successful.
			int updatedRows = ps.executeUpdate();
			if(updatedRows != 0) {
				ResultSet keys = ps.getGeneratedKeys();
				if(keys.next()) {
					statusId = keys.getInt("reimb_id");
					return statusId;
				}
			}
		}catch(SQLException e) {	
			e.printStackTrace();
		}
		return -1;
	}

	// Retrieves request object by ID number
	@Override
	public RequestQueryObject retrieveRequestQueryObjectById(int id) {
		try(Connection conn = MyCustomConnectionFactory.getConnection()){
			// SQL query statement 
			String sqlStatement = sqlSingleRequestQueryEmployee();
			PreparedStatement ps = conn.prepareStatement(sqlStatement);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return makeTempQueryObject(rs);
		}catch(SQLException e) {	
			e.printStackTrace();
		}
		return null;
	}
	
	// Retrieves a single request object by ID number and returns special type of 
	// RequestQueryObject for manager account type.
	@Override
	public RequestQueryObject retrieveManagerQueryObjectById(int id) {
		try(Connection conn = MyCustomConnectionFactory.getConnection()){
			// SQL query statement 
			String sqlStatement = sqlSingleRequestQueryManager();
			PreparedStatement ps = conn.prepareStatement(sqlStatement);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return makeManagerQueryObject(rs);
		}catch(SQLException e) {	
			e.printStackTrace();
		}
		return null;
	}
	
	// Retrieves all request objects by their status.
	@Override
	public List<RequestQueryObject> retrieveTicketsByStatus(String byStatus) {
		List<RequestQueryObject> queryList = new ArrayList<>();
		RequestQueryObject queryObject;
		
		try(Connection conn = MyCustomConnectionFactory.getConnection()){
			// SQL query statement 
			String sqlStatement = sqlQueryStatement(byStatus);
			PreparedStatement ps = conn.prepareStatement(sqlStatement);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				queryObject = makeTempQueryObject(rs);
				queryList.add(queryObject);
			}
		}catch(SQLException e) {	
			e.printStackTrace();
		}
		return queryList;
	}
	
	// Retrieves all requests by User ID number
	@Override
	public List<RequestQueryObject> retrieveTicketsByUserId(int userId) {
		List<RequestQueryObject> queryList = new ArrayList<>();
		RequestQueryObject queryObject;
		
		try(Connection conn = MyCustomConnectionFactory.getConnection()){
			// SQL query statement 
			String sqlStatement = sqlQueryByUserId(userId);
			PreparedStatement ps = conn.prepareStatement(sqlStatement);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				queryObject = makeTempQueryObject(rs);
				queryList.add(queryObject);
			}
		}catch(SQLException e) {	
			e.printStackTrace();
		}
		return queryList;
	}
	
	// Retrieves all requests stored in database and returns Manager RequestQueryObject
	public List<RequestQueryObject> managerQuery(){
		List<RequestQueryObject> queryList = new ArrayList<>();
		RequestQueryObject queryObject;
		
		try(Connection conn = MyCustomConnectionFactory.getConnection()){
			// SQL query statement 
			String sqlStatement = managerQueryStatement();
			PreparedStatement ps = conn.prepareStatement(sqlStatement);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				queryObject = makeManagerQueryObject(rs);
				System.out.println(queryObject);
				queryList.add(queryObject);
			}
		}catch(SQLException e) {	
			e.printStackTrace();
		}
		return queryList;
	}
	
	// Retrieves all reimbursement requests
	@Override
	public List<ReimbursementRequest> retrieveAllReq() {
		List<ReimbursementRequest> requestList = new ArrayList<>();
		ReimbursementRequest tempRR;
		
		try(Connection conn = MyCustomConnectionFactory.getConnection()){
			// SQL query statement 
			String sqlStatement = "SELECT * FROM ers_reimbursement";
			PreparedStatement ps = conn.prepareStatement(sqlStatement);
			ResultSet rs = ps.executeQuery();
			
			// while result set from query has rows, we make new temp request object
			// and add to the return list of ReimbursementRequests.
			while(rs.next()) {
				tempRR = makeTempRequestObject(rs);
				requestList.add(tempRR);
			}
		}catch(SQLException e) {	
			e.printStackTrace();
		}
		return requestList;
	}

	// Updates ReimbursementRequest object
	@Override 
	public boolean updateReimbReq(ReimbursementRequest myRequest, String newStatus) {
		try(Connection conn = MyCustomConnectionFactory.getConnection()){
			// SQL UPDATE statement 
			String sqlStatement = sqlUpdateStatement();
			PreparedStatement ps = conn.prepareStatement(sqlStatement);
			System.out.println(myRequest.getReqResolver());
			System.out.println(myRequest.getReqId());
			ps.setInt(1, myRequest.getReqResolver());
			ps.setInt(2, myRequest.getReqId());
			statusDao.updateReimbStatus(myRequest.getReqId(), newStatus);
			
			// checks if new insert was successful.
			int updatedRows = ps.executeUpdate();
			if(updatedRows != 0) {
				return true;
			}
		}catch(SQLException e) {	
			e.printStackTrace();
		}
		return false;
	}
	
	//  Generates a new reimbursement status id and returns new Key ID number.
	private static int generateStatusId() {
		return statusDao.insertReimbStatus();
	}
	
	/** Private class method for making a temporary request object and returning it
	 * as a singular object or in a List of these object types.
	 * @param rs
	 * @return
	 */
	private static ReimbursementRequest makeTempRequestObject(ResultSet rs) {
		ReimbursementRequest rr = new ReimbursementRequest();
		try {
			rr.setReqId(rs.getInt("reimb_id"));	// 1
			rr.setReqAmount(rs.getDouble("reimb_amount")); // 2
			rr.setReqSubmissionTime(rs.getTimestamp("reimb_submitted")); // 3
			rr.setReqResolvedTime(rs.getTimestamp("reimb_resolved")); // 4
			rr.setReqDescription(rs.getString("reimb_description")); // 5
			rr.setReqAuthor(rs.getInt("reimb_author")); // 6
			rr.setReqResolver(rs.getInt("reimb_resolver")); // 7
			rr.setReqStatusId(rs.getInt("reimb_status_id")); // 8
			rr.setReqTypeId(rs.getInt("reimb_type_id")); // 9
			// receipt is not currently added to object
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("rs extraction failed!");
		}
		return rr;
	}
	
	/** Private class method which creates temporary objects for a special 
	 * RequestQueryObject for an employee account type. 
	 * @param rs
	 * @return
	 */
	private static RequestQueryObject makeTempQueryObject(ResultSet rs) {
		RequestQueryObject queryObject = new RequestQueryObject();
		try {
			Timestamp sub = rs.getTimestamp("reimb_submitted");
			Timestamp res = rs.getTimestamp("reimb_resolved");
			String submitted = sub == null ? "" : sub.toString();
			String resolved = res == null ? "" : res.toString();
			queryObject.setReqId(rs.getInt("reimb_id"));
			queryObject.setReqAmount(rs.getDouble("reimb_amount"));
			queryObject.setReqSubmissionTime(submitted);
			queryObject.setReqResolvedTime(resolved);
			queryObject.setReqDescription(rs.getString("reimb_description"));
			queryObject.setReqType(rs.getString("reimb_type"));
			queryObject.setReqStatus(rs.getString("reimb_status"));
			queryObject.setResolverFirstName(rs.getString("resolver_first_name"));
			queryObject.setResolverLastName(rs.getString("resolver_last_name"));
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("rs extraction failed!");
		}
		return queryObject;
	}
	
	/** Private class method which creates temporary objects for a special 
	 * RequestQueryObject for a manager account type. 
	 * @param rs
	 * @return
	 */
	private static RequestQueryObject makeManagerQueryObject(ResultSet rs) {
		RequestQueryObject queryObject = new RequestQueryObject();
		try {
			Timestamp sub = rs.getTimestamp("reimb_submitted");
			Timestamp res = rs.getTimestamp("reimb_resolved");
			String submitted = sub == null ? "" : sub.toString();
			String resolved = res == null ? "" : res.toString();
			queryObject.setReqId(rs.getInt("reimb_id"));
			queryObject.setReqAmount(rs.getDouble("reimb_amount"));
			queryObject.setReqSubmissionTime(submitted);
			queryObject.setReqResolvedTime(resolved);
			queryObject.setReqDescription(rs.getString("reimb_description"));
			queryObject.setReqType(rs.getString("reimb_type"));
			queryObject.setReqStatus(rs.getString("reimb_status"));
			queryObject.setRequesterFirstName(rs.getString("author_first_name"));
			queryObject.setRequesterLastName(rs.getString("author_last_name"));
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("rs extraction failed!");
		}
		return queryObject;
	}
	
	/** Private class method for an insert SQL statement
	 * @return String containing an SQL INSERT statement.
	 */
	private static String sqlInsertStatement() {
		String sqlStatement = "INSERT INTO ers_reimbursement("
				+ "reimb_amount, reimb_submitted, reimb_description, " 
				+ "reimb_author, reimb_status_id, reimb_type_id) " 
				+ "VALUES(?, current_timestamp, ?, " 
				+ "?, ?, ?)";
		return sqlStatement;
	} 
	
	/** Private class method for querying requests by their status.
	 * @param byStatus the status parameter to query requests by.
	 * @return String containing an SQL query statement.
	 */
	private static String sqlQueryStatement(String byStatus) {
		String sqlStatement = ""
				+ "SELECT ers_reimbursement.reimb_id"
				+ "	, ers_reimbursement.reimb_amount"
				+ "	, ers_reimbursement.reimb_submitted"
				+ "	, ers_reimbursement.reimb_resolved"
				+ "	, ers_reimbursement.reimb_description"
				+ "	, ers_reimbursement_type.reimb_type"
				+ "	, ers_reimbursement_status.reimb_status"
				+ "	FROM ers_reimbursement"
				+ "	JOIN ers_reimbursement_status"
				+ "	ON ers_reimbursement.reimb_status_id = ers_reimbursement_status.reimb_status_id"
				+ "	JOIN ers_reimbursement_type"
				+ "	ON ers_reimbursement.reimb_type_id = ers_reimbursement_type.reimb_type_id"
				+ "	WHERE reimb_status = '" + byStatus +"'";
		return sqlStatement;
	}
	
	/** Private class method for query requests by User ID number
	 * @param userId
	 * @return String containing an SQL query statement.
	 */
	private static String sqlQueryByUserId(int userId) {
		String sqlStatement = "SELECT ers_reimbursement.reimb_id\r\n"
				+ "	, ers_reimbursement.reimb_amount\r\n"
				+ "	, ers_reimbursement.reimb_submitted \r\n"
				+ "	, ers_reimbursement.reimb_resolved \r\n"
				+ "	, ers_reimbursement.reimb_description \r\n"
				+ "	, ers_reimbursement_type.reimb_type\r\n"
				+ "	, ers_reimbursement_status.reimb_status \r\n"
				+ "	, ers_reimbursement.reimb_resolver  \r\n"
				+ "	, ers_users.user_first_name AS resolver_first_name\r\n"
				+ "	, ers_users.user_last_name  AS resolver_last_name\r\n"
				+ "	FROM ers_reimbursement\r\n"
				+ "	JOIN ers_reimbursement_status \r\n"
				+ "	ON ers_reimbursement.reimb_status_id = ers_reimbursement_status.reimb_status_id\r\n"
				+ "	JOIN ers_reimbursement_type\r\n"
				+ "	ON ers_reimbursement.reimb_type_id = ers_reimbursement_type.reimb_type_id \r\n"
				+ "	FULL OUTER JOIN ers_users \r\n"
				+ "	ON ers_reimbursement.reimb_resolver = ers_users.ers_users_id\r\n"
				+ "	WHERE ers_reimbursement.reimb_author =" + userId 
				+ "\r\n ORDER BY reimb_id";
		
		return sqlStatement;
	}
	
	/** Private class method for special manager query, which will return request author's
	 * account information
	 * @return String containing special manager query SQL statement.
	 */
	private static String managerQueryStatement() {
		String sqlStatement = "SELECT ers_reimbursement.reimb_id\r\n"
				+ "	, ers_reimbursement.reimb_amount\r\n"
				+ "	, ers_reimbursement.reimb_submitted \r\n"
				+ "	, ers_reimbursement.reimb_resolved \r\n"
				+ "	, ers_reimbursement.reimb_description \r\n"
				+ "	, ers_reimbursement_type.reimb_type\r\n"
				+ "	, ers_reimbursement_status.reimb_status \r\n"
				+ "	, ers_reimbursement.reimb_status_id \r\n"
				+ "	, ers_reimbursement.reimb_resolver  \r\n"
				+ "	, ers_users.user_first_name AS author_first_name\r\n"
				+ "	, ers_users.user_last_name  AS author_last_name\r\n"
				+ "	FROM ers_reimbursement\r\n"
				+ "	JOIN ers_reimbursement_status \r\n"
				+ "	ON ers_reimbursement.reimb_status_id = ers_reimbursement_status.reimb_status_id\r\n"
				+ "	JOIN ers_reimbursement_type\r\n"
				+ "	ON ers_reimbursement.reimb_type_id = ers_reimbursement_type.reimb_type_id \r\n"
				+ "	JOIN ers_users \r\n"
				+ "	ON ers_reimbursement.reimb_author = ers_users.ers_users_id \r\n"
				+ " ORDER BY reimb_id;";
		return sqlStatement;
	}
	
	/** Private class method for an SQL query statement to query 1 request.
	 * @return String containing an SQL query statement.
	 */
	private static String sqlSingleRequestQueryEmployee() {
		String sqlStatement = "SELECT ers_reimbursement.reimb_id\r\n"
				+ "	, ers_reimbursement.reimb_amount\r\n"
				+ "	, ers_reimbursement.reimb_submitted \r\n"
				+ "	, ers_reimbursement.reimb_resolved \r\n"
				+ "	, ers_reimbursement.reimb_description \r\n"
				+ "	, ers_reimbursement_type.reimb_type\r\n"
				+ "	, ers_reimbursement_status.reimb_status \r\n"
				+ "	, ers_reimbursement.reimb_resolver  \r\n"
				+ "	, ers_users.user_first_name AS resolver_first_name\r\n"
				+ "	, ers_users.user_last_name  AS resolver_last_name\r\n"
				+ "	FROM ers_reimbursement\r\n"
				+ "	JOIN ers_reimbursement_status \r\n"
				+ "	ON ers_reimbursement.reimb_status_id = ers_reimbursement_status.reimb_status_id\r\n"
				+ "	JOIN ers_reimbursement_type\r\n"
				+ "	ON ers_reimbursement.reimb_type_id = ers_reimbursement_type.reimb_type_id \r\n"
				+ "	FULL OUTER JOIN ers_users \r\n"
				+ "	ON ers_reimbursement.reimb_resolver = ers_users.ers_users_id\r\n"
				+ "	WHERE ers_reimbursement.reimb_id= ?";
		return sqlStatement;
	}
	
	/** Private class method for returning a special manager account type query.
	 * @return String containing special SQL query statmene for manager account type.
	 */
	private static String sqlSingleRequestQueryManager() {
		String sqlStatement = "SELECT ers_reimbursement.reimb_id\r\n"
				+ "	, ers_reimbursement.reimb_amount\r\n"
				+ "	, ers_reimbursement.reimb_submitted \r\n"
				+ "	, ers_reimbursement.reimb_resolved \r\n"
				+ "	, ers_reimbursement.reimb_description \r\n"
				+ "	, ers_reimbursement_type.reimb_type\r\n"
				+ "	, ers_reimbursement_status.reimb_status \r\n"
				+ "	, ers_reimbursement.reimb_resolver  \r\n"
				+ "	, ers_users.user_first_name AS author_first_name\r\n"
				+ "	, ers_users.user_last_name  AS author_last_name\r\n"
				+ "	FROM ers_reimbursement\r\n"
				+ "	JOIN ers_reimbursement_status \r\n"
				+ "	ON ers_reimbursement.reimb_status_id = ers_reimbursement_status.reimb_status_id\r\n"
				+ "	JOIN ers_reimbursement_type\r\n"
				+ "	ON ers_reimbursement.reimb_type_id = ers_reimbursement_type.reimb_type_id \r\n"
				+ "	JOIN ers_users \r\n"
				+ "	ON ers_reimbursement.reimb_author = ers_users.ers_users_id\r\n"
				+ "	WHERE ers_reimbursement.reimb_id= ?";
		return sqlStatement;
	}
	
	/** Private class method for an SQL UPDATE statement.
	 * @return String containing an SQL UPDATE statement
	 */
	private static String sqlUpdateStatement() {
		String sqlStatement = "UPDATE ers_reimbursement "
				+ "SET reimb_resolved = current_timestamp, reimb_resolver = ? " 
				+ "WHERE reimb_id = ?";
		return sqlStatement; // 1 resolver, 2 id
	}
}
