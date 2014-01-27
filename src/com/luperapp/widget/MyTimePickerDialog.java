package com.luperapp.widget;

import java.util.Calendar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class MyTimePickerDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

	private Button mButton;
	
	/*
	 * @param Button buttonClicked - The text of this button will be set to the time selected in the format 12:00 pm 
	 */
	public void setArguments(Button buttonClicked)
	{
		
		mButton = buttonClicked;

	}
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker picker, int hourOfDay, int minute) {
    	
        if(mButton != null)
        {
        	String am_pm = "am";
        	if(hourOfDay > 12)
        	{
        		hourOfDay -= 12;
        		am_pm = "pm";
        	}
        	if(hourOfDay == 12)
        	{
        		am_pm = "pm";
        	}
        	if(hourOfDay == 0)
        	{
        		hourOfDay = 12;
        	}
        	mButton.setText(""+hourOfDay+":"+minute+" "+am_pm);
        }
        	
    }
}
