#!/usr/bin/env python

import optparse
import struct
from enum import Enum
from osgeo import gdal
from gdalconst import GA_ReadOnly

class RasterDataType(Enum): (UNKNOWN, BYTE, UINT16, INT16, 
                             UINT32, INT32, FLOAT32, FLOAT64, 
                             CINT16, CINT32, CFLOAT32, CFLOAT64) = range(12)

def probe_geotiff(geotiff_filename):

    # get read-only access to the dataset
    dataset = gdal.Open(geotiff_filename, GA_ReadOnly)

    #
    driver = dataset.GetDriver()
    projection = dataset.GetProjection()
    geotransform = dataset.GetGeoTransform()
    origin_x = geotransform[0]
    origin_y = geotransform[3]
    pixel_size_x = abs(geotransform[1])
    pixel_size_y = abs(geotransform[5])
    pixels_per_degree_x = int(1.0 / pixel_size_x)
    pixels_per_degree_y = int(1.0 / pixel_size_y)

    # get dataset dimensions
    x_size = dataset.RasterXSize
    y_size = dataset.RasterYSize
    band_count = dataset.RasterCount
    band = dataset.GetRasterBand(1)
    blocksize_x, blocksize_y= band.GetBlockSize()
    nodata_value = band.GetNoDataValue()

    print('Driver            {}'.format(driver.LongName))
    print('Origin            {}, {} '.format(origin_x, origin_y))
    print('Pixel size (deg)  {:f} x {:f}  (1/{:d} x 1/{:d})'
        .format(pixel_size_x, pixel_size_y, pixels_per_degree_x, pixels_per_degree_y))
    print('Raster size (px)  {} x {} '.format(x_size, y_size))
    print('Raster size (deg) {:f} x {:f}'.format(x_size * pixel_size_x, y_size * pixel_size_y))
    print('Number of bands   {}'.format(band_count))
    print('Block size        {} x {}'.format(blocksize_x, blocksize_y))
    print('No-data value     {}'.format(nodata_value))
    print('Pixel data type   {}'.format(RasterDataType(band.DataType).name))

    min = band.GetMinimum()
    max = band.GetMaximum()
    if min is None or max is None:
        (min,max) = band.ComputeRasterMinMax(1)

    print('Minimum value     {}'.format(min))
    print('Maximum value     {}'.format(max))


    # band_data = dataset.ReadRaster(xsize=blocksize_x, ysize=1)
    # print(len(band_data))

    # band = dataset.GetRasterBand(2000)

if __name__ == '__main__':
    
    parser = optparse.OptionParser()
    (options, args) = parser.parse_args()

    if len(args) != 1:
        print("\n***** ERROR: Required argument path_to_geotiff was not provided *****\n")
        exit()

    probe_geotiff(args[0])
