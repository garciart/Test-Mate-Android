package com.whystudyalone.tmandroid;

import android.graphics.drawable.Drawable;

/** @author Steven Osborn - http://steven.bitsetters.com **/
public class IconifiedText implements Comparable<IconifiedText>
{
    // Thanks to plusminus at http://www.anddev.org/
	private String mText = "";
	private Drawable mIcon;
	private boolean mSelectable = true;

	public IconifiedText(String text, Drawable bullet)
	{
		mIcon = bullet;
		mText = text;
	}

	public boolean isSelectable()
	{
		return mSelectable;
	}
	
	public void setSelectable(boolean selectable)
	{
		mSelectable = selectable;
	}
	
	public String getText()
	{
		return mText;
	}
	
	public void setText(String text)
	{
		mText = text;
	}
	
	public void setIcon(Drawable icon)
	{
		mIcon = icon;
	}
	
	public Drawable getIcon()
	{
		return mIcon;
	}
	
	/* Make IconifiedText comparable by its name */
	@Override
	public int compareTo(IconifiedText other)
	{
		if(this.mText != null)
		{
			return this.mText.compareTo(other.getText());
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}
}