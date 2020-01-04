---
typora-root-url: docs
---

# myretail-tacs
A PoC RESTful service that combines item information with pricing to return products for sale. For our definition a product is a single item combined with zero or more prices with only one price value per currency.

![https://github.com/doug-hughes/myretail-tacs/blob/master/docs/myRetail%20Logical.svg](/myRetail Logical.svg)

##### Scoping Assumptions

1. Only one product will be returned by the redsky service when providing a tcin

2. The tcin stands for a Target.com item number which is an eight digit value

3. A user does not need to be authenticated before retrieving these items and prices

4. We do not need to return localized values for this exercise


##### Building from Source

[Gradle](https://gradle.org/) wrapper is used for the build system

myRetail was developed with [STS](https://spring.io/tools/sts) with the [Eclipse Buildship plugin](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=2ahUKEwjKz4G12MnmAhWSFTQIHfoABc8QFjAAegQIChAC&url=https%3A%2F%2Fprojects.eclipse.org%2Fprojects%2Ftools.buildship&usg=AOvVaw3w79u_pTg-smfQ0zfK5PIN) and can be imported as a gradle project. The project will automatically build as part of the import

run .`/gradlew assemble` to build outside of your IDE, classes will be generated in the `build` directory

##### Running from Source

To run the project outside of STS:

​	run `./gradlew bootRun` from the project directory

##### Running Tests

`./gradlew build` to build and run unit tests with results saved to `build/reports/test/`

`./gradlew integrationTest` to run integration tests with results save to `build/reports/integrationTest`

##### Building/Running an Executable Jar

`./gradlew buildJar` will create an executable jar at `build/libs/myretail-tacs-0.0.1-SNAPSHOT.jar`

The application will need a running [MongoDb](https://www.google.com/aclk?sa=l&ai=DChcSEwii1KyT1OPmAhUUq-wKHW8DA7IYABAAGgJwag&sig=AOD64_0LyvvA3ePBXBi4ZQ0SG1o2LFZcrw&rct=j&q=&ved=2ahUKEwiqtqWT1OPmAhW-GDQIHSxNAJgQ0Qx6BAgKEAE&adurl=) instance running on the localhost at port 27017. To run the development MongoDb instance run `./gradlew startMongoDb`

To run the jar call java with the -jar option and the path to the jar. We run java from the project directory so we do not lock the build/libs directory and also so that the /config directory will be used for the application.properties and log4j.xml configuration. We add the jmxremote option so that we can monitor with jconsole or VisualVM

`PS C:\dev\repos\myretail-tacs> &java "-Dcom.sun.management.jmxremote=true" -jar ./build/libs/myretail-tacs-0.0.1-SNAPSHOT.jar`

> "-Dlogging.config=file:./log4j2.xml" can be added to specify a log4j configuration file

This will launch a restful service at http://localhost:8080/ that will respond to HTTP GET requests at /products/{id} and return a JSON Object representing the product for the {id} as documented at src/docs/[products-GET.md](https://github.com/doug-hughes/myretail-tacs/blob/master/src/doc/products-GET.md) 

[Postman](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=2ahUKEwjl3cTxz-PmAhX1NX0KHe6XC0gQFjAAegQIFRAC&url=https%3A%2F%2Fwww.getpostman.com%2F&usg=AOvVaw1vWzpwzQOHi5ErKZnywLDR) examples for interacting with the service are available at src/test/resources

When you are done running the jar, you may stop MongoDb by running `./gradlew stopMongoDb` from the project directory.

##### Sample Interactions

We'll try with a product Id that represents *The Big Lebowski (Blu-ray)*

```
curl http://localhost:8080/products/13860428
```

which will return

```
{"id":13860428,"name":"The Big Lebowski (Blu-ray)"}
```

If a current_price has not been added for the product it will not contain any pricing



Running in development we can populate some sample data by running

```
curl http://localhost:8080/dev/prices/populate?query=kittens -X POST
```

which will give us a list of identifiers that now have prices

```
[53899693,76455241,16734840,14486932,79194128,53122190,76349158,53183666,76562633]
```

and now we can get current_price for these identifiers

```
 curl http://localhost:8080/products/53899693
```

```
{"id":53899693,"name":"The Shy Little Kitten Book and Vinyl Record - by  Cathleen Schurr (Mixed media product)","current_price":{"value":8.99,"currency_code":"USD"}}
```

##### Technical Stack

SpringBoot

- ubiquitous with RESTful services
- active development

MongoDb

- Good support in Spring Data
- NoSQL store
- wide adoption

##### Decisions

1. What validation can be performed on current_price insertion? min/max value boundaries, tcin exists externally, validate currency code - limit to 3 chars?