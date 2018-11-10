# **Blinck-Server** [![codebeat badge](https://codebeat.co/badges/ee5cad0d-2b6c-48b2-b52f-26adb3c698c2)](https://codebeat.co/projects/github-com-henryco-blinck-server-master) [![Build Status](http://174.138.0.194:1997/buildStatus/icon?job=tinder-samurai/blinck-server/master)](http://174.138.0.194:1997/job/tinder-samurai/job/blinck-server/job/master/)
Blinck backend based on spring boot.   
High quality code with 70-80% test coverage.  

____


# Login
#### User JSON:

``` json
 {
    "facebook_uid":      "",
    "facebook_token":   ""
 }
```
 
#### Admin JSON:

``` json
 {
    "user_id":    "",
    "password":   ""
 }
```
 
#### HTTP Authorization header:
 
 ```
   Authorization:    JWT String
 ```
 
 Endpoint | Method | Body | Response | &nbsp;
--- | --- | --- | --- | ---
**`/login/user`** | **POST** | *User JSON* | HTTP Authorization header | Login as user  (register automaticaly)
**`/login/admin`** | **POST** | *Admin JSON* | HTTP Authorization header | Login as admin
**`/protected/admin/registration`** | **POST** | *Admin JSON* | 200 | Register new admin

### WebSocket:
#### Connection header:

```
  User:                   Long
  Authorization:    JWT String
```

 Endpoint | Method  | Connection header | HTTP Authorization Header | &nbsp;
--- | --- | --- | --- | ---
**`/stomp`** | **CONNECT** | YES | YES | Connect to server via jwt

____

# Public

 Endpoint | Method  | Response | &nbsp;
--- | --- | --- | ---
**`/public/about`** | **GET** | ```String``` | Get application info
**`/public/facebook/permissions`** | **GET** | ```String[]``` | Get required facebook permission
**`/rel/res/public/images/{resource}`** | **GET** | ```byte[]``` | Get image resource

____

# Session 

#### StatusResponse JSON:

```json
 {
    "principal":      String,
    "active":          Boolean
 }
```
 
 Endpoint | Method | Authorization | Response | &nbsp;
--- | --- | --- | --- | --- 
**`/session/user`** | **GET** | USER | ```StatusResponse JSON``` | Get user authorization info
**`/session/admin`** | **GET** | ADMIN | ```StatusResponse JSON``` | Get admin authorization info
**`/session/user/logout`** | **GET, POST** | USER | ```String``` | Logout user
**`/session/admin/logout`** | **GET, POST** | ADMIN | ```String``` | Logout admin

____

# User profile

#### BioEntity JSON:

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

#### MediaEntity JSON:

```json
{
  "photo" : {
    "avatar" : String,
    "photos" : String
  }
}
```

#### PrivateProfile JSON:

```json
{
  "email" : String
}
```

#### NameDetails JSON:

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
**`/protected/user/profile/bio`** | **GET** | ```Long```: id | ```BioEntity``` | Get user bio
**`/protected/user/profile/media`** | **GET** | ```Long```: id | ```MediaEntity``` | Get user media
**`/protected/user/profile/priv`** | **GET** | **NONE** | ```PrivateProfile``` | Get user private profile
**`/protected/user/profile/find/one`** | **GET** | ```String```: username | ```BioEntity``` | Find user profile
**`/protected/user/profile/find`** | **GET** | ```String```: username ```Int```: page, size | ```NameDetails[]``` | Find user profiles
**`/protected/user/profile/update/bio`** | **POST** | **BODY:** ```BioEntity``` | ```Boolean```| Update user bio
**`/protected/user/profile/update/nickname`** | **POST** | **BODY:** ```String``` | ```Boolean``` | Update username
**`/protected/user/profile/update/priv`** | **POST** | **BODY:** ```PrivateProfile``` | ```Boolean``` | Update private profile

____

# User image media

#### UserImageInfo JSON:

```json
{
  "position" : Integer,
  "image" : String
}
```

 Endpoint | Method | Arguments | Response | &nbsp;
