package test;

import models.*;
import org.junit.*;
import play.mvc.*;
import play.test.*;
import play.libs.F.*;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

public class SimpleTest {

	@Test 
    public void simpleCheck() {
        int a = 1 + 1;
        assertThat(a).isEqualTo(2);
    }

    @Test
	public void findById() {
	  running(fakeApplication(), new Runnable() {
	    public void run() {
	      //tests here
	    }
	  });
	}

    // @Test
    // public void testRoute() {
    //     Result result = routeAndCall(fakeRequest(GET, "/persons"));
    //     assertThat(result).isNotNull();
    // }

    // @Test
    // public void callIndex() {
    //     Result result = callAction(controllers.routes.ref.Application.index());
    //     assertThat(status(result)).isEqualTo(OK);
    //     assertThat(contentType(result)).isEqualTo("text/html");
    //     assertThat(charset(result)).isEqualTo("utf-8");
    //     assertThat(contentAsString(result)).contains("hello, world");
    // }

}