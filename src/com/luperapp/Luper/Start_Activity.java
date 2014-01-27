package com.luperapp.Luper;

/*
 * This Activity is the first to launch when the app is opened.
 * 
 * Its purpose is to display a splash screen before launching MainActivity.java
 */


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;

import com.luperapp.Luper.Global.Globals;
import com.luperapp.model.AlarmReceiver;
import com.luperapp.model.ContactData;
import com.luperapp.Luper.R;
import com.luperapp.Luper.R.id;
import com.luperapp.Luper.R.layout;
import com.luperapp.model.AlarmItem;



public class Start_Activity extends Activity {
	
	
	private final int SPLASH_DISPLAY_LENGHT = 2000;


	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		
	        /* New Handler to start the Menu-Activity 
	         * and close this Splash-Screen after some seconds.*/
	        new Handler().postDelayed(new Runnable()
	        {
	            @Override
	            public void run() 
	            {
	                /* Create an Intent that will start the Menu-Activity. */
	                Intent mainIntent = new Intent(Start_Activity.this,MainActivity.class);
	                Start_Activity.this.startActivity(mainIntent);
	                Start_Activity.this.finish();
	            }
	        }, SPLASH_DISPLAY_LENGHT);
	    }
		
	

}	

