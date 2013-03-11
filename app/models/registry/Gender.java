package models;

import java.util.*;
import play.db.ebean.*;
import play.data.validation.*;
import javax.persistence.*;

/**
 * Gender entity managed by Ebean. 
 * ISO/IEC 5218 Information technology — Codes for the representation of human 
 * sexes is an international standard that defines a representation of human 
 * sexes through a language-neutral single-digit code. It can be used 
 *in information systems such as database applications.
 * The four codes specified in ISO/IEC 5218 are:
 * 0 = not known,
 * 1 = male,
 * 2 = female,
 * 9 = not applicable.
 */
@Entity
public class Gender extends Model {
    
	@Id
	public Long id;
	// @Constraints.Required
  public Short genderCode;
	/**
		* Generic query helper for entity Gender with id Long
		*/
	public static Finder<Long, Gender> find = new Finder(Long.class, Gender.class);

	public static List<Gender> all() {
		return find.all();
	}

	public static void create(Gender gender) {
		gender.save();
	}

	public static void delete(Long id) {
		find.ref(id).delete();
	}

	public static Gender get(Long id) {
		return find.ref(id);
	}
}