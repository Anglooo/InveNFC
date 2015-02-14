package com.hw.thomasfrow.invenfc;

/**
 * Created by thomas on 10/02/15.
 */
public class Item {
    private int id;
    private int ownerID;
    private String name;
    private String room;
    private String brand;
    private String model;
    private String comment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
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



    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {

        String output = "name " + name + " id " + id + " owner " +ownerID;

        return output;
    }
}
