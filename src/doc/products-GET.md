# Get Product
Returns json data about a single product

## Request

| Method | Request URL    |
| ------ | -------------- |
| GET    | /products/{id} |

### URL Path Parameters

| Name | Description                                                  |
| ---- | ------------------------------------------------------------ |
| id   | The unique identifier for the product that will be retrieved |
### Query Parameters
None
### Body
None
## Response
#### Status Codes

| Status Code | Denotes           |
| ----------- | ----------------- |
| 200         | Success           |
| 404         | Product not found |

### Success Response
#### Status Code
`200`
#### Body (application/json)

| Name          | Description                                 |
| ------------- | ------------------------------------------- |
| id            | Unique identifier for the product           |
| name          | A label for the product                     |
| current_price | The sale value and currency for the product |
| value         | The sale value for the product              |
| currency_code | ISO 4217 code of the currency               |
#### Example 1 (product found with price)
##### URL

```
http://api.myretail.tacs/products/26396662
```

##### Status Code

`200`

##### Body

```json
{
    "id": 26396662,
    "name": "Exploding Kittens Game",
    "current_price": {
        "value": 19.99,
        "currency_code": "USD"
    }
}
```

#### Example 2 (product found without price)

##### URL
```
http://api.myretail.tacs/products/79144556
```

##### Status Code
`200`

##### Body

```json
{
    "id": 79144556,
    "name": "Wilson NCAA ICON Football"
}
```

### Error Response

#### Status Code
`404`

#### Body (application/json)
None
#### Example (product doesn't exist for provided id)
##### URL
```
http://api.myretail.tacs/products/15117729
```
##### Status Code

`404`

##### Body
None