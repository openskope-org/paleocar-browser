PaleoCAR Browser
================

This repo represents an exploration of alternative technologies for implementing the SKOPE system.  It is a fork of [digital-antiquity/skope](https://github.com/digital-antiquity/skope) 
which stores code for the SKOPE I prototype.  The current implementation of the **PaleoCAR Browser** is designed to (1) make it easy for the browser-based frontend to employ different implementations of the backend services; and (2) enable anyone to use the **PaleoCAR Browser** with different data sets without installing this data on the production server.

Default backend services are included in the **PaleoCAR Browser** application so that the entire prototpe can be run by executing a single JAR file. Because the application can itself serve static map tiles a web server is not required (although one may be used for this purpose). GDAL binaries must be installed on the same system if the built-in raster-data service is to be employed.

The remainder of this README describes how to run the PaleoCAR browser and how to configure it to use different backend services or datasets. A demonstration of the **PaloeCAR Browser** can be found at [http://45.79.81.187:8000/](http://45.79.81.187:8000/).

Running the PaleoCAR Browser service
------------------------------------
The **PaleoCAR Browser** service can be run from any computer with a Java Runtime Environment (JRE) version 1.8 or higher and an installation of GDAL. Because the application is implemented using [Spring Boot](http://projects.spring.io/spring-boot/) and packaged with an embedded Tomcat application server, it is not necessary to install and configure Tomcat.

### Install a Java Runtime Environment (JRE)

To determine the version of java installed on your computer use the -version option to the java command. For example,

    $ java -version
    java version "1.8.0_101"
    Java(TM) SE Runtime Environment (build 1.8.0_101-b13)
    Java HotSpot(TM) 64-Bit Server VM (build 25.101-b13, mixed mode)
    $

A JRE may be downloaded from Oracle's [Java SE Downloads](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) page or installed via a package manager. Install a JDK rather than a JRE if you plan to build or repackage the application.

### Install GDAL 

The `Raster Data Service` included in the default packaging depends on a local installation of [GDAL](http://www.gdal.org/) (Geospation Data Abstraction Library).  Currently only the program [gdallocationinfo](http://www.gdal.org/gdallocationinfo.html) is needed.  Note that the Python bindings and scripts (which can be tricky to install on Windows) are *not* required.  If the `gdallocationinfo` program is in your PATH at the command line then no further GDAL components need be installed.  Otherwise follow the instructions below for your platform:

#### Installing GDAL binaries on Linux

On Ubuntu systems the GDAL binaries can be installed using `apt-get`:

    apt-get install gdal-bin

For Ubuntu 16.04LTS the version of GDAL installed by `apt-get` will be 1.11.3. This version is sufficient for running the **PaleoCAR browser**.

For other Linux distributions check for installer packages or [build from source](http://trac.osgeo.org/gdal/wiki/BuildHints).

#### Installing GDAL binaries on macOS

Install either the [GDAL Complete](http://www.kyngchaos.com/software:frameworks#gdal_complete) (version 2.1) package *or* the individual [GDAL](http://www.kyngchaos.com/software:frameworks#gdal), [PROJ](http://www.kyngchaos.com/software:frameworks#proj), and [UnixImageIO](http://www.kyngchaos.com/software:frameworks#uniximageio) packages.  Add the GDAL binaries directory to your `PATH` so that the `gdallocationinfo` command works at the terminal prompt.  One way to do this is to add the following line to the `.bashrc` file in your home directory:

    export PATH=$PATH:/Library/Frameworks/GDAL.framework/Programs/

#### Installing GDAL on windows

Install one of the `win32` or `x64` packages (for 32-bit and 64-bit Windows, respectively) provided by [GISInternals](http://www.gisinternals.com/release.php). Add the GDAL binaries directory (e.g. `C:\Program Files\GDAL`) to your `PATH` so that the `gdallocationinfo` command works at the terminal prompt.


### Download the Jar file for the latest release

A pre-built jar file is included in each [release](https://github.com/openskope/paleocar-browser/releases) of the **PaleoCAR browser** package.  Download the jar file to your system.  The jar file corresponding to the first release is named `paleocar-browser-0.1.0.jar`.

### Run the jar file

The **PaleoCAR Browser** service now can be run using the `java -jar` command. For example:

    $ java -jar paleocar-browser-0.1.0.jar

The above command will start the **PaleoCAR Browser** service on port 8000.  Open a web browser to http://localhost:8000 to launch the web-based user interface on the same computer as the service.

Note that the jar file itself does not contain the data served by the **PaleoCAR Browser** application. Without any data to serve, the colored tile overlays will not appear on the map in the browser interface and no timeseries data will be displayed when a point on the map is clicked.  See below for instructions on downloading the required data files and setting command line options specifying their location.

#### Configuring the port used by the PaleoCAR Browser

To run the service on a different port specify it using the `server-port` option. For example,

    $ sudo java -jar paleocar-browser-0.1.0.jar --server.port=80

starts the **PaleoCAR Browser** service on port 80. The `sudo` command is required in macOS and Linux environments when using port 80.

### Download the data files served by PaleoCAR browser

THe minimal data for running the **PaleoCAR Browser** may be downloaded from [http://45.79.81.187/data/](http://45.79.81.187/data/).



