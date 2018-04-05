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

  # TODO annotation & code & test the test
  Scenario: (POST /composition/contribution)

  # TODO annotation & code & test the test
  Scenario: (POST /composition/covert/json)

  # TODO test the test
  Scenario: (POST /composition/convert/tdd) Client wants to convert a composition from JSON to XML
    Then A flat json file IDCR - Immunisation summary.v0.flat.json with template id IDCR - Immunisation summary.v0 is converted

  # TODO annotation & code & test the test
  Scenario: (POST /composition/generated)

  # TODO test the test 
  Scenario: (POST /composition/getByAql [flat])
    When Flat json file IDCR - Immunisation summary.v0.flat.json with template id IDCR - Immunisation summary.v0 is committed to service
    Then Composition id should allow returning of composition by query

  # TODO test the test 
  Scenario: (POST /composition/getByUids [flat])
    When Flat json file IDCR - Immunisation summary.v0.flat.json with template id IDCR - Immunisation summary.v0 is committed to service
    Then Composition id should allow returning of composition by id

  # TODO annotation & code & test the test
  Scenario: (GET /composition/{uid}/signature)

  # TODO annotation & code & test the test
  Scenario: (GET /composition/soa/{uid})

  # TODO code & test the test
  Scenario: (general case PUT  /compositon/{uid} [flat])
    When Flat json file IDCR - Immunisation summary.v0.flat.json with template id IDCR - Immunisation summary.v0 is committed to service
    Then Composition id should allow update of existing composition

  # TODO annotation & code & test the test
  Scenario: (PUT /composition/{uid}/signature)

  # TODO annotation & code & test the test
  Scenario: (PUT /composition/soa/{uid})