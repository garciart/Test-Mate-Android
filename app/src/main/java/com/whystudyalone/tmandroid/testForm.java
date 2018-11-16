package com.whystudyalone.tmandroid;

import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class testForm extends Activity implements OnClickListener
{
	// Call at the beginning and then only once, or it will regenerate the same set of numbers...
	private Random getRandom = new Random();
	
	// Member Variables //
	private static long m_longStartTime = 0; // Start time in ticks...
	private String m_strTestTitle = ""; // The test title...
	private int m_nNumberOfQuestions = 0; // Total number of questions; figured out using z++ after each read...
	private String[] m_strQuestion = new String[300]; // Container for the actual question...
	private int[] m_nMediaFlag = new int[300]; // 1 for images, 2 for audio files and 3 for video files
	private String[] m_strMediaName = new String[300]; // Container for media file names, based on the media flag...
	private int[] m_nNumberOfAnswers = new int[300]; // Container for the number of answers, zero-based 9 (10) except for TF...
	private String[][] m_strAnswers = new String[300][9]; // Container for the actual answers; up to zero-based 9 (10)...
	private String[] m_strCorrectAnswer = new String[300]; // Container for the correct answer...
	private String[] m_strExplanation = new String[300]; // Container for the explanation...
	private int m_nCount = 0; // The array number of the question currently being shown...
	private int m_nNumberCorrect = 0; // The number of questions answered correctly...
	
	private float m_fDefaultTextSize = 0;
	public String m_strTemp = ""; // To pass info to inner classes (see Save option in Menu)...
	
	// Declare the components here since all functions in this class will need access...
	TextView IDC_TEST_LABEL_QUESTIONBOX;
	RadioGroup IDC_TEST_RADIOGROUP;
	RadioButton IDC_TEST_RADIO_ANSWER1;
	RadioButton IDC_TEST_RADIO_ANSWER2;
	RadioButton IDC_TEST_RADIO_ANSWER3;
	RadioButton IDC_TEST_RADIO_ANSWER4;
	
	MenuItem MENU_ACCESSMEDIA;
	MenuItem MENU_SAVE;
	MenuItem MENU_EXIT;
	MenuItem MENU_INCREASEFONT;
	MenuItem MENU_DECREASEFONT;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		try
		{
	    	this.setTheme(android.R.style.Theme_Light);
			setContentView(R.layout.testform);
	        IDC_TEST_LABEL_QUESTIONBOX = (TextView) this.findViewById(R.id.IDC_TEST_LABEL_QUESTIONBOX);
	        IDC_TEST_RADIOGROUP = (RadioGroup) this.findViewById(R.id.IDC_TEST_RADIOGROUP);
			IDC_TEST_RADIO_ANSWER1 = (RadioButton) this.findViewById(R.id.IDC_TEST_RADIO_ANSWER1);
			IDC_TEST_RADIO_ANSWER2 = (RadioButton) this.findViewById(R.id.IDC_TEST_RADIO_ANSWER2);
			IDC_TEST_RADIO_ANSWER3 = (RadioButton) this.findViewById(R.id.IDC_TEST_RADIO_ANSWER3);
			IDC_TEST_RADIO_ANSWER4 = (RadioButton) this.findViewById(R.id.IDC_TEST_RADIO_ANSWER4);
			IDC_TEST_LABEL_QUESTIONBOX.setTextSize(IDC_TEST_RADIO_ANSWER1.getTextSize());
			m_fDefaultTextSize = IDC_TEST_LABEL_QUESTIONBOX.getTextSize();
			IDC_TEST_RADIO_ANSWER1.setOnClickListener(this);
			IDC_TEST_RADIO_ANSWER2.setOnClickListener(this);
			IDC_TEST_RADIO_ANSWER3.setOnClickListener(this);
			IDC_TEST_RADIO_ANSWER4.setOnClickListener(this);
			if(mainMenuForm.g_nMenuChoice == 0)
			{
				if(prepareNewTest() == true)
				{
		    		final Builder builder = new AlertDialog.Builder(this);
		    		builder.setTitle("Test Mate");
		    		builder.setIcon(R.drawable.tmicon48);
		    		builder.setMessage("Test loaded!");
		    		builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener()
		    		{
		    			@Override
		    			public void onClick(DialogInterface dialog, int which)
		    			{
		    		    	m_longStartTime = System.currentTimeMillis();
							drawTest();
	    				}
	    			});
		    		builder.show();
				}
				else
				{
					final Builder builder = new AlertDialog.Builder(this);
		    		builder.setTitle("Test Mate");
		    		builder.setIcon(R.drawable.tmicon48);
		    		builder.setMessage("Unable to load that test!");
		    		builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener()
		    		{
		    			public void onClick(DialogInterface dialog, int which)
		    			{
		    				finish();
	    				}
	    			});
		    		builder.show();
				}
			}
			else if(mainMenuForm.g_nMenuChoice == 1)
			{
				if(loadSavedTest(mainMenuForm.g_strFileName) == true)
				{
		    		final Builder builder = new AlertDialog.Builder(this);
		    		builder.setTitle("Test Mate");
		    		builder.setIcon(R.drawable.tmicon48);
		    		builder.setMessage("Test loaded!");
		    		builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener()
		    		{
		    			public void onClick(DialogInterface dialog, int which)
		    			{
		                    // Time is reset in loadFile()...
							drawTest();
	    				}
	    			});
		    		builder.show();
				}
				else
				{
					final Builder builder = new AlertDialog.Builder(this);
		    		builder.setTitle("Test Mate");
		    		builder.setIcon(R.drawable.tmicon48);
		    		builder.setMessage("Unable to load your saved test!");
		    		builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener()
		    		{
		    			public void onClick(DialogInterface dialog, int which)
		    			{
		    				finish();
	    				}
	    			});
		    		builder.show();
				}
			}
			else
			{
	    		final Builder builder = new AlertDialog.Builder(this);
	    		builder.setTitle("Test Mate");
	    		builder.setIcon(R.drawable.tmicon48);
	    		builder.setMessage("We're sorry, but that choice is unavailable.");
	    		builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener()
	    		{
	    			public void onClick(DialogInterface dialog, int which)
	    			{
	    				finish();
    				}
    			});
	    		builder.show();
			}
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
		try
		{
			// TODO Auto-generated method stub
			String l_strTemp = "";
			switch(v.getId())
			{
				case R.id.IDC_TEST_RADIO_ANSWER1:
				{
					l_strTemp = (String) IDC_TEST_RADIO_ANSWER1.getText();
					break;
				}
				case R.id.IDC_TEST_RADIO_ANSWER2:
				{
					l_strTemp = (String) IDC_TEST_RADIO_ANSWER2.getText();
					break;
				}
				case R.id.IDC_TEST_RADIO_ANSWER3:
				{
					l_strTemp = (String) IDC_TEST_RADIO_ANSWER3.getText();
					break;
				}
				case R.id.IDC_TEST_RADIO_ANSWER4:
				{
					l_strTemp = (String) IDC_TEST_RADIO_ANSWER4.getText();
					break;
				}
				default:
				{
					final Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Test Mate");
		    		builder.setIcon(R.drawable.tmicon48);
		    		builder.setMessage("Nothing selected!");
		    		builder.setPositiveButton("OK", null);
		    		builder.show();
					break;
				}
			}
			if(l_strTemp.equals(m_strCorrectAnswer[m_nCount]))
			{
				m_nNumberCorrect++;
				if(m_nCount < m_nNumberOfQuestions)
				{
					if(mainMenuForm.g_nProvideFeedbackToggle == 1)
					{
						final Builder builder = new AlertDialog.Builder(this);
						builder.setTitle("Test Mate");
			    		builder.setIcon(R.drawable.tmicon48);
			    		builder.setMessage("Correct. " + m_strExplanation[m_nCount]);
			    		builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener()
	    			    {
	    					@Override
	    					public void onClick(DialogInterface dialog, int which)
	    			        {
	    						m_nCount++;
	    						IDC_TEST_RADIOGROUP.check(-1);
	    						drawTest();
	    			        }
	    			    });
			    		builder.show();
					}
					else
                    {
						m_nCount++;
						IDC_TEST_RADIOGROUP.check(-1);
						drawTest();
                    }
				}
				else // if the last question...
				{
					if(mainMenuForm.g_nProvideFeedbackToggle == 1)
					{
						final Builder builder = new AlertDialog.Builder(this);
						builder.setTitle("Test Mate");
			    		builder.setIcon(R.drawable.tmicon48);
			    		builder.setMessage("Correct. " + m_strExplanation[m_nCount]);
			    		builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener()
	    			    {
	    					@Override
	    					public void onClick(DialogInterface dialog, int which)
	    			        {
	    						builder.setTitle("Test Mate");
	    			    		builder.setIcon(R.drawable.tmicon48);
	    			    		builder.setMessage("Congratulations, you've completed your test!");
	    			    		builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener()
	    	    			    {
	    	    					@Override
	    	    					public void onClick(DialogInterface dialog, int which)
	    	    			        {
	    	    						builder.setTitle("Test Mate");
	    	    			    		builder.setIcon(R.drawable.tmicon48);
	    	    			    		builder.setMessage("Your statistics:\n" +
	    	    								m_nNumberCorrect + " correct out of " + (m_nNumberOfQuestions + 1) +
	    	    								"\nScore: " + (int)((m_nNumberCorrect * 100) / (m_nNumberOfQuestions + 1)) +
	    	    								"%\nElapsed time: " + getElapsedTime(m_longStartTime));
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
	    			    });
			    		builder.show();
					}
					else
                    {
						final Builder builder = new AlertDialog.Builder(this);
						builder.setTitle("Test Mate");
			    		builder.setIcon(R.drawable.tmicon48);
			    		builder.setMessage("Congratulations, you've completed your test!");
			    		builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener()
	    			    {
	    					@Override
	    					public void onClick(DialogInterface dialog, int which)
	    			        {
	    						builder.setTitle("Test Mate");
	    			    		builder.setIcon(R.drawable.tmicon48);
	    			    		builder.setMessage("Your statistics:\n" +
	    								m_nNumberCorrect + " correct out of " + (m_nNumberOfQuestions + 1) +
	    								"\nScore: " + (int)((m_nNumberCorrect * 100) / (m_nNumberOfQuestions + 1)) +
	    								"%\nElapsed time: " + getElapsedTime(m_longStartTime));
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
			else // if incorrect...
			{
				if(m_nCount < m_nNumberOfQuestions)
				{
					if(mainMenuForm.g_nProvideFeedbackToggle == 1)
					{
						final Builder builder = new AlertDialog.Builder(this);
						builder.setTitle("Test Mate");
			    		builder.setIcon(R.drawable.tmicon48);
			    		builder.setMessage("Incorrect. " + m_strExplanation[m_nCount]);
			    		builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener()
	    			    {
	    					@Override
	    					public void onClick(DialogInterface dialog, int which)
	    			        {
	    						m_nCount++;
	    						IDC_TEST_RADIOGROUP.check(-1);
	    						drawTest();
	    			        }
	    			    });
			    		builder.show();
					}
					else
                    {
						m_nCount++;
						IDC_TEST_RADIOGROUP.check(-1);
						drawTest();
                    }
				}
				else // if the last question...
				{
					if(mainMenuForm.g_nProvideFeedbackToggle == 1)
					{
						final Builder builder = new AlertDialog.Builder(this);
						builder.setTitle("Test Mate");
			    		builder.setIcon(R.drawable.tmicon48);
			    		builder.setMessage("Incorrect. " + m_strExplanation[m_nCount]);
			    		builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener()
	    			    {
	    					@Override
	    					public void onClick(DialogInterface dialog, int which)
	    			        {
	    						builder.setTitle("Test Mate");
	    			    		builder.setIcon(R.drawable.tmicon48);
	    			    		builder.setMessage("Congratulations, you've completed your test!");
	    			    		builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener()
	    	    			    {
	    	    					@Override
	    	    					public void onClick(DialogInterface dialog, int which)
	    	    			        {
	    	    						builder.setTitle("Test Mate");
	    	    			    		builder.setIcon(R.drawable.tmicon48);
	    	    			    		builder.setMessage("Your statistics:\n" +
	    	    								m_nNumberCorrect + " correct out of " + (m_nNumberOfQuestions + 1) +
	    	    								"\nScore: " + (int)((m_nNumberCorrect * 100) / (m_nNumberOfQuestions + 1)) +
	    	    								"%\nElapsed time: " + getElapsedTime(m_longStartTime));
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
	    			    });
			    		builder.show();
					}
					else
                    {
						final Builder builder = new AlertDialog.Builder(this);
						builder.setTitle("Test Mate");
			    		builder.setIcon(R.drawable.tmicon48);
			    		builder.setMessage("Congratulations, you've completed your test!");
			    		builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener()
	    			    {
	    					@Override
	    					public void onClick(DialogInterface dialog, int which)
	    			        {
	    						builder.setTitle("Test Mate");
	    			    		builder.setIcon(R.drawable.tmicon48);
	    			    		builder.setMessage("Your statistics:\n" +
	    								m_nNumberCorrect + " correct out of " + (m_nNumberOfQuestions + 1) +
	    								"\nScore: " + (int)((m_nNumberCorrect * 100) / (m_nNumberOfQuestions + 1)) +
	    								"%\nElapsed time: " + getElapsedTime(m_longStartTime));
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
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".onClick().");
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		try
		{
			if (keyCode == KeyEvent.KEYCODE_BACK)
			{
				if (m_nCount != m_nNumberOfQuestions)
				{
		    		final Builder builder = new AlertDialog.Builder(this);
		    		builder.setTitle("Test Mate");
		    		builder.setIcon(R.drawable.tmicon48);
		    		builder.setMessage("Are you sure you want to stop taking this test?");
		    		builder.setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener()
		    		{
		    			public void onClick(DialogInterface dialog, int which)
		    			{
		    				finish();
	    				}
	    			});
		    		builder.setNegativeButton("No", null); 
		    		builder.show();
					return true;
				}
				else
				{
					finish();
					return true;
				}
			}
			else
			{
				return super.onKeyDown(keyCode, event);
			}
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".onKeyDown().");
			return true;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);		
		try
		{
			MENU_ACCESSMEDIA = menu.add("Access Media...");
			MENU_SAVE = menu.add("Save...");
			MENU_EXIT = menu.add("Exit...");
			MENU_INCREASEFONT = menu.add("Increase Font...");
			MENU_DECREASEFONT = menu.add("Decrease Font...");
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
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);
		try
		{
			if(m_nMediaFlag[m_nCount] > 0)
			{
				MENU_ACCESSMEDIA.setVisible(true);
			}
			else
			{
				MENU_ACCESSMEDIA.setVisible(false);
			}
			return true;
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".onPrepareOptionsMenu().");
			return false;
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		try
		{
			if(item == MENU_ACCESSMEDIA)
			{
				mainMenuForm.g_nTempMediaFlagHolder = m_nMediaFlag[m_nCount];
				mainMenuForm.g_strTempMediaNameHolder = mainMenuForm.g_strDirectoryName + "/" + m_strMediaName[m_nCount];
				Intent i = new Intent(this, mediaForm.class);
				startActivity(i);
				return true;
			}
			else if(item == MENU_SAVE)
			{ 
				// CHANGE //
				String l_strSavedFileName = "";
				if(mainMenuForm.g_strFileName.endsWith(".td"))
				{
					l_strSavedFileName = mainMenuForm.g_strFileName.substring(0, mainMenuForm.g_strFileName.indexOf(".")) + ".ts";
				}
				else if(mainMenuForm.g_strFileName.endsWith(".tdu"))
				{
					l_strSavedFileName = mainMenuForm.g_strFileName.substring(0, mainMenuForm.g_strFileName.indexOf(".")) + ".tsu";
				}
				m_strTemp = l_strSavedFileName;
				// CHANGE //

				boolean exists = (new File(mainMenuForm.g_strDirectoryName + "/" + l_strSavedFileName)).exists();
				if(exists == true)
				{
		    		final Builder builder = new AlertDialog.Builder(this);
		    		builder.setTitle("Test Mate");
		    		builder.setIcon(R.drawable.tmicon48);
		    		builder.setMessage("A saved test of this subject exists. Overwrite?");
		    		builder.setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener()
		    		{
		    			public void onClick(DialogInterface dialog, int which)
		    			{
							if(saveCurrentTest(m_strTemp) == true)
							{
					    		builder.setTitle("Test Mate");
					    		builder.setIcon(R.drawable.tmicon48);
					    		builder.setMessage("Test saved!");
					    		builder.setPositiveButton("OK", null);
					    		builder.show();
							}
							else
							{
								builder.setTitle("Test Mate");
					    		builder.setIcon(R.drawable.tmicon48);
					    		builder.setMessage("Could not save your test! Please contact us at tmdevelopment@whystudyalone.com.");
					    		builder.setPositiveButton("OK", null);
					    		builder.show();
							}
	    				}
	    			});
		    		builder.setNegativeButton("No", null);
		    		builder.show();
		    		return true;
				}
				else
				{
					if(saveCurrentTest(l_strSavedFileName) == true)
					{
						final Builder builder = new AlertDialog.Builder(this);
						builder.setTitle("Test Mate");
			    		builder.setIcon(R.drawable.tmicon48);
			    		builder.setMessage("Test saved!");
			    		builder.setPositiveButton("OK", null);
			    		builder.show();
					}
					else
					{
						final Builder builder = new AlertDialog.Builder(this);
						builder.setTitle("Test Mate");
			    		builder.setIcon(R.drawable.tmicon48);
			    		builder.setMessage("Could not save your test! Please contact us at tmdevelopment@whystudyalone.com.");
			    		builder.setPositiveButton("OK", null);
			    		builder.show();
					}
				}
				return true;
			}
			else if(item == MENU_EXIT)
			{
				if (m_nCount != m_nNumberOfQuestions)
				{
		    		final Builder builder = new AlertDialog.Builder(this);
		    		builder.setTitle("Test Mate");
		    		builder.setIcon(R.drawable.tmicon48);
		    		builder.setMessage("Are you sure you want to stop taking this test?");
		    		builder.setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener()
		    		{
		    			public void onClick(DialogInterface dialog, int which)
		    			{
		    				finish();
	    				}
	    			});
		    		builder.setNegativeButton("No", null); 
		    		builder.show();
					return true;
				}
				else
				{
					finish();
					return true;
				}
			}
			else if(item == MENU_INCREASEFONT)
			{
				if(IDC_TEST_LABEL_QUESTIONBOX.getTextSize() < (m_fDefaultTextSize + 9))
				{
					float l_fTextSize = IDC_TEST_LABEL_QUESTIONBOX.getTextSize();
					this.IDC_TEST_LABEL_QUESTIONBOX.setTextSize(l_fTextSize + 3);
					this.IDC_TEST_RADIO_ANSWER1.setTextSize(l_fTextSize + 3);
					this.IDC_TEST_RADIO_ANSWER2.setTextSize(l_fTextSize + 3);
					this.IDC_TEST_RADIO_ANSWER3.setTextSize(l_fTextSize + 3);
					this.IDC_TEST_RADIO_ANSWER4.setTextSize(l_fTextSize + 3);
					return true;
				}
				else
				{
					final Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Test Mate");
		    		builder.setIcon(R.drawable.tmicon48);
		    		builder.setMessage("This is the maximum size allowed!");
		    		builder.setPositiveButton("OK", null);
		    		builder.show();
					return true;
				}
			}
			else if(item == MENU_DECREASEFONT)
			{
				if(IDC_TEST_LABEL_QUESTIONBOX.getTextSize() > (m_fDefaultTextSize - 9))
				{
					float l_fTextSize = IDC_TEST_LABEL_QUESTIONBOX.getTextSize();
					this.IDC_TEST_LABEL_QUESTIONBOX.setTextSize(l_fTextSize - 3);
					this.IDC_TEST_RADIO_ANSWER1.setTextSize(l_fTextSize - 3);
					this.IDC_TEST_RADIO_ANSWER2.setTextSize(l_fTextSize - 3);
					this.IDC_TEST_RADIO_ANSWER3.setTextSize(l_fTextSize - 3);
					this.IDC_TEST_RADIO_ANSWER4.setTextSize(l_fTextSize - 3);
					return true;
				}
				else
				{
					final Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Test Mate");
		    		builder.setIcon(R.drawable.tmicon48);
		    		builder.setMessage("This is the minimum size allowed!");
		    		builder.setPositiveButton("OK", null);
		    		builder.show();
					return true;
				}
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
	private void drawTest()
	{
		try
		{
			String l_strTemp = m_strQuestion[m_nCount] + " (" + (m_nCount + 1) + " of " + (m_nNumberOfQuestions + 1) + ")";
			if(m_nMediaFlag[m_nCount] > 0)
			{
				l_strTemp += "[M].";
			}
			else
			{
				l_strTemp += ".";
			}
			IDC_TEST_LABEL_QUESTIONBOX.setText(l_strTemp);
			switch(m_nNumberOfAnswers[m_nCount])
			{
				case 0:
				{

					IDC_TEST_RADIO_ANSWER1.setVisibility(View.VISIBLE);
					IDC_TEST_RADIO_ANSWER1.setText(m_strAnswers[m_nCount][0]);
					IDC_TEST_RADIO_ANSWER2.setVisibility(View.GONE);
					IDC_TEST_RADIO_ANSWER3.setVisibility(View.GONE);
					IDC_TEST_RADIO_ANSWER4.setVisibility(View.GONE);
					break;
				}
				case 1:
				{
					IDC_TEST_RADIO_ANSWER1.setVisibility(View.VISIBLE);
					IDC_TEST_RADIO_ANSWER1.setText(m_strAnswers[m_nCount][0]);
					IDC_TEST_RADIO_ANSWER2.setVisibility(View.VISIBLE);
					IDC_TEST_RADIO_ANSWER2.setText(m_strAnswers[m_nCount][1]);
					IDC_TEST_RADIO_ANSWER3.setVisibility(View.GONE);
					IDC_TEST_RADIO_ANSWER4.setVisibility(View.GONE);
					break;
				}
				case 2:
				{
					IDC_TEST_RADIO_ANSWER1.setVisibility(View.VISIBLE);
					IDC_TEST_RADIO_ANSWER1.setText(m_strAnswers[m_nCount][0]);
					IDC_TEST_RADIO_ANSWER2.setVisibility(View.VISIBLE);
					IDC_TEST_RADIO_ANSWER2.setText(m_strAnswers[m_nCount][1]);
					IDC_TEST_RADIO_ANSWER3.setVisibility(View.VISIBLE);
					IDC_TEST_RADIO_ANSWER3.setText(m_strAnswers[m_nCount][2]);
					IDC_TEST_RADIO_ANSWER4.setVisibility(View.GONE);
					break;
				}
				case 3:
				{
					IDC_TEST_RADIO_ANSWER1.setVisibility(View.VISIBLE);
					IDC_TEST_RADIO_ANSWER1.setText(m_strAnswers[m_nCount][0]);
					IDC_TEST_RADIO_ANSWER2.setVisibility(View.VISIBLE);
					IDC_TEST_RADIO_ANSWER2.setText(m_strAnswers[m_nCount][1]);
					IDC_TEST_RADIO_ANSWER3.setVisibility(View.VISIBLE);
					IDC_TEST_RADIO_ANSWER3.setText(m_strAnswers[m_nCount][2]);
					IDC_TEST_RADIO_ANSWER4.setVisibility(View.VISIBLE);
					IDC_TEST_RADIO_ANSWER4.setText(m_strAnswers[m_nCount][3]);
					break;
				}
				default:
				{
                    throw new Exception("illegal number of choices in drawTest().");
				}
			}
			this.IDC_TEST_RADIO_ANSWER1.requestFocus();
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".drawTest().");
		}
	}

	// Device Dependent Method //
	private String getElapsedTime(long l_longStartTime)
	{
		try
		{
			// Remember, there are 10000 ticks in a millisecond!
			// Remember, Java uses milliseconds!
			long l_nElapsedTime = (System.currentTimeMillis() - l_longStartTime) / 1000;
			int l_nSeconds = ((int)(l_nElapsedTime % 60)) + 100;
			int l_nMinutes = ((int)((l_nElapsedTime % 3600) / 60)) + 100;
			int l_nHours = ((int)(l_nElapsedTime / 3600)) + 100;
			// Ugly, but works, since String.format() is found in J2SE but not in the RIM API.
			String l_strElapsedTime = Integer.toString(l_nHours).substring(1, 3) + ":" + Integer.toString(l_nMinutes).substring(1, 3) + ":" + Integer.toString(l_nSeconds).substring(1, 3);
			// C# = string l_strElapsedTime = string.Format("{0:00}:{1:00}:{2:00}", l_nHours, l_nMinutes, l_nSeconds);
			return l_strElapsedTime;
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".getElapsedTime().");
			return null;
		}
	}

	// Device Dependent Method //
	private String readFileContents(final String l_strFileName)
	{
		try
		{
			if(l_strFileName == null)
			{
				if(TMAndroid.g_bDebugFlag == true)
				{
					throw new Exception("no data received.");
				}
				else
				{
					return null;
				}
			}
			else
			{
				try
				{
					java.io.FileInputStream inputStream = new java.io.FileInputStream(l_strFileName);
					InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
					int c = 0;
					StringBuffer buffer = new StringBuffer();
					while((c = inputStreamReader.read()) != -1 && (char)c != '\u0000')
					{
						buffer.append((char)c);
					}
					String l_strEncryptedData = new String(buffer);
					inputStreamReader.close();
					inputStream.close();
		            return l_strEncryptedData;
				}
				catch(Exception e)
				{
					if(TMAndroid.g_bDebugFlag == true)
					{
						// For debug purposes, since everything here throws 101 types of exceptions!
						handleError(e.toString(), getClass().toString() + ".readFileContents() read.");
					}
					// If verbose is false, do not handle e; return to the main program...
					return null;
				}
			}
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".readFileContents().");
			return null;
		}
	}
	
	// Device Dependent Method //
	private boolean writeFileContents(String l_strFileName, String l_strFileData)
	{
		try
		{
			if(l_strFileName == null || l_strFileData == null)
			{
				if(TMAndroid.g_bDebugFlag == true)
				{
					throw new Exception("no data received.");
				}
				else
				{
					return false;
				}
			}
			else
			{
				// Replaces using statement in C#
				try
				{
					java.io.FileOutputStream outputStream = new java.io.FileOutputStream(l_strFileName);
					OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
					outputStreamWriter.write(l_strFileData, 0, l_strFileData.length());
					outputStreamWriter.close();
					outputStream.close();
					return true;
				}
				catch(Exception e)
				{
					if(TMAndroid.g_bDebugFlag == true)
					{
						// For debug purposes, since everything here throws 101 types of exceptions!
						handleError(e.toString(), getClass().toString() + ".writeFileContents() write.");
					}
					// If verbose is false, do not handle e; return to the main program...
					return false;
				}
			}
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".writeFileContents().");
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
    
	// INDEPENDENT CODE //
	// INDEPENDENT CODE //
	// INDEPENDENT CODE //

	// Independent Function //
	private String cryptThis(String l_strInput, String l_strKey)
	{
		try
		{
			if(l_strInput == null || l_strKey == null)
			{
				if(TMAndroid.g_bDebugFlag == true)
				{
					throw new Exception("no data received.");
				}
				else
				{
					return null;
				}
			}
			else
			{
				// Special thanks to Shrishail Rana and friends at CodeProject
				// Both key buffers correspond to a Substitution-Box (S-Box)...
				// Use the size of the input String, which changes the crypt completely when increased or decreased in size...
				StringBuffer l_sbKeyBuffer = new StringBuffer(l_strInput.length());
				StringBuffer l_sbKeyBuffer2 = new StringBuffer(l_strInput.length());
				StringBuffer l_sbResult = new StringBuffer();
				int m, n, o, p;
				char l_cTempChar, l_cXORChar;
				// This unsecured key is to be used only when there is no input key from the user...
				StringBuffer l_sbUnsecuredKey = new StringBuffer("Houston, we've had a problem.");
				m = n = o = p = 0;
				l_cTempChar = (char)0;
				// Initialize first sbox...
				for(m = 0; m < l_strInput.length(); m++)
				{
					l_sbKeyBuffer.append((char)m);
				}
				// Check if there is an input key from the user...
				if(l_strKey.length() != 0)
				{
					// Initialize the second sbox with the user key if present...
					for(m = 0; m < l_strInput.length(); m++)
					{
						if(n == l_strKey.length())
						{
							n = 0;
						}
						l_sbKeyBuffer2.append(l_strKey.charAt(n++));
					}
				}
				else
				{
					// Initialize the second sbox with our key if there is no user key...
					for(m = 0; m < l_strInput.length(); m++)
					{
						if(n == l_sbUnsecuredKey.length())
						{
							n = 0;
						}
						l_sbKeyBuffer2.append(l_sbUnsecuredKey.charAt(n++));
					}
				}
				// Reset n to 0...
				n = 0;
				// Scramble sbox 1 with sbox 2
				for(m = 0; m < l_strInput.length(); m++)
				{
					n = (n + l_sbKeyBuffer.charAt(m) + l_sbKeyBuffer2.charAt(m)) % l_strInput.length();
					l_cTempChar = (char) l_sbKeyBuffer.charAt(m);
					l_sbKeyBuffer.setCharAt(m, l_sbKeyBuffer.charAt(n));
					l_sbKeyBuffer.setCharAt(n, l_cTempChar);
				}
				m = n = 0;
				for(p = 0; p < l_strInput.length(); p++)
				{
					m = (m + 1) % l_strInput.length();
					n = (n + l_sbKeyBuffer.charAt(m)) % l_strInput.length();
					// Scramble sbox 1 further so encryption routine will repeat itself at a great interval...
					l_cTempChar = l_sbKeyBuffer.charAt(m);
					l_sbKeyBuffer.setCharAt(m, l_sbKeyBuffer.charAt(n));
					l_sbKeyBuffer.setCharAt(n, l_cTempChar);
					// Get ready to create pseudo random byte for the encryption key...
					o = (l_sbKeyBuffer.charAt(m) + l_sbKeyBuffer.charAt(n)) % l_strInput.length();
					// Get the random byte...
					l_cXORChar = l_sbKeyBuffer.charAt(o);
					// XOR with the data. Leave alone if the result equals NULL...
					if(((char)(l_strInput.charAt(p) ^ l_cXORChar)) == 0x0000)
					{
						l_sbResult.append(l_strInput.charAt(p));
					}
					else
					{
						l_sbResult.append((char)(l_strInput.charAt(p) ^ l_cXORChar));
					}
				}
				return(l_sbResult.toString());
			}
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".cryptThis().");
			return null;
		}
	}

	// Independent Function //
	private String decryptProfile(String l_strEncryptedData)
	{
		try
		{
			if(l_strEncryptedData == null)
			{
				if(TMAndroid.g_bDebugFlag == true)
				{
					throw new Exception("no data received.");
				}
				else
				{
					return null;
				}
			}
			else
			{
				int x = 0;
				StringBuffer l_sbTemp = new StringBuffer();
				// Decrypt the data...
				String l_strDecryptedData = cryptThis(l_strEncryptedData, "");
				if(l_strDecryptedData == null)
				{
					if(TMAndroid.g_bDebugFlag == true)
					{
						throw new Exception("data sent to subroutine; nothing returned.");
					}
					else
					{
						return null;
					}
				}
				else
				{
					// Initialize the table...
					int[] l_nOTP = new int[255];
					for(x = 0; x < l_nOTP.length; x++)
					{
						l_nOTP[x] = x;
					}
					// Select a table based on the first character of the decrypted file...
					switch(l_strDecryptedData.charAt(0))
					{
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						{
							l_nOTP = new int[] { 66, 80, 187, 179, 133, 255, 93, 250, 22, 185, 243, 212, 130, 87, 134, 139, 182, 123, 42, 213, 230, 254, 20, 240, 91, 177, 171, 219, 234, 10, 68, 166, 3, 198, 58, 97, 23, 122, 40, 211, 9, 27, 5, 156, 203, 33, 47, 208, 157, 152, 146, 72, 95, 96, 191, 126, 239, 251, 12, 62, 218, 2, 186, 144, 221, 34, 192, 13, 163, 32, 141, 136, 29, 202, 231, 164, 148, 30, 241, 53, 14, 113, 52, 235, 229, 180, 168, 155, 92, 8, 227, 129, 223, 44, 116, 209, 18, 35, 17, 237, 201, 99, 249, 207, 137, 172, 54, 199, 169, 183, 154, 16, 184, 88, 21, 50, 161, 104, 25, 31, 225, 26, 175, 159, 48, 181, 73, 118, 107, 245, 128, 108, 158, 82, 24, 78, 105, 38, 114, 132, 60, 37, 150, 206, 232, 19, 194, 195, 75, 253, 106, 189, 228, 236, 174, 79, 224, 103, 217, 140, 15, 135, 162, 7, 46, 1, 204, 94, 83, 61, 71, 246, 149, 100, 205, 110, 59, 69, 124, 41, 63, 28, 170, 242, 64, 56, 109, 190, 210, 188, 117, 43, 45, 143, 138, 102, 98, 85, 76, 125, 233, 238, 145, 131, 244, 39, 70, 120, 11, 248, 65, 147, 153, 86, 101, 84, 247, 121, 178, 57, 6, 214, 165, 142, 193, 119, 222, 176, 220, 112, 160, 167, 111, 252, 127, 115, 200, 4, 36, 226, 151, 89, 67, 51, 81, 173, 90, 215, 55, 196, 197, 74, 216, 77, 49 };
							break;
						}
						case 'F':
						case 'G':
						case 'H':
						case 'I':
						{
							l_nOTP = new int[] { 135, 50, 217, 125, 90, 193, 77, 12, 60, 248, 96, 191, 201, 116, 32, 211, 19, 101, 215, 13, 138, 113, 112, 59, 70, 163, 237, 62, 234, 24, 175, 243, 26, 51, 127, 36, 75, 47, 18, 194, 242, 54, 142, 181, 85, 185, 140, 119, 247, 148, 52, 84, 255, 210, 162, 117, 92, 236, 253, 121, 3, 133, 102, 128, 106, 91, 180, 10, 34, 165, 20, 72, 86, 2, 23, 66, 176, 159, 218, 216, 5, 81, 94, 196, 225, 120, 110, 227, 11, 129, 27, 131, 251, 171, 235, 187, 197, 31, 42, 65, 144, 139, 108, 252, 28, 97, 73, 157, 38, 204, 208, 124, 172, 29, 152, 168, 158, 33, 224, 141, 231, 118, 156, 64, 222, 69, 198, 25, 35, 189, 40, 223, 155, 214, 79, 174, 150, 39, 83, 122, 30, 192, 229, 74, 7, 188, 164, 100, 147, 80, 170, 126, 55, 17, 137, 212, 123, 167, 45, 57, 238, 239, 205, 21, 76, 61, 241, 178, 104, 105, 207, 149, 68, 232, 98, 46, 154, 37, 103, 14, 161, 240, 249, 53, 230, 22, 195, 115, 132, 206, 71, 41, 136, 88, 107, 6, 87, 213, 179, 44, 111, 186, 221, 244, 245, 177, 43, 16, 220, 48, 202, 209, 199, 151, 63, 89, 99, 93, 1, 82, 226, 254, 4, 143, 182, 109, 134, 130, 228, 145, 56, 200, 219, 183, 15, 190, 160, 153, 114, 184, 203, 250, 233, 58, 146, 95, 67, 169, 246, 166, 49, 9, 78, 173, 8 };
							break;
						}
						case 'J':
						case 'K':
						case 'L':
						case 'M':
						{
							l_nOTP = new int[] { 55, 103, 136, 251, 173, 215, 81, 225, 54, 7, 61, 102, 64, 57, 6, 214, 95, 75, 121, 111, 60, 206, 140, 53, 25, 159, 98, 177, 230, 72, 49, 192, 28, 65, 180, 1, 217, 171, 17, 211, 132, 199, 254, 66, 181, 108, 175, 172, 184, 24, 157, 156, 15, 100, 161, 153, 129, 125, 22, 126, 67, 74, 18, 8, 77, 167, 216, 86, 218, 51, 229, 34, 10, 79, 138, 178, 5, 155, 245, 220, 76, 115, 82, 248, 212, 228, 58, 250, 124, 106, 120, 128, 90, 117, 142, 146, 165, 176, 70, 89, 243, 219, 92, 119, 114, 43, 21, 94, 158, 50, 127, 209, 204, 139, 144, 4, 20, 160, 154, 97, 255, 239, 179, 42, 109, 151, 205, 131, 190, 116, 36, 73, 213, 26, 198, 168, 197, 134, 83, 68, 242, 163, 16, 104, 191, 188, 123, 183, 185, 253, 3, 9, 38, 137, 30, 37, 246, 170, 23, 187, 252, 189, 88, 84, 29, 101, 107, 222, 150, 40, 78, 110, 52, 174, 113, 11, 147, 135, 59, 141, 145, 118, 130, 35, 226, 32, 87, 105, 208, 63, 164, 148, 27, 46, 196, 31, 201, 236, 2, 45, 41, 47, 13, 247, 234, 240, 169, 244, 149, 238, 221, 91, 200, 203, 12, 237, 71, 96, 133, 19, 56, 207, 231, 241, 80, 93, 99, 186, 249, 69, 85, 232, 223, 48, 143, 14, 227, 224, 235, 122, 233, 194, 39, 166, 112, 62, 44, 152, 195, 162, 33, 202, 182, 193, 210 };
							break;
						}
						case 'N':
						case 'O':
						case 'P':
						case 'Q':
						{
							l_nOTP = new int[] { 66, 80, 187, 179, 133, 255, 93, 250, 22, 185, 243, 212, 130, 87, 134, 139, 182, 123, 42, 213, 230, 254, 20, 240, 91, 177, 171, 219, 234, 10, 68, 166, 3, 198, 58, 97, 23, 122, 40, 211, 9, 27, 5, 156, 203, 33, 47, 208, 157, 152, 146, 72, 95, 96, 191, 126, 239, 251, 12, 62, 218, 2, 186, 144, 221, 34, 192, 13, 163, 32, 141, 136, 29, 202, 231, 164, 148, 30, 241, 53, 14, 113, 52, 235, 229, 180, 168, 155, 92, 8, 227, 129, 223, 44, 116, 209, 18, 35, 17, 237, 201, 99, 249, 207, 137, 172, 54, 199, 169, 183, 154, 16, 184, 88, 21, 50, 161, 104, 25, 31, 225, 26, 175, 159, 48, 181, 73, 118, 107, 245, 128, 108, 158, 82, 24, 78, 105, 38, 114, 132, 60, 37, 150, 206, 232, 19, 194, 195, 75, 253, 106, 189, 228, 236, 174, 79, 224, 103, 217, 140, 15, 135, 162, 7, 46, 1, 204, 94, 83, 61, 71, 246, 149, 100, 205, 110, 59, 69, 124, 41, 63, 28, 170, 242, 64, 56, 109, 190, 210, 188, 117, 43, 45, 143, 138, 102, 98, 85, 76, 125, 233, 238, 145, 131, 244, 39, 70, 120, 11, 248, 65, 147, 153, 86, 101, 84, 247, 121, 178, 57, 6, 214, 165, 142, 193, 119, 222, 176, 220, 112, 160, 167, 111, 252, 127, 115, 200, 4, 36, 226, 151, 89, 67, 51, 81, 173, 90, 215, 55, 196, 197, 74, 216, 77, 49 };
							// Thanks to Fred Swartz
							for(int left = 0, right = l_nOTP.length - 1; left < right; left++, right--)
							{
								// exchange the first and last
								int temp = l_nOTP[left]; l_nOTP[left] = l_nOTP[right]; l_nOTP[right] = temp;
							}
							break;
						}
						case 'R':
						case 'S':
						case 'T':
						case 'U':
						{
							l_nOTP = new int[] { 135, 50, 217, 125, 90, 193, 77, 12, 60, 248, 96, 191, 201, 116, 32, 211, 19, 101, 215, 13, 138, 113, 112, 59, 70, 163, 237, 62, 234, 24, 175, 243, 26, 51, 127, 36, 75, 47, 18, 194, 242, 54, 142, 181, 85, 185, 140, 119, 247, 148, 52, 84, 255, 210, 162, 117, 92, 236, 253, 121, 3, 133, 102, 128, 106, 91, 180, 10, 34, 165, 20, 72, 86, 2, 23, 66, 176, 159, 218, 216, 5, 81, 94, 196, 225, 120, 110, 227, 11, 129, 27, 131, 251, 171, 235, 187, 197, 31, 42, 65, 144, 139, 108, 252, 28, 97, 73, 157, 38, 204, 208, 124, 172, 29, 152, 168, 158, 33, 224, 141, 231, 118, 156, 64, 222, 69, 198, 25, 35, 189, 40, 223, 155, 214, 79, 174, 150, 39, 83, 122, 30, 192, 229, 74, 7, 188, 164, 100, 147, 80, 170, 126, 55, 17, 137, 212, 123, 167, 45, 57, 238, 239, 205, 21, 76, 61, 241, 178, 104, 105, 207, 149, 68, 232, 98, 46, 154, 37, 103, 14, 161, 240, 249, 53, 230, 22, 195, 115, 132, 206, 71, 41, 136, 88, 107, 6, 87, 213, 179, 44, 111, 186, 221, 244, 245, 177, 43, 16, 220, 48, 202, 209, 199, 151, 63, 89, 99, 93, 1, 82, 226, 254, 4, 143, 182, 109, 134, 130, 228, 145, 56, 200, 219, 183, 15, 190, 160, 153, 114, 184, 203, 250, 233, 58, 146, 95, 67, 169, 246, 166, 49, 9, 78, 173, 8 };
							// Thanks to Fred Swartz
							for(int left = 0, right = l_nOTP.length - 1; left < right; left++, right--)
							{
								// exchange the first and last
								int temp = l_nOTP[left]; l_nOTP[left] = l_nOTP[right]; l_nOTP[right] = temp;
							}
							break;
						}
						case 'V':
						case 'W':
						case 'X':
						case 'Y':
						case 'Z':
						{
							l_nOTP = new int[] { 55, 103, 136, 251, 173, 215, 81, 225, 54, 7, 61, 102, 64, 57, 6, 214, 95, 75, 121, 111, 60, 206, 140, 53, 25, 159, 98, 177, 230, 72, 49, 192, 28, 65, 180, 1, 217, 171, 17, 211, 132, 199, 254, 66, 181, 108, 175, 172, 184, 24, 157, 156, 15, 100, 161, 153, 129, 125, 22, 126, 67, 74, 18, 8, 77, 167, 216, 86, 218, 51, 229, 34, 10, 79, 138, 178, 5, 155, 245, 220, 76, 115, 82, 248, 212, 228, 58, 250, 124, 106, 120, 128, 90, 117, 142, 146, 165, 176, 70, 89, 243, 219, 92, 119, 114, 43, 21, 94, 158, 50, 127, 209, 204, 139, 144, 4, 20, 160, 154, 97, 255, 239, 179, 42, 109, 151, 205, 131, 190, 116, 36, 73, 213, 26, 198, 168, 197, 134, 83, 68, 242, 163, 16, 104, 191, 188, 123, 183, 185, 253, 3, 9, 38, 137, 30, 37, 246, 170, 23, 187, 252, 189, 88, 84, 29, 101, 107, 222, 150, 40, 78, 110, 52, 174, 113, 11, 147, 135, 59, 141, 145, 118, 130, 35, 226, 32, 87, 105, 208, 63, 164, 148, 27, 46, 196, 31, 201, 236, 2, 45, 41, 47, 13, 247, 234, 240, 169, 244, 149, 238, 221, 91, 200, 203, 12, 237, 71, 96, 133, 19, 56, 207, 231, 241, 80, 93, 99, 186, 249, 69, 85, 232, 223, 48, 143, 14, 227, 224, 235, 122, 233, 194, 39, 166, 112, 62, 44, 152, 195, 162, 33, 202, 182, 193, 210 };
							// Thanks to Fred Swartz
							for(int left = 0, right = l_nOTP.length - 1; left < right; left++, right--)
							{
								// exchange the first and last
								int temp = l_nOTP[left]; l_nOTP[left] = l_nOTP[right]; l_nOTP[right] = temp;
							}
							break;
						}
						default:
						{
							if(TMAndroid.g_bDebugFlag == true)
							{
								throw new Exception("invalid profile.");
							}
							else
							{
								return null;
							}
						}
					}
					x = 0;
					// Read the encryption key...
					do
					{
						l_sbTemp.append(l_strDecryptedData.charAt(l_nOTP[x] + 1));
						x++;
					} while(l_sbTemp.charAt(x - 1) != '\u00B6');
					String l_strDecryptedKey = l_sbTemp.toString();
					return l_strDecryptedKey;
				}
			}
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".decryptProfile().");
			return null;
		}
	}

	// Independent Function //
	private String[] decryptTestData(String l_strEncryptedData, String l_strDecryptedKey)
	{
		try
		{
			if(l_strEncryptedData == null || l_strDecryptedKey == null)
			{
				if(TMAndroid.g_bDebugFlag == true)
				{
					throw new Exception("no data received.");
				}
				else
				{
					return null;
				}
			}
			else
			{
				int w = 0;
				int x = 0;
				Vector<String> vector = new Vector<String>(); // ArrayList in C#...
				// Total array slots required is 2283
				// Max mem is 362769, allowed is 65535 due to DataImputStream.ReadUTF
				// Arrays size is 297, rounded to 300 (0 - 99 terms, multiple choice and true/false)
				// Decrypt the data...
				
                // CHANGE //
				String l_strDecryptedData = "";
                if (mainMenuForm.g_strFileName.endsWith(".tdu") || mainMenuForm.g_strFileName.endsWith(".tsu"))
                {
                    switch (getRandom.nextInt(3) + 1)
                    {
                        case 1:
                            {
                                l_strDecryptedData = "01010010\r\n" + l_strEncryptedData;
                                break;
                            }
                        case 2:
                            {
                                l_strDecryptedData = "01001111\r\n" + l_strEncryptedData;
                                break;
                            }
                        case 3:
                            {
                                l_strDecryptedData = "01000010\r\n" + l_strEncryptedData;
                                break;
                            }
                        default:
                            {
                                if (TMAndroid.g_bDebugFlag == true)
                                {
                                    throw new Exception("cannot generate a random number.");
                                }
                                else
                                {
                                    return null;
                                }
                            }
                    }
                }
                else
                {
                    l_strDecryptedData = cryptThis(l_strEncryptedData, l_strDecryptedKey);
                }
                // CHANGE //

				if(l_strDecryptedData == null)
				{
					if(TMAndroid.g_bDebugFlag == true)
					{
						throw new Exception("data sent to subroutine; nothing returned.");
					}
					else
					{
						return null;
					}
				}
				else
				{
					for(x = 0; x < l_strDecryptedData.length(); x++)
					{
						if(l_strDecryptedData.charAt(x) == '\r' && l_strDecryptedData.charAt(x + 1) == '\n')
						{
							// REMEMBER, SUBSTRING PARAMETER #2 IS END OF SUBSTRING IN JAVA BUT LENGTH OF SUBSTRING IN C#!
							vector.addElement(l_strDecryptedData.substring(w, x));
							w = x + 2; // Take out carriage return and newline character...
						}
					}
					vector.addElement(l_strDecryptedData.substring(w, x));
					String[] l_strDecryptedDataArray = new String[vector.size()];
					vector.copyInto(l_strDecryptedDataArray);
					return l_strDecryptedDataArray;
				}
			}
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".decryptTestData().");
			return null;
		}
	}

	// Independent Function //
	private boolean loadSavedTest(String l_strSavedFileName)
	{
		try
		{
			if(l_strSavedFileName == null)
			{
				if(TMAndroid.g_bDebugFlag == true)
				{
					throw new Exception("no data received.");
				}
				else
				{
					return false;
				}
			}
			else
			{
				int w = 1;
				int x = 0;
				int y = 0;
				String l_strEncryptedData = readFileContents("/sdcard/tm/tm.tp");
				if(l_strEncryptedData == null)
				{
					if(TMAndroid.g_bDebugFlag == true)
					{
						throw new Exception("asked for profile data; nothing received.");
					}
					else
					{
						return false;
					}
				}
				else
				{
					String l_strDecryptedKey = decryptProfile(l_strEncryptedData);
					if(l_strDecryptedKey == null)
					{
						if(TMAndroid.g_bDebugFlag == true)
						{
							throw new Exception("profile data sent; nothing returned.");
						}
						else
						{
							return false;
						}
					}
					else
					{
						// BTW, THIS ONE DECRYPTS THE SAVED TEST, NOT THE ACTUAL TEST!
						l_strEncryptedData = readFileContents(mainMenuForm.g_strDirectoryName + "/" + l_strSavedFileName);
						if(l_strEncryptedData == null)
						{
							if(TMAndroid.g_bDebugFlag == true)
							{
								throw new Exception("asked for saved test data; nothing received.");
							}
							else
							{
								return false;
							}
						}
						else
						{
							String[] l_strDecryptedSavedDataArray = decryptTestData(l_strEncryptedData, l_strDecryptedKey);
							// length is not zero-based. Minimum good saved file dimensions are at least 6
							if(java.util.Arrays.equals(l_strDecryptedSavedDataArray, null) || l_strDecryptedSavedDataArray.length < 6)
							{
								if(TMAndroid.g_bDebugFlag == true)
								{
									throw new Exception("saved test data sent; nothing returned.");
								}
								else
								{
									return false;
								}
							}
							else
							{
								// Do not put together into an else if...
								if(l_strDecryptedSavedDataArray[0].equals("01010010") || l_strDecryptedSavedDataArray[0].equals("01001111") || l_strDecryptedSavedDataArray[0].equals("01000010"))
								{
									// BTW, THIS ONE DECRYPTS THE ACTUAL TEST, NOT THE SAVED TEST!
									mainMenuForm.g_strFileName = l_strDecryptedSavedDataArray[w++];
									l_strEncryptedData = readFileContents(mainMenuForm.g_strDirectoryName + "/" + mainMenuForm.g_strFileName);
									if(l_strEncryptedData == null)
									{
										if(TMAndroid.g_bDebugFlag == true)
										{
											throw new Exception("asked for test data; nothing received.");
										}
										else
										{
											return false;
										}
									}
									else
									{
										String[] l_strDecryptedDataArray = decryptTestData(l_strEncryptedData, l_strDecryptedKey);
										if(java.util.Arrays.equals(l_strDecryptedDataArray, null))
										{
											if(TMAndroid.g_bDebugFlag == true)
											{
												throw new Exception("test data sent; nothing returned.");
											}
											else
											{
												return false;
											}
										}
										else
										{
											// mainMenuForm.g_strDirectoryName = l_strDecryptedSavedDataArray[w++];
											mainMenuForm.g_nMenuChoice = Integer.parseInt(l_strDecryptedSavedDataArray[w++]);
											mainMenuForm.g_nQuestionOrderToggle = Integer.parseInt(l_strDecryptedSavedDataArray[w++]);
											mainMenuForm.g_nTermDisplayToggle = Integer.parseInt(l_strDecryptedSavedDataArray[w++]);
											mainMenuForm.g_nProvideFeedbackToggle = Integer.parseInt(l_strDecryptedSavedDataArray[w++]);
											mainMenuForm.g_strTempMediaNameHolder = l_strDecryptedSavedDataArray[w++];
											m_strTestTitle = l_strDecryptedDataArray[Integer.parseInt(l_strDecryptedSavedDataArray[w++])];
											m_nNumberOfQuestions = Integer.parseInt(l_strDecryptedSavedDataArray[w++]);
											m_nCount = Integer.parseInt(l_strDecryptedSavedDataArray[w++]);
											m_nNumberCorrect = Integer.parseInt(l_strDecryptedSavedDataArray[w++]);
											for(x = 0; x <= m_nNumberOfQuestions; x++)
											{
												m_strQuestion[x] = l_strDecryptedDataArray[Integer.parseInt(l_strDecryptedSavedDataArray[w++])];
												m_nMediaFlag[x] = Integer.parseInt(l_strDecryptedSavedDataArray[w++]);
												if(m_nMediaFlag[x] != 0)
												{
													m_strMediaName[x] = l_strDecryptedDataArray[Integer.parseInt(l_strDecryptedSavedDataArray[w++])];
												}
												m_nNumberOfAnswers[x] = Integer.parseInt(l_strDecryptedSavedDataArray[w++]);
												for(y = 0; y <= m_nNumberOfAnswers[x]; y++)
												{
													m_strAnswers[x][y] = l_strDecryptedDataArray[Integer.parseInt(l_strDecryptedSavedDataArray[w++])];
												}
												m_strCorrectAnswer[x] = l_strDecryptedDataArray[Integer.parseInt(l_strDecryptedSavedDataArray[w++])];
												m_strExplanation[x] = l_strDecryptedSavedDataArray[w++];
											}
											m_longStartTime = System.currentTimeMillis() - Integer.parseInt(l_strDecryptedSavedDataArray[w++]);
											return true;
										}
									}
								}
								else
								{
									if(TMAndroid.g_bDebugFlag == true)
									{
										throw new Exception("invalid saved test.");
									}
									else
									{
										return false;
									}
								}
							}
						}
					}
				}
			}
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".loadSavedTest().");
			return false;
		}
	}

	// Independent Function //
	private int[] generateRandomNumbers(int l_nMixedArrayMaxLimit, int l_nMixedArrayUpperLimit, int l_nMixedArrayKeyNumber)
	{
		try
		{
			// Implementation of the Durstenfeld's Algorithm...
			int n = 0; // The counter
			int l_nSwapIndex = 0;
			int l_nTemp = 0;
			int[] l_nMixedArray = new int[300];
			// Assign sequential numbers to the mixed array; wrap if they hit zero...
			for(n = 0; n <= l_nMixedArrayMaxLimit; n++)
			{
				if((n + l_nMixedArrayKeyNumber) <= l_nMixedArrayMaxLimit)
				{
					l_nMixedArray[n] = n + l_nMixedArrayKeyNumber;
				}
				else
				{
					l_nMixedArray[n] = ((n + l_nMixedArrayKeyNumber) - (l_nMixedArrayMaxLimit + 1));
				}
			}
			// Rearrange the array based on the upper limit desired...
			// If the max limit is less than double the upper limit, mix all except the key number (at array[0])...
			if(l_nMixedArrayMaxLimit <= l_nMixedArrayUpperLimit * 2)
			{
				for(n = 1; n <= l_nMixedArrayUpperLimit; n++)
				{
					l_nSwapIndex = (getRandom.nextInt(l_nMixedArrayMaxLimit) + 1);
					l_nTemp = l_nMixedArray[n];
					l_nMixedArray[n] = l_nMixedArray[l_nSwapIndex];
					l_nMixedArray[l_nSwapIndex] = l_nTemp;
				}
			}
			// If the max limit is greater than double the upper limit, mix only double the upper limit...
			// This is to to prevent earlier answers from appearing until the end...
			else // if(l_nMixedArrayMaxLimit > l_nMixedArrayUpperLimit * 2)
			{
				for(n = 1; n <= l_nMixedArrayUpperLimit; n++)
				{
					l_nSwapIndex = (getRandom.nextInt(l_nMixedArrayUpperLimit * 2) + 1);
					l_nTemp = l_nMixedArray[n];
					l_nMixedArray[n] = l_nMixedArray[l_nSwapIndex];
					l_nMixedArray[l_nSwapIndex] = l_nTemp;
				}
			}
			// Second swap to mix up key number...
			for(n = 0; n <= l_nMixedArrayUpperLimit; n++)
			{
				l_nSwapIndex = (getRandom.nextInt(l_nMixedArrayUpperLimit + 1));
				l_nTemp = l_nMixedArray[n];
				l_nMixedArray[n] = l_nMixedArray[l_nSwapIndex];
				l_nMixedArray[l_nSwapIndex] = l_nTemp;
			}
			return l_nMixedArray;
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".generateRandomNumbers().");
			return null;
		}
	}
	
	// Independent Function //
	private boolean prepareNewTest()
	{
		try
		{
			// Initialize OpenFile-specific variables and release the memory when complete
			// 99 per term, multiple choice and true/false, round up just in case...
			int l_nNumberOfTerms = 0;
			String[] l_strTerm = new String[100];
			int[] l_nTermMediaFlag = new int[100];
			String[] l_strTermMediaName = new String[100];
			String[] l_strTermDefinition = new String[100];
			int l_nNumberOfMC = 0;
			String[] l_strMCQuestion = new String[100];
			int[] l_nMCMediaFlag = new int[100];
			String[] l_strMCMediaName = new String[100];
			int[] l_nNumberOfMCAnswers = new int[100];
			String[][] l_strMCAnswers = new String[100][10];
			String[] l_strMCExplanation = new String[100];
			int l_nNumberOfTF = 0;
			String[] l_strTFQuestion = new String[100];
			int[] l_nTFMediaFlag = new int[100];
			String[] l_strTFMediaName = new String[100];
			String[] l_strTFAnswer = new String[100];
			String[] l_strTFExplanation = new String[100];
			int[] l_nMixedArray = new int[300];
			int[] l_n5050SplitMixedArray = new int[100];
			int[] l_nQuestionMixedArray = new int[300];
			int[][] l_nAnswerMixedArray = new int[300][10];
			int l_nMixedArrayMaxLimit = 0; // The pool of items to be randomized, i.e., 65 terms for IBADrinks...
			int l_nMixedArrayUpperLimit = 0; // The number of items from the pool selected, i.e., 4 terms out of 65 from IBADrinks...
			int l_nMixedArrayKeyNumber = 0; // An item that must appear, i.e., the correct answer, or -1...
			int w = 2; // Starts count after TO Number and verifying code is read...
			int x = 0;
			int y = 0;
			int z = 0;
			String l_strEncryptedData = readFileContents("/sdcard/tm/tm.tp");
			if(l_strEncryptedData == null)
			{
				if(TMAndroid.g_bDebugFlag == true)
				{
					throw new Exception("asked for profile data; nothing received.");
				}
				else
				{
					return false;
				}
			}
			else
			{
				String l_strDecryptedKey = decryptProfile(l_strEncryptedData);
				if(l_strDecryptedKey == null)
				{
					if(TMAndroid.g_bDebugFlag == true)
					{
						throw new Exception("profile data sent; nothing returned.");
					}
					else
					{
						return false;
					}
				}
				else
				{
					l_strEncryptedData = readFileContents(mainMenuForm.g_strDirectoryName + "/" + mainMenuForm.g_strFileName);
					if(l_strEncryptedData == null)
					{
						if(TMAndroid.g_bDebugFlag == true)
						{
							throw new Exception("asked for test data; nothing received.");
						}
						else
						{
							return false;
						}
					}
					else
					{
						String[] l_strDecryptedDataArray = decryptTestData(l_strEncryptedData, l_strDecryptedKey);
						// length is not zero-based. Minimum good file dimensions are 6 (TO#, DB Ver, Title, #Terms, #MC and #MC)
						if(java.util.Arrays.equals(l_strDecryptedDataArray, null) || l_strDecryptedDataArray.length < 6)
						{
							if(TMAndroid.g_bDebugFlag == true)
							{
								throw new Exception("test data sent; nothing returned.");
							}
							else
							{
								return false;
							}
						}
						else
						{
                            // CHANGE //
                            // Remove BOM (EF BB BF (or) #65279) if present and Throw-Off Number!
                            // Read and check database verification data (spells "Rob")...
                            if (l_strDecryptedDataArray[0].equals("01010010") || l_strDecryptedDataArray[0].equals("01001111") || l_strDecryptedDataArray[0].equals("01000010"))
                            {
                                w = 1;
                            }
                            else if (l_strDecryptedDataArray[1].equals("01010010") || l_strDecryptedDataArray[1].equals("01001111") || l_strDecryptedDataArray[1].equals("01000010"))
                            {
                                w = 2;
                            }
                            else
                            {
                                w = 0;
                            }

                            if (w == 1 || w == 2)
                            // CHANGE //
							{
								// Read the test title...
								m_strTestTitle = l_strDecryptedDataArray[w++];
								// Check for terms and read...
								l_nNumberOfTerms = Integer.parseInt(l_strDecryptedDataArray[w++]);
								if(l_nNumberOfTerms != -1)
								{
									for(x = 0; x <= l_nNumberOfTerms; x++)
									{
										l_strTerm[x] = l_strDecryptedDataArray[w++];
										l_nTermMediaFlag[x] = Integer.parseInt(l_strDecryptedDataArray[w++]);
										if(l_nTermMediaFlag[x] >= 1 && l_nTermMediaFlag[x] <= 3)
										{
											l_strTermMediaName[x] = l_strDecryptedDataArray[w++];
										}
										else
										{
											// Continue...
										}
										l_strTermDefinition[x] = l_strDecryptedDataArray[w++];
										z++; // Add to total question count...
									}
								}
								else
								{
									// Continue...
								}
								//  Check for multiple choice questions and read...
								l_nNumberOfMC = Integer.parseInt(l_strDecryptedDataArray[w++]);
								if(l_nNumberOfMC != -1)
								{
									for(x = 0; x <= l_nNumberOfMC; x++)
									{
										l_strMCQuestion[x] = l_strDecryptedDataArray[w++];
										l_nMCMediaFlag[x] = Integer.parseInt(l_strDecryptedDataArray[w++]);
										if(l_nMCMediaFlag[x] >= 1 && l_nMCMediaFlag[x] <= 3)
										{
											l_strMCMediaName[x] = l_strDecryptedDataArray[w++];
										}
										else
										{
											// Continue...
										}
										l_nNumberOfMCAnswers[x] = Integer.parseInt(l_strDecryptedDataArray[w++]);
										for(y = 0; y <= l_nNumberOfMCAnswers[x]; y++)
										{
											l_strMCAnswers[x][y] = l_strDecryptedDataArray[w++];
										}
										l_strMCExplanation[x] = l_strDecryptedDataArray[w++];
										z++; // Add to total question count...
									}
								}
								else
								{
									// Continue...
								}
								// Check for true or false questions and read...
								l_nNumberOfTF = Integer.parseInt(l_strDecryptedDataArray[w++]);
								if(l_nNumberOfTF != -1)
								{
									for(x = 0; x <= l_nNumberOfTF; x++)
									{
										l_strTFQuestion[x] = l_strDecryptedDataArray[w++];
										l_nTFMediaFlag[x] = Integer.parseInt(l_strDecryptedDataArray[w++]);
										if(l_nTFMediaFlag[x] >= 1 && l_nTFMediaFlag[x] <= 3)
										{
											l_strTFMediaName[x] = l_strDecryptedDataArray[w++];
										}
										else
										{
											// Continue...
										}
										l_strTFAnswer[x] = l_strDecryptedDataArray[w++];
										l_strTFExplanation[x] = l_strDecryptedDataArray[w++];
										z++; // Add to total question count...
									}
								}
								else
								{
									// Continue...
								}
								m_nNumberOfQuestions = z - 1;
								x = 0;
								y = 0;
								z = 0;
							}
							else
							{
								if(TMAndroid.g_bDebugFlag == true)
								{
									throw new Exception("invalid test.");
								}
								else
								{
									return false;
								}
							}
							
							/*
							// End of actually reading the file, next is preparing the test...
							// The following subroutines are system dependent...
							// END SYSTEM DEPENDENT
							*/

							// Randomize question order if toggle is on...
							if(mainMenuForm.g_nQuestionOrderToggle == 1)
							{
								l_nMixedArray = generateRandomNumbers(m_nNumberOfQuestions, m_nNumberOfQuestions, 0);
								if(l_nMixedArray == null)
								{
									if(TMAndroid.g_bDebugFlag == true)
									{
										throw new Exception("FATAL ERROR: Cannot generate number of questions.");
									}
									else
									{
										return false;
									}
								}
								else
								{
									for(x = 0; x <= m_nNumberOfQuestions; x++)
									{
										l_nQuestionMixedArray[x] = l_nMixedArray[x];
									}
								}
							}
							else
							{
								for(x = 0; x <= m_nNumberOfQuestions; x++)
								{
									l_nQuestionMixedArray[x] = x;
								}
							}
							// Assign terms to global matrix
							if(l_nNumberOfTerms != -1)
							{
								// Generate a random number matrix for 50/50 Split term choice
								l_nMixedArray = generateRandomNumbers(l_nNumberOfTerms, l_nNumberOfTerms, 0);
								if(l_nMixedArray == null)
								{
									if(TMAndroid.g_bDebugFlag == true)
									{
										throw new Exception("FATAL ERROR: Cannot generate number of terms.");
									}
									else
									{
										return false;
									}
								}
								else
								{
									for(x = 0; x <= l_nNumberOfTerms; x++)
									{
										l_n5050SplitMixedArray[x] = (l_nMixedArray[x] % 2);
									}
									// Assign terms to global matrix
									for(x = 0; x <= l_nNumberOfTerms; x++)
									{
										m_nMediaFlag[l_nQuestionMixedArray[z]] = l_nTermMediaFlag[x];
										m_strMediaName[l_nQuestionMixedArray[z]] = l_strTermMediaName[x];
										if(l_nNumberOfTerms < 3)
										{
											m_nNumberOfAnswers[l_nQuestionMixedArray[z]] = l_nNumberOfTerms;
										}
										else
										{
											m_nNumberOfAnswers[l_nQuestionMixedArray[z]] = 3;
										}
										l_nMixedArray = generateRandomNumbers(m_nNumberOfAnswers[l_nQuestionMixedArray[z]], m_nNumberOfAnswers[l_nQuestionMixedArray[x]], 0);
										if(l_nMixedArray == null)
										{
											if(TMAndroid.g_bDebugFlag == true)
											{
												throw new Exception("FATAL ERROR: Cannot generate number of answers.");
											}
											else
											{
												return false;
											}
										}
										else
										{
											for(y = 0; y <= m_nNumberOfAnswers[l_nQuestionMixedArray[z]]; y++)
											{
												l_nAnswerMixedArray[l_nQuestionMixedArray[z]][y] = l_nMixedArray[y];
											}
											l_nMixedArrayMaxLimit = l_nNumberOfTerms;
											l_nMixedArrayUpperLimit = m_nNumberOfAnswers[l_nQuestionMixedArray[z]];
											l_nMixedArrayKeyNumber = x;
											l_nMixedArray = generateRandomNumbers(l_nMixedArrayMaxLimit, l_nMixedArrayUpperLimit, l_nMixedArrayKeyNumber);
											if(l_nMixedArray == null)
											{
												if(TMAndroid.g_bDebugFlag == true)
												{
													throw new Exception("FATAL ERROR: Cannot generate random numbers.");
												}
												else
												{
													return false;
												}
											}
											else
											{
												// Assign term answers as request by user
												switch(mainMenuForm.g_nTermDisplayToggle)
												{
													case 0: // Question = term
													{
														m_strQuestion[l_nQuestionMixedArray[z]] = l_strTerm[x];
														for(y = 0; y <= m_nNumberOfAnswers[l_nQuestionMixedArray[z]]; y++)
														{
															m_strAnswers[l_nQuestionMixedArray[z]][l_nAnswerMixedArray[l_nQuestionMixedArray[z]][y]] = l_strTermDefinition[l_nMixedArray[y]];
															m_strCorrectAnswer[l_nQuestionMixedArray[z]] = l_strTermDefinition[x];
														}
														break;
													}
													case 1: // Question = definition
													{
														m_strQuestion[l_nQuestionMixedArray[z]] = l_strTermDefinition[x];
														for(y = 0; y <= m_nNumberOfAnswers[l_nQuestionMixedArray[z]]; y++)
														{
															m_strAnswers[l_nQuestionMixedArray[z]][l_nAnswerMixedArray[l_nQuestionMixedArray[z]][y]] = l_strTerm[l_nMixedArray[y]];
															m_strCorrectAnswer[l_nQuestionMixedArray[z]] = l_strTerm[z];
														}
														break;
													}
													case 2: // Mix it up...
													{
														if(l_n5050SplitMixedArray[x] == 0)
														{
															m_strQuestion[l_nQuestionMixedArray[z]] = l_strTerm[x];
															for(y = 0; y <= m_nNumberOfAnswers[l_nQuestionMixedArray[z]]; y++)
															{
																m_strAnswers[l_nQuestionMixedArray[z]][l_nAnswerMixedArray[l_nQuestionMixedArray[z]][y]] = l_strTermDefinition[l_nMixedArray[y]];
																m_strCorrectAnswer[l_nQuestionMixedArray[z]] = l_strTermDefinition[x];
															}
														}
														else
														{
															m_strQuestion[l_nQuestionMixedArray[z]] = l_strTermDefinition[x];
															for(y = 0; y <= m_nNumberOfAnswers[l_nQuestionMixedArray[z]]; y++)
															{
																m_strAnswers[l_nQuestionMixedArray[z]][l_nAnswerMixedArray[l_nQuestionMixedArray[z]][y]] = l_strTerm[l_nMixedArray[y]];
																m_strCorrectAnswer[l_nQuestionMixedArray[z]] = l_strTerm[x];
															}
														}
														break;
													}
													default:
													{
														if(TMAndroid.g_bDebugFlag == true)
														{
															throw new Exception("cannot determine how to display terms.");
														}
														else
														{
															return false;
														}
													}
												}

												// CHANGE //
												String l_strTemp = l_strTerm[x] + ": " + l_strTermDefinition[x];
												// CHANGE //

												m_strExplanation[l_nQuestionMixedArray[z]] = l_strTemp;
												z++;
											}
										}
									}
								}
							}
							// Assign multiple choice questions to global matrix
							if(l_nNumberOfMC != -1)
							{
								for(x = 0; x <= l_nNumberOfMC; x++)
								{
									m_strQuestion[l_nQuestionMixedArray[z]] = l_strMCQuestion[x];
									m_nMediaFlag[l_nQuestionMixedArray[z]] = l_nMCMediaFlag[x];
									m_strMediaName[l_nQuestionMixedArray[z]] = l_strMCMediaName[x];
									if(l_nNumberOfMCAnswers[x] < 3)
									{
										m_nNumberOfAnswers[l_nQuestionMixedArray[z]] = l_nNumberOfMCAnswers[x];
									}
									else
									{
										m_nNumberOfAnswers[l_nQuestionMixedArray[z]] = 3;
									}
									l_nMixedArray = generateRandomNumbers(m_nNumberOfAnswers[l_nQuestionMixedArray[z]], m_nNumberOfAnswers[l_nQuestionMixedArray[x]], 0);
									if(l_nMixedArray == null)
									{
										if(TMAndroid.g_bDebugFlag == true)
										{
											throw new Exception("FATAL ERROR: Cannot generate number of answers.");
										}
										else
										{
											return false;
										}
									}
									else
									{
										for(y = 0; y <= m_nNumberOfAnswers[l_nQuestionMixedArray[z]]; y++)
										{
											l_nAnswerMixedArray[l_nQuestionMixedArray[z]][y] = l_nMixedArray[y];
										}
										l_nMixedArrayMaxLimit = l_nNumberOfMCAnswers[x];
										l_nMixedArrayUpperLimit = m_nNumberOfAnswers[l_nQuestionMixedArray[z]];
										l_nMixedArrayKeyNumber = 0;
										l_nMixedArray = generateRandomNumbers(l_nMixedArrayMaxLimit, l_nMixedArrayUpperLimit, l_nMixedArrayKeyNumber);
										if(l_nMixedArray == null)
										{
											if(TMAndroid.g_bDebugFlag == true)
											{
												throw new Exception("FATAL ERROR: Cannot generate random numbers.");
											}
											else
											{
												return false;
											}
										}
										else
										{
											for(y = 0; y <= m_nNumberOfAnswers[l_nQuestionMixedArray[z]]; y++)
											{
												m_strAnswers[l_nQuestionMixedArray[z]][y] = l_strMCAnswers[x][l_nMixedArray[y]];
											}
											m_strCorrectAnswer[l_nQuestionMixedArray[z]] = l_strMCAnswers[x][0];
											m_strExplanation[l_nQuestionMixedArray[z]] = l_strMCExplanation[x];
											z++;
										}
									}
								}
							}
							else
				            {
				                // Continue...
				            }
							// Assign True or False questions to global matrix
							if(l_nNumberOfTF != -1)
							{
								for(x = 0; x <= l_nNumberOfTF; x++)
								{
									m_strQuestion[l_nQuestionMixedArray[z]] = l_strTFQuestion[x];
									m_nMediaFlag[l_nQuestionMixedArray[z]] = l_nTFMediaFlag[x];
									m_strMediaName[l_nQuestionMixedArray[z]] = l_strTFMediaName[x];
									m_nNumberOfAnswers[l_nQuestionMixedArray[z]] = 1;
									// The first choice displayed is always True
									m_strAnswers[l_nQuestionMixedArray[z]][0] = "True.";
									m_strAnswers[l_nQuestionMixedArray[z]][1] = "False.";
									m_strCorrectAnswer[l_nQuestionMixedArray[z]] = l_strTFAnswer[x];
									m_strExplanation[l_nQuestionMixedArray[z]] = l_strTFExplanation[x];
									z++;
								}
							}
							else
				            {
				                // Continue...
				            }
							m_nCount = 0;
							return true;
						}
					}
				}
			}
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".prepareNewTest().");
			return false;
		}
	}
	
	// Independent Function //
	private boolean saveCurrentTest(String l_strSavedFileName)
	{
		try
		{
			if(l_strSavedFileName == null)
			{
				if(TMAndroid.g_bDebugFlag == true)
				{
					throw new Exception("no data received.");
				}
				else
				{
					return false;
				}
			}
			else
			{
				String l_strEncryptedData = readFileContents("/sdcard/tm/tm.tp");
				if(l_strEncryptedData == null)
				{
					if(TMAndroid.g_bDebugFlag == true)
					{
						throw new Exception("asked for profile data; nothing received.");
					}
					else
					{
						return false;
					}
				}
				else
				{
					String l_strDecryptedKey = decryptProfile(l_strEncryptedData);
					if(l_strDecryptedKey == null)
					{
						if(TMAndroid.g_bDebugFlag == true)
						{
							throw new Exception("profile data sent; nothing returned.");
						}
						else
						{
							return false;
						}
					}
					else
					{
						l_strEncryptedData = readFileContents(mainMenuForm.g_strDirectoryName + "/" + mainMenuForm.g_strFileName);
						if(l_strEncryptedData == null)
						{
							if(TMAndroid.g_bDebugFlag == true)
							{
								throw new Exception("asked for test data; nothing received.");
							}
							else
							{
								return false;
							}
						}
						else
						{
							String[] l_strDecryptedDataArray = decryptTestData(l_strEncryptedData, l_strDecryptedKey);
							if(l_strDecryptedDataArray == null)
							{
								if(TMAndroid.g_bDebugFlag == true)
								{
									throw new Exception("test data sent; nothing returned.");
								}
								else
								{
									return false;
								}
							}
							else
							{
								Vector<String> l_strSavedFileVector = new Vector<String>();
								int x = 0;
								int y = 0;
								int z = 0;
								// CHANGE //
                                if (mainMenuForm.g_strFileName.endsWith(".tdu"))
                                {
                                    // Continue, do not add validation code...
                                }
                                else if (mainMenuForm.g_strFileName.endsWith(".td"))
                                {
                                    switch (getRandom.nextInt(3) + 1)
                                    {
                                        case 1:
                                            {
                                                l_strSavedFileVector.addElement("01010010");
                                                break;
                                            }
                                        case 2:
                                            {
                                                l_strSavedFileVector.addElement("01001111");
                                                break;
                                            }
                                        case 3:
                                            {
                                                l_strSavedFileVector.addElement("01000010");
                                                break;
                                            }
                                        default:
                                            {
                                                if (TMAndroid.g_bDebugFlag == true)
                                                {
                                                    throw new Exception("cannot generate a random number.");
                                                }
                                                else
                                                {
                                                    return false;
                                                }
                                            }
                                    }
                                }
                                // CHANGE //

                                l_strSavedFileVector.addElement(mainMenuForm.g_strFileName);
								// l_strSavedFileVector.addElement(mainMenuForm.g_strDirectoryName);
								l_strSavedFileVector.addElement(Integer.toString(mainMenuForm.g_nMenuChoice));
								l_strSavedFileVector.addElement(Integer.toString(mainMenuForm.g_nQuestionOrderToggle));
								l_strSavedFileVector.addElement(Integer.toString(mainMenuForm.g_nTermDisplayToggle));
								l_strSavedFileVector.addElement(Integer.toString(mainMenuForm.g_nProvideFeedbackToggle));
								l_strSavedFileVector.addElement(mainMenuForm.g_strTempMediaNameHolder);
								while(z <= l_strDecryptedDataArray.length)
								{
									if(l_strDecryptedDataArray[z].equals(m_strTestTitle))
									{
										l_strSavedFileVector.addElement(Integer.toString(z));
										z = l_strDecryptedDataArray.length + 1;
									}
									else
									{
										z++;
									}
								}
								if(l_strSavedFileVector.lastElement() == null)
								{
									if(TMAndroid.g_bDebugFlag == true)
									{
										throw new Exception("Could not synchronize with actual test.");
									}
									else
									{
										return false;
									}
								}
								else
								{
									l_strSavedFileVector.addElement(Integer.toString(m_nNumberOfQuestions));
									l_strSavedFileVector.addElement(Integer.toString(m_nCount));
									l_strSavedFileVector.addElement(Integer.toString(m_nNumberCorrect));
									for(x = 0; x <= m_nNumberOfQuestions; x++)
									{
										z = 0;
										while(z <= l_strDecryptedDataArray.length)
										{
											if(l_strDecryptedDataArray[z].equals(m_strQuestion[x]))
											{
												l_strSavedFileVector.addElement(Integer.toString(z));
												z = l_strDecryptedDataArray.length + 1;
											}
											else
											{
												z++;
											}
										}
										if(l_strSavedFileVector.lastElement() == null)
										{
											if(TMAndroid.g_bDebugFlag == true)
											{
												throw new Exception("Could not synchronize with actual test.");
											}
											else
											{
												return false;
											}
										}
										else
										{
											l_strSavedFileVector.addElement(Integer.toString(m_nMediaFlag[x]));
											if(m_nMediaFlag[x] != 0)
											{
												z = 0;
												while(z <= l_strDecryptedDataArray.length)
												{
													if(l_strDecryptedDataArray[z].equals(m_strMediaName[x]))
													{
														l_strSavedFileVector.addElement(Integer.toString(z));
														z = l_strDecryptedDataArray.length + 1;
													}
													else
													{
														z++;
													}
												}
												if(l_strSavedFileVector.lastElement() == null)
												{
													if(TMAndroid.g_bDebugFlag == true)
													{
														throw new Exception("Could not synchronize with actual test.");
													}
													else
													{
														return false;
													}
												}
                                                else
                                                {
                                                    // Continue...
                                                }
											}
											else
											{
												// Continue...
											}
											l_strSavedFileVector.addElement(Integer.toString(m_nNumberOfAnswers[x]));
											for(y = 0; y <= m_nNumberOfAnswers[x]; y++)
											{
												z = 0;
												while(z <= l_strDecryptedDataArray.length)
												{
													if(l_strDecryptedDataArray[z].equals(m_strAnswers[x][y]))
													{
														l_strSavedFileVector.addElement(Integer.toString(z));
														z = l_strDecryptedDataArray.length + 1;
													}
													else
													{
														z++;
													}
												}
												if(l_strSavedFileVector.lastElement() == null)
												{
													if(TMAndroid.g_bDebugFlag == true)
													{
														throw new Exception("Could not synchronize with actual test.");
													}
													else
													{
														return false;
													}
												}
                                                else
                                                {
                                                    // Continue...
                                                }
											}
											z = 0;
											while(z <= l_strDecryptedDataArray.length)
											{
												if(l_strDecryptedDataArray[z].equals(m_strCorrectAnswer[x]))
												{
													l_strSavedFileVector.addElement(Integer.toString(z));
													z = l_strDecryptedDataArray.length + 1;
												}
												else
												{
													z++;
												}
											}
											if(l_strSavedFileVector.lastElement() == null)
											{
												if(TMAndroid.g_bDebugFlag == true)
												{
													throw new Exception("Could not synchronize with actual test.");
												}
												else
												{
													return false;
												}
											}
                                            else
                                            {
                                                // Continue...
                                            }
											// Done separately since the explanation is different for each type of question...
											l_strSavedFileVector.addElement(m_strExplanation[x]);
										}
									}
									l_strSavedFileVector.addElement(Long.toString(System.currentTimeMillis() - m_longStartTime));
									String l_strUnEncryptedText = "";
									for(x = 0; x < l_strSavedFileVector.size(); x++)
									{
										l_strUnEncryptedText += l_strSavedFileVector.elementAt(x) + "\r\n";
									}

									// CHANGE //
                                    String l_strEncryptedText = "";
                                    if (mainMenuForm.g_strFileName.endsWith(".tdu"))
                                    {
                                        l_strEncryptedText = l_strUnEncryptedText;
                                    }
                                    else if (mainMenuForm.g_strFileName.endsWith(".td"))
                                    {
                                        l_strEncryptedText = cryptThis(l_strUnEncryptedText, l_strDecryptedKey);
                                    }
                                    // CHANGE //

									if(l_strEncryptedText == null)
									{
										if(TMAndroid.g_bDebugFlag == true)
										{
											throw new Exception("data sent to subroutine; nothing returned.");
										}
										else
										{
											return false;
										}
									}
									else
									{
										l_strEncryptedText += '\u0000';
										if(writeFileContents(mainMenuForm.g_strDirectoryName + "/" + l_strSavedFileName, l_strEncryptedText) == false)
										{
											if(TMAndroid.g_bDebugFlag == true)
											{
												throw new Exception("data sent to disk; nothing returned.");
											}
											else
											{
												return false;
											}
										}
                                        else
                                        {
                                        	return true;
                                        }
									}
								}
							}
						}
					}
				}
			}
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			handleError(exception.toString(), getClass().toString() + ".saveCurrentTest().");
			return false;
		}
	}
}