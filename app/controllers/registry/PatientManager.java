package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import models.*;
import views.html.registry.patient.*;
import views.html.registry.doctor.*;
import views.html.registry.calendar.*;
import views.html.registry.*;
import views.html.*;
import java.util.*;
import util.pdf.PDF;
import com.avaje.ebean.*;

import org.joda.time.DateTime;
import org.joda.time.format.*;

public class PatientManager extends Controller {
	public static Form<Patient> patientForm = Form.form(Patient.class);
	public static Form<Doctor> doctorForm = Form.form(Doctor.class);
	public static Result REGISTRY_HOME = PatientManager.index();

	//TODO: Delete or refactor this later. it is used for study 
	public static Result document() {
    return RuPDF.ok(document.render("Your new application is ready."));
  }
  public static Result coupon(Long id) {
    Logger.info("PatientManager.coupon() has worked");
    // response().setContentType("text/html; charset=iso-8859-5");
  	Event event = Event.find.byId(id);
    Form<Event> eventForm = Form.form(Event.class).fill(event);
    return PDF.ok(coupon.render(event));
    // return ok(coupon.render(event)).as("picture/stream");
    // return RuPDF.ok(coupon.render(event));
    // return RuPDF.ok(coupon.render(event), "iso-8859-5");
  }

  /**
    * registry home page. it is find patient page currently.
    */
	public static Result index() {
		return redirect(routes.Application.registry());
	}

	/**
	  * show all patients info
	  */
	public static Result patients() {
		return ok(patients.render(Patient.all()));
	}

	/**
	  * show patient info by id
	  */
	public static Result patient(Long id) {
		return ok(patientinfo.render(Patient.get(id)));
	}

	/**
	  * show doctor info by id
	  */
	public static Result doctor(Long id) {
		return ok(doctorinfo.render(Doctor.get(id)));
	}


	/**
	  * create new patient
	  */
	public static Result newPatient() {
		Logger.info("PatientManager.newPatient() has worked");
		Form<Patient> filledForm = Form.form(Patient.class).bindFromRequest();

		if(filledForm.hasErrors()) {
      return badRequest(addpatient.render(filledForm));
		} else {
			Patient patient = filledForm.get();
			
			//add gender:
			Long id = Long.parseLong(filledForm.apply("patient.gender").value());
			Gender gender = Gender.get(id);
			patient.gender = gender;
			
			//add identity document:
			IdentityDoc identityDoc = new IdentityDoc();
			Long id2 = Long.parseLong(filledForm.apply("identityDocType").value());
			IdentityDocType identityDocType = IdentityDocType.get(id2);
			identityDoc.identityDocType = identityDocType;
			identityDoc.number = Integer.parseInt(filledForm.apply("number").value());
			identityDoc.series = Integer.parseInt(filledForm.apply("series").value());
			DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");
			DateTime dt = formatter.parseDateTime(filledForm.apply("dateIssue").value());
			identityDoc.dateIssue = dt.toDate();
			identityDoc.whoIssue = filledForm.apply("whoIssue").value();
			
			//save in db:
			// TODO: check and debug this moment in future, maybe it is unnecessary
      // IdentityDoc.create(identityDoc);
			patient.identityDoc = identityDoc;;
			Patient.create(patient);
      return REGISTRY_HOME;
		}
	}
	public static Result newPatientPage() {
		return ok(addpatient.render(Form.form(Patient.class)));
	}

	/**
	  * delete patient by id
	  */
	public static Result deletePatient(Long id) {
		Patient.delete(id);
		return REGISTRY_HOME;
	}

	/**
	  * find patient, if succes redirect to show patient info page
	  */
	public static Result findPatient() {
		Form<Patient> filledform = Form.form(Patient.class).bindFromRequest();
		String fullName = filledform.apply("fullName").value();	
		String stringBirthday = filledform.apply("searchBirthday").value();
		String stringNumber = filledform.apply("searchIdentityNumber").value();
		String stringSeries = filledform.apply("searchIdentitySeries").value();
		Boolean simpleSearchFlag = true; 
		Integer number = 0, series = 0;
		Date birthday = new Date();
		if (stringBirthday != null && !"".equals(stringBirthday)) {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");
			DateTime dt = formatter.parseDateTime(filledform.apply("searchBirthday").value());
			birthday = dt.toDate();
			simpleSearchFlag = false;
		}
		if (stringNumber != null && !"".equals(stringNumber)) {
			number = Integer.parseInt(filledform.apply("searchIdentityNumber").value());
			simpleSearchFlag = false;
		}
		if (stringSeries != null && !"".equals(stringSeries)) {
			series = Integer.parseInt(filledform.apply("searchIdentitySeries").value());
			simpleSearchFlag = false;
		}

			Patient patient;
			if(simpleSearchFlag) {
				Logger.info("simpleSearch");
				patient = Patient.findByFullName(fullName);
			} else {
				Logger.info("advancedSearch");
				patient = Patient.advancedSearch(fullName, number, series, birthday);
			}
			
			if (patient != null) {
				return redirect(routes.PatientManager.patient(patient.id));
			} else {
				return badRequest(findpatient.render(filledform));
			}
	}

	public static Result findPatientPage() {
		return ok(findpatient.render(Form.form(Patient.class)));
	}

	/**
	  * choose doctor page
	  */
	public static Result chooseDoctor(Long id) {
		return ok(choosedoc.render(doctorForm, id));
	}

	/**
	  * doctor list
	  */
	public static Result doctorList(Long id) {
		return ok(doctorlist.render(Doctor.fioMapByType(id)));
	}

	/**
	  * make an appointent for the patient and fo the doctor
	  */
	public static Result newAppointment(Long patientId) {		
		Form<Doctor> docForm = Form.form(Doctor.class).bindFromRequest();	
		//TODO: attend to this moment String -> Long
		Long id = Long.parseLong(docForm.apply("doctorFullName").value());
		Doctor doctor = Doctor.get(id);
		return redirect(routes.CalendarController.calendar(doctor.id,patientId));
	}
}