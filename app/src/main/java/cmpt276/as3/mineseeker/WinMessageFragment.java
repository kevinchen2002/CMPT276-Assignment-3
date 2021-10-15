package cmpt276.as3.mineseeker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

/**
 * TODO: [Josh insert stuff here]
 */
public class WinMessageFragment extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.win_message, null);


        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requireActivity().finish();
            }
        };
        AlertDialog winMessage = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok, listener)
                .show();
        TextView fragmentHeader = (TextView)winMessage.findViewById(android.R.id.button1);
        Typeface pokeFont = getResources().getFont(R.font.pokemon_font);
        fragmentHeader.setTypeface(pokeFont);


        return winMessage;

    }
}
