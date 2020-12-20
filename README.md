

# Patents

Patents is a spring boot service for storing patents zip files.

## Requirements 
* Java 8
* Mongodb
## Installation

1. Download source code from github.
2. Configure mongodb connection using application.properties file under src/main/resources directory.
    - mongo.host = mongo database host.
    - mongo.port = mongo database port.
    - mongo.database = mongo database name.
3. Fill chemicals.csv file under src/main/resources directory with chemical names separated by a comma.   
4. Build application.

```bash
./gradlew build
```
## Usage

1. Run the application from command line:

```python
./gradlew bootRun
```
2. Call to any of the exposed resources:
    - /patents resource: To store new patents.
    method: POST. 
    Content-type: multipart/form-data. 
    Post attribute: data containing patents zip file. 
    - /patents resource: To delete all stored patents.
    method: DELETE.

# Design 

I used spring boot with jersey because it's a simple solution for a rest service and the vast majority of java
engineers knows how it works. 
As application architecture I used a hex architecture because it is the best suited for DDD (the design technique I work with).
For the patents POST endpoint I used an async endpoint for optimizing server resources (expect high load on this resource). 

## License
[the unlicense](https://unlicense.org)