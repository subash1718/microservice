Feature: Order API Test

Background:
    * url 'http://localhost:' + karate.properties['port']

Scenario: Create and get orders

    Given path '/orders'
    And request { "productName": "Test", "quantity": 1 }
    When method post
    Then status 200

    Given path '/orders'
    When method get
    Then status 200
