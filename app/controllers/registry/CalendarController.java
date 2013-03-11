package controllers;

import models.*;
import play.data.Form;
import org.joda.time.DateTime;
import play.*;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.registry.calendar.*;
import views.html.registry.*;
import views.html.*;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import com.avaje.ebean.*;
import util.pdf.PDF;

public class CalendarController extends Controller {

  final static Form<Event> eventForm = Form.form(Event.class);

    public static Result document() {
      Logger.info("CalendarController.document() has worked");      
      Long id = new Long(33);
      Event event = Event.find.byId(id);
      Form<Event> eventForm = Form.form(Event.class).fill(event);
      return PDF.ok(formEdit.render(id, eventForm, event));     
    }

    /**
     * Checks if events ends the same day which starts
     * @param start Date
     * @param end Date
     * @return Boolean: True if ends same day
     */
    private static Boolean endsSameDay(Date start, Date end){
      Logger.info("CalendarController.endsSameDay() has worked");
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
      return dateFormat.format(start).equals(dateFormat.format(end));
    }


    /**
     * Returns list of events for calendar view
     * @param start Long Timestamp of current view start
     * @param end Long Timestamp of current view end
     * @return Result
     */
    // public static Result json(Long start, Long end) {
    //   Logger.info("CalendarController.json() has worked");
    //   Date startDate = new Date(start*1000);
    //   Date endDate = new Date(end*1000);

    //   List<Event> resultList = Event.findInDateRange(startDate, endDate);
    //   ArrayList<Map<Object, Serializable>> allEvents = new ArrayList<Map<Object, Serializable>>();
    //   DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    //   for (Event event : resultList) {
    //     Map<Object, Serializable> eventRemapped = new HashMap<Object, Serializable>();
    //     eventRemapped.put("id", event.id);
    //     eventRemapped.put("title", event.title);
    //     eventRemapped.put("start", df.format(event.start));
    //     eventRemapped.put("end", df.format(event.end));
    //     eventRemapped.put("allDay", event.allDay);
    //     eventRemapped.put("url", controllers.routes.CalendarController.edit(event.id).toString());

    //     allEvents.add(eventRemapped);
    //   }
    //   return ok(play.libs.Json.toJson(allEvents));
    // }
    public static Result json(Long start, Long end, Long doctor_id) {
        Logger.info("CalendarController.json() has worked");
        Date startDate = new Date(start*1000);
        Date endDate = new Date(end*1000);
        if(doctor_id != null) {
          Logger.info("doctor_id form JSON == " + doctor_id);
        }
        /*Form<Doctor> form = form(Doctor.class).bindFromRequest(); 
        Logger.info("form.name=="+form.name()); 
        Logger.info("form.data=="+form.data()); 
        Logger.info("form.apply(fullName)=="+form.apply("fullName").value());
        String[] fio = form.apply("fullName").value().split(" ");
        Logger.info("surname=="+fio[0]);
        Logger.info("name=="+fio[1]);
        Logger.info("patronymic=="+fio[2]);
        Logger.info(form.toString());
        String query = "find doctor where name=:name and surname=:surname and patronymic=:patronymic";  
        Doctor d = Ebean.find(Doctor.class)  
        .setQuery(query)  
        .setParameter("surname", fio[0])  
        .setParameter("name", fio[1]) 
        .setParameter("patronymic", fio[2]) 
        .findUnique(); 
        Logger.info("doctorType=="+d.doctorType.doctorTypeName);
        Logger.info("doctorFullName=="+d.getFullName());
        Logger.info("doctorId=="+d.id);*/

        // List<Event> resultList = Event.findInDateRange(startDate, endDate);
        // Long docId = new Long(1);
        List<Event> resultList = Event.findInDateRangeByDoc(startDate, endDate, doctor_id);
        Logger.info("events: "+resultList);
        ArrayList<Map<Object, Serializable>> allEvents = new ArrayList<Map<Object, Serializable>>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        for (Event event : resultList) {
            Map<Object, Serializable> eventRemapped = new HashMap<Object, Serializable>();
            eventRemapped.put("id", event.id);
            eventRemapped.put("title", event.title);
            eventRemapped.put("start", df.format(event.start));
            eventRemapped.put("end", df.format(event.end));
            eventRemapped.put("allDay", event.allDay);
            // eventRemapped.put("doctor", event.doctor);
            // eventRemapped.put("patient", event.patient);
            eventRemapped.put("url", controllers.routes.CalendarController.edit(event.id).toString());

            allEvents.add(eventRemapped);
        }
        return ok(play.libs.Json.toJson(allEvents));
    }


    /**
     * Displays full calendar
     * @return Result
     */
    public static Result calendar(Long doctorId, Long patientId) {
      Logger.info("CalendarController.calendar() has worked");
      return ok(calendar.render("Title of this calendar...", doctorId, patientId));
    }


    /**
     * List of events in table view
     * @return Result
     */
    public static Result list() {
      Logger.info("CalendarController.list() has worked");
      List<Event> events = Event.find.order().desc("start").findList();
      return ok(list.render(events));
    }


    /**
     * Displays blank form
     * @return Result
     */
    public static Result blank() {
      Logger.info("CalendarController.blank() has worked");
      return ok(formNew.render(eventForm));
    }


