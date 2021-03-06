# First Class Procedure Example

First class procedure weaving together OO, Functional, Imperative, Reactive

See [OO Functional Imperative Reactive weaved together](https://sagenschneider.blogspot.com/2019/04/oo-functional-imperative-reactive.html) for details of this repository.

![Continuous Integration](https://github.com/officefloor/OoFunctionImperativeReactive/workflows/Continuous%20Integration/badge.svg) [![Dependabot Status](https://api.dependabot.com/badges/status?host=github&repo=officefloor/OoFunctionImperativeReactive)](https://dependabot.com)

## Try out the example code

 1. Clone this repository
 
 1. In the cloned directory, run: `mvn clean install`
 
 1. Once built, run the following to start up the application: `mvn net.officefloor.maven:officefloor-maven-plugin:open`
 
    Note: once the Graalvm issue is resolved, can also run the Spring uber jar: [Graalvm: Exception when run from uber-jar](https://github.com/graalvm/graaljs/issues/125)
    
 1. Once running, a simple user page is available at [http://localhost:7878](http://localhost:7878)  (open in a browser)
 
 1. This page provides means to invoke the various end points
 
 1. Once done, the application can be stopped with `mvn net.officefloor.maven:officefloor-maven-plugin:close`

For more information, see [OfficeFloor](http://officefloor.net) - particularly the [tutorials](http://officefloor.net/tutorials/)

Also, see the [blog](http://sagenschneider.blogspot.com) regarding why.