--- | --- | --- | --- | ---
**`/protected/user/media/image/max`** | **GET** | **NONE** | ```Integer``` | Get max numb of images
**`/protected/user/media/image/list`** | **GET** | ```Long```: id | ```UserImageInfo[]``` | Get user images
**`/protected/user/media/image/avatar`** | **GET** | ```Long```: id | ```String``` | Get user avatar
**`/protected/user/media/image/swap`** | **POST, GET** | ```Int```: one, two | 200 | Swap user images
**`/protected/user/media/image/delete`** | **DELETE, POST** | ```Int```: image | 200 | Delete user image
**`/protected/user/media/image/add`** | **POST** | **File:** image | 200 | Add user image
**`/protected/user/media/image/set`** | **POST** | **File:** image ```Int```: index | 200 | Set user image 
**`/protected/user/media/image/avatar`** | **POST** | **File:** image | 200 | Set user avatar

____

#  Report 

 Endpoint | Method | Arguments | Response | &nbsp;
--- | --- | --- | --- | ---
**`/protected/user/report`** | **POST** | ```Long```: id ```String```: reason | ```Boolean``` | Report user, reason might be null


____

#  Notifications

#### NotificationForm JSON:

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
**`/protected/user/notifications/count`** | **GET** | **NONE** | ```Long``` | Get notifications count
**`/protected/user/notifications/list`** | **GET** | **Int:** page, size | ```NotificationForm[]``` | Get notifications
**`/protected/user/notifications/list/all`** | **GET** | **NONE** | ```NotificationForm[]``` | Get all notifications
**`/protected/user/notifications/list/all/pop`** | **GET** | **NONE** | ```NotificationForm[]``` | Pop all notifications
**`/protected/user/notifications/last`** | **GET** | **NONE** | ```NotificationForm``` | Get last notification
**`/protected/user/notifications/remove`** | **DELETE,  POST, GET** | **Long:** id | 200 | Remove notification
**`/protected/user/notifications/remove`** | **DELETE,  POST, GET** | **NONE** | 200 | Remove all notification

### WebSocket:

Endpoint | Method | Response | &nbsp;
--- | --- | --- | ---
**`/user/queue/notification`** | **SUBSCRIBE** | ```NotificationForm``` | Receive user notificaions instantly

### Notification types:

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

____

# Matching

#### TypeForm JSON:

```json
{
  "ident" : Gender,
  "wanted" : Gender,
  "dimension" : Dimension,
  "ages": String
}
```


#### Type Dimensions: 

Dimension | Value
--- | ---
**1x1** | 1
**2x2** | 2
**3x3** | 3
**5x5** | 5

#### Type Genders:

Gender | Value
--- | ---
**MALE** | "male"
**FEMALE** | "female"
**BOTH** | "both"



Endpoint | Method | Arguments | Response | &nbsp;
--- | --- | --- | --- | ---
**`/protected/user/match/queue/solo`** | **POST** | **BODY:** ```TypeForm``` | 200 | Enter to solo queue
**`/protected/user/match/queue/list`** | **GET** | **NONE** | ```Long[]``` | Get id's of user rooms in queue
**`/protected/user/match/queue/leave`** | **POST, DELETE** | **Long:** id | 200 | Leave room in queue
**`/protected/user/match/queue/custom`** | **POST** | **BODY:** ```TypeForm``` | ```Long``` | Create cutom room and returns its ID
**`/protected/user/match/queue/custom/delete`** | **DELETE** | **Long:** id | ```Boolean``` | Delete cutom room
**`/protected/user/match/queue/custom/list`** | **GET** | **NONE** | ```Long[]``` | Get id's of users custom rooms
**`/protected/user/match/queue/custom/join`** | **POST** | **Long:** id | ```Boolean``` | Join to custom room
**`/protected/user/match/queue/custom/invite`** | **POST** | **Long:** id **BODY:** ```Long[]``` | Boolean | Invite users to room
**`/protected/user/match/queue/custom/leave`** | **POST,  DELETE** | **Long:** id | ```Boolean``` | Leave custom room
**`/protected/user/match/queue/custom/start`** | **POST** | **Long:** id | ```Boolean``` | Move custom room to queue

