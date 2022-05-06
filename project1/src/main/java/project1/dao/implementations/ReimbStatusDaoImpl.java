package project1.dao.implementations;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import project1.dao.interfaces.ReimbStatusDao;
import project1.dao.MyCustomConnectionFactory;

public class ReimbStatusDaoImpl implements ReimbStatusDao{

	// CREATES NEW REIMBURSEMENT STATUS ID
	@Override
	public int insertReimbStatus() {
		// DAO Method for inserting new row into reimbursement status.
		// return reimbursement status id for further inserts on reimbursement request table.
		int statusId = -1;
		try(Connection conn = MyCustomConnectionFactory.getConnection()){
			// SQL insert statement 
			String sqlStatement = "INSERT INTO ers_reimbursement_status(reimb_status) "
					+ "VALUES('Pending') RETURNING reimb_status_id";
			PreparedStatement ps = conn.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
			
			// checks if new insert was successful.
			int updatedRows = ps.executeUpdate();
			if(updatedRows != 0) {
				ResultSet keys = ps.getGeneratedKeys();
				
				// then if insert was executed method returns status id.
				if(keys.next()) {
					statusId = keys.getInt("reimb_status_id");
					return statusId;
				}
			}
		}catch(SQLException e) {	
			e.printStackTrace();
		}
		return statusId;
	}

	// RETRIEVES REQUEST STATUS BY STATUS ID NUMBER
	@Override
	public String retrieveReimbStatusById(int statusId) {
		// establishes DB connection and executes a query for table key and status value.
		try(Connection conn = MyCustomConnectionFactory.getConnection()) {
			String sqlQuery = "SELECT * FROM ers_reimbursement_status WHERE reimb_status_id = ?"; 
			PreparedStatement ps = conn.prepareStatement(sqlQuery);
			ps.setInt(1, statusId);
			ResultSet rs = ps.executeQuery();
			
			// if query is successful, then method will return string containing the status
			// which should be pending, approved or denied.
			if(rs.next()) {
				return rs.getString("reimb_status");
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	// UPDATES REQUEST STATUS BY REQUEST STATUS ID NUMBER
	@Override
	public boolean updateReimbStatus(int statusId, String status) {
		// establishes DB connection and executes an update for reimbursement status by status id.
		try(Connection conn = MyCustomConnectionFactory.getConnection()) {
			// SQL statement preparation.
			String sql = "UPDATE ers_reimbursement_status "
					+ "SET reimb_status = ? WHERE reimb_status_id = ?"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, status);
			ps.setInt(2, statusId);
			System.out.println(status);
			int update = ps.executeUpdate();
			if(update != 0) {
				return true;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
