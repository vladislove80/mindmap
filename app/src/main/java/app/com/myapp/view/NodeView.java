package app.com.myapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import app.com.myapp.model.Node;

class NodeView extends View {
    private static final int TEXT_SIZE = 20;

    private static final int[] sColor = { Color.BLACK, Color.BLUE, Color.CYAN, Color.DKGRAY,
            Color.GRAY, Color.GREEN, Color.LTGRAY, Color.MAGENTA, Color.RED, Color.TRANSPARENT,
            Color.WHITE, Color.YELLOW};
    Paint p;
    Node node;
    float x;
    float y;
    int side;
    int heigth;

    public NodeView(Context context, Node node) {
        super(context);
        p = new Paint();
        p.setTextSize(TEXT_SIZE);
        this.node = node;
        x = node.getCenterX();
        y = node.getCenterY();
        float width = p.measureText(node.getText());// get text width to draw rectangle
        heigth = TEXT_SIZE + 10;// необходимо предусмотреть условие вхождения в строку символа переноса каретки
        side = (int)width + 20;
        //setId(nodeCounter);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredHeight = measureHeight(heightMeasureSpec);
        int measuredWidth = measureWidth(widthMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    private int measureHeight(int measureSpec) {
        // Height
        return (int) (30);
    }

    private int measureWidth(int measureSpec) {
        // Width
        return (int) (side);
    }
    protected void onDraw(Canvas canvas) {
        p.setStyle(Paint.Style.FILL);
        canvas.drawARGB(80, 102, 204, 255);
        p.setColor(Color.RED);
        // draw square
        canvas.drawRect(0, 0, side, 30, p);
        p.setColor(Color.BLUE);
        // draw text relative to the center of square
        canvas.drawText(node.getText(), 10, 20, p);

    }

}
