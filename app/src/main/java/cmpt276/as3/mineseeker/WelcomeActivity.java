package cmpt276.as3.mineseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        Log.e("OrientationDemo", "Running onCreate()!");
    }

    void continueToMainButton() {
        Button btn = findViewById(R.id.welcome_button);
        btn.setOnClickListener(v -> {
            //Intent launch
        });
    }
}