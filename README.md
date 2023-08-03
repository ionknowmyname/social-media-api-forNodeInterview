





SETUP 

cd into this directory and run command below to build docker image:
docker build -t name-of-your-choice:1.0 .

Run docker-compose.yml with the command below:
docker-compose up


instead of saving the base64 string directly into the  db, we can convert the base64 into a file, save to s3 bucket, 
then get the url and send to frontend


Didn't get the time to test anything, but all should work as expected

// no need to include lists of posts with User; same can be said about including lists of comments in posts
// when you get a user, you don't necessarily need to get all his posts, since we using the same user for spring
security. For Posts and comments tho, most times when you  get a post, you'd most likely need all the comments, 
so you can save your DB another call by hitting again to find the comments for a post. It could also bounce back in
your face if you always loading a list of comments when you only need the post, it'll increase latency. Hopefully
frontend uses the pagination well to reduce response time


// created FollowerUser so your list of followers and following is not referencing the same User entity; and also so
we can control the amount of information when a user gets his lists of followers. You can decide to also save 
FollowerUser in its own table as well. I didn't for no particular reason. 