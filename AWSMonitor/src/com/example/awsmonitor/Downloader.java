package com.example.awsmonitor;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;
import android.util.Log;
//import android.widget.TextView;

import com.example.awsmonitor.GsnParser.Entry;

class Downloader extends AsyncTask<String, Integer, ArrayList<String>>{
	
	ArrayList<String> getValue(String url) throws IOException{
		try{
			Log.i("Downloader.getValue(String url)", url);
			return loadXmlFromNetwork(url);
		} catch (XmlPullParserException e) {
			Log.e("Main", e.getMessage());
            //return (float) -1234.0;//getResources().getString(R.string.xml_error);
			return null;
        }

	}
	
	protected ArrayList<String> doInBackground(String... url){
		try{
			//callerActivity = (MainActivity) arg0[1];
			ArrayList<String> parsed = getValue(url[0]);
			return parsed;
		} catch (IOException e){				
			Log.e("MainActivity", e.getMessage());
			//return Float.NaN;
			return null;
		}
	}
	
	/*protected void onPostExecute(ArrayList<Float> result){
		callerActivity.viewResult(result);
		super.onPostExecute(result);
	TextView tv = (TextView)findViewById(R.id.textView1);

		Float tmp = 0.0f;
		for (Float tmp1 : result) {
			tmp += tmp1;
		}
		
		tv.setText(getText(R.string.testo1).toString() + (tmp/result.size())); 
	}*/
	
	private ArrayList<String> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
		InputStream stream = null;
		
	    // Instantiate the parser
	    GsnParser parser = new GsnParser();
	    List<GsnParser.Entry> entries = null;
	    ArrayList<String> values = new ArrayList<String>();
	    /*Calendar rightNow = Calendar.getInstance(); 
	    DateFormat formatter = new SimpleDateFormat("MMM dd h:mmaa");
	        
	    // Checks whether the user set the preference to include summary text
	    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
	    boolean pref = sharedPrefs.getBoolean("summaryPref", false);
	        
	    StringBuilder htmlString = new StringBuilder();
	    htmlString.append("<h3>" + getResources().getString(R.string.page_title) + "</h3>");
	    htmlString.append("<em>" + getResources().getString(R.string.updated) + " " + 
	            formatter.format(rightNow.getTime()) + "</em>");
	       */ 
	    try {
	        stream = downloadUrl(urlString);        
	        entries = parser.parse(stream);
	    // Makes sure that the InputStream is closed after the app is
	    // finished using it.
	    } finally {
	        if (stream != null) {
	            stream.close();
	        } 
	     }
	    
	    // StackOverflowXmlParser returns a List (called "entries") of Entry objects.
	    // Each Entry object represents a single post in the XML feed.
	    // This section processes the entries list to combine each entry with HTML markup.
	    // Each entry is displayed in the UI as a link that optionally includes
	    // a text summary.
	    for (Entry entry : entries) {       
	    	values.add(entry.field);
	    }
	    return values;
	}

	// Given a string representation of a URL, sets up a connection and gets
	// an input stream.
	public InputStream downloadUrl(String urlString) throws IOException {
	    URL url = new URL(urlString);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setReadTimeout(10000 /* milliseconds */);
	    conn.setConnectTimeout(15000 /* milliseconds */);
	    conn.setRequestMethod("GET");
	    conn.setDoInput(true);
	    // Starts the query
	    conn.connect();
	    return conn.getInputStream();
	}
}