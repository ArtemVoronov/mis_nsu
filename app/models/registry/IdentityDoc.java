package models;

import java.util.*;
import play.db.ebean.*;
import play.data.validation.Constraints.*;
import javax.persistence.*;
import com.avaje.ebean.*;
import play.data.format.Formats;
/**
 * IdentityDoc entity managed by Ebean.
 */
@Entity
public class IdentityDoc extends Model {
    
  @Id 
  public Long id;  

  // @OneToOne(cascade=CascadeType.ALL) 
  @ManyToOne(cascade=CascadeType.ALL)
  @JoinColumn(name="identity_doc_type_id",referencedColumnName="id")  
  public IdentityDocType identityDocType; 

  public int number; 
  public int series;
  @Formats.DateTime(pattern = "dd.MM.yyyy")
  public Date dateIssue;
  public String whoIssue;
  /**
    * Generic query helper for entity IdentityDoc with id Long
    */
  public static Finder<Long, IdentityDoc> find = new Finder(Long.class, IdentityDoc.class);

  public static List<IdentityDoc> all() {
    return find.all();
  }

  public static void create(IdentityDoc identityDoc) {
    identityDoc.save();
  }

  public static void delete(Long id) {
    find.ref(id).delete();
  }

  public static IdentityDoc get(Long id) {
    return find.ref(id);
  }
}