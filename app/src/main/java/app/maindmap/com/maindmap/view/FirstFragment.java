package app.maindmap.com.maindmap.view;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import app.maindmap.com.maindmap.R;
import app.maindmap.com.maindmap.dialog.DialogNewMindmap;
import app.maindmap.com.maindmap.model.AllMindmapsList;
import app.maindmap.com.maindmap.model.Mindmap;
import app.maindmap.com.maindmap.model.Node;

public class FirstFragment extends ListFragment {

    DialogNewMindmap newMindmapDialog;

    private AllMindmapsList allMindmapsList;
    private SecondFragment secondFragment;
    private FragmentTransaction transaction;
    String nameMindmap;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.mainlist, initData());
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (position == 0){
                newMindmapDialog = new DialogNewMindmap();
                newMindmapDialog.show(getFragmentManager(), "dialog !!");
                nameMindmap = newMindmapDialog.getNewName();
        } else {
            TextView tv = (TextView)v.findViewById(R.id.textMinmapName);
            nameMindmap = tv.getText().toString();
        }
            secondFragment = new SecondFragment();
            Bundle b = new Bundle();
            b.putString("MindmapName", nameMindmap);
            secondFragment.setArguments(b);

            FragmentManager manager = getFragmentManager();
            transaction = manager.beginTransaction();
            transaction.replace(R.id.conteiner, secondFragment);
            transaction.addToBackStack(null);
            transaction.commit();
    }

    private String[] initData(){
        Mindmap nmMP1 = new Mindmap("+ new", new java.util.Date (), new ArrayList<Node>());
        Mindmap nmMP2 = new Mindmap("Mindmap 1", new java.util.Date(), new ArrayList<Node>());
        Mindmap nmMP3 = new Mindmap("Mindmap 2", new java.util.Date(), new ArrayList<Node>());
        Mindmap nmMP4 = new Mindmap("Mindmap 3", new java.util.Date(), new ArrayList<Node>());

        ArrayList<Mindmap> arListMMP = new ArrayList<>();
        arListMMP.add(nmMP1);
        arListMMP.add(nmMP2);
        arListMMP.add(nmMP3);
        arListMMP.add(nmMP4);
        allMindmapsList = new AllMindmapsList(arListMMP);
        String[] str = new String[allMindmapsList.getAllMapList().size()];
        int i = 0;
        for(Mindmap mp : allMindmapsList.getAllMapList()) {
            str[i] = mp.getName();
            i++;
        }
        return str;
    }
}
