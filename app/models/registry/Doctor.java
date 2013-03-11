package models;

import java.util.*;
import play.db.ebean.*;
import play.data.validation.Constraints.*;
import javax.persistence.*;
import com.avaje.ebean.*; 
/**
 * Doctor entity managed by Ebean.
 */
@Entity
public class Doctor extends Person {
    
	@Id
	public Long id;	
	//@ManyToOne(targetEntity=DoctorType.class,mappedBy="id", cascade=CascadeType.ALL)
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="doctor_type_id",referencedColumnName="id")  
	public DoctorType doctorType;	

	public int officeNumber;

	public String getDoctorTypeName () {
		return this.doctorType.doctorTypeName;
	}	

	/**
		* Generic query helper for entity Doctor with id Long
		*/
	public static Finder<Long, Doctor> find = new Finder(Long.class, Doctor.class);

	public static List<Doctor> allDoctors() {
		return find.all();
	}

	public static List<String> fioList() { 
		List<String> theList = new ArrayList<String>();
		for (Doctor doctor : allDoctors()) {  
			theList.add(doctor.getFullName());
		}  
		return theList;
	}

	public static List<String> fioSQLListByType(String doctorTypeName) { 
		//TODO: check this statement at latest version of PostgreSQL
		List<String> theList = new ArrayList<String>();
		String sql = "SELECT concat(surname, ' ', name, ' ', patronymic) as fio FROM DOCTOR LEFT JOIN DOCTOR_TYPE ON DOCTOR_TYPE.ID = DOCTOR.DOCTOR_TYPE_ID WHERE DOCTOR_TYPE_NAME=:DOCTOR_TYPE_NAME";
		SqlQuery sqlQuery = Ebean.createSqlQuery(sql).setParameter("DOCTOR_TYPE_NAME", doctorTypeName);
		List<SqlRow> rows = sqlQuery.findList();
		// List<SqlRow> rows = Doctor.fioSQLListByType("Терапевт");
		for (SqlRow row : rows) {
			//Logger.info("row: "+ row);
			theList.add(row.getString("fio"));
		}
		
		return theList;
	}
//SELECT DOCTOR .id as id, concat(surname, ' ', name, ' ', patronymic) as fio, doctor_type_name as type FROM DOCTOR LEFT JOIN DOCTOR_TYPE ON DOCTOR_TYPE.ID = DOCTOR.DOCTOR_TYPE_ID
	public static Map<String, String> fioMapByType(Long doctorTypeNameId) { 
		//TODO: check this statement at latest version of PostgreSQL
		Map<String, String> theMap = new HashMap<String,String>();
		String sql = "SELECT DOCTOR.id as id, concat(surname, ' ', name, ' ', patronymic) as fio FROM DOCTOR LEFT JOIN DOCTOR_TYPE ON DOCTOR_TYPE.ID = DOCTOR.DOCTOR_TYPE_ID WHERE doctor_type.id=:type_id";
		SqlQuery sqlQuery = Ebean.createSqlQuery(sql).setParameter("type_id", doctorTypeNameId);
		List<SqlRow> rows = sqlQuery.findList();
		for (SqlRow row : rows) {			
			theMap.put(row.getString("id"),row.getString("fio"));
		}
		
		return theMap;
	}

//TODO: repair choosing doctor by typename
	public static List<String> fioListByType(String doctorTypeName) { 
		 /*String query = "find doctor fetch doctor_type.doctor_type_id";  
		 List<Doctor> doctors = Ebean.find(Doctor.class)  
     .setQuery(query)  
     .setParameter("doctor_type_id", new Long(1))  
     .findList(); */
		String sql = "SELECT * FROM DOCTOR LEFT JOIN DOCTOR_TYPE ON DOCTOR_TYPE.ID = DOCTOR.DOCTOR_TYPE_ID";
		SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
		List<SqlRow> list = sqlQuery.findList();
		/*RawSql rawSql = RawSqlBuilder.parse(sql).create(); 
		javax.persistence.Query<Doctor> query = Ebean.find(Doctor.class);  
    query.setRawSql(rawSql);
    //.columnMapping("id",  "doctor.id")
    //.columnMapping("name",  "doctor.name")
    //.columnMapping("surname",  "doctor.surname")
    //.columnMapping("patronymic",  "doctor.patronymic")
    //.columnMapping("patronymic",  "doctor_type.doctor_type_name");
    List<Doctor> doctors = query.findList();  */

		// List<Doctor> doctors = Ebean.find(Doctor.class)
		// .fetch("doctor_type","doctor.doctor_type_id")
		// .findList(); 
		//List<Doctor> doctors = find.fetch("doctor_type")  
      //.where().eq("doctor.doctor_type_name", doctorTypeName)
     // .findList();  
    /*com.avaje.ebean.Query q = Ebean.createQuery(Doctor.class);
		q.join("doctor_type");	
		final List<Doctor> eventList = q.findList();*/
		 List<Doctor> doctors =  find.where().eq("doctor_type_id", new Long(1)).findList();
		List<String> theList = new ArrayList<String>();
		for (Doctor doctor : doctors) {  
			theList.add(doctor.getFullName());
		}  
		return theList;
	}

	public static Doctor findByFullName(String fullName) {
  	String[] fio = fullName.split(" ");
  	String query = "find doctor where name=:name and surname=:surname and patronymic=:patronymic";  
		/*Doctor doctor = Ebean.find(Doctor.class).setQuery(query).setParameter("surname", fio[0])
		.setParameter("name", fio[1]).setParameter("patronymic", fio[2]).findUnique(); */
		Doctor doctor = find.setQuery(query).setParameter("surname", fio[0])
		.setParameter("name", fio[1]).setParameter("patronymic", fio[2]).findUnique(); 
		return doctor;
  }

	public static void create(Doctor doctor) {
		doctor.save();
	}

	public static void delete(Long id) {
		find.ref(id).delete();
	}

	public static Doctor get(Long id) {
		return find.ref(id);
	}
}