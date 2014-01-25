package com.luperapp.Luper.Global;



import android.app.Application;

public class AppInstance extends Application {

	
	  private String myState;
	
	  
	
	  public String getState(){
	    return myState;
	  }
	  public void setState(String s){
	    myState = s;
	  }
	  
	 // private 
}
