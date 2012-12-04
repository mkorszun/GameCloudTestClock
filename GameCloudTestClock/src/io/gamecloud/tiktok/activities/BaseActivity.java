package io.gamecloud.tiktok.activities;

import java.util.concurrent.atomic.AtomicBoolean;
import io.gamecloud.tiktok.R;
import io.gamecloud.tiktok.toolbar.ConstantsConfig;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Window;

public abstract class BaseActivity extends Activity {

	private static final String TAG = "BASE_ACTIVITY";
	
	protected static AtomicBoolean abDialogShowing;
	protected AlertDialog alert;
	public static ConstantsConfig constantsConfig;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		abDialogShowing = new AtomicBoolean();
		constantsConfig = new ConstantsConfig();

		checkNetworks();

	}

	private void setupCoreViews() {

	}

	private void setupCoreEvents() {

	}

	@Override
	protected void onResume() {
		super.onResume();

		checkNetworks();
		setupResume();
	}

	@Override
	protected void onPause() {
		super.onPause();

		setupPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	protected void onDestroy() {
		super.onDestroy();
		setupDestroy();
	}

	protected abstract int setupContentLayout();

	protected abstract void setupViews();

	protected abstract void setupEvents();

	protected abstract void setupResume();

	protected abstract void setupPause();

	protected abstract void setupDestroy();

	protected void checkNetworks() {
		if (isWifi() == false && abDialogShowing.compareAndSet(false, true)) {
			dialogNoWifi();
		}
	}

	protected boolean isWifi() {
		ConnectivityManager hConnMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean bInetWifi = hConnMngr.getNetworkInfo(
				ConnectivityManager.TYPE_WIFI).isConnected();
		boolean bInetMobile = hConnMngr.getNetworkInfo(
				ConnectivityManager.TYPE_MOBILE).isConnected();
		if (bInetWifi || bInetMobile) {
			return true;
		} else {
			return false;
		}
	}

	private void dialogNoWifi() {

		AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
		adBuilder.setTitle(getResources().getString(R.string.s_alert));
		adBuilder.setMessage(getResources()
				.getString(R.string.s_no_int_message));
		adBuilder.setCancelable(false);
		adBuilder.setNegativeButton(getResources().getString(R.string.s_exit),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface diDialog, int id) {
						abDialogShowing.set(false);

						finish();

					}
				});
		adBuilder.setNeutralButton(
				getResources().getString(R.string.s_wifi_settings),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface diDialog, int id) {
						alert.dismiss();
						abDialogShowing.set(false);
						Intent iIntent = new Intent(
								android.provider.Settings.ACTION_WIRELESS_SETTINGS);
						iIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						startActivity(iIntent);
						// finish();
					}
				});

		alert = adBuilder.create();
		alert.show();
	}

}
