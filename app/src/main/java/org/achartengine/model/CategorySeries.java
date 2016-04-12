package org.achartengine.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public class CategorySeries implements Serializable {
    private List<String> mCategories;
    private String mTitle;
    private List<Double> mValues;

    public CategorySeries(String str) {
        this.mCategories = new ArrayList();
        this.mValues = new ArrayList();
        this.mTitle = str;
    }

    public void add(double d) {
        add(this.mCategories.size() + XmlPullParser.NO_NAMESPACE, d);
    }

    public void add(String str, double d) {
        this.mCategories.add(str);
        this.mValues.add(Double.valueOf(d));
    }

    public void clear() {
        this.mCategories.clear();
        this.mValues.clear();
    }

    public String getCategory(int i) {
        return (String) this.mCategories.get(i);
    }

    public int getItemCount() {
        return this.mCategories.size();
    }

    public String getTitle() {
        return this.mTitle;
    }

    public double getValue(int i) {
        return ((Double) this.mValues.get(i)).doubleValue();
    }

    public void remove(int i) {
        this.mCategories.remove(i);
        this.mValues.remove(i);
    }

    public XYSeries toXYSeries() {
        XYSeries xYSeries = new XYSeries(this.mTitle);
        int i = 0;
        for (Double doubleValue : this.mValues) {
            int i2 = i + 1;
            xYSeries.add((double) i2, doubleValue.doubleValue());
            i = i2;
        }
        return xYSeries;
    }
}