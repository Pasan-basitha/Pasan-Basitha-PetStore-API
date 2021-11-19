# Middleware Architecture Assignment - 18001637

## Introduction

MicroProfile Starter has generated this MicroProfile application for you.

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .



## 1. How to build and/or deploy the API

##i. Build the project


    ./gradlew build


## ii. Deploy and run the project

    java -jar build/PetStore-runner.jar

#2. How to run test suite
     

    ./gradlew test
#3. How to run a CURL/WGET command to test the APIs once deployed

###Pets
###View all pets

    curl -XGET -H "Content-type: application/json" 'http://localhost:8080/data/pets'

###Add new pet

     curl -XPOST -H "Content-type: application/json" -d '{"age": 5,"name":"Bruzer","type":"Dog"}' 'http://localhost:8080/data/pets/add'

###Update a pet

     curl -XPUT -H "Content-type: application/json" -d '{"age":9}' 'http://localhost:8080/data/pets/edit/3'

###Delete a pet

     curl -XDELETE -H "Content-type: application/json" 'http://localhost:8080/data/pets/delete/1'

###Search for a pet by id

      curl -XGET -H "Content-type: application/json" 'http://localhost:8080/data/pets/search?id=1'


