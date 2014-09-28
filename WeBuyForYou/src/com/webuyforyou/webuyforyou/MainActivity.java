package com.webuyforyou.webuyforyou;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.R.drawable;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.widget.ListView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends SherlockActivity {
  ListView list;
  String [] SelectText = {"Save", "Settings"};

  ArrayList<String> Name = new ArrayList<String>();
  ArrayList<Bitmap> Images = new ArrayList<Bitmap>();
  LayoutInflater li;
  View promptsView;
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
                	startActivity(new Intent(getApplicationContext(), ProfileScreen.class));
                    Toast.makeText(getApplicationContext(), "You Clicked at " +Utility.descriptions.get(position), Toast.LENGTH_SHORT).show();
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
	  else if (item.getTitle().equals("Add"))
	  {
		  li = LayoutInflater.from(getApplicationContext());
		  promptsView = li.inflate(R.layout.dialogdate, null);

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

			// set prompts.xml to alertdialog builder
			alertDialogBuilder.setView(promptsView);

			final EditText UserInput = (EditText) promptsView
					.findViewById(R.id.username);
			final EditText OccasionInput = (EditText) promptsView
					.findViewById(R.id.occasion);
			final EditText DateInput = (EditText) promptsView
					.findViewById(R.id.dob);

			// set dialog message
			alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK",
				  new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,int id) {
					// get user input and set it to result
					// edit text
				    }
				  })
				.setNegativeButton("Cancel",
				  new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				    }
				  });

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
	  }
	  else
		  Toast.makeText(this, "Handling in onOptionsItemSelected avoided",Toast.LENGTH_SHORT).show();
	  
      return false;
  }

  
}