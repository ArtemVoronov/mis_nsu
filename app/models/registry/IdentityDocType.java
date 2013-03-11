package models;

import java.util.*;
import play.db.ebean.*;
import play.data.validation.Constraints.*;
import javax.persistence.*;
import com.avaje.ebean.*;

/**
 * IdentityDocType entity managed by Ebean.
 */
@Entity
public class IdentityDocType extends Model {
    
	@Id	
	public Long id;	 
	@OneToMany(targetEntity=IdentityDoc.class, mappedBy="identityDocType")
  public List<IdentityDoc> identityDocs; 
	public String identityDocTypeName;	

	/**
		* Generic query helper for entity IdentityDocType with id Long
		*/
	public static Finder<Long, IdentityDocType> find = new Finder(Long.class, IdentityDocType.class);

	public static List<IdentityDocType> all() {
		return find.all();
	}

	public static List<String> typesList() { 
		List<String> theList = new ArrayList<String>();
		for (IdentityDocType type : all()) {  
			theList.add(type.identityDocTypeName);
		}  
		return theList;
	}

	public static Map<String, String> typesMap() { 
		Map<String, String> theMap = new HashMap<String,String>();
		for (IdentityDocType type : all()) {  
			theMap.put(type.id.toString(),type.identityDocTypeName);
		}  
		return theMap;
	}

	public static void create(IdentityDocType identityDocType) {
		identityDocType.save();
	}

	public static void delete(Long id) {
		find.ref(id).delete();
	}

	public static IdentityDocType get(Long id) {
		return find.ref(id);
	}
}