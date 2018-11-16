package com.whystudyalone.tmandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class optionQuestionOrderForm extends Activity implements OnClickListener
{
	// Declare the components here since all functions in this class will need access...
	TextView IDC_ORDEROPTION_LABEL;
	RadioGroup IDC_ORDEROPTION_RADIOGROUP;
	RadioButton IDC_ORDEROPTION_RADIO_DEFAULT;
	RadioButton IDC_ORDEROPTION_RADIO_RANDOM;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		try
		{
	    	this.setTheme(android.R.style.Theme_Light);
			setContentView(R.layout.optionquestionorderform);
	        IDC_ORDEROPTION_LABEL = (TextView) this.findViewById(R.id.IDC_ORDEROPTION_LABEL);
	        IDC_ORDEROPTION_RADIOGROUP = (RadioGroup) this.findViewById(R.id.IDC_ORDEROPTION_RADIOGROUP);
	        IDC_ORDEROPTION_RADIO_DEFAULT = (RadioButton) this.findViewById(R.id.IDC_ORDEROPTION_RADIO_DEFAULT);
	        IDC_ORDEROPTION_RADIO_RANDOM = (RadioButton) this.findViewById(R.id.IDC_ORDEROPTION_RADIO_RANDOM);
	        IDC_ORDEROPTION_LABEL.setTextSize(IDC_ORDEROPTION_RADIO_DEFAULT.getTextSize());
			IDC_ORDEROPTION_RADIO_DEFAULT.setOnClickListener(this);
			IDC_ORDEROPTION_RADIO_RANDOM.setOnClickListener(this);
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".onCreate().");
		}
    }
	
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		try
		{
			switch(v.getId())
			{
				case R.id.IDC_ORDEROPTION_RADIO_DEFAULT:
				{
					mainMenuForm.g_nQuestionOrderToggle = 0;
					break;
				}
				case R.id.IDC_ORDEROPTION_RADIO_RANDOM:
				{
					mainMenuForm.g_nQuestionOrderToggle = 1;
					break;
				}
			}
	        Intent i = new Intent(this, optionTermDisplayForm.class);
	        startActivity(i);
			finish();
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".onClick().");
		}
	}

	@Override
	public void onStop()
	{
		// Close when not visible to save memory!
		super.onStop();
		finish();
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