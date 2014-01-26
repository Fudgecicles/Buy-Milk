package com.example.remindmehere;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Reminder {
	
	private String strText;
	private boolean boolNotify;
	private int intId;
	
	public Reminder(String text, boolean notify, int id, int button)
	{
		strText = text;
		boolNotify = notify;
		setId(id);
	}
	
	public Reminder(String text, int id)
	{
		strText = text;
		boolNotify = false;
		this.intId = id;
	}
	
	public Reminder(String text)
	{
		strText = text;
		boolNotify = false;
		setId(-1);
	}
	
	public String getText()
	{
		return strText;
	}

	public void setText(String newText)
	{
		strText = newText;
	}
	
	public boolean getNotify()
	{
		return boolNotify;
	}
	
	public void setNotify(boolean newNotify)
	{
		boolNotify = newNotify;
	}

	public int getId() {
		return intId;
	}

	public void setId(int intId) {
		this.intId = intId;
	}

}
