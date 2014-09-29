package com.webuyforyou.webuyforyou;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

public class Utility {
	public static ArrayList<String> nameOfEvent = new ArrayList<String>();
	public static ArrayList<String> startDates = new ArrayList<String>();
	public static ArrayList<String> endDates = new ArrayList<String>();
	public static ArrayList<String> descriptions = new ArrayList<String>();

	public static ArrayList<String> readCalendarEvent(Context context) {
		Cursor cursor = context.getContentResolver()
				.query(Uri.parse("content://com.android.calendar/events"),
						new String[] { "calendar_id", "title", "description",
								"dtstart", "dtend", "eventLocation" }, null,
						null, null);
		if(cursor!=null){
		cursor.moveToFirst();
		// fetching calendars name
		String CNames[] = new String[cursor.getCount()];

		// fetching calendars id
		nameOfEvent.clear();
		startDates.clear();
		endDates.clear();
		descriptions.clear();
		for (int i = 0; i < CNames.length; i++) {

			if(cursor.getString(1)!=null)
			nameOfEvent.add(cursor.getString(1));
			if(cursor.getString(3)!=null)
//			startDates.add(getDate(Long.parseLong(cursor.getString(3))));
			if(cursor.getString(4)!=null)
//			endDates.add(getDate(Long.parseLong(cursor.getString(4))));
			if(cursor.getString(3)!=null)
				startDates.add(cursor.getString(3));
			else
				startDates.add("");
			if(cursor.getString(4)!=null)
			endDates.add(cursor.getString(4));
			else
				endDates.add("");
			descriptions.add(cursor.getString(2));
			CNames[i] = cursor.getString(1);
			cursor.moveToNext();

		}
		
		Collections.sort(startDates);

		cursor.close();
		}
		else
		{
			Toast.makeText(context, "Calendar don have entries", Toast.LENGTH_LONG).show();
		}
		return nameOfEvent;
	}
	
	class StringDateComparator implements Comparator<String>
	{
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	    public int compare(String lhs, String rhs)
	    {
	        try {
				return dateFormat.parse(lhs).compareTo(dateFormat.parse(rhs));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
	    }
	}


	public static String getDate(long milliSeconds) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}
}
