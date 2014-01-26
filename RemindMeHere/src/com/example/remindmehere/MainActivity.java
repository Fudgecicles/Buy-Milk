package com.example.remindmehere;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	  private Double latituteField;
	  private Double longitudeField;
	  private LocationManager locationManager;
	  private String provider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button createBtn = (Button) findViewById(R.id.create_button);
		Button locationBtn = (Button) findViewById(R.id.location_button);
		
	

	createBtn.setOnClickListener(new View.OnClickListener() {	
		@Override
		   public void onClick(View v) 
		   {
			Intent intent = new Intent(MainActivity.this, HandleReminders.class);
			startActivity(intent); //switch activities
		   }
		});
	
	locationBtn.setOnClickListener(new View.OnClickListener() {		
		@Override
		public void onClick(View v) {
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
		    } else {
		      latituteField  = -1.0;
		      longitudeField = -1.0;
		    }
		  }
	});
	
	}
	  public void onLocationChanged(Location location) {
	    Double lat = location.getLatitude();
	    Double lng = location.getLongitude();
	    latituteField = lat;
	    longitudeField = lng;
	    Toast.makeText(this, "Latitude: " + latituteField + " - Longitude: " + longitudeField,
		        Toast.LENGTH_SHORT).show();
	  }

	  public void onStatusChanged(String provider, int status, Bundle extras) {
	    // TODO Auto-generated method stub

	  }

	  public void onProviderEnabled(String provider) {
	    Toast.makeText(this, "Enabled new provider " + provider,
	        Toast.LENGTH_SHORT).show();

	  }

	  public void onProviderDisabled(String provider) {
	    Toast.makeText(this, "Disabled provider " + provider,
	        Toast.LENGTH_SHORT).show();
	  }
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
