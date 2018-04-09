# Ehrscape API - /ehr

Feature: Provide EHR API access via REST calls
  In order to work with openEHR data
  As a client system
  I want to make REST API calls over http to create, modify and delete EHRs

  Background:
  The server is ready and the user is logged in.

    Given The server is running
    And The client system is logged into a server session

  Scenario: (POST /ehr)
    Given An EHR is created

  Scenario: (GET /ehr)
    Given An EHR is created
    Then Subject id should allow retrieval

  Scenario: (GET /ehr/{ehrId})
    Given An EHR is created
    Then Ehr id should allow retrieval
  
  # FIXME: error
  Scenario: (GET /ehr/{ehrId}/folder)
    Given An EHR is created
    Then Ehr id should allow retrieval of folder

  # FIXME: no routing - if resolved: add test body & test the test
  Scenario: (PUT /ehr/{ehrId}/folder)
    Given An EHR is created
    Then Ehr id should allow setting of folder

  # FIXME: no routing
  Scenario: (PUT /ehr/status)  
    Given An EHR is created
    Then Ehr id should allow modifying the Ehr status

  # FIXME: no routing  - if resolved: add test body & test the test
  Scenario: (PUT /ehr/status/other_details)
    Given An EHR is created
    Then Ehr id should allow setting of other_details