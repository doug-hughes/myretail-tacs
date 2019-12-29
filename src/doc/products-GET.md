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
| 403         | Product not found |

### Success Response
#### Status Code
`200`
#### Body (application/json)

| Name | Description |
| ---- | ----------- |
|      |             |
|      |             |
|      |             |
|      |             |
|      |             |
|      |             |
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
http://api.myretail.tacs/products/26396662
```

##### Status Code
`200`

##### Body

```json

```



### Error Response

#### Status Code
403

#### Body (application/json)

| Name | Description |
| ---- | ----------- |
|      |             |
|      |             |
|      |             |
|      |             |
|      |             |
|      |             |
#### Example
##### URL
##### Status Code
##### Body

```json

```