# :cook: Frello Manager REST API
Management system to control end to end the work process in a fast food restaurant, Frello Rico y Casero, in the city of La Plata, Argentina.
<br>
This repository contains the back-end part of the project, which consists of a REST API developed in java with Spring.<br><br>
You can look at the <a href="https://api.frellomanager.online/swagger-ui/index.html#/">endpoints documentation</a> 
and also you can view a <a href="https://www.youtube.com/watch?v=GSdKzLrq3Ds/">demo of the proyect</a>.
<br><br>
## :wrench: Project Setup and Tools
* <a href="https://www.oracle.com/java/technologies/javase/jdk19-archive-downloads.html">JDK 19</a> 
* <a href="https://www.postgresql.org/">PostgreSql</a> 
* <a href="https://www.postman.com/">Postman</a>
* <a href="https://https://docs.docker.com/get-docker/">Docker</a>
* <a href="https://docs.docker.com/compose/">Docke-compose</a>
<br><br>
## :green_circle: Run Locally using Docker
1 - Install [Docker](https://https://docs.docker.com/get-docker/) and [Docker Compose](https://docs.docker.com/compose/)<br>
2 - Clone this repo and move into the downloaded folder<br>
3 - Run the command  **`docker-compose -f deploy/docker-compose.yml up`**<br>
4. The `docker-compose.yml` file located in the `deploy` folder will handle starting two services:
   - **db:** a PostgreSQL container, configured to expose port 5432 on your host.
   - **app:** the Spring Boot application, which is built automatically using the `Dockerfile` in the `deploy` folder. The build process uses a Maven image to compile the source code and package the project, then runs the resulting JAR in a lightweight Java container using a JDK image.
5. Once started, the application will be available at [http://localhost:8080](http://localhost:8080).

## :building_construction: How it Works
It is a REST API that exposes a series of endpoints from which, through HTTPS requests, information concerning Frello's work process can be accessed and manipulated. The architecture of the solution is designed for the entry of a new order, its preparation in the kitchen, its dispatch by delivery or take away and its subsequent balance saved in the sales ledger. <br>
### Security
Each request is protected and encrypted by the HTTPS protocol, while to access the resources it must also be validated by a JWT, present in the header (except for the registration and authorization), the creation and validation of which is carried out by Spring Security together with the JWT libraries.
<br>
### Storage
The application is connected to a service with a PostgreSql database, which is managed by JPA and Hibernate.
<br>
### API
Through Spring Web, a series of endpoints are exposed with which to use the application, accessible according to the user's authorization level.
<br>
### Documentation
The application is documented by means of Swagger and OpenApi, where the resources, their requirements and possible responses are described, as well as the data scheme to make use of them.
<br>
### Testing
With the incorporation of Junit, all endpoints and service functionalities are tested in unit tests.
<br>
<br><br>
