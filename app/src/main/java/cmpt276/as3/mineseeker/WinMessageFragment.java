package cmpt276.as3.mineseeker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

/**
 * WinMessageFragment shows a nice message and a picture of Professor Oak
 * when the player gets all the Pokemon.
 */
public class WinMessageFragment extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.win_message, null);
        DialogInterface.OnClickListener listener = (dialog, which) -> requireActivity().finish();

        AlertDialog winMessage = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok, listener)
                .show();
        TextView fragmentHeader = winMessage.findViewById(android.R.id.button1);
        Typeface pokeFont = getResources().getFont(R.font.pokemon_font);
        fragmentHeader.setTypeface(pokeFont);

        return winMessage;

    }
}
