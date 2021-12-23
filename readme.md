# MarsPads Server

![badge](https://sonar.ti.howest.be/badges/project_badges/quality_gate?project=2021.project-ii:mars-server-17)
![badge](https://sonar.ti.howest.be/badges/project_badges/measure?project=2021.project-ii:mars-server-17&metric=bugs)
![badge](https://sonar.ti.howest.be/badges/project_badges/measure?project=2021.project-ii:mars-server-17&metric=code_smells)
![badge](https://sonar.ti.howest.be/badges/project_badges/measure?project=2021.project-ii:mars-server-17&metric=coverage)
![badge](https://sonar.ti.howest.be/badges/project_badges/measure?project=2021.project-ii:mars-server-17&metric=vulnerabilities)
![badge](https://sonar.ti.howest.be/badges/project_badges/measure?project=2021.project-ii:mars-server-17&metric=duplicated_lines_density)



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
  

* [OpenAPI-spec](https://project-ii.ti.howest.be/monitor/swagger-ui/?url=https://project-ii.ti.howest.be/monitor/apis/group-17)
  
* [ERD database diagram](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-17/documentation/-/blob/main/Eerd/Mars-17.png)




# Run Locally

1. Clone the project in a folder:
```bash
git clone git@git.ti.howest.be:TI/2021-2022/s3/analysis-and-development-project/projects/group-17/server.git
```

2. And also clone the documentation repository:
```bash
git clone git@git.ti.howest.be:TI/2021-2022/s3/analysis-and-development-project/projects/group-17/documentation.git
```

3. And make sure that these 2 folders client and server are in the same root folder

4. Then open your favorite Java editor with Gradle support

5. **Make sure there isn't anything else running on ports 8089 or 9000**

6. Run the project with Gradle run  

<br>

### The api is now accessible on: http://localhost:8089/  

### The eventbus path is: http://localhost:8089/events

