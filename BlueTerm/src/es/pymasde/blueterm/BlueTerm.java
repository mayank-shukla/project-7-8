package es.pymasde.blueterm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.app.Activity;
import android.app.AlertDialog;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

public class BlueTerm extends Activity {
	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;
	private static TextView mTitle;
	// Name of the connected device
	private String mConnectedDeviceName = null;
	/**
	 * Set to true to add debugging code and logging.
	 */
	public static final boolean DEBUG = true;
	/**
	 * Set to true to log each character received from the remote process to the
	 * android log, which makes it easier to debug some kinds of problems with
	 * emulating escape sequences and control codes.
	 */
	public static final boolean LOG_CHARACTERS_FLAG = DEBUG && false;
	/**
	 * Set to true to log unknown escape sequences.
	 */
	public static final boolean LOG_UNKNOWN_ESCAPE_SEQUENCES = DEBUG && false;
	/**
	 * The tag we use when logging, so that our messages can be distinguished from
	 * other messages in the log. Public because it's used by several classes.
	 */
	public static final String LOG_TAG = "BlueTerm";
	// Message types sent from the BluetoothReadService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";
	private BluetoothAdapter mBluetoothAdapter = null;
	/**
	 * Our main view. Displays the emulated terminal screen.
	 */
	private EmulatorView mEmulatorView;
	/**
	 * A key listener that tracks the modifier keys and allows the full ASCII
	 * character set to be entered.
	 */
	private TermKeyListener mKeyListener;
	/*
	 * USER CHANGES
	 */
	public boolean bThreadBool;
	private static BluetoothSerialService mSerialService = null;
	private static InputMethodManager mInputManager;
	private boolean mEnablingBT;
	private boolean mLocalEcho = false;
	private int mFontSize = 9;
	private int mColorId = 2;
	private int mControlKeyId = 0;
	private static final String LOCALECHO_KEY = "localecho";
	private static final String FONTSIZE_KEY = "fontsize";
	private static final String COLOR_KEY = "color";
	private static final String CONTROLKEY_KEY = "controlkey";
	public static final int WHITE = 0xffffffff;
	public static final int BLACK = 0xff000000;
	public static final int BLUE = 0xff344ebd;
	private static final int[][] COLOR_SCHEMES = { {BLACK,WHITE}, {WHITE,BLACK}, {WHITE,BLUE}};
	private static final int[] CONTROL_KEY_SCHEMES = {KeyEvent.KEYCODE_DPAD_CENTER,KeyEvent.KEYCODE_AT,KeyEvent.KEYCODE_ALT_LEFT,KeyEvent.KEYCODE_ALT_RIGHT};
	// private static final String[] CONTROL_KEY_NAME = {
	// "Ball", "@", "Left-Alt", "Right-Alt"
	// };
	private static String[] CONTROL_KEY_NAME;
	private int mControlKeyCode;
	private SharedPreferences mPrefs;
	private MenuItem mMenuItemConnect;
	private LocationManager mlocManager;
	private es.pymasde.blueterm.MyLocationListener mlocListener;
	private int[] data;
	private double min = 0;
	private double max = 400;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (DEBUG)
			Log.e(LOG_TAG,"+++ ON CREATE +++");
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		readPrefs();
		CONTROL_KEY_NAME = getResources().getStringArray(R.array.entries_controlkey_preference);
		mInputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		// Set up the window layout
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.custom_title);
		// Set up the custom title
		mTitle = (TextView)findViewById(R.id.title_left_text);
		mTitle.setText(R.string.app_name);
		mTitle = (TextView)findViewById(R.id.title_right_text);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			finishDialogNoBluetooth();
			return;
		}
		setContentView(R.layout.term_activity);
		mEmulatorView = (EmulatorView)findViewById(R.id.emulatorView);
		mEmulatorView.initialize(this);
		mKeyListener = new TermKeyListener();
		mEmulatorView.setFocusable(true);
		mEmulatorView.setFocusableInTouchMode(true);
		mEmulatorView.requestFocus();
		mEmulatorView.register(mKeyListener);
		mSerialService = new BluetoothSerialService(this,mHandlerBT,mEmulatorView);
		if (DEBUG)
			Log.e(LOG_TAG,"+++ DONE IN ON CREATE +++");
	}

	@Override
	public void onStart() {
		super.onStart();
		if (DEBUG)
			Log.e(LOG_TAG,"++ ON START ++");
		mEnablingBT = false;
		bThreadBool = false;
		// data = new int[1082050];
		try {
			InputStream inS = getBaseContext().getAssets().open("bomen.txt");
			InputStreamReader inR = new InputStreamReader(inS);
			BufferedReader buffR = new BufferedReader(inR);
			data = new int[1082050];
			int j = 0;
			for (String i = buffR.readLine();i != null;i = buffR.readLine()) {
				data[j] = Integer.parseInt(i);
				j++;
			}
		}
		catch (IOException e) {
			Log.e(LOG_TAG,"IO error" + e.getMessage());
		}
		catch (IllegalArgumentException e) {
			Log.e(LOG_TAG,"illegal argument error" + e.getMessage());
		}
		catch (IndexOutOfBoundsException e) {
			Log.e(LOG_TAG,"index out of bounds error" + e.getMessage());
		}
		
		new FakeData().start();
	}

	public synchronized void locChange(double lo, double la) {
		Log.e(LOG_TAG,"locatie GPS:" + la + "," + lo);
		double dF = 0.36 * (la - 52.15517440);
		double dL = 0.36 * (lo - 5.38720621);
		double SomX = (190094.945 * dL) + (-11832.228 * dF * dL) + (-144.221 * Math.pow(dF,2) * dL) + (-32.391 * Math.pow(dL,3)) + (-0.705 * dF) + (-2.340 * Math.pow(dF,3) * dL) + (-0.608 * dF * Math.pow(dL,3)) + (-0.008 * Math.pow(dL,2)) + (0.148 * Math.pow(dF,2) * Math.pow(dL,3));
		double SomY = (309056.544 * dF) + (3638.893 * Math.pow(dL,2)) + (73.077 * Math.pow(dF,2)) + (-157.984 * dF * Math.pow(dL,2)) + (59.788 * Math.pow(dF,3)) + (0.433 * dL) + (-6.439 * Math.pow(dF,2) * Math.pow(dL,2)) + (-0.032 * dF * dL) + (0.092 * Math.pow(dL,4)) + (-0.054 * dF * Math.pow(dL,4));
		double RDX = 155000 + SomX;
		double RDY = 463000 + SomY;
		int bomen = 0;
		Log.e(LOG_TAG,"locatie RD:" + RDX + "," + RDY);
		// (((RDY-429013)/25)^*1615) + (((RDX-60509)/25)^)
		int regel = (int)((Math.round((RDY - 429013) / 25) * 1615) + (Math.round((RDX - 60509) / 25)));
		Log.e(LOG_TAG,"locatie txt map: regel " + (regel + 1));
		try {
			bomen = data[regel];
		}
		catch (ArrayIndexOutOfBoundsException e) {
			Log.e(LOG_TAG,"GPS coords out of map");
		}
		Log.e(LOG_TAG,"bomen: " + bomen);
		//(max-min)/100;//bomen per 1%
		//(bomen-min)/bomenper1%//percentage bomen
		bomen = (int)((bomen - min) / ((max - min) / 100.0));
		Log.e(LOG_TAG,"procent: " + bomen);
		if (bomen == 0)
			bomen = 1;
		if (bomen > 100)
			bomen = 100;
		byte[] data = new byte[1];
		data[0] = (byte)bomen;
		mSerialService.write(data);
	}

	public void setLoc() {
		if (mlocManager == null) {
			Log.e(LOG_TAG,"initalse GPS");
			mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			if (mlocManager != null) {
				mlocListener = new MyLocationListener(this);
				mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1,mlocListener);
				try {
					Location loc = mlocManager.getLastKnownLocation(mlocManager.getBestProvider(new Criteria(),true));
					locChange(loc.getLongitude(),loc.getAltitude());
				}
				catch (NullPointerException e) {
					Log.e(LOG_TAG,"first location failed");
				}
			}
		}
		else {
			Log.e(LOG_TAG,"delete GPS");
			mlocManager.removeUpdates(mlocListener);
			mlocManager = null;
		}
	}

	@Override
	public synchronized void onResume() {
		super.onResume();
		if (DEBUG) {
			Log.e(LOG_TAG,"+ ON RESUME +");
		}
		if (!mEnablingBT) { // If we are turning on the BT we cannot check if it's enable
			if ((mBluetoothAdapter != null) && (!mBluetoothAdapter.isEnabled())) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.alert_dialog_turn_on_bt).setIcon(android.R.drawable.ic_dialog_alert).setTitle(R.string.alert_dialog_warning_title).setCancelable(false).setPositiveButton(R.string.alert_dialog_yes,new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mEnablingBT = true;
						Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
						startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
					}
				}).setNegativeButton(R.string.alert_dialog_no,new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						finishDialogNoBluetooth();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
			if (mSerialService != null) {
				// Only if the state is STATE_NONE, do we know that we haven't
				// started already
				if (mSerialService.getState() == BluetoothSerialService.STATE_NONE) {
					// Start the Bluetooth chat services
					mSerialService.start();
				}
			}
			if (mBluetoothAdapter != null) {
				readPrefs();
				updatePrefs();
				mEmulatorView.onResume();
			}
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mEmulatorView.updateSize();
	}

	@Override
	public synchronized void onPause() {
		super.onPause();
		if (DEBUG)
			Log.e(LOG_TAG,"- ON PAUSE -");
		if (mEmulatorView != null) {
			mInputManager.hideSoftInputFromWindow(mEmulatorView.getWindowToken(),0);
			mEmulatorView.onPause();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (DEBUG)
			Log.e(LOG_TAG,"-- ON STOP --");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (DEBUG)
			Log.e(LOG_TAG,"--- ON DESTROY ---");
		if (mSerialService != null)
			mSerialService.stop();
	}

	private void readPrefs() {
		mLocalEcho = mPrefs.getBoolean(LOCALECHO_KEY,mLocalEcho);
		mFontSize = readIntPref(FONTSIZE_KEY,mFontSize,20);
		mColorId = readIntPref(COLOR_KEY,mColorId,COLOR_SCHEMES.length - 1);
		mControlKeyId = readIntPref(CONTROLKEY_KEY,mControlKeyId,CONTROL_KEY_SCHEMES.length - 1);
	}

	private void updatePrefs() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mEmulatorView.setTextSize((int)(mFontSize * metrics.density));
		setColors();
		mControlKeyCode = CONTROL_KEY_SCHEMES[mControlKeyId];
	}

	private int readIntPref(String key, int defaultValue, int maxValue) {
		Log.d(LOG_TAG,"readIntPref " + key);
		int val;
		try {
			val = Integer.parseInt(mPrefs.getString(key,Integer.toString(defaultValue)));
		}
		catch (NumberFormatException e) {
			val = defaultValue;
		}
		val = Math.max(0,Math.min(val,maxValue));
		return val;
	}

	public int getConnectionState() {
		return mSerialService.getState();
	}

	public void send(byte[] out) {
		Log.d("BlueTerm","Sending Data..." + out);
		mSerialService.write(out);
	}

	public void toggleKeyboard() {
		mInputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
	}

	public int getTitleHeight() {
		return mTitle.getHeight();
	}

	// The Handler that gets information back from the BluetoothService
	private final Handler mHandlerBT = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case MESSAGE_STATE_CHANGE:
					if (DEBUG)
						Log.i(LOG_TAG,"MESSAGE_STATE_CHANGE: " + msg.arg1);
					switch(msg.arg1) {
						case BluetoothSerialService.STATE_CONNECTED:
							if (mMenuItemConnect != null) {
								mMenuItemConnect.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
								mMenuItemConnect.setTitle(R.string.disconnect);
							}
							mInputManager.showSoftInput(mEmulatorView,InputMethodManager.SHOW_IMPLICIT);
							mTitle.setText(R.string.title_connected_to);
							mTitle.append(mConnectedDeviceName);
							break;
						case BluetoothSerialService.STATE_CONNECTING:
							mTitle.setText(R.string.title_connecting);
							break;
						case BluetoothSerialService.STATE_LISTEN:
						case BluetoothSerialService.STATE_NONE:
							if (mMenuItemConnect != null) {
								mMenuItemConnect.setIcon(android.R.drawable.ic_menu_search);
								mMenuItemConnect.setTitle(R.string.connect);
							}
							mInputManager.hideSoftInputFromWindow(mEmulatorView.getWindowToken(),0);
							mTitle.setText(R.string.title_not_connected);
							break;
					}
					break;
				case MESSAGE_WRITE:
					if (mLocalEcho) {
						byte[] writeBuf = (byte[])msg.obj;
						mEmulatorView.write(writeBuf,msg.arg1);
					}
					break;
				/*
				 * case MESSAGE_READ: byte[]
				 * readBuf = (byte[]) msg.obj;
				 * mEmulatorView.write(readBuf,
				 * msg.arg1); break;
				 */
				case MESSAGE_DEVICE_NAME:
					// save the connected device's
					// name
					mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
					Toast.makeText(getApplicationContext(),"Connected to " + mConnectedDeviceName,Toast.LENGTH_SHORT).show();
					break;
				case MESSAGE_TOAST:
					Toast.makeText(getApplicationContext(),msg.getData().getString(TOAST),Toast.LENGTH_SHORT).show();
					break;
			}
		}
	};

	public void finishDialogNoBluetooth() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.alert_dialog_no_bt).setIcon(android.R.drawable.ic_dialog_info).setTitle(R.string.app_name).setCancelable(false).setPositiveButton(R.string.alert_dialog_ok,new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				finish();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (DEBUG)
			Log.d(LOG_TAG,"onActivityResult Test" + resultCode);
		switch(requestCode) {
			case REQUEST_CONNECT_DEVICE:
				// When DeviceListActivity returns with a device to connect
				if (resultCode == Activity.RESULT_OK) {
					// Get the device MAC address
					String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
					// Get the BLuetoothDevice object
					BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
					// Attempt to connect to the device
					mSerialService.connect(device);
				}
				break;
			case REQUEST_ENABLE_BT:
				// When the request to enable Bluetooth returns
				if (resultCode == Activity.RESULT_OK) {
					Log.d(LOG_TAG,"BT not enabled");
					finishDialogNoBluetooth();
				}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(LOG_TAG,"Pressed KeyDown" + keyCode);
		if (handleControlKey(keyCode,true)) {
			return true;
		}
		else if (isSystemKey(keyCode,event)) {
			// Don't intercept the system keys
			return super.onKeyDown(keyCode,event);
		}
		else if (handleDPad(keyCode,true)) {
			return true;
		}
		// Translate the keyCode into an ASCII character.
		int letter = mKeyListener.keyDown(keyCode,event);
		if (letter >= 0) {
			byte[] buffer = new byte[1];
			buffer[0] = (byte)letter;
			mSerialService.write(buffer);
		}
		return true;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Log.d(LOG_TAG,"Pressed KeyUp" + keyCode);
		if (handleControlKey(keyCode,false)) {
			return true;
		}
		else if (isSystemKey(keyCode,event)) {
			// Don't intercept the system keys
			return super.onKeyUp(keyCode,event);
		}
		else if (handleDPad(keyCode,false)) {
			return true;
		}
		mKeyListener.keyUp(keyCode);
		return true;
	}

	private boolean handleControlKey(int keyCode, boolean down) {
		Log.d(LOG_TAG,"Pressed ControlKey" + keyCode);
		if (keyCode == mControlKeyCode) {
			mKeyListener.handleControlKey(down);
			return true;
		}
		return false;
	}

	/**
	 * Handle dpad left-right-up-down events. Don't handle dpad-center, that's our
	 * control key.
	 * 
	 * @param keyCode
	 * @param down
	 */
	private boolean handleDPad(int keyCode, boolean down) {
		Log.d(LOG_TAG,"Pressed handleDPad Key" + keyCode);
		byte[] buffer = new byte[1];
		if (keyCode < KeyEvent.KEYCODE_DPAD_UP || keyCode > KeyEvent.KEYCODE_DPAD_CENTER) {
			return false;
		}
		if (down) {
			if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
				buffer[0] = '\r';
				mSerialService.write(buffer);
			}
			else {
				char code;
				switch(keyCode) {
					case KeyEvent.KEYCODE_DPAD_UP:
						code = 'A';
						break;
					case KeyEvent.KEYCODE_DPAD_DOWN:
						code = 'B';
						break;
					case KeyEvent.KEYCODE_DPAD_LEFT:
						code = 'D';
						break;
					default:
					case KeyEvent.KEYCODE_DPAD_RIGHT:
						code = 'C';
						break;
				}
				buffer[0] = 27; // ESC
				mSerialService.write(buffer);
				if (mEmulatorView.getKeypadApplicationMode()) {
					buffer[0] = 'O';
					mSerialService.write(buffer);
				}
				else {
					buffer[0] = '[';
					mSerialService.write(buffer);
				}
				buffer[0] = (byte)code;
				mSerialService.write(buffer);
			}
		}
		return true;
	}

	private boolean isSystemKey(int keyCode, KeyEvent event) {
		return event.isSystem();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu,menu);
		mMenuItemConnect = menu.getItem(0);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.connect:
				if (getConnectionState() == BluetoothSerialService.STATE_NONE) {
					// Launch the DeviceListActivity to see devices and do scan
					Intent serverIntent = new Intent(this,DeviceListActivity.class);
					startActivityForResult(serverIntent,REQUEST_CONNECT_DEVICE);
				}
				else if (getConnectionState() == BluetoothSerialService.STATE_CONNECTED) {
					mSerialService.stop();
					mSerialService.start();
				}
				return true;
			case R.id.preferences:
				doPreferences();
				return true;
			case R.id.menu_special_keys:
				doDocumentKeys();
				return true;
			case R.id.item1: {
				// TODO
				setLoc();
				return true;
			}
		}
		return false;
	}

	private void doPreferences() {
		startActivity(new Intent(this,TermPreferences.class));
	}

	private void setColors() {
		int[] scheme = COLOR_SCHEMES[mColorId];
		mEmulatorView.setColors(scheme[0],scheme[1]);
	}

	private void doDocumentKeys() {
		String controlKey = CONTROL_KEY_NAME[mControlKeyId];
		new AlertDialog.Builder(this).setTitle(getString(R.string.title_document_key_press) + " \"" + controlKey + "\" " + getString(R.string.title_document_key_rest)).setMessage(" Space ==> Control-@ (NUL)\n" + " A..Z ==> Control-A..Z\n" + " I ==> Control-I (TAB)\n" + " 1 ==> Control-[ (ESC)\n" + " 5 ==> Control-_\n" + " . ==> Control-\\\n" + " 0 ==> Control-]\n" + " 6 ==> Control-^").show();
	}

	private class FakeData extends Thread {
		public void run() {
			byte[] data = new byte[1];
			data[0] = 0x00;
			while (true) {
				if(mSerialService.getState()==mSerialService.STATE_CONNECTED) {
					mSerialService.write(data);
					Log.e(LOG_TAG,"fake data send");
				}
				else {
					Log.e(LOG_TAG,"fake data not send");
				}
				try {
					Thread.sleep(3000);
				}
				catch (InterruptedException e) {}
			}
		}
	}
}