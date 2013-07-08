package com.example.awsmonitor;

import java.util.Date;

import org.achartengine.ChartFactory;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;

public class LineGraph {
	private Date[] x;
	private double[] y;
	
	public LineGraph(Date[] x, double[] y){
		this.x = x;
		this.y = y;
	}
	
	public Intent getIntent (Context context){
		
		TimeSeries series = new TimeSeries("Line 1");
		for(int i = 0; i < this.x.length; i++){
			series.add(this.x[i], this.y[i]);
		}
		
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(series);
		
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		XYSeriesRenderer renderer = new XYSeriesRenderer();
		mRenderer.addSeriesRenderer(renderer);
		
		Intent intent = ChartFactory.getLineChartIntent(context, dataset, mRenderer, "TITLE");
		
		return intent;
	}
}
