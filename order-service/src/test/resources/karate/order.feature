Feature: Order API Test

Scenario: Get all orders
  Given url 'http://localhost:8082/orders'
  When method get
  Then status 200
  And match response[0].product != null