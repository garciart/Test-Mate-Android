package com.whystudyalone.tmandroid;

import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class mainMenuForm extends Activity implements OnClickListener
{
	// Global variables //
	public static int g_nQuestionOrderToggle = 0; // Administer questions in random order? Default is no...
	public static int g_nTermDisplayToggle = 0; // Display terms as question, definition as question or mix it up? Default is term as question...
	public static int g_nProvideFeedbackToggle = 1; // Provide feedback after answering? Default is yes...
	public static boolean g_boolOpenFileDialog = true;
	public static int g_nMenuChoice = 0;
	public static int g_nTempMediaFlagHolder = 0; // Used to reduce memory usage (instead of sending the array)...
	public static String g_strDirectoryName = "";
	public static String g_strFileName = "";
	public static String g_strTempMediaNameHolder = ""; // Used to reduce memory usage...
	public static String g_strUserName = ""; // Load from mainMenuForm for a more personal experience...

	// Declare the components here since all functions in this class will need access...
	ImageView IDC_MAIN_PICTUREBOX;
	Button IDC_MAIN_BUTTON_STARTANEWTEST;
	Button IDC_MAIN_BUTTON_CONTINUEATEST;
	Button IDC_MAIN_BUTTON_ABOUTTESTMATE;
	Button IDC_MAIN_BUTTON_EXITTESTMATE;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		try
		{
	    	this.setTheme(android.R.style.Theme_Light);
			setContentView(R.layout.mainmenuform);
	        IDC_MAIN_PICTUREBOX = (ImageView) this.findViewById(R.id.IDC_MAIN_PICTUREBOX);
	        IDC_MAIN_BUTTON_STARTANEWTEST = (Button) this.findViewById(R.id.IDC_MAIN_BUTTON_STARTANEWTEST);
			IDC_MAIN_BUTTON_CONTINUEATEST = (Button) this.findViewById(R.id.IDC_MAIN_BUTTON_CONTINUEATEST);
			IDC_MAIN_BUTTON_ABOUTTESTMATE = (Button) this.findViewById(R.id.IDC_MAIN_BUTTON_ABOUTTESTMATE);
			IDC_MAIN_BUTTON_EXITTESTMATE = (Button) this.findViewById(R.id.IDC_MAIN_BUTTON_EXITTESTMATE);
			IDC_MAIN_BUTTON_STARTANEWTEST.setOnClickListener(this);
			IDC_MAIN_BUTTON_CONTINUEATEST.setOnClickListener(this);
			IDC_MAIN_BUTTON_ABOUTTESTMATE.setOnClickListener(this);
			IDC_MAIN_BUTTON_EXITTESTMATE.setOnClickListener(this);
			if(TMAndroid.g_bTMStarted == false)
			{
				mainMenuForm.g_strUserName = getUserName();
				if(mainMenuForm.g_strUserName == null)
				{
		    		final Builder builder = new AlertDialog.Builder(this);
		    		builder.setTitle("Test Mate");
		    		builder.setIcon(R.drawable.tmicon48);
		    		builder.setMessage("Unable to open your profile!");
		    		builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener()
		    	    {
		    			@Override
		    			public void onClick(DialogInterface dialog, int which)
		    	        {
		    		    	builder.setTitle("Test Mate");
		    		    	builder.setIcon(R.drawable.tmicon48);
		    		    	builder.setMessage("Please contact us at customerservice@whystudyalone.com as soon as possible.");
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
				else
				{
		    		final Builder builder = new AlertDialog.Builder(this);
		    		builder.setTitle("Test Mate");
		    		builder.setIcon(R.drawable.tmicon48);
		    		builder.setMessage("Welcome back, " + mainMenuForm.g_strUserName + "!");
		    		builder.setPositiveButton("OK", null);
		    		builder.show();
					TMAndroid.g_bTMStarted = true;
				}
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
		// TODO Auto-generated method stub
		try
		{
			switch(v.getId())
			{
				case R.id.IDC_MAIN_BUTTON_STARTANEWTEST:
				{
					g_nMenuChoice = 0;
					Intent i = new Intent(this, myOpenFileDialog.class);
					startActivity(i);
					break;
				}
				case R.id.IDC_MAIN_BUTTON_CONTINUEATEST:
				{
					g_nMenuChoice = 1;
					Intent i = new Intent(this, myOpenFileDialog.class);
					startActivity(i);
					break;
				}
				case R.id.IDC_MAIN_BUTTON_ABOUTTESTMATE:
				{
					g_nMenuChoice = 2;
					Intent i = new Intent(this, aboutForm.class);
					startActivity(i);
					break;
				}
				case R.id.IDC_MAIN_BUTTON_EXITTESTMATE:
				{
					g_nMenuChoice = 3;
		    		final Builder builder = new AlertDialog.Builder(this);
		    		builder.setTitle("Test Mate");
		    		builder.setIcon(R.drawable.tmicon48);
		    		builder.setMessage("Are you sure you want to quit?");
		    		builder.setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener()
		    		{
		    			public void onClick(DialogInterface dialog, int which)
		    			{
		    				finish();
	    				}
	    			});
		    		builder.setNegativeButton("No", null);
		    		builder.show();
					break;
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
	    		final Builder builder = new AlertDialog.Builder(this);
	    		builder.setTitle("Test Mate");
	    		builder.setIcon(R.drawable.tmicon48);
	    		builder.setMessage("Are you sure you want to quit?");
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
	
	// Device Dependent Method //
	private String getUserName()
	{
		try
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
					return null;
				}
			}
			else
			{
				String l_strUserName = decryptProfile(l_strEncryptedData);
				if(l_strUserName == null)
				{
					if(TMAndroid.g_bDebugFlag == true)
					{
						throw new Exception("profile data sent; nothing returned.");
					}
					else
					{
						return null;
					}
				}
				else
				{
					l_strUserName = l_strUserName.substring(l_strUserName.indexOf('\u00A7') + 1, l_strUserName.indexOf('.'));
					return l_strUserName;
				}
			}
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			if(TMAndroid.g_bDebugFlag == true)
			{
				handleError(exception.toString(), getClass().toString() + ".getUserName().");
				return null;
			}
			else
			{
				return null;
			}
		}
	}

	// Device Dependent Method //
	private String readFileContents(String l_strFileName)
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
				// Replaces using statement in C#
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
						return null;
					}
					// If verbose is false, do not handle e; return to the main program...
					else
					{
						return null;
					}
				}
			}
		}
		catch(Exception exception)
		{
			// ANDROID DOES NOT SUPPORT REFLECTION!!!
			if(TMAndroid.g_bDebugFlag == true)
			{
				handleError(exception.toString(), getClass().toString() + ".readFileContents().");
				return null;
			}
			else
			{
				return null;
			}
		}
	}
			
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
			if(TMAndroid.g_bDebugFlag == true)
			{
				handleError(exception.toString(), getClass().toString() + ".cryptThis().");
				return null;
			}
			else
			{
				return null;
			}
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
			if(TMAndroid.g_bDebugFlag == true)
			{
				handleError(exception.toString(), getClass().toString() + ".decryptProfile().");
				return null;
			}
			else
			{
				return null;
			}
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