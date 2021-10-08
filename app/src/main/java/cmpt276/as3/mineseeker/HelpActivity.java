package cmpt276.as3.mineseeker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, HelpActivity.class);
    }
}
