package com.ethercis.vehr;

import com.ethercis.logonservice.session.I_SessionManager;
import com.ethercis.servicemanager.runlevel.I_ServiceRunMode;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.xml.XmlPath;
import com.jayway.restassured.response.Response;
import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RestAPIQuerySteps {

    private RestAPIBackgroundSteps bacgroundSteps;

    public RestAPIQuerySteps(RestAPIBackgroundSteps pBackgroundSteps){
        bacgroundSteps = pBackgroundSteps;
    }

    public RestAPIQuerySteps() {
    }

    @After
    public void cleanUp() throws Exception {
        bacgroundSteps.launcher.stop();
    }

    @When("^A composition is persisted under the EHR$")
    public void aCompositionIsPersistedUnderTheEHR() throws Throwable {
        MakeCallToPersistComposition(true);
    }

    private void MakeCallToPersistComposition(boolean pPassEhrId) throws IOException {
        byte[] xmlContent =
            Files
            .readAllBytes(
                Paths.get(bacgroundSteps.resourcesRootPath + "test_data/Prescription.xml"));

        Response response =
            given()
                .header(bacgroundSteps.secretSessionId, bacgroundSteps.SESSION_ID_TEST_SESSION)
                .header(bacgroundSteps.CONTENT_TYPE, bacgroundSteps.CONTENT_TYPE_XML)
                .content(xmlContent)
            .when()
            .post("/rest/v1/composition?format=XML"
                + (pPassEhrId
                    ? "&ehrId=" + bacgroundSteps.ehrId
                    : ""));
        assertNotNull(response);

        String xml = response.getBody().asString();
        String uid =
            XmlPath
                .from(xml)
                .getString(bacgroundSteps.COMPOSITION_UID_PATH_IN_XML);
        assertNotNull(uid);
        Pattern pattern = Pattern.compile("[a-z0-9-]*::[a-z0-9.]*::[0-9]*");
        Matcher matcher = pattern.matcher(uid);
        assertTrue(matcher.matches());
    }

    @Then("^An AQL query should return data from the composition in the EHR$")
    public void anAQLQueryShouldReturnDataPersistedIntoTheComposition() throws Throwable {
        Response response = given()
            .header(bacgroundSteps.secretSessionId, bacgroundSteps.SESSION_ID_TEST_SESSION)
            .param("aql", buildAqlQuery())
            .get("/rest/v1/query");

        assertEquals("Status code not 200; error: " + response.header("x-error-message")+";", 200, response.statusCode());

        List<Map<String,String>> queryResults = response.getBody().jsonPath().getList("resultSet");
        assertNotNull(queryResults);
        assertTrue(queryResults.size() == 1);
        for(Map<String,String> row:queryResults){
            assertTrue(row.keySet().size() == 1 && row.keySet().contains("uid"));
        }
    }

    private String buildAqlQuery(){
        return "select a/uid/value " +
            "from EHR e [ehr_id/value='" + bacgroundSteps.ehrId.toString() + "']" +
            "contains COMPOSITION a[openEHR-EHR-COMPOSITION.prescription.v1] ";
    }

    // prepare AQL for POST
    private String buildJsonAqlQuery() {
        return "{\"aql\":\"" + buildAqlQuery() + "\"}";
    }

    @When("^A composition is persisted under the EHR without an EHR identifier$")
    public void aCompositionIsPersistedUnderTheEHRWithoutAnEHRIdentifier() throws Throwable {
        MakeCallToPersistComposition(false);
    }

    // (GET /query/csv)
    @Then("^An AQL query should allow retrieving data as CSV$")
    public void anAQLQueryShouldAllowRetrievingDataAsCSV() throws Throwable {
        Response response = given()
            .header(bacgroundSteps.secretSessionId, bacgroundSteps.SESSION_ID_TEST_SESSION)
            .param("aql", buildAqlQuery())
            .get("/rest/v1/query/csv");

        assertEquals("Status code not 200; error: " + response.header("x-error-message")+";", 200, response.statusCode());
    }

    // TODO: (GET /query/form/{name}/{version})

    // TODO: first POST /query/poll
    // TODO: (GET /query/poll/{queryUuid)

    // FIXME: (POST /query)
    @Then("^Querying with named parameter support should work$")
    public void queryingWithNamedParameterSupportShouldWork() throws Throwable {
        Response response = 
            given()
                .header(bacgroundSteps.secretSessionId, bacgroundSteps.SESSION_ID_TEST_SESSION)
                .header(bacgroundSteps.CONTENT_TYPE, bacgroundSteps.CONTENT_TYPE_JSON)
                .body(buildJsonAqlQuery())
            .when()
                .post("/rest/v1/query");

        assertEquals("Status code not 200; error: " + response.header("x-error-message")+";", 200, response.statusCode());
    }

    // FIXME: (POST /query/csv)
    @Then("^Querying with named parameter support and csv output should work$")
    public void queryingWithNamedParameterSupportAndCsvOuputShouldWork() throws Throwable {
        Response response = 
            given()
                .header(bacgroundSteps.secretSessionId, bacgroundSteps.SESSION_ID_TEST_SESSION)
                .header(bacgroundSteps.CONTENT_TYPE, bacgroundSteps.CONTENT_TYPE_JSON)
                .body(buildJsonAqlQuery())
            .when()
                .post("/rest/v1/query/csv");

        assertEquals("Status code not 200; error: " + response.header("x-error-message")+";", 200, response.statusCode());
    }

    // TODO: (POST /query/poll)
    @Then("^Submitting a polling query should work$")
    public void submittingAPollingQueryShouldWork() throws Throwable {
        Response response = 
            given()
                .header(bacgroundSteps.secretSessionId, bacgroundSteps.SESSION_ID_TEST_SESSION)
                .header(bacgroundSteps.CONTENT_TYPE, bacgroundSteps.CONTENT_TYPE_JSON)
                .body(buildJsonAqlQuery())
            .when()
                .post("/rest/v1/query/poll");

        assertEquals("Status code not 200; error: " + response.header("x-error-message")+";", 200, response.statusCode());
    }
}
