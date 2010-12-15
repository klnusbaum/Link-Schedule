package org.klnusbaum.linkschedule;

import android.app.Activity;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

public class ScheduleActivity extends Activity{
	private LinkSchedule linkSchedule;
	private TickReceiver tickReceiver;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
		linkSchedule = new LinkSchedule(getResources());	
		tickReceiver = new TickReceiver(this);
		registerReceiver(
			tickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));

		refreshTimes();
  }

	@Override
	protected void onDestroy(){
		super.onDestroy();
		unregisterReceiver(tickReceiver);
	}
			
	private void refreshTimes(){
		setStopTime(R.id.sexton_time, linkSchedule.getNextTime(LinkSchedule.BusStop.sexton));
		setStopTime(R.id.gorecki_time, linkSchedule.getNextTime(LinkSchedule.BusStop.gorecki));
		setStopTime(R.id.flynntown_time, linkSchedule.getNextTime(LinkSchedule.BusStop.flynntown));
		setStopTime(R.id.hcc_time, linkSchedule.getNextTime(LinkSchedule.BusStop.hcc));
	}

	private void setStopTime(int id, String time){
		((TextView)findViewById(id)).setText(time);
	}

	private class TickReceiver extends BroadcastReceiver{
		
		private ScheduleActivity scheduleActivity;

		public TickReceiver(ScheduleActivity scheduleActivity){
			super();
			this.scheduleActivity = scheduleActivity;
		}
			
		@Override
		public void onReceive(Context context, Intent intent){
			scheduleActivity.refreshTimes();
		}
	}

}
