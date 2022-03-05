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
	private RLottieImageView lottieImageView2;
	
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
			
		lottieImageView2 = findViewById(R.id.lottieImageView2);
		lottieImageView2.post(new Runnable() {
				@Override
				public void run() {
					loadLottieAnim2();
				}
			});
    }
	
	private void loadLottieAnim() {
		lottieImageView.setAutoRepeat(true);
		lottieImageView.setAnimation(R.raw.loader_desygner, 90, 90);
		lottieImageView.setLayerColor("Shape Layer 1.**", Color.parseColor("#FF7043"));
		lottieImageView.playAnimation();
	}
	
	private void loadLottieAnim2() {
		lottieImageView2.setAutoRepeat(true);
		lottieImageView2.setAnimation(R.raw.top_badge, 90, 90);
		lottieImageView2.playAnimation();
	}
}
