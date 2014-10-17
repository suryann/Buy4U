package com.webuyforyou.sendmail;

import com.webuyforyou.BaseApplication;

import android.util.Log;
import android.widget.Toast;


public class SendEmail {
    /** Called when the activity is first created. */
    protected void sendMail(String Body) {
    	
    	Mail m = new Mail("webuyforyou7@gmail.com", "yaminivb4u"); 
        String[] toArr = {"suryann@gmail.com","kannappan88@gmail.com"}; 
        m.setTo(toArr); 
        m.setFrom("webuyforyou7@gmail.com");
        m.setSubject("user mail ID and Phone number"); 
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