package app.maindmap.com.maindmap.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import app.maindmap.com.maindmap.R;

public class DialogNewMindmap extends DialogFragment implements OnClickListener {
    private String newMindamapName;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder yes = adb.setView(inflater.inflate(R.layout.newmapname, null))
                .setTitle("Title of the map:")
                .setPositiveButton("Yes", this)
                .setNegativeButton(R.string.no, this);

        return adb.create();
    }

    public void onClick(DialogInterface dialog, int which) {

        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                newMindamapName = ((EditText) dialog).getText().toString();//?????
                break;
            case Dialog.BUTTON_NEGATIVE:

                break;
        }

    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
    public String getNewName(){
        return newMindamapName;
    }


}
