package com.whystudyalone.tmandroid;

import java.util.ArrayList;
import java.util.List;
 
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
 
/** @author Steven Osborn - http://steven.bitsetters.com **/
public class IconifiedTextListAdapter extends BaseAdapter
{
    // Thanks to plusminus at http://www.anddev.org/
	/* Remember our context so we can use it when constructing views. */
	private Context mContext;
	private List<IconifiedText> mItems = new ArrayList<IconifiedText>();

	public IconifiedTextListAdapter(Context context)
	{
		mContext = context;
	}
	
	public void addItem(IconifiedText it)
	{
		mItems.add(it);
	}

	public void setListItems(List<IconifiedText> lit)
	{
		mItems = lit;
	}

	public int getCount()
	{
		return mItems.size();
	}

	public Object getItem(int position)
	{
		return mItems.get(position);
	}

	public boolean areAllItemsSelectable()
	{
		return false;
	}

	public boolean isSelectable(int position)
	{
		try
		{
			return mItems.get(position).isSelectable();
		}
		catch (IndexOutOfBoundsException aioobe)
		{
			return isSelectable(position);
		}
	}
	
	/* Use the array index as a unique id. */
	public long getItemId(int position)
	{
		return position;
	}
	
	/* @param convertView The old view to overwrite, if one is passed
	 * @returns a IconifiedTextView that holds wraps around an IconifiedText */
	public View getView(int position, View convertView, ViewGroup parent)
	{
		return new IconifiedTextView(mContext, mItems.get(position));
	}
}
