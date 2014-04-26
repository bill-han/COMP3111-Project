package com.example.not3r;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Bitmap btm = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_launcher);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_launcher).setContentTitle(
				"New message");
		mBuilder.setTicker("New message");
		mBuilder.setNumber(12);
		mBuilder.setLargeIcon(btm);
		mBuilder.setAutoCancel(true);
		// mBuilder.setWhen(c.getTimeInMillis());

		Intent newIntent = new Intent(context, Not3rList.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				newIntent, 0);
		mBuilder.setContentIntent(contentIntent);

		notificationManager.notify(0, mBuilder.build());
		
		Toast.makeText(context, "New message", Toast.LENGTH_LONG).show(); 
	}

}
