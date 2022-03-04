package com.itsrohit.rlottiesample.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.view.Gravity;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;

import com.itsrohit.rlottiesample.R;

import com.itsrohit.rlottie.RLottieImageView;
import com.itsrohit.rlottie.RLottieDrawable;
import com.itsrohit.rlottie.RLottie;

public class MainActivity extends AppCompatActivity {

	private RLottieImageView lottieImageView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		lottieImageView = findViewById(R.id.lottieImageView);
		lottieImageView.post(new Runnable() {
				@Override
				public void run() {
					loadLottieAnim();
				}
			});
    }
	
	private void loadLottieAnim() {
		lottieImageView.setAutoRepeat(true);
		lottieImageView.setAnimation(R.raw.loader_desygner, 90, 90);
		lottieImageView.setLayerColor("Shape Layer 1.**", Color.parseColor("#FF7043"));
		lottieImageView.playAnimation();
	}
}
