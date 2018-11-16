package com.whystudyalone.tmandroid;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
 
/** @author Steven Osborn - http://steven.bitsetters.com **/
public class IconifiedTextView extends LinearLayout
{
	private TextView mText;
	private ImageView mIcon;
	
	public IconifiedTextView(Context context, IconifiedText aIconifiedText)
	{
		super(context);
		// First Icon and the Text to the right (horizontal), not above and below (vertical)
		this.setOrientation(HORIZONTAL);
		this.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		mIcon = new ImageView(context);
		mIcon.setImageDrawable(aIconifiedText.getIcon());
		mIcon.setPadding(5, 0, 0, 0);
		// At first, add the Icon to ourself (! we are extending LinearLayout) //
		addView(mIcon,  new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mText = new TextView(context);
		mText.setText(aIconifiedText.getText());
		mText.setPadding(5, 0, 0, 0);
		// Now the text (after the icon) //
		addView(mText, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}
	
	@Override
	public void onAttachedToWindow()
    {
        super.onAttachedToWindow();
		int l_nHeight = (int)mText.getTextSize() * 3;
		this.setMinimumHeight(l_nHeight);
	}
	
	public void setText(String words)
	{
		mText.setText(words);
	}
	
	public void setIcon(Drawable bullet)
	{
		mIcon.setImageDrawable(bullet);
	}
}

