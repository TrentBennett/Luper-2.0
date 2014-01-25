package com.luperapp.Luper;
/*
 * Displays the lup details from the contact selected
 */
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.luperapp.Luper.Global.Constant;
import com.luperapp.Luper.Global.Globals;
import com.luperapp.model.AlarmReceiver;
import com.luperapp.Luper.R;
import com.luperapp.Luper.R.id;
import com.luperapp.Luper.R.layout;
import com.luperapp.model.AlarmItem;
import com.luperapp.widget.MyDatePickerDialog;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ContactDetailsActivity extends Activity implements OnClickListener {

	private static final int CONTACT_PICKER_RESULT = 1001;
	private static final int FLAG_NEXT_APPOINT = Constant.Profile.FLAG_NEXT_APPOINT;
	private static final int FLAG_LAST_APPOINT = Constant.Profile.FLAG_LAST_APPOINT;
	
	private AlarmItem me;
	private QuickContactBadge mContactBadge;
	private String contactID, email="", phone="", name="";
	private int period;
	private ArrayList<AlarmItem> arrReminders;
	
	private Button mSaveButton;
	private ToggleButton mPhoneToggle, mEmailToggle, mTextToggle;
	protected TextView mButtonLastContactDate;
	protected TextView mButtonNextContactDate;
	private Button mButtonNotes;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_detials);
		
		//Set Views
		LinearLayout lyBar = (LinearLayout) findViewById(R.id.LinearLayout_Bar);
		LinearLayout lyContactInfo = (LinearLayout) findViewById(R.id.LinearLayout_Contact_Info);
		mContactBadge = (QuickContactBadge) findViewById(R.id.contactBadge);
		TextView tvCategoryName = (TextView) findViewById(R.id.txt_category_name);
		Button backButton = (Button) findViewById(R.id.btn_back);
		mSaveButton = (Button) findViewById(R.id.save_button);
		
		period = getIntent().getExtras().getInt("period");
		loadArray(period);
		
		//Set ContactBadge and Contact Details
		email = getIntent().getExtras().getString("contact.email");
		phone = getIntent().getExtras().getString("contact.phone");
		name = getIntent().getExtras().getString("contact.name");
		
		((TextView)findViewById(R.id.emailTV)).setText(email);
		((TextView)findViewById(R.id.phoneTV)).setText(phone);
		((TextView)findViewById(R.id.nameTV)).setText(name);
		
		mPhoneToggle = (ToggleButton) findViewById(R.id.phone_toggle);
		mTextToggle = (ToggleButton) findViewById(R.id.text_toggle);
		mEmailToggle = (ToggleButton) findViewById(R.id.email_toggle);
		
		mButtonNotes = (Button) findViewById(R.id.notes_btn);
		mButtonNotes.setOnClickListener(this);
		
		mButtonLastContactDate = (Button) findViewById(R.id.text_last_contact);
		mButtonNextContactDate = (Button) findViewById(R.id.text_next_contact);

		if(phone==null)
			mContactBadge.assignContactFromPhone(phone, false);
		else
			mContactBadge.assignContactFromEmail(email, false);
		mContactBadge.setMode(ContactsContract.QuickContact.MODE_SMALL);
		mContactBadge.setImageResource(R.drawable.generic_profile_pic);
		
		
		int bgColor = Globals.getColorFromPeriod(getResources(), period);
		lyBar.setBackgroundColor(bgColor);
		lyContactInfo.setBackgroundColor(bgColor-0x99000000);
		tvCategoryName.setText(Globals.getStringFromPeriod(getResources(), period));
		
		backButton.setOnClickListener(this);
		mSaveButton.setOnClickListener(this);
		mButtonLastContactDate.setOnClickListener(this);
		mButtonNextContactDate.setOnClickListener(this);
		
		loadContactInfo();
		
		boolean isAction = getIntent().getExtras().getBoolean("isAction");
		if(isAction)
			onAction();
		
	}

	private void loadContactInfo() {
		int index = -1;
 		me=null;
 		if(arrReminders == null)
 			arrReminders = new ArrayList<AlarmItem>();
		for (int i= 0; i<arrReminders.size(); i++) //loop to find exist object
 		{
 			if (arrReminders.get(i).getName().equals(name) == true)
 			{
 				me = arrReminders.get(i);
 				index = i;
 			}
 		}
 		if (me != null && index>=0)  //me is exist object
 		{
 			mPhoneToggle.setChecked(me.isPhoneEnable());
 			mTextToggle.setChecked(me.isTextEnable());
 			mEmailToggle.setChecked(me.isEmailEnable());
 			mButtonLastContactDate.setText(me.getLastContactTime());
 			mButtonNextContactDate.setText(me.getNextContactTime());			
 		}
 		else //me is new object
 		{
 			me = new AlarmItem(this.name, phone, email, period);
 			arrReminders.add(me);
 		}
	}
	
	public void saveContactInfo()
	{
		me.setPhoneEnable(mPhoneToggle.isChecked());
		me.setTextEnable(mTextToggle.isChecked());
		me.setEmailEnable(mEmailToggle.isChecked());
		
		me.setLastContactTime((String) mButtonLastContactDate.getText());
		me.setNextContactTime((String) mButtonNextContactDate.getText());
		
		scheduleReminder();
		saveArray();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_detials, menu);
		return true;
	}
	
	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
	        if (resultCode == RESULT_OK) { 
	        	
	            switch (requestCode) 
	            { 
		            case CONTACT_PICKER_RESULT: 
		            	
		                Uri contactUri = data.getData();                   
		                Cursor cursorID = getContentResolver().query(contactUri, new  String[]{ContactsContract.Contacts._ID}, null, null, null);
			                if (cursorID.moveToFirst()) {
			                          contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
			                }                 
		                InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
		                 ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));
		               
		                BufferedInputStream buf =new BufferedInputStream(input);
		                Bitmap my_btmp = BitmapFactory.decodeStream(buf);
		                
		                if(my_btmp != null)
		                	mContactBadge.setImageBitmap(my_btmp);
		                else
		                	mContactBadge.setImageResource(R.drawable.generic_profile_pic); 
		                mContactBadge.assignContactUri(contactUri); 
		                mContactBadge.setMode(ContactsContract.QuickContact.MODE_LARGE);  
	            } 
	        } 
	    }

	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
			case R.id.btn_back:
				try
				{
					saveContactInfo();
					finish();
					
				}catch(Exception e){}
				break;
			
			case R.id.notes_btn:
				Intent noteIntent = new Intent(this, NotesActivity.class);
				noteIntent.putExtra("period", period);
				noteIntent.putExtra("id", me.getAlarmId());
				startActivity(noteIntent);
				break;
			case R.id.save_button:
				saveContactInfo();
				setResult(1);
				finish();
				break;
				
			case R.id.text_last_contact:				
				showDatePicker(mButtonLastContactDate.getText().toString(),FLAG_LAST_APPOINT);				
				break;
				
			case R.id.text_next_contact:
				showDatePicker(mButtonNextContactDate.getText().toString(),FLAG_NEXT_APPOINT);
				break;
				
		}

	}
	
	private void showDatePicker(String originalDateStr,final int flag){
		Date originalDate = null;
		try {
			originalDate = new SimpleDateFormat(Constant.Profile.DATE_PATTERN).parse(originalDateStr);
		} catch (ParseException e) {
		
			e.printStackTrace();
		}
		if(originalDate == null){
			originalDate = new Date();
		}
		
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(originalDate);
		
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day =calendar.get(Calendar.DATE);
		
		MyDatePickerDialog dialog = new MyDatePickerDialog(ContactDetailsActivity.this,new MyDatePickerDialog.OnDateSetListener(){

			@Override
			public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
				Date today = new Date();
				Date date;
				Calendar calendar = GregorianCalendar.getInstance();
				calendar.set(Calendar.YEAR, arg1);
				calendar.set(Calendar.MONTH, arg2);
				calendar.set(Calendar.DATE, arg3);
				date = calendar.getTime();
				
				
				String str = DateFormat.format(Constant.Profile.DATE_PATTERN, date).toString();
				if(flag == FLAG_LAST_APPOINT){
					mButtonLastContactDate.setText(str);
					int period_days = Constant.PERIOD_DAY[period];
					
					String dateTime = str + Constant.Profile.DATE_AND_TIME_SEPARATOR + mButtonLastContactDate.getText().toString();
					saveToPreferences(Constant.SharePreferences.KEY_FIXED_LAST_APPOINT, dateTime);
					
					
					calendar.setTime(today);
					calendar.add(Calendar.DAY_OF_YEAR, period_days);
					date = calendar.getTime();
					String strNextDate = DateFormat.format(Constant.Profile.DATE_PATTERN, date).toString();
					mButtonNextContactDate.setText(strNextDate);
					
					
				}
				if(flag == FLAG_NEXT_APPOINT){
					mButtonNextContactDate.setText(str);
					
					String dateTime = str + Constant.Profile.DATE_AND_TIME_SEPARATOR + mButtonNextContactDate.getText().toString();
					
					
					saveToPreferences(Constant.SharePreferences.KEY_FIXED_NEXT_APPOINT, dateTime);
				}
				
				
			}},year
			,month
			,day);
		Date today = new Date();
		
		if(flag == FLAG_LAST_APPOINT){
			
			dialog.setMaxDate(today);
			dialog.setTitle(R.string.last_contact_date);
		}
		if(flag == FLAG_NEXT_APPOINT){
			
			dialog.setMinDate(today);
			dialog.setTitle(R.string.next_contact_date);
		}
		
		dialog.show();
		
	}
	private void saveToPreferences(String paramString1, String paramString2) {
		SharedPreferences.Editor localEditor = getSharedPreferences(
				Constant.SharePreferences.KEY_SAVE, 0).edit();
		localEditor.putString(paramString1, paramString2);
		 
		localEditor.commit();
	}
	
	public void scheduleReminder()
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.Profile.DATE_PATTERN);
		try {
			Date date_new = sdf.parse(this.mButtonNextContactDate.getText().toString());
			Calendar cal_new =  Calendar.getInstance();;
			cal_new.setTime(date_new);
			
			
			cal.set(Calendar.YEAR, cal_new.get(Calendar.YEAR));
			cal.set(Calendar.MONTH, cal_new.get(Calendar.MONTH));
			cal.set(Calendar.DATE, cal_new.get(Calendar.DATE));
			cal.add(Calendar.SECOND, 5);
			
		} catch (ParseException e) {
			
			e.printStackTrace();
			return;
		}
		
		
		
		Intent intent = new Intent(this, AlarmReceiver.class);
		intent.putExtra("alarm message", "Don't forget to reach out to "+ name);
		intent.putExtra("period", period);
		intent.putExtra("contact.name", name);
		intent.putExtra("contact.phone", phone);
		intent.putExtra("contact.email", email); //Alarm Receiver//
		
		Log.v("Test", ""+me.getAlarmId());
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, me.getAlarmId(), intent, Intent.FLAG_ACTIVITY_NEW_TASK);
		
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		
		alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

	}
	
	
	public void onAction()
	{
		mContactBadge.onClick(mContactBadge);
		
		me.setLastContactTime(me.getNextContactTime());
		
		Date date;
		Calendar calendar = new GregorianCalendar();//GregorianCalendar.getInstance();
		
		calendar.add(Calendar.DAY_OF_YEAR, Constant.PERIOD_DAY[period]);
		
		date = calendar.getTime();
		
		String strNextDate = DateFormat.format(Constant.Profile.DATE_PATTERN, date).toString();
		
		me.setNextContactTime(strNextDate);
		
		mButtonLastContactDate.setText(me.getLastContactTime());
		mButtonNextContactDate.setText(me.getNextContactTime());
		
	}
	
	public boolean loadArray(int period){
		
		
		try{
		
			FileInputStream fis = openFileInput("Save_" + Integer.toString(period));
			ObjectInputStream is = new ObjectInputStream(fis);
			arrReminders = (ArrayList<AlarmItem>) is.readObject();
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
	
	public boolean saveArray(){
		
		try{

			FileOutputStream fos = openFileOutput("Save_" + Integer.toString(period), Context.MODE_PRIVATE);
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
