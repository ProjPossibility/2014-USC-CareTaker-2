package com.example.caretaker2;

import java.util.Locale;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.media.AudioManager;
import android.telephony.SmsManager;

public class MainActivity extends Activity {

	public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	private TextToSpeech ttobj;

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

	
		 /* String response = "I am trying to call your emergency contact";
		  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG)
		  .show(); ttobj.speak(response, TextToSpeech.QUEUE_FLUSH, null);*/
		 
		sendText( "I am calling primary emergency contact. I may be in trouble.");
		
		try {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:408-887-5230"));
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
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(15000);
	}

	public void launchConfig(View view) {

		Intent intent = new Intent(this, ConfigurationActivity.class);
		startActivity(intent);
	}

	public void launchCamera(View view) {
		/*
		 * Camera mCamera = Camera.open(); Camera.Parameters params =
		 * mCamera.getParameters(); // Check Whether device supports AutoFlash,
		 * If you YES then set // AutoFlash List<String> flashModes =
		 * params.getSupportedFlashModes(); if (flashModes
		 * .contains(android.hardware.Camera.Parameters.FLASH_MODE_OFF)) {
		 * params.setFlashMode(Parameters.FLASH_MODE_ON); }
		 * mCamera.setParameters(params); mCamera.startPreview();
		 */
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		startActivityForResult(intent, 0);
	}

	public void turnGPSOn(View view) {
		startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		// intent.putExtra("enabled", true);
		// sendBroadcast(intent);
	}

	public void voiceRecognition(View view) {
		try {
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
					"Voice recognition Demo...");
			startActivityForResult(intent, 1234);
		} catch (ActivityNotFoundException e) {
			Intent browserIntent = new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("https://market.android.com/details?id=APP_PACKAGE_NAME"));
			startActivity(browserIntent);
		}
	}
	
	public void sendHelp(View view)
	{
		String output = "Please Help!";
		sendText( output );
	}
	
	public void sendText(String message)
	{
		
		String phoneNo = "2132687789"/*"4088875230"*/;

		  try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phoneNo, null, message, null, null);
			Toast.makeText(getApplicationContext(), "SMS Sent! Message says " + message.toString(),
						Toast.LENGTH_LONG).show();
		  } catch (Exception e) {
			Toast.makeText(getApplicationContext(),
				"SMS faild, please try again later!",
				Toast.LENGTH_LONG).show();
			e.printStackTrace();
		  }
	}
}
