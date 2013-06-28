package com.example.awsmonitor;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.example.awsmonitor.GsnParser;
import com.example.awsmonitor.GsnParser.Entry;

public class MainActivity extends Activity {
	
	//static final String BASE_URL = "http://verruca.iet.unipi.it:22001/multidata?vs[0]=lamare_raw&field[0]=airtc&agg_function=avg&agg_unit=604800000&agg_period=1&download_format=xml&time_format=iso&download_mode=inline";
	static final String BASE_URL = "http://verruca.iet.unipi.it:22001/multidata?vs[0]=lamare_raw&field[0]=airtc&from=25%2F12%2F2012+00%3A00%3A00&to=26%2F12%2F2012+00%3A00%3A00&time_format=iso&download_format=xml&download_mode=inline&reportclass=report-default";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Downloader dl = new Downloader();
		dl.execute(BASE_URL, this);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void viewResult(ArrayList<Float>result){
		TextView tv = (TextView)findViewById(R.id.textView1);

		Float tmp = 0.0f;
		for (Float tmp1 : result) {
			tmp += tmp1;
		}
		
		tv.setText(getText(R.string.testo1).toString() + (tmp/result.size()));
	}
}
