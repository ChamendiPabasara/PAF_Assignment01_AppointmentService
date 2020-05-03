package com;
import java.sql.Date;

import java.sql.Date;
import java.text.SimpleDateFormat;

//For REST Service
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
//For JSON
import com.google.gson.*;

import model.Appointment;

//For XML
import org.jsoup.*;
import org.jsoup.parser.*;
import org.jsoup.nodes.Document;

import model.Appointment;

@Path("/Appointments")
public class AppointmentService {
	
	Appointment App1 = new Appointment();

	@GET
	@Path("/")
	@Produces(MediaType.TEXT_HTML)
	public String ReadAppointments() {
		
		return App1.ReadAppointments();
	}
	
	@GET
	@Path("/getAppointmentbyID/App1/{id}")
	@Produces(MediaType.TEXT_HTML)
	public String getPayment(@PathParam("id") int id) {
		System.out.println(id);

		return this.App1.getAppointmentByPatient(id);
	}	
	

}
