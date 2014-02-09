package com.example.caretaker2;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button button = (Button) findViewById(R.id.emergencyCall);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				callEmergency(view);
			}
		});
	}

	public void callEmergency(View view) {

		try {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:2132687789"));
			String callingNumber = "2132687789";
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + callingNumber));
			callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void saySomething(View view){
		
	}
	public void launchConfig(View view) {

		Intent intent = new Intent(this, ConfigurationActivity.class);
		/*
		 * EditText edittext = (EditText) findViewById(R.id.edit_message);
		 * String message = edittext.getText().toString();
		 */
		startActivity(intent);
	}
}
