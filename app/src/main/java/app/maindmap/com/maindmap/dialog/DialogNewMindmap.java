package app.maindmap.com.maindmap.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Date;

import app.maindmap.com.maindmap.R;
import app.maindmap.com.maindmap.data.DBManager;
import app.maindmap.com.maindmap.model.Mindmap;
import app.maindmap.com.maindmap.model.Node;
import app.maindmap.com.maindmap.view.SecondFragment;

public class DialogNewMindmap extends DialogFragment{

    private EditText atNewMindmapName;
    private SecondFragment secondFragment;
    private FragmentTransaction transaction;
    private FragmentManager manager;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        View view = inflater.inflate(R.layout.newmapname, null);
        atNewMindmapName = (EditText) view.findViewById(R.id.editNewMaindmapName);
        AlertDialog.Builder yes = adb.setView(view);

        yes.setTitle("Title of the map:");
        yes.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String newMindamapName = atNewMindmapName.getText().toString(); //new mindmap name from dialog edittext
                DBManager helper = new DBManager(getActivity());
                Log.d("myLogs", "New map added to database !!!");
                Mindmap newMindmap = new Mindmap(newMindamapName, new Date(), new ArrayList<Node>());
                helper.addMindmap(newMindmap); //add new mind map to database

                //add second fragment
                secondFragment = new SecondFragment();
                //send new mindmap name to second fragment
                Bundle b = new Bundle();
                b.putString("MindmapName", newMindamapName);
                secondFragment.setArguments(b);

                manager = getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.conteiner, secondFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        yes.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        return adb.create();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }



}
