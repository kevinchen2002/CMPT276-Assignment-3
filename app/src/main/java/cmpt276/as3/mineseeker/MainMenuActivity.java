package cmpt276.as3.mineseeker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);

        continueToGameButton();
    }

    void continueToGameButton() {
        Button btn = findViewById(R.id.main_to_game_btn);
        btn.setOnClickListener(v -> {
            Intent launchGame = GameActivity.makeIntent(MainMenuActivity.this);
            startActivity(launchGame);
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, MainMenuActivity.class);
    }
}
