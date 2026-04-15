Feature: Order Service API Test

Scenario: Get all orders
    Given url 'http://localhost:' + karate.properties['karate.server.port'] + '/orders'
    When method GET
    Then status 200
