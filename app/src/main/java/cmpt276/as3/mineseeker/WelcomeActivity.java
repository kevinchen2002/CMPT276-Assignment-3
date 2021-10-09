package cmpt276.as3.mineseeker;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {
    private final Timer timer = new Timer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        clockwise();
        continueToMainButton();
        timedStart();
    }

    void continueToMainButton() {
        Button btn = findViewById(R.id.welcome_button);
        btn.setOnClickListener(v -> {
            Intent launchMainMenu = MainMenuActivity.makeIntent(WelcomeActivity.this);
            startActivity(launchMainMenu);
            timer.cancel();
        });
    }

    /**
     * Basic timer functionality learned from https://www.youtube.com/watch?v=KyeS8uuahiU
     */
    void timedStart() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent launchMainMenu = MainMenuActivity.makeIntent(WelcomeActivity.this);
                startActivity(launchMainMenu);
                timer.cancel();
            }
        };
        timer.schedule(timerTask, 5000, 5000);
    }

    /**
     * Animations learned from https://www.tutorialspoint.com/android/android_animations.htm
     */
    void clockwise() {
        ImageView image = findViewById(R.id.exampleImage);
        Animation clockwiseSpin =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clockwise);
        image.startAnimation(clockwiseSpin);
    }
}