____


# Friendship

#### Friendship JSON: 

```json
{
  "friendship" : Long,
  "timestamp" : Long,
  "user_1" : Long,
  "user_2": Long
}
```

#### SimpFriendship JSON:

```json
{
  "friendship" : Long,
  "friend" : Long
}
```

#### FriendshipNotification JSON:

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
**`/protected/user/friends/count`** | **GET** | **NONE**  | ```Long``` | Get friends count
**`/protected/user/friends/list`** | **GET** | **Int:** page, size  | ```Long[]``` | Get friends ID list
**`/protected/user/friends/detailed`** | **GET** | **Long:** id  | ```Friendship``` | Get friendship
**`/protected/user/friends/add`** | **POST, GET** | **Long:** user_id  | 200 | Add friend
**`/protected/user/friends/remove`** | **DELETE, POST, GET** | **Long:** user_id  | 200 | Delete user
**`/protected/user/friends/request/accept`** | **POST, GET** | **Long:** user_id  | 200 | Accept friend request
**`/protected/user/friends/request/decline`** | **POST,  GET** | **Long:** user_id  | 200 | Decline friend request
**`/protected/user/friends/request/direct/delete`** | **DELETE, POST,  GET** | **Long:** id  | 200 | Delete friend request
**`/protected/user/friends/request/list/income`** | **GET** | **Int:** page, size  | ```FriendshipNotification[]``` | Get friend requests
**`/protected/user/friends/request/list/outcome`** | **GET** | **Int:** page, size  | ```FriendshipNotification[]``` | Get own friend requests

### Conversation REST:
#### MessageForm JSON:

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
**`/protected/user/friends/conversation/messages/count`** | **GET** | **Long:** id  | ```Long``` | Count messages
**`/protected/user/friends/conversation/messages/list`** | **GET** |  **Long:** id **Int:** page, size  | ```MessageForm[]``` | Get messages
**`/protected/user/friends/conversation/messages/last`** | **GET** | **Long:** id  | ```MessageForm``` | Get last message
**`/protected/user/friends/conversation/messages/send`** | **POST** | **BODY:** ```MessageForm```  | 200 | Send message
**`/protected/user/friends/conversation/remove`** | **DELETE** | **Long:** id  | 200 | Delete conversation

### Conversation STOMP:
#### StatusForm JSON:

```json
{
  "destination" : String,
  "timestamp": Long,
  "status": Boolean
}
```

Endpoint | Method | Payload | &nbsp;
--- | --- | --- | ---
**`/user/message/friendship/{friendship_id}`** | **SUBSCRIBE** | ```MessageForm``` | Receive friendship messages
**`/user/message/friendship/stat`** | **SUBSCRIBE** | ```StatusForm``` | Receive status of sended messages
**`/app/message/friendship`** | **SEND** | ```MessageForm``` | Send message to friendship


# SubParty

#### Type JSON: 

```json
{
  "ident" : String,
  "wanted" : String,
  "dimension" : Integer
}
```

#### Info JSON: 

```json
{
  "id" : Long,
  "party" : Long,
  "type" : {
    "ident" : String,
    "wanted" : String,
    "dimension" : Integer
  },
  "users" : [ Long ]
}

```

Endpoint | Method | Arguments | Response | &nbsp;
--- | --- | --- | --- | ---
**`/protected/user/subgroup/details`** | **GET** | **Long:** id  | ```Info``` | Get subparty info
**`/protected/user/subgroup/details/group`** | **GET** | **Long:** id  | ```Long``` | Get parent party id
**`/protected/user/subgroup/details/type`** | **GET** | **Long:** id  | ```Type``` | Get subparty type
**`/protected/user/subgroup/details/users`** | **GET** | **Long:** id  | ```Long[]``` | Get subparty users id's
**`/protected/user/subgroup/list/id`** | **GET** | **NONE** | ```Long[]``` | Get subparty id list
**`/protected/user/subgroup/list/details`** | **GET** | **NONE** | ```Info[]``` | Get subparty info list
**`/protected/user/subgroup/leave`** | **DELETE, POST, GET** | **Long:** id | 200 | Leave subparty (and parent party)