    /**
     * Save new event in DB (a.k.a. submit action in other examples)
     * @return Result
     */
    public static Result add() {
      Logger.info("CalendarController.add() has worked");
      Form<Event> eventForm = Form.form(Event.class).bindFromRequest();
      if (eventForm.hasErrors()) {
        return badRequest(formNew.render(eventForm));
      }

      Event newEvent = eventForm.get();

      newEvent.allDay = newEvent.allDay != null;
      if (newEvent.end == null) {
        newEvent.end = new DateTime(newEvent.start).plusMinutes(30).toDate();
      }
      newEvent.endsSameDay = endsSameDay(newEvent.start, newEvent.end);
      newEvent.save();
      return redirect(controllers.routes.CalendarController.list());
    }


    /**
     * Dislays form for editing existing event
     * @param id Long
     * @return Result
     */
    public static Result edit(Long id) {
      Logger.info("CalendarController.edit() has worked");
      Event event = Event.find.byId(id);
      return ok(eventModal.render(event));
      // Form<Event> eventForm = Form.form(Event.class).fill(event);
      // return ok(formEdit.render(id, eventForm, event));

    }


    /**
     * Save new event in DB (a.k.a. submit action in other examples)
     * @param id Long
     * @return Result
     */
    public static Result update(Long id) {
      Logger.info("CalendarController.update() has worked");
      Form<Event> eventForm = Form.form(Event.class).bindFromRequest();
      if (eventForm.hasErrors()) {
        return badRequest(formEdit.render(id, eventForm, Event.find.byId(id)));
      }
      Event updatedEvent = eventForm.get();
      updatedEvent.allDay = updatedEvent.allDay != null;
      if (updatedEvent.end == null) {
        updatedEvent.end = new DateTime(updatedEvent.start).plusMinutes(30).toDate();
      }
      updatedEvent.endsSameDay = endsSameDay(updatedEvent.start, updatedEvent.end);
      updatedEvent.update(id);
      return redirect(controllers.routes.CalendarController.list());
    }


    /**
     * Deletes event
     * @param id Long
     * @return Result
     */
    public static Result delete(Long id) {
      Logger.info("CalendarController.delete() has worked");
      Event.find.ref(id).delete();
      return redirect(controllers.routes.CalendarController.list());
    }


    /**
     * Adds event after clicking on calendar
     * @return Result
     */
    public static Result addByAjax() {
      Logger.info("CalendarController.addByAjax() has worked");
      Form<Event> eventForm = Form.form(Event.class).bindFromRequest();
      // Logger.info("form.name=="+eventForm.name()); 
      // Logger.info("form.data=="+eventForm.data()); 
      // Logger.info("form.apply(doctorId)=="+eventForm.apply("doctorId").value());
      // Logger.info("form.apply(patientId)=="+eventForm.apply("patientId").value());
      Long doctorId = new Long(eventForm.apply("doctorId").value());
      Long patientId = new Long(eventForm.apply("patientId").value());
      Doctor d = Doctor.get(doctorId);

      Patient p = Patient.get(patientId);
      // Logger.info("doctor=="+d);
      // Logger.info("doctorTypeId=="+d.doctorType);
      // Logger.info("patient=="+p);
      Event newEvent = eventForm.get();
      // Logger.info("newEvent=="+newEvent);

      newEvent.endsSameDay = endsSameDay(newEvent.start, newEvent.end);
      newEvent.doctor = d;
      newEvent.patient = p;
      newEvent.save();
      // Logger.info("newEvent.doctor=="+newEvent.doctor);

      if (eventForm.hasErrors()){
        return badRequest("There was some errors in form");
      }
      Logger.info("newEvent.id=="+newEvent.id.toString());
      Logger.info("newEvent.url=="+controllers.routes.CalendarController.edit(newEvent.id).toString());
      Map<String, String> result = new HashMap<String, String>();
      result.put("id", newEvent.id.toString());
      result.put("url", controllers.routes.CalendarController.edit(newEvent.id).toString());

      return ok(play.libs.Json.toJson(result));
    }


    /**
     * Saves in DB date changed by event drag
     * @return Result
     */
    public static Result move() {
      Logger.info("CalendarController.move() has worked");
      Long id = Long.valueOf(Form.form().bindFromRequest().get("id"));
      int dayDelta = Integer.parseInt(Form.form().bindFromRequest().get("dayDelta"));
      int minuteDelta = Integer.parseInt(Form.form().bindFromRequest().get("minuteDelta"));

      Event event = Event.find.byId(id);
      event.start = new DateTime(event.start).plusDays(dayDelta).plusMinutes(minuteDelta).toDate();
      event.end = new DateTime(event.end).plusDays(dayDelta).plusMinutes(minuteDelta).toDate();
      event.allDay = Boolean.valueOf(Form.form().bindFromRequest().get("allDay"));
      event.endsSameDay = endsSameDay(event.start, event.end);
      event.update();

      // if (thereIsSomeError){
      //   return badRequest("You can not move this event!");
      // }

      return ok("changed");
    }

    /**
     * Saves in DB end date changed by event resize
     * @return Result
     */
    public static Result resize() {
      Logger.info("CalendarController.json() has resize");
      Long id = Long.valueOf(Form.form().bindFromRequest().get("id"));
      int dayDelta = Integer.parseInt(Form.form().bindFromRequest().get("dayDelta"));
      int minuteDelta = Integer.parseInt(Form.form().bindFromRequest().get("minuteDelta"));

      Event event = Event.find.byId(id);
      event.end = new DateTime(event.end).plusDays(dayDelta).plusMinutes(minuteDelta).toDate();
      event.endsSameDay = endsSameDay(event.start, event.end);
      event.update();

      // if (thereIsSomeError){
      //   return badRequest("You can not resize this event!");
      // }

      return ok("changed");
    }

  }