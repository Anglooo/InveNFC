package com.hw.thomasfrow.invenfc;

import android.util.Log;

/**
 * Created by thomas on 10/02/15.
 */
public class Item {
    private int id;
    private String ownerID;
    private String name;
    private String room;
    private String brand;
    private String model;
    private String comment;
    private int tag;
    private int photo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getRoom(){
        return room;
    }

    public void setRoom(String room){
        this.room = room;
    }

    public String getBrand(){
        return brand;
    }

    public void setBrand(String brand){
        this.brand = brand;
    }

    public String getModel(){
        return model;
    }

    public void setModel(String model){
        this.model = model;
    }

    public String getComment(){
       return comment;
    }

    public void setComment(String comment){
       this.comment = comment;
    }

    public void setTag(int inputTag){
        if(inputTag == 1){
            Log.i("TAG","Setting tag to 1");
            this.tag = 1;
        }else{
            this.tag = 0;
        }
    }

    public boolean getTag(){
        Log.i("TAG","getTag");
        if(tag == 1){
            Log.i("TAG","returning 1");

            return true;
        }else{
            Log.i("TAG","returning 0");

            return false;
        }
    }

    @Override
    public String toString() {

        String output = "name " + name + " id " + id + " owner " +ownerID;

        return output;
    }
}
