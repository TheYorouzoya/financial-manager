# API Docuementation

The api definitions can be accessed via the swagger UI link on `http://localhost:8080/swagger-ui/index.html` once the app is up and running. Here's a list of some of the routes with example requests:

## Auth Routes: `/api/v1/auth`

### `POST` `/register`

**Description:**  
Endpoint to register a new user

### Request

**Method:** `POST`  
**Authentication Required:** `No`  
**Content-Type:** `application/json`

#### Request Body

```json
{
  "username": "string",     // Required
  "email": "string",        // Required
  "password": "stringst",   // Required
  "firstName": "string",
  "lastName": "string",
  "phoneNumber": "string",
  "profilePictureURL": "string"
}
```

---

### ✅ Success Response

**Status Code:** `200 OK`  
**Content-Type:** `application/json`

```json
{
  "username": "string",     
  "email": "string",
  "firstName": "string",
  "lastName": "string",
  "phoneNumber": "string",
  "profilePictureUrl": "string"
}
```



### ❌ Error Responses

#### 🔹 `400 Bad Request` — Validation Error

```json
{
  "email": "Email is required.",
  "username": "Username is required."
}
```

#### 🔹 `409 Conflict` — Duplicate Resource

```json
{
  "error": "Resource already exists",
  "message": "Username is already in use."
}
```

---

### `POST` `/login`

**Description:**  
Endpoint to login a user.

### Request

**Method:** `POST`  
**Authentication Required:** `No`  
**Content-Type:** `application/json`

#### Request Body

```json
{
  "username": "string",   // Required
  "password": "string"    // Required
}
```

---

### ✅ Success Response

**Status Code:** `200 OK`  
**Content-Type:** `application/json`

```json
{
  "accessToken": "string",
  "refreshToken": "string"
}
```



### ❌ Error Responses

#### 🔹 `400 Bad Request` — Validation Error

```json
{
  "username": "username cannot be blank"
}
```

#### 🔹 `401 Unauthorized` — Bad Credentials

```json
{
  "error": "Unauthorized",
  "message": "Bad credentials"
}
```

---  


### `POST` `/refresh`

**Description:**  
Endpoint to refresh the user's access token.

### Request

**Method:** `POST`  
**Authentication Required:** `Yes`  
**Content-Type:** `application/json`

#### Request Body

```json
{
  "id": "string",   // Required
}
```

---

### ✅ Success Response

**Status Code:** `200 OK`  
**Content-Type:** `application/json`

```json
{
  "accessToken": "string",
  "refreshToken": "string"
}
```

### ❌ Error Responses

#### 🔹 `401 Unauthorized` — Bad Credentials

```json
{
  "error": "Unauthorized",
  "message": "Refresh Token Id is required."
}
```

```json
{
  "error": "Unauthorized",
  "message": "Refresh Token not found."
}
```

```json
{
  "error": "Unauthorized",
  "message": "Refresh Token already expired or revoked."
}
```

---  


### `POST` `/logout`

**Description:**  
Endpoint to refresh the user's access token.

### Request

**Method:** `POST`  
**Authentication Required:** `Yes`  
**Content-Type:** `application/json`

---

### ✅ Success Response

**Status Code:** `204 No Content`  

### ❌ Error Responses

#### 🔹 `401 Unauthorized`

```json
{
  "error": "Unauthorized",
  "message": "Token has been blacklisted."
}
```

---  
