package cmpt276.as3.mineseeker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatDialogFragment;

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

        return new AlertDialog.Builder(getActivity())
                .setTitle("Congratulations!")
                .setView(v)
                .setPositiveButton(android.R.string.ok, listener)
                .create();
    }
}
