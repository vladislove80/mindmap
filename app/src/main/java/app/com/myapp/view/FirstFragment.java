package app.com.myapp.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import app.com.myapp.R;
import app.com.myapp.data.DBManager;
import app.com.myapp.dialog.DialogDeleteMindmap;
import app.com.myapp.dialog.DialogNewMindmap;
import app.com.myapp.model.AllMindmapsList;
import app.com.myapp.model.Mindmap;

public class FirstFragment extends ListFragment {
    private final String LOG_TAG = "myLogs";
    private final int MENU_EDIT_MINDMAP = 1;
    private final int MENU_DELETE_MINDMAP = 2;
    private static final int REQUEST_CODE_DELETE_MINDMAP = 1;
    private DialogNewMindmap newMindmapDialog;
    private String[] mindmapsList;
    private static String FRAGMENT_INSTANCE_NAME = "fragment2";
    private AllMindmapsList allMindmaps;
    private SecondFragment secondFragment;
    private FragmentTransaction transaction;
    private int idMindmap;
    private TextView textView;
    private ListView lvMain;
    private View mainView;
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.mainlist0, null);
        lvMain = (ListView) mainView.findViewById(android.R.id.list);
        mindmapsList = initData();
        adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, mindmapsList);
        lvMain.setAdapter(adapter);
        registerForContextMenu(lvMain);
        return mainView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (position == 0){
            //dialog to create new mindmap
            newMindmapDialog = new DialogNewMindmap();
            newMindmapDialog.show(getFragmentManager(), "dialog !!");
        } else { //choose existing minmap
            secondFragment = new SecondFragment();
            //send mindmap ID to second fragment
            //(position - 1) in mindmap_list complies with position in listID
            idMindmap = allMindmaps.getAllMindmapsID().get(position - 1);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // Get the list item position
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        int position = info.position;
        //(position - 1) in mindmap_list complies with position in listID
        idMindmap = allMindmaps.getAllMindmapsID().get(position - 1);
        if (v.getId() == android.R.id.list && position > 1) {
                menu.add(0, MENU_EDIT_MINDMAP, 0, "Edit name");
                menu.add(0, MENU_DELETE_MINDMAP, 0, "Delete");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            // пункты меню для tvColor
            case MENU_EDIT_MINDMAP:

                break;
            case MENU_DELETE_MINDMAP:
                DialogFragment df = new DialogDeleteMindmap();
                df.setTargetFragment(this, REQUEST_CODE_DELETE_MINDMAP);
                df.show(getFragmentManager(), df.getClass().getName());
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_DELETE_MINDMAP:
                    Toast.makeText(getActivity(), "CLick-Click!!", Toast.LENGTH_LONG).show();
                    deleteMindmap(idMindmap);
                    mindmapsList = initData();
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    private void deleteMindmap(int id_mindmap){
        DBManager helper = new DBManager(getContext());
        helper.deleteMindmap(id_mindmap);
    }
}
