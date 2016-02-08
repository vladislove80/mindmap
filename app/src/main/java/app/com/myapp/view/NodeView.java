package app.com.myapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;

import app.com.myapp.model.Node;

class NodeView extends View {
    private final String lineBreak = "\n";
    private static final int TEXT_SIZE = 20;

    private int lineHight, viewHight;
    private Paint p;
    private Node node;
    private float x;
    private float y;
    private int color;
    private int side;
    private ArrayList<String> nodeTextArray;
    private String maxLengthWord;


    public NodeView(Context context, Node node) {
        super(context);
        p = new Paint();
        p.setTextSize(TEXT_SIZE);
        this.node = node;
        x = node.getCenterX();
        y = node.getCenterY();
        nodeTextArray = findLineBreak(node.getText());
        this.color = node.getColor();
        lineHight = 10 + TEXT_SIZE;// необходимо предусмотреть условие вхождения в строку символа переноса каретки
        ArrayList<String> wordsInNodesText = findLineBreak(node.getText());
        if(!wordsInNodesText.isEmpty()){
            maxLengthWord = findMaxLengthString(p, wordsInNodesText);
            viewHight = lineHight*wordsInNodesText.size();
        } else {
            maxLengthWord = node.getText();
            viewHight = lineHight;
        }
        float width = p.measureText(maxLengthWord);// get text width to draw rectangle

        side = (int)width + 20;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredHeight = measureHeight(heightMeasureSpec);
        int measuredWidth = measureWidth(widthMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    private int measureHeight(int measureSpec) {
        // Height
        return (int) (viewHight);
    }

    private int measureWidth(int measureSpec) {
        // Width
        return (int) (side);
    }
    protected void onDraw(Canvas canvas) {
        p.setStyle(Paint.Style.FILL);
        //canvas.drawARGB(80, 102, 204, 255);
        p.setColor(color);
        // draw square
        int y_newLine = 0;
        if(nodeTextArray.isEmpty()) {
            canvas.drawRect(0, 0, side, lineHight, p);
            p.setColor(Color.BLUE);
            // draw text relative to the center of square
            canvas.drawText(node.getText(), 10, 20, p);
        } else {
            p.setColor(color);
            canvas.drawRect(0, y_newLine, side, viewHight, p);
            for(String lineText : nodeTextArray){
                p.setColor(Color.BLUE);
                // draw text relative to the center of square
                canvas.drawText(lineText, 10, lineHight - 10 + y_newLine, p);
                y_newLine += 30;
            }
        }

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
