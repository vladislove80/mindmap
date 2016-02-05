package app.com.myapp.view;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import app.com.myapp.R;
import app.com.myapp.data.DBManager;
import app.com.myapp.dialog.DialogNewMindmap;
import app.com.myapp.model.AllMindmapsList;
import app.com.myapp.model.Mindmap;

public class FirstFragment extends ListFragment {
    DialogNewMindmap newMindmapDialog;
    private static String FRAGMENT_INSTANCE_NAME = "fragment2";
    private AllMindmapsList allMindmaps;
    private SecondFragment secondFragment;
    private FragmentTransaction transaction;
    String nameMindmap;
    int idMindmap;

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
            //send mindmap ID to second fragment
            idMindmap = position;
            Bundle b = new Bundle();
            b.putInt("idMindmap", idMindmap);
            secondFragment.setArguments(b);
            //add second fragment
            FragmentManager manager = getFragmentManager();
            transaction = manager.beginTransaction();
            transaction.replace(R.id.conteiner, secondFragment, FRAGMENT_INSTANCE_NAME);
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
