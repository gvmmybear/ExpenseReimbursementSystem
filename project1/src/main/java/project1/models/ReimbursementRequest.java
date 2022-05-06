package project1.models;



import lombok.Data;
import java.sql.Timestamp;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data public class ReimbursementRequest {

	private int reqId; 
	private double reqAmount; 
	private Timestamp reqSubmissionTime; 
	private Timestamp reqResolvedTime; 
	private String reqDescription; 
	private String reqReceipt; 
	private int reqAuthor; 
	private int reqResolver; 
	private int reqStatusId;
	private int reqTypeId; 

	@Override
	public String toString() {
		return "ReimbursementRequest [reqId=" + reqId + ", reqAmount=" + reqAmount + ", reqSubmissionTime="
				+ reqSubmissionTime + ", reqResolvedTime=" + reqResolvedTime + ", reqDescription=" + reqDescription
				+ ", reqReceipt=" + reqReceipt + ", reqAuthor=" + reqAuthor + ", reqResolver=" + reqResolver
				+ ", reqStatusId=" + reqStatusId + ", reqTypeId=" + reqTypeId + "]";
	}
	
	
}
