package com.example.awsmonitor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
//import android.util.Log;

public class GSNServer {
	//private static final String TAG = "GSNServer";
	private static final String BASE_URL = "http://verruca.iet.unipi.it";
	private static int port = 22001;
	 
	public List<VirtualSensor> vs;
	
	public class VirtualSensor{
		private String name;
		public List<Field> fields;
		private long latestTimed;
		
		public VirtualSensor(String name){
			this.name = name;
		}
		
		@SuppressLint("NewApi")
		public Date getLatestRecord() throws InterruptedException, ExecutionException{
			String url = BASE_URL+":"+String.valueOf(port)+"/gsn?REQUEST=114&name="+this.name+"&fields=max(timed)";
			Downloader dl = new Downloader(){
				protected void onPostExecute(ArrayList<String> result){
					super.onPostExecute(result);
				}
			};
			
			dl.execute(url);

			ArrayList<String> tmp = dl.get();
			this.latestTimed = Long.parseLong(tmp.get(0));
			Date latest = new Date(this.latestTimed);
			return latest;
		}

		public long getLatestTimed() {
			return this.latestTimed;
		}
	}
	
	public class Field {
		private String name;
		
		public Field(String name){
			this.name = name;
		}
	}	
	
}
