package com.example.awsmonitor;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.example.awsmonitor.GSNServer.VirtualSensor;
import com.example.awsmonitor.GsnParser;
import com.example.awsmonitor.GsnParser.Entry;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class MainActivity extends Activity {
	
	private static final String TAG = "MainActivity";
	
	//static final String BASE_URL = "http://verruca.iet.unipi.it:22001/multidata?vs[0]=lamare_raw&field[0]=airtc&agg_function=avg&agg_unit=604800000&agg_period=1&download_format=xml&time_format=iso&download_mode=inline";
	static final String BASE_URL = "http://verruca.iet.unipi.it:22001/multidata?vs[0]=lamare_raw&field[0]=airtc&from=25%2F12%2F2012+00%3A00%3A00&to=26%2F12%2F2012+00%3A00%3A00&time_format=iso&download_format=xml&download_mode=inline&reportclass=report-default";
	private GSNServer server;
	
	
	/*
	 * Google Cloud Messaging Stuff...
	 */
	String SENDER_ID = "762668984695";
	GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;
    TextView mDisplay;


    String regid;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_ON_SERVER_EXPIRATION_TIME = "onServerExpirationTimeMs";

	private static final long REGISTRATION_EXPIRY_TIME_MS = 1000 * 3600 * 24 * 7;
    


	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        context = getApplicationContext();

		Downloader dl = new Downloader(){
			protected void onPostExecute(ArrayList<String> result){
				viewResult(result);
				super.onPostExecute(result);
			}
		};
		dl.execute(BASE_URL);
		this.server = new GSNServer();
		GSNServer.VirtualSensor vs = this.server.new VirtualSensor("lamare_raw");
		//server.vs.add(vs);
		//Date latest = server.vs.get(0).getLatestRecord();
		Date latest = new Date(0);
		try {
			latest = vs.getLatestRecord();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setContentView(R.layout.activity_main);
		TextView tv = (TextView)findViewById(R.id.lastRecord);
		SimpleDateFormat format_latest = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
		tv.setText(getText(R.string.ultimo_record)+" "+format_latest.format(latest));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void viewResult(ArrayList<String> result){
		TextView tv = (TextView)findViewById(R.id.lastDay);
		float [] floatValues = new float[result.size()];

		for (int i = 0; i < result.size(); i++) {
		    floatValues[i] = Float.parseFloat(result.get(i));
		}
		Float tmp = 0.0f;
		for (Float tmp1 : floatValues) {
			tmp += tmp1;
		}
		
		tv.setText(getText(R.string.testo1).toString() + (tmp/result.size())+"°C");
	}
	
	public void graphMonthTempHandler(View view){
		//long latest = this.server.vs.get(0).getLatestTimed();
		Date[] x = {new Date(0),new Date(2),new Date(3)};
		double[] y = {1,2,3};
		
		LineGraph line = new LineGraph(x,y);
		Intent lineIntent = line.getIntent(this);
		startActivity(lineIntent);
	}
}
