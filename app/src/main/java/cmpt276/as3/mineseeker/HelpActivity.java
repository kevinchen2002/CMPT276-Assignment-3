package cmpt276.as3.mineseeker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity);

        showHyperlink();
    }

    void showHyperlink() {
        TextView hyperlink = findViewById(R.id.helpText);
        hyperlink.setMovementMethod(LinkMovementMethod.getInstance());
        hyperlink.setLinkTextColor(Color.BLUE);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, HelpActivity.class);
    }
}
