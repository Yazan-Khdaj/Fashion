package com.YM.allCars;

public class Ware
{
    private int id;
    private String image;
    private String model;
    private String color;
    private String date;
    private String desc;
    private double price;

    public Ware( String image, String model, String color, String date, String desc, double price )
    {
        this.image = image;
        this.model = model;
        this.color = color;
        this.date = date;
        this.desc = desc;
        this.price = price;
    }

    public Ware( int id, String image, String model, String color, String date, String desc, double price )
    {
        this.id = id;
        this.image = image;
        this.model = model;
        this.color = color;
        this.date = date;
        this.desc = desc;
        this.price = price;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public int getId( )
    {
        return id;
    }

    public void setImage( String image )
    {
        this.image = image;
    }

    public String getImage( )
    {
        return image;
    }

    public void setModel( String model )
    {
        this.model = model;
    }

    public String getModel( )
    {
        return model;
    }

    public void setColor( String color )
    {
        this.color = color;
    }

    public String getColor( )
    {
        return color;
    }

    public void setDate( String date )
    {
        this.date = date;
    }

    public String getDate( )
    {
        return date;
    }

    public void setDesc( String desc )
    {
        this.desc = desc;
    }

    public String getDesc( )
    {
        return desc;
    }

    public void setPrice( double price )
    {
        this.price = price;
    }

    public double getPrice( )
    {
        return price;
    }

}
