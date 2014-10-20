package com.webuyforyou.util;

import java.util.LinkedList;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.telephony.TelephonyManager;

public class UserName {

	/**
	 * Get UserName implementation from google Accounts
	 * 
	 */
	public String getUserName(Context appContext) {
		AccountManager manager = AccountManager.get(appContext);
		Account[] accounts = manager.getAccounts();
		List<String> possibleEmails = new LinkedList<String>();

		for (Account account : accounts) {
			// account.name as an email address only for certain account.type values
			possibleEmails.add(account.name);
		}

		if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
			String email = possibleEmails.get(0);
//			String[] parts = email.split("@");
//			if (parts.length > 0 && parts[0] != null)
				return email;
		} 
		return null;
	}
	
	public String getMobileNumber(Context appContext)
	{
		TelephonyManager tm = (TelephonyManager)appContext.getSystemService(Context.TELEPHONY_SERVICE); 
		String number = tm.getLine1Number();
		return number;
	}
}
