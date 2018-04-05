package com.ethercis.vehr;

import com.jayway.restassured.response.Response;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RestAPISessionSteps {
    private static final String SESSION_ENDPOINT = "/rest/v1/session";
    private final RestAPIBackgroundSteps bground;

    public RestAPISessionSteps(RestAPIBackgroundSteps pBackgroundSteps){
        bground = pBackgroundSteps;
    }

    //NOTE: POST funtion for /session already defined in RestAPIBackgroundSteps.java

    @Then("^The client system can be logged out of server session$")
    public void theClientSystemCanBeLoggedOutOfServerSession() throws Throwable {

        Response response =
            given()
                .header(bground.secretSessionId, bground.SESSION_ID_TEST_SESSION)
                .when()
                .delete(SESSION_ENDPOINT);
        assertNotNull(response);

        assertTrue("DEL status: " + response.statusCode(), response.statusCode() == 200);
    }

    @Then("^The client system can ping the server session$")
    public void theClientSystemCanPingTheServerSession() throws Throwable {

        Response response =
            given()
                .header(bground.secretSessionId, bground.SESSION_ID_TEST_SESSION)
                .when()
                .put(SESSION_ENDPOINT);
        assertNotNull(response);

        assertTrue("PUT status: " + response.statusCode(), response.statusCode() == 200);
    }
}
