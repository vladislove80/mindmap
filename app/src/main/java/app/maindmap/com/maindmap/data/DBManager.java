package app.maindmap.com.maindmap.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import app.maindmap.com.maindmap.model.AllMindmapsList;
import app.maindmap.com.maindmap.model.Border;
import app.maindmap.com.maindmap.model.Form;
import app.maindmap.com.maindmap.model.Mindmap;
import app.maindmap.com.maindmap.model.Node;

public class DBManager extends SQLiteOpenHelper {
    final String LOG_TAG = "myLogs";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "mapBD";
    // Tables
    private static final String TABLE_MINDMAPS = "mindmaps";
    private static final String TABLE_NODES = "nodes";
    // Mindmaps Table Columns names
    private static final String MAP_ID = "id";
    private static final String MAP_NAME = "name";
    private static final String MAP_DATE = "date";
    // Nodes Table Column names
    private static final String MINDMAP_ID = "mindmap_id";
    private static final String NODE_TEXT = "text";
    private static final String NODE_FORM = "form";
    private static final String NODE_BORDER = "border";
    private static final String NODE_COLOR = "color";
    private static final String NODE_MARKER = "marker";
    private static final String NODE_NUMBER = "number";
    private static final String NODE_PARENT_NUMBER = "parent_number";

    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "onCreate database");

        String CREATE_MAPS_TABLE = "CREATE TABLE "
                + TABLE_MINDMAPS + "("
                + MAP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MAP_NAME + " TEXT,"
                + MAP_DATE + " TIMESTAMP" + ");";
        db.execSQL(CREATE_MAPS_TABLE);
        String CREATE_NODES_TABLE =  "CREATE TABLE "
                + TABLE_NODES + "("
                + MINDMAP_ID + " INTEGER, "
                + NODE_TEXT + " TEXT, "
                + NODE_FORM + " TEXT, "
                + NODE_BORDER + " TEXT, "
                /*+ NODE_COLOR + "TEXT"
                + NODE_MARKER + "TEXT"*/
                + NODE_NUMBER + " INTEGER, "
                + NODE_PARENT_NUMBER + " INTEGER"
                + ")";
        db.execSQL(CREATE_NODES_TABLE);

        // add test mindmap
        Mindmap gettingStartedMindmap = new Mindmap("Getting started", new Date(),  new ArrayList<Node>());

        ContentValues values = new ContentValues();
        values.put(MAP_NAME, gettingStartedMindmap.getName()); // Mindmap Name
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        values.put(MAP_DATE, dateFormat.format(gettingStartedMindmap.getDate())); // Mindmap date criation
        // Inserting Row
        long rowID = db.insert(TABLE_MINDMAPS, null, values);
        Log.d(LOG_TAG, "map inserted, ID = " + rowID);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        //db.execSQL("DROP TABLES IF EXISTS " + TABLE_MINDMAPS + ", " + TABLE_NODES);
        Log.d(LOG_TAG, "!!!!!!!!!!!000000000000 work onUpgrade 000000000!!!!!!!!!!!!!!");
        // Create tables again
        //onCreate(db);
    }
    public void addMindmap(Mindmap mindmap){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MAP_NAME, mindmap.getName()); // Mindmap Name

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        values.put(MAP_DATE, dateFormat.format(mindmap.getDate())); // Mindmap date criation
        // Inserting Row
        long rowID = db.insert(TABLE_MINDMAPS, null, values);
        Log.d(LOG_TAG, "map inserted, ID = " + rowID);
        db.close(); // Closing database connection
    }
    public void addNode(int MAP_ID, Node node) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MINDMAP_ID, MAP_ID);
        /*id of last insert: SELECT TOP 1 id FROM table ORDER BY id DESC;*/
        values.put(NODE_TEXT, node.getText()); // Node text - Mindemap name !!!
        values.put(NODE_FORM, node.getForm().form); // Node form
        values.put(NODE_BORDER, node.getBorder().border); // Node border
        /*values.put(NODE_COLOR, node.getText()); // Node color
        values.put(NODE_MARKER, node.getText()); // Node marker*/
        values.put(NODE_NUMBER, node.getNumber()); // Node number
        values.put(NODE_PARENT_NUMBER, node.getParentNodeNumber()); // Node parent number
        long rowID = db.insert(TABLE_NODES, null, values);
        Log.d(LOG_TAG, "Node inserted, ID = " + rowID);
        db.close(); // Closing database connection

    }

    public AllMindmapsList getAllMindmaps(){
        ArrayList<Mindmap> mindmapList = new ArrayList<Mindmap>();
        String selectQuery = "SELECT  * FROM " + TABLE_MINDMAPS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {

                int id = Integer.parseInt(c.getString(0));
                String name = c.getString(1);
                Date date = new Date();
                //date.setTime(Date.parse(c.getString(2)));
                Mindmap mindmap = new Mindmap(name, date, new ArrayList<Node>());
                mindmapList.add(mindmap);
            } while (c.moveToNext());
        } else Log.d("myLogs", "Error getAllMindmaps");
        return new AllMindmapsList(mindmapList);
    }
    public int getMindmapsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MINDMAPS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

    public ArrayList<Node> getAllNodes(int MAP_ID){
        ArrayList<Node> nodesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_NODES + " WHERE " + MINDMAP_ID + " = " + MAP_ID + ";";

        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                int id = Integer.parseInt(c.getString(0));
                String text = c.getString(1);
                String form = c.getString(2);
                String border = c.getString(3);
                int number = Integer.parseInt(c.getString(4));
                int parentNumber = Integer.parseInt(c.getString(5));

                Node node = new Node(text);
                node.setForm(new Form(form));
                node.setBorder(new Border(border));
                node.setNumber(number);
                node.setParentNodeNumber(parentNumber);
                nodesList.add(node);
            } while (c.moveToNext());
        } else Log.d("myLogs", "Error getAllNodes");

        return nodesList;
    }

    public HashMap<Integer, String> getIdMindmapByName(String mindMapName){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = new String[]{mindMapName};
        Cursor cursor = db.query(TABLE_MINDMAPS,
                null,
                MAP_NAME + "=?",
                selectionArgs,
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        HashMap<Integer, String> mindmap = new HashMap<>();
        Log.d(LOG_TAG, "!!!!!" + cursor.getCount());
        int mindmap_id = Integer.parseInt(cursor.getString(0));
        String mindmapName = cursor.getString(1);
        mindmap.put(mindmap_id, mindmapName);

        return mindmap;
    }

    public int updateMindmap(Mindmap mindmap){
        return 0;
    }
    public void deleteMindmap(Mindmap mindmap){

    }
}
