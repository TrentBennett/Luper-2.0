package com.luperapp.Luper;
//
/*
comment *The AboutActivity displays the Luper icon and web address, and an OK button  
 */

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AboutActivity extends Activity implements OnClickListener {
	private Button urlButton;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutactivity);
		View okButton = findViewById(R.id.BtnOK);
		okButton.setOnClickListener(this);
		 urlButton =(Button) findViewById(R.id.TextViewUrl);
		urlButton.setOnClickListener(this);
	//	startActivity(new Intent(this, Start_Activity.class));
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.BtnOK:
			try{
				AboutActivity.this.finish();
				
			}catch(Exception e){
				
			}
			
			break;
		case R.id.TextViewUrl:
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlButton.getText().toString()));
			startActivity(browserIntent);
			
		}
		
		
	}
}
