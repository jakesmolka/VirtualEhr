# Ehrscape API - /session

# TODO: whole /session is deprecated?!

# TODO: expand specification cases!
# [X] POST ([ ] all status codes)
# [X] DELETE ([ ] all status codes)
# [ ] PUT (3 times) ([ ] all status codes)

Feature: Provide EHR API access via REST calls
  In order to work with openEHR data
  As a client system
  I want to make REST API calls over http to create, modify and delete EHRs

  Background:
  The server is ready.

    Given The server is running

  Scenario: User logs in and creates session
  With a running server a user logs in and creates a session

    Then The client system is logged into a server session

  Scenario: User logs out and deletes session
  As part of logging out the session gets deleted 

    When The client system is logged into a server session
    Then The client system can be logged out of server session

  Scenario: Client pings session
  In order to extent session time to prevent time out the session is pinged
  TODO: method is deprecated...

    When The client system is logged into a server session
    Then The client system can ping the server session

# TODO: stopped here because whole /session is deprecated?!