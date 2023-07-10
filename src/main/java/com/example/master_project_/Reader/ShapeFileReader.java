package com.example.master_project_.Reader;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

public class ShapeFileReader {
    private static final Logger LOGGER = Logger.getLogger(ShapeFileReader.class.getName());

    public static SimpleFeatureCollection shapeFileReader(String filename) throws IOException {
        try {
            File file = new File(filename);
            FileDataStore fileDataStore = FileDataStoreFinder.getDataStore(file);

            if (fileDataStore == null) {
                throw new FileNotFoundException("Shape file not found: " + filename);
            }

            SimpleFeatureCollection features = fileDataStore.getFeatureSource().getFeatures();

            return features;
        } catch (FileNotFoundException e) {
            LOGGER.severe("Shape file not found: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.severe("Error reading shape file: " + e.getMessage());
            throw new IOException("Error reading shape file", e);
        }
    }
}
