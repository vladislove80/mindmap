package app.maindmap.com.maindmap.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.maindmap.com.maindmap.R;
import app.maindmap.com.maindmap.dialog.DialogNewMindmap;

public class SecondFragment extends Fragment{

    private TextView mindmapName;
    String mName;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Bundle extras = getArguments();
        if (extras != null) {
            mName = extras.getString("MindmapName");
        } else mName = "";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.secondfragment, null);

        mindmapName = (TextView) v.findViewById(R.id.minmapName);
        mindmapName.setText(mName);
        return v;
    }

}
