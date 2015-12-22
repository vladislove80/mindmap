package app.maindmap.com.maindmap.model;

import java.util.ArrayList;
import java.util.Date;

public class Mindmap {

    private String name;
    private Date date;
    private ArrayList<Node> mindmap;

    public Mindmap(String name, Date date, ArrayList<Node> mindmap) {
        this.name = name;
        this.date = date;
        this.mindmap = mindmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<Node> getMindmap() {
        return mindmap;
    }

    public void setMindmap(ArrayList<Node> mindmap) {
        this.mindmap = mindmap;
    }
}
