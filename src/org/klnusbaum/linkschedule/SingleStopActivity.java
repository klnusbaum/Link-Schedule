/**
 * Copyright 2011 Kurtis Nusbaum
 *
 * This file is part of LinkSchedule.  
 *
 * LinkSchedule is free software: you can 
 * redistribute it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either version 2 of the 
 * License, or (at your option) any later version.  
 *
 * LinkSchedule is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.  You should have received a copy of the GNU  General 
 * Public License along with LinkSchedule. If not, see 
 * http://www.gnu.org/licenses/.
 */

package org.klnusbaum.linkschedule;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.content.Context;
import android.widget.TimePicker;
import android.widget.Toast;
import android.view.ContextMenu;
import android.util.Log;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.SortedMap;
import java.util.Iterator;
import java.util.Map;
import java.util.GregorianCalendar;

/**
 * Activity for displaying the contents for a single bus stop.
 *
 * @author Kurtis Nusbaum
 * @version 1.0
 */
public class SingleStopActivity extends BusStopActivity implements Refreshable{

  /**
   * Reference to the LinkSchedule
   */
  private LinkSchedule linkSchedule;
  /**
   * Name of the bus stop this object is displaing.
   */
  private String busStop;

  /**
   * BroadcastReceiver to receive broadcasts when the time changes.
   */
  private TimeChangeReceiver timeChangeReceiver;

  /**
   * ClickListener for the StopViews.
   */
  final View.OnClickListener stopListener = new View.OnClickListener(){
    public void onClick(View v){
      v.showContextMenu();
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.bus_stop_activity);
    busStop = getString(R.string.unknown_stop);
    if(getIntent().hasExtra(BusStopActivity.EXTRA_STOPNAME)){
      busStop = getIntent().getStringExtra(BusStopActivity.EXTRA_STOPNAME);
    }
    if(!LinkSchedule.validBusStop(busStop, this)){
      Log.e("special", "unknow bus stop: " + busStop);
      Toast.makeText(
        this, R.string.contact_dev, Toast.LENGTH_SHORT).show();
      finish();
    }
    else{
      TextView header = (TextView)findViewById(R.id.stop_name);
      header.setText(busStop);

      setupStopView(R.id.nextTime);
      setupStopView(R.id.time1);
      setupStopView(R.id.time2);
      setupStopView(R.id.time3);
      setupStopView(R.id.time4);
      setupStopView(R.id.time5);
      setupStopView(R.id.time6);
      setupStopView(R.id.time7);
      setupStopView(R.id.time8);

      linkSchedule = LinkSchedule.getLinkSchedule(getResources());

      timeChangeReceiver = new TimeChangeReceiver(this);
      timeChangeReceiver.registerIntents(this);
      refreshSchedule();
    }
  }

  /**
   * Sets up a particular stop view.
   * 
   * @param id The id of the view to be setup.
   */
  private void setupStopView(int id){
    View v = findViewById(id);
    v.setOnClickListener(stopListener);
    registerForContextMenu(v);
  }

  @Override
  protected void onDestroy(){
    super.onDestroy();
    if(timeChangeReceiver != null){
      unregisterReceiver(timeChangeReceiver);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu){
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.stop_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case R.id.menuMainScreen:
      Intent mainScreenIntent = new Intent(
        SingleStopActivity.this, OmniScheduleActivity.class);
      startActivity(mainScreenIntent);
      return true;
    default:
      return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void refreshSchedule(){
    SortedMap<GregorianCalendar, String> snapshot = 
      linkSchedule.getSnapshot(busStop);

    Iterator it = snapshot.entrySet().iterator();
    Map.Entry pair = (Map.Entry)it.next();

    setStopTimeViewContents(R.id.previousTime, (GregorianCalendar)pair.getKey(),
      getString(R.string.previous_bus) + " " + pair.getValue());
    pair = (Map.Entry)it.next();
    setStopTimeViewContents(R.id.nextTime, (GregorianCalendar)pair.getKey(),
      getString(R.string.next_bus) + " " + pair.getValue());
    pair = (Map.Entry)it.next();
    setStopTimeViewContents(R.id.time1, (GregorianCalendar)pair.getKey(), 
      "" + pair.getValue());
    pair = (Map.Entry)it.next();
    setStopTimeViewContents(R.id.time2, (GregorianCalendar)pair.getKey(), 
      "" + pair.getValue());
    pair = (Map.Entry)it.next();
    setStopTimeViewContents(R.id.time3, (GregorianCalendar)pair.getKey(), 
      "" + pair.getValue());
    pair = (Map.Entry)it.next();
    setStopTimeViewContents(R.id.time4, (GregorianCalendar)pair.getKey(), 
      "" + pair.getValue());
    pair = (Map.Entry)it.next();
    setStopTimeViewContents(R.id.time5, (GregorianCalendar)pair.getKey(), 
      "" + pair.getValue());
    pair = (Map.Entry)it.next();
    setStopTimeViewContents(R.id.time6, (GregorianCalendar)pair.getKey(), 
      "" + pair.getValue());
    pair = (Map.Entry)it.next();
    setStopTimeViewContents(R.id.time7, (GregorianCalendar)pair.getKey(), 
      "" + pair.getValue());
    pair = (Map.Entry)it.next();
    setStopTimeViewContents(R.id.time8, (GregorianCalendar)pair.getKey(), 
      "" + pair.getValue());
  }

  /**
   * Set the contents of a stop view.
   *
   * @param id Id of the view to be setup.
   * @param cal Calendar that should put in the stop view.
   * @param label The label that should be put in the stop view.
   */
  private void setStopTimeViewContents(int id, GregorianCalendar cal, 
    String label)
  {
    StopTimeView view = (StopTimeView)findViewById(id);
    view.setText(label);
    view.setCalendar(cal);
  }

  @Override
  public void resetSchedule(){
    linkSchedule.reset();
  }

  @Override
  public String getCurrentBusStop(){
    return busStop;
  }

  /*
  private Uri getBusStopLink(String busStop){
    Uri.Builder builder = new Uri.Builder();
    builder.scheme("http").authority("csbsju.edu").appendPath("Transportation");
    GregorianCalendar currentTime = 
      (GregorianCalendar)GregorianCalendar.getInstance();
    if(busStop.equals(getString(R.string.flynntown_name))){
      if(LinkSchedule.isWeekday(currentTime)){
        GregorianCalendar dayEnd = 
          LinkSchedule.getCalendarFromString(
            getString(R.id.flynntown_weekday_end));
        if(currentTime.before(dayEnd)){
          builder.appendPath("Daily-Flynntown-Sexton.htm");
        }
        else if(LinkSchedule.isFriday(currentTime)){
          builder.appendPath("Evening-Fri-Sat.htm");
        }
        else{
          builder.appendPath("Evening-Sun-Thu.htm");
        }
      }
      else{
        GregorianCalendar dayEnd = 
          LinkSchedule.getCalendarFromString(
            getString(R.id.flynntown_weekend_day_end));
        if(currentTime.before(dayEnd)){
          builder.appendPath("Weekend.htm");
        }
        else if(LinkSchedule.isSunday(currentTime)){
          builder.appendPath("Evening-Sun-Thu.htm");
        }
        else{
          builder.appendPath("Evening-Fri-Sat.htm");
        }  
      }  
    }
    else if(busStop.equals(getString(R.string.gorecki_name))){

    }
    else if(busStop.equals(getString(R.string.hcc_name))){

    }
    else if(busStop.equals(getString(R.string.sexton_name))){

    }
    return builder.build();
  }*/

}
