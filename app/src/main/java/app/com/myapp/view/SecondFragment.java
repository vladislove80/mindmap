package app.com.myapp.view;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.com.myapp.R;

import app.com.myapp.data.DBManager;
import app.com.myapp.dialog.DialogBackgroundColor;
import app.com.myapp.dialog.DialogForm;
import app.com.myapp.dialog.DialogNewNode;
import app.com.myapp.dialog.DialogText;
import app.com.myapp.model.Node;

public class SecondFragment extends Fragment implements View.OnTouchListener {
    private static final int REQUEST_CODE_UPDATE_TEXT = 1;
    private static final int REQUEST_CODE_NEW_NODE = 2;
    private static final int REQUEST_CODE_COLOR = 3;
    private static final int REQUEST_CODE_FORM = 4;

    private final String[] listPopup = {"+ new", "Form", "Border"};
    final String LOG_TAG = "myLogs";
    private TextView mindmapName;
    String mName;
    private RelativeLayout myCanvas;
    private ImageButton imgBtn;
    private View mainView;
    private View selected_item = null;
    private int offset_x = 0;
    private int offset_y = 0;
    Boolean touchFlag = false;
    int idMindmap;
    ArrayList<Node> allNodeForMindmap;
    ArrayList<NodeView> listNodeView;
    NodeView nodePaint;
    DrawLine newLine;
    ArrayList<DrawLine> listLines = new ArrayList<>();
    DBManager helper;

    public SecondFragment(){
        super();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //get mindmap name from from firstfragment list or from dialog
        Bundle extras = getArguments();
        if (extras != null) {
            idMindmap = extras.getInt("idMindmap");
        } else idMindmap = -1;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.secondfragment, null);
        mindmapName = (TextView) mainView.findViewById(R.id.mindmapName);
        //set mindmapname
        mindmapName.setText(idMindmap + " mindmap ID");

        myCanvas = (RelativeLayout) mainView.findViewById(R.id.canvas);
        myCanvas.setOnTouchListener(m_onTouchListener);
        // DB
        helper = new DBManager(getContext());
        allNodeForMindmap = helper.getAllNodes(idMindmap);

        for (Node node : allNodeForMindmap) {Log.d(LOG_TAG, "allNodeForMindmap: " + node.getText() + ", ");}
        mName = helper.getMindmapById(idMindmap);
        listNodeView = new ArrayList<>();

        paintAllNode(allNodeForMindmap);

