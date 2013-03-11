package models;

import java.util.*;
import play.db.ebean.*;
import play.data.validation.Constraints.*;
import javax.persistence.*;
import com.avaje.ebean.*;

import play.Logger;

/**
 * Patient entity managed by Ebean.
 */
@Entity
public class Patient extends Person {
  /**
   * student number, or other id from outer systems
   */ 
	public String medOrg;
	public String insuranceNum;
	public String benefitCode;
	public String snils;  
  public String residenceAddress;
  public String currentAddress;
  public String homePhone;
  public String workPhone;
  public String disable;
  public String workPlace;
  public String dependent;
  public String blood;  
  public Boolean isAddressTheSame;

  @OneToOne(cascade=CascadeType.ALL) 
  public IdentityDoc identityDoc; 

	/**
		* Generic query helper for entity Patient with id Long
		*/
	public static Finder<Long, Patient> find = new Finder(Long.class, Patient.class);

	public static List<Patient> all() {
		return find.all();
	}

  public static List<String> fioList() { 
    List<String> theList = new ArrayList<String>();
    for (Patient patient : all()) {  
      theList.add("'"+patient.getFullName()+"'");
    }  
    return theList;
  }

	public static void create(Patient patient) {
		patient.save();
	}

	public static void delete(Long id) {
		find.ref(id).delete();
	}

	public static Patient get(Long id) {
		return find.ref(id);
	}

  public static Patient advancedSearch(String fullName, int number, int series, Date birthday) {
    if((fullName == null || "".equals(fullName) && (number==0 || series==0))) {
      return null;
    } else {
      if(number!=0 && series!=0) {
        Logger.info("number====" + number);
        if (fullName != null && !"".equals(fullName)) {
          Logger.info("findByFullNameAndIdentityDoc");
          return findByFullNameAndIdentityDoc(fullName, number, series); 
        } else {
          Logger.info("findByIdentityDoc");
          return findByIdentityDoc(number, series); 
        }                
      } else if(birthday!=null && fullName != null && !"".equals(fullName)) {
        Logger.info("findByFullNameAndBirthday");
        return findByFullNameAndBirthday(fullName, birthday);
      } else if (fullName != null && !"".equals(fullName)) {
        Logger.info("findByFullName");
        return findByFullName(fullName);
      } else {
        return null;
      }
    }

  }

  public static Patient findByFullName(String fullName) {
    String[] fio = fullName.split(" ");
    String query = "find patient where name=:name and surname=:surname and patronymic=:patronymic";  
    Patient patient = find.setQuery(query)  
    .setParameter("surname", fio[0])
    .setParameter("name", fio[1])
    .setParameter("patronymic", fio[2])
    .findUnique(); 
    return patient;
  }

  public static Patient findByIdentityDoc(int number, int series) { 
    //TODO: check this statement at latest version of PostgreSQL
    String sql = "SELECT * FROM PATIENT LEFT JOIN IDENTITY_DOC ON PATIENT.IDENTITY_DOC_ID  = IDENTITY_DOC.ID WHERE number=:number and series=:series";
    SqlQuery sqlQuery = Ebean.createSqlQuery(sql)
    .setParameter("number", number)
    .setParameter("series", series);
    List<SqlRow> rows = sqlQuery.findList();
    if(!rows.isEmpty()) {
      Long patientId = rows.get(0).getLong("id");
      return Patient.get(patientId);
    } else {
      return null;
    }
  }

  public static Patient findByFullNameAndBirthday(String fullName, Date birthday) { 
    //TODO: check this statement at latest version of PostgreSQL
    String sql = "select * from patient where name=:name and surname=:surname and patronymic=:patronymic and cast(birthday as date)=:birthday";
    String[] fio = fullName.split(" ");
    SqlQuery sqlQuery = Ebean.createSqlQuery(sql)    
    .setParameter("surname", fio[0])
    .setParameter("name", fio[1])
    .setParameter("patronymic", fio[2])
    .setParameter("birthday", birthday);
    List<SqlRow> rows = sqlQuery.findList();
    if(!rows.isEmpty()) {
      Long patientId = rows.get(0).getLong("id");
      return Patient.get(patientId);
    } else {
      return null;
    }
  }

  public static Patient findByFullNameAndIdentityDoc(String fullName, int number, int series) { 
    //TODO: check this statement at latest version of PostgreSQL
    String sql = "SELECT * FROM PATIENT LEFT JOIN IDENTITY_DOC ON PATIENT.IDENTITY_DOC_ID  = IDENTITY_DOC.ID WHERE number=:number and series=:series and name=:name and surname=:surname and patronymic=:patronymic";
    String[] fio = fullName.split(" ");
    SqlQuery sqlQuery = Ebean.createSqlQuery(sql)
    .setParameter("number", number)
    .setParameter("series", series)
    .setParameter("surname", fio[0])
    .setParameter("name", fio[1])
    .setParameter("patronymic", fio[2]);
    List<SqlRow> rows = sqlQuery.findList();
    if(!rows.isEmpty()) {
      Long patientId = rows.get(0).getLong("id");
      return Patient.get(patientId);
    } else {
      return null;
    }
  }
}