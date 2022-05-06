package project1.models;

import lombok.Data;

@Data public class UserAccount {
	
	private int userId;
	private int userRoleId;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
}
