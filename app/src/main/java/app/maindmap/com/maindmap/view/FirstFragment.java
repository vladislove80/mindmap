package app.maindmap.com.maindmap.view;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import app.maindmap.com.maindmap.R;
import app.maindmap.com.maindmap.data.DBManager;
import app.maindmap.com.maindmap.dialog.DialogNewMindmap;
import app.maindmap.com.maindmap.model.AllMindmapsList;
import app.maindmap.com.maindmap.model.Mindmap;

public class FirstFragment extends ListFragment {

    DialogNewMindmap newMindmapDialog;

    private AllMindmapsList allMindmaps;
    private SecondFragment secondFragment;
    private FragmentTransaction transaction;
    String nameMindmap;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //list of mindmaps from database
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.mainlist, initData());
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (position == 0){
            //dialog to create new mindmap
                newMindmapDialog = new DialogNewMindmap();
                newMindmapDialog.show(getFragmentManager(), "dialog !!");
        } else { //choose existing minmap
            TextView tv = (TextView)v.findViewById(R.id.textMinmapName);
            nameMindmap = tv.getText().toString();
            secondFragment = new SecondFragment();
            //send mindmap name to second fragment
            Bundle b = new Bundle();
            b.putString("MindmapName", nameMindmap);
            secondFragment.setArguments(b);
            //add second fragment
            FragmentManager manager = getFragmentManager();
            transaction = manager.beginTransaction();
            transaction.replace(R.id.conteiner, secondFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
//get mindmaps from database for list
    private String[] initData(){
        DBManager helper = new DBManager(getContext());
        allMindmaps = helper.getAllMindmaps();
        String[] mindmapsList = new String[allMindmaps.getAllMapList().size()+1];
        mindmapsList[0] = "+";
        int i = 1;
        for(Mindmap mp : allMindmaps.getAllMapList()) {
            mindmapsList[i] = mp.getName();
            i++;
        }
        return mindmapsList;
    }
}
