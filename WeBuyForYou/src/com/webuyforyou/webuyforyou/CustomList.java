package com.webuyforyou.webuyforyou;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomList extends ArrayAdapter<String> {
	private final Activity context;
	private String[] web = {};
	private Integer[] imageId;
	private ArrayList<String> ContactName = new ArrayList<String>();
	private ArrayList<Bitmap> Images = new ArrayList<Bitmap>();
	View rowView;
	TextView txtTitle;
	TextView BDate;
	ImageView imageView;

	public CustomList(Activity context, ArrayList<String> ContactName, ArrayList<Bitmap> Images) {
		super(context, R.layout.list_single, ContactName);
		this.context = context;
		this.ContactName = ContactName;
		this.Images = Images;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		rowView = inflater.inflate(R.layout.list_single, null, true);
		txtTitle = (TextView) rowView.findViewById(R.id.name);
		BDate = (TextView) rowView.findViewById(R.id.dateofbirth);
		imageView = (ImageView) rowView.findViewById(R.id.img);
		txtTitle.setText(ContactName.get(position));
		BDate.setText(getDate(Long.parseLong(Utility.startDates.get(position))));
//		imageView.setImageResource(imageId[position]);
		return rowView;
	}
	
	public String getDate(long milliSeconds) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}
}