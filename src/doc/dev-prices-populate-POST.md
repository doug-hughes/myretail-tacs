# Populate Prices
Development convenience method to populate the local prices collection with data from the redsky.target.com service. Uses a keyword search to retrieve at most 16 tcin identifier and price and inserts locally with USD currency.

## Request

| Method | Request URL          |
| ------ | -------------------- |
| POST   | /dev/prices/populate |

### URL Path Parameters

None

### Query Parameters

| Name  | Description                                           |
| ----- | ----------------------------------------------------- |
| query | The keyword that will be used to retrieve sample data |
### Body
None
## Response
#### Status Codes

| Status Code | Denotes           |
| ----------- | ----------------- |
| 200         | Success           |
| 404         | Product not found |
| 500         | Server Error      |

### Success Response
#### Status Code
`200`
#### Body (application/json)

A JSON Array of tcin identifiers that were inserted

#### Example 1 (populate prices with keyword=kittens)
##### URL

```
http://api.myretail.tacs/dev/prices/populate?query=kittens
```

##### Status Code

`200`

##### Body

```json
[
    26396662,
    52423414,
    14721830,
    52092602,
    53859440,
    76780059,
    75666110,
    76780049,
    75878613,
    75878614,
    75878637,
    52093598,
    50052652,
    52616153,
    75878610,
    52619724,
    14910498,
    14766374
]
```

### Error Response

#### Status Code
`500`

#### Body (application/json)
None