//package com.webuyforyou.activity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.webuyforyou.R;
//import com.webuyforyou.model.BirthdayDataModel;
//import com.webuyforyou.util.Utility;
//
//public class CustomList extends ArrayAdapter<String> {
//	private final Activity context;
//	private String[] web = {};
//	private Integer[] imageId;
//	private ArrayList<String> ContactName = new ArrayList<String>();
//	private ArrayList<Bitmap> Images = new ArrayList<Bitmap>();
//	View rowView;
//	TextView txtTitle;
//	TextView BDate;
//	ImageView imageView;
//
//	public CustomList(Activity context, List<BirthdayDataModel> birthdayDataModels) {
//		super(context, R.layout.list_single, ContactName);
//		this.context = context;
//	}
//
//	@Override
//	public View getView(int position, View view, ViewGroup parent) {
//		LayoutInflater inflater = context.getLayoutInflater();
//		rowView = inflater.inflate(R.layout.list_single, null, true);
//		txtTitle = (TextView) rowView.findViewById(R.id.name);
//		BDate = (TextView) rowView.findViewById(R.id.dateofbirth);
//		imageView = (ImageView) rowView.findViewById(R.id.img);
//
//		txtTitle.setText(ContactName.get(position));
//		if (Utility.startDates.get(position).equals(" ")) {
//			BDate.setText(Utility.startDates.get(position));
//		} else {
//			BDate.setText(Utility.getDate(
//					Long.parseLong(Utility.startDates.get(position)),
//					Utility.DEFAULT_DATE_FORMATTER,
//					Utility.mTimezones.get(position)));
//		}
//		// imageView.setImageResource(imageId[position]);
//		return rowView;
//	}
//}