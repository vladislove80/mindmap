package app.maindmap.com.maindmap.model;

import java.util.ArrayList;

public class AllMindmapsList {

    private ArrayList<Mindmap> allMapList;
    public AllMindmapsList(ArrayList<Mindmap> allMapList){
        this.allMapList = allMapList;
    }

    public ArrayList<Mindmap> getAllMapList() {
        return allMapList;
    }
}
