package org.klnusbaum.linkschedule;

import android.app.Activity;
import android.os.Bundle;

public class ScheduleActivity extends Activity{
	private LinkSchedule linkSchedule;
	private ViewGroup flynntownView, goreckiView, hccView, sextonView;

  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
		linkSchedule = new LinkSchedule(getResources());	
		registerReciever(new TickReciever(), new IntentFilter(Intent.ACTION_TIME_TICK));

		flynntownView = (ViewGroup)findViewByID(flynntown_clock);
		setStopText(flynntownView, R.string.flynntown_name);
		goreckiView = (ViewGroup)findViewByID(gorecki_clock);
		setStopText(goreckiView, gorecki_name);
		hccView = (ViewGroup)findViewByID(hcc_clock);
		setStopTime(hccView, hcc_name);
		sextonView = (ViewGroup)findViewByID(sexton_clock);
		setStopTime(sextonView, sexton_name);
		refreshTimes();
  }

	private void refreshTimes(){
		setStopTime(flynntownView, linkSchedule.getNextTime(BusStop.flynntown));
		setStopTime(goreckiView, linkSchedule.getNextTime(BusStop.gorecki));
		setStopTime(hccView, linkSchedule.getNextHCCTime(BusStop.hcc));
		setStopTime(sextonView, linkSchedule.getNextSextonTime(BusStop.sexton));
	}

	private void setStopTime(ViewGroup busStopView, String time){
		((TextView)busStopView.findViewByID(R.id.time)).setText(time);
	}

	private void setStopText(ViewGroup busStopView, int name_id){
		((TextView)busStopView.findViewByID(R.id.stopName)).setText(name_id);
	}

	private class TickReciever extends BroadcastReciever{
		@Override
		public void onRecieve(Context context, Intent intent){
			refresh();
		}
	}

}
