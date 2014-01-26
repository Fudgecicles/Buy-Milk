package com.example.remindmehere;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

import android.os.AsyncTask;

public class LoginController extends AsyncTask<String, Void, Void>{

	@Override
	protected Void doInBackground(String... arg0) {
		
		Session session = Session.getActiveSession();
		Request request = Request.newGraphPathRequest(session, "me", null);
		Response response = Request.executeAndWait(request);
		String userId = "";
		try {
			JSONObject json = response.getGraphObject().getInnerJSONObject();
			System.out.println(json);
			userId = json.getString("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		String url_str = "http://remindmehere.cloudapp.net/android/auth/callback?facebook&token=" + arg0[0] + "&id=" + userId;
		System.out.println(url_str);
		URL url;
		try {
			url = new URL(url_str);
			URLConnection connection = url.openConnection();
			connection.connect();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	

}
