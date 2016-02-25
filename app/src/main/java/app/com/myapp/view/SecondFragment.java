package app.com.myapp.view;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

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
    private final String LOG_TAG = "myLogs";
    private TextView mindmapName;
    private String mName;
    private FrameLayout myCanvas;
    private ImageButton imgBtn;
    private View mainView;
    private View selected_item = null;
    private int offset_x = 0;
    private int offset_y = 0;
    private Boolean touchFlag = false;
    private int idMindmap;
    private ArrayList<Node> allNodeForMindmap;
    private ArrayList<NodeView> listNodeView;
    private NodeView nodePaint;
    private DrawLine newLine;
    private ArrayList<DrawLine> listLines;
    private DBManager helper;
    private HashSet<Node> listNodesToDelete;
    private int tempCounter = 0;
    private PopupMenu popupMenu;

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
        myCanvas = (FrameLayout) mainView.findViewById(R.id.canvas);
        myCanvas.setOnTouchListener(m_onTouchListener);
        // DB
        helper = new DBManager(getContext());
        allNodeForMindmap = helper.getAllNodes(idMindmap);
        for (Node node : allNodeForMindmap) {Log.d(LOG_TAG, "allNodeForMindmap: " + node.getText() + ", ");}
        mName = helper.getMindmapById(idMindmap);
        listNodesToDelete = new HashSet<>();
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
        listNodeView = new ArrayList<>();
        listLines = new ArrayList<>();
        for(Node node : allNodeForMindmap) {
            if (node.getNumber() != 0) {
                newLine = drawLineToParentNode(getContext(), node.getNumber());
                newLine.setId(node.getNumber());
                myCanvas.addView(newLine);
                listLines.add(newLine);
            }
        }
        for(Node node : allNodeForMindmap) {
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
                        showPopupMenu(selected_item);
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
                        for (int nodeNumber : findIdAllChildNodes(selectedViewId)) {
                            myCanvas.removeView(listLines.get(nodeNumber-1));// nodeNumber ?????
                            listLines.set(nodeNumber-1, newLine = drawLineToParentNode(getContext(), nodeNumber));
                            myCanvas.addView(newLine);
                        }
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                new ViewGroup.MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                        lp.setMargins(x, y, 0, 0);
                        selected_item.setLayoutParams(lp);
                        selected_item.invalidate();
                        mindmapName.setText(selectedViewId + " ID Node, x=" + selected_item.getX() + ", y=" + selected_item.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        Handler handler = new Handler();
                        //
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                popupMenu.dismiss();
                            }}, 3000);
                        touchFlag = false;
                        myCanvas.removeAllViews();
                        paintAllNode(allNodeForMindmap);
                        break;
                    default:
                        break;
                }
            }
            return true;
        }

    };

    public boolean onTouch(View v, MotionEvent event) {
        Log.d(LOG_TAG, "method onTouch working");
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(LOG_TAG, "ACTION_DOWN !!!!!");
                touchFlag = true;
                offset_x = (int) event.getX();
                offset_y = (int) event.getY();
                selected_item = v;
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
        for(Node node : allNodeForMindmap) {
            helper.updateNode(idMindmap, node);
        }
        Log.d(LOG_TAG, "onDestroy !!");
        Log.d(LOG_TAG, "Count onStart method = " + tempCounter);
    }

    public Node findParentNode(Node nodeChild){
        for(Node node : allNodeForMindmap) {
            if(nodeChild.getParentNodeNumber() == node.getNumber()) {
                return node;
            }
        }
        return null;
    }
    public ArrayList<Integer> findIdAllChildNodes(int parentNodeNumber){
        ArrayList<Integer> nodesId = new ArrayList<>();
        for(Node node : allNodeForMindmap) {
            if(node.getParentNodeNumber() == parentNodeNumber) {
                nodesId.add(node.getNumber());
            }
        }
        return nodesId;
    }
    public ArrayList<Node> findAllChildNodes(Node parentNode){
        ArrayList<Node> childNodesList = new ArrayList<>();
        for(Node node : allNodeForMindmap) {
            if(node.getParentNodeNumber() == parentNode.getNumber()) {
                childNodesList.add(node);
            }
        }
        return childNodesList;
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

    @Override
    public void onStart() {
        tempCounter++;
        super.onStart();
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
    public void getListNodesToDelete(ArrayList<Node> allNodeForMindmap, int nodeNumberToDelete){
        if(nodeNumberToDelete == 0){
            Toast.makeText(getContext(), "Can't delete !!",
                    Toast.LENGTH_SHORT).show();
        } else {
            for(Node node : allNodeForMindmap){
                if(node.getNumber() == nodeNumberToDelete) {
                    ArrayList<Node> childNodesList = findAllChildNodes(node);
                    if(childNodesList.size() != 0) {
                        for (Node childNode : childNodesList){
                            listNodesToDelete.add(childNode);
                            getListNodesToDelete(allNodeForMindmap, childNode.getNumber());
                        }
                    }
                    listNodesToDelete.add(node);
                }
            }
        }
    }
    private void deleteNodes(ArrayList<Node> allNodeForMindmap, HashSet<Node> listNodesToDelete){
        Iterator<Node> itr = listNodesToDelete.iterator();
        ArrayList<NodeView> viewListToDelete = new ArrayList();
        while (itr.hasNext()) {
            Node nodeToDelete = itr.next();
            helper.deleteNode(nodeToDelete);
            allNodeForMindmap.remove(nodeToDelete);
            for(NodeView nodePaint : listNodeView) {
                if(nodePaint.getId() == nodeToDelete.getNumber())
                {viewListToDelete.add(nodePaint);}
            }
        }
        for(NodeView nv: viewListToDelete){
            listNodeView.remove(nv);
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
        popupMenu = new PopupMenu(getContext(), v);
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
                                getListNodesToDelete(allNodeForMindmap, selected_item.getId());
                                deleteNodes(allNodeForMindmap, listNodesToDelete);
                                myCanvas.removeAllViews();
                                paintAllNode(allNodeForMindmap);
                                return true;
                            default:
                                return false;
                        }
                    }

                });
        popupMenu.show();
    }

    public void openTextDialog() {
        DialogFragment fragment = new DialogText();
        fragment.setTargetFragment(this, REQUEST_CODE_UPDATE_TEXT);
        fragment.show(getFragmentManager(), fragment.getClass().getName());
        Bundle b = new Bundle();
        b.putString("NodeOldText", findNodeByNodeNumber(selected_item.getId()).getText());
        fragment.setArguments(b);
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
                    /*updateViewNode(selected_item.getId());*/
                    myCanvas.removeAllViews();
                    paintAllNode(allNodeForMindmap);
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
        }
    }
}
