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

import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalPayment;

import java.math.BigDecimal;
import java.util.Locale;

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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

/**
 * Activity for starting the process of making a donation.
 *
 * @author Kurtis Nusbaum
 * @version 1.0
 */
public class DonateActivity extends Activity implements TextWatcher{ 

	private static final int server = PayPal.ENV_SANDBOX;
	private static final int INITIALIZE_SUCCESS = 0;
	private static final int INITIALIZE_FAILURE = 1;
	private static final int LOADING_DIALOG = 0;
	private static final int LOADING_FAILED_DIALOG = 1;

	private static final int DONATE_REQUEST = 1;

	private EditText donationEdit;
	private Button donateButton;


  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
		setContentView(R.layout.donate);
		donationEdit = (EditText) findViewById(R.id.donate_amount);		
		donateButton = (Button) findViewById(R.id.donate_button);
		donationEdit.addTextChangedListener(this);	
		validate();
  }

	@Override
	public Dialog onCreateDialog(int id){
		switch(id){
		case LOADING_DIALOG:
			ProgressDialog loadingDialog = new ProgressDialog(this);
			loadingDialog.setIndeterminate(true);
			loadingDialog.setMessage(getString(R.string.loading));
			loadingDialog.setCancelable(false);
			return loadingDialog;
		case LOADING_FAILED_DIALOG:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.paypal_error)
			.setCancelable(false)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int id){
					dismissDialog(LOADING_FAILED_DIALOG);
				}
			});
			return builder.create();
		default:
			return null;
		}
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
		new InitPaypal().execute(getString(R.string.paypal_app_id));
	}

	private class InitPaypal extends AsyncTask<String, Void, Boolean>{

		protected void onPreExecute(){
			showDialog(LOADING_DIALOG);
		}
		
		protected Boolean doInBackground(String... params){
			String appID = params[0];
			PayPal pp = PayPal.getInstance();
			if(pp == null){
				pp = PayPal.initWithAppID(DonateActivity.this, appID, server);
				pp.setLanguage(Locale.getDefault().getCountry());
			}
			if(PayPal.getInstance().isLibraryInitialized()){
				return new Boolean(true);
			}
			else{
				return new Boolean(false);
			}
		}

		protected void onPostExecute(Boolean result){
			dismissDialog(LOADING_DIALOG);
			if(result.booleanValue()){
				PayPalPayment payment = new PayPalPayment();
				payment.setCurrencyType("USD");
				payment.setPaymentType(PayPal.PAYMENT_TYPE_SERVICE);
				payment.setRecipient("klnusbaum@bazzarsolutions.com");
				payment.setSubtotal(new BigDecimal(donationEdit.getText().toString()));
				payment.setDescription("Donation from Link Schedule");
				payment.setMemo("Donation for Link Schedule");
				Intent checkoutIntent = PayPal.getInstance().checkout(
					payment, DonateActivity.this);
				startActivityForResult(checkoutIntent, DONATE_REQUEST);
			}
			else{
				showDialog(LOADING_FAILED_DIALOG);	
			}
		}

	}
		
				

}
