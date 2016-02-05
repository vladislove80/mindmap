package app.com.myapp.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import app.com.myapp.R;

public class DialogNewNode extends DialogFragment {
    private EditText newNodeName;
    public static final String TAG_NEW_TEXT = "NewNodeText";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.newnodename, null);
        newNodeName = (EditText) view.findViewById(R.id.newNodeName);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(view)
                .setTitle("Text")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //отправляем результат обратно
                        Intent intent = new Intent();
                        intent.putExtra(TAG_NEW_TEXT, newNodeName.getText().toString());
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    }
                });
        return builder.create();
    }
}
