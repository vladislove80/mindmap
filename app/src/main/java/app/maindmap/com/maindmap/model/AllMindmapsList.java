package app.maindmap.com.maindmap.model;

import java.util.ArrayList;

public class AllMindmapsList {

    private ArrayList<Mindmap> allMapList;

    public ArrayList<Mindmap> getAllMapList() {
        return allMapList;
    }

    public void setAllMapList(ArrayList<Mindmap> allMapList) {
        this.allMapList = allMapList;
    }
    public void addMindmap(Mindmap mindmap){
        allMapList.add(mindmap);
    }
}
