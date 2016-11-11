#!/usr/bin/env python

import optparse
import struct

from osgeo import gdal
from gdalconst import GA_ReadOnly

def probe_geotiff(geotiff_filename):

    # get read-only access to the dataset in the file
    dataset = gdal.Open(geotiff_filename, GA_ReadOnly)

    # print GDAL driver information
    driver = dataset.GetDriver()
    print('')
    print('Driver: {}'.format(driver.LongName))
    print('')

    print("***** Dataset *****")
    geotransform = dataset.GetGeoTransform()
    projection = dataset.GetProjection()
    print('Projection:  {}'.format(projection))
    origin_x = geotransform[0]
    origin_y = geotransform[3]
    print('Origin:      {}, {} '.format(origin_x, origin_y))
    pixel_size_x = geotransform[1]
    pixel_size_y = - geotransform[5]
    pixels_per_degree_x = int(1.0 / pixel_size_x)
    pixels_per_degree_y = int(1.0 / pixel_size_y)
    print('Pixel size:  {:f} x {:f}  (1/{:d} x 1/{:d})'.format(
        pixel_size_x, pixel_size_y, pixels_per_degree_x, pixels_per_degree_y))

    # print dataset dimensions
    x_size = dataset.RasterXSize 
    y_size = dataset.RasterYSize 
    print('Raster size: {} x {} ({:f} deg x {:f} deg)'.format(x_size, y_size, x_size * pixel_size_x, y_size * pixel_size_y))
    bands = dataset.RasterCount
    print('Bands:       {}'.format(bands))

#    print('Dataset size:  {}pixels x {}pixels x {} bands ({:f} deg)').format(x_size,  x_size * pixel_size_x)
#    print('Y-size:  {} pixels ({:f} deg)').format(y_size)
    print('')

    # print band 1 information

    print('***** Band 1 *****')
    band = dataset.GetRasterBand(1)
    blocksize_x, blocksize_y= band.GetBlockSize()
    nodata_value = band.GetNoDataValue()
    print('Block size:     {}x{}'.format(blocksize_x, blocksize_y))
    print('No-data value:  {}'.format(nodata_value))
    print(band.DataType)
    print('')

    metadata = band.GetMetadataItem('TIFFTAG_GDAL_NODATA')
    print(metadata)

    band_data = dataset.ReadRaster(xsize=blocksize_x, ysize=1)
    print(len(band_data))

    band = dataset.GetRasterBand(2000)


if __name__ == '__main__':
    
    parser = optparse.OptionParser()
    (options, args) = parser.parse_args()

    if len(args) != 1:
        print("\n***** ERROR: Required argument path_to_geotiff was not provided *****\n")
        exit()

    probe_geotiff(args[0])
