package com.example.not3r;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(
						intent.getStringExtra("com.example.not3r.Reminder"))
				.setTicker("Things to do!").setAutoCancel(true);

		Intent newIntent = new Intent(context, Not3rList.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(contentIntent);

		notificationManager.notify(0, mBuilder.build());

		Toast.makeText(context, "Reminder added", Toast.LENGTH_LONG).show();
	}

}
