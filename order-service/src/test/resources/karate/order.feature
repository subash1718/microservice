Feature: Order API Test

Scenario: Create and get orders

Given url 'http://localhost:8082/orders'
And request { product: 'Laptop', quantity: 2 }
When method post
Then status 200

Given url 'http://localhost:8082/orders'
When method get
Then status 200
And match response[0].product == 'Laptop'