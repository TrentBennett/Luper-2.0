package com.luperapp.model;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.text.format.DateFormat;


import android.util.Log;

import com.luperapp.Luper.ContactDetailsActivity;
import com.luperapp.Luper.Global.Constant;
import com.luperapp.Luper.R;



public class AlarmReceiver extends BroadcastReceiver {


	private int period;
	private String name;
	private String phone;
	private String email;
	private String message;
	private String last_contact_date;
	private String next_contact_date;
	
	private ArrayList<AlarmItem> arrReminders = new ArrayList<AlarmItem>();
	

	
	@Override
	public void onReceive(Context context, Intent intent){
		
		try{
			Bundle bundle = intent.getExtras();
			message = bundle.getString("alarm message");
			period = bundle.getInt("period");
			name = bundle.getString("contact.name");
			phone = bundle.getString("contact.phone");
			email = bundle.getString("contact.email");

			
			NotificationManager notifier = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			
			Notification notify = new Notification(R.drawable.ic_launcher, message, System.currentTimeMillis());
			this.scheduleReminder(context);
			
			Intent intent2 = new Intent(context, ContactDetailsActivity.class);
			intent2.putExtra("period", period );
			intent2.putExtra("contact.name", name);
			intent2.putExtra("contact.phone", phone);
			intent2.putExtra("contact.email", email);
			intent2.putExtra("last_contact_date", last_contact_date);
			intent2.putExtra("next_contact_date", next_contact_date);
			intent2.putExtra("isAction", true);
			
			this.loadArray(period, context);
			for (AlarmItem curItem : arrReminders)
			{
				if (curItem.getName().equals(name))
				{
					curItem.setLastContactTime(last_contact_date);
					curItem.setNextContactTime(next_contact_date);
					curItem.setEmail("MyEMAIL@Fuckoff.com");
					this.saveArray(context);
					
				}
			}
			//intent2.putExtra("last_contact_date", last_contact_date);
			//intent2.putExtra("next_contact_date", next_contact_date);
			//Toast.makeText(context, "next:"+next_contact_date, Toast.LENGTH_SHORT).show();
		
			PendingIntent pender = PendingIntent.getActivity(context, 0, intent2, Intent.FLAG_ACTIVITY_NEW_TASK);
			
			notify.setLatestEventInfo(context, "Luper", message, pender);
			
				
			notify.flags |= Notification.FLAG_SHOW_LIGHTS;	
			notify.ledARGB = 0xff9D00FF;
			notify.ledOnMS = 500;
			notify.ledOffMS = 1500;
			notify.flags |= Notification.FLAG_AUTO_CANCEL;
			notify.vibrate = new long[]{0,250,200,600};
			notify.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			notifier.notify(1, notify);
			
			
		}catch (Exception e){
			
			e.printStackTrace();
		}
	}
	
	public void scheduleReminder(Context context)
	{
		Calendar cal = Calendar.getInstance();
		switch (period)
		{
		case 0:
			cal.add(Calendar.WEEK_OF_YEAR,1);
			break;
		case 1:
			cal.add(Calendar.MONTH, 1);
			break;
		case 2:
			cal.add(Calendar.MONTH, 3);
			break;
		case 3:
			cal.add(Calendar.MONTH, 6);
			break;
		case 4:
			cal.add(Calendar.YEAR,1);
			break;
			
		}
			
				
		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.putExtra("alarm message", "Don't forget to reach out to "+ name);
		intent.putExtra("period", period);
		intent.putExtra("name", name);
		
		Date today = new Date();
		Date next_date = cal.getTime();
		last_contact_date = DateFormat.format(Constant.Profile.DATE_PATTERN, today).toString();
		next_contact_date = DateFormat.format(Constant.Profile.DATE_PATTERN, next_date).toString();
		
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

		
	}

	public boolean loadArray(int period, Context context){
		
		
		try{
		
			FileInputStream fis = context.openFileInput("Save_" + Integer.toString(period));
			ObjectInputStream is = new ObjectInputStream(fis);
			is.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
		for (int i= 0; i<arrReminders.size(); i++)
		{
			if (arrReminders.get(i).getPeriod() != period)
			{
				arrReminders.remove(i);
				i--;
				continue;
			}
		}
		return true;
		
		
				
	}
	
	public boolean saveArray(Context context){
		
		try{

		
			FileOutputStream fos = context.openFileOutput("Save_" + Integer.toString(period), Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(this.arrReminders);
			os.close();
			
			
			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
