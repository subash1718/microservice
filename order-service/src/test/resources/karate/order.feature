Feature: Order API Test

Background:
    * def port = karate.properties['port']
    * url 'http://localhost:' + port

Scenario: Create and get orders

    Given path '/orders'
    And request { "productName": "Test", "quantity": 1 }
    When method post
    Then status 200

    Given path '/orders'
    When method get
    Then status 200
