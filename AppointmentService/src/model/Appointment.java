package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import config.DBconnector;

public class Appointment {
	
public String addAppointment(Date day, String time, int pid,int did,int hosID) {
		
		try(Connection con  = DBconnector.getConnection()){
			
			//Query for count AppId for duplicate date & time for same doctor and hospital
			String checkQuery="select count(appoinment_id)  from appoinment where date = ? and time = ? and doctor_doc_id = ? and hospital_hosp_id = ?";
			PreparedStatement preparedstatement = con.prepareStatement(checkQuery);
			
			preparedstatement.setDate(1,day);
			preparedstatement.setString(2,time);
			preparedstatement.setInt(3,did);
			preparedstatement.setInt(4,hosID);
			
			ResultSet newresultset = preparedstatement.executeQuery();
			
			newresultset.next();
			
			//convert count Appointment ids to integer  
			int value = Integer.parseInt(newresultset.getObject(1).toString());
			
			if(value !=0)
			{
				return "The particular time slot has been reserved please choose another a time slot.";
				
			}
			else {
				//check date is before current date 
				SimpleDateFormat  simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date(System.currentTimeMillis());

				if(day.compareTo(date)<0) {
					
					return "You cannot request past dates as appointment dates please select a future date";
				}
				
				else {
					
			String insertAppQuery = " insert into appoinment values (NULL,?, ?, ?, ?, ?)";
			PreparedStatement pstmnt = con.prepareStatement(insertAppQuery);
			
			pstmnt.setDate(1,day);
			pstmnt.setString(2,time);
			pstmnt.setInt(3,pid);
			pstmnt.setInt(4,did);
			pstmnt.setInt(5,hosID);
			

			pstmnt.execute();
			
			return "Appointment added successfully...";
				}
			}
		}
		catch(SQLException e){
			
			return "Error occured during adding an Appoinment\n" + e.getMessage();
		}
		
	}

public String getAppointmentByPatient(int id) {
	
	
	try(Connection con  = DBconnector.getConnection()){
		
		String getAppoQuery = "select a.appoinment_id, p.p_fname, p.p_lname,"
				+ " a.date, a.time, d.doc_fname, d.doc_lname, h.hosp_name "
				+ "from appoinment a "
				+ "join patient p on a.patient_patient_id = p.patient_id "
				+ "join doctor d on a.doctor_doc_id = d.doc_id "
				+ "join hospital h on a.hospital_hosp_id = h.hosp_id where patient_id = ?";
		
		PreparedStatement pstmnt = con.prepareStatement(getAppoQuery);
		
		pstmnt.setInt(1, id);
		
		String output = "<table border=\"1\"><tr><th>Appointment ID</th>"+
		 		"<th>Appointment Date</th> "+
		 		"<th>Appointment Time</th>"
		 		+ "<th>Patient Name </th>"
		 		+"<th>Doctor Name</th>"
		 		+"<th>Hospital Name</th>" ;
		
		ResultSet rs = pstmnt.executeQuery();
		
		while(rs.next()) {
			
			int AppID = rs.getInt("appoinment_id");
			Date day = rs.getDate("date");
			String time = rs.getString("time");
			String patientName = rs.getString("p_fname") + " " + rs.getString("p_lname");
			String doctor = rs.getString("doc_fname") + " " + rs.getString("doc_lname");
			String hospital = rs.getString("hosp_name");


			output += "<tr><td>" + AppID + "</td>";
			output += "<td>" + day + "</td>";
			output += "<td>" + time + "</td>";
			output += "<td>" + patientName + "</td>";
			output += "<td>" + doctor + "</td>";
			output += "<td>" + hospital + "</td>";

		}
		output += "</table>";
		
		return output;
	}
	catch(SQLException e){
		
		return "Error occur during retrieving \n" +
		
				e.getMessage();
	}
	
}

public String ReadAppointments() {
	
	
	try(Connection con  = DBconnector.getConnection()){
		
		
		LocalDate prvPaymentDate = null;
		String readQuery = "select * from appoinment";
		
		 PreparedStatement pstmt = con.prepareStatement(readQuery);
		 
		 String output = "<table border=\"1\"><tr><th>Appointment ID</th>"+
				 		"<th>Appointment Date</th> "+
				 		"<th>Appointment Time</th>"
				 		+ "<th>Patient ID</th>"
				 		+"<th>Doctor ID</th>"
				 		+"<th>Hospital ID</th></tr>";
				 		 
			
		 ResultSet rs = pstmt.executeQuery(readQuery); 
		
		 
		while(rs.next()) {
			int AppID = rs.getInt("appoinment_id");
		    Date day = rs.getDate("date");
			String time = rs.getString("time");
			int pid = rs.getInt("patient_patient_id");
			int did = rs.getInt("doctor_doc_id");
			int hosID = rs.getInt("hospital_hosp_id");
		
			

			output += "<tr><td>" + AppID + "</td>";
			output += "<td>" + day + "</td>";
			output += "<td>" + time + "</td>";
			output += "<td>" + pid + "</td>";
			output += "<td>" + did + "</td>";
			output += "<td>" + hosID + "</td>";
			

		}
	
	   output += "</table>";
		  return output;
	}
	catch(SQLException e){
		
		e.printStackTrace();
		return "Error occured during retrieving data";
	}
}


}
