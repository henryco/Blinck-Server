# Blinck-Server <br><a href="https://codebeat.co/projects/github-com-henryco-blinck-server-master"><img alt="codebeat badge" src="https://codebeat.co/badges/ee5cad0d-2b6c-48b2-b52f-26adb3c698c2" /></a>
Blinck backend based on spring boot. <br>
~17k+ lines of high quality code with 70-80% test coverage.
<br><br>

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

<br><br>
<h2>Admin panel</h2>

 Endpoint | Method | Body | Response | &nbsp;
--- | --- | --- | --- | ---
**`/protected/admin/registration`** | **POST** | *Admin JSON* | 200 | Register new admin
todo: documentation

<br><br>
<h2> Public </h2>

 Endpoint | Method  | Response | &nbsp;
--- | --- | --- | ---
**`/public/about`** | **GET** | String | Get application info
**`/public/facebook/permissions`** | **GET** | String[] | Get required facebook permission
**`/rel/res/public/image/{resource}`** | **GET** | byte[] | Get image resource

<br><br>
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

<br><br>
<h2> User profile </h2>

<h4>BioEntity JSON:</h4>

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

<h4>MediaEntity JSON:</h4>

```json
{
  "photo" : {
    "avatar" : String,
    "photos" : String
  }
}
```

<h4>PrivateProfile JSON:</h4>

```json
{
  "email" : String
}
```

<h4>NameDetails JSON:</h4>

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

 Endpoint | Method | Arguments | Response | &nbsp;
--- | --- | --- | --- | ---
**`/protected/user/profile`** | **GET** | **NONE** | BioEntity | Get user bio
**`/protected/user/profile/bio`** | **GET** | **Long:** id | BioEntity | Get user bio
**`/protected/user/profile/media`** | **GET** | **Long:** id | MediaEntity | Get user media
**`/protected/user/profile/priv`** | **GET** | **NONE** | PrivateProfile | Get user private profile
**`/protected/user/profile/find/one`** | **GET** | **String:** username | BioEntity | Find user profile
**`/protected/user/profile/find`** | **GET** | **String:** username <br>**Int:** page, size | NameDetails[] | Find user profiles
**`/protected/user/profile/update/bio`** | **POST** | **BODY:** BioEntity | Boolean | Update user bio
**`/protected/user/profile/update/nickname`** | **POST** | **BODY:** String | Boolean | Update username
**`/protected/user/profile/update/priv`** | **POST** | **BODY:** PrivateProfile | Boolean | Update private profile

<br><br>
<h2> User image media </h2>

<h4>UserImageInfo JSON:</h4>

```json
{
  "position" : Integer,
  "image" : String
}
```

 Endpoint | Method | Arguments | Response | &nbsp;
--- | --- | --- | --- | ---
**`/protected/user/media/image/max`** | **GET** | **NONE** | Integer | Get max numb of images
**`/protected/user/media/image/list`** | **GET** | **Long:** id | UserImageInfo[] | Get user images
**`/protected/user/media/image/avatar`** | **GET** | **Long:** id | String | Get user avatar
**`/protected/user/media/image/swap`** | **POST, GET** | **Int:** one, two | 200 | Swap user images
**`/protected/user/media/image/delete`** | **DELETE, POST** | **Int:** image | 200 | Delete user image
**`/protected/user/media/image/add`** | **POST** | **File:** image | 200 | Add user image
**`/protected/user/media/image/set`** | **POST** | **File:** image<br>**Int:** index | 200 | Set user image 
**`/protected/user/media/image/avatar`** | **POST** | **File:** image | 200 | Set user avatar

<br><br>
<h2> Report </h2>

 Endpoint | Method | Arguments | Response | &nbsp;
--- | --- | --- | --- | ---
**`/protected/user/report`** | **POST** | **Long:** id<br>**String:** reason | Boolean | Report user, reason might be null


<br><br>
<h2> Notifications </h2>

<h4>NotificationForm JSON:</h4>

```json
{
  "id" : Long,
  "type" : String,
  "info" : String,
  "timestamp" : Long
}
```

 Endpoint | Method | Arguments | Response | &nbsp;
