package com.example.caretaker2;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	TextToSpeech ttobj;

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
		ttobj = new TextToSpeech(getApplicationContext(),
				new TextToSpeech.OnInitListener() {
					@Override
					public void onInit(int status) {
						if (status != TextToSpeech.ERROR) {
							ttobj.setLanguage(Locale.US);
						}
					}
				});
	}

	@Override
	public void onPause() {
		if (ttobj != null) {
			ttobj.stop();
			ttobj.shutdown();
		}
		super.onPause();
	}

	public void callEmergency(View view) {

		/*
		 * String response = "I am trying to call your emergency contact";
		 * Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG)
		 * .show(); ttobj.speak(response, TextToSpeech.QUEUE_FLUSH, null);
		 */
		try {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:213-268-7789"));
			callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(callIntent);
			finish();
		} catch (Exception ex) {
			Toast.makeText(MainActivity.this, "Call failed, dial again!",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void saySomething(View view) {
		String response = "Are you OK?";
		Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT)
				.show();
		try {
			ttobj.speak(response, TextToSpeech.QUEUE_FLUSH, null);
		} catch (Exception ex) {
			Toast.makeText(this, "Talk failed, pls try again!",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void vibrate(View view) {
		Vibrator v = (Vibrator) this.getApplicationContext().getSystemService(
				Context.VIBRATOR_SERVICE);
		v.vibrate(500);
	}

	public void launchConfig(View view) {

		Intent intent = new Intent(this, ConfigurationActivity.class);
		startActivity(intent);
	}

	public void launchCamera(View view) {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		startActivityForResult(intent, 0);
	}
}
