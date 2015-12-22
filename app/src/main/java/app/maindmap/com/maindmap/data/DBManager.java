package app.maindmap.com.maindmap.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import app.maindmap.com.maindmap.model.Mindmap;
import app.maindmap.com.maindmap.model.Node;

public class DBManager extends SQLiteOpenHelper {
    final String LOG_TAG = "myLogs";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "mapBD";
    // Tables
    private static final String TABLE_MINDMAPS = "minmaps";
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
                + MAP_DATE + " DATE TIMESTAMP"
                + ")";
        db.execSQL(CREATE_MAPS_TABLE);
        String CREATE_NODES_TABLE =  "CREATE TABLE "
                + TABLE_NODES + "("
                + MINDMAP_ID + "INTEGER"
                + NODE_TEXT + "TEXT"
                + NODE_FORM + "TEXT"
                + NODE_BORDER + "TEXT"
                + NODE_COLOR + "TEXT"
                + NODE_MARKER + "TEXT"
                + NODE_NUMBER + "INTEGER UNIQUE KEY"
                + NODE_PARENT_NUMBER + "INTEGER"
                + ")";
        db.execSQL(CREATE_NODES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLES IF EXISTS " + TABLE_MINDMAPS + ", " + TABLE_NODES);

        // Create tables again
        onCreate(db);
    }
    public void addMindmap(Mindmap mindmap){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MAP_NAME, mindmap.getName()); // Mindmap Name
        values.put(MAP_DATE, mindmap.getDate().toString()); // Mindmap date criation
        // Inserting Row
        db.insert(TABLE_MINDMAPS, null, values);
        db.close(); // Closing database connection
    }
    public void addNode(Node node) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NODE_TEXT, node.getText()); // Node text - Mindemap name !!!
        /*values.put(NODE_FORM, node.getForm().); // Node form
        values.put(NODE_BORDER, node.getText()); // Node border
        values.put(NODE_COLOR, node.getText()); // Node color
        values.put(NODE_MARKER, node.getText()); // Node marker
        values.put(NODE_NUMBER, node.getText()); // Node number
        values.put(NODE_TEXT, node.getText()); // Node parent number
        db.insert(TABLE_NODES, null, values);
        db.close(); // Closing database connection*/

    }
    /*public Mindmap getMindmapById(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MINDMAPS, new String[] { MAP_ID,
                        MAP_NAME, MAP_DATE}, MAP_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Mindmap mindmap = new Mindmap(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return mindmap
        return mindmap;
    }*/
    public List<Mindmap> getAllMindmaps(){
        return null;
    }
    public int updateMindmap(Mindmap mindmap){
        return 0;
    }
    public void deleteMindmap(Mindmap mindmap){

    }
}
