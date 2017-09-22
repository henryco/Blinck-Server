# Blinck-Server <br><a href="https://codebeat.co/projects/github-com-henryco-blinck-server-master"><img alt="codebeat badge" src="https://codebeat.co/badges/ee5cad0d-2b6c-48b2-b52f-26adb3c698c2" /></a>
Blinck backend based on spring boot. <br>
~15k+ lines of high quality code with 70-80% test coverage.
<br>

<h2>Api endpoints</h2>
<br>
<h4>User login JSON:</h4>

```json
 {
    "facebook_id":         "",
    "facebook_token":   ""
 }
 ```
 
 <h4>Admin login JSON:</h4>

```json
 {
    "user_id":      "",
    "password":   ""
 }
 ```
 
 Endpoint | Method | Body | Response | &nbsp;
--- | --- | --- | --- | ---
**`/login/user`** | **POST** | *User login JSON* | Authorization header | Login as user
**`/login/admin`** | **POST** | *Admin login JSON* | Authorization header | Login as admin
