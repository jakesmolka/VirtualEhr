# Ehrscape API - /ehr

# TODO: expand specification cases!
# [X] POST ([ ] all status codes)
# [X] GET (3 times) ([ ] all status codes)
# [X] PUT (3 times) ([ ] all status codes)

Feature: Provide EHR API access via REST calls
  In order to work with openEHR data
  As a client system
  I want to make REST API calls over http to create, modify and delete EHRs

  Background:
  The server is ready and the user is logged in.

    Given The server is running
    And The client system is logged into a server session

  Scenario: (POST EHR)
    TEXT
    TODO: how to treat 'An EHR is created' is usally called as @And and here maybe as @Then 

    And An EHR is created

  Scenario: (GET EHR)
    
    Given An EHR is created
    Then Subject id should allow retrieval

  Scenario: (PUT EHR)
    
    Given An EHR is created
    Then Ehr id should allow modifying the Ehr status