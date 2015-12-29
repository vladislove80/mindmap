package app.maindmap.com.maindmap.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.maindmap.com.maindmap.R;

public class SecondFragment extends Fragment implements View.OnTouchListener {
    final String LOG_TAG = "myLogs";
    private TextView mindmapName;
    String mName;
    private RelativeLayout canvas;
    private ImageButton imgBtn;
    private View mainView;
    private View selected_item = null;
    private int offset_x = 0;
    private int offset_y = 0;
    Boolean touchFlag = false;
    int eX, eY;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //get mindmap name from from firstfragment list or from dialog
        Bundle extras = getArguments();
        if (extras != null) {
            mName = extras.getString("MindmapName");
        } else mName = "";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.secondfragment, null);
        mindmapName = (TextView) mainView.findViewById(R.id.mindmapName);
        //set mindmapname
        mindmapName.setText(mName);
        //imagebutton
        imgBtn = (ImageButton) mainView.findViewById(R.id.imageButton);
        imgBtn.setOnClickListener(m_onClickListener);

        canvas = (RelativeLayout) mainView.findViewById(R.id.canvas);
        canvas.setOnTouchListener(m_onTouchListener);
        return mainView;

    }

    View.OnTouchListener m_onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (touchFlag) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_MOVE:
                        eX = (int) event.getX();
                        eY = (int) event.getY();
                        int x = (int) event.getX() - offset_x;
                        int y = (int) event.getY() - offset_y;
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(new ViewGroup.MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                        lp.setMargins(x, y, 0, 0);
                        v.invalidate();
                        selected_item.setLayoutParams(lp);
                        break;
                    case MotionEvent.ACTION_UP:
                        touchFlag = false;
                        break;
                    default:
                        break;
                }
            }
            return true;
        }

    };
    View.OnClickListener m_onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view)
        {
            switch (view.getId())
            {
                case R.id.imageButton:
                    addView();
                    break;
                default:
                    break;
            }
        }
    };

    private void addView() {
        //RelativeLayout mainL = (RelativeLayout) mainView.findViewById(R.id.canvas);
        TextView tv = new TextView(getContext());
        tv.setText("Drug me !!!");
        tv.setOnTouchListener(this);
        canvas.addView(tv);
        MyView myViewNew = new MyView(getContext());
        myViewNew.setOnTouchListener(this);
        canvas.addView(myViewNew);


    }
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touchFlag = true;
                offset_x = (int) event.getX();
                offset_y = (int) event.getY();
                selected_item = v;
                break;
            case MotionEvent.ACTION_UP:
                selected_item = null;
                touchFlag = false;
                break;
            default:
                break;
        }
        return false;
    }

    class MyView extends View {
        Paint p;
        // координаты для рисования квадрата
        float x = 100;
        float y = 100;
        int side = 100;

        // переменные для перетаскивания
        boolean drag = false;
        float dragX = 0;
        float dragY = 0;

        public MyView(Context context) {
            super(context);
            p = new Paint();
            p.setColor(Color.RED);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int measuredHeight = measureHeight(heightMeasureSpec);
            int measuredWidth = measureWidth(widthMeasureSpec);
            setMeasuredDimension(measuredHeight, measuredWidth);
        }

        private int measureHeight(int measureSpec) {
            // Верните измеренную высоту виджета.
            return (int) (x + side);
        }

        private int measureWidth(int measureSpec) {
            // Верните измеренную ширину виджета.
            return (int) (y + side);
        }
        protected void onDraw(Canvas canvas) {
            // рисуем квадрат
            canvas.drawRect(x, y, x + side, y + side, p);

        }
    }
}
