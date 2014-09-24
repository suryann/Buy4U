package com.webuyforyou.webuyforyou;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ListView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public class MainActivity extends Activity {
  ListView list;
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
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    CustomList adapter = new
        CustomList(MainActivity.this, web, imageId);
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
}