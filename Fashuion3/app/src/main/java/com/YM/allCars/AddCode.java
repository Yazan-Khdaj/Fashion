package com.YM.allCars;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.ImageButton;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.Intent;
import android.widget.Toast;
import android.content.Context;
import android.net.Uri;

public class AddCode extends AppCompatActivity
{
    private TextView tv_send;
    private EditText et_code;
    private ImageButton btn_check;

    private Date date;
    private SimpleDateFormat sdfDay, sdfMuomth, sdfHuor;

    private static SharedPreferences save;
    private static SharedPreferences.Editor edit;

    private static final String DAY = "days";
    private static final int dayFree = 15;
    private final int dayPackage = 30;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_code );

        et_code = findViewById( R.id.add_code_et_code );
        btn_check = findViewById( R.id.add_code_btn_check );
        tv_send = findViewById( R.id.add_code_tv_send_email );

        tv_send.setOnClickListener( new View.OnClickListener( )
            {
                @Override 
                public void onClick( View view )
                {
                    try
                    {
                        Intent sendEmail = new Intent( Intent.ACTION_SENDTO );
                        
                        sendEmail.setData( Uri.parse( "mailto:Fashuion3@gmail.com" ) );
                        sendEmail.putExtra( Intent.EXTRA_SUBJECT, getResources( ).getString( R.string.add_code_email_subject ) );

                        startActivity( sendEmail );
                    }
                    catch (Exception e)
                    {
                        Toast.makeText( AddCode.this, getResources( ).getString( R.string.add_code_error_toast ), Toast.LENGTH_SHORT ).show( );
                    }
                }
            } );

        btn_check.setOnClickListener( new View.OnClickListener( )
            {
                @Override
                public void onClick( View p1 )
                {
                    String code = et_code.getText( ).toString( );

                    if ( !code.isEmpty( ) && code.length( ) == 6 )
                    {
                        char[] arr = code.toCharArray( );

                        String strDay = arr[ 0 ] + "" + arr[ 1 ] + "";
                        String strMuonth = arr[ 4 ] + "" + arr[ 5 ] + "";
                        String strHours = arr[ 2 ] + "" + arr[ 3 ] + "";

                        date = new Date( );

                        sdfDay = new SimpleDateFormat( "dd", Locale.ENGLISH );
                        sdfMuomth = new SimpleDateFormat( "MM", Locale.ENGLISH );
                        sdfHuor = new SimpleDateFormat( "hh", Locale.ENGLISH );

                        int day = Integer.parseInt( sdfDay.format( date ) );
                        int muonth = Integer.parseInt( sdfMuomth.format( date ) );
                        int huor = Integer.parseInt( sdfHuor.format( date ) );
                        int codeDay = Integer.parseInt( strDay );
                        int codeMuonth = Integer.parseInt( strMuonth );
                        int codeHour = Integer.parseInt( strHours );

                        if ( day == codeDay && muonth == codeMuonth && huor == codeHour )
                        {
                            Toast.makeText( getBaseContext( ), getResources( ).getString( R.string.add_code_reRegister_toast ), Toast.LENGTH_LONG ).show( );

                            save = PreferenceManager.getDefaultSharedPreferences( getBaseContext( ) );
                            edit = save.edit( );

                            edit.putInt( DAY, dayPackage );
                            edit.apply( );

                            Tutorials.welcome = true;

                            startActivity( new Intent( getBaseContext( ), MainActivity.class ) );
                            finish( );
                        }

                    }
                }     
            } );

    }

    public static int getDay( Context context )
    {
        save = PreferenceManager.getDefaultSharedPreferences( context );
        edit = save.edit( );

        int day = save.getInt( DAY, dayFree );

        return day;
    }


}
