package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import models.*;
// import views.html.registry.*;
// import views.html.homepage.*;
// import views.html.*;
import views.html.*;
import views.html.homepage.*;
import views.html.registry.*;

public class Application extends Controller {
  /**
    * This result directly redirect to application home.
    */
  public static final Result HOME = redirect(routes.Application.homepage());
  /**
    * login form - wrapper of Login class.
    */
  public static Form<Login> logoForm = Form.form(Login.class);

  public static Result index() {
    return HOME;
  }

  public static Result homepage() {
    return ok(homepage.render());
  }

  public static Result about() {
    return ok(about.render(session("username")));
  }

  public static Result contacts() {
    return ok(contacts.render(session("username")));
  }
  
  public static Result registry() {
    return ok(registry.render(session("username")));
  }

  public static Result todoPage() {
    return TODO;
  }

  public static Result okPage() {
    return ok(views.html.okpage.render());
  }

  public static Result temp() {
    return ok(temp.render());
  }

  public static Result help() {
    return TODO;
  }

	// -- Authentication

  public static class Login {

    public String email;
    public String password;

    public String validate() {
      if(User.authenticate(email, password) == null) {
        return "Указано неверное имя пользователя или пароль";
      }
      return null;
    }

  }

  /**
   * Login page.
   */
  public static Result login() {
    return ok(
      login.render(Form.form(Login.class))
      );
  }
    
  /**
   * Handle login form submission.
   */
  public static Result authenticate() {
    Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
    if(loginForm.hasErrors()) {
      //TODO: make badRequest routing
      // return badRequest(login.render(loginForm));
      return redirect(routes.Application.homepage());
    } else {
      Login login = loginForm.get();
      User user = User.findByEmail(login.email);
      session("email", user.email);
      session("username", user.name);
      return redirect(routes.Application.registry());
    }
  }

  /**
   * Logout and clean the session.
   */
  public static Result logout() {
    session().clear();
    flash("success", "Вы вышли из системы");
    return redirect(routes.Application.homepage());
  }
}