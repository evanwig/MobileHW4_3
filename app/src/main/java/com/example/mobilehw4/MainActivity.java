package com.example.mobilehw4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements CreateDialog.CreateDialogListener, EditDialog.EditDialogListener {
    //Values used for card creation
    private String entryName, entryCategory, entryDate, entryNotes;
    private Float entryAmount;

    private FloatingActionButton button;

    int position;

    //Used for getter's on edits
    private CardItem cItem;

    //Variables needed for recycler view layout
    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private ArrayList<CardItem> myCardList;

    //Sets up MainActivity as the main interface
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Checks if there is saved data, if not, loads in empty arraylist
        loadData();

        //sets up recyclerview
        myRecyclerView = findViewById(R.id.recyclerView);
        myRecyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(this);
        myAdapter = new CardAdapter(myCardList);

        myRecyclerView.setLayoutManager(myLayoutManager);
        myRecyclerView.setAdapter(myAdapter);

        //Sets up ItemTouchHelper for swipe functionality
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(myRecyclerView);

        //Set up FloatingActionButton
        button = findViewById(R.id.floatingActionButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateDialog();
            }
        });
    }

    //Saves data to SharedPreferences using gson functionality implemented in gradle dependencies
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(myCardList);
        editor.putString("task list", json);
        editor.apply();
    }

    //Loads data from SharedPreferences using gson functionality implemented in gradle dependencies
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<CardItem>>() {
        }.getType();
        myCardList = gson.fromJson(json, type);
        if (myCardList == null) {
            myCardList = new ArrayList<>();
        }
    }

    //Inserts item at last spot on list
    public void insertItem() {
        position = myCardList.size();
        myCardList.add(position, new CardItem(entryName, entryCategory, entryDate, entryNotes, (float) entryAmount));
        myAdapter.notifyItemInserted(position);
        saveData();
    }

    //Removes item from swiped position
    public void removeItem(@NonNull RecyclerView.ViewHolder viewHolder) {
        position = viewHolder.getAdapterPosition();
        myCardList.remove(position);
        myAdapter.notifyItemRemoved(position);
        saveData();
    }

    //Removes pre-edited item and replaces with post-edited item in the same position
    public void editItem() {
        myCardList.remove(position);
        myAdapter.notifyItemRemoved(position);
        myCardList.add(position, new CardItem(entryName, entryCategory, entryDate, entryNotes, (float) entryAmount));
        myAdapter.notifyItemInserted(position);
        saveData();
    }

    //Pulls up custom dialog box for entry creation functionality
    public void openCreateDialog() {
        CreateDialog createDialog = new CreateDialog();
        createDialog.setCancelable(false);
        createDialog.show(getSupportFragmentManager(), "example dialog");
    }

    //Pulls up custom dialog box for entry editing functionality, passes current item variables to dialog box
    public void openEditDialog(RecyclerView.ViewHolder viewHolder) {
        position = viewHolder.getAdapterPosition();

        cItem = myCardList.get(viewHolder.getAdapterPosition());
        entryName = cItem.getName();
        entryCategory = cItem.getCategory();
        entryDate = cItem.getDate();
        entryNotes = cItem.getNotes();
        entryAmount = cItem.getAmount();

        //stores card values in bundle to send to editdialog
        Bundle args = new Bundle();
        args.putString("namekey", entryName);
        args.putString("categorykey", entryCategory);
        args.putString("datekey", entryDate);
        args.putString("noteskey", entryNotes);
        args.putFloat("amountkey", entryAmount);

        EditDialog editDialog = new EditDialog();
        editDialog.setArguments(args);
        editDialog.setCancelable(false);

        editDialog.show(getSupportFragmentManager(), "edit dialog");
    }

    //Listener application from CreateDialog
    @Override
    public void applyTexts(String name, String category, String date, String notes, Float amount) {
        entryName = name;
        entryCategory = category;
        entryDate = date;
        entryNotes = notes;
        entryAmount = amount;
        insertItem();
    }

    //Listener application from EditDialog
    public void applyEditTexts(String name, String category, String date, String notes, Float amount) {
        entryName = name;
        entryCategory = category;
        entryDate = date;
        entryNotes = notes;
        entryAmount = amount;
        editItem();
    }

    //Swipe Functionality
    //Swipe right to delete
    //Swipe left to edit
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if (direction == ItemTouchHelper.RIGHT) {
                removeItem(viewHolder);
            } else if (direction == ItemTouchHelper.LEFT) {
                openEditDialog(viewHolder);
            }
        }
    };
}