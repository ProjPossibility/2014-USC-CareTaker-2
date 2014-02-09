package com.example.caretaker2;

import java.util.Locale;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

public class MainActivity extends Activity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	private TextToSpeech ttobj;
	Location mCurrentLocation;
	private LocationClient mLocationClient;
	private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mLocationClient = new LocationClient(this, this, this);
		Button button = (Button) findViewById(R.id.emergencyCall);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				callEmergency(view);
			}
		});
		
		button = (Button) findViewById(R.id.location);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getLocation(view);
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

	@Override
	protected void onStart() {
		super.onStart();
		mLocationClient.connect();
	}

	@Override
	protected void onStop() {
		mLocationClient.disconnect();
		super.onStop();
	}

	public void callEmergency(View view) {

		/*
		 * String response = "I am trying to call your emergency contact";
		 * Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG)
		 * .show(); ttobj.speak(response, TextToSpeech.QUEUE_FLUSH, null);
		 */
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

	private void getLocation(View view) {
		int gpStatus = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getApplicationContext());
		if (gpStatus == ConnectionResult.SUCCESS) {
		} else {
			GooglePlayServicesUtil.getErrorDialog(gpStatus, this, 1234);
		}
		mCurrentLocation = mLocationClient.getLastLocation();
		 
		Toast.makeText(getApplicationContext(), mCurrentLocation.toString(),
		Toast.LENGTH_SHORT).show();
		
		/*TextView textView = new TextView(this);
		textView.setTextSize(40);
		textView.setText(mCurrentLocation.toString());*/
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);

				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
			// connectionResult.getErrorCode();
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		if(!mLocationClient.isConnected()){
			mLocationClient.connect();
		}
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
		mLocationClient.getLastLocation();
	}

	@Override
	public void onDisconnected() {
		Toast.makeText(this, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();
	}
}
