package com.example.mobilehw4;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

public class EditDialog extends AppCompatDialogFragment {
    //sets up the custom dialog box for editing
    private EditText editTextName, editTextCategory, editTextAmount, editTextDate, editTextNotes;
    private EditDialogListener listener2;

    private String cname, ccategory, cdate, cnotes, camount;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle("Edit Entry")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick (DialogInterface dialogInterface, int i) {
                        listener2.applyEditTexts(cname, ccategory, cdate, cnotes, Float.valueOf(camount));
                        //Applies pre-edited texts on cancel
                    }
                })
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick (DialogInterface dialogInterface, int i) {
                        if (isEmpty(editTextName) || isEmpty(editTextAmount) || isEmpty(editTextCategory) || isEmpty(editTextDate)) {
                            Toast.makeText(getActivity(), "All fields but notes must be filled in", Toast.LENGTH_SHORT).show();
                            listener2.applyEditTexts(cname, ccategory, cdate, cnotes, Float.valueOf(camount));
                            //Applies pre-edited texts on user error
                        }
                        else {
                            String name = editTextName.getText().toString();
                            String category = editTextCategory.getText().toString();
                            String date = editTextDate.getText().toString();
                            String notes = editTextNotes.getText().toString();
                            Float amount = Float.valueOf(editTextAmount.getText().toString());
                            listener2.applyEditTexts(name, category, date, notes, amount);
                            //Applies edited texts on save with no errors
                        }
                    }
                });
        editTextName = view.findViewById(R.id.editName);
        editTextCategory = view.findViewById(R.id.editCategory);
        editTextAmount = view.findViewById(R.id.editAmount);
        editTextDate = view.findViewById(R.id.editDate);
        editTextNotes = view.findViewById(R.id.editNotes);

        //Sets edit text values to the values found on the card
        Bundle args = getArguments();

        editTextName.setText(args.getString("namekey"));
        editTextCategory.setText(args.getString("categorykey"));
        editTextDate.setText(args.getString("datekey"));
        editTextNotes.setText(args.getString("noteskey"));
        Float eta = args.getFloat("amountkey");
        editTextAmount.setText(eta.toString());

        //Saves the loaded values to reload if the user cancels or fails to fill out all necessary boxes
        cname = editTextName.getText().toString();
        ccategory = editTextCategory.getText().toString();
        cdate = editTextDate.getText().toString();
        cnotes = editTextNotes.getText().toString();
        camount = editTextAmount.getText().toString();

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
            listener2 = (EditDialogListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface EditDialogListener {
        void applyEditTexts(String name, String category, String date, String notes, Float amount);
    }
}