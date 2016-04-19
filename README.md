## lcfserver

This is a java reference server implementation of the [BIC Library Communication Framework](http://www.bic.org.uk/114/lcf/).

The Library Communications Framework (LCF) is a set of library interoperability standards which defines a framework for the communication of data between self-service and other library terminal applications to and from library management systems.

### To run local version

**Pre-requisites:**
* [Git](https://git-scm.com/)
* [JDK (>= 1.8)](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven (>= 3.3)](https://maven.apache.org/)

**To install and run servlet implementation:**
```bash
git clone https://github.com/ceridwen-com/lcfserver.git
cd lcfserver
mvn jetty:run -Dlcf.server.frontend=servlet
```

**To install and run org.restlet implementation:** **
```bash
git clone https://github.com/ceridwen-com/lcfserver.git
cd lcfserver
mvn jetty:run -Dlcf.server.frontend=restlet
```

**To install and run org.restlet implementation with optional modules:**
```bash
git clone https://github.com/ceridwen-com/lcfserver.git
cd lcfserver
mvn jetty:run -Dlcf.server.frontend=restlet -Dlcf.server.frontend.module.raml -Dlcf.server.frontend.module.swagger
```

-------

See [lcfserver-assembly/src/main/resources/README.md](lcfserver-assembly/src/main/resources/README.md) for further details