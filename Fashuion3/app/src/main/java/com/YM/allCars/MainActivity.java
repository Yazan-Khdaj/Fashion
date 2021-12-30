// Start this project in 3/11/2021 by Yazan Khdaj
package com.YM.allCars;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import android.util.DisplayMetrics;
import android.content.res.Configuration;

public class MainActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private RecyclerView rv;
    private FloatingActionButton fabtn;

    private ArrayList<Ware> listwares;
    private DataBaseAcesst database;
    private AdapterRecyclerView adapter;
    private RecyclerView.LayoutManager lm;
    private Date date;
    private SimpleDateFormat sdf;

    private final int REQ_ADD = 1;
    private final int REQ_EDIT = 2;
    private final int REQ_SETTING = 2;
    public static final String ID = "id";
    public static final String SETTING = "setting";
    private long time;

    private SharedPreferences save;
    private SharedPreferences.Editor edit;
    public static boolean showCopy = false;
    private boolean showList = true;
    public static String getTextCopy;
    public static String lang = "";
    public static boolean theme;
    public static boolean autoTheme;

    public static final String COPY = "copy";
    public static final String SHOW = "show";
    public static final String TEXT = "text";
    public static final String LANG = "lang";
    public static final String THEME = "theme";
    public static final String AUTO_THEME = "autotheme";
    private final String BAST_EMAIL = "fashuion3@gmail.com";
    private final String BAST_PASS = "yazankh1234554321";

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );

        save = PreferenceManager.getDefaultSharedPreferences( this );
        edit = save.edit( );
        theme = save.getBoolean( THEME, false );
        autoTheme = save.getBoolean( AUTO_THEME, true );
        lang = save.getString( LANG, getResources( ).getString( R.string.lang ) );

        setLocale( lang );

        date = new Date( );
        sdf = new SimpleDateFormat( "H", Locale.ENGLISH );

        if ( autoTheme )
        {
            if ( Integer.parseInt( sdf.format( date ) ) >= 18 || Integer.parseInt( sdf.format( date ) ) <= 5  )
            {
                theme = true;
            }
            else
            {
                theme = false;
            }
        }

        if ( theme )
        {
            setTheme( R.style.Night_AppTheme );
        }
        else
        {
            setTheme( R.style.AppTheme );
        }

        setContentView( R.layout.activity_main );

        toolbar = findViewById( R.id.toolbar );
        rv = findViewById( R.id.main_rv );
        fabtn = findViewById( R.id.main_fabtn );

        setSupportActionBar( toolbar );

        database = DataBaseAcesst.getAcesst( this );

        fabtn.setOnClickListener( new View.OnClickListener( )
            {           
                @Override
                public void onClick( View p1 )
                {
                    Intent intent = new Intent( getBaseContext( ), WareView.class );
                    startActivityForResult( intent, REQ_ADD );
                }
            } );    

        if ( !( Login.emails.equals( BAST_EMAIL )  && Login.pass.equals( BAST_PASS ) ) )
        {
            if ( Tutorials.welcome( this ) )
            {
                startActivity( new Intent( this, AddCode.class ) );
                finish( );
            }
        }
        else
        {
            Tutorials.welcomeBast( this );
        }

    }

    @Override
    protected void onStart( )
    {
        super.onStart( );

        showCopy = save.getBoolean( COPY, false );
        showList = save.getBoolean( SHOW, true );
        getTextCopy = save.getString( TEXT, "" );

        database.open( );
        listwares = database.getAllWares( );
        database.close( );

        adapter = new AdapterRecyclerView( listwares , new RecyclerOnClickLisaner( )
            {
                @Override
                public void clickItem( int id )
                {
                    Intent intent = new Intent( getBaseContext( ), WareView.class );
                    intent.putExtra( ID, id );
                    startActivityForResult( intent, REQ_EDIT );
                }
            } );


        if ( showList )
        {
            lm = new GridLayoutManager( this, 2 );
        }
        else
        {
            lm = new LinearLayoutManager( this );
        }

        rv.setHasFixedSize( true );
        rv.setLayoutManager( lm );
        rv.setAdapter( adapter );
    }


    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        getMenuInflater( ).inflate( R.menu.main_menu, menu );

        final SearchView searchView = (SearchView) menu.findItem( R.id.search ).getActionView( );

        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener( )
            {
                @Override
                public boolean onQueryTextSubmit( String p1 )
                {                
                    return false;
                }

                @Override
                public boolean onQueryTextChange( String p1 )
                {         
                    database.open( );
                    listwares = database.getWares( p1.trim( ) );
                    database.close( );

                    adapter.setWares( listwares );
                    adapter.notifyDataSetChanged( );

                    return true;
                }
            } );

        return true;
    }


    @Override       
    public boolean onOptionsItemSelected( MenuItem item )
    {
        switch ( item.getItemId( ) )
        {
            case R.id.setting:

                Bundle b = new Bundle( );

                b.putBoolean( COPY, showCopy );
                b.putBoolean( SHOW, showList );
                b.putString( TEXT, getTextCopy );
                b.putString( LANG, lang );
                b.putBoolean( THEME, theme );
                b.putBoolean( AUTO_THEME, autoTheme );

                Intent intent = new Intent( getBaseContext( ), Setting.class );
                intent.putExtra( SETTING, b );
                startActivityForResult( intent, REQ_SETTING );

                return true;
            case R.id.abut:

                Intent intent1 = new Intent( getBaseContext( ), Abut.class );
                startActivity( intent1 );

                return true;
            case R.id.exit:

                AlertDialog dialog = new AlertDialog.Builder( this )
                    .setTitle( getResources( ).getString( R.string.main_dialog_title_exit ) )
                    .setMessage(  getResources( ).getString( R.string.main_dialog_msg_exit ) )
                    .setIcon( android.R.drawable.ic_dialog_alert )
                    .setPositiveButton( getResources( ).getString( R.string.main_dialog_btn_yes_exit ), new DialogInterface.OnClickListener( ) 
                    {
                        @Override 
                        public void onClick( DialogInterface dia, int which )
                        {
                            finish( );
                        }
                    } )
                    .setNegativeButton( getResources( ).getString( R.string.main_dialog_btn_no_exit ), null )
                    .create( );
                dialog.show( );

                return true;
        }

        return false;
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        if ( ( requestCode == REQ_ADD || requestCode == REQ_EDIT ) && resultCode == RESULT_OK && data == null )
        {
            database.open( );
            listwares = database.getAllWares( );
            database.close( );

            adapter.setWares( listwares );
            adapter.notifyDataSetChanged( );  
        }

        if ( data != null && requestCode == REQ_SETTING && resultCode == RESULT_OK )
        {
            Bundle b = data.getBundleExtra( Setting.DATA );

            edit.putBoolean( COPY, b.getBoolean( Setting.SHOW ) );
            edit.putBoolean( SHOW, b.getBoolean( Setting.LIST ) );
            edit.putBoolean( THEME, b.getBoolean( Setting.THEME ) );
            edit.putBoolean( AUTO_THEME, b.getBoolean( Setting.AUTO_THEME ) );
            edit.putString( TEXT, b.getString( Setting.TEXT ) );
            edit.putString( LANG, b.getString( Setting.LANG ) );
            edit.apply( );

            finish( );
            startActivity( new Intent( this, MainActivity.class ) );
        }

    } 

    @Override
    public void onBackPressed( )
    {
        if ( time + 2000 > System.currentTimeMillis( ) )
        {
            super.onBackPressed( );
        }
        else
        {
            Toast.makeText( this, getResources( ).getString( R.string.main_back_toast ), Toast.LENGTH_SHORT ).show( );
        }

        time = System.currentTimeMillis( );
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
    }

}
