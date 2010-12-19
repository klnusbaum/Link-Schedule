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
import android.content.res.Resources;
import android.widget.TextView;
import android.util.Log;

import java.util.Scanner;
import java.io.InputStream;

/**
 * Class for displaying information about LinkSchedule.
 */
public class AboutActivity extends Activity {

	/** Text to display */
	private String text;
	/** TextView in which the text will be displayed */
	private TextView aboutText;
	/** Tag for loggin */
	private static final String TAG = "AboutActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
    setContentView(R.layout.about_layout);
		Scanner sc = new Scanner(getResources().openRawResource(R.raw.about));
		String text = new String();
		while(sc.hasNext()){
			text = text + sc.nextLine() + "\n";
		}
		aboutText = (TextView) findViewById(R.id.about_text);
		aboutText.setText(text);
	}

}

