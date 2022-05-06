package project1.models;

import lombok.Data;

@Data public class RequestQueryObject {
	
	private int reqId; 
	private int reqStatusId;
	private double reqAmount; 
	private String reqType; 
	private String reqStatus;
	private String requesterFirstName;
	private String requesterLastName;
	private String resolverFirstName;
	private String resolverLastName;
	private String reqDescription; 
	private String reqSubmissionTime; 
	private String reqResolvedTime; 
	
	@Override
	public String toString() {
		return "RequestQueryObject [reqId=" + reqId + ", reqAmount=" + reqAmount + ", reqSubmissionTime="
				+ reqSubmissionTime + ", reqResolvedTime=" + reqResolvedTime + ", reqDescription=" + reqDescription
				+ ", reqType=" + reqType + ", reqStatus=" + reqStatus + ", resolverFirstName=" + resolverFirstName
				+ ", resolverLastName=" + resolverLastName + "]";
	}	
}
