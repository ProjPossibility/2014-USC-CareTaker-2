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
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.os.BatteryManager;
import android.content.IntentFilter;

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
	String got;

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
	} // end of onCreate()

	@Override
	public void onPause() {
		if (ttobj != null) {
			ttobj.stop();
			ttobj.shutdown();
		}
		super.onPause();
	}

	//turn on gps
	
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
	
	//turn off gps
	
	public void callEmergency(View view)
	{
		call();
	}
	
	public void call() {
		saySomething("Calling your emergency contact");
		
		 try {
             Thread.sleep(2750); // Delay 3 seconds to handle better turning on loudspeaker
           } catch (InterruptedException e) {
           }
		
		sendText("I am calling the emergency contact. I may need help.");
		
		try {
            Thread.sleep(1000); // Delay 1 second to handle better turning on loudspeaker
          } catch (InterruptedException e) {
          }

		try {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:408-887-5230"));
			callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(callIntent);
			//finish();
		} catch (Exception ex) {
			Toast.makeText(MainActivity.this, "Call failed, dial again!",
					Toast.LENGTH_SHORT).show();
		}

	}
	
	public void ask(View view)
	{
		saySomething("Are you OK?");
			 try {
	                Thread.sleep(500); // Delay 0,5 seconds to handle better turning on loudspeaker
	              } catch (InterruptedException e) {
	              }
		voiceInput();
		
		if ( got.contains( "I am hurt" ) )
			call();
		else
			saySomething("Good to know.");
	}

	public void saySomething(String response) {
		Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT)
				.show();
		try {
			ttobj.speak(response, TextToSpeech.QUEUE_FLUSH, null);
		} catch (Exception ex) {
			Toast.makeText(this, "Talk failed, pls try again!",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		got = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
			Toast.makeText(this, got,
					Toast.LENGTH_SHORT).show();

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
		Intent intent = new Intent("android.media.action.VIDEO_CAPTURE");
		startActivityForResult(intent, 0);
		finish();
	}

	public void turnGPSOn(View view) {
		startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		// intent.putExtra("enabled", true);
		// sendBroadcast(intent);
	}

	public void voiceRecognition(View view)
	{
		voiceInput();
	}
	
	public void voiceInput() {
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
		
		/*String altitude = Double.toString(mCurrentLocation.getAltitude());*/
		String longitude = Double.toString(mCurrentLocation.getLongitude());
		String lattitude = Double.toString(mCurrentLocation.getLatitude());
		/*String time = Double.toString(mCurrentLocation.getTime());*/

		/*Toast.makeText(getApplicationContext(), "Height: " + altitude,
				Toast.LENGTH_SHORT).show();*/
		Toast.makeText(getApplicationContext(),
				"Present Location: " + longitude + " ," + lattitude,
				Toast.LENGTH_SHORT).show();
		/*Toast.makeText(getApplicationContext(), "Current Time: " +  time,
				Toast.LENGTH_SHORT).show();*/
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

	//start of wi-fi connect
	
	@Override
	public void onConnected(Bundle arg0) {
		if (!mLocationClient.isConnected()) {
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

	//end of wi-fi connect
	
	public void sendHelp(View view) {
		String output = "Please Help!";
		sendText(output);
	}

	public void sendText(String message) {

		String phoneNo = "2132687789"/* "4088875230" */;

		try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phoneNo, null, message, null, null);
			Toast.makeText(getApplicationContext(),
					"SMS Sent! Message says: " + message.toString(),
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"SMS faild, please try again later!", Toast.LENGTH_LONG)
					.show();
			e.printStackTrace();
		}
	}
	
	
public void checkBattery(View view) {

		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = getApplicationContext().registerReceiver(null,
				ifilter);

		// Are we charging / charged?
		int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING
				|| status == BatteryManager.BATTERY_STATUS_FULL;

		// How are we charging?
		int chargePlug = batteryStatus.getIntExtra(
				BatteryManager.EXTRA_PLUGGED, -1);
		boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
		boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
		int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

		float batteryPct = level/* / (float) scale*/;
		
		if ( batteryPct < 70 )
		{
			saySomething( "My battery level is low." );
			 try {
	                Thread.sleep(500); // Delay 0,5 seconds to handle better turning on loudspeaker
	              } catch (InterruptedException e) {
	              }
			 
			sendText( "My battery level is under 30%" );
		}

		Toast.makeText(this, (Float.toString(batteryPct)+"%"), Toast.LENGTH_SHORT).show();
	}
}
