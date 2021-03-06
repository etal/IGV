/*
 * Copyright (c) 2007-2012 The Broad Institute, Inc. 
 * SOFTWARE COPYRIGHT NOTICE
 * This software and its documentation are the copyright of the Broad Institute, Inc. All rights are reserved.
 *
 * This software is supplied without any warranty or guaranteed support whatsoever. The Broad Institute is not responsible for its use, misuse, or functionality.
 *
 * This software is licensed under the terms of the GNU Lesser General Public License (LGPL),
 * Version 2.1 which is available at http://www.opensource.org/licenses/lgpl-2.1.php.
 */

package org.broad.igv.feature;

import org.broad.igv.track.FeatureSource;
import htsjdk.tribble.Feature;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * A FeatureSource wrapper which provides caching.
 * The cache is only cleared when the source is closed.
 *
 * @author jacob
 * @date 2012/05/15
 */
public class CachingFeatureSource extends AbstractCacher implements FeatureSource {

    private static final int maxBinCount = 1000;
    private static final int defaultBinSize = 16000; // <= 16 kb

    private FeatureSource source;


    /**
     * Wraps the provided {@code source} with a caching version,
     * using default parameters.
     * @param source
     * @api
     */
    public CachingFeatureSource(FeatureSource source) {
        this(source, maxBinCount, defaultBinSize);
    }


    public CachingFeatureSource(FeatureSource source, int binCount, int binSize) {
        super(binCount, binSize);
        this.source = source;
    }

    @Override
    protected Iterator<Feature> queryRaw(String chr, int start, int end) throws IOException {
        return source.getFeatures(chr, start, end);
    }

    @Override
    public Iterator getFeatures(String chr, int start, int end) throws IOException {
        return super.queryCached(chr, start, end);
    }

    @Override
    public List<LocusScore> getCoverageScores(String chr, int start, int end, int zoom) {
        return source.getCoverageScores(chr, start, end, zoom);
    }

    @Override
    public int getFeatureWindowSize() {
        return source.getFeatureWindowSize();
    }

    @Override
    public void setFeatureWindowSize(int size) {
        source.setFeatureWindowSize(size);
    }

    /**
     * Return the source which backs this CachingFeatureSource
     * @return
     */
    public FeatureSource getSource(){
        return this.source;
    }
}
