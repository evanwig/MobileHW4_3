package com.example.mobilehw4;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

public class CreateDialog extends AppCompatDialogFragment {
    //sets up the custom dialog box
    private EditText editTextName, editTextCategory, editTextAmount, editTextDate, editTextNotes;
    private CreateDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle("New Entry")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick (DialogInterface dialogInterface, int i) {
                        //Exits the dialog box and does not save input
                    }
                })
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick (DialogInterface dialogInterface, int i) {
                        if (isEmpty(editTextName) || isEmpty(editTextAmount) || isEmpty(editTextCategory) || isEmpty(editTextDate)) {
                            Toast.makeText(getActivity(), "All fields but notes must be filled in", Toast.LENGTH_SHORT).show();
                            //Gives user error and exits dialog box without saving input on failure to fill out all necessary boxes
                        }
                        else {
                            String name = editTextName.getText().toString();
                            String category = editTextCategory.getText().toString();
                            String date = editTextDate.getText().toString();
                            String notes = editTextNotes.getText().toString();
                            Float amount = Float.valueOf(editTextAmount.getText().toString());
                            listener.applyTexts(name, category, date, notes, amount);
                            //Saves input as new card entry in MainActivity
                        }
                    }
                });
        editTextName = view.findViewById(R.id.editName);
        editTextCategory = view.findViewById(R.id.editCategory);
        editTextAmount = view.findViewById(R.id.editAmount);
        editTextDate = view.findViewById(R.id.editDate);
        editTextNotes = view.findViewById(R.id.editNotes);

        return builder.create();
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (CreateDialogListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() +
                    "must implement CreateDialogListener");
        }
    }

    public interface CreateDialogListener {
        void applyTexts(String name, String category, String date, String notes, Float amount);
    }
}
