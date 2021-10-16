package cmpt276.as3.mineseeker;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * WelcomeActivity is the activity seen upon startup.
 * It plays a brief animation and continues when said animation is complete.
 * The user can also press a button to manually continue to MainMenuActivity.
 */
public class WelcomeActivity extends AppCompatActivity {
    private final Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        clockwise(R.id.welcomePokeball);
        slideFromLeft(R.id.welcomeHorsea);
        slideFromRight(R.id.welcomeTurtwig);
        slideFromBottom(R.id.welcomeTitle);
        continueToMainButton();
        timedStart();
    }

    void continueToMainButton() {
        Button btn = findViewById(R.id.welcome_button);
        btn.setOnClickListener(v -> launchToMainMenu());
    }

    void timedStart() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                launchToMainMenu();
            }
        };
        timer.schedule(timerTask, 9000, 18000);
    }

    void launchToMainMenu() {
        Intent launchMainMenu = MainMenuActivity.makeIntent(WelcomeActivity.this);
        startActivity(launchMainMenu);
        playAudio(R.raw.pokeball_pop);
        timer.cancel();
    }

    /*
     * Animations learned from https://www.tutorialspoint.com/android/android_animations.htm
     */
    void clockwise(int id) {
        ImageView image = findViewById(id);
        Animation clockwiseSpin =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clockwise);
        image.startAnimation(clockwiseSpin);
    }

    void slideFromLeft(int id) {
        ImageView image = findViewById(id);
        Animation slideFromLeft =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_from_left);
        image.startAnimation(slideFromLeft);
    }

    void slideFromRight(int id) {
        ImageView image = findViewById(id);
        Animation slideFromRight =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_from_right);
        image.startAnimation(slideFromRight);
    }

    void slideFromBottom(int id) {
        ImageView image = findViewById(id);
        Animation slideFromBottom =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_from_bottom);
        image.startAnimation(slideFromBottom);
    }

    private void playAudio (int id) {
        MediaPlayer mp = MediaPlayer.create(this, id);
        mp.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }
}