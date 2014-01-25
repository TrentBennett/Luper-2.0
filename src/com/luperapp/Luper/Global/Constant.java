package com.luperapp.Luper.Global;



public class Constant {

	public static final int WEEK_DAY = 7;
	public static final int MONTH_DAY = 30;
	public static final int QUARTER_DAY = 91;
	public static final int HALFYEAR_DAY = 183;
	public static final int YEAR_DAY = 365;
	
	public static final int ACTION_CALL = 0;
	public static final int ACTION_TEXT = 1;
	public static final int ACTION_EMAIL = 2;
	public static final int ACTION_CANCEL = 3;
	
	public static final String CALL = "Call";
	public static final String EMAIL = "Email";
	public static final String TEXT = "Text";
	public static final String CANCEL = "Cancel";
	
	 public static final int[] PERIOD_DAY;

	    static
	    {
	      int[] arrayOfDay = new int[5];
	      arrayOfDay[0] = WEEK_DAY;
	      arrayOfDay[1] = MONTH_DAY;
	      arrayOfDay[2] = QUARTER_DAY;
	      arrayOfDay[3] = HALFYEAR_DAY;
	      arrayOfDay[4] = YEAR_DAY;
	      PERIOD_DAY = arrayOfDay;
	    }
	    
	  public static final class SharePreferences
	  {
	    public static final String KEY_DUE = "due";
	    public static final String KEY_GOALS = "goals";
	    public static final String KEY_LOCATION = "location";
	    public static final String KEY_NAME_1 = "name_1";
	    public static final String KEY_NAME_2 = "name_2";
	    public static final String KEY_NAME_3 = "name_3";
	    public static final String KEY_NAME_4 = "name_4";
	    public static final String KEY_NEXT_APPOINT = "next_appoint";
	    public static final String KEY_PROFILE_PREFERENCES_NAME = "profile_preferences_name";
	    public static final String KEY_ROLE_1 = "role_1";
	    public static final String KEY_ROLE_2 = "role_2";
	    public static final String KEY_ROLE_3 = "role_3";
	    public static final String KEY_ROLE_4 = "role_4";
	    public static final String KEY_FIXED_NEXT_APPOINT = "fixed_next_appoint";
	    public static final String KEY_SAVE = "key_save";
	    public static final String KEY_FIXED_LAST_APPOINT = "fixed_last_appoint";
	    public static final String KEY_SAVED_REMINDERS = "key_saved_reminders";
	    public static final String KEY_NOTIFY_IS_CHECK = "KEY_NOTIFY_IS_CHECK";
	  }
	  
	  public static final class Profile
	  {
	    public static final int FLAG_DUE = 102;
	    public static final int FLAG_GOALS = 101;
	    public static final int FLAG_NEXT_APPOINT = 100;
	    public static final int FLAG_LAST_APPOINT = 103;
	    public static final String KEY_BUSINESS_NAME = "business_name";
	    public static final String KEY_DUE = "due";
	    public static final String KEY_EMAIL_ADDRESS = "email_address";
	    public static final String KEY_FLAG = "flag";
	    public static final String KEY_GOALS = "goals";
	    public static final String KEY_NAME = "name";
	    public static final String KEY_NEXT_APPOINT = SharePreferences.KEY_NEXT_APPOINT;
	    public static final String KEY_PHONE_NUMBER = "phone_number";
	    public static final String KEY_PROFILE_NAME = SharePreferences.KEY_PROFILE_PREFERENCES_NAME;
	    public static final String KEY_RESULT = "result";
	    public static final int RESULT_SUCCESS = 1;
	    public static final int RESULT_FAIL = -1;
	    public static final String KEY_FIXED_NEXT_APPOINT = SharePreferences.KEY_FIXED_NEXT_APPOINT;
	    public static final String DATE_AND_TIME_SEPARATOR = ",";
	    public static final String DATE_PATTERN = "MM/dd/yyyy";
	    public static final String TIME_PATTERN = "hh:mm aa";
	    public static final String DATE_TIME_PATTERN = "yyyy/MM/dd E,hh:mm aa";

	  }
}