--- | --- | --- | --- | ---
**`/protected/user/notifications/count`** | **GET** | **NONE** | Long | Get notifications count
**`/protected/user/notifications/list`** | **GET** | **Int:** page, size | NotificationForm[] | Get notifications
**`/protected/user/notifications/list/all`** | **GET** | **NONE** | NotificationForm[] | Get all notifications
**`/protected/user/notifications/list/all/pop`** | **GET** | **NONE** | NotificationForm[] | Pop all notifications
**`/protected/user/notifications/last`** | **GET** | **NONE** | NotificationForm | Get last notification
**`/protected/user/notifications/remove`** | **DELETE, <br>POST,<br> GET** | **Long:** id | 200 | Remove notification
**`/protected/user/notifications/remove`** | **DELETE, <br>POST,<br> GET** | **NONE** | 200 | Remove all notification

<br><h3>WebSocket:</h3>

Endpoint | Method | Response | &nbsp;
--- | --- | --- | ---
**`/user/queue/notification`** | **SUBSCRIBE** | NotificationForm | Receive user notificaions instantly

<br><h3>Notification types:</h3>

Type | Value
--- | ---
**FRIEND_MESSAGE_STOMP** | `"friendship_message_stomp"`
**FRIEND_MESSAGE_REST** | `"friendship_message_rest"`
**FRIEND_REQUEST** | `"friendship_request"`
**FRIEND_ACCEPTED** | `"friendship_accepted"`
**FRIEND_DECLINED** | `"friendship_declined"`
**FRIEND_DELETED** | `"friendship_deleted"`
**CUSTOM_SUB_PARTY_REMOVE** | `"custom_sub_party_removed"`
**CUSTOM_SUB_PARTY_JOIN** | `"custom_sub_party_joined"`
**CUSTOM_SUB_PARTY_INVITE** | `"custom_sub_party_invite"`
**CUSTOM_SUB_PARTY_LEAVE** | `"custom_sub_party_leave"`
**SUB_PARTY_MESSAGE_STOMP** | `"sub_party_message_stomp"`
**SUB_PARTY_MESSAGE_REST** | `"sub_party_message_rest"`
**SUB_PARTY_FOUND** | `"sub_party_found"`
**SUB_PARTY_IN_QUEUE** | `"sub_party_in_queue"`
**PARTY_MESSAGE_STOMP** | `"party_message_stomp"`
**PARTY_MESSAGE_REST** | `"party_message_rest"`
**PARTY_FOUND** | `"party_found"`
**PARTY_MEETING_SET** | `"party_meeting_set"`
**PARTY_MEETING_PROPOSITION** | `"party_meeting_proposition"`
**PARTY_MEETING_VOTE** | `"party_meeting_vote"`
**PARTY_MEETING_VOTE_FINAL** | `"party_meeting_vote_final"`
**PARTY_MEETING_VOTE_FINAL_FAIL** | `"party_meeting_vote_final_fail"`
**PARTY_MEETING_VOTE_FINAL_SUCCESS** | `"party_meeting_vote_final_success"`
**QUEUE_LEAVE** | `"queue_leave"`


<br><br>
<h2> Matching </h2>

<h4> TypeForm JSON: </h4>

```json
{
  "ident" : String,
  "wanted" : String,
  "dimension" : Integer,
  "ages": String
}
```

Endpoint | Method | Arguments | Response | &nbsp;
--- | --- | --- | --- | ---
**`/protected/user/match/queue/solo`** | **POST** | **BODY:** TypeForm | 200 | Enter to solo queue
**`/protected/user/match/queue/list`** | **GET** | **NONE** | Long[] | Get id's of user rooms in queue
**`/protected/user/match/queue/leave`** | **POST,<br> DELETE** | **Long:** id | 200 | Leave room in queue
**`/protected/user/match/queue/custom`** | **POST** | **BODY:** TypeForm | Long | Create cutom room and returns its ID
**`/protected/user/match/queue/custom/delete`** | **DELETE** | **Long:** id | Boolean | Delete cutom room
**`/protected/user/match/queue/custom/list`** | **GET** | **NONE** | Long[] | Get id's of users custom rooms
**`/protected/user/match/queue/custom/join`** | **POST** | **Long:** id | Boolean | Join to custom room
**`/protected/user/match/queue/custom/invite`** | **POST** | **Long:** id<br>**BODY:** Long[] | Boolean | Invite users to room
**`/protected/user/match/queue/custom/leave`** | **POST,<br> DELETE** | **Long:** id | Boolean | Leave custom room
**`/protected/user/match/queue/custom/start`** | **POST** | **Long:** id | Boolean | Move custom room to queue


<br><br>
<h2> Friendship </h2>

