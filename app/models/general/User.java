package models;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

/**
 * User entity managed by Ebean
 */
@Entity 
@Table(name="account")
public class User extends Model {

  @Id
  public Long id;

  @Constraints.Required
  @Formats.NonEmpty
  public String email;
  
  @Constraints.Required
  public String name;
  
  @Constraints.Required
  public String password;

    //TODO: add password hash instead of the password
    /*
    @Constraints.Required
    @Transient
    public String password;
    public String passwordMD5;
    */

    // -- Queries
    
    public static Model.Finder<Long,User> find = new Model.Finder(Long.class, User.class);
    
    /**
     * Retrieve all users.
     */
    public static List<User> findAll() {
      return find.all();
    }

    /**
     * Retrieve a User from email.
     */
    public static User findByEmail(String email) {
      return find.where().eq("email", email).findUnique();
    }
    
    /**
     * Authenticate a User.
     */
    public static User authenticate(String email, String password) {
      return find.where()
      .eq("email", email)
      .eq("password", password)
      .findUnique();
    }

    public static void create(User user) {
      user.save();
    }

    public static void delete(Long id) {
      find.ref(id).delete();
    }

    public static User get(Long id) {
      return find.ref(id);
    }
    
    // --
    
    public String toString() {
      return "User(" + email + ")";
    }

  }