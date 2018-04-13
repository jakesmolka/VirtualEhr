package com.ethercis.vehr;

import com.jayway.restassured.response.Response;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static com.ethercis.vehr.RestAPIBackgroundSteps.COMPOSITION_ENDPOINT;
import static com.ethercis.vehr.RestAPIBackgroundSteps.STATUS_CODE_OK;
import static com.ethercis.vehr.RestAPIBackgroundSteps.TEST_DATA_DIR;
import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RestAPICompositionSteps {

    //private static final String COMPOSITION_ENDPOINT = "/rest/v1/composition";
    private static final String FORMAT_RAW = "RAW";
    private static final String FORMAT_XML = "XML";
    private final RestAPIBackgroundSteps bg;
    private String _compositionUid;

    public RestAPICompositionSteps(RestAPIBackgroundSteps pBackgroundSteps){
        bg = pBackgroundSteps;
    }

    public void setCompositionUid(String value) {
        _compositionUid = value;
    }

    public String getCompositionUid() {
        return _compositionUid;
    }

    // (POST /composition FLAT)
    @When("^Flat json file ([a-zA-Z \\-\\.0-9]+\\.json) with template id ([a-zA-Z \\-\\.0-9]+) is committed to service$")
    public void flatJsonFileIsCommittedToService(String pTemplateFileName, String pTemplateId) throws Exception {
    //     Path jsonFilePath =
    //         Paths
    //             .get(bg.resourcesRootPath + "test_data/" + pTemplateFileName);
    //     byte[] fileContents = Files.readAllBytes(jsonFilePath);

    //     Response commitCompositionResponse =
    //         given()
    //             .header(bg.secretSessionId, bg.SESSION_ID_TEST_SESSION)
    //             .header(bg.CONTENT_TYPE, bg.CONTENT_TYPE_JSON)
    //         .content(fileContents)
    //         .when()
    //             .post(COMPOSITION_ENDPOINT + "?format=FLAT&templateId=" + pTemplateId)
    //         .then().statusCode(200).extract().response();
    //     assertEquals("Status code not 200; error: " + commitCompositionResponse.header("x-error-message")+";", 200, commitCompositionResponse.statusCode());

    //    _compositionUid = commitCompositionResponse.body().jsonPath().getString("compositionUid");

       _compositionUid = bg.postFlatJsonComposition(TEST_DATA_DIR, pTemplateFileName, pTemplateId);
    }

    @After
    public void cleanUp() throws Exception {
        bg.launcher.stop();
    }

    @Then("^A composition id should be returned by the API$")
    public void aCompositionIdShouldBeReturnedByTheAPI() throws Throwable {
        assertTrue(_compositionUid.split("::").length == 3);
    }

    /** 
     * helper function for GET - exluding central status code check so negative codes can be tested too
     */
    private Response getComposition(String objectId, String pContentType, String pFormat) {
        return given()
                .header(bg.secretSessionId, bg.SESSION_ID_TEST_SESSION)
                .header(bg.ACCEPT, pContentType)
                .when()
                    .get(COMPOSITION_ENDPOINT + "/" + objectId + "?format=" + pFormat);
    }

    // (GET /composition/{uuid} RAW)
    @And("^Composition id should allow retrieval of composition in raw format$")
    public void compositionIdShouldAllowRetrievalOfCompositionInRawFormat() throws Throwable {
        String objectId =_compositionUid.substring(0,_compositionUid.indexOf("::"));

        Response response = getComposition(objectId, bg.CONTENT_TYPE_JSON, FORMAT_RAW);
        assertEquals("Status code not 200; error: " + response.header("x-error-message")+";", 200, response.statusCode());

        Object composition = response.body().jsonPath().getJsonObject("composition");
        assertNotNull(composition);
    }

    // (GET /composition/{uuid} XML)
    // @Then("^Composition id should allow retrieval of composition in xml format$")
    // public void compositionIdShouldAllowRetrievalOfCompositionInXmlFormat() throws Throwable {
    //     String objectId =_compositionUid.substring(0,_compositionUid.indexOf("::"));

    //     Response response = getComposition(objectId, bg.CONTENT_TYPE_XML, FORMAT_XML);
    //     assertEquals("Status code not 200; error: " + response.header("x-error-message")+";", 200, response.statusCode());

    //     String composition = response.body().asString();
    //     assertNotNull(composition);

    //     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    //     DocumentBuilder documentBuilder = factory.newDocumentBuilder();
    //     Document xmlComposition =
    //         documentBuilder
    //             .parse(new ByteArrayInputStream(composition.getBytes()));

    //     XPathFactory xPathFactory = XPathFactory.newInstance();
    //     XPath xPath = xPathFactory.newXPath();
    //     Node rootNode = (Node) xPath.evaluate("/composition", xmlComposition.getDocumentElement(), XPathConstants.NODE);
    //     assertNotNull(rootNode);
    //     assertTrue(rootNode.getNodeName().equals("composition"));

    // }

    // (GET /composition/{uuid} XML)
    @And("^Composition id should allow retrieval of composition in xml format$")
    public void compositionIdShouldAllowRetrievalOfCompositionInXmlFormat() throws Exception {
        Document xmlComposition = getXmlCompositionFromRestApi();

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        Node rootNode = (Node) xPath.evaluate("/composition", xmlComposition.getDocumentElement(), XPathConstants.NODE);
        assertNotNull(rootNode);
        assertTrue(rootNode.getNodeName().equals("composition"));

    }

    // helper function - TODO: what for?
    public Document getXmlCompositionFromRestApi() throws ParserConfigurationException, SAXException, IOException {
        String composition = getXmlStringFromRestAPI();
        assertNotNull(composition);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        return documentBuilder
            .parse(new ByteArrayInputStream(composition.getBytes()));
    }

    // helper function - TODO: what for?
    public String getXmlStringFromRestAPI() {
        String objectId = _compositionUid.substring(0, _compositionUid.indexOf("::"));

        Response response = getComposition(objectId, bg.CONTENT_TYPE_XML, FORMAT_XML);
        assertEquals("Status code not 200; error: " + response.header("x-error-message")+";", 200, response.statusCode());

        return response.body().asString();
    }

    // helper function for DELETE
    private Response delComposition(String objectId) {
        return given()
                .header(bg.secretSessionId, bg.SESSION_ID_TEST_SESSION)
                .when()
                    .delete(COMPOSITION_ENDPOINT + "/" + objectId);
    }

    // (DELETE /composition/{uuid})
    @Then("^Composition id should allow deletion of composition$")
    public void compositionIdShouldAllowDeletionOfComposition() throws Throwable {
        String objectId =_compositionUid.substring(0,_compositionUid.indexOf("::"));

        Response response = delComposition(objectId);

        assertEquals("Status code not 200; error: " + response.header("x-error-message")+";", 200, response.statusCode());
    }

    // TODO: (POST /composition/contribution)

    // FIXME: (POST /composition/covert/json) - ERROR
    @Then("^A xml file ([a-zA-Z \\-\\.0-9]+\\.xml) is converted into json$")
    public void xmlFileIsConverted(String xmlFileName) throws Exception {
        // Path xmlFilePath =
        //     Paths
        //         .get(bg.resourcesRootPath + "test_data/" + xmlFileName);
        // byte[] fileContents = Files.readAllBytes(xmlFilePath);

        byte[] fileContents = bg.getFileContent(TEST_DATA_DIR, xmlFileName);

        Response response =
            given()
                .header(bg.secretSessionId, bg.SESSION_ID_TEST_SESSION)
                .header(bg.CONTENT_TYPE, bg.CONTENT_TYPE_XML)
            .content(fileContents)
            .when()
                .post(COMPOSITION_ENDPOINT + "/convert/json" + "?format=FLAT");

        assertEquals("Status code not 200; error: " + response.header("x-error-message")+";", 200, response.statusCode());
    }

    // (POST /composition/convert/tdd)
    @Then("^A flat json file ([a-zA-Z \\-\\.0-9]+\\.json) with template id ([a-zA-Z \\-\\.0-9]+) is converted$")
    public void flatJsonFileIsConverted(String pTemplateFileName, String pTemplateId) throws Exception {
        // Path jsonFilePath =
        //     Paths
        //         .get(bg.resourcesRootPath + "test_data/" + pTemplateFileName);
        // byte[] fileContents = Files.readAllBytes(jsonFilePath);

        byte[] fileContents = bg.getFileContent(TEST_DATA_DIR, pTemplateFileName);

        Response response =
            given()
                .header(bg.secretSessionId, bg.SESSION_ID_TEST_SESSION)
                .header(bg.CONTENT_TYPE, bg.CONTENT_TYPE_JSON)
            .content(fileContents)
            .when()
                .post(COMPOSITION_ENDPOINT + "/convert/tdd" + "?format=FLAT&templateId=" + pTemplateId);

        assertEquals("Status code not 200; error: " + response.header("x-error-message")+";", 200, response.statusCode());
    }

    // TODO: (POST /composition/generated)

    // FIXME: (POST /composition/getByAql [flat]) - ERROR
    @Then("^Composition id should allow returning of composition by query$")
    public void compositionIdShouldAllowReturningOfCompositionByQuery() throws Throwable {
        String query = "\"SELECT c FROM COMPOSITION c WHERE c/name/value = :name\"";
        String queryParameter = "\"IDCR - Immunisation summary.v0\"";

        Response response =
            given()
                .header(bg.secretSessionId, bg.SESSION_ID_TEST_SESSION)
                .header(bg.CONTENT_TYPE, bg.CONTENT_TYPE_JSON)
                .body("{\"aql\": \""+ query +"\","
                +"\"aqlParameter\":{\"name\":"+queryParameter+"}}")
            .when()
                .post(COMPOSITION_ENDPOINT + "/getByAql");

        assertEquals("Status code not 200; error: " + response.header("x-error-message")+";", 200, response.statusCode());
    }

    // FIXME: (POST /composition/getByUids [flat]) - ERROR
    @Then("^Composition id should allow returning of composition by id$")
    public void compositionIdShouldAllowReturningOfCompositionById() throws Throwable {
        String objectId =_compositionUid.substring(0,_compositionUid.indexOf("::"));
        
        Response response =
            given()
                .header(bg.secretSessionId, bg.SESSION_ID_TEST_SESSION)
                .header(bg.CONTENT_TYPE, bg.CONTENT_TYPE_JSON)
                .body("{\"uuids\": [\""+ objectId +"\"]}")
            .when()
                .post(COMPOSITION_ENDPOINT + "/getByUids");

        assertEquals("Status code not 200; error: " + response.header("x-error-message")+";", 200, response.statusCode());
    }

    // TODO: (POST /composition/soa)

    // FIXME: (GET /composition/{uid}/signature)
    @Then("^Composition id should allow retrieval of signature$")
    public void compositionIdShouldAllowRetrievalOfSignature() throws Throwable {
        // needs full uids instead of following, right?
        //String objectId =_compositionUid.substring(0,_compositionUid.indexOf("::"));

        Response response =
            given()
                .header(bg.secretSessionId, bg.SESSION_ID_TEST_SESSION)
            .when()
                .get(COMPOSITION_ENDPOINT + "/"  +_compositionUid + "/signature");

        assertEquals("Status code not 200; error: " + response.header("x-error-message")+";", 200, response.statusCode());
    }

    // TODO: (GET /composition/soa/{uid})

    // (PUT  /compositon/{uid} [flat])
    @Then("^Composition id should allow update of template with id ([a-zA-Z \\-\\.0-9]+) from file ([a-zA-Z \\-\\.0-9]+\\.json)$")
    public void compositionIdShouldAllowUpdateOfExistingComposition(String pTemplateId, String pTemplateFile) throws Throwable {
        // Path jsonFilePath =
        //     Paths
        //         .get(bg.resourcesRootPath + "test_data/" + pTemplateFile);
        // byte[] fileContents = Files.readAllBytes(jsonFilePath);

        byte[] fileContents = bg.getFileContent(TEST_DATA_DIR, pTemplateFile);
        
        String objectId =_compositionUid.substring(0,_compositionUid.indexOf("::"));

        Response response =
            given()
                .header(bg.secretSessionId, bg.SESSION_ID_TEST_SESSION)
                .header(bg.CONTENT_TYPE, bg.CONTENT_TYPE_JSON)
            .content(fileContents)
            .when()
                .put(COMPOSITION_ENDPOINT + "/"  + objectId + "?templateId=" + pTemplateId + "&format=FLAT");

        assertEquals("Status code not 200; error: " + response.header("x-error-message")+";", 200, response.statusCode());
    }

    // FIXME: (PUT /composition/{uid}/signature)
    @Then("^Composition id should allow signing with another signature$")
    public void compositionIdShouldAllowSigningWithAnotherSignature() throws Throwable {
        // Path filePath =
        //     Paths
        //         .get(bg.resourcesRootPath + "test_data/digital_signature");
        // byte[] fileContents = Files.readAllBytes(filePath);

        byte[] fileContents = bg.getFileContent(TEST_DATA_DIR, "digital_signature");
        
        // needs full uids instead of following, right?
        //String objectId =_compositionUid.substring(0,_compositionUid.indexOf("::"));

        Response response =
            given()
                .header(bg.secretSessionId, bg.SESSION_ID_TEST_SESSION)
                .header(bg.CONTENT_TYPE, bg.CONTENT_TYPE_PLAIN)
            .content(fileContents)
            .when()
                .put(COMPOSITION_ENDPOINT + "/"  +_compositionUid + "/signature");

        assertEquals("Status code not 200; error: " + response.header("x-error-message")+";", 200, response.statusCode());
    }

    // TODO: (PUT /composition/soa/{uid})

    // FIXME: (DELETE /composition/{uid}/signature)
    @Then("^Composition id should allow removing of signature$")
    public void compositionIdShouldAllowRemovingOfSignature() throws Throwable {
        // needs full uids instead of following, right?
        //String objectId =_compositionUid.substring(0,_compositionUid.indexOf("::"));

        Response response =
            given()
                .header(bg.secretSessionId, bg.SESSION_ID_TEST_SESSION)
            .when()
                .delete(COMPOSITION_ENDPOINT + "/"  +_compositionUid + "/signature");

        assertEquals("Status code not 200; error: " + response.header("x-error-message")+";", 200, response.statusCode());
    }
}