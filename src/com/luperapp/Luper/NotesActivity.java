package com.luperapp.Luper;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import com.luperapp.model.AlarmItem;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.EditText;

public class NotesActivity extends Activity {

	private int alarmId;
	private int period;
	private ArrayList<String> notes;
	private EditText mNoteEdit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notes);
		
		alarmId = getIntent().getExtras().getInt("id");
		period = getIntent().getExtras().getInt("period");
		
		notes = null;
		loadNotes(period);
		
		mNoteEdit = (EditText) findViewById(R.id.edit_text_note);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notes, menu);
		return true;
	}
	
	
	
	
	public boolean loadNotes(int period){
		
		ArrayList<AlarmItem> arrReminders = null;
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
		
		
		for(AlarmItem x : arrReminders)
		{
			if(x.getAlarmId() == alarmId)
			{
				if(x.getNotes() == null)
				{
					x.setNotes(new ArrayList<String>());
				}
				notes = x.getNotes();
				
				return true;
			}
		}
		return false;
		
		
				
	}

}
