package app.com.myapp.data;

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

import app.com.myapp.model.AllMindmapsList;
import app.com.myapp.model.Mindmap;
import app.com.myapp.model.Node;

public class DBManager extends SQLiteOpenHelper{
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
    private static final String NODE_CENTER_X = "center_X";
    private static final String NODE_CENTER_Y = "center_Y";


    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "onCreate database");

        String CREATE_MAPS_TABLE = "CREATE TABLE "
                + TABLE_MINDMAPS + "("
                + MAP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MAP_NAME + " TEXT, "
                + MAP_DATE + " TIMESTAMP" + ");";
        db.execSQL(CREATE_MAPS_TABLE);
        String CREATE_NODES_TABLE =  "CREATE TABLE "
                + TABLE_NODES + "("
                + MINDMAP_ID + " INTEGER, "
                + NODE_TEXT + " TEXT, "
                + NODE_FORM + " TEXT, "
                + NODE_BORDER + " TEXT, "
                + NODE_COLOR + " INTEGER, "
                /*+ NODE_MARKER + "TEXT"*/
                + NODE_NUMBER + " INTEGER, "
                + NODE_PARENT_NUMBER + " INTEGER, "
                + NODE_CENTER_X + " INTEGER, "
                + NODE_CENTER_Y + " INTEGER"
                + ")";
        db.execSQL(CREATE_NODES_TABLE);

        // add "Getting started" mindmap
        ArrayList<Node> startNodes = new ArrayList<Node>();

        Node node = new Node("Getting started", "Rectangle", "Red", 0, -1, 100, 180);
        startNodes.add(new Node("Getting started", "Rectangle", "Red", 0, -1, 100, 180));
        startNodes.add(new Node("What is mindmap?", "Rectangle", "Red", 1, 0, 20, 20));
        startNodes.add(new Node("Add topics", "Rectangle", "Red", 2, 0, 250, 40));
        startNodes.add(new Node("Navigate", "Rectangle", "Red", 3, 0, 250, 300));
        startNodes.add(new Node("Customize", "Rectangle", "Red", 4, 0, 50, 350));
        startNodes.add(new Node("Shape", "Ellipse", "Red", 5, 4, 60, 420));
        startNodes.add(new Node("Marker", "Circle", "Red", 6, 4, 180, 450));

        ContentValues values = new ContentValues();
        /****************/
        values.put(MINDMAP_ID, 1);
        /*id of last insert: SELECT TOP 1 id FROM table ORDER BY id DESC;*/
        values.put(NODE_TEXT, "Getting started"); // Node text - Mindemap name !!!
        values.put(NODE_FORM, "Rectangle"); // Node form
        values.put(NODE_BORDER, "Red"); // Node border
        values.put(NODE_COLOR, node.getColor()); // Node color
        /* values.put(NODE_MARKER, node.getText()); // Node marker*/
        values.put(NODE_NUMBER, 0); // Node number
        values.put(NODE_PARENT_NUMBER, -1); // Node parent number
        values.put(NODE_CENTER_X, 100); // Node center coordinate X
        values.put(NODE_CENTER_Y, 180); // Node center coordinate Y
        long rowID = db.insert(TABLE_NODES, null, values);
        Log.d(LOG_TAG, "nodep inserted, ID = " + rowID);
        /****************/
        values = new ContentValues();
        values.put(MINDMAP_ID, 1);
        values.put(NODE_TEXT, "What is mindmap?");
        values.put(NODE_FORM, "Rectangle");
        values.put(NODE_BORDER, "Red");
        values.put(NODE_COLOR, node.getColor()); // Node color
        /* values.put(NODE_MARKER, node.getText()); // Node marker*/
        values.put(NODE_NUMBER, 1);
        values.put(NODE_PARENT_NUMBER, 0);
        values.put(NODE_CENTER_X, 20);
        values.put(NODE_CENTER_Y, 20);
        rowID = db.insert(TABLE_NODES, null, values);
        Log.d(LOG_TAG, "nodep inserted, ID = " + rowID);
        /****************/
        values = new ContentValues();
        values.put(MINDMAP_ID, 1);
        values.put(NODE_TEXT, "Add topics");
        values.put(NODE_FORM, "Rectangle");
        values.put(NODE_BORDER, "Red");
        values.put(NODE_COLOR, node.getColor()); // Node color
        /* values.put(NODE_MARKER, node.getText()); // Node marker*/
        values.put(NODE_NUMBER, 2);
        values.put(NODE_PARENT_NUMBER, 0);
        values.put(NODE_CENTER_X, 250);
        values.put(NODE_CENTER_Y, 40);
        rowID = db.insert(TABLE_NODES, null, values);
        Log.d(LOG_TAG, "nodep inserted, ID = " + rowID);
        /****************/
        values = new ContentValues();
        values.put(MINDMAP_ID, 1);
        values.put(NODE_TEXT, "Navigate");
        values.put(NODE_FORM, "Rectangle");
        values.put(NODE_BORDER, "Red");
        values.put(NODE_COLOR, node.getColor()); // Node color
        /* values.put(NODE_MARKER, node.getText()); // Node marker*/
        values.put(NODE_NUMBER, 3);
        values.put(NODE_PARENT_NUMBER, 0);
        values.put(NODE_CENTER_X, 250);
        values.put(NODE_CENTER_Y, 300);
        rowID = db.insert(TABLE_NODES, null, values);
        Log.d(LOG_TAG, "nodep inserted, ID = " + rowID);
        /****************/
        values = new ContentValues();
        values.put(MINDMAP_ID, 1);
        values.put(NODE_TEXT, "Customize");
        values.put(NODE_FORM, "Rectangle");
        values.put(NODE_BORDER, "Red");
        values.put(NODE_COLOR, node.getColor()); // Node color
        /* values.put(NODE_MARKER, node.getText()); // Node marker*/
        values.put(NODE_NUMBER, 4);
        values.put(NODE_PARENT_NUMBER, 0);
        values.put(NODE_CENTER_X, 50);
        values.put(NODE_CENTER_Y, 350);
        rowID = db.insert(TABLE_NODES, null, values);
        Log.d(LOG_TAG, "nodep inserted, ID = " + rowID);
        /****************/
        values = new ContentValues();
        values.put(MINDMAP_ID, 1);
        values.put(NODE_TEXT, "Shape");
        values.put(NODE_FORM, "Ellipse");
        values.put(NODE_BORDER, "Red");
        values.put(NODE_COLOR, node.getColor()); // Node color
        /* values.put(NODE_MARKER, node.getText()); // Node marker*/
        values.put(NODE_NUMBER, 5);
        values.put(NODE_PARENT_NUMBER, 4);
        values.put(NODE_CENTER_X, 60);
        values.put(NODE_CENTER_Y, 420);
        rowID = db.insert(TABLE_NODES, null, values);
        Log.d(LOG_TAG, "nodep inserted, ID = " + rowID);
        /****************/
        values = new ContentValues();
        values.put(MINDMAP_ID, 1);
        values.put(NODE_TEXT, "Marker");
        values.put(NODE_FORM, "RoundRect");
        values.put(NODE_BORDER, "Red");
        values.put(NODE_COLOR, node.getColor()); // Node color
        /* values.put(NODE_MARKER, node.getText()); // Node marker*/
        values.put(NODE_NUMBER, 6);
        values.put(NODE_PARENT_NUMBER, 4);
        values.put(NODE_CENTER_X, 180);
        values.put(NODE_CENTER_Y, 450);
        rowID = db.insert(TABLE_NODES, null, values);
        Log.d(LOG_TAG, "nodep inserted, ID = " + rowID);

        Mindmap gettingStartedMindmap = new Mindmap("Getting started", new Date(), startNodes);
        values = new ContentValues();
        values.put(MAP_NAME, gettingStartedMindmap.getName()); // Mindmap Name
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        values.put(MAP_DATE, dateFormat.format(gettingStartedMindmap.getDate())); // Mindmap date criation
        // Inserting Row
        rowID = db.insert(TABLE_MINDMAPS, null, values);
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
        values.put(NODE_FORM, node.getForm()); // Node form
        values.put(NODE_BORDER, node.getBorder()); // Node border
        values.put(NODE_COLOR, node.getColor()); // Node color
        /* values.put(NODE_MARKER, node.getText()); // Node marker*/
        values.put(NODE_NUMBER, node.getNumber()); // Node number
        values.put(NODE_PARENT_NUMBER, node.getParentNodeNumber()); // Node parent number
        values.put(NODE_CENTER_X, node.getCenterX()); // Node center coordinate X
        values.put(NODE_CENTER_Y, node.getCenterY()); // Node center coordinate Y
        long rowID = db.insert(TABLE_NODES, null, values);
        Log.d(LOG_TAG, "Node inserted, ID = " + rowID);
        db.close(); // Closing database connection
    }

    public AllMindmapsList getAllMindmaps(){
        ArrayList<Mindmap> mindmapList = new ArrayList<Mindmap>();
        ArrayList<Integer> allMindmapsID = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_MINDMAPS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                int id = Integer.parseInt(c.getString(0));
                allMindmapsID.add(id);
                String name = c.getString(1);
                Date date = new Date();
                //date.setTime(Date.parse(c.getString(2)));
                Mindmap mindmap = new Mindmap(name, date, new ArrayList<Node>());
                mindmapList.add(mindmap);
            } while (c.moveToNext());
        } else Log.d("myLogs", "Error getAllMindmaps");
        return new AllMindmapsList(mindmapList, allMindmapsID);
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
                int color = Integer.parseInt(c.getString(4));
                int number = Integer.parseInt(c.getString(5));
                int parentNumber = Integer.parseInt(c.getString(6));
                int x = Integer.parseInt(c.getString(7));
                int y = Integer.parseInt(c.getString(8));

                Node node = new Node(text);
                node.setForm(form);
                node.setBorder(border);
                node.setColor(color);
                node.setNumber(number);
                node.setParentNodeNumber(parentNumber);
                node.setCenterX(x);
                node.setCenterY(y);

                nodesList.add(node);
            } while (c.moveToNext());
        } else Log.d("myLogs", "Error getAllNodes");

        return nodesList;
    }

    public String getMindmapById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(TABLE_MINDMAPS,
                null,
                MAP_ID + " = ?",
                selectionArgs,
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Log.d(LOG_TAG, "!!!!!" + cursor.getCount());
        String mindmapName = cursor.getString(1);
        return mindmapName;
    }

    public int updateMindmap(Mindmap mindmap){
        return 0;
    }
    public int updateNode(int MAP_ID, Node node){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MINDMAP_ID, MAP_ID);
        values.put(NODE_TEXT, node.getText());
        values.put(NODE_FORM, node.getForm());
        values.put(NODE_BORDER, node.getBorder());
        values.put(NODE_COLOR, node.getColor()); // Node color
        /*values.put(NODE_MARKER, node.getText()); // Node marker*/
        values.put(NODE_NUMBER, node.getNumber());
        values.put(NODE_PARENT_NUMBER, node.getParentNodeNumber());
        values.put(NODE_CENTER_X, node.getCenterX());
        values.put(NODE_CENTER_Y, node.getCenterY());

        return db.update(TABLE_NODES, values, MINDMAP_ID  + " = ? AND " + NODE_NUMBER + " = ?" ,
                new String[] {String.valueOf(MAP_ID), String.valueOf(node.getNumber())});
    }

    public void deleteNode(Node node){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NODES, NODE_NUMBER + " = ?", new String[] { String.valueOf(node.getNumber()) });
        db.close();
    }
    public void deleteMindmap(int ID){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Node> allNodes = getAllNodes(ID);
        for(int i = 0; i < allNodes.size(); i++){
            db.delete(TABLE_NODES, MINDMAP_ID + " = ?", new String[] { String.valueOf(ID) });
        }
        db.delete(TABLE_MINDMAPS, MAP_ID + " = ?", new String[] { String.valueOf(ID) });
        db.close();
    }
}
