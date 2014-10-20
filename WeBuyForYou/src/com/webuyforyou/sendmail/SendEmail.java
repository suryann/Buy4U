package com.webuyforyou.sendmail;

import com.webuyforyou.BaseApplication;
import com.webuyforyou.R;
import com.webuyforyou.util.UserName;

import android.util.Log;
import android.widget.Toast;


public class SendEmail {
    /** Called when the activity is first created. */
    protected void sendMail(String Body) {
    	UserName UserDetails = new UserName();
    	Mail m = new Mail(BaseApplication.getApplication().getString(R.string.emailid),BaseApplication.getApplication().getString(R.string.password)); 
        String[] toArr = {"suryann@gmail.com","kannappan88@gmail.com","webuyforyou7@gmail.com"}; 
        m.setTo(toArr); 
        m.setFrom("webuyforyou7@gmail.com");
        m.setSubject("User mail ID:" + UserDetails.getUserName(BaseApplication.getApplication())+"	Mobile Number:"+UserDetails.getMobileNumber(BaseApplication.getApplication())); 
        m.setBody(Body); 
        try { 
          if(m.send()) { 
            Toast.makeText(BaseApplication.getApplication(), "Email was sent successfully.", Toast.LENGTH_LONG).show(); 
          } else { 
            Toast.makeText(BaseApplication.getApplication(), "Email was not sent.", Toast.LENGTH_LONG).show(); 
          } 
        } catch(Exception e) { 
          Log.e("MailApp", "Could not send email", e); 
        } 
    }
}