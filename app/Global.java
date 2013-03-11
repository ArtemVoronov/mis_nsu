import play.*;
import play.libs.*;

import java.util.*;

import com.avaje.ebean.*;

import models.*;

public class Global extends GlobalSettings {

  @Override
  public void onStart(Application app) {
    Logger.info("Application has started");
    InitialData.insert(app);
  }

  @Override
  public void onStop(Application app) {
    Logger.info("Application shutdown...");
  } 
  
  static class InitialData {

    public static void insert(Application app) {
      if(Ebean.find(User.class).findRowCount() == 0) {

        Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial-data.yml");

        // Insert users first
        Ebean.save(all.get("users"));
        // Insert patients
        Ebean.save(all.get("patients"));
        //basic gender type (see ISO/IEC 5218)
        Ebean.save(all.get("genders"));
        //basic doctor types
        Ebean.save(all.get("doctorTypes"));
        // Insert doctors
        Ebean.save(all.get("doctors"));
        // Insert events
        // Ebean.save(all.get("events"));
        // Insert organizations
        Ebean.save(all.get("organizations"));
        // Insert identity doc types
        Ebean.save(all.get("identityDocTypes"));
        // Insert identity docs
        Ebean.save(all.get("identityDocs"));
      }
    }
    
  }
  
}