### Conversation REST: 
#### MessageForm JSON: 

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
**`/protected/user/subgroup/conversation/messages/count`** | **GET** | **Long:** id  | ```Long``` | Count messages
**`/protected/user/subgroup/conversation/messages/list`** | **GET** |  **Long:** id  **Int:** page, size  | ```MessageForm[]``` | Get messages
**`/protected/user/subgroup/conversation/messages/send`** | **POST** | **BODY:** ```MessageForm```  | 200 | Send message

### Conversation STOMP:
#### StatusForm JSON:

```json
{
  "destination" : String,
  "timestamp": Long,
  "status": Boolean
}
```

Endpoint | Method | Payload | &nbsp;
--- | --- | --- | ---
**`/user/message/subgroup/{subgroup_id}`** | **SUBSCRIBE** | ```MessageForm``` | Receive subgroup messages
**`/user/message/subgroup/stat`** | **SUBSCRIBE** | ```StatusForm``` | Receive status of sended messages
**`/app/message/subgroup`** | **SEND** | ```MessageForm``` | Send message to subgroup


____

# Party

#### PartyInfo JSON: 

```json
{
  "id" : Long,
  "meeting" : {
    "time" : Long,
    "active_after" : Long,
    "venue" : String
  },
  "sub_parties" : [ Long ],
  "users" : [ Long ]
}
```

#### Meeting JSON:

```json
{
  "time" : Long,
  "active_after" : Long,
  "venue" : String
}
```

Endpoint | Method | Arguments | Response | &nbsp;
--- | --- | --- | --- | ---
**`/protected/user/group/details`** | **GET** | **Long:** id  | ```PartyInfo``` | Get party info
**`/protected/user/group/details/meeting`** | **GET** | **Long:** id  | ```Meeting``` | Get party meeting
**`/protected/user/group/details/user`** | **GET** | **Long:** id  | ```Long[]``` | Get party users id's
**`/protected/user/group/details/isactive`** | **GET** | **Long:** id  | ```Boolean``` | Get party status
**`/protected/user/group/list/id`** | **GET** | **NONE**  | ```Long[]``` | Get parties id list
**`/protected/user/group/list/details`** | **GET** | **NONE**  | ```PartyInfo[]``` | Get parties info list

### Meetings:
#### OfferInfo JSON:

```json
{
  "id" : Long,
  "votes" : Integer,
  "offer" : {
    "time" : Long,
    "active_after" : Long,
    "venue" : String
  }
}
```

Endpoint | Method | Arguments | Response | &nbsp;
--- | --- | --- | --- | ---
**`/protected/user/group/meeting/list`** | **GET** | **Long:** id  | ```OfferInfo``` | Get proposed meeting list
**`/protected/user/group/meeting/propose`** | **POST** | **Long:** id **BODY:** Meeting  | ```Boolean``` | Propose meeting
**`/protected/user/group/meeting/vote`** | **POST, GET** | **Long:** proposition **Boolean:** option  | 200 | Vote
**`/protected/user/group/meeting/vote/final`** | **POST, GET** | **Long:** proposition  **Boolean:** option  | 200 | Vote final

### Conversation REST:
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
**`/protected/user/group/conversation/messages/count`** | **GET** | **Long:** id  | ```Long``` | Count messages
**`/protected/user/group/conversation/messages/list`** | **GET** |  **Long:** id **Int:** page, size  | ```MessageForm[]``` | Get messages
**`/protected/user/group/conversation/messages/send`** | **POST** | **BODY:** MessageForm  | 200 | Send message

### Conversation STOMP:
#### StatusForm JSON:

```json
{
  "destination" : String,
  "timestamp": Long,
  "status": Boolean
}
```

Endpoint | Method | Payload | &nbsp;
--- | --- | --- | ---
**`/user/message/group/{group_id}`** | **SUBSCRIBE** | MessageForm | Receive party messages
**`/user/message/group/stat`** | **SUBSCRIBE** | StatusForm | Receive status of sended messages
**`/app/message/group`** | **SEND** | MessageForm | Send message to party



