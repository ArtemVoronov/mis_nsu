package models;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import play.libs.Crypto;

import java.math.BigInteger;
import java.security.MessageDigest;

// import org.apache.commons.*;
import org.apache.commons.codec.digest.DigestUtils;

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
  @Transient
  // @Password
  public String password;
  public String passwordMD5;

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
  // public static User authenticate(String email, String password) {
  //   return find.where()
  //   .eq("email", email)
  //   .eq("password", password)
  //   .findUnique();
  // }

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

  public void setEmail(String email) {
    this.email = email;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPassword(String password) {
    this.password = password;
    this.passwordMD5 = DigestUtils.md5Hex(password);
  }

  public static boolean isValidLogin(String username, String password) {
    // return TRUE if there is a single matching username/passwordHash
    if (authenticate(username, password) != null) return true;
    else return false;
  }

  /**
   * Authenticate a User.
   */
  public static User authenticate(String email, String password) {
    return find.where()
    .eq("email", email)
    .eq("passwordMD5", DigestUtils.md5Hex(password))
    .findUnique();
  }
}