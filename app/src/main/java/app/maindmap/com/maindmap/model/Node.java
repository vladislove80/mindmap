package app.maindmap.com.maindmap.model;


public class Node {

    private String text;
    private Form form;
    private Border botder;
    private Color color;
    private Marker marker;
    private int number;
    private int parentNodeNumber;

    public Node(){}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Border getBotder() {
        return botder;
    }

    public void setBotder(Border botder) {
        this.botder = botder;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getParentNodeNumber() {
        return parentNodeNumber;
    }

    public void setParentNodeNumber(int parentNodeNumber) {
        this.parentNodeNumber = parentNodeNumber;
    }
}
