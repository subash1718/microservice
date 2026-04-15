Feature: Order API Test

Background:
    * def port = karate.properties['karate.port']
    * url 'http://localhost:' + port

Scenario: Create and get orders

    Given path 'orders'
    And request { productName: 'Phone', quantity: 2, price: 1000, status: 'CREATED' }
    When method post
    Then status 200

    Given path 'orders'
    When method get
    Then status 200
