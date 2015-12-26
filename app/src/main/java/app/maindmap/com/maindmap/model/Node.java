package app.maindmap.com.maindmap.model;


public class Node {

    private String text;
    private Form form;
    private Border border;
    private Color color;
    private Marker marker;
    private int number;
    private int parentNodeNumber;

    public Node(String text){
        this.text = text;
    }

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

    public Border getBorder() {
        return border;
    }

    public void setBorder(Border botder) {
        this.border = botder;
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
    public String toString(){
        return "text = " + text + ", form = " + form.form + ", border = " + border.border + ", node number = "
                + number + ", node parent number = " + parentNodeNumber;
    }
}
