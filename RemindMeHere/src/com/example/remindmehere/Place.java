package com.example.remindmehere;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener;
import com.google.android.gms.location.LocationClient.OnRemoveGeofencesResultListener;

public class Place extends Activity{
	double[] lat = {1};
	double[] longi = {1};
	String[] name = {"poop"};
	int length;
	ArrayList<Geofence> fences = new ArrayList<Geofence>();
	
	public Place(String url) {
		System.out.println(url);
		new GetJson().execute(url);
	}
	
	private class GetJson extends AsyncTask<String, Void, JSONArray> {
		
		protected JSONArray doInBackground(String... urls) {	
			InputStream is = null;
			try {
				URL url = new URL(urls[0]);
				URLConnection connection = url.openConnection();
				is = new BufferedInputStream(connection.getInputStream());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			String json = "";
			
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				json = sb.toString();
				System.out.println(json);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			JSONArray object = null;
			try {
				object = new JSONArray(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return object;
		}
		protected void onPostExecute(JSONArray json){
			
			length = json.length();
			try{
			for(int k=0;k<length;k++){
				lat[k] = json.getJSONObject(k).getDouble("latitude");
				longi[k] = json.getJSONObject(k).getDouble("longitude");
				name[k] = json.getJSONObject(k).getString("name");
				}
			}
			catch (JSONException e1){
				e1.printStackTrace();
			}
			for(int k=0;k<length;k++){
			Geofence fence = new Geofence.Builder()
			.setCircularRegion(lat[k], longi[k], 50)
			.setExpirationDuration(-1)
			.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
			.setRequestId(name[k]).build();
			fences.add(fence);
			}
			
			System.out.println("Added " + fences.size() + " geofences");
		}
		handler dude  = new handler();

	}
	public class handler extends FragmentActivity implements
				ConnectionCallbacks,
				OnConnectionFailedListener,
				OnAddGeofencesResultListener{
		private LocationClient client;
		public handler (){
			System.out.println("did we get this far");
			client = new LocationClient(this,this,this);
			populateGeofences();
		}
		
		public void populateGeofences(){
			for(int k=0;k<length;k++){
				Intent intent = new Intent(this,NotifyMessage.class);
				intent.putExtra("name", name[k]);
				PendingIntent pIntent = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_ONE_SHOT);
				List<Geofence> single = (List<Geofence>) fences.get(k); 
				client.addGeofences(single, pIntent, this);
			}
		}
		public void removeGeofences(){
			ArrayList<String> names = new ArrayList();
			for(int k=0;k<length;k++)
				names.add(name[k]);
			client.removeGeofences(names,(OnRemoveGeofencesResultListener) client);
		}

		@Override
		public void onAddGeofencesResult(int arg0, String[] arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onConnectionFailed(ConnectionResult arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onConnected(Bundle arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDisconnected() {
			// TODO Auto-generated method stub
			
		}
		
	}
}
