package app.maindmap.com.maindmap;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import app.maindmap.com.maindmap.data.DBManager;
import app.maindmap.com.maindmap.view.FirstFragment;

public class MainActivity extends FragmentActivity {

    private FirstFragment firstFragment;
    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Log.d("myLogs", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!START!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        DBManager helper = new DBManager(this);

        //test database
       /* Mindmap gettingStartedMindmap = new Mindmap("Getting started", new Date(),  new ArrayList<Node>());
        Mindmap test1 = new Mindmap("TestMindMap", new Date(), new ArrayList<Node>());
        Mindmap test2 = new Mindmap("SomeName", new Date(), new ArrayList<Node>());
        Mindmap test3 = new Mindmap("MyMindmap", new Date(), new ArrayList<Node>());
        Mindmap test4 = new Mindmap("Mymindmap2", new Date(), new ArrayList<Node>());

        helper.addMindmap(gettingStartedMindmap);
        helper.addMindmap(test1);
        helper.addMindmap(test2);
        helper.addMindmap(test3);
        helper.addMindmap(test4);
        Node testNode = new Node("Node 1");
        testNode.setForm(new Form("Ellipse"));
        testNode.setBorder(new Border("Thin"));
        testNode.setNumber(1);
        testNode.setParentNodeNumber(4);
        helper.addNode(1, testNode);
        testNode = new Node("Node 2");
        testNode.setForm(new Form("Ellipse"));
        testNode.setBorder(new Border("Thin"));
        testNode.setNumber(2);
        testNode.setParentNodeNumber(12);
        helper.addNode(1, testNode);
        testNode = new Node("Node 3");
        testNode.setForm(new Form("Ellipse"));
        testNode.setBorder(new Border("Thin"));
        testNode.setNumber(3);
        testNode.setParentNodeNumber(7);
        helper.addNode(2, testNode);

        AllMindmapsList allMindmaps = helper.getAllMindmaps();

        String[] mindmapsList = new String[allMindmaps.getAllMapList().size()+1];
        mindmapsList[0] = "+";
        int i = 1;
        for(Mindmap mp : allMindmaps.getAllMapList()) {
            mindmapsList[i] = mp.getName();
            Log.d("myLogs", "Mindmap !!! from database " + mindmapsList[i]);
            i++;
        }

        HashMap<Integer, String> map = helper.getIdMindmapByName("SomeName");
        for(Map.Entry<Integer, String> pair : map.entrySet()){
            Integer key = pair.getKey();
            String name = pair.getValue();
            Log.d("myLogs", "ID = " + key + ", name = " + name);
        }*/

        /*Log.d("myLogs", "N O D E S from database");
        ArrayList<Node> allNodes = helper.getAllNodes(1);

        int i = 0;
        for(Node node : allNodes) {
            Log.d("myLogs", node.toString());
        }*/
        //SQLiteDatabase db = helper.getWritableDatabase();

        /*String selectQuery = "SELECT  * FROM " + "nodes";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                int idM = Integer.parseInt(c.getString(0));
                String text = c.getString(1);
                String form = c.getString(2);
                String border = c.getString(3);
                int num = Integer.parseInt(c.getString(4));
                int pnum = Integer.parseInt(c.getString(5));

                Log.d("myLogs",
                        "ID = " + idM +
                                ", text = " + text +
                                ", form = " + form +
                                ", border = " + border +
                                ", num = " + num +
                                ", pnum = " + pnum

                );
            } while (c.moveToNext());
        } else Log.d("myLogs", "000000000000000000000000000000000");
        c.close();*/

        //add fragment
        manager = getSupportFragmentManager();
        firstFragment = new FirstFragment();
        transaction = manager.beginTransaction();
        transaction.add(R.id.conteiner, firstFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
