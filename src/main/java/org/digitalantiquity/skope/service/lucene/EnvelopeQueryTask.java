package org.digitalantiquity.skope.service.lucene;

import java.util.List;

import org.digitalantiquity.skope.service.LuceneService;
import org.geojson.FeatureCollection;
import org.postgis.Polygon;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class EnvelopeQueryTask {

    private FeatureCollection featureCollection = new FeatureCollection();

    public synchronized FeatureCollection getFeatureCollection() {
        return featureCollection;
    }

    public FeatureCollection run(ThreadPoolTaskExecutor taskExecutor, List<Polygon> boxes, LuceneService service, int level, int year) {
        for (Polygon poly : boxes) {
            EnvelopeQuerySubTask task = new EnvelopeQuerySubTask(this, poly, service, level, year);
            taskExecutor.execute(task);
        }

        while (taskExecutor.getActiveCount() != 0) {
            int count = taskExecutor.getActiveCount();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (count == 0) {
                taskExecutor.shutdown();
                break;
            }
        }
        return getFeatureCollection();
    }

}