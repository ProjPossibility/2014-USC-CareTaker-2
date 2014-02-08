package com.example.myfirstapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void callEmergency(View view) {
		/*
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText edittext = (EditText) findViewById(R.id.edit_message);
		String message = edittext.getText().toString();

		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
		*/
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:9111"));
		startActivity(callIntent);
	}
}
