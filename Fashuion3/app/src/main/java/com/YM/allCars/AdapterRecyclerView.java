package com.YM.allCars;

import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import android.widget.Toast;

public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.WareHolder>
{
    private ArrayList<Ware> wares;
    private RecyclerOnClickLisaner onClickLisaner;

    public AdapterRecyclerView( ArrayList<Ware> wares , RecyclerOnClickLisaner onClickLisaner )
    {
        this.wares = wares;
        this.onClickLisaner = onClickLisaner;
    }

    public void setWares( ArrayList<Ware> wares )
    {
        this.wares = wares;
    }

    public ArrayList<Ware> getWares( )
    {
        return wares;
    }

    @Override
    public AdapterRecyclerView.WareHolder onCreateViewHolder( ViewGroup p1, int p2 )
    {
        View view = LayoutInflater.from( p1.getContext( ) ).inflate( R.layout.custom_ware, null, false );

        return new AdapterRecyclerView.WareHolder( view );
    }

    @Override
    public void onBindViewHolder( AdapterRecyclerView.WareHolder p1, int p2 )
    {
        Ware ware = wares.get( p2 );

        p1.tv_model.setText( ware.getModel( ) );
        p1.tv_color.setText( ware.getColor( ) );
        p1.tv_date.setText( ware.getDate( ) );
        p1.tv_price.setText( ware.getPrice( ) + "" );

        if ( ware.getImage( ) != null && !ware.getImage( ).isEmpty( ) )
        {
            p1.image.setImageURI( Uri.parse( ware.getImage( ) ) );
        }
        else
        {
            p1.image.setImageResource( R.drawable.image_1 ); 
        }

        p1.image.setTag( ware.getId( ) );     

        try
        {
            p1.tv_color.setTextColor( Color.parseColor( ware.getColor( ) ) );
        }
        catch (Exception e)
        {}
    }

    @Override
    public int getItemCount( )
    {
        return wares.size( );
    }


    public class WareHolder extends RecyclerView.ViewHolder
    {
        TextView tv_model,tv_color,tv_price,tv_date;
        ImageView image;

        public WareHolder( View view )
        {
            super( view );

            tv_model = view.findViewById( R.id.custom_ware_tv_model );
            tv_color = view.findViewById( R.id.custom_ware_tv_color );
            tv_price = view.findViewById( R.id.custom_ware_tv_prise );
            tv_date = view.findViewById( R.id.custom_ware_tv_date );
            image = view.findViewById( R.id.custom_ware_image );

            view.setOnClickListener( new View.OnClickListener( )
                {
                    @Override
                    public void onClick( View p1 )
                    {
                        int id = image.getTag( );
                        onClickLisaner.clickItem( id );
                    }
                } );
        }
    }
}
