package com.example.not3r;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class NotificationReceiver extends BroadcastReceiver {

	private static int NOTIFICATION_ID = 0;

	@Override
	public void onReceive(Context context, Intent intent) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Not3r!")
				.setContentText(
						intent.getStringExtra("com.example.not3r.Reminder"))
				.setTicker("Things to do!").setAutoCancel(true);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		Intent newIntent = new Intent(context, Editor.class);
		newIntent.putExtra("com.example.not3r.Note",
				intent.getLongExtra("com.example.not3r.Note", -1));
		newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		stackBuilder.addParentStack(Editor.class);
		stackBuilder.addNextIntent(newIntent);
		PendingIntent contentIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(contentIntent);

		notificationManager.notify(NOTIFICATION_ID++, mBuilder.build());
	}

}
