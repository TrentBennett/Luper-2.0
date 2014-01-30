package com.luperapp.Luper;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import com.luperapp.Luper.Global.Globals;
import com.luperapp.model.AlarmReceiver;
import com.luperapp.Luper.R;
import com.luperapp.Luper.R.id;
import com.luperapp.Luper.R.layout;
import com.luperapp.model.AlarmItem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements OnClickListener{

	private ArrayList<AlarmItem> arrReminders;
	private ArrayList<Button> buttonList;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		buttonList = new ArrayList<Button>();
		
		View appNameButton = findViewById(R.id.BtnAppName);
		appNameButton.setOnClickListener(this);
		
		View weeklyButton = findViewById(R.id.BtnWeekly);
		weeklyButton.setOnClickListener(this);
		buttonList.add((Button) weeklyButton);
		
		View monthlyButton = findViewById(R.id.BtnMonthly);
		monthlyButton.setOnClickListener(this);
		buttonList.add((Button) monthlyButton);
		
		View quarterlyButton = findViewById(R.id.BtnQuarterly);
		quarterlyButton.setOnClickListener(this);
		buttonList.add((Button) quarterlyButton);
		
		View halfYearButton = findViewById(R.id.BtnHalfYear);
		halfYearButton.setOnClickListener(this);
		buttonList.add((Button) halfYearButton);
		
		View yearlyButton = findViewById(R.id.BtnYearly);
		yearlyButton.setOnClickListener(this);		
		buttonList.add((Button) yearlyButton);
		
		View customButton = findViewById(R.id.BtnCustom);
		customButton.setOnClickListener(this);		
		buttonList.add((Button) customButton);
		
		showAlarmNumbers();
		
		//Add Animation
		if(Build.VERSION.SDK_INT >= 12)
		{
			int width = getWindowManager().getDefaultDisplay().getWidth();
			
			if(Build.VERSION.SDK_INT >= 14)
			{
				int totalDuration = 800;
				int durSlice = totalDuration/buttonList.size();
				
				int i = 0;
				for(View x : buttonList)
				{
					x.animate().setStartDelay(durSlice*i);
					i++;
				}
			}
				
			for(View x : buttonList)
			{
				x.setTranslationX(-width);
				x.setAlpha(0);
				x.setRotationY(-85);
				
				x.animate().setDuration(500).translationXBy(width).alpha(1).rotationY(0);
			}

		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		showAlarmNumbers();
	}

	@Override
	public void onClick(View v) {
		
		if (v.getId() == R.id.BtnAppName){
			try{
				Intent k = new Intent(MainActivity.this, AboutActivity.class);
				startActivity(k);
			}catch(Exception e){
				
			}
		} 
		else{
			
			Button b = (Button)v;
			//try{
				Intent k = new Intent(MainActivity.this, CategoryActivity.class);
				Integer i = Integer.parseInt(b.getTag().toString());
				k.putExtra("period",i);

				startActivity(k);
			//}catch(Exception e){
			//	Log.d("aaa", "aaa");
			//}
		}
		
	
	}
	
	public void showAlarmNumbers()
	{
		int p=0;
		for(Button x : buttonList)
		{
			resetButton(x);
			if(loadArray(p))
			{
				if(arrReminders.size() > 0)
					x.setText(x.getText().toString().replace("--", "("+arrReminders.size()+")"));
			}
			p++;
		}
	}
	
	private void resetButton(Button x) {
		
		switch(Integer.parseInt(x.getTag().toString()))
		{
		case 0:
			x.setText(getResources().getString(R.string.weekly));
			break;
		case 1:
			x.setText(getResources().getString(R.string.monthly));
			break;
		case 2:
			x.setText(getResources().getString(R.string.quarterly));
			break;
		case 3:
			x.setText(getResources().getString(R.string.halfyear));
			break;
		case 4:
			x.setText(getResources().getString(R.string.yearly));
			break;
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

}
