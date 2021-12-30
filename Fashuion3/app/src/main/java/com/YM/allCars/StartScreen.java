package com.YM.allCars;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
import android.content.Intent;

public class StartScreen extends AppCompatActivity
{
    private final int TIME = 3000;
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_start_screen );

        Handler handler = new Handler( );

        handler.postDelayed( new Runnable( )
            {
                @Override
                public void run( )
                {
                    startActivity( new Intent( StartScreen.this, Login.class ) );
                    finish( );
                }       
            }, TIME );
    }

}
