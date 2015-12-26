package app.maindmap.com.maindmap.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.maindmap.com.maindmap.R;

public class SecondFragment extends Fragment{

    private TextView mindmapName;
    String mName;
    private RelativeLayout canvas;
    private ImageButton imgBtn;

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
        View v = inflater.inflate(R.layout.secondfragment, null);
        mindmapName = (TextView) v.findViewById(R.id.mindmapName);
        //set mindmapname
        mindmapName.setText(mName);
        //area to paint
        canvas = (RelativeLayout)v.findViewById(R.id.canvas);
        canvas.addView(new MyView(getContext()));
        //imagebutton
        imgBtn = (ImageButton) v.findViewById(R.id.imageButton);
        imgBtn.setOnClickListener(m_onClickListener);
        return v;
    }

    class MyView extends View {
        Paint p;
        // координаты для рисования квадрата
        float x = 200;
        float y = 300;
        int side = 100;
        // переменные для перетаскивания
        boolean drag = false;
        float dragX = 0;
        float dragY = 0;

        public MyView(Context context) {
            super(context);
            p = new Paint();
            p.setColor(Color.GREEN);
        }
        protected void onDraw(Canvas canvas) {
            // рисуем квадрат
            canvas.drawRect(x, y, x + side, y + side, p);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            // координаты Touch-события
            float evX = event.getX();
            float evY = event.getY();

            switch (event.getAction()) {
                // касание началось
                case MotionEvent.ACTION_DOWN:
                    // если касание было начато в пределах квадрата
                    if (evX >= x && evX <= x + side && evY >= y && evY <= y + side) {
                        // включаем режим перетаскивания
                        drag = true;
                        // разница между левым верхним углом квадрата и точкой касания
                        dragX = evX - x;
                        dragY = evY - y;
                    }
                    break;
                // тащим
                case MotionEvent.ACTION_MOVE:
                    // если режим перетаскивания включен
                    if (drag) {
                        // определеяем новые координаты для рисования
                        x = evX - dragX;
                        y = evY - dragY;
                        // перерисовываем экран
                        invalidate();
                    }
                    break;
                // касание завершено
                case MotionEvent.ACTION_UP:
                    // выключаем режим перетаскивания
                    drag = false;
                    break;
            }
            return true;
        }
    }
    View.OnClickListener m_onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view)
        {
            switch (view.getId())
            {
                case R.id.imageButton:
                    canvas.addView(new MyView(getContext()));
                    break;
                default:
                    break;
            }
        }
    };
}
