package com.example.mobilehw4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ExampleViewHolder> {
    //sets up the recycler view adapter
    ArrayList<CardItem> myCardList;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        TextView cardName, cardCategory, cardDate, cardNotes, cardAmount;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            cardName = itemView.findViewById(R.id.cardName);
            cardCategory = itemView.findViewById(R.id.cardCategory);
            cardDate = itemView.findViewById(R.id.cardDate);
            cardNotes = itemView.findViewById(R.id.cardNotes);
            cardAmount = itemView.findViewById(R.id.cardAmount);
        }
    }

    CardAdapter(ArrayList<CardItem> exampleList) {
        myCardList = exampleList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        CardItem currentItem = myCardList.get(position);

        holder.cardName.setText(currentItem.getName());
        holder.cardCategory.setText(currentItem.getCategory());
        holder.cardDate.setText(currentItem.getDate());
        holder.cardNotes.setText(currentItem.getNotes());
        holder.cardAmount.setText((currentItem.getAmount()).toString());
    }

    @Override
    public int getItemCount() {
        return myCardList.size();
    }
}
