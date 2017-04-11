PaleoCAR Browser
================

[![Build Status](https://travis-ci.org/openskope/paleocar-browser.svg?branch=master)](https://travis-ci.org/openskope/paleocar-browser)
[![Build status](https://ci.appveyor.com/api/projects/status/xpuyyvc3s44sc24d?svg=true)](https://ci.appveyor.com/project/TimothyMcPhillips/paleocar-browser)
[![codecov](https://codecov.io/gh/openskope/paleocar-browser/branch/master/graph/badge.svg)](https://codecov.io/gh/openskope/paleocar-browser)

This repo represents an exploration of alternative technologies for implementing the SKOPE system.  It is a fork of [digital-antiquity/skope](https://github.com/digital-antiquity/skope) 
which stores code for the original SKOPE I prototype.  The current implementation of the **PaleoCAR Browser** is designed to (1) make it easy for the browser-based frontend to employ different implementations of the backend services; and (2) enable anyone to use the **PaleoCAR Browser** with different data sets without installing this data on the production server.

Default backend services are included in the **PaleoCAR Browser** application so that the entire prototpe can be run by deploying a single WAR file to a Tomcat instance, or by executing the same WAR file as an executable JAR. Because the application can itself serve static map tiles a web server is not required (although one may be used for this purpose). GDAL binaries must be installed on the same system if the built-in raster-data-service is to be employed.

The remainder of this README describes how to run the **PaleoCAR Browser** and how to configure it to use different backend services or datasets. A demonstration of the **PaleoCAR Browser** can be found at [http://xkope.org/paleocar-browser](http://www.xkope.org/paleocar-browser).

Running the PaleoCAR Browser service
------------------------------------
The **PaleoCAR Browser** service can be run from any computer with a Java Runtime Environment (JRE) version 1.8 or higher and an installation of GDAL. Because the application is implemented using [Spring Boot](http://projects.spring.io/spring-boot/) and packaged with an embedded Tomcat application server, it is not necessary to install and configure Tomcat or a web server.

### Install a Java Runtime Environment (JRE)

To determine the version of java installed on your computer use the -version option to the java command. For example,

    $ java -version
    java version "1.8.0_101"
    Java(TM) SE Runtime Environment (build 1.8.0_101-b13)
    Java HotSpot(TM) 64-Bit Server VM (build 25.101-b13, mixed mode)
    $

A version 1.8.x JRE may be downloaded from Oracle's [Java SE Downloads](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) page or installed via a package manager. Install a JDK rather than a JRE if you plan to build or repackage the application.

### Install GDAL 

The `Raster Data Service` included in the default packaging depends on a local installation of [GDAL](http://www.gdal.org/) (Geospation Data Abstraction Library).  Currently only the program [gdallocationinfo](http://www.gdal.org/gdallocationinfo.html) is needed.  Note that the Python bindings and scripts are *not* required.  If the `gdallocationinfo` program is in your PATH at the command prompt then no further GDAL components need be installed.  Otherwise follow the instructions below for your platform:

#### Installing GDAL binaries on Linux

On Ubuntu systems the GDAL binaries can be installed using `apt-get`:

    apt-get install gdal-bin

For Ubuntu 16.04LTS the version of GDAL installed by `apt-get` will be 1.11.3. This version is sufficient for running the **PaleoCAR browser**.

For other Linux distributions check for installer packages or [build from source](http://trac.osgeo.org/gdal/wiki/BuildHints).

#### Installing GDAL binaries on macOS

If you use [homebrew](https://brew.sh/) to manage packages on your macOS system you can simply type the following at the terminal prompt:

    $ brew install gdal

This will install GDAL 1.11.5 which is sufficient to run the **PaleoCAR browser**.

If you do not use homebrew, or if you want to use a more recent version of GDAL than homebrew provides, download and install either the [GDAL Complete](http://www.kyngchaos.com/software:frameworks#gdal_complete) (version 2.1) package *or* the individual [GDAL](http://www.kyngchaos.com/software:frameworks#gdal), [PROJ](http://www.kyngchaos.com/software:frameworks#proj), and [UnixImageIO](http://www.kyngchaos.com/software:frameworks#uniximageio) packages.  Add the GDAL binaries directory to your `PATH` so that the `gdallocationinfo` command works at the terminal prompt.  One way to do this is to add the following line to the `.bashrc` file in your home directory:

    export PATH=$PATH:/Library/Frameworks/GDAL.framework/Programs/


#### Installing GDAL on Windows

Install one of the `win32` or `x64` packages (for 32-bit and 64-bit Windows, respectively) provided by [GISInternals](http://www.gisinternals.com/release.php). Add the GDAL binaries directory (e.g. `C:\Program Files\GDAL`) to your `PATH` so that the `gdallocationinfo` command works at the terminal prompt.


### Download the WAR file for the latest release

A pre-built WAR file is included in each [release](https://github.com/openskope/paleocar-browser/releases) of the **PaleoCAR browser** package.  Download the gzipped WAR file to your system.  The release WAR file corresponding to this version of the README is named `paleocar-browser-0.1.4.war`.

Use `gunzip` to unzip the WAR file at the command prompt:

    $ gunzip paleocar-browser-0.1.4.war.gz

You will now have a file named `paleocar-browser-0.1.4.war`.

### Run the WAR file

The **PaleoCAR Browser** service now can be run using the `java -jar` command. For example:

    $ java -jar paleocar-browser-0.1.4.war

The above command will start the **PaleoCAR Browser** service on port 8000.  Open a web browser to [http://localhost:8000](http://localhost:8000) to launch the web-based user interface on the same computer as the service.

Note that the WAR file itself does not contain the data served by the **PaleoCAR Browser** application. Without any data to serve, the colored tile overlays will not appear on the map in the browser interface and no timeseries data will be displayed when a point on the map is clicked. See below for instructions on downloading the required data files and for setting command line options specifying their location.

#### Configuring the port used by the PaleoCAR Browser

To run the service on a different port, specify it using the `server-port` option. For example,

    $ sudo java -jar paleocar-browser-0.1.4.war --server.port=80

starts the **PaleoCAR Browser** service on port 80. For Linux or macOS, if you want to run it on port 80, you can either proxy it through [Apache](https://www.digitalocean.com/community/tutorials/how-to-use-apache-http-server-as-reverse-proxy-using-mod_proxy-extension) or [Nginx](https://www.digitalocean.com/community/tutorials/understanding-nginx-http-proxying-load-balancing-buffering-and-caching), or use [authbind](http://manpages.ubuntu.com/manpages/wily/man1/authbind.1.html) on linux to allow a non-root user to serve content on port 80.

See below for how to specify the server port in a properties file.

### Download the data files served by PaleoCAR browser

The minimal data for running the **PaleoCAR Browser** may be downloaded from [xkope.org:8000](http://xkope.org:8000).

The retrodicted environmental condition coverages are stored in four GeoTIFF files, each ~9GB in size:
    
* [GDD_may_sept_demosaic.tif](http://xkope.org:8000/GDD_may_sept_demosaic.tif)
* [PPT_annual_demosaic.tif](http://xkope.org:8000/PPT_annual_demosaic.tif)
* [PPT_may_sept_demosaic.tif](http://xkope.org:8000/PPT_may_sept_demosaic.tif)
* [PPT_water_year.tif](http://xkope.org:8000/PPT_water_year.tif)

Download the GeoTIFF files and store them in a single directory (named `data` for example) on your computer.   These converages span all 2000 years of the PaleoCAR reconstructions. 

Precomputed map display tiles for year 1 CE are stored in gzipped tar files:

* [GDD_may_sept_demosaic_tiles-1.tar.gz](http://xkope.org:8000/GDD_may_sept_demosaic_tiles-1.tar.gz)
* [PPT_water_year_tiles-1.tar.gz](http://xkope.org:8000/PPT_water_year_tiles-1.tar.gz)

Download and expand (using `gunzip`) these archive. If you have insufficient disk space you may store the data files on a USB flash drive and attach this drive when running the **PaleoCAR Browser** application.  The application runs well in this mode.

### Specify the location of data when starting the PaleoCAR Browser

When serving data downloaded as described above and stored on your computer, specify the location of the data files and tiles using the options below:

Option                        | Description                                                    | Default Value
------------------------------|----------------------------------------------------------------|--------------
raster-data-service.data-dir  | Local directory containing the four GeoTIFF data files         | `.`
static-tile-service.tiles-dir | Local directory where the map tile archives have been expanded | `.`

Note that by default the data files and the roots of the tile directory hierarchies are assumed to be located in the current working directory.

For example, to run the **PaleoCAR Browser** application on port 8000 on a macOS system where the data files are stored in the `data` directory on a USB flash drive mounted at /Volumes/SKOPE: 

    $ java -jar paleocar-browser-0.1.4.war                             \
                --raster-data-service.data-dir=/Volumes/SKOPE/data/    \ 
                --static-tile-service.tiles-dir=/Volumes/SKOPE/data/

Similarly, to run **PaleoCAR Browser** on a Windows computer where the data files are stored on a USB flash drive mounted on drive letter `E`:

    $ java -jar paleocar-browser-0.1.4.war                       \
                --raster-data-service.data-dir='E:/SKOPE/data'   \
                --static-tile-service.tiles-dir='E:/SKOPE/data'  \

Due to the default values for the two command line options, the above is equivalent to changing directory to `E:/SKOPE/data` and executing the WAR file without any options at all.


### Use a remote tile server instead of locally stored map tiles

The archives of map tiles available for download cover the year 1 CE only.  Map tiles for the full 2000 years of the PaleoCAR reconstruction currently are publicly accessible at http://demo.envirecon.org/browse/img.  To instruct the **PaleoCAR Browser** to display map tiles available from this map tile server use the `--static-tile-service.endpoint` option. For example,

    $ java -jar paleocar-browser-0.1.4.war                                          \             
                --static-tile-service.endpoint=http://demo.envirecon.org/browse/img

All 2000 years of map tiles will then be accessible through the browser interface without downloading them to your computer.

### Store customized configuration in a properties file

Note that any of the command-line options described above also can be given in a `application.properties` file in the directory where the application is started.  For example, if the current directory contains a file named `application.properties` with these contents:

    server.port                     = 8080
    raster-data-service.data-dir    = data
    static-tile-service.endpoint    = http://demo.envirecon.org/browse/img

then an instance of PaleoCAR browser started from this directory will accept connections on port 8080, will serve time series data from GeoTIFF files found in the `data` subdirectory, and will pull map tiles from `http://demo.envirecon.org/browse/img`. Options given on the command line will override property values given in this file.


Deploying the PaleoCAR Browser service to Tomcat
------------------------------------------------

The WAR file associated with a release of the  **PaleoCAR Browser** alternatively may be deployed to a Tomcat instance.  Simply rename the WAR file to reflect the name you wish to give the web application and copy the renamed WAR file to the webapps directory for your Tomcat instance.

Once Tomcat has expanded the WAR file, edit the `application.properties` file in the `WEB-INF/classes` directory of the web application directory tree to set the properties as needed for your environment.