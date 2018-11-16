package com.whystudyalone.tmandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

public class mediaForm extends Activity
{
	// Declare the components here since all functions in this class will need access...
	LinearLayout IDC_MEDIA_FORM;
	ImageView IDC_MEDIA_IMAGE;
	MenuItem MENU_CLOSE;
	MediaPlayer mediaPlayer;
	VideoView videoView;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		try
		{
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			if(mainMenuForm.g_nTempMediaFlagHolder == 1)
			{
		        setContentView(R.layout.mediaform);
				IDC_MEDIA_IMAGE = (ImageView) this.findViewById(R.id.IDC_MEDIA_IMAGE);
				Bitmap myBitmap = BitmapFactory.decodeFile(mainMenuForm.g_strTempMediaNameHolder);
				IDC_MEDIA_IMAGE.setImageBitmap(myBitmap);
			}
			else if(mainMenuForm.g_nTempMediaFlagHolder == 2)
			{
				setContentView(R.layout.mediaform);
				IDC_MEDIA_FORM = (LinearLayout) this.findViewById(R.id.IDC_MEDIAFORM_LINEARLAYOUT);
				IDC_MEDIA_FORM.setBackgroundColor(Color.WHITE);
				IDC_MEDIA_IMAGE = (ImageView) this.findViewById(R.id.IDC_MEDIA_IMAGE);
				mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(mainMenuForm.g_strTempMediaNameHolder));
				mediaPlayer.seekTo(0);
				mediaPlayer.start();
			}
			else if(mainMenuForm.g_nTempMediaFlagHolder == 3)
			{
		        setContentView(R.layout.videoform);
				videoView = (VideoView)findViewById(R.id.IDC_VIDEO_SURFACE);
				videoView.setKeepScreenOn(true);
				videoView.setVideoPath(mainMenuForm.g_strTempMediaNameHolder);
				videoView.seekTo(0);
				videoView.start();
			}
			else
			{
				// Continue
			}
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".onCreate().");
		}
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
    	try
    	{
			if(mainMenuForm.g_nTempMediaFlagHolder == 2)
			{
				mediaPlayer.stop();
				mediaPlayer.release();
			}
			else if(mainMenuForm.g_nTempMediaFlagHolder == 3)
			{
				videoView.stopPlayback();
			}
    	}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".onDestroy().");
		}
	}
	
    @Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
    	try
    	{
			MENU_CLOSE = menu.add("Close");
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
