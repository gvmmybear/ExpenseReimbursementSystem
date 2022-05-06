package project1.dao.interfaces;


public interface ReimbStatusDao {
	public int insertReimbStatus();
	public String retrieveReimbStatusById(int statusId);
	public boolean updateReimbStatus(int statusId, String status);
}
