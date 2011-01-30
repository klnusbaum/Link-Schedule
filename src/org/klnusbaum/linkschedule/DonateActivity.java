/**
 * Copyright 2010 Kurtis Nusbaum
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
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Button;
import android.text.TextWatcher;
import android.text.Editable;
import android.util.Log;
import android.view.View;

/**
 * Activity for starting the process of making a donation.
 *
 * @author Kurtis Nusbaum
 * @version 1.0
 */
public class DonateActivity extends Activity implements TextWatcher{ 

//	private static final int server = PayPal.ENV_SANDBOX;
	private static final int INITIALIZE_SUCCESS = 0;
	private static final int INITIALIZE_FAILURE = 1;
	private static final int LOADING_DIALOG = 0;
	private static final int LOADING_FAILED_DIALOG = 1;

	private EditText donationEdit;
	private Button donateButton;

	/*Handler hRefresh = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			dismissDialog(LOADING_DIALOG);
			switch(msg.what){
    	case INITIALIZE_SUCCESS:
    		setupButtons();
        break;
    	case INITIALIZE_FAILURE:
    		showDialog(LOADING_FAILED_DIALOG);
    		break;
			}
		}
	};*/

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
		setContentView(R.layout.donate);
		donationEdit = (EditText) findViewById(R.id.donate_amount);		
		donateButton = (Button) findViewById(R.id.donate_button);
		donationEdit.addTextChangedListener(this);	
		/*Thread initLibThread = new Thread(){
			public void run(){
				initLibrary();
				if(PayPal.getInstance().isLibraryInitialized()){
					hRefresh.sendEmptyMessage(INITIALIZE_SUCCESS);
				}
				else{
					hRefresh.sendEmptyMessage(INITIALIZE_FAILURE);
				}
			}
		};
		initLibThread.start();*/
		//showDialog(LOADING_DIALOG);
		validate();
  }

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count){
	
	}

	@Override
	public void beforeTextChanged(
		CharSequence s, int start, int count, int after)
	{
	
	}

	@Override
	public void afterTextChanged(Editable editable){
		validate();
	}

	private void validate(){
		if(donationEdit.getText().toString().equals("")){
			donateButton.setEnabled(false);
		}
		else{
			donateButton.setEnabled(true);
		}
	}

/*	private void initLibrary() {
		PayPal pp = PayPal.getInstance();
		if(pp == null){
			pp = PayPal.initWithAppID(this, appID, server);
			pp.setLanguage(Locale.getDefault().getCountry());
		}
	}*/

	public void startDonation(View view){

	}
				

}
