# MarsPads Server


# Server info

With this server user can :
- create an account 
- add and remove other people as friends
- view their friends list 
- Users can send chat requests to their friends
- they can then accept those requests and create chats 
- In those chats they can send messages to eachother which are stored in the database
- Users can also join a general chat and talk with all other users, these logs are not stored

## Features:

- 8 api endpoints
- Websockets implemented with SockJS that handle the public chatroom and private chatroom and also handle the subscriptions for push notifications
- Push notifications with [this](https://github.com/web-push-libs/webpush-java) library
