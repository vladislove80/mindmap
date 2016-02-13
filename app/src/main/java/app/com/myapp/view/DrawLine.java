package app.com.myapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import app.com.myapp.model.Node;

public class  DrawLine extends View {
    final String LOG_TAG = "myLogs";
    private static final int TEXT_SIZE = 20;
    private static final int VIEW_HEIGHT = 30;
    Paint p;
    Path path;
    int x1, y1, x2, y2;
    float widthViewNode1, widthViewNode2, hightViewNode1, hightViewNode2;
    int viewHight;
    /*Point point21;
    Point point22;*/

    public DrawLine(Context context, Node node1, Node node2) {
        super(context);
        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        path = new Path();
        p.setTextSize(TEXT_SIZE);
        viewHight = TEXT_SIZE;// need to find a symbol of the transition to the next line
        ArrayList<String> wordsInNode1Text = findLineBreak(node1.getText());
        ArrayList<String> wordsInNode2Text = findLineBreak(node2.getText());
        if(!wordsInNode1Text.isEmpty()){
            widthViewNode1 = (p.measureText(findMaxLengthString(p, wordsInNode1Text))+20)/2;
            hightViewNode1 = viewHight*wordsInNode1Text.size()/2;
        } else {
            widthViewNode1 = (p.measureText(node1.getText())+20)/2;// get text1 width, need to find a symbol of the transition to the next line
            hightViewNode1 = viewHight/2;
        }
        if(!wordsInNode2Text.isEmpty()){
            widthViewNode2 = (p.measureText(findMaxLengthString(p, wordsInNode2Text))+20)/2;
            hightViewNode2 = viewHight*wordsInNode2Text.size()/2;
        } else {hightViewNode2 = viewHight/2;
            widthViewNode2 = (p.measureText(node2.getText())+20)/2;
            hightViewNode2 = viewHight/2;
        }

        this.x1 = node1.getCenterX() + (int)widthViewNode1;
        this.y1 = node1.getCenterY() + (int)hightViewNode1;
        this.x2 = node2.getCenterX() + (int)widthViewNode2;
        this.y2 = node2.getCenterY() + (int)hightViewNode2;
    }

    protected void onDraw(Canvas canvas){
        Log.d(LOG_TAG, "onDraw starts!!");
        p.setStrokeWidth(2);
        path.reset();
        Log.d(LOG_TAG, "on Drow: x = " +x1 + ", " + "y = " + y1);
        canvas.drawLine(x1, y1, x2, y2, p);
        /*path.moveTo(x1, y1);
        path.cubicTo(point21.x, point21.y, point22.x, point22.y, x2, y2);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, p);*/
    }
    private static ArrayList<String> findLineBreak(String nodeText){
        ArrayList<String> nodeTextList = new ArrayList<>();
        int beginIndex = 0;
        int endIndex;
        int index = 0;
        index = nodeText.indexOf("\n");
        while (index != -1) {
            if(index != -1) {
                endIndex = index;
                nodeTextList.add(nodeText.substring(beginIndex, endIndex));
                index += 1;
                beginIndex = index;
            }
            index = nodeText.indexOf("\n", index);
        }
        if(!nodeTextList.isEmpty()){
            nodeTextList.add(nodeText.substring(beginIndex, nodeText.length()));
        }
        return nodeTextList;
    }
    private String findMaxLengthString(Paint p, ArrayList<String> nodeTextList){
        String maxLengthString = "";
        float maxWidth = p.measureText(maxLengthString);
        for(String tempString : nodeTextList){
            if(maxWidth < p.measureText(tempString)){
                maxLengthString = tempString;
                maxWidth = p.measureText(tempString);
            }
        }
        return maxLengthString;
    }
}
