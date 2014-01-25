package com.luperapp.Luper;

/*
 * The CategoryActivity shows the user a list of their saved lups in the category chosen. 
 */



import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.luperapp.Luper.Global.Globals;
import com.luperapp.model.AlarmReceiver;
import com.luperapp.Luper.R;
import com.luperapp.Luper.R.id;
import com.luperapp.Luper.R.layout;
import com.luperapp.model.AlarmItem;


public class CategoryActivity extends Activity implements OnClickListener {
	
	private static final int CONTACT_PICKER_RESULT = 1001;
	
	private Button mButton_Add;
	private int period;
	private ArrayList<AlarmItem> arrReminders = null;
	ListView lvItem;

    String displayName="", emailAddress="", phoneNumber="";
    CustomBaseAdapter itemAdapter;
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.categoryactivity);
		View okButton = findViewById(R.id.btn_back);
		okButton.setOnClickListener(this);
		initViews();
		
	}

	protected void onResume(){
		super.onResume();
		
		 if ( this.loadArray(period) == true && arrReminders != null)
		 {
			 lvItem = (ListView)this.findViewById(R.id.lv_alarms);
			  itemAdapter = new CustomBaseAdapter(this, arrReminders );
			  lvItem.setAdapter(itemAdapter);
		 }
	}

	private void initViews(){
		mButton_Add = (Button) findViewById(R.id.btn_add);
		  TextView headTxt = (TextView)findViewById(R.id.btn_category_name);
		  LinearLayout linearBar = (LinearLayout)findViewById(R.id.LinearLayout_Bar);
		  Bundle extras = getIntent().getExtras();
		  if (extras != null){
			  period = extras.getInt("period");

			  headTxt.setText(Globals.getStringFromPeriod(getResources(), period));
			  linearBar.setBackgroundColor(Globals.getColorFromPeriod(getResources(), period));

		  }
		 
		  lvItem = (ListView)this.findViewById(R.id.lv_alarms);
		  if ( this.loadArray(period) == true && arrReminders != null)
		  {
			 
			  itemAdapter = new CustomBaseAdapter(this, arrReminders );
			  lvItem.setAdapter(itemAdapter);
		  }

		  lvItem.setEmptyView(findViewById(R.id.empty));

		  mButton_Add.setOnClickListener(this);
		
		}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.btn_back:
			try{
				CategoryActivity.this.finish();
				
			}catch(Exception e){
				
			}
			break;
		
		case R.id.btn_add:
			launchContactPicker();			
			break;

		}
	}
		
	private void launchContactPicker() {
		
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
		
	    startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);  
		
	}	
	
	/*
	 * (non-Javadoc)
	 * Gets the the contact information from the contact the user chose in the contact picker
	 * 
	 * @param requestCode The code returned from the Activity
	 * @param resultCode The result of the requestCode
	 * @param data Data from the Intent
	 */
	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
	        if (resultCode == RESULT_OK) { 
	        	
	            switch (requestCode) 
	            { 
		            case CONTACT_PICKER_RESULT: 
		            	
		            	String id, name, phone, hasPhone, email;
		            	int idx, colIdx;
		            	
		                Uri contactUri = data.getData();                   
		                Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
			            if (cursor.moveToFirst()) 
			            {			                	
			                idx = cursor.getColumnIndex(ContactsContract.Contacts._ID);
			                id = cursor.getString(idx);

			                idx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			                name = cursor.getString(idx);

			                idx = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
			                hasPhone = cursor.getString(idx);
			                
			           
			                email=null;
			                phone=null;
			                
							// Get phone number - if they have one
			                if ("1".equalsIgnoreCase(hasPhone)) {
			                    cursor = getContentResolver().query(
			                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
			                            null,
			                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "+ id, 
			                            null, null);
			                    if (cursor.moveToFirst()) {
			                        colIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
			                        phone = cursor.getString(colIdx);
			                    }
			                    cursor.close();
			                }

			                // Get email address
			                cursor = getContentResolver().query(
			                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
			                        null,
			                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id,
			                        null, null);
			                if (cursor.moveToFirst()) {
			                    colIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
			                    email = cursor.getString(colIdx);
			                }
			                cursor.close();
			                
		    				//Intent i = new Intent(this,ReminderSettingActivity.class);
			                Intent i = new Intent(this,ContactDetailsActivity.class);
	    					i.putExtra("period", period );
	    					i.putExtra("contact.name", name);
	    					i.putExtra("contact.email", email);
	    					i.putExtra("contact.phone", phone);
	    					i.putExtra("isAction", false);
	    					
	    					startActivity(i);
			                
			            }                 
		               
	            } 
	        } 
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
			Log.v("EXECTION","LOad Failed!!");
			e.printStackTrace();
			return false;
		}
		
		for (int i= 0; i<arrReminders.size(); i++)
		{
			Log.v("Load", ""+arrReminders.get(i).getPeriod());
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
	

	
	public class CustomBaseAdapter extends BaseAdapter{
	     
	    private LayoutInflater inflater = null;
	    private ArrayList<AlarmItem> infoList = null;

	    public CustomBaseAdapter(Context c , ArrayList<AlarmItem> arrays){
	        this.inflater = LayoutInflater.from(c);
	        this.infoList = arrays;
	    }
	 
	  
	    
	    @Override
	    public int getCount() {
	        return infoList.size();
	    }
	 

	    @Override
	    public AlarmItem getItem(int position) {
	        return infoList.get(position);
	    }
	 

	    @Override
	    public long getItemId(int position) {
	        return position;
	    }
	 

	    @Override
	    public View getView(int position, View convertview, ViewGroup parent) {

	        View v = convertview;
	        //v.setBackgroundColor(0xFFFFFFFF);
	        
	        Button btn_button = null;
	        if(v == null){

	            v = inflater.inflate(R.layout.contact_cell, null);

	            
	             
	        }else {
	        	
	  
	        }
	        btn_button = (Button)v.findViewById(R.id.save_button);
	
	        if(!getItem(position).isActivate())
	        	btn_button.setBackgroundColor(0xFF555555);
	        btn_button.setTag(position);
	        btn_button.setText(getItem(position).getName());
	        btn_button.setOnClickListener(buttonClickListener);
	        btn_button.setOnLongClickListener(buttonLongClickListener);
	         
	        return v;
	    }
	     
	   
	    public void setArrayList(ArrayList<AlarmItem> arrays){
	        this.infoList = arrays;
	    }
	     
	    public ArrayList<AlarmItem> getArrayList(){
	        return infoList;
	    }
	     

		
	    private View.OnLongClickListener buttonLongClickListener = new View.OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
				
				
				
				Alert("Are you sure to delete reminder of '"+((Button)v).getText() +"'?", ((Button)v).getText().toString(), (Integer)((Button)v).getTag());
				return true;
				}
	
		};
		
		public void Alert(String text, String title, final int tag)
		{
			
			
			AlertDialog.Builder builder = 	new AlertDialog.Builder(CategoryActivity.this);
			
			builder.setTitle(title);
			builder.setMessage(text);
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					deleteReminder(arrReminders.get(tag).getName(), arrReminders.get(tag).getPeriod(), getApplicationContext());
					arrReminders.remove(tag);
					
					saveArray();
					notifyDataSetChanged();
					
				}
			});
			builder.setNegativeButton("No", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
				
			});
			builder.show();
			
		}
		
		
		
	    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	            switch (v.getId()) {
	             
	         
	            case R.id.save_button:
	            	try{
	            		Log.v("SAVE", "save_button clicked?");
	            		AlarmItem alarm = arrReminders.get((Integer) v.getTag());
	    				//Intent i = new Intent(CategoryActivity.this,ReminderSettingActivity.class);
	            		Intent i = new Intent(CategoryActivity.this,ContactDetailsActivity.class);//NEW
	            		
	    					i.putExtra("period", period );
	    					i.putExtra("contact.name", alarm.getName());
	    					i.putExtra("contact.email", alarm.getEmail());
	    					i.putExtra("contact.phone", alarm.getPhone());
	    					i.putExtra("isAction", false);
	    				startActivityForResult(i, 0);
	    				}catch(Exception e){
	    					Log.d("aa", "error");
	    				}

	                break;
	 
	         
	            default:
	                break;
	            }
	        }
	    };
	     
	
	 
	     
	  
	}
	public void deleteReminder(String name, int period, Context context)
	{
		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.putExtra("alarm message", "Don't forget to reach out to "+ name);
		intent.putExtra("period", period);
		intent.putExtra("name", name);
		
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		
		
		
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
		pendingIntent.cancel();

	}
	
}
