package com.ethercis.vehr;

import com.jayway.restassured.response.Response;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class RestAPIEhrSteps {
    private static final String EHR_ENDPOINT = "/rest/v1/ehr";
    private final RestAPIBackgroundSteps bground;

    public RestAPIEhrSteps(RestAPIBackgroundSteps pBackgroundSteps){
        bground = pBackgroundSteps;
    }

    //NOTE: POST funtion for /ehr already defined in RestAPIBackgroundSteps.java

    // (GET /ehr)
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

        assertEquals("Status code not 200; error: " + response.header("x-error-message")+";", 200, response.statusCode());
    }

    // (GET /ehr/{ehrId})
    @Then("^Ehr id should allow retrieval$")
    public void ehrIdShouldAllowRetrieval() throws Throwable {
        Response response =
            given()
                .header(bground.secretSessionId, bground.SESSION_ID_TEST_SESSION)
            .when()
                .get(
                    EHR_ENDPOINT + "/" + bground.ehrId);
        assertNotNull(response);

        assertEquals("Status code not 200; error: " + response.header("x-error-message")+";", 200, response.statusCode());
    }
    
    // FIXME: (GET /ehr/{ehrId}/folder)
    @Then("^Ehr id should allow retrieval of folder$")
    public void ehrIdShouldAllowRetrievalOfFolder() throws Throwable {
        Response response =
            given()
                .header(bground.secretSessionId, bground.SESSION_ID_TEST_SESSION)
            .when()
                .get(
                    EHR_ENDPOINT + "/" + bground.ehrId + "/folder");
        assertNotNull(response);

        assertEquals("Status code not 200; error: " + response.header("x-error-message")+";", 200, response.statusCode());
    }
  
    // FIXME: (PUT /ehr/{ehrId}/folder)
    @Then("^Ehr id should allow setting of folder$")
    public void ehrIdShouldAllowSettingOfFolder() throws Throwable {
        Response response =
            given()
                .header(bground.secretSessionId, bground.SESSION_ID_TEST_SESSION)
            .when()
                .put(
                    EHR_ENDPOINT + "/" + bground.ehrId + "/folder");
        assertNotNull(response);

        assertEquals("Status code not 200; error: " + response.header("x-error-message")+";", 200, response.statusCode());
    }

    // FIXME: (PUT /ehr/status)
    @Then("^Ehr id should allow modifying the Ehr status$")
    public void ehrIdShouldAllowModifyingTheEhrStatus() throws Throwable {
        Response response =
            given()
                .header(bground.secretSessionId, bground.SESSION_ID_TEST_SESSION)
                .body(ehrStatus())
            .when()
                .put(
                    EHR_ENDPOINT + "/{ehrId}/status"
                    , bground.ehrId);
        assertNotNull(response);

        assertEquals("Status code not 200; error: " + response.header("x-error-message")+";", 200, response.statusCode());
    }

    private String ehrStatus() {
        return "{" +
            "\"subjectId\": \"90470912\"," +
            "\"subjectNamespace\": \"ExternalDB\"," + 
            "\"queryable\": true," +
            "\"modifiable\": true" +
        "}";
    }

    // FIXME: (PUT /ehr/status/other_details)
    @Then("^Ehr id should allow setting of other_details$")
    public void ehrIdShouldAllowSettingOfOtherDetails() throws Throwable {
        Response response =
            given()
                .header(bground.secretSessionId, bground.SESSION_ID_TEST_SESSION)
                .body(ehrStatus())
            .when()
                .put(
                    EHR_ENDPOINT + "/{ehrId}/status/other_details"
                    , bground.ehrId);
        assertNotNull(response);

        assertEquals("Status code not 200; error: " + response.header("x-error-message")+";", 200, response.statusCode());
    }
}
