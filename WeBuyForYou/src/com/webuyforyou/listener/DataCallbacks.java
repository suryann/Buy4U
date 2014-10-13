package com.webuyforyou.listener;

import java.util.List;

import com.webuyforyou.model.BirthdayDataModel;

public interface DataCallbacks {

	void processSuccess(List<BirthdayDataModel> birthdayDataModels);
}
