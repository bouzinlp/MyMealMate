package org.achartengine.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public class MultipleCategorySeries implements Serializable {
    private List<String> mCategories;
    private String mTitle;
    private List<String[]> mTitles;
    private List<double[]> mValues;

    public MultipleCategorySeries(String str) {
        this.mCategories = new ArrayList();
        this.mTitles = new ArrayList();
        this.mValues = new ArrayList();
        this.mTitle = str;
    }

    public void add(String str, String[] strArr, double[] dArr) {
        this.mCategories.add(str);
        this.mTitles.add(strArr);
        this.mValues.add(dArr);
    }

    public void add(String[] strArr, double[] dArr) {
        add(this.mCategories.size() + XmlPullParser.NO_NAMESPACE, strArr, dArr);
    }

    public void clear() {
        this.mCategories.clear();
        this.mTitles.clear();
        this.mValues.clear();
    }

    public int getCategoriesCount() {
        return this.mCategories.size();
    }

    public String getCategory(int i) {
        return (String) this.mCategories.get(i);
    }

    public int getItemCount(int i) {
        return ((double[]) this.mValues.get(i)).length;
    }

    public String[] getTitles(int i) {
        return (String[]) this.mTitles.get(i);
    }

    public double[] getValues(int i) {
        return (double[]) this.mValues.get(i);
    }

    public void remove(int i) {
        this.mCategories.remove(i);
        this.mTitles.remove(i);
        this.mValues.remove(i);
    }

    public XYSeries toXYSeries() {
        return new XYSeries(this.mTitle);
    }
}