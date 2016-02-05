package app.com.myapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;
import android.view.View;

import app.com.myapp.model.Node;

public class DrawLine extends View {
    final String LOG_TAG = "myLogs";
    private static final int TEXT_SIZE = 20;
    private static final int VIEW_HEIGHT = 30;
    Paint p;
    Path path;
    int x1, y1, x2, y2;
    float widthViewNode1, widthViewNode2, hightViewNode1, hightViewNode2;
    int viewHigth;
    /*Point point21;
    Point point22;*/

    public DrawLine(Context context, Node node1, Node node2) {
        super(context);
        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        path = new Path();
        p.setTextSize(TEXT_SIZE);
        viewHigth = TEXT_SIZE;// need to find a symbol of the transition to the next line
        widthViewNode1 = p.measureText(node1.getText())+20;// get text1 width, need to find a symbol of the transition to the next line
        hightViewNode2 = hightViewNode1 = viewHigth/2;
        widthViewNode2 = p.measureText(node2.getText())+20;// get text2 width
        this.x1 = node1.getCenterX() + (int)widthViewNode1/2;
        this.y1 = node1.getCenterY() + (int)hightViewNode1;
        this.x2 = node2.getCenterX() + (int)widthViewNode2/2;
        this.y2 = node2.getCenterY() + (int)hightViewNode2;;
        /*point21 = new Point((int)1.4*x1,(int)1.2*y1);
        point22 = new Point((int)1.2*x2,(int)0.5*y2);*/
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
}
