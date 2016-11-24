PaleoCAR Browser
================

This repo represents an exploration of alternative technologies for implementing the SKOPE system.  It is a fork of [digital-antiquity/skope](https://github.com/digital-antiquity/skope) 
which stores code for the SKOPE I prototype.  The current implementation of the **PaleoCAR Browser** is designed to (1) make it easy for the browser-based frontend to employ different implementations of the backend services; and (2) enable anyone to use the **PaleoCAR Browser** with different data sets without installing this data on the production server.

Default backend services are included in the **PaleoCAR Browser** application so that the entire prototpe can be run by executing a single JAR file. Because the application can itself serve static map tiles a web server is not required (although one may be used for this purpose). GDAL binaries must be installed on the same system if the built-in raster-data service is to be employed.

The remainder of this README describes how to run the **PaleoCAR Browser** and how to configure it to use different backend services or datasets. A demonstration of the **PaloeCAR Browser** can be found at [xkope.org:8000](http://www.xkope.org:8000/).

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

A pre-built jar file is included in each [release](https://github.com/openskope/paleocar-browser/releases) of the **PaleoCAR browser** package.  Download the jar file to your system.  The release jar file corresponding to this version of the README is named `paleocar-browser-0.1.1.jar`.

### Run the jar file

The **PaleoCAR Browser** service now can be run using the `java -jar` command. For example:

    $ java -jar paleocar-browser-0.1.1.jar

The above command will start the **PaleoCAR Browser** service on port 8000.  Open a web browser to http://localhost:8000 to launch the web-based user interface on the same computer as the service.

Note that the jar file itself does not contain the data served by the **PaleoCAR Browser** application. Without any data to serve, the colored tile overlays will not appear on the map in the browser interface and no timeseries data will be displayed when a point on the map is clicked. See below for instructions on downloading the required data files and for setting command line options specifying their location.

#### Configuring the port used by the PaleoCAR Browser

To run the service on a different port, specify it using the `server-port` option. For example,

    $ sudo java -jar paleocar-browser-0.1.1.jar --server.port=80

starts the **PaleoCAR Browser** service on port 80. The `sudo` command is required in macOS and Linux environments when using port 80.

### Download the data files served by PaleoCAR browser

The minimal data for running the **PaleoCAR Browser** may be downloaded from [xkope.org/data](http://www.xkope.org/data/).

The retrodicted environmental condition coverages are stored in four GeoTIFF files, each ~9GB in size:
    
* [GDD_may_sept_demosaic.tif](http://45.79.81.187/data/GDD_may_sept_demosaic.tif)
* [PPT_annual_demosaic.tif](http://45.79.81.187/data/PPT_annual_demosaic.tif)
* [PPT_may_sept_demosaic.tif](http://45.79.81.187/data/PPT_may_sept_demosaic.tif)
* [PPT_water_year.tif](http://45.79.81.187/data/PPT_water_year.tif)

Download the GeoTIFF  files and store them in a single directory (named `data` for example) on your computer.   These converages span all 2000 years of the PaleoCAR reconstructions. 

Precomputed map display tiles for year 1 CE are stored in gzipped tar files:

* [GDD_may_sept_demosaic_tiles-1.tar.gz](http://45.79.81.187/data/GDD_may_sept_demosaic_tiles-1.tar.gz)
* [PPT_water_year_tiles-1.tar.gz](http://45.79.81.187/data/PPT_water_year_tiles-1.tar.gz)

Download and expand these archives (for example into a directory named `tiles`).

If you have insufficient disk space you may store the data files on a USB Flash drive and attach this drive when running the **PaleoCAR Browser** application.  The application runs well in this mode.

### Specify the location of data when starting the PaleoCAR Browser

When serving data downloaded as described above and stored on your computer, specify the location of the data files and tiles using the options below:

Option                       | Description                                                    | Default Value
-----------------------------|----------------------------------------------------------------|--------------
rasterdata-service.data-dir  | Local directory containing the four GeoTIFF data files         | `.`
rastertile-service.tiles-dir | Local directory where the map tile archives have been expanded | `.`

Note that by default the data files and the roots of the tile directory hierarchies are assumed to be located in the current working directory.

For example, to run the **PaleoCAR Browser** application port 8000 on a Windows computer where the data files are stored on a USB flash drive mounted on drive letter `E`:

    $ java -jar paleocar-browser-0.1.1.jar                  \
                --rasterdata-service.data-dir='E:/SKOPE/'   \
                --rastertile-service.tiles-dir='E:/SKOPE/'  \

Due to the default values for the two command line options, the above is equivalent to changing directory to `E:/SKOPE` and executing the jar file without any options at all.

### Use a remote tile server instead of locally stored map tiles

The archives of map tiles available for download cover the year 1 CE only.  Map tiles for the full 2000 years of the PaleoCAR reconstruction currently are publicly accessible at http://demo.envirecon.org/browse/img.  To instruct the **PaleoCAR Browser** to display map tiles available from this map tile server use the `--rastertile-service.base` option. For example,

    $ java -jar paleocar-browser-0.1.1.jar                                      \             
                --rastertile-service.base=http://demo.envirecon.org/browse/img

All 2000 years of map tiles will then be accessible through the browser interface.
