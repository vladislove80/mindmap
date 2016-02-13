package app.com.myapp.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import app.com.myapp.R;

public class DialogForm extends DialogFragment {
    private ListView newNodeForm;
    public static final String TAG_NEW_FORM = "NewNodeForm";
    private String[] forms = {"Rectangle", "Ellipse", "RoundRect"};
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.newnodeform, null);
        newNodeForm = (ListView) view.findViewById(R.id.listNodeForm);
        // устанавливаем режим выбора пунктов списка
        newNodeForm.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_single_choice, forms);

        // присваиваем адаптер списку
        newNodeForm.setAdapter(adapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(view)
                .setTitle("Select node form:")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //отправляем результат обратно
                        Intent intent = new Intent();
                        intent.putExtra(TAG_NEW_FORM, forms[newNodeForm.getCheckedItemPosition()]);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    }
                });
        return builder.create();
    }
}
