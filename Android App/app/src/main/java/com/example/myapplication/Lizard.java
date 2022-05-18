package com.example.myapplication;

import java.util.Date;

public class Lizard{
    private int id;
    private String name;
    private Date dateOfBirth;
    private String sizeCentimeters;

    Lizard(int id, String newName, String sizeCentimeters, Date dateOfBirth){
        this.id = id;
        this.name = newName;
        this.dateOfBirth = dateOfBirth;
        this.sizeCentimeters = sizeCentimeters;
    }

    public int getId(){return this.id;}
    public String getName(){return this.name;}
    public Date getDateOfBirth(){return this.dateOfBirth;}
    public String getSizeCentimeters(){return this.sizeCentimeters;}

}
