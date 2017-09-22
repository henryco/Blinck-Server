# Blinck-Server <br><a href="https://codebeat.co/projects/github-com-henryco-blinck-server-master"><img alt="codebeat badge" src="https://codebeat.co/badges/ee5cad0d-2b6c-48b2-b52f-26adb3c698c2" /></a>
Blinck backend based on spring boot. <br>
~17k+ lines of high quality code with 70-80% test coverage.
<br>
<h2>Login</h2>
<h4>User JSON:</h4>

```json
 {
    "facebook_id":         "",
    "facebook_token":   ""
 }
 ```
 
 <h4>Admin JSON:</h4>

```json
 {
    "user_id":      "",
    "password":   ""
 }
 ```
 
 Endpoint | Method | Body | Response | &nbsp;
--- | --- | --- | --- | ---
**`/login/user`** | **POST** | *User JSON* | Authorization header | Login as user
**`/login/admin`** | **POST** | *Admin JSON* | Authorization header | Login as admin

<br>
<h2>Admin panel</h2>

 Endpoint | Method | Body | Response | &nbsp;
--- | --- | --- | --- | ---
**`/protected/admin/registration`** | **POST** | *Admin JSON* | 200 | Register new admin
todo

<br>
<h2> Public </h2>

 Endpoint | Method  | Response | &nbsp;
--- | --- | --- | ---
**`/public/about`** | **GET** | String | Get application info
**`/public/facebook/permissions`** | **GET** | String[] | Get required facebook permission
**`/rel/res/public/image/{resource}`** | **GET** | byte[] | Get image resource

<br>
<h2> Session </h2>

 <h4>StatusResponse JSON:</h4>

```json
 {
    "principal":      String,
    "active":          Boolean
 }
 ```
 
 Endpoint | Method | Authorization | Response | &nbsp;
--- | --- | --- | --- | ---
**`/session/user`** | **GET** | USER | StatusResponse JSON | Get user authorization info
**`/session/admin`** | **GET** | ADMIN | StatusResponse JSON | Get admin authorization info
**`/session/user/logout`** | **GET, POST** | USER | String | Logout user
**`/session/admin/logout`** | **GET, POST** | ADMIN | String | Logout admin

<br>
<h2> User profile </h2>

<h4>BioEntity JSON</h4>

```json
{
  "userName" : {
    "firstName" :  String,
    "secondName" :  String,
    "lastName" : String,
    "nickname" : String
  },
  "gender" : String,
  "about" : String,
  "birthday" : Long
}
```

<h4>MediaEntity JSON</h4>

```json
{
  "photo" : {
    "avatar" : String,
    "photos" : String
  }
}
```

<h4>PrivateProfile JSON</h4>

```json
{
  "email" : String
}
```

<h4>NameDetails JSON</h4>

```json
{
  "id" : Long,
  "user" : {
    "firstName" : String,
    "secondName" : String,
    "lastName" : String,
    "nickname" : String
  }
}
```

 Endpoint | Method | Authorization | Response | &nbsp;
--- | --- | --- | --- | ---
**`/session/user`** | **GET** | USER | StatusResponse JSON | Get user authorization info
**`/session/admin`** | **GET** | ADMIN | StatusResponse JSON | Get admin authorization info
**`/session/user/logout`** | **GET, POST** | USER | String | Logout user
**`/session/admin/logout`** | **GET, POST** | ADMIN | String | Logout admin