        return mainView;
    }
    public void paintViewNode(Node node){
        nodePaint = new NodeView(getContext(), node);
        nodePaint.setId(node.getNumber());
        nodePaint.setOnTouchListener(this);
        listNodeView.add(nodePaint);
        myCanvas.addView(nodePaint);
        //margins view
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                new ViewGroup.MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        lp.setMargins(node.getCenterX(), node.getCenterY(), 0, 0);
        nodePaint.setLayoutParams(lp);
        //nodePaint.invalidate();
    }

    public void paintNodeWithLine(Node node){
        paintViewNode(node);
        //draw the lines
        if (node.getNumber() != 0) {
            newLine = drawLineToParentNode(getContext(), node.getNumber());
            newLine.setId(nodePaint.getId());
            myCanvas.addView(newLine);
            listLines.add(newLine);
        }
    }

    public void paintAllNode(ArrayList<Node> allNodeForMindmap) {
        for(Node node : allNodeForMindmap) {
            if (node.getNumber() != 0) {
                newLine = drawLineToParentNode(getContext(), node.getNumber());
                newLine.setId(node.getNumber());
                myCanvas.addView(newLine);
                listLines.add(newLine);
            }
        }
        for(Node node : allNodeForMindmap) {
            //paintNodeWithLine(node);
            paintViewNode(node);
        }
    }

    View.OnTouchListener m_onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.d(LOG_TAG, "m_onTouchListener working");
            if (touchFlag) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        /*popupMenu = new PopupMenu(getContext(), selected_item);
                        //popupMenu.setOnMenuItemClickListener(getContext());
                        popupMenu.inflate(R.menu.popup_menu);
                        popupMenu.show();*/
                        showPopupMenu(selected_item);  //showPopupMenu(getContext(), selected_item);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int selectedViewId = selected_item.getId();
                        float evX = event.getX();
                        float evY = event.getY();
                        int x = (int) evX - offset_x;
                        int y = (int) evY - offset_y;

                        //save new x,y to Node in nodesList
                        updateXandYInAllNodesList(allNodeForMindmap, selectedViewId, x, y);
                        //move line from childNode to parentNode
                        if (selectedViewId != 0){
                            myCanvas.removeView(listLines.get(selectedViewId-1));
                            listLines.set(selectedViewId - 1, newLine = drawLineToParentNode(getContext(), selectedViewId));
                            myCanvas.addView(newLine);
                        }
                        //move line from parentNode to childNode
                        for (int nodeNumber : findIDAllChildNodes(selectedViewId)) {
                            myCanvas.removeView(listLines.get(nodeNumber-1));// nodeNumber ?????
                            listLines.set(nodeNumber-1, newLine = drawLineToParentNode(getContext(), nodeNumber));
                            myCanvas.addView(newLine);
                        }
                        //move nodeView
                        /*myCanvas.removeView(listNodeView.get(selectedViewId));
                        Node nodeForNewView = findNodeByNodeNumber(selectedViewId);
                        nodePaint = new NodeView(getContext(), nodeForNewView);
                        nodePaint.setId(nodeForNewView.getNumber());
                        nodePaint.setOnTouchListener(this);
                        listNodeView.set(selectedViewId, nodePaint);
                        myCanvas.addView(nodePaint);
                        //margins view
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                new ViewGroup.MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                        lp.setMargins(nodeForNewView.getCenterX(), nodeForNewView.getCenterY(), 0, 0);
                        nodePaint.setLayoutParams(lp);*/
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                new ViewGroup.MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                        lp.setMargins(x, y, 0, 0);
                        selected_item.setLayoutParams(lp);
                        selected_item.invalidate();
                        mindmapName.setText(selectedViewId + " ID Node, x=" + selected_item.getX() + ", y=" + selected_item.getY());
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

    private void addView() {
        /*TextView tv = new TextView(getContext());
        tv.setText("Drug me !!!");
        tv.setOnTouchListener(this);
        canvas.addView(tv);*/

        /*NodeView zeroNode = new NodeView(getContext(), mName);
        zeroNode.setOnTouchListener(this);
        canvas.addView(zeroNode);*/

    }
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(LOG_TAG, "method onTouch working");
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(LOG_TAG, "ACTION_DOWN !!!!!");
                touchFlag = true;
                offset_x = (int) event.getX();
                offset_y = (int) event.getY();
                selected_item = v;
                //showPopupMenu(selected_item);
                break;
            case MotionEvent.ACTION_UP:
                Log.d(LOG_TAG, "ACTION_UP !!!!!");
                selected_item = null;
                touchFlag = false;
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // save all  nodes changes
        //helper = new DBManager(getContext());
        for(Node node : allNodeForMindmap) {
            helper.updateNode(idMindmap, node);
        }

        Log.d(LOG_TAG, "onDestroy !!");
    }

    public Node findParentNode(Node nodeChild){
        for(Node node : allNodeForMindmap) {
            if(nodeChild.getParentNodeNumber() == node.getNumber()) {
                return node;
            }
        }
        return null;
    }
    public ArrayList<Integer> findIDAllChildNodes(int parentNodeNumber){
        ArrayList<Integer> nodesId = new ArrayList<>();
        for(Node node : allNodeForMindmap) {
            if(node.getParentNodeNumber() == parentNodeNumber) {
                nodesId.add(node.getNumber());
            }
        }
        return nodesId;
    }
    public Node findNodeByNodeNumber(int nodeNumber) {
        for(Node node : allNodeForMindmap) {
            if(nodeNumber == node.getNumber()) {
                return node;
            }
        }
        return null;
    }
    public DrawLine drawLineToParentNode(Context context, int nodeViewNumber) {
        Node node1 = findNodeByNodeNumber(nodeViewNumber);
        Node node2 = findParentNode(node1);
        Log.d(LOG_TAG, "x = " + node1.getCenterX() + ", " + "y = " + node1.getCenterY());
        DrawLine newLine = new DrawLine(context, node1, node2);
        return newLine;
    }

    public void updateXandYInAllNodesList(ArrayList<Node> allNodeForMindmap, int nodeNumber, int x, int y) {
        for (int i = 0; i < allNodeForMindmap.size(); i++) {
            if (allNodeForMindmap.get(i).getNumber() == nodeNumber) {
                allNodeForMindmap.get(i).setCenterX(x);
                allNodeForMindmap.get(i).setCenterY(y);
            }
        }
    }
    public void updateTextNode(ArrayList<Node> allNodeForMindmap, int nodeNumber, String newText) {
        for (int i = 0; i < allNodeForMindmap.size(); i++) {
            if (allNodeForMindmap.get(i).getNumber() == nodeNumber) {
                allNodeForMindmap.get(i).setText(newText);
            }
        }
    }
    public void updateNodeColor(ArrayList<Node> allNodeForMindmap, int nodeNumber, int newBackgroundColorFromPicker){
        for (int i = 0; i < allNodeForMindmap.size(); i++) {
            if (allNodeForMindmap.get(i).getNumber() == nodeNumber) {
                allNodeForMindmap.get(i).setColor(newBackgroundColorFromPicker);
            }
        }
    }
    public void updateNodeForm(ArrayList<Node> allNodeForMindmap, int nodeNumber, String newForm){
        for (int i = 0; i < allNodeForMindmap.size(); i++) {
            if (allNodeForMindmap.get(i).getNumber() == nodeNumber) {
                allNodeForMindmap.get(i).setForm(newForm);
            }
        }
    }

    public ArrayList<Node> addNewNodeInList(Node node){
        allNodeForMindmap.add(node);
        return allNodeForMindmap;
    }

    public void updateViewNode(int nodeNumber){
        int viewNumber = nodeNumber;
        for (int i = 0; i < listNodeView.size(); i++) {
            if (listNodeView.get(i).getId() == viewNumber) {
                myCanvas.removeView(listNodeView.get(viewNumber));
                Node nodeForNewView = findNodeByNodeNumber(nodeNumber);
                NodeView newViewNode = new NodeView(getContext(), nodeForNewView);
                newViewNode.setId(nodeForNewView.getNumber());
                newViewNode.setOnTouchListener(this);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        new ViewGroup.MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                lp.setMargins(nodeForNewView.getCenterX(), nodeForNewView.getCenterY(), 0, 0);
                newViewNode.setLayoutParams(lp);
                newViewNode.invalidate();
                listNodeView.set(viewNumber, newViewNode);
                myCanvas.addView(newViewNode);
            }
        }
    }
    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item1:
                                openNewNodeDialog();
                                //paintNewNodeView();
                                return true;
                            case R.id.item2:
                                openTextDialog();
                                return true;
                            case R.id.item3:
                                openFormDialog();
                                return true;
                            case R.id.item4:
                                openColorPickDialog();
                                return true;
                            case R.id.item5:
                                Toast.makeText(getContext(),
                                        "Вы выбрали PopupMenu 3",
                                        Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }

                });

        /*popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {

            @Override
            public void onDismiss(PopupMenu menu) {
                Toast.makeText(getContext(), "onDismiss",
                        Toast.LENGTH_SHORT).show();
            }
        });*/
        popupMenu.show();
    }

    public void openTextDialog() {
        DialogFragment fragment = new DialogText();
        fragment.setTargetFragment(this, REQUEST_CODE_UPDATE_TEXT);
        fragment.show(getFragmentManager(), fragment.getClass().getName());
    }
    public void openNewNodeDialog() {
        DialogFragment fragment = new DialogNewNode();
        fragment.setTargetFragment(this, REQUEST_CODE_NEW_NODE);
        fragment.show(getFragmentManager(), fragment.getClass().getName());
    }
    public void openColorPickDialog(){
        DialogFragment fragment = new DialogBackgroundColor();
        fragment.setTargetFragment(this, REQUEST_CODE_COLOR);
        fragment.show(getFragmentManager(), fragment.getClass().getName());
    }
    public void openFormDialog(){
        DialogFragment fragment = new DialogForm();
        fragment.setTargetFragment(this, REQUEST_CODE_FORM);
        fragment.show(getFragmentManager(), fragment.getClass().getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_UPDATE_TEXT:
                    String newNodeTextFromDialog = data.getStringExtra(DialogText.TAG_NEW_TEXT);
                    updateTextNode(allNodeForMindmap, selected_item.getId(), newNodeTextFromDialog);
                    updateViewNode(selected_item.getId());
                    break;
                case REQUEST_CODE_NEW_NODE:
                    String newNodeNameFromDialog = data.getStringExtra(DialogText.TAG_NEW_TEXT);
                    Node newNode = new Node(newNodeNameFromDialog, allNodeForMindmap.size(), selected_item.getId());
                    helper.addNode(idMindmap, newNode);
                    addNewNodeInList(newNode);
                    paintNodeWithLine(newNode);
                    break;
                case REQUEST_CODE_COLOR:
                    int newBackgroundColorFromPicker = data.getIntExtra(DialogBackgroundColor.TAG_COLOR, -1);
                    updateNodeColor(allNodeForMindmap, selected_item.getId(), newBackgroundColorFromPicker);
                    updateViewNode(selected_item.getId());
                    break;
                case REQUEST_CODE_FORM:
                    String newNodeFormFromDialog = data.getStringExtra(DialogForm.TAG_NEW_FORM);
                    updateNodeForm(allNodeForMindmap, selected_item.getId(), newNodeFormFromDialog);
                    updateViewNode(selected_item.getId());
                    break;
                //обработка других requestCode
            }
            //updateUI();
        }
    }
}
