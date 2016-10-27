#!/usr/bin/perl


##################################################################################################################
#
# This script takes the input files from Kyle and follows a few processes:
# 1. it merges them into latitude based groups so there are fewer files to manage
# 2. it then extracts each 'band' of data from the geotiff into its own file
# 3. finally, it recombines the latitude based groups into a single tile per band
#
#
# This script is dependent on GDAL being installed on your local machine

# @begin reslice_paleocar_retrodictions
# @in lat_long_slices @uri file:in/{lat}W{long}N.recon.tif
# @out year_slices @uri file:out/merge_{year}.tif

use threads;

# begin define_gdal_commands
# out $GDAL_MERGE as gdal_merge_command
# out $GDAL_TRANSLATE as gdal_translate_command
my $GDAL_MERGE = "gdal_merge.py";
my $GDAL_TRANSLATE = "gdal_translate";
# end define_gdal_commands

# begin create_output_dirs
mkdir("out");
mkdir("out/tmp");
mkdir("out/comb");
# out directories_created
# end create_output_dirs

my @prefixes = ();


# @begin combine_latitudes @desc Combine (1deg x 1deg x 2000yr) columns \n into (1deg x 12deg x 2000yr) slabs
# param directories_created
# param $GDAL_MERGE as gdal_merge_command
# @in lat_long_slices
# @out long_slices @uri file:out/comb/{long}W_comb.tif
print ">> PROCESSING INPUTS AND CREATING LATITUDE BASED PASSES \n\n";

my @seq = (103..115);
my @prefixes = ();
my @threads = ();

foreach my $i (@seq) {

	my $prefix = $i."W_comb";
	my $outfile = "out/comb/".$prefix.".tif";
	push @prefixes, $prefix;
	if (! -e $outfile) {
		my $cmd = "$GDAL_MERGE -o $outfile in/".$i."W*.recon.tif";
	    my $thr = async(sub{
			print $cmd." --> ";
			system($cmd) or print STDERR "$?";
			print "\r\n";
	    } ,cmd);
        push(@threads, $thr);
	}
}

foreach my $thr (@threads) {
    $thr->join();
}
# @end combine_latitudes


# @begin split_years @desc Split (1deg x 12deg x 2000yr) slabs \n into (1deg x 12deg x 1yr) columns 
# param gdal_translate_command
# @in long_slices @uri file:out/comb/{long}W_comb.tif
# @out long_year_slices @uri file:out/tmp/{long}W_comb_{year}.tif

print "\n>> PROCESSING LATITUDE BASED PASSES EXTRACTING BANDS AND CREATING UNIFIED BANDS\n\n";

# taking each "strip" and pulling out each "year's" worth of data
foreach my $i (1..2000) {
	@threads = ();
	print ">>> PROCESSING BAND $i \n\n";	
	foreach my $prefix (@prefixes) {
	    my $thr = async(sub{
			my $filename="out/comb/".$prefix.".tif";
			my $outfile="out/tmp/".$prefix."_".$i.".tif";
			my $cmd = "$GDAL_TRANSLATE -of GTiff -b $i $filename $outfile";
			print " $i $prefix >> " . $cmd." --> ". (system($cmd) or print STDERR "$?"). "\r\n";	    	
	    } ,$prefix);
        push(@threads, $thr);
	}

	foreach my $thr (@threads) {
	    $thr->join();
	}
# @end split_years

# @begin combine_longitudes @desc Combine (1deg x 12deg x 1yr) columns \n into (13deg x 12deg x 1yr) slabs
# param $GDAL_MERGE as gdal_merge_command
# @in long_year_slices @uri file:out/tmp/{long}W_comb_{year}.tif
# @out year_slices @uri file:out/merge_{year}.tif

	# taking each "year" and creating a combined tiff
	my $outfile = "out/merge_".$i.".tif";
	my $input = "out/tmp/*_".$i.".tif";
	my $cmd = "$GDAL_MERGE -o $outfile $input";
	print "\n  ===> " . $cmd . " --> ";
	system($cmd) or print STDERR "$?";
}
# @end combine_longitudes

# @end reslice_paleocar_retrodictions
