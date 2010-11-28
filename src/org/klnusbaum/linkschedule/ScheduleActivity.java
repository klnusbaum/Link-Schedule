package org.klnusbaum.linkschedule;

import android.app.Activity;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.Context;

public class ScheduleActivity extends Activity{
	private LinkSchedule linkSchedule;
	private ViewGroup flynntownView, goreckiView, hccView, sextonView;

  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
		linkSchedule = new LinkSchedule(getResources());	
		registerReceiver(new TickReceiver(this), new IntentFilter(Intent.ACTION_TIME_TICK));

		flynntownView = (ViewGroup)findViewById(R.id.flynntown_clock);
		setStopText(flynntownView, R.string.flynntown_name);
		goreckiView = (ViewGroup)findViewById(R.id.gorecki_clock);
		setStopText(goreckiView, R.string.gorecki_name);
		hccView = (ViewGroup)findViewById(R.id.hcc_clock);
		setStopText(hccView, R.string.hcc_name);
		sextonView = (ViewGroup)findViewById(R.id.sexton_clock);
		setStopText(sextonView, R.string.sexton_name);
		refreshTimes();
  }

	private void refreshTimes(){
		setStopTime(flynntownView, linkSchedule.getNextTime(LinkSchedule.BusStop.flynntown));
		setStopTime(goreckiView, linkSchedule.getNextTime(LinkSchedule.BusStop.gorecki));
		setStopTime(hccView, linkSchedule.getNextTime(LinkSchedule.BusStop.hcc));
		setStopTime(sextonView, linkSchedule.getNextTime(LinkSchedule.BusStop.sexton));
	}

	private void setStopTime(ViewGroup busStopView, String time){
		((TextView)busStopView.findViewById(R.id.time)).setText(time);
	}

	private void setStopText(ViewGroup busStopView, int name_id){
		((TextView)busStopView.findViewById(R.id.stopName)).setText(name_id);
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
