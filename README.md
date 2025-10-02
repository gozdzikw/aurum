# Aurum – Bank Account & Currency Exchange API

This is a simple Spring Boot application that allows users to create bank accounts 
and perform currency exchange (PLN ↔ USD) using live exchange rates 
from the NBP.

---

## Features

- Create a user account with an initial balance (PLN and USD)
- Retrieve account balances
- Exchange currencies between PLN and USD using live rates from NBP
- All operations exposed via REST API


## Running the Application

### 1. Clone the repository

```bash
git clone https://github.com/gozdzikw/aurum.git
cd aurum
```

### 2. Build the project

```bash
./gradlew build
```

### 3. Run the application

```bash
./gradlew bootRun
```

### 3. Check application state

```bash
http://localhost:8080/actuator/health
```

## Tests & database

### 1. Running tests

```bash
./gradlew test iT
```
### 2. Check database console

```bash
  http://localhost:8080/h2-console
```

## API Endpoints

# 1. Create account

Request
```bash
POST /accounts
```
Request body:
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "initialBalance": 999
}
```
Response
```json
{
  "accountId":1,
  "userId":1,
  "firstName":"John",
  "lastName":"Doe",
  "balancePLN":999.00,
  "balanceUSD":0.00
}
```
cURL example
```bash
curl -X POST http://localhost:8080/accounts \
  -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Doe","initialBalance":999}'
```

# 2. Get account balance

Request
```bash
GET /accounts/{accountId}
```
Response
```json
{
  "accountId": 1,
  "userId": 1,
  "firstName": "John",
  "lastName": "Doe",
  "balancePLN": 999,
  "balanceUSD": 0
}
```
cURL example
```bash
curl http://localhost:8080/accounts/1
```
# 3. Exchange currency

Request
```bash
POST /api/exchange
```
Request body:
```json
{
  "accountId": 1,
  "from": "PLN",
  "to": "USD",
  "amount": 500
}
```
Response
```json
{
  "accountId":1,
  "balancePLN":500.00,
  "balanceUSD":138.08,
  "rate":3.6210
}
```
cURL example
```bash
curl -X POST http://localhost:8080/api/exchange \
  -H "Content-Type: application/json" \
  -d '{"accountId":1,"from":"PLN","to":"USD","amount":500}'
```


