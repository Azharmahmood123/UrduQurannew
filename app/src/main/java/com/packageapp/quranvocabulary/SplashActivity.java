package com.packageapp.quranvocabulary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.QuranReading.urduquran.R;

/**
 * Created by cyber on 1/19/2016.
 */
public class SplashActivity extends AppCompatActivity {

    private Runnable r;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_quranvocabulary);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        isActive = true;
        splashThread();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        isActive = false;
        handler.removeCallbacks(r);

    }

    public void splashThread() {
        r=new Runnable() {
            @Override
            public void run() {
//                if (isActive) {
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                }
                SplashActivity.this.finish();
            }
        };

        handler.postDelayed(r, 1000);
    }
}