# Admin panel 

#### SimpNotification JSON:

```json
{
  "receiver_id" : Long,
  "type" : String,
  "notification" : String
}
```

 Endpoint | Method | Arguments | Response | &nbsp;
--- | --- | --- | --- | ---
**`/protected/admin/list`** | **GET** | **NONE** | ```String[]``` | Get admin id's list
**`/protected/admin/verification`** | **POST,  GET** | **Int:** size | ```String[]``` | Get admin verification id's list
**`/protected/admin/activate`** | **POST** | **BODY:** ```String[]``` | 200 | Activate admin profiles
**`/protected/admin/authority/add`** | **POST,  GET** | **String:** name, role | 200 | Add authority to admin
**`/protected/admin/authority/remove`** | **POST,  GET** | **String:** name, role | 200 | Remove authority from admin
**`/protected/admin/authority/session/close`** | **POST, GET** | **String:** name | 200 | Close user session
**`/protected/admin/authority/session/admin`** | **POST,  GET** | **String:** name | 200 | Close admin session
**`/protected/admin/notification/user`** | **POST** | **BODY:** ```SimpNotification``` | 200 | Send notification to user

### Authentication and Bans
#### UserAuth JSON:

```json
{
  "id" : Long,
  "locked" : Boolean,
  "authorities" : String
}
```

#### ReportList JSON:

```json
{
  "id" : Long,
  "reason" : String,
  "reporter_id" : Long,
  "reported_id" : Long
}
```

 Endpoint | Method | Arguments | Response | &nbsp;
--- | --- | --- | --- | ---
**`/protected/admin/bans/count/user`** | **GET** | **NONE** | ```Long``` | Get locked users count
**`/protected/admin/bans/list/user`** | **GET** | **Int:** page, size | ```UserAuth[]``` | Get locked users list
**`/protected/admin/bans/details/user`** | **GET** | **Long:** id | ```UserAuth``` | Get locked users
**`/protected/admin/bans/lock/user`** | **POST,  GET** | **Long:** id **Boolean:** option | 200 | Lock (or not) user
**`/protected/admin/bans/reporst/user/count`** | **GET** | **NONE** | ```Long``` | Get user reports count
**`/protected/admin/bans/reporst/user/list`** | **GET** | **Long:** id | ```ReportList[]``` | Get user reports list
**`/protected/admin/bans/reporst/user/list/all`** | **GET** | **Int:** page, size | ```ReportList[]``` | Get all users reports
**`/protected/admin/bans/reporst/user/clear`** | **POST,  GET** | **Long:** id | 200 | Clear user reports history

### Matcher monitor
#### I'm highly recommend to don't use it.
#### Party JSON:

```json
{
  "id" : Long,
  "meeting" : {
    "time" : Long,
    "active_after" : Long,
    "venue" : String
  },
  "subParties" : [ Long ],
  "details" : {
    "type" : {
      "ident" : String,
      "wanted" : String,
      "dimension" : Integer
    },
    "inQueue" : Boolean
  }
}
```

#### SubParty JSON:

```json
{
  "id" : Long,
  "party" : {
    "id" : Long,
    "meeting" : {
      "time" : Long,
      "active_after" : Long,
      "venue" : String
    },
    "subParties" : [ Long ],
    "details" : {
      "type" : {
        "ident" : String,
        "wanted" : String,
        "dimension" : Integer
      },
      "inQueue" : Boolean
    }
  },
  "details" : {
    "type" : {
      "ident" : String,
      "wanted" : String,
      "dimension" : Integer
    },
    "inQueue" : Boolean
  },
  "users" : [ Long ]
}
```

 Endpoint | Method | Arguments | Response | &nbsp;
--- | --- | --- | --- | ---
**`/protected/admin/monitor/party/all`** | **GET** | **NONE** | ```Party[]``` | Get all parties
**`/protected/admin/monitor/subparty/all`** | **GET** | **NONE** | ```SubParty[]``` | Get all subparties



