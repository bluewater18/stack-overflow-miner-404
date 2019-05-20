package chart;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.colors.ChartColor;
import parsing.Parser;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.List;

public class CustomAreaChart {
    private Map<Integer,Map<String, Integer>> map;
    private List<Integer> years = new ArrayList<>(Arrays.asList(2009,2010,2011,2012,2013,2014,2015,2016,2017,2018,2019));
    private XYChart chart;
    private String jarString;
    private String chartName;


    public CustomAreaChart(String chartName, Map<Integer,Map<String, Integer>> map) {
        this.chartName = chartName;
        this.map = map;
        createChart();
        setStyle(chart);
        export();
    }

    private void createChart() {

        String jarUrl = null;
        try {
            jarUrl = URLDecoder.decode(Parser.class.getProtectionDomain().getCodeSource().getLocation().getPath(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assert jarUrl != null;
        File jarDir = new File(jarUrl);
        jarString = jarDir.getParentFile().toString();



        chart = new XYChartBuilder().width(2048).height(2048).xAxisTitle("Year").yAxisTitle("Occurrences").title(chartName).build();



        Map<String, Map<Integer, Integer>> data = new HashMap<>();
//        for(Map.Entry<String, Map<Integer, Integer>> e: data.entrySet()) {
//            for(Integer year: years)
//                e.getValue().put(year,0);
//        }

        //List<Map.Entry<Integer, Map<String, Integer>>> temp = sortMapByYear(map);

        for(Map.Entry<Integer, Map<String, Integer>> e: map.entrySet()) {
            //List<Map.Entry<String, Integer>> tempInside = sortMapByOccurence(e.getValue());
            for(Map.Entry<String, Integer> m : e.getValue().entrySet()) {
                if(data.get(m.getKey()) == null) {
                    data.put(m.getKey(), new HashMap<Integer, Integer>());

                }
                data.get(m.getKey()).put(e.getKey(), m.getValue());
            }
        }
        if(chartName.contains("tag"))
            addTagSeries(data);
        else
            addNormalSeries(data);

    }

    private void addTagSeries(Map<String,Map<Integer,Integer>> data) {
        for(Map.Entry<String, Map<Integer, Integer>> e: data.entrySet())
        {
            double[] years = new double[]{2009,2010,2011,2012,2013,2014,2015,2016,2017,2018,2019};
            double[] values = new double[11];
            int count = 0;
            boolean add = false;



            for(Map.Entry<Integer, Integer> m : sortMapByYearInt(e.getValue())){
                count++;
                if(m.getValue().doubleValue() <30 || m.getKey() < 2009 || m.getKey()> 2019)
                    continue;
                values[count-1] = m.getValue().doubleValue();
                add = true;

            }
            if(e.getKey().equals("architecture") || !add)
                continue;
            chart.addSeries(e.getKey(), years, values);
        }
    }

    private void addNormalSeries(Map<String, Map<Integer, Integer>> data) {
        for(Map.Entry<String, Map<Integer, Integer>> e: data.entrySet())
        {
            double[] years = new double[11];
            double[] values = new double[11];
            int count = 0;

            for(Map.Entry<Integer, Integer> m : sortMapByYearInt(e.getValue())){
                years[count] = m.getKey().doubleValue();
                values[count] = m.getValue().doubleValue();
                count++;
            }
            chart.addSeries(e.getKey(), years, values);


        }
    }

    private List<Map.Entry<Integer, Integer>> sortMapByYearInt(Map<Integer, Integer> map) {
        List<Map.Entry<Integer, Integer>> list = new LinkedList<Map.Entry<Integer, Integer>>(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> t0, Map.Entry<Integer, Integer> t1) {
                return(t0.getKey()).compareTo(t1.getKey());
            }
        });

        return list;

    }


    private List<Map.Entry<Integer, Map<String, Integer>>> sortMapByYear(Map<Integer, Map<String, Integer>> map) {
        List<Map.Entry<Integer, Map<String, Integer>>> list = new LinkedList<>(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<Integer, Map<String,Integer>>>() {
            @Override
            public int compare(Map.Entry<Integer, Map<String, Integer>> t0, Map.Entry<Integer, Map<String,Integer>> t1) {
                return(t0.getKey()).compareTo(t1.getKey());
            }
        });
        return list;
    }

    /**
     * Sets the style of the chart
     * @param chart chart to have style changed
     */
    private void setStyle(XYChart chart) {

        chart.getStyler().setYAxisMin(0.00);
        chart.getStyler().setToolTipsEnabled(true);
        chart.getStyler().setHasAnnotations(true);
        chart.getStyler().setPlotBackgroundColor(ChartColor.getAWTColor(ChartColor.GREY));
        chart.getStyler().setPlotGridLinesColor(new Color(255, 0, 255));
        chart.getStyler().setChartBackgroundColor(Color.WHITE);
        chart.getStyler().setLegendBackgroundColor(Color.WHITE);
        chart.getStyler().setChartFontColor(Color.BLACK);
        chart.getStyler().setChartTitleBoxBackgroundColor(Color.white);
        chart.getStyler().setChartTitleBoxVisible(true);
        chart.getStyler().setChartTitleBoxBorderColor(Color.BLACK);
        chart.getStyler().setPlotGridLinesVisible(false);
        chart.getStyler().setChartTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 32));
        chart.getStyler().setLegendFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        chart.getStyler().setLegendSeriesLineLength(12);
        chart.getStyler().setLegendLayout(Styler.LegendLayout.Vertical);
        chart.getStyler().setAxisTitleFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));
        chart.getStyler().setDecimalPattern("#0.0");
        chart.getStyler().setPlotBorderColor(Color.WHITE);
        chart.getStyler().setPlotBackgroundColor(Color.WHITE);

    }

    private void export(){
        try {
            BitmapEncoder.saveBitmap(chart, jarString+ System.getProperty("file.separator") + chartName + ".png", BitmapEncoder.BitmapFormat.PNG );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
