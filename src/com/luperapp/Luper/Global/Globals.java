package com.luperapp.Luper.Global;


import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Color;

import com.luperapp.model.ContactData;
import com.luperapp.Luper.R;

public final class Globals {

	private static final Globals instance = new Globals();
	public ArrayList<ContactData> contactlist = new ArrayList<ContactData>();
	private static boolean thread_flag = false;
	public static Thread thread;
	
	public static Globals getInstance() {
		return instance;
	}
	
	
	public static int getColorFromPeriod(Resources r, int period){
		String[] arrColorCategory = r.getStringArray(R.array.category_color);
		
		int  color = Color.parseColor(arrColorCategory[period]);
		return color;
	}
	public static String getStringFromPeriod(Resources r, int period)
	{
		String[] arrStrCategory = r.getStringArray(R.array.category); //get Category String Array
		return arrStrCategory[period];
	}
	
	public static String getStringFromIndex(Resources r, int which)
	{
		String[] arrStrAction = r.getStringArray(R.array.action_click); //get Category String Array
		return arrStrAction[which];
	}
	
	public static void lock_data()
	{
		thread_flag = true;
	}
	public static void unlock_data()
	{
		thread_flag = false;
	}
	public static boolean isLocked()
	{
		return thread_flag;
	}
}



