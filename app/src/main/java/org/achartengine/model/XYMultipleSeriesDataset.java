package org.achartengine.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class XYMultipleSeriesDataset implements Serializable {
    private List<XYSeries> mSeries;

    public XYMultipleSeriesDataset() {
        this.mSeries = new ArrayList();
    }

    public void addSeries(XYSeries xYSeries) {
        this.mSeries.add(xYSeries);
    }

    public XYSeries[] getSeries() {
        return (XYSeries[]) this.mSeries.toArray(new XYSeries[0]);
    }

    public XYSeries getSeriesAt(int i) {
        return (XYSeries) this.mSeries.get(i);
    }

    public int getSeriesCount() {
        return this.mSeries.size();
    }

    public void removeSeries(int i) {
        this.mSeries.remove(i);
    }

    public void removeSeries(XYSeries xYSeries) {
        this.mSeries.remove(xYSeries);
    }
}