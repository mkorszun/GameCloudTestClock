package io.gamecloud.tiktok.activities;

import io.gamecloud.tiktok.R;
import io.gamecloud.tiktok.toolbar.ConstantsConfig;

import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class SplashActivity extends BaseActivity {

	private static final String TAG = "SPLASH_ACTIVITY";

	protected boolean _active = true;
	protected int _splashTime = 2000;

	private RelativeLayout _rlSplash = null;
	private AtomicBoolean _abStarted;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(setupContentLayout());
		ConstantsConfig constantsConfig = new ConstantsConfig();

		_abStarted = new AtomicBoolean();
		setupViews();
		setupEvents();

	}

	@Override
	protected int setupContentLayout() {
		return R.layout.splash_activity;
	}

	@Override
	protected void setupViews() {
		_rlSplash = (RelativeLayout) findViewById(R.id.rl_splash);
	}

	@Override
	protected void setupEvents() {
		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (_active && (waited < _splashTime)) {
						sleep(100);
						if (_active) {
							waited += 100;
						}
					}
				} catch (InterruptedException e) {

				} finally {
					finish();

					startActivity(new Intent(SplashActivity.this,
							MainActivtiy.class));
				}
			}
		};
		splashTread.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			_active = false;
		}
		return true;
	}

	@Override
	protected void setupResume() {

	}

	@Override
	protected void setupPause() {

	}

	@Override
	protected void setupDestroy() {

	}

}
