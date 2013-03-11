package models;

import java.util.*;
import play.db.ebean.*;
import play.data.validation.Constraints.*;
import javax.persistence.*;
import com.avaje.ebean.*; 

/**
 * Organization entity managed by Ebean.
 */
@Entity
public class Organization extends Model {

	@Id
	public Long id;	
	public String name;

	/**
		* Generic query helper for entity Organization with id Long
		*/
	public static Finder<Long, Organization> find = new Finder(Long.class, Organization.class);

	public static List<Organization> all() {
		return find.all();
	}

	public static List<String> allNames() { 
		List<String> theList = new ArrayList<String>();
		for (Organization organization : all()) {  
			theList.add(organization.name);
		}  
		return theList;
	}

	public static void create(Organization organization) {
		organization.save();
	}

	public static void delete(Long id) {
		find.ref(id).delete();
	}

	public static Organization get(Long id) {
		return find.ref(id);
	}
}