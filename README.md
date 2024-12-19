## Ontology Microservice

This service allows you to deploy any ontology as a rest api with graph traversal. To support a particular
ontology you must update `ontology.sh` and if you are leveraging github actions `.github/workflows/ontology-cron.yml`

## Requirements
Java 17

## Configuration
```sh
    ONTOLOGY_SERVICE_ONTOLOGY=<ontology-to-use>
    ONTOLOGY_SERVICE_LOAD=true
    ONTOLOGY_SERVICE_INTERNATIONAL=<true if babelon translations enabled>
```


### Running Locally

```sh
./gradlew run
```


## Building Docker Container
1. Get the data 
    ```bash ontology.sh hp```
2. Configure envrionment
    ```
    export ONTOLOGY_SERVICE_ONTOLOGY=hp
    export ONTOLOGY_SERVICE_LOAD=true
    export ONTOLOGY_SERVICE_INTERNATIONAL=true
    ```
3. Build container
    ```./gradlew dockerBuild```
4. Run container
   ```docker run ontology-service:<version> -p 8080:8080```
5. Open http://localhost:8080/api/hp/docs

## Deployment
These services auto deploy based on the hashed value of the ontology file. The hash value is then stored as a tag on the docker container, when the two hashes do not align we auto-deploy the new "updated" ontology. The only time we need to manually deploy is when we add a new ontology.