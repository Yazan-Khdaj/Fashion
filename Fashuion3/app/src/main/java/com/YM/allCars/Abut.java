package com.YM.allCars;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class Abut extends Activity
{
    private WebView wv;
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_abut );

        wv = findViewById( R.id.abut_wv );

        wv.loadUrl( "file:///android_asset/abut.html" );
    }

}
