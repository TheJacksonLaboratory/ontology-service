## Ontology Microservice

This service allows you to deploy any ontology as a rest api with graph traversal. To support a particular
ontology you must update `ontology.sh` and if you are leveraging github actions `.github/workflows/ontology-cron.yml`

### Configuration
```sh
    ONTOLOGY_SERVICE_ONTOLOGY=<ontology-to-use>
    ONTOLOGY_SERVICE_LOAD=true
    ONTOLOGY_SERVICE_INTERNATIONAL=<true if babelon translations enabled>
```


### Running

```sh
./gradlew run
```
