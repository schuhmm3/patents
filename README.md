

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
I get chemicals data from a properties file in order to maintain the code challenge as simple as I can but in a 
real environment this data could be taken from a remote service, a database, etc.

## License
[the unlicense](https://unlicense.org)

## Results

### Build

Works (after depedency fix) with gradle 

### Run pipeline

Calling

```
schuhmm3@azure:~/challenges/backend/pg/patents$ curl -v -X POST -H 'Content-Type: multipart/form-data' -F 'data=@../../uspat1_201831_back_80001_100000.zip' localhost:8080/patents
*   Trying 127.0.0.1...
* TCP_NODELAY set
* Connected to localhost (127.0.0.1) port 8080 (#0)
> POST /patents HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.58.0
> Accept: */*
> Content-Length: 90724823
> Content-Type: multipart/form-data; boundary=------------------------231a6746f2ce1080
> Expect: 100-continue
>
< HTTP/1.1 100
< HTTP/1.1 200
< Content-Length: 0
< Date: Wed, 23 Dec 2020 06:09:59 GMT
<
* Connection #0 to host localhost left intact
```

Logfile 

Database

```
> db.patent.find().count();
7173
```

```
> db.patent.findOne();
{
        "_id" : "3b5d8b46-288a-47e2-9f89-3b17f9f72cf4",
        "title" : "Electrical apparatus",
        "application" : "Questel",
        "year" : "2018",
        "abs" : "<p id=\"P-EN-00001\" num=\"00001\">      <br/>      An electrical apparatus comprises an electrical device including at least a first terminal.      <br/>      A carrier for electrically connecting the electrical device to a printed circuit board having at least a conductive contact includes a bridging terminal disposed between the first terminal and the conductive contact.      <br/>      The bridging terminal is arranged to be selectively displaced between a first position at which the first terminal and the conductive contact are not connected, and a second position at which the first terminal and the conductive contact are electrically connected by the bridging terminal.    </p>",
        "chemicals" : [ ],
        "_class" : "com.basf.codechallenge.domain.patents.Patent"
}
```

```
> db.patent.find().limit(5);
{ "_id" : "3b5d8b46-288a-47e2-9f89-3b17f9f72cf4", "title" : "Electrical apparatus", "application" : "Questel", "year" : "2018", "abs" : "<p id=\"P-EN-00001\" num=\"00001\">      <br/>      An electrical apparatus comprises an electrical device including at least a first terminal.      <br/>      A carrier for electrically connecting the electrical device to a printed circuit board having at least a conductive contact includes a bridging terminal disposed between the first terminal and the conductive contact.      <br/>      The bridging terminal is arranged to be selectively displaced between a first position at which the first terminal and the conductive contact are not connected, and a second position at which the first terminal and the conductive contact are electrically connected by the bridging terminal.    </p>", "chemicals" : [ ], "_class" : "com.basf.codechallenge.domain.patents.Patent" }
{ "_id" : "8af7ec7e-99b3-4511-9d3b-49e1bb9abe50", "title" : "Heat sink having standoff buttons and a method of manufacturing therefor", "application" : "Questel", "year" : "2018", "abs" : "<p id=\"P-EN-00001\" num=\"00001\">      <br/>      The present invention provides a method of manufacturing a heat sink for use with a circuit board having a predetermined thickness and an opening formed therein.      <br/>      In one particularly embodiment the method comprises forming a heat sink body, forming a support shoulder in the heat sink body by protruding a portion of the heat sink body to a predetermined first height, and forming a button shoulder in the heat sink body by protruding a portion of the support shoulder to a predetermined second height.      <br/>      Preferably, the heat sink body comprises aluminum or aluminum alloy.      <br/>      However, it should, of course, be recognized that other extrudable or malleable materials may be used in place of aluminum or its alloys.      <br/>      Additionally, it should be understood that a plurality of such support shoulders and button shoulders may be formed to provide a plurality of coupling points with the circuit board, which could also have a corresponding number of openings.    </p>", "chemicals" : [ ], "_class" : "com.basf.codechallenge.domain.patents.Patent" }
{ "_id" : "0244e5a9-449c-4abd-898f-a9574da516ea", "title" : "Device and method for mounting electronic components on printed circuit boards", "application" : "Questel", "year" : "2018", "abs" : "<p id=\"P-EN-00001\" num=\"00001\">      <br/>      The present invention relates to mounting an electric connector (20) onto a printed circuit board, particularly to a printed circuit board comprised of ceramic material, for instance either an LTCC substrate or an HTCC substrate.      <br/>      The problem addressed is one where the connector (20) tends to loosen from the substrate when the temperature varies.      <br/>      This is due to the difference in the coefficients of thermal expansion of the printed circuit board and the connector (20).      <br/>      The problem is solved with the aid of a so-called shim (10) that has a coefficient of thermal expansion between that of the printed circuit board and that of the connector.      <br/>      One side of the shim (10) is soldered onto the connector and the other side of the shim is soldered onto the printed circuit board.      <br/>      The connector (20) is therewith fastened to the printed circuit board.      <br/>      Shear stresses acting between the connector (20) and the printed circuit board are distributed through said board through the medium of two joints instead of one.      <br/>      The connector may alternatively be provided with a built-in shim.    </p>", "chemicals" : [ ], "_class" : "com.basf.codechallenge.domain.patents.Patent" }
{ "_id" : "a1856502-ba2a-4c0d-8d3d-0f6421da1e70", "title" : "Meter device for vehicle", "application" : "Questel", "year" : "2018", "abs" : "<p id=\"P-EN-00001\" num=\"00001\">      <br/>      A vehicle instrumentation system comprising a meter block having a display portion, a control portion, and a power supply portion.      <br/>      The display portion provides a display according to signals from the electronic component units.      <br/>      The control portion controls the electronic component units.      <br/>      The electric power supply portion supplies electric power to the electronic component units.      <br/>      These electronic component units are obtained by classifying and integrating electronic components other than the meter block.      <br/>      A mounting reference portion permits the meter block and the electronic component units to be attached and detached.    </p>", "chemicals" : [ ], "_class" : "com.basf.codechallenge.domain.patents.Patent" }
{ "_id" : "17e46c35-fabf-49c4-a54d-d4b3201ec76c", "title" : "Electrical device module", "application" : "Questel", "year" : "2018", "abs" : "<p id=\"P-EN-00001\" num=\"00001\">      <br/>      A module of an electrical device has a printed circuit board which may be pushed into the device and plugged onto a backplane of the device.      <br/>      A cuboid cage is connected to the printed circuit board of the device.      <br/>      The cuboid cage has a backplane arranged perpendicular to the printed circuit board of the device and is connected to the circuit board by means of connectors.      <br/>      The backplane of the cage has its own connectors for electrical connection to separate modular cage push-in units, in the form of cards or boards, which may be inserted into the cuboid cage of the device.    </p>", "chemicals" : [ ], "_class" : "com.basf.codechallenge.domain.patents.Patent" }
```
