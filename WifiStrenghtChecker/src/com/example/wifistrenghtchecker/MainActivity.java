package com.example.wifistrenghtchecker;


import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

   WifiManager mainWifiObj;
   WifiScanReceiver wifiReciever;
   ListView list;
   String wifis[];
   List<ScanResult> wifiScanList;
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      list = (ListView)findViewById(R.id.list);
      mainWifiObj = (WifiManager) getSystemService(Context.WIFI_SERVICE);
      wifiReciever = new WifiScanReceiver();
      mainWifiObj.startScan();
      
   }


   protected void onPause() {
      unregisterReceiver(wifiReciever);
      super.onPause();
   }

   protected void onResume() {
      registerReceiver(wifiReciever, new IntentFilter(
      WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
      super.onResume();
   }
   
   class WifiScanReceiver extends BroadcastReceiver {
      @SuppressLint("UseValueOf")
      public void onReceive(Context c, Intent intent) {

    	  wifiScanList = mainWifiObj.getScanResults();
         wifis = new String[wifiScanList.size()];
         for(int i = 0; i < wifiScanList.size(); i++){
        	 wifis[i]=((wifiScanList.get(i)).SSID.toString());
            //wifis[i] = ((wifiScanList.get(i)).toString());
        	
         }

         list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
        		 R.layout.list_black_text,R.id.list_content,wifis));
         checkthestrength();
      
      }
   }
public void checkthestrength() {
	// TODO Auto-generated method stub
	 list.setOnItemClickListener(new OnItemClickListener() {
	        

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				int pos=0;
			    String selectedFromList =(String) (list.getItemAtPosition(position));
			    for(int i = 0; i < wifiScanList.size(); i++){
		        	 if(((wifiScanList.get(i)).SSID.toString()).matches(selectedFromList))
		        	 {
		        		 pos=i;
		        	 }
		   
			    }
			    int strength = ((wifiScanList.get(pos)).level);
			    int value=WifiManager.calculateSignalLevel(strength,11);
			    String val=String.valueOf(value);
			    String rssi=String.valueOf(strength);
			   // Toast.makeText(MainActivity.this,val , Toast.LENGTH_SHORT).show();
			    Intent intent = new Intent(MainActivity.this,Activity2.class);
			    intent.putExtra("Name", selectedFromList);
                intent.putExtra("Strength", val);
                intent.putExtra("Rss",rssi);
                  startActivity(intent);
				// TODO Auto-generated method stub
			 
			}                 
	    });
	
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.activity2, menu);
	return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	// Handle action bar item clicks here. The action bar will
	// automatically handle clicks on the Home/Up button, so long
	// as you specify a parent activity in AndroidManifest.xml.
	int id = item.getItemId();
	if (id == R.id.action_settings) {
		return true;
	}
	return super.onOptionsItemSelected(item);
}
   
   }

