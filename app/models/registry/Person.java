package models;

import java.util.*;
import play.db.ebean.*;
import play.data.validation.*;
import javax.persistence.*;
import com.avaje.ebean.*;
import play.data.format.Formats;

/**
 * Person entity managed by Ebean. Parent for Doctor and Patient classes.
 */
@Entity
public class Person extends Model {

	@Id
	public Long id;

	//TODO: resolve the problem of null objects when constaints are enabled
	// @Constraints.Required	
	// @Constraints.Max(35)
	public String name;

	// @Constraints.Required
	// @Constraints.Max(35)
	public String surname;

	// @Constraints.Required
	// @Constraints.Max(35)
	public String patronymic;

	// @Constraints.Required
	@OneToOne(cascade=CascadeType.ALL)
	public Gender gender;

	// @Constraints.Required
	@Formats.DateTime(pattern = "dd.MM.yyyy")
	// @Constraints.Max(12)
	public Date birthday;    

	/**
		* Generic query helper for entity Person with id Long
		*/
		public static Finder<Long, Person> find = new Finder(Long.class, Person.class);

		public static List<Person> allPersons() {
			return find.all();
		}

		public static void create(Person person) {
			person.save();
		}

		public static void delete(Long id) {
			find.ref(id).delete();
		}

		public static Person get(Long id) {
			return find.ref(id);
		}

		public String getFullName() {
			return this.surname + " " + this.name + " " + this.patronymic;
		}
	}