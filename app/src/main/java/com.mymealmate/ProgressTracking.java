package com.mymealmate;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.mymealmate.ExerciseEntryData.Cursor;
import com.mymealmate.MyMealMateDatabase.MyMealMateCursor;
import com.mymealmate.Prefs.Unit;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.TimeChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class ProgressTracking extends ListActivity {
    private MyMealMateDatabase db_;
    private String[] menuText_;

    public ProgressTracking() {
        this.menuText_ = new String[]{"Weight chart", "Daily calorie consumption chart"};
        this.db_ = null;
    }

    private XYMultipleSeriesDataset getBarDataset(double[] dArr, double[] dArr2) {
        XYMultipleSeriesDataset xYMultipleSeriesDataset = new XYMultipleSeriesDataset();
        int length = dArr.length;
        int i = 0;
        while (i < 2) {
            CategorySeries categorySeries = i == 0 ? new CategorySeries("Achived calorie goal") : new CategorySeries("Exceeds calorie goal");
            for (int i2 = 0; i2 < length; i2++) {
                if (i == 0) {
                    if (dArr[i2] > dArr2[i2]) {
                        categorySeries.add(0.0d);
                    } else {
                        categorySeries.add(dArr[i2]);
                    }
                } else if (dArr[i2] > dArr2[i2]) {
                    categorySeries.add(dArr[i2]);
                } else {
                    categorySeries.add(0.0d);
                }
            }
            xYMultipleSeriesDataset.addSeries(categorySeries.toXYSeries());
            i++;
        }
        return xYMultipleSeriesDataset;
    }

    private float getCaloriesForDate(long j) {
        float f = 0.0f;
        long dateTime = DateProvider.getInstance().getDateTime();
        DateProvider.getInstance().setDateTime(j);
        Iterator it = FoodEntryData.getArrayByDate(this.db_, DateProvider.getInstance()).iterator();
        float f2 = 0.0f;
        while (it.hasNext()) {
            FoodEntryData foodEntryData = (FoodEntryData) it.next();
            if (foodEntryData.foodItem != 0) {
                MyMealMateCursor foodCursorById = this.db_.getFoodCursorById(foodEntryData.foodItem);
                float size = (foodEntryData.amount * ((foodCursorById.getSize() / 100.0f) * foodCursorById.getCalories())) + f2;
                foodCursorById.close();
                f2 = size;
            }
        }
        Cursor byDate = ExerciseEntryData.getByDate(this.db_, DateProvider.getInstance());
        DateProvider.getInstance().setDateTime(dateTime);
        for (int i = 0; i < byDate.getCount(); i++) {
            byDate.moveToPosition(i);
            f += byDate.getCals().floatValue();
        }
        byDate.close();
        return f2 - f;
    }

    private XYMultipleSeriesDataset getDateWeightDataset(double[] dArr, long j) {
        XYMultipleSeriesDataset xYMultipleSeriesDataset = new XYMultipleSeriesDataset();
        int length = dArr.length;
        XYSeries timeSeries = new TimeSeries("Weight");
        timeSeries.add(new Date(j + TimeChart.DAY), dArr[length - 1]);
        for (int i = 0; i < length; i++) {
            timeSeries.add(new Date(j - (((long) i) * TimeChart.DAY)), dArr[(length - i) - 1]);
        }
        xYMultipleSeriesDataset.addSeries(timeSeries);
        return xYMultipleSeriesDataset;
    }

    private XYMultipleSeriesRenderer getLineRenderer(double d, double d2, Unit unit) {
        XYMultipleSeriesRenderer xYMultipleSeriesRenderer = new XYMultipleSeriesRenderer();
        xYMultipleSeriesRenderer.setChartTitle(this.menuText_[0]);
        xYMultipleSeriesRenderer.setAxesColor(-12303292);
        xYMultipleSeriesRenderer.setLabelsColor(DefaultRenderer.TEXT_COLOR);
        if (unit == Unit.METRIC) {
            xYMultipleSeriesRenderer.setYTitle("kg");
            xYMultipleSeriesRenderer.setYAxisMin(d2 - 0.5d);
            xYMultipleSeriesRenderer.setYAxisMax(0.5d + d);
        } else if (unit == Unit.IMPERIAL_ST) {
            xYMultipleSeriesRenderer.setYTitle("st / lb");
            double d3 = (double) ((int) d2);
            double d4 = (double) ((int) d);
            if (d4 - d3 > 1.0d) {
                d4 += 1.0d;
            } else {
                d3 += 0.5d;
                d4 += 0.5d;
                xYMultipleSeriesRenderer.setYLabels(3);
            }
            xYMultipleSeriesRenderer.setYAxisMin(d3);
            xYMultipleSeriesRenderer.setYAxisMax(d4);
        } else {
            xYMultipleSeriesRenderer.setYTitle("lbs");
            xYMultipleSeriesRenderer.setYAxisMin(d2 - 5.0d);
            xYMultipleSeriesRenderer.setYAxisMax(5.0d + d);
        }
        xYMultipleSeriesRenderer.setXLabels(7);
        SimpleSeriesRenderer xYSeriesRenderer = new XYSeriesRenderer();
        xYSeriesRenderer.setPointStyle(PointStyle.CIRCLE);
        xYSeriesRenderer.setColor(-16711936);
        xYSeriesRenderer.setFillPoints(true);
        xYMultipleSeriesRenderer.addSeriesRenderer(xYSeriesRenderer);
        xYMultipleSeriesRenderer.setLabelsTextSize(15.0f);
        xYMultipleSeriesRenderer.setAxisTitleTextSize(15.0f);
        xYMultipleSeriesRenderer.setChartTitleTextSize(25.0f);
        xYMultipleSeriesRenderer.setLegendTextSize(20.0f);
        return xYMultipleSeriesRenderer;
    }

    private void setChartSettings(XYMultipleSeriesRenderer xYMultipleSeriesRenderer, double d, Calendar calendar) {
        xYMultipleSeriesRenderer.setChartTitle(this.menuText_[1]);
        xYMultipleSeriesRenderer.setYTitle("kcal");
        xYMultipleSeriesRenderer.setYAxisMin(0.0d);
        xYMultipleSeriesRenderer.setYAxisMax(5.0d + d);
        xYMultipleSeriesRenderer.setXAxisMin(0.5d);
        xYMultipleSeriesRenderer.setXAxisMax(31.0d);
        xYMultipleSeriesRenderer.setXLabels(1);
        for (int i = 30; i >= 0; i -= 6) {
            xYMultipleSeriesRenderer.addTextLabel((double) i, DateProvider.getFormatedDate(calendar));
            calendar.add(5, -6);
        }
    }

    public XYMultipleSeriesRenderer getBarRenderer() {
        XYMultipleSeriesRenderer xYMultipleSeriesRenderer = new XYMultipleSeriesRenderer();
        SimpleSeriesRenderer simpleSeriesRenderer = new SimpleSeriesRenderer();
        simpleSeriesRenderer.setColor(-16711936);
        xYMultipleSeriesRenderer.addSeriesRenderer(simpleSeriesRenderer);
        simpleSeriesRenderer = new SimpleSeriesRenderer();
        simpleSeriesRenderer.setColor(-65536);
        xYMultipleSeriesRenderer.addSeriesRenderer(simpleSeriesRenderer);
        xYMultipleSeriesRenderer.setLabelsTextSize(15.0f);
        xYMultipleSeriesRenderer.setAxisTitleTextSize(15.0f);
        xYMultipleSeriesRenderer.setChartTitleTextSize(25.0f);
        xYMultipleSeriesRenderer.setLegendTextSize(20.0f);
        return xYMultipleSeriesRenderer;
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.progress_tracking);
        setListAdapter(new ArrayAdapter(this, 17367043, this.menuText_));
        this.db_ = new MyMealMateDatabase(this);
    }

    protected void onListItemClick(ListView listView, View view, int i, long j) {
        Intent timeChartIntent;
        super.onListItemClick(listView, view, i, j);
        double d;
        if (i == 0) {
            double[] dArr = new double[30];
            DateProvider instance = DateProvider.getInstance();
            long dateTime = instance.getDateTime();
            Prefs prefs = new Prefs(this.db_, this);
            Object obj = prefs.getWeightUnit() == Unit.METRIC ? 1 : null;
            d = 0.0d;
            double d2 = 0.0d;
            for (int i2 = 29; i2 >= 0; i2--) {
                dArr[i2] = (double) WeightData.getActualWeight(this.db_, instance.getEndOfDay());
                if (obj == null) {
                    if (prefs.getWeightUnit() == Unit.IMPERIAL_ST) {
                        dArr[i2] = (double) (WeightData.KgToLbs((float) dArr[i2]) / 14.0f);
                    } else {
                        dArr[i2] = (double) WeightData.KgToLbs((float) dArr[i2]);
                    }
                }
                if (d <= dArr[i2]) {
                    d = dArr[i2];
                }
                if (d2 == 0.0d) {
                    d2 = dArr[i2];
                }
                if (d2 >= dArr[i2]) {
                    d2 = dArr[i2];
                }
                instance.prevDate();
            }
            instance.setDateTime(dateTime);
            timeChartIntent = ChartFactory.getTimeChartIntent(this, getDateWeightDataset(dArr, dateTime), getLineRenderer(d, d2, prefs.getWeightUnit()), null);
        } else {
            Calendar instance2 = Calendar.getInstance();
            instance2.set(10, 1);
            instance2.set(12, 1);
            instance2.set(13, 1);
            instance2.set(14, 1);
            long timeInMillis = instance2.getTimeInMillis();
            double[] dArr2 = new double[30];
            double[] dArr3 = new double[30];
            d = 0.0d;
            int i3 = 29;
            while (i3 >= 0) {
                dArr2[i3] = (double) getCaloriesForDate(instance2.getTime().getTime());
                dArr3[i3] = (double) TargetSettings.getGoal(this.db_, instance2.getTime().getTime());
                double d3 = dArr2[i3] > dArr3[i3] ? dArr2[i3] : dArr3[i3];
                if (d > d3) {
                    d3 = d;
                }
                instance2.add(5, -1);
                i3--;
                d = d3;
            }
            XYMultipleSeriesRenderer barRenderer = getBarRenderer();
            instance2.setTimeInMillis(timeInMillis);
            setChartSettings(barRenderer, d, instance2);
            timeChartIntent = ChartFactory.getBarChartIntent(this, getBarDataset(dArr2, dArr3), barRenderer, Type.STACKED);
        }
        startActivity(timeChartIntent);
    }

    public void onStop() {
        this.db_.close();
        super.onStop();
    }
}