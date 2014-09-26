package com.webuyforyou.webuyforyou;

import java.util.ArrayList;

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

	public CustomList(Activity context, String[] web, Integer[] imageId) {
		super(context, R.layout.list_single, web);
		this.context = context;
		this.web = web;
		this.imageId = imageId;
	}
	
	public CustomList(Activity context, ArrayList<String> ContactName, ArrayList<Bitmap> Images) {
		super(context, R.layout.list_single, ContactName);
		this.context = context;
		this.ContactName = ContactName;
		this.Images = Images;
	}
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.list_single, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.name);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
		txtTitle.setText(web[position]);
		imageView.setImageResource(imageId[position]);
		return rowView;
	}
}