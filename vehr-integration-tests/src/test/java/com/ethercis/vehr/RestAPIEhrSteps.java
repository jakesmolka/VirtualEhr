package com.ethercis.vehr;

import com.jayway.restassured.response.Response;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RestAPIEhrSteps {
    private static final String EHR_ENDPOINT = "/rest/v1/ehr";
    private final RestAPIBackgroundSteps bground;

    public RestAPIEhrSteps(RestAPIBackgroundSteps pBackgroundSteps){
        bground = pBackgroundSteps;
    }

    //NOTE: POST funtion for /ehr already defined in RestAPIBackgroundSteps.java

    @Then("^Subject id should allow retrieval$")
    public void subjectIdShouldAllowRetrieval() throws Throwable {
        Response response =
            given()
                .header(bground.secretSessionId, bground.SESSION_ID_TEST_SESSION)
                .when()
                .get(
                    EHR_ENDPOINT + "?subjectId={subjectId}&subjectNamespace={subjectNs}"
                    , bground.SUBJECT_CODE_ID, bground.SUBJECT_NAMESPACE);
        assertNotNull(response);

        assertTrue("GET status: " + response.statusCode(), response.statusCode() == 200);
    }

    @Then("^Ehr id should allow modifying the Ehr status$")
    public void ehrIdShouldAllowModifyingTheEhrStatus() throws Throwable {
        Response response =
            given()
                .header(bground.secretSessionId, bground.SESSION_ID_TEST_SESSION)
                .body(ehrStatus())
                .when()
                .put(
                    EHR_ENDPOINT + "/status/" + "{ehrId}"
                    , bground.ehrId);
        assertNotNull(response);

        assertTrue("PUT status: " + response.statusCode(), response.statusCode() == 200);
    }

    private String ehrStatus() {
        return "{" +
            "\"subjectId\": \"90470912\"," +
            "\"subjectNamespace\": \"ExternalDB\"," + 
            "\"queryable\": true," +
            "\"modifiable\": true" +
        "}";
    }
}
