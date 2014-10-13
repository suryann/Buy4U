package com.webuyforyou.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;

import com.webuyforyou.model.BirthdayDataModel;

public class Session {
	private static Session session = null;
	private Map<String, List<BirthdayDataModel>> map = new LinkedHashMap<String, List<BirthdayDataModel>>();

	private Session() {
	}

	public static Session getInstance() {
		if (session == null) {
			session = new Session();
		}
		return session;
	}

	/**
	 * get all calendar events
	 * 
	 * @return
	 */
	public Map<String, List<BirthdayDataModel>> getSortedCalendarEventData(
			List<BirthdayDataModel> birthdayDataModels) {
		map.clear();
		// List<BirthdayDataModel> birthdayDataModels = Utility
		// .readCalendarEvent(BaseApplication.getApplication());

		Collections.sort(birthdayDataModels, new DateComparator());
		Collections.sort(birthdayDataModels, new MonthComparator());

		if (birthdayDataModels != null && birthdayDataModels.size() > 0) {
			List<BirthdayDataModel> list = new ArrayList<BirthdayDataModel>();
			for (BirthdayDataModel birthdayDataModel : birthdayDataModels) {
				String month = birthdayDataModel.getMonth();
				if (!TextUtils.isEmpty(month)) {
					if (map.containsKey(month)) {
						List<BirthdayDataModel> dataModels = map.get(month);
						dataModels.add(birthdayDataModel);
						map.put(month, dataModels);
					} else {
						list.add(birthdayDataModel);
						map.put(month, list);
						list = new ArrayList<BirthdayDataModel>();
					}
				}
			}

		}
		return map;
	}

	/**
	 * 
	 * @author Kannappan
	 * 
	 */
	class MonthComparator implements Comparator<BirthdayDataModel> {

		@Override
		public int compare(BirthdayDataModel lhs, BirthdayDataModel rhs) {
			return lhs.getMonthCount() - rhs.getMonthCount();
		}
	}

	/**
	 * 
	 * @author Kannappan
	 * 
	 */
	class DateComparator implements Comparator<BirthdayDataModel> {

		@Override
		public int compare(BirthdayDataModel lhs, BirthdayDataModel rhs) {
			String lhsString = lhs.getStartDate();
			long milliSeconds = Long.parseLong(lhsString);
			Date lhsDate = new Date(milliSeconds);

			String rhsString = rhs.getStartDate();
			long rhsMilliSeconds = Long.parseLong(rhsString);
			Date rhsDate = new Date(rhsMilliSeconds);
			return lhsDate.compareTo(rhsDate);
		}
	}

}
