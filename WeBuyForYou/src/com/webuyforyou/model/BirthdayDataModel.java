package com.webuyforyou.model;

import java.io.Serializable;

import android.text.TextUtils;

import com.webuyforyou.BaseApplication;
import com.webuyforyou.R;
import com.webuyforyou.util.Utility;

public class BirthdayDataModel implements Serializable{

	private String title;
	private String startDate;
	private String imageUrl;
	private String description;
	private String endDate;
	private String timezone;
	private String eventLocation;
	private String month;
	private int monthCount;
	private String[] monthsFullName = BaseApplication.getApplication()
			.getResources().getStringArray(R.array.months_full_name);

	/**
	 * @return the month
	 */
	public String getMonth() {
		return month;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
		if (!TextUtils.isEmpty(startDate)) {
			long milliseconds = Long.parseLong(startDate);
			String dateFormat = Utility.getDate(milliseconds, null);
			String month = Utility.getMonth(dateFormat);
			if (!TextUtils.isEmpty(month)) {
				setMonth(month);
				for (int i = 0; i < monthsFullName.length; i++) {
					if (month.equalsIgnoreCase(monthsFullName[i])) {
						setMonthCount(i);
						break;
					}
				}
			}

		}
	}

	private void setMonth(String month) {
		this.month = month;
	}

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @param imageUrl
	 *            the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the timezone
	 */
	public String getTimezone() {
		return timezone;
	}

	/**
	 * @param timezone
	 *            the timezone to set
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	/**
	 * @return the eventLocation
	 */
	public String getEventLocation() {
		return eventLocation;
	}

	/**
	 * @param eventLocation
	 *            the eventLocation to set
	 */
	public void setEventLocation(String eventLocation) {
		this.eventLocation = eventLocation;
	}

	public int getMonthCount() {
		return monthCount;
	}

	public void setMonthCount(int monthCount) {
		this.monthCount = monthCount;
	}

}
