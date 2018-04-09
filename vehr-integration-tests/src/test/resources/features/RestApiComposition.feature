# Ehrscape API - /composition
# Status: should list all endpoint but only general cases

Feature: Provide Composition API access over REST
  As a clinical informatics actor
  In order to create, access and modify data based on openEHR compositions
  I want to use a REST API to perform operations on compositions.

  Background:
  The server is ready, an EHR and a template is in place and the user is logged in.

    Given The server is running
    And The client system is logged into a server session
    And The openEHR template IDCR - Immunisation summary.v0.opt for the composition is available to the server
    And An EHR is created

  Scenario: Commit composition in flat json format 
    When Flat json file IDCR - Immunisation summary.v0.flat.json with template id IDCR - Immunisation summary.v0 is committed to service
    Then A composition id should be returned by the API

  Scenario: Commit composition in flat json and retrieve raw format
    When Flat json file IDCR - Immunisation summary.v0.flat.json with template id IDCR - Immunisation summary.v0 is committed to service
    Then Composition id should allow retrieval of composition in raw format

  Scenario: Commit composition in flat json and retrieve xml format
    When Flat json file IDCR - Immunisation summary.v0.flat.json with template id IDCR - Immunisation summary.v0 is committed to service
    Then Composition id should allow retrieval of composition in xml format

  Scenario: Commit composition in flat json and remove composition again
    When Flat json file IDCR - Immunisation summary.v0.flat.json with template id IDCR - Immunisation summary.v0 is committed to service
    Then Composition id should allow deletion of composition

  # TODO: annotation & code & test the test
  # Scenario: (POST /composition/contribution)

  # FIXME: error demands templateId parameter but Ehrscape API doesn't list templateId as parameter here
  Scenario: (POST /composition/covert/json)
    Then A xml file Prescription.xml is converted into json

  Scenario: (POST /composition/convert/tdd) Client wants to convert a composition from JSON to XML
    Then A flat json file IDCR - Immunisation summary.v0.flat.json with template id IDCR - Immunisation summary.v0 is converted

  # TODO: annotation & code & test the test
  # NOTE: only raw file composition supported - needed as test file
  # Scenario: (POST /composition/generated)

  # FIXME: fails with 500 - xml error? 
  Scenario: (POST /composition/getByAql [flat])
    When Flat json file IDCR - Immunisation summary.v0.flat.json with template id IDCR - Immunisation summary.v0 is committed to service
    Then Composition id should allow returning of composition by query

  # FIXME: fails with 500 - xml error?
  Scenario: (POST /composition/getByUids [flat])
    When Flat json file IDCR - Immunisation summary.v0.flat.json with template id IDCR - Immunisation summary.v0 is committed to service
    Then Composition id should allow returning of composition by id
  
  # TODO: annotation & code & test the test
  # Scenario: (POST /composition/soa)

  # FIXME: fails with 400 - invalid template ID even though it should be a valid one
  Scenario: (GET /composition/{uid}/signature)
    When Flat json file IDCR - Immunisation summary.v0.flat.json with template id IDCR - Immunisation summary.v0 is committed to service
    Then Composition id should allow retrieval of signature

  # TODO: annotation & code & test the test
  # Scenario: (GET /composition/soa/{uid})

  Scenario: (PUT  /compositon/{uid} [flat])
    When Flat json file IDCR - Immunisation summary.v0.flat.json with template id IDCR - Immunisation summary.v0 is committed to service
    Then Composition id should allow update of template with id IDCR - Immunisation summary.v0 from file IDCR - Immunisation summary.v1.flat.json

  # FIXME: fails with 400 - invalid template ID even though it should be a valid one
  Scenario: (PUT /composition/{uid}/signature)
    When Flat json file IDCR - Immunisation summary.v0.flat.json with template id IDCR - Immunisation summary.v0 is committed to service
    Then Composition id should allow signing with another signature

  # TODO: annotation & code & test the test
  # Scenario: (PUT /composition/soa/{uid})

  # FIXME: fails with 400 - invalid template ID even though it should be a valid one
  Scenario: (DELETE /composition/{uid}/signature)
    When Flat json file IDCR - Immunisation summary.v0.flat.json with template id IDCR - Immunisation summary.v0 is committed to service
    # active signing necessary?
    Then Composition id should allow removing of signature