<h4>Friendship JSON: </h4>

```json
{
  "friendship" : Long,
  "timestamp" : Long,
  "user_1" : Long,
  "user_2": Long
}
```

<h4>SimpFriendship JSON: </h4>

```json
{
  "friendship" : Long,
  "friend" : Long
}
```

<h4>FriendshipNotification JSON: </h4>

```json
{
  "notification" : Long,
  "from" : Long,
  "to" : Long,
  "timestamp": Long
}
```

Endpoint | Method | Arguments | Response | &nbsp;
--- | --- | --- | --- | ---
**`/protected/user/friends/count`** | **GET** | **NONE**  | Long | Get friends count
**`/protected/user/friends/list`** | **GET** | **Int:** page, size  | Long[] | Get friends ID list
**`/protected/user/friends/detailed`** | **GET** | **Long:** id  | Friendship | Get friendship
**`/protected/user/friends/add`** | **POST,<br> GET** | **Long:** user_id  | 200 | Add friend
**`/protected/user/friends/remove`** | **DELETE,<br> POST,<br> GET** | **Long:** user_id  | 200 | Delete user
**`/protected/user/friends/request/accept`** | **POST,<br> GET** | **Long:** user_id  | 200 | Accept friend request
**`/protected/user/friends/request/decline`** | **POST,<br> GET** | **Long:** user_id  | 200 | Decline friend request
**`/protected/user/friends/request/direct/delete`** | **DELETE,<br> POST,<br> GET** | **Long:** id  | 200 | Delete friend request
**`/protected/user/friends/request/list/income`** | **GET** | **Int:** page, size  | FriendshipNotification[] | Get friend requests
**`/protected/user/friends/request/list/outcome`** | **GET** | **Int:** page, size  | FriendshipNotification[] | Get own friend requests

<br><h3>Conversation REST:</h3>
<h4>MessageForm JSON:</h4>

```json
{
  "topic" : Long,
  "author" : Long,
  "message" : String,
  "timestamp": Long
}
```

Endpoint | Method | Arguments | Response | &nbsp;
--- | --- | --- | --- | ---
**`/protected/user/friends/conversation/messages/count`** | **GET** | **Long:** id  | Long | Count messages
**`/protected/user/friends/conversation/messages/list`** | **GET** |  **Long:** id<br> **Int:** page, size  | MessageForm[] | Get messages
**`/protected/user/friends/conversation/messages/last`** | **GET** | **Long:** id  | MessageForm | Get last message
**`/protected/user/friends/conversation/messages/send`** | **POST** | **BODY:** MessageForm  | 200 | Send message
**`/protected/user/friends/conversation/remove`** | **DELETE** | **Long:** id  | 200 | Delete conversation

<br><h3>Conversation STOMP:</h3>
<h4>StatusForm JSON:</h4>

```json
{
  "destination" : String,
  "timestamp": Long,
  "status": Boolean
}
```

Endpoint | Method | Payload | &nbsp;
--- | --- | --- | ---
**`/user/message/friendship/{friendship_id}`** | **SUBSCRIBE** | MessageForm | Receive friendship messages
**`/user/message/friendship/stat`** | **SUBSCRIBE** | StatusForm | Receive status of sended messages
**`/app/message/friendship`** | **SEND** | MessageForm | Send message to friendship


<br><br>
<h2> SubParty </h2>

<h4>Type JSON: </h4>

```json
{
  "ident" : String,
  "wanted" : String,
  "dimension" : Integer
}
```

<h4>Info JSON: </h4>

```json

```

Endpoint | Method | Arguments | Response | &nbsp;
--- | --- | --- | --- | ---
**`/protected/user/subgroup/details`** | **GET** | **Long:** id  | Info | Get subparty info
**`/protected/user/subgroup/details/group`** | **GET** | **Long:** id  | Long | Get parent party id
**`/protected/user/subgroup/details/type`** | **GET** | **Long:** id  | Type | Get subparty type
**`/protected/user/subgroup/details/users`** | **GET** | **Long:** id  | Long[] | Get subparty users id's
**`/protected/user/subgroup/list/id`** | **GET** | **NONE** | Long[] | Get subparty id list
**`/protected/user/subgroup/list/details`** | **GET** | **NONE** | Info[] | Get subparty info list
**`/protected/user/subgroup/leave`** | **DELETE,<br>POST,<br>GET** | **Long:** id | 200 | Leave subparty (and parent party)







