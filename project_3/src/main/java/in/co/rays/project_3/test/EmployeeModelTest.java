package in.co.rays.project_3.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import in.co.rays.project_3.dto.EmployeeDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.EmployeeModelHibImp;
import in.co.rays.project_3.model.EmployeeModelInt;

public class EmployeeModelTest {

	public static EmployeeModelInt model=new EmployeeModelHibImp();

	public static void main(String[] args) throws Exception {
		addTest();
		//deleteTest();
		//updateTest();
		//findByPKTest();
		//findByNameTest();	
	}
	
	public static void addTest() throws Exception {
		// TODO Auto-generated method stub
		 EmployeeDTO dto = new EmployeeDTO();
		 SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			
	     dto.setId(1L);
	     dto.setName("Davv");
	     dto.setUserName("de@gmail.com");
	     dto.setPassword("123");
	     dto.setDob(sdf.parse("13-05-1999"));
	     dto.setContactNo("9874563210");
	     long pk = model.add(dto); 
	     System.out.println(pk + "data successfully insert");
	}
	
}
