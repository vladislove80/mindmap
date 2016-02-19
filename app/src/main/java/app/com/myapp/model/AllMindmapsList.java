package app.com.myapp.model;

import java.util.ArrayList;
import java.util.HashMap;

public class AllMindmapsList {
    private ArrayList<Mindmap> allMapList;
    private ArrayList<Integer> allMindmapsID;
    public AllMindmapsList(ArrayList<Mindmap> allMapList, ArrayList<Integer> allMindmapsID){
        this.allMapList = allMapList;
        this.allMindmapsID = allMindmapsID;
    }

    public ArrayList<Mindmap> getAllMapList() {
        return allMapList;
    }
    public ArrayList<Integer> getAllMindmapsID(){
        return allMindmapsID;
    }
}
