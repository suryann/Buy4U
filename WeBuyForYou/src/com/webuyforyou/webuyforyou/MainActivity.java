package com.webuyforyou.webuyforyou;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.R.drawable;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.widget.ListView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;


public class MainActivity extends SherlockActivity {
  ListView list;
  String [] SelectText = {"Save", "Settings"};
  String[] web = {
    "Google Plus",
      "Twitter",
      "Windows",
      "Bing",
      "Itunes",
      "Wordpress",
      "Drupal"
  } ;
  
  Integer[] imageId = {
      R.drawable.ic_launcher,
      R.drawable.ic_launcher,
      R.drawable.ic_launcher,
      R.drawable.ic_launcher,
      R.drawable.ic_launcher,
      R.drawable.ic_launcher,
      R.drawable.ic_launcher
  };

  ArrayList<String> Name = new ArrayList<String>();
  ArrayList<Bitmap> Images = new ArrayList<Bitmap>();
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
      setTheme(SampleList.THEME); //Used for theme switching in samples

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
//    CustomList adapter = new
//        CustomList(MainActivity.this, web, imageId);
    
    new Utility();
	Name = Utility.readCalendarEvent(getApplicationContext());
	Images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.ic_action_content_new));
	
    CustomList adapter = new
            CustomList(MainActivity.this, Name, Images);
    list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Toast.makeText(MainActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
                }
            });
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      //Used to put dark icons on light action bar
      boolean isLight = SampleList.THEME == R.style.Theme_Sherlock_Light;

      menu.add(SelectText[0]).setTitle("Add")
          .setIcon(R.drawable.ic_action_content_new)
          .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

      menu.add(SelectText[1]).setTitle("Settings")
          .setIcon(R.drawable.ic_action_action_settings)
          .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

      return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
      // If this callback does not handle the item click, onPerformDefaultAction
      // of the ActionProvider is invoked. Hence, the provider encapsulates the
      // complete functionality of the menu item.
	  if (item.getTitle().equals("Settings"))
		  startActivity(new Intent(MainActivity.this, Preference.class));
	  else
		  Toast.makeText(this, "Handling in onOptionsItemSelected avoided",Toast.LENGTH_SHORT).show();
	  
      return false;
  }

  
}