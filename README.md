# **SOCIAL MEDIA API - JAVA 17 - APACHE MAVEN - POSTGRES DATABASE**

## SETUP

### 1. LOCAL
- Git clone
- Setup postgres database locally or run a docker image. Check application.properties for connection uri, username &  passowrd
- Run `mvn spring-boot:run` in terminal. Service on port 8080

### 2. DOCKER
- cd into this directory and run command below to build docker image:
 ```
docker build -t social-media-api:1.0 .
```
- Run docker-compose.yml with the command below:
```
docker-compose up -d
```


## BUILD PROCESS

- Didn't get the time to test anything, but all should work as expected

-  instead of saving the base64 string directly into the  db, we can convert the base64 into a file, save to s3 bucket, 
then get the url and send to frontend

- no need to include lists of posts with User; same can be said about including lists of comments in posts

- when you get a user, you don't necessarily need to get all his posts, since we using the same user for spring
security. For Posts and comments tho, most times when you get a post, you'd most likely need all the comments, 
so you can save your DB another call by hitting again to find the comments for a post. It could also bounce back in
your face if you always loading a list of comments when you only need the post, it'll increase latency. Hopefully
frontend uses the pagination well to reduce response time

- created FollowerUser so your list of followers and following is not referencing the same User entity; and also so
we can control the amount of information when a user gets his lists of followers. You can decide to also save 
FollowerUser in its own table as well. I didn't for no particular reason. 

- if you going to save FollowerUser to its own table, then its relationship with user would switch to Many-to-Many, since 
a FollowerUser is a user and he can follow many people as well

- changed my mind, I'm saving FollowerUser to its table and switching to Many-to-Many

- you should have a seperate endpoint for adding followers. To save time, I combined all of them in the edit user 
endpoint. Ideally edit user should just edit user details, so they should be at least 2-4 endpoint (plus another 
endpoint to activate user). I just lumped all of them into a single edit user endpoint to save time. 

- update password should be a separate endpoint too, since its implementation would follow a whole other process i.e
sending links to email, or otp to phone number, etc

- also should have used more helper methods in edit user since its doing a lot of stuff

- To implement liking a post, create a Like entity with User &  Post entity, both with Many-to-One, create a 
LikeService. When a user likes a post just call the service and associate that post with that user in the LikeRepository

- would be better off if all ids where UUID instead of Long

- Wrote tests for unit & integration tests for User. It'll be similar for Post & Comment