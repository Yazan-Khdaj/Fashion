package com.YM.allCars;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import java.util.ArrayList;
import android.database.Cursor;
import java.util.Collections;

public class DataBaseAcesst
{
    private SQLiteDatabase dataBase;
    private SQLiteOpenHelper openHelper;

    private static DataBaseAcesst acesst;

    private DataBaseAcesst( Context context )
    {
        openHelper = new DataBase( context );
    }

    public static DataBaseAcesst getAcesst( Context context )
    {
        if ( acesst == null )
        {
            acesst = new DataBaseAcesst( context );
        }

        return acesst;
    }

    public void open( )
    {
        dataBase = openHelper.getWritableDatabase( );
    }

    public void close( )
    {
        if ( dataBase != null )
        {
            dataBase.close( );
        }
    }

    public boolean insertData( Ware ware )
    {
        ContentValues value = new ContentValues( );

        value.put( DataBase.TB_CLN_MODEL, ware.getModel( ) );
        value.put( DataBase.TB_CLN_COLOR, ware.getColor( ) );
        value.put( DataBase.TB_CLN_DESC, ware.getDesc( ) );
        value.put( DataBase.TB_CLN_PRICE, ware.getPrice( ) );
        value.put( DataBase.TB_CLN_IMAGE, ware.getImage( ) );
        value.put( DataBase.TB_CLN_DATE, ware.getDate( ) );

        long result = dataBase.insert( DataBase.TB_NAME, null, value );

        return result != -1;
    }

    public boolean deleteData( Ware ware )
    {
        int result = dataBase.delete( DataBase.TB_NAME, DataBase.TB_CLN_ID + "=?", new String[]{String.valueOf( ware.getId( ) )} );

        return result > 0;
    }

    public boolean updateData( Ware ware )
    {
        ContentValues value = new ContentValues( );

        value.put( DataBase.TB_CLN_MODEL, ware.getModel( ) );
        value.put( DataBase.TB_CLN_COLOR, ware.getColor( ) );
        value.put( DataBase.TB_CLN_DESC, ware.getDesc( ) );
        value.put( DataBase.TB_CLN_PRICE, ware.getPrice( ) );
        value.put( DataBase.TB_CLN_IMAGE, ware.getImage( ) );
        value.put( DataBase.TB_CLN_DATE, ware.getDate( ) );

        int result = dataBase.update( DataBase.TB_NAME, value, DataBase.TB_CLN_ID + "=?", new String[]{String.valueOf( ware.getId( ) )} );

        return result > 0;
    }

    public ArrayList<Ware> getAllWares( )
    {
        ArrayList<Ware> wares = new ArrayList<>( );

        Cursor cursor = dataBase.rawQuery( "SELECT * FROM " + DataBase.TB_NAME, null );

        if ( cursor != null && cursor.moveToFirst( ) )
        {
            do
            {
                int id = cursor.getInt( cursor.getColumnIndex( DataBase.TB_CLN_ID ) );
                String model = cursor.getString( cursor.getColumnIndex( DataBase.TB_CLN_MODEL ) );
                String color = cursor.getString( cursor.getColumnIndex( DataBase.TB_CLN_COLOR ) );
                String desc = cursor.getString( cursor.getColumnIndex( DataBase.TB_CLN_DESC ) );
                String image = cursor.getString( cursor.getColumnIndex( DataBase.TB_CLN_IMAGE ) );
                String date = cursor.getString( cursor.getColumnIndex( DataBase.TB_CLN_DATE ) );
                double price = cursor.getDouble( cursor.getColumnIndex( DataBase.TB_CLN_PRICE ) );

                Ware ware = new Ware( id, image, model, color, date, desc, price );

                wares.add( ware );

            }while(cursor.moveToNext( ));

            cursor.close( );
        }
        
        Collections.reverse(wares);

        return wares;
    }

    public ArrayList<Ware> getWares( String Model )
    {
        ArrayList<Ware> wares = new ArrayList<>( );

        Cursor cursor = dataBase.rawQuery( "SELECT * FROM " + DataBase.TB_NAME + " WHERE " + DataBase.TB_CLN_MODEL + " LIKE ?", new String[]{"%" + Model + "%"} );

        if ( cursor != null && cursor.moveToFirst( ) )
        {
            do
            {
                int id = cursor.getInt( cursor.getColumnIndex( DataBase.TB_CLN_ID ) );
                String model = cursor.getString( cursor.getColumnIndex( DataBase.TB_CLN_MODEL ) );
                String color = cursor.getString( cursor.getColumnIndex( DataBase.TB_CLN_COLOR ) );
                String desc = cursor.getString( cursor.getColumnIndex( DataBase.TB_CLN_DESC ) );
                String image = cursor.getString( cursor.getColumnIndex( DataBase.TB_CLN_IMAGE ) );
                String date = cursor.getString( cursor.getColumnIndex( DataBase.TB_CLN_DATE ) );
                double price = cursor.getDouble( cursor.getColumnIndex( DataBase.TB_CLN_PRICE ) );

                Ware ware = new Ware( id, image, model, color, date, desc, price );

                wares.add( ware );

            }while(cursor.moveToNext( ));

            cursor.close( );
        }

        Collections.reverse(wares);
        
        return wares;
    }

    public Ware getWare( int Id )
    {
        Cursor cursor = dataBase.rawQuery( "SELECT * FROM " + DataBase.TB_NAME + " WHERE " + DataBase.TB_CLN_ID + "=?", new String[]{String.valueOf( Id )} );

        if ( cursor != null && cursor.moveToFirst( ) )
        {
            int id = cursor.getInt( cursor.getColumnIndex( DataBase.TB_CLN_ID ) );
            String model = cursor.getString( cursor.getColumnIndex( DataBase.TB_CLN_MODEL ) );
            String color = cursor.getString( cursor.getColumnIndex( DataBase.TB_CLN_COLOR ) );
            String desc = cursor.getString( cursor.getColumnIndex( DataBase.TB_CLN_DESC ) );
            String image = cursor.getString( cursor.getColumnIndex( DataBase.TB_CLN_IMAGE ) );
            String date = cursor.getString( cursor.getColumnIndex( DataBase.TB_CLN_DATE ) );
            double price = cursor.getDouble( cursor.getColumnIndex( DataBase.TB_CLN_PRICE ) );

            Ware ware = new Ware( id, image, model, color, date, desc, price );

            cursor.close( );

            return ware;
        }

        return null;
    }
}
