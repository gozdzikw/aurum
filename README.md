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

###  4. Running tests

```bash
./gradlew test iT
```

## API Endpoints

# 1. Create account
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
