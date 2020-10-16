package com.example.mobilehw4;

public class CardItem {
    //Sets up the card
    String cardName, cardCategory, cardDate, cardNotes;
    Float cardAmount;

    public CardItem (String name, String category, String date, String notes, Float amount) {
        cardName = name;
        cardCategory = category;
        cardDate = date;
        cardNotes = notes;
        cardAmount = amount;
    }

    //Getter's
    public String getName() {
        return cardName;
    }
    public String getCategory() {
        return cardCategory;
    }
    public String getDate() {
        return cardDate;
    }
    public String getNotes() {
        return cardNotes;
    }
    public Float getAmount() {
        return cardAmount;
    }
}
