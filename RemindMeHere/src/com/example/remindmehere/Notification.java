package com.example.remindmehere;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Vibrator;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.view.View;

public class Notification extends Activity {
	NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
 public Vibrator vibrator;
  public void startVibrate(String note) {
  long pattern[] = { 0, 100, 200, 300, 400 };
  vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
  vibrator.vibrate(pattern, 0);
  NotificationCompat.Builder mBuilder =
		    new NotificationCompat.Builder(this)
		    .setSmallIcon(R.drawable.ic_launcher)
		    .setContentTitle("My notification")
		    .setContentText("Hello World!");
  manager.notify(1,mBuilder.build());
 }




}