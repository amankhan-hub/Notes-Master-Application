package com.example.quicknote;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ImageView logo;
        TextView textView;
        Animation logoanim,textanim;
        logo=findViewById(R.id.logo);
        textView=findViewById(R.id.logotext);
        logoanim= AnimationUtils.loadAnimation(this,R.anim.logoanimation);
        logo.startAnimation(logoanim);
        textanim=AnimationUtils.loadAnimation(this,R.anim.textanimation);
        textView.startAnimation(textanim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(Splash_Screen.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },1000);
    }
}