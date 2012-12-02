package io.gamecloud.gamecloudtestclock;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivtiy extends Activity implements Runnable {

	private static final String TAG = "MAIN_ACTIVITY";

	// UI
	private TextView tvSeconds;
	private TextView tvMinutes;
	private TextView tvHours;
	private Button butTakeSnapshot;

	SimpleDateFormat hours_sdf;
	SimpleDateFormat mins_sdf;
	SimpleDateFormat secs_sdf;

	Thread runner;
	Date currentTime;
	private View contentToSend = null;

	final Runnable updater = new Runnable() {
		public void run() {
			updateClockValues();
		};
	};

	final Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupViews();
		setupEvents();

		if (runner == null) { // start the song
			runner = new Thread(this);
			runner.start();
		}

	}

	private void updateClockValues() {
		currentTime = Calendar.getInstance().getTime();

		tvSeconds.setText(hours_sdf.format(currentTime.getTime()));
		tvMinutes.setText(mins_sdf.format(currentTime.getTime()));
		tvHours.setText(secs_sdf.format(currentTime.getTime()));

	}

	private void setupViews() {
		setContentView(R.layout.layout_main_activtiy);

		tvSeconds = (TextView) findViewById(R.id.tv_current_seconds);
		tvMinutes = (TextView) findViewById(R.id.tv_current_minutes);
		tvHours = (TextView) findViewById(R.id.tv_current_hours);
		butTakeSnapshot = (Button) findViewById(R.id.but_take_snapshot);

		hours_sdf = new SimpleDateFormat("HH");
		mins_sdf = new SimpleDateFormat("mm");
		secs_sdf = new SimpleDateFormat("ss");

	}

	private void setupEvents() {

		contentToSend = findViewById(R.id.ll_time);
		contentToSend.setDrawingCacheEnabled(true);

		currentTime = Calendar.getInstance().getTime();

		butTakeSnapshot.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				getScreen(contentToSend);

				Toast.makeText(
						MainActivtiy.this,
						"Snapshot taken and saved to "
								+ "/sdcard/screenshotTikTok.png",
						Toast.LENGTH_LONG).show();

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.layout_main_activtiy, menu);
		return true;
	}

	@Override
	public void run() {
		while (runner != null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			;
			mHandler.post(updater);
		}
	}

	private void getScreen(View content) {
		Bitmap bitmap = content.getDrawingCache();

		File file = new File("/sdcard/screenshotTikTok.png");
		try {
			file.createNewFile();
			FileOutputStream ostream = new FileOutputStream(file);
			bitmap.compress(CompressFormat.PNG, 100, ostream);
			ostream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
