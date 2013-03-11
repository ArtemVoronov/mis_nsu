package models;

import java.util.*;
import play.db.ebean.*;
import play.data.validation.Constraints.*;
import javax.persistence.*;
import com.avaje.ebean.*;

/**
 * DoctorType entity managed by Ebean.
 */
@Entity
public class DoctorType extends Model {
    
	@Id	
	public Long id;	
	// @OneToMany (mappedBy="doctor_type_id") 
	@OneToMany(targetEntity=Doctor.class, mappedBy="doctorType")
  public List<Doctor> doctors; 
	public String doctorTypeName;	

	/**
		* Generic query helper for entity DoctorType with id Long
		*/
	public static Finder<Long, DoctorType> find = new Finder(Long.class, DoctorType.class);

	public static List<DoctorType> all() {
		return find.all();
	}

	public static List<String> typesList() { 
		List<String> theList = new ArrayList<String>();
		for (DoctorType type : all()) {  
			theList.add(type.doctorTypeName);
		}  
		return theList;
	}

	public static Map<String, String> typesMap() { 
		Map<String, String> theMap = new HashMap<String,String>();
		for (DoctorType type : all()) {  
			theMap.put(type.id.toString(),type.doctorTypeName);
		}  
		return theMap;
	}

	public static void create(DoctorType doctorType) {
		doctorType.save();
	}

	public static void delete(Long id) {
		find.ref(id).delete();
	}

	public static DoctorType get(Long id) {
		return find.ref(id);
	}
}