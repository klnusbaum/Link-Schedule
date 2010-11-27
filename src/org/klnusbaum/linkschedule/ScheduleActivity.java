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
		((TextView)flynntownView.findViewByID(R.id.stopName)).setText(R.string.flyntown_name);
		goreckiView = (ViewGroup)findViewByID(gorecki_clock);
		((TextView)goreckiView.findViewByID(R.id.stopName)).setText(R.string.gorecki_name);
		hccView = (ViewGroup)findViewByID(hcc_clock);
		((TextView)hccView.findViewByID(R.id.stopName)).setText(R.string.hcc_name);
		sextonView = (ViewGroup)findViewByID(sexton_clock);
		((TextView)sextonView.findViewByID(R.id.stopName)).setText(R.string.sexton_name);
		refresh();
  }

	private void refresh(){
		setStopTime(flynntownView, linkSchedule.getNextFlynntownTime());
		setStopTime(goreckiView, linkSchedule.getNextGoreckiTime());
		setStopTime(hccView, linkSchedule.getNextHCCTime());
		setStopTime(sextonView, linkSchedule.getNextSextonTime());
	}

	private void setStopTime(ViewGroup busStopView, String time){
		((TextView)busStopView.findViewByID(R.id.time)).setText(time);
	}

	private class TickReciever extends BroadcastReciever{
		@Override
		public void onRecieve(Context context, Intent intent){
			refresh();
		}
	}

}
