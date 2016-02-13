package app.com.myapp.model;

public class Node {
    private static final String form1 = "Rectangle";
    private static final String form2 = "Ellipse";
    private static final String form3 = "RoundRect";

    private String text;
    private String form;
    private String border;
    private int color;
    private Marker marker;
    private int number;
    private int parentNodeNumber;
    private int centerX;
    private int centerY;

    public Node(String text){
        this.text = text;
        this.form = form1;
        this.color = 0xff59bd87;
        this.number = 0;
        this.parentNodeNumber = -1;
        this.centerX = 50;
        this.centerY = 50;
    }
    public Node(String text, int number, int parentNodeNumber){
        this.text = text;
        this.form = form1;
        this.color = 0xff59bd87;
        this.number = number;
        this.parentNodeNumber = parentNodeNumber;
        this.centerX = 50;
        this.centerY = 50;
    }
    public Node(String text, String form, String border, int number, int parentNodeNumber, int centerX, int centerY){
        this.text = text;
        this.form = form;
        this.border = border;
        this.color = 0xff59bd87;
        this.number = number;
        this.parentNodeNumber = parentNodeNumber;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
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
        return "text = " + text + ", form = " + form + ", border = " + border + ", node number = "
                + number + ", node parent number = " + parentNodeNumber;
    }
}
