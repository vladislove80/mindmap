package app.com.myapp.dialog;

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

import app.com.myapp.R;
import app.com.myapp.data.DBManager;
import app.com.myapp.model.AllMindmapsList;
import app.com.myapp.model.Mindmap;
import app.com.myapp.model.Node;
import app.com.myapp.view.SecondFragment;

public class DialogNewMindmap extends DialogFragment{

    private EditText atNewMindmapName;
    private SecondFragment secondFragment;
    private FragmentTransaction transaction;
    private FragmentManager manager;
    private static String FRAGMENT_INSTANCE_NAME = "fragment2";
    int idMindmap;
    private AllMindmapsList allMindmaps;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        View view = inflater.inflate(R.layout.newmapname, null);
        atNewMindmapName = (EditText) view.findViewById(R.id.editNewMaindmapName);
        AlertDialog.Builder yes = adb.setView(view);

        yes.setTitle("Title of the map:");
        yes.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String newMindamapName = atNewMindmapName.getText().toString(); //new mindmap name from dialog edittext
                if(!newMindamapName.equals("")){
                    DBManager helper = new DBManager(getActivity());
                    //get mindmap ID
                    ArrayList<Node> nodeZeroList = new ArrayList<>();
                    Node nodeZero = new Node(newMindamapName);
                    nodeZeroList.add(nodeZero);
                    Mindmap newMindmap = new Mindmap(newMindamapName, new Date(), nodeZeroList);
                    helper.addMindmap(newMindmap); //add new mind map to database
                    Log.d("myLogs", "New map added to database !!!");

                    allMindmaps = helper.getAllMindmaps();
                    //get last elemt(mindmap ID) from ArrayList<Integer> allMindmapsID, that just was added
                    idMindmap = allMindmaps.getAllMindmapsID().get(allMindmaps.getAllMapList().size()-1);
                    //add node to NodesTable with mindmap_id
                    helper.addNode(idMindmap, nodeZero);
                    //add second fragment
                    secondFragment = new SecondFragment();
                    //send new mindmap ID to second fragment
                    Bundle b = new Bundle();
                    b.putInt("idMindmap", idMindmap);
                    secondFragment.setArguments(b);

                    manager = getFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.conteiner, secondFragment, FRAGMENT_INSTANCE_NAME);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
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
