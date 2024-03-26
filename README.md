# Getting Started

## Project Dependencies
* Spring Web
* H2 Database
* Spring Data JPA
* Docker Compose Support
* Lombok
* Java 17 (not needed when ran on docker)
* Gradle (not needed when ran on docker, or you can run with gradle wrapper installed in the project - see gradlew)


## Running on docker
* Make sure your local docker is up and running
* Command to run the service:
  * docker-compose up -f compose.yaml -d

## Stopping the docker container and cleaning the resources
* Make sure your local docker is up and running
* Command to stop the service:
  * docker-compose down -f compose.yaml

## Running on command line
* Build the project: (make sure to change directory of the project)
  * ./gradlew clean
  * ./gradlew build
* Run the tests locally
  * ./gradlew test
* See the test report : build/reports/tests/test/index.html

## Calling endpoints via Postman
* Use the postman collection under /docs

## Assumptions and ideas
* I've started by creating the database schema  
![Database_schema](/docs/simplesystemtodo.drawio.png)
* Then created the general todo items as list
* The "production ready" has come during the process, so kept it updated during the development process

## To see the sql logs on command line
* Set active profile "dev" on intellij
* Set active profile "dev" on command line :
  * ./gradlew bootRun --args='--spring.profiles.active=dev'

## Todo List
- [X] Create Database Diagram
- [X] Start creating controller endpoint : add item
- [X] Start creating controller endpoint: change description of item
- [X] Start creating controller endpoint: mark item done
- [X] Start creating controller endpoint: mark item "not done"
- [x] Start creating controller endpoint: get all items that are "not done" / with an option to retrieve all (add filtering)
- [X] Start creating controller endpoint: get details of a specific item
- [x] Update a todo item when it is past due.

## Production readiness
* add authorization
* add prioritization
* add labels
* add collaboration
* add pagination
* add elasticsearch or solr for searching
* add redis or memcached for cache
* add thymeleaf for email sending
* implement email sending when a todo item is finished , or for daily reporting
* for daily reporting implement, also, a quartz job which can be azure function too
* deploy the application to ACR
* create CI/CD with terraform and github actions

----------
## Project requirements and requests:
Design and implement a backend service allowing basic management of a simple to-do list.

### Functional requirements
#### Each to-do item has the following attributes:
* description,
* status: "not done", "done", "past due",
* date-time of creation,
* due date-time,
* date-time at which the item was marked as done.

#### The service should provide a RESTful API that allows to:
* add an item,
* change description of an item,
* mark an item as "done",
* mark an item as "not done",
* get all items that are "not done" (with an option to retrieve all items),
* get details of a specific item.


#### The service should automatically change status of items that are past their due date as "past due".
* The service should forbid changing "past due" items via its REST API.

### Non-functional requirements
##### the service should:
* be dockerized
* use H2 in-memory database
* contain automatic tests (we don't expect a very high coverage but would like to see your approach to writing automatic tests)
##### the service should not:
* implement authentication
##### readme should contain brief notes covering:
* service description and made assumptions
* tech stack used (runtime environment, frameworks, key libraries)
##### how to:
* build the service
* run automatic tests
* run the service locally
##### Evaluation criteria
* alignment with the requirements
* usage of best practices when dealing with edge cases that are not covered here
* code quality and readability
* presence and quality of (or lack of) automatic tests
* commit history (thought process, commit messages)

Your work should be handed off in a form of a link to a git repository we can clone into
