## Docker
to build:
`docker build -t pokemon-catch-backend .`   
to start: `docker run -d -p8080:8080 -e "SPRING_PROFILES_ACTIVE=docker" --name pokemon-catch-backend pokemon-catch-backend`   
To see the logs: `docker logs pokemon-catch-backend -f`