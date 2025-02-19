package in.co.rays.project_3.dto;

public class CustomerDTO extends BaseDTO{

	private String clienName;
	private String location;
	private String contactNo;
	private long importance;
	
	public String getClienName() {
		return clienName;
	}

	public void setClienName(String clienName) {
		this.clienName = clienName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public long getImportance() {
		return importance;
	}

	public void setImportance(long importance) {
		this.importance = importance;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
