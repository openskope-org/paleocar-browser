PaleoCAR Browser
================

This repo represents an exploration of alternative technologies for
implementing the SKOPE system.  It is a fork of 
[digital-antiquity/skope](https://github.com/digital-antiquity/skope) 
which stores code for the SKOPE I prototype.  The new implementation here--the 
**PaleoCAR browser**--is designed to (1) make it easy for
the browser-based frontend to employ different implementations of the backend
services; and (2) enable anyone to use the **PaleoCAR browser** with 
different data sets without installing this data on the production server.

The default backend services included in the **PaleoCAR browser** application are 
implemented using Spring Boot so that the entire prototpe can be run by executing
a single JAR file. Only a Java runtime is needed to execute the jar; however GDAL 
also must be installed if the built-in raster-data service is employed.

The remainder of this README describes how to run the PaleoCAR 
browser and how to configure it to use different backend services or datasets.








