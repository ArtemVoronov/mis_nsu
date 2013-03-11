package models;

import com.avaje.ebean.*;
import play.data.format.Formats;
import play.data.validation.*;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Event entity managed by Ebean.
 */
@Entity
public class Event extends Model {

  @Id
  public Long id;

  @Constraints.Required
  public String title;
  public Boolean allDay;

  @Constraints.Required
  @Formats.DateTime(pattern = "dd.MM.yyyy HH:mm")
  public Date start = new Date();

  @Formats.DateTime(pattern = "dd.MM.yyyy HH:mm")
  public Date end = new Date();

  public Boolean endsSameDay;

  @OneToOne(cascade=CascadeType.ALL) 
  public Doctor doctor; 
  @OneToOne(cascade=CascadeType.ALL) 
  public Patient patient;

  /**
    * Generic query helper for entity Event with id Long
    */
  public static Finder<Long,Event> find = new Finder<Long,Event>(Long.class, Event.class);

  public Event(String title, Date start, Date end, Boolean allDay) {
    this.title = title;
    this.start = start;
    this.end = end;
    this.allDay = allDay;

  }

  public static List<Event> findInDateRange(Date start, Date end) {

    return find.where().or(
      Expr.and(
        Expr.lt("start", start),
        Expr.gt("end", end)
        ),
      Expr.or(
        Expr.and(
          Expr.gt("start", start),
          Expr.lt("start", end)
          ),
        Expr.and(
          Expr.gt("end", start),
          Expr.lt("end", end)
          )
        )
      ).findList();
  }

  public static List<Event> findInDateRangeByDoc(Date start, Date end, Long doctorId) {
    /*return find.where().or(
      Expr.and(
        Expr.lt("start", start),
        Expr.gt("end", end)
        ),
      Expr.or(
        Expr.and(
          Expr.gt("start", start),
          Expr.lt("start", end)
          ),
        Expr.and(
          Expr.gt("end", start),
          Expr.lt("end", end)
          )
        )
      ).eq("doctor_id", doctorId).findList();*/
      return find.where().eq("doctor_id", doctorId).or(
      Expr.and(
        Expr.lt("start", start),
        Expr.gt("end", end)
        ),
      Expr.or(
        Expr.and(
          Expr.gt("start", start),
          Expr.lt("start", end)
          ),
        Expr.and(
          Expr.gt("end", start),
          Expr.lt("end", end)
          )
        )
      ).findList();
    //return findInDateRange(start,end).where().eq("doctorId", doctorId).findList();
  }

  // public static List<Event> findInDateRangeByDoc(Date start, Date end, String type) {

  //   return  find.where().eq("doctorType", type).where().or(
  //     Expr.and(
  //       Expr.lt("start", start),
  //       Expr.gt("end", end)
  //       ),
  //     Expr.or(
  //       Expr.and(
  //         Expr.gt("start", start),
  //         Expr.lt("start", end)
  //         ),
  //       Expr.and(
  //         Expr.gt("end", start),
  //         Expr.lt("end", end)
  //         )
  //       )
  //     ).findList();
  // }
}
