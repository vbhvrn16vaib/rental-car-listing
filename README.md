# rental-car-listing
This is an application built with java 15 and spring boot. We can do following operation insert / update for any listing.

# Dependencies:
- maven
- java 15
- docker

# Run
* Build the jar file using mvn command 
    ```text
    mvn clean package
    ```
    This should generate the jar file under target folder

*  run the application using docker-compose
   
    console mode
    ```text
     docker-compose up --build
    ```
   detach mode
    ```text
     docker-compose up --build -d
    ```    
    Note: make sure docker is running locally.
   

* Now access the application using port `8080`
    ```text
      http://localhost:8080/search
    ```
  
# Endpoints details:

* GET `/search`  -> all the available listing from all the providers
* GET `/search?make={query}&color={query}` -> should be able to filter by make and color by using query params
* POST `/upload/csv/{dealer_id}` -> should be able to upload csv with dealers listing
* POST `/vendors_listing/{dealer_id}`-> should be able to post listing (json api)

# From Dockerhub

If too lazy to download everything and run..  then we can also use dockerhub. Just run the latest image by using below command
```
docker run -p 8080:8080 shankeytcs/vehicle-listing:latest
```

