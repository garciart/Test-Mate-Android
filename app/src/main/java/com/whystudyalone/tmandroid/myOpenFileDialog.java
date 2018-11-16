package com.whystudyalone.tmandroid;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class myOpenFileDialog extends ListActivity
{
    // Thanks to plusminus at http://www.anddev.org/
	private enum DISPLAYMODE{ ABSOLUTE, RELATIVE; }
    private final DISPLAYMODE displayMode = DISPLAYMODE.RELATIVE;
    private List<IconifiedText> directoryEntries = new ArrayList<IconifiedText>();
    private File currentDirectory = new File("/sdcard/");

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	try
		{
	    	// IconifiedTextView handles what setContentView() does
	    	this.setTheme(android.R.style.Theme_Light);
	    	String l_strTitle = mainMenuForm.g_nMenuChoice == 0 ? "Open a New Test..." : "Load a Saved Test...";
	    	this.setTitle(l_strTitle);
	    	browseToRoot();
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".onCreate().");
		}
	}
    
	@Override
	public void onStop()
	{
		// Close when not visible to save memory!
		super.onStop();
		finish();
	}
    
    /** This function browses to the root-directory of the file-system. */
    private void browseToRoot()
    {
    	try
		{
    		browseTo(new File("/sdcard/"));
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".browseToRoot().");
		}
	}
    
    /** This function browses up one level according to the field: currentDirectory */
    private void upOneLevel()
    {
    	try
		{
    		if(this.currentDirectory.getParent() != null)
			{
	    		this.browseTo(this.currentDirectory.getParentFile());
			}
	    	else
	    	{
	    		// Continue...
	    	}
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".upOneLevel().");
		}
    }
    
    /** Determines whether the passed parameter is a directory (calls "fill(aDirectory.listFiles());") or a File (calls "openFile(aDirectory);" if the user permitted.) */
    private void browseTo(final File aDirectory)
    {
    	try
		{
	    	if (aDirectory.isDirectory())
			{
				this.currentDirectory = aDirectory;
	    		fill(aDirectory.listFiles());
			}
			else
			{
				mainMenuForm.g_strFileName = aDirectory.getName();
				mainMenuForm.g_strDirectoryName = aDirectory.getParent();
				mainMenuForm.g_boolOpenFileDialog = true;
				if(mainMenuForm.g_strFileName.endsWith(".td") || mainMenuForm.g_strFileName.endsWith(".tdu"))
				{
					Intent i = new Intent(this, optionQuestionOrderForm.class);
					startActivity(i);
					finish();
				}
				else if(mainMenuForm.g_strFileName.endsWith(".ts") || mainMenuForm.g_strFileName.endsWith(".tsu"))
				{
					Intent i = new Intent(this, testForm.class);
					startActivity(i);
					finish();
				}
		    	else
		    	{
		    		// Continue...
		    	}
			}
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".browseTo().");
		}
	}
    
    /** Takes a File[] array, and displays each file name on the screen in the list, including a switch on out Mode (Rel. or Abs.). It also displays two Strings for backward navigation (we define in strings.xml: 'up_one_level', ...) */
    private void fill(File[] files)
    {
    	try
    	{
	    	// String l_strFileExt = mainMenuForm.g_nMenuChoice == 0 ? ".td" : ".ts";
	    	this.directoryEntries.clear();
	    	// Add the back icon and the ".."
	    	if(this.currentDirectory.getParent() != null)
	    	{
	    		this.directoryEntries.add(new IconifiedText(getString(R.string.up_one_level), getResources().getDrawable(R.drawable.back)));
	    	}
	    	else
	    	{
	    		// Continue...
	    	}
	    	Drawable currentIcon = null;
	    	for (File currentFile : files)
	    	{
	    		if (currentFile.isDirectory())
	    		{
	    			currentIcon = getResources().getDrawable(R.drawable.directory);
				}
	    		// else if(currentFile.getName().endsWith(l_strFileExt) == true)
				else if(mainMenuForm.g_nMenuChoice == 0 && currentFile.getName().endsWith(".td") || mainMenuForm.g_nMenuChoice == 0 && currentFile.getName().endsWith(".tdu"))
	    		{
	    			currentIcon = getResources().getDrawable(R.drawable.tmicon16);
				}
				else if(mainMenuForm.g_nMenuChoice == 1 && currentFile.getName().endsWith(".ts") || mainMenuForm.g_nMenuChoice == 1 && currentFile.getName().endsWith(".tsu"))
	    		{
	    			currentIcon = getResources().getDrawable(R.drawable.tmicon16);
				}
				else
		    	{
		    		// Continue...
		    	}
	    		switch(this.displayMode)
		    	{
			    	case ABSOLUTE:
			    	{
		    			// if((currentFile.isDirectory() == true) || (currentFile.getName().endsWith(l_strFileExt) == true))
			    		if((currentFile.isDirectory() == true) || (mainMenuForm.g_nMenuChoice == 0 && currentFile.getName().endsWith(".td") || mainMenuForm.g_nMenuChoice == 0 && currentFile.getName().endsWith(".tdu")) == true)
		    			{
		    				this.directoryEntries.add(new IconifiedText(currentFile.getPath(), currentIcon));
		    			}
			    		else if((currentFile.isDirectory() == true) || (mainMenuForm.g_nMenuChoice == 1 && currentFile.getName().endsWith(".ts") || mainMenuForm.g_nMenuChoice == 1 && currentFile.getName().endsWith(".tsu")) == true)
		    			{
		    				this.directoryEntries.add(new IconifiedText(currentFile.getPath(), currentIcon));
		    			}
			    		else
		    	    	{
		    	    		// Continue...
		    	    	}
			    		break;
			    	}
					case RELATIVE: // On relative Mode, we have to add the current-path to the beginning
					{
						int currentPathStringLength = this.currentDirectory.getAbsolutePath().length();
		    			// if((currentFile.isDirectory() == true) || (currentFile.getName().endsWith(l_strFileExt) == true))
			    		if((currentFile.isDirectory() == true) || (mainMenuForm.g_nMenuChoice == 0 && currentFile.getName().endsWith(".td") || mainMenuForm.g_nMenuChoice == 0 && currentFile.getName().endsWith(".tdu")) == true)
			    		{
		    				this.directoryEntries.add(new IconifiedText(currentFile.getAbsolutePath().substring(currentPathStringLength), currentIcon));
		    			}
			    		else if((currentFile.isDirectory() == true) || (mainMenuForm.g_nMenuChoice == 1 && currentFile.getName().endsWith(".ts") || mainMenuForm.g_nMenuChoice == 1 && currentFile.getName().endsWith(".tsu")) == true)
			    		{
		    				this.directoryEntries.add(new IconifiedText(currentFile.getAbsolutePath().substring(currentPathStringLength), currentIcon));
		    			}
			    		else
		    	    	{
		    	    		// Continue...
		    	    	}
						break;
					}
				}
			}
			Collections.sort(this.directoryEntries);
			IconifiedTextListAdapter itla = new IconifiedTextListAdapter(this);
			itla.setListItems(this.directoryEntries);
			this.setListAdapter(itla);
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".fill().");
		}
	}

    // @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
    	super.onListItemClick(l, v, position, id);
    	try
    	{
	    	String selectedFileString = this.directoryEntries.get(position).getText();
	    	// Leave the first 'if' here or it doesn't work 
	    	if (selectedFileString.equals("."))
	    	{
	    		// Refresh
	    		this.browseTo(this.currentDirectory);
			}
	    	else if(selectedFileString.equals(".."))
	    	{
	    		this.upOneLevel();
			}
	    	else
	    	{
	    		File clickedFile = null;
	    		switch (this.displayMode)
	    		{
					case ABSOLUTE:
					{
						clickedFile = new File(this.directoryEntries.get(position).getText());
						break;
					}
		    		case RELATIVE:
	    			{
	    				clickedFile = new File(this.currentDirectory.getAbsolutePath() + this.directoryEntries.get(position).getText());
	    				break;
	    			}
				}
	    		if (clickedFile != null)
	    		{
	    			this.browseTo(clickedFile);
	    		}
		    	else
		    	{
		    		// Continue...
		    	}
			}
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".onListItemClick().");
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