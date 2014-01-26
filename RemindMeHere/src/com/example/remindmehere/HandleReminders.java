package com.example.remindmehere;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HandleReminders extends Activity {

	  private Double latituteField;
	  private Double longitudeField;
	  private LocationManager locationManager;
	  private String provider;
	
	ArrayList <Reminder> arrList = new ArrayList<Reminder>();
    LinearLayout realListLayout; 
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.handle_reminders);
		
		final LinearLayout listLayout = (LinearLayout)findViewById(R.id.list_layout);
		realListLayout = (LinearLayout) findViewById(R.id.real_list_layout);

		Button addBtn = (Button) findViewById(R.id.add_button);
	    final EditText textInput = (EditText) findViewById(R.id.reminder_input);

	    //Call populate. 
	    new PopulateReminders().execute();
			
	addBtn.setOnClickListener(new View.OnClickListener() {	
		@Override
		   public void onClick(View v) 
		   {
				// hide keyboard
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(listLayout.getWindowToken(), 0);		    
			    
//			    LinearLayout tempLayout = createLayout();
//			    arrList.add(new Reminder(textInput.getText().toString(), false, -1, arrList.size())); //buttonId = size
//			    
//			    tempLayout.addView(createTextView(textInput.getText().toString()));
//			    tempLayout.addView(createDltButton());
//			    realListLayout.addView(tempLayout);
//			    
//				addReminder(textInput.getText().toString());
				new AddReminder().execute(textInput.getText().toString());
				
				textInput.setText("");
		   }
		});
	
	}
	@SuppressLint("NewApi")
	private TextView createTextView(String text) {
	    TextView textView = new TextView(this);
	    textView.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.2f));
	    textView.setText(text);
	    textView.setTextSize(25);
	    return textView;
	}
	@SuppressLint("NewApi")
	private Button createDltButton(final int num) {
	    Button button = new Button(this);
	    button.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.8f));
	    button.setText("Dlt");	 
	    button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DeleteReminder().execute(num);
			}
		});
	    return button;
	}
	    
	   
	@SuppressLint("NewApi")
	private LinearLayout createLayout() {
	    LinearLayout layout = new LinearLayout(this);
	    layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	    layout.setOrientation(LinearLayout.HORIZONTAL);
	    layout.setWeightSum((float) 1.0);
	    return layout;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	  public void onLocationChanged(Location location) {
		    Double lat = location.getLatitude();
		    Double lng = location.getLongitude();
		    latituteField = lat;
		    longitudeField = lng;
		    Toast.makeText(this, "Latitude: " + latituteField + " - Longitude: " + longitudeField,
			        Toast.LENGTH_SHORT).show();
		    
		    
		  }
	
	// Do add to view stuff in here. 
	public void outputReminders(ArrayList<Reminder> reminders) {
		realListLayout.removeAllViews();
		// Create text view based off string. 
		for(Reminder r: reminders)
		{
			LinearLayout tempLayout = createLayout();
			tempLayout.addView(createTextView(r.getText()));
			tempLayout.addView(createDltButton(r.getId()));
			realListLayout.addView(tempLayout);
		}
		// Add a button onclick to delete based off id number // ask
	}
	
	public ArrayList<Reminder> getReminders(JSONArray object) {
		ArrayList<Reminder> reminders  = new ArrayList<Reminder>();
		if (object == null) return reminders;
		for(int k=0;k<object.length();k++){
			try {
				reminders.add(new Reminder(object.getJSONObject(k).getString("name"),object.getJSONObject(k).getInt("id")));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return reminders;
	}

	private class PopulateReminders extends AsyncTask<Void, Void, ArrayList<Reminder>> {
		protected ArrayList<Reminder> doInBackground(Void... urls) {	
			InputStream is = null;
			try {
				String url_str = "http://remindmehere.cloudapp.net/reminders";
				URL url = new URL(url_str);
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
			
			return getReminders(object);
		}

		protected void onPostExecute(ArrayList<Reminder> reminders) {
			// Call the print to screen function. 
			outputReminders(reminders);
		}
	
	}
	
	private class AddReminder extends AsyncTask<String, Void, ArrayList<Reminder>> {
		protected ArrayList<Reminder> doInBackground(String ... tasks) {	
		
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://remindmehere.cloudapp.net/reminders.json");
			
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("reminder[name]", tasks[0]));
			try {
				post.setEntity(new UrlEncodedFormEntity(pairs));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
				
			String json = "";
			
			try {
				HttpResponse response = client.execute(post);
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				json = sb.toString();
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
			
			return getReminders(object);
		}

		protected void onPostExecute(ArrayList<Reminder> reminders) {
			// Call the print to screen function. 
			outputReminders(reminders);
			
			// Get the location manager
		    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		    // Define the criteria how to select the locatioin provider -> use
		    // default
		    Criteria criteria = new Criteria();
		    provider = locationManager.getBestProvider(criteria, false);
		    Location location = locationManager.getLastKnownLocation(provider);

		    // Initialize the location fields
		    if (location != null) {
		      System.out.println("Provider " + provider + " has been selected.");
		      onLocationChanged(location);
		      
		      String url = "http://remindmehere.cloudapp.net/current_location?lat=" + location.getLatitude() 
			    		+ "&long=" + location.getLongitude();
			    new Place(url);
			    
		    } else {
		      latituteField  = -1.0;
		      longitudeField = -1.0;
		    }
		    
		    
		    
		}
	
	}
	
	private class DeleteReminder extends AsyncTask<Integer, Void, ArrayList<Reminder>> {
		protected ArrayList<Reminder> doInBackground(Integer ... ids) {	
			InputStream is = null;
			try {
				String url_str = "http://remindmehere.cloudapp.net/reminders/" + ids[0] + ".json";
				System.out.println("Delete URL: " + url_str);
				URL url = new URL(url_str);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setRequestMethod("DELETE");
				connection.connect();
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
			
			return getReminders(object);
		}

		protected void onPostExecute(ArrayList<Reminder> reminders) {
			// Call the print to screen function. 
			outputReminders(reminders);
		}
	
	}
}
