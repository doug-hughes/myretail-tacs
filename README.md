---
typora-root-url: docs
---

# myretail-tacs
A PoC RESTful service that combines item information with pricing to return products for sale. For our definition a product is a single item combined with zero or more prices with only one price value per currency.

![](/myRetail Logical.svg)



##### Scoping Assumptions

1. Only one product will be returned by the redsky service when providing a tcin

2. The tcin stands for a Target.com item number which is an eight digit value

3. A user does not need to be authenticated before retrieving these items and prices

4. We do not need to return localized values for this exercise

   

##### Building from Source

[Gradle](https://gradle.org/) wrapper is used for the build system

myRetail was developed with [STS](https://spring.io/tools/sts) with the [Eclipse Buildship plugin](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=2ahUKEwjKz4G12MnmAhWSFTQIHfoABc8QFjAAegQIChAC&url=https%3A%2F%2Fprojects.eclipse.org%2Fprojects%2Ftools.buildship&usg=AOvVaw3w79u_pTg-smfQ0zfK5PIN) and can be imported as a gradle project. The project will automatically build as part of the import

To run the project outside of STS:

1.  run the `./gradlew startMongoDb` from the project directory
2.  run the `./gradlew bootRun` from the project directory

##### Decisions

1. What validation can be performed on current_price insertion? min/max value boundaries, tcin exists externally, validate currency code - limit to 3 chars?