package com.example.caretaker2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

public class ConfigurationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuration);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.configuration, menu);
		return true;
	}

	public void updateConfig(View view) {
		Integer height = ((SeekBar) findViewById(R.id.seekBarHeight))
				.getProgress();
		Toast.makeText(this, height.toString(), Toast.LENGTH_SHORT).show();
	}
	
	public void cancelConfig(View view){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
}
