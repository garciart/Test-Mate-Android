package com.whystudyalone.tmandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class aboutForm extends Activity
{
	// Declare the components here since all functions in this class will need access...
	TextView IDC_ABOUT_LABEL_PRODUCTNAME;
	TextView IDC_ABOUT_LABEL_COPYRIGHT;
	TextView IDC_ABOUT_LINKLABEL;
	TextView IDC_ABOUT_LABEL_DESCRIPTION;
	TextView IDC_ABOUT_LABEL_WARNING;
	
	MenuItem MENU_CLOSE;
	MenuItem MENU_DEBUG;
	MenuItem MENU_SETUP;
	
    @Override
	public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	try
		{
	    	this.setTheme(android.R.style.Theme_Light);
    		setContentView(R.layout.aboutform);
	        IDC_ABOUT_LABEL_PRODUCTNAME = (TextView) this.findViewById(R.id.IDC_ABOUT_LABEL_PRODUCTNAME);
	    	IDC_ABOUT_LABEL_COPYRIGHT = (TextView) this.findViewById(R.id.IDC_ABOUT_LABEL_COPYRIGHT);
	    	IDC_ABOUT_LINKLABEL = (TextView) this.findViewById(R.id.IDC_ABOUT_LINKLABEL);
	    	IDC_ABOUT_LABEL_DESCRIPTION = (TextView) this.findViewById(R.id.IDC_ABOUT_LABEL_DESCRIPTION);
	    	IDC_ABOUT_LABEL_WARNING = (TextView) this.findViewById(R.id.IDC_ABOUT_LABEL_WARNING);
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".onCreate().");
		}
    }
	
    @Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
    	try
    	{
			MENU_CLOSE = menu.add("Close");
			if(TMAndroid.g_bDebugFlag == true)
			{
				MENU_DEBUG = menu.add("Turn Debug Off");
			}
			else if(TMAndroid.g_bDebugFlag == false)
			{
				MENU_DEBUG = menu.add("Turn Debug On");
			}
			MENU_SETUP = menu.add("Setup");
			return true;
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".onCreateOptionsMenu().");
			return false;
		}
	}
	
    @Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
    	try
    	{
			if(item == MENU_CLOSE)
			{
				finish();
				return true;
			}
			else if(item == MENU_DEBUG)
			{
				if(TMAndroid.g_bDebugFlag == true)
				{
					TMAndroid.g_bDebugFlag = false;
					MENU_DEBUG.setTitle("Turn Debug On");
				}
				else
				{
					TMAndroid.g_bDebugFlag = true;
					MENU_DEBUG.setTitle("Turn Debug Off");
				}
				finish();
				return true;
			}
			else if(item == MENU_SETUP)
			{
				/*
				Intent i = new Intent(this, setup.class);
				startActivity(i);
				*/
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".onOptionsItemSelected().");
			return false;
		}
	}
    
    // Device Dependent Method //
    public void handleError(String l_strExceptionString, String l_strExceptionLocation)
    {
    	if(TMAndroid.g_bDebugFlag == true)
		{
    		final Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("Test Mate");
    		builder.setIcon(R.drawable.tmicon48);
    		builder.setMessage("Houston, we've had a problem. We've had a(n) " + l_strExceptionString + " at " + l_strExceptionLocation);
    		builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener()
    	    {
    			@Override
    			public void onClick(DialogInterface dialog, int which)
    	        {
    				finish();
   		        }
            });
    		builder.show();
		}
		else
		{
    		final Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("Test Mate");
    		builder.setIcon(R.drawable.tmicon48);
    		builder.setMessage("We're sorry, but we've encountered a problem!");
    		builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener()
    	    {
    			@Override
    			public void onClick(DialogInterface dialog, int which)
    	        {
    		    	builder.setTitle("Test Mate");
    		    	builder.setIcon(R.drawable.tmicon48);
    		    	builder.setMessage("Please contact us at tmdevelopment@whystudyalone.com as soon as possible.");
    		    	builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener()
    			    {
    					@Override
    					public void onClick(DialogInterface dialog, int which)
    			        {
    						finish();
    			        }
    		        });
    		    	builder.show();
    	        }
            });
    		builder.show();
		}
    }
}
