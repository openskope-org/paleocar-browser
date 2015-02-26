package org.digitalantiquity.skope.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.spatial.prefix.RecursivePrefixTreeStrategy;
import org.apache.lucene.spatial.prefix.tree.GeohashPrefixTree;
import org.apache.lucene.spatial.prefix.tree.SpatialPrefixTree;
import org.apache.lucene.spatial.query.SpatialArgs;
import org.apache.lucene.spatial.query.SpatialOperation;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.postgis.Polygon;
import org.springframework.stereotype.Service;

import com.spatial4j.core.context.SpatialContext;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

@Service
public class LuceneService {

    private static final String X = "x";
    private static final String Y = "y";
    private static final String CODE = "code";
    private static final String QUAD = "quad";
    private static final String QUAD_ = QUAD + "_";
    private final Logger logger = Logger.getLogger(getClass());
    SpatialContext ctx = SpatialContext.GEO;
    SpatialPrefixTree grid = new GeohashPrefixTree(ctx, 24);
    RecursivePrefixTreeStrategy strategy = new RecursivePrefixTreeStrategy(grid, "location");


    public org.geojson.FeatureCollection searchBbox(double x1, double y1, double x2, double y2, int cols) throws IOException {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File("indexes")));
        org.geojson.FeatureCollection fc = new org.geojson.FeatureCollection();

        IndexSearcher searcher = new IndexSearcher(reader);
        List<Polygon> boxes = BoundingBoxHelper.createBoundindBoxes(x1, y1, x2, y2, cols);
        for (Polygon poly : boxes) {

            SpatialArgs args = new SpatialArgs(SpatialOperation.IsWithin, ctx.makeRectangle(Math.min(x1, x2), Math.max(x1,x2), Math.min(y1, y2), Math.max(y1, y2)));
            Filter filter = strategy.makeFilter(args);
            int limit = 1_000_000;
            TopDocs topDocs = searcher.search(new MatchAllDocsQuery(), filter, limit);
            logger.debug(topDocs.scoreDocs.length + " | " + poly);
            DoubleWrapper dw = new DoubleWrapper();
            for (int i = 0; i < topDocs.scoreDocs.length; i++) {
                Document document = reader.document(topDocs.scoreDocs[i].doc);
                dw.increment(Double.parseDouble(document.get(CODE)));
            }
            fc.add(FeatureHelper.createFeature(poly, dw.getAverage()));
        }
        return fc;
    }

    public org.geojson.FeatureCollection search(double x1, double y1, double x2, double y2, int cols) throws IOException {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File("indexes")));
        org.geojson.FeatureCollection fc = new org.geojson.FeatureCollection();

        IndexSearcher searcher = new IndexSearcher(reader);
        List<Polygon> boxes = BoundingBoxHelper.createBoundindBoxes(x1, y1, x2, y2, cols);
        Long q1 = Long.parseLong(toQuadTree(x1, y1, 18));
        Long q2 = Long.parseLong(toQuadTree(x2, y2, 18));
        Query q = NumericRangeQuery.newLongRange(QUAD_, q2, q1, true, true);
        if (q1 < q2) {
            q = NumericRangeQuery.newLongRange(QUAD_, q1, q2, true, true);
        }
        TopDocs search = searcher.search(q, null, 10000000);
        logger.debug(q + " (" + search.totalHits + ")");
        Map<String, DoubleWrapper> valueMap = new HashMap<String, DoubleWrapper>();
        for (int i = 0; i < search.scoreDocs.length; i++) {
            Document document = reader.document(search.scoreDocs[i].doc);
            String key = document.get(QUAD_);
            DoubleWrapper double1 = valueMap.get(key);
            if (double1 == null) {
                double1 = new DoubleWrapper();
            }
            double1.increment(Double.parseDouble(document.get(CODE)));
            valueMap.put(key, double1);
            // logger.debug(document);
        }

        Map<Polygon, DoubleWrapper> polymap = new HashMap<Polygon, DoubleWrapper>();
        for (String key : valueMap.keySet()) {
            logger.trace(key + " - " + valueMap.get(key).getAverage());
            boolean seen = false;
            String prev = "";
            Long key_ = Long.parseLong(key);
            // logger.debug(key);
            for (Polygon poly : boxes) {
                Long quadTree = Long.parseLong(toQuadTree(poly.getPoint(0).x, poly.getPoint(0).y, 18));
                Long quadTree_ = Long.parseLong(toQuadTree(poly.getPoint(2).x, poly.getPoint(2).y, 18));
                String message = quadTree + " - " + quadTree_;
                if (Math.min(quadTree, quadTree_) < key_ && Math.max(quadTree, quadTree_) > key_) {
                    DoubleWrapper doubleWrapper = polymap.get(poly);
                    if (doubleWrapper == null) {
                        doubleWrapper = new DoubleWrapper();
                    }
                    doubleWrapper.increment(valueMap.get(key).getAverage());
                    polymap.put(poly, doubleWrapper);
                    if (seen == true) {
                        // logger.error("seen twice:" + message);
                        // logger.error("          :" + prev);

                    }
                    prev = message;
                    seen = true;
                }
            }
        }
        for (Polygon poly : boxes) {
            DoubleWrapper doubleWrapper = polymap.get(poly);
            Double avg = -1d;
            if (doubleWrapper != null) {
                avg = doubleWrapper.getAverage();
            }
            fc.add(FeatureHelper.createFeature(poly, avg));
        }
        return fc;
    }

    public void parse() throws IOException {
        Map<String, URL> connect = new HashMap<>();
        File file = new File("/Users/abrin/Dropbox/skope-dev");
        connect.put("url", file.toURI().toURL());

        DataStore dataStore = DataStoreFinder.getDataStore(connect);
        String[] typeNames = dataStore.getTypeNames();
        String typeName = typeNames[0];
        logger.info(typeName);
        System.out.println("Reading content " + typeName);
        logger.info("info:" + dataStore.getInfo().getTitle() + dataStore.getInfo().getDescription());
        FeatureSource<?, ?> featureSource = dataStore.getFeatureSource(typeName);
        FeatureCollection<?, ?> collection = featureSource.getFeatures();
        FeatureIterator<?> iterator = collection.features();

        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LATEST, analyzer);

        if (true) {
            // Create a new index in the directory, removing any
            // previously indexed documents:
            iwc.setOpenMode(OpenMode.CREATE);
        } else {
            // Add new documents to an existing index:
            iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
        }

        // Optional: for better indexing performance, if you
        // are indexing many documents, increase the RAM
        // buffer. But if you do this, increase the max heap
        // size to the JVM (eg add -Xmx512m or -Xmx1g):
        //
        // iwc.setRAMBufferSizeMB(256.0);

        File path = new File("indexes");
        path.mkdirs();
        Directory dir = FSDirectory.open(path);
        IndexWriter writer = new IndexWriter(dir, iwc);
        int count = 0;

        Map<String, DoubleWrapper> valueMap = new HashMap<String, DoubleWrapper>();
        while (iterator.hasNext()) {
            count++;
            SimpleFeature obj = (SimpleFeature) iterator.next();
            Double gridCode = (Double) obj.getAttribute("GRID_CODE");

            Point point = (Point) obj.getDefaultGeometry();
            Coordinate coord = point.getCoordinate();
            String quadTree = toQuadTree(coord.x, coord.y, 18);
            long parseLong = Long.parseLong(quadTree);
            if (count % 10_000 == 0) {
                logger.debug(count + "| " + quadTree + " " + parseLong);
            }

            DoubleWrapper double1 = valueMap.get(quadTree);
            if (double1 == null) {
                double1 = new DoubleWrapper();
            }
            double1.increment(gridCode);
            valueMap.put(quadTree, double1);

            Document doc = new Document();

            Field latField = new StringField(X, Double.toString(coord.x), Field.Store.YES);
            Field longField = new StringField(Y, Double.toString(coord.y), Field.Store.YES);
            Field codeField = new StringField(CODE, Double.toString(gridCode), Field.Store.YES);
            Field quad = new LongField(QUAD, parseLong, Field.Store.YES);
            com.spatial4j.core.shape.Point makePoint = ctx.makePoint(coord.x, coord.y);
            for (Field f : strategy.createIndexableFields(makePoint)) {
                doc.add(f);
            }

            doc.add(latField);
            doc.add(longField);
            doc.add(codeField);
            doc.add(quad);
            writer.addDocument(doc);
            doc = new Document();
        }

        for (String key : valueMap.keySet()) {
            Double val = valueMap.get(key).getAverage();
            StringField codeField = new StringField(CODE, Double.toString(val), Field.Store.YES);
            LongField quad = new LongField(QUAD_, Long.parseLong(key), Field.Store.YES);
            Document doc = new Document();
            doc.add(codeField);
            doc.add(quad);
            writer.addDocument(doc);

        }
        writer.close();
    }

    public static String toQuadTree(Double x1_, Double y1_, int depth) {
        String toReturn = "";
        Double x1 = Math.floor(x1_ * Math.pow(2, 10) / 256);
        Double y1 = Math.floor(y1_ * Math.pow(2, 10) / 256);
        for (int i = depth; i > 0; i--) {
            int pow = 1 << (i - 1);
            int cell = 0;
            if ((x1.intValue() & pow) > 0) {
                cell++;
            }
            if ((y1.intValue() & pow) > 0) {
                cell += 2;
            }
            toReturn += cell;
        }
        return toReturn;
    }

    /**
     * function(x, y, z){
     * var arr = [];
     * for(var i=z; i>0; i--) {
     * var pow = 1<<(i-1);
     * var cell = 0;
     * if ((x&pow) != 0) cell++;
     * if ((y&pow) != 0) cell+=2;
     * arr.push(cell);
     * }
     * return arr.join("");
     * }
     **/

}
