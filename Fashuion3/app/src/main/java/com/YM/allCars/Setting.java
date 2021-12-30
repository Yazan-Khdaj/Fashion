package com.YM.allCars;

import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.Locale;
import android.os.Handler;

public class Setting extends AppCompatActivity
{
    public static boolean showIsList;
    public static boolean showCopy;
    public static boolean theme;
    public static boolean autoTheme;
    public static String copy;
    public static String lang;
    private Intent intent;
    private Bundle b;

    public static final String SHOW = "show";
    public static final String LIST = "list";
    public static final String TEXT = "text";
    public static final String DATA = "data";
    public static final String DELETE = "delete";
    public static final String LANG = "lang";
    public static final String THEME = "theme";
    public static final String AUTO_THEME = "auto_theme";

    private EditText et;
    private CheckBox cb_copy, cb_list,cb_autoTheme;
    private ImageButton btn_reStar;
    private Spinner sp_lan;
    private Switch sw_theme;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );

        if ( MainActivity.theme )
        {
            setTheme( R.style.Night_AppTheme );
        }
        else
        {
            setTheme( R.style.AppTheme );
        }

        setContentView( R.layout.activity_setting );

        Toolbar toolbar = findViewById( R.id.toolbar );
        sp_lan = findViewById( R.id.setting_sp );
        et = findViewById( R.id.setting_et_allow );
        cb_copy = findViewById( R.id.setting_cb_show_copy );
        cb_list = findViewById( R.id.setting_cb_show_list );
        btn_reStar = findViewById( R.id.setting_btn_restar );
        sw_theme = findViewById( R.id.setting_sw_theme );
        cb_autoTheme = findViewById( R.id.setting_cb_auto_theme );

        setSupportActionBar( toolbar );
        getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        getSupportActionBar( ).setHomeButtonEnabled( true );
        toolbar.setNavigationOnClickListener( new View.OnClickListener( ) 
            {
                @Override 
                public void onClick( View _v )
                {
                    onBackPressed( );
                }
            } );

        intent = getIntent( );

        b = intent.getBundleExtra( MainActivity.SETTING );

        showCopy = b.getBoolean( MainActivity.COPY, false );
        showIsList = b.getBoolean( MainActivity.SHOW , true );
        theme = b.getBoolean( MainActivity.THEME, false );
        autoTheme = b.getBoolean( MainActivity.AUTO_THEME, true );
        copy = b.getString( MainActivity.TEXT, "" );
        lang = b.getString( MainActivity.LANG, "" );

        cb_copy.setChecked( showCopy );
        cb_list.setChecked( showIsList );
        sw_theme.setChecked( theme );
        cb_autoTheme.setChecked( autoTheme );

        et.setEnabled( showCopy );
        et.setText( copy );
        sw_theme.setEnabled( !autoTheme );

        cb_autoTheme.setOnCheckedChangeListener( new CheckBox.OnCheckedChangeListener( )
            {
                @Override
                public void onCheckedChanged( CompoundButton p1, boolean p2 )
                {
                    autoTheme = p2;
                    sw_theme.setEnabled( !p2 );
                }
            } );

        sw_theme.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener( )
            {
                @Override
                public void onCheckedChanged( CompoundButton p1, boolean p2 )
                {
                    theme = p2;
                }        
            } );

        sp_lan.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener( )
            {
                @Override
                public void onItemSelected( AdapterView<?> p1, View p2, int p3, long p4 )
                {
                    switch ( p3 )
                    {
                        case 1:
                            lang = "ar";
                            setLocale( lang );                 
                            break;
                        case 2:
                            lang = "en";
                            setLocale( lang );
                            break;
                    }
                }

                @Override
                public void onNothingSelected( AdapterView<?> p1 )
                {}                   
            } ) ;

        cb_copy.setOnCheckedChangeListener( new CheckBox.OnCheckedChangeListener( )
            {
                @Override
                public void onCheckedChanged( CompoundButton p1, boolean p2 )
                {
                    if ( p2 )
                    {
                        Tutorials.useCopy( Setting.this );
                    }

                    showCopy = p2;
                    et.setEnabled( p2 );

                    if ( !p2 )
                    {
                        et.setText( "" );
                    }
                }          
            } );

        cb_list.setOnCheckedChangeListener( new CheckBox.OnCheckedChangeListener( )
            {
                @Override
                public void onCheckedChanged( CompoundButton p1, boolean p2 )
                {
                    showIsList = p2;
                }
            } );

        btn_reStar.setOnClickListener( new ImageButton.OnClickListener( )
            {
                @Override
                public void onClick( View p1 )
                {
                    AlertDialog dialog = new AlertDialog.Builder( Setting.this )
                        .setTitle( getResources( ).getString( R.string.setting_dialog_title_restar ) )
                        .setMessage( getResources( ).getString( R.string.setting_dialog_msg_restar ) )
                        .setIcon( android.R.drawable.ic_dialog_alert )
                        .setPositiveButton( getResources( ).getString( R.string.setting_dialog_btn_yes_restar ), new DialogInterface.OnClickListener( ) 
                        {
                            @Override 
                            public void onClick( final DialogInterface dia, final int which )
                            {   
                                if ( Tutorials.addPass( Setting.this ) )
                                {
                                    Tutorials.loadingDialogShow( Setting.this );

                                    Handler handler = new Handler( );
                                    handler.postDelayed( new Runnable( )
                                        {
                                            @Override
                                            public void run( )
                                            {
                                                boolean is = ( (ActivityManager) getSystemService( ACTIVITY_SERVICE ) ).clearApplicationUserData( );

                                                Tutorials.loadingDialogDismiss( );

                                                if ( is )
                                                {
                                                    startActivity( new Intent( Setting.this, Login.class ) );
                                                    finish( );
                                                }
                                            }        
                                        }, Tutorials.TIME_LOADING );
                                }
                            }
                        } )
                        .setNegativeButton( getResources( ).getString( R.string.setting_dialog_btn_no_restar ), null )
                        .create( );
                    dialog.show( );
                }

            } );
    }

    @Override
    public void onBackPressed( )
    {
        b.putBoolean( LIST, showIsList );
        b.putBoolean( SHOW, showCopy );
        b.putBoolean( THEME, theme );
        b.putBoolean( AUTO_THEME, autoTheme );
        b.putString( LANG , lang );
        b.putString( TEXT, et.getText( ).toString( ) );
        intent.putExtra( DATA, b );
        setResult( RESULT_OK, intent );

        finish( );
    }

    public void setLocale( String lang )
    {
        if ( !lang.isEmpty( ) )
        {
            Locale myLocale = new Locale( lang );

            DisplayMetrics dis = getResources( ).getDisplayMetrics( );
            Configuration con = getResources( ).getConfiguration( );

            con.locale = myLocale;
            getResources( ).updateConfiguration( con, dis );
        }

        onBackPressed( );
    }

}
