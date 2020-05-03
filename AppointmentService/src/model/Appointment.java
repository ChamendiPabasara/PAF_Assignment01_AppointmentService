package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

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

}
