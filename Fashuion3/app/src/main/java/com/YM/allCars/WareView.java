package com.YM.allCars;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WareView extends AppCompatActivity
{
    private TextInputEditText et_model, et_color, et_prise, et_desc, et_date;
    private ImageView iv;
    private Toolbar toolbar;

    private int id = -1;
    private final int REQ_PERMISSION = 1;

    private DataBaseAcesst database;
    private Uri uri;

    private final int REQ_CODE = 1;

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

        setContentView( R.layout.activity_ware_view );

        toolbar = findViewById( R.id.toolbar );
        et_model = findViewById( R.id.ware_view_et_model );
        et_color = findViewById( R.id.ware_view_et_color );
        et_prise = findViewById( R.id.ware_view_et_prise );
        et_desc = findViewById( R.id.ware_view_et_desc );
        et_date = findViewById( R.id.ware_view_et_date );
        iv = findViewById( R.id.ware_view_image );

        setSupportActionBar( toolbar );

        Intent intent = getIntent( );
        id = intent.getIntExtra( MainActivity.ID , -1 );

        database = DataBaseAcesst.getAcesst( this );

        if ( id == -1 )
        {
            //ADD
            setEnabaled( true );
            Tutorials.useCamera( this );
        }
        else
        {
            //EDIT | SHOW
            setEnabaled( false );
            setSelectAll( true );

            database.open( );
            Ware ware  = database.getWare( id );
            database.close( );

            fillData( ware );
        }

        iv.setOnClickListener( new View.OnClickListener( )
            {
                @Override
                public void onClick( View p1 )
                {
                    if ( ContextCompat.checkSelfPermission( getBaseContext( ), Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED )
                    {
                        ActivityCompat.requestPermissions( WareView.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQ_PERMISSION );
                    }
                    else
                    {
                        Intent intent = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
                        startActivityForResult( intent, REQ_CODE );
                    }
                }      
            } );

    }

    private void setEnabaled( boolean ok )
    {
        et_model.setEnabled( ok );
        et_color.setEnabled( ok );
        et_prise.setEnabled( ok );
        et_desc.setEnabled( ok );
        et_date.setEnabled( false );
        iv.setEnabled( ok );
    }

    private void setSelectAll( boolean ok )
    {
        et_model.setSelectAllOnFocus( ok );
        et_color.setSelectAllOnFocus( ok );
        et_prise.setSelectAllOnFocus( ok );
        et_desc.setSelectAllOnFocus( ok );
    }

    private void fillData( Ware ware )
    {
        et_color.setText( ware.getColor( ) );
        et_desc.setText( ware.getDesc( ) );
        et_prise.setText( ware.getPrice( ) + "" );
        et_date.setText( ware.getDate( ) );
        et_model.setText( ware.getModel( ) );

        if ( ware.getImage( ) != null && !ware.getImage( ).isEmpty( ) )
        {
            iv.setImageURI( Uri.parse( ware.getImage( ) ) );
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        getMenuInflater( ).inflate( R.menu.ware_view_menu, menu );

        MenuItem save = menu.findItem( R.id.save );
        MenuItem delete = menu.findItem( R.id.delete );
        MenuItem edit = menu.findItem( R.id.edit );
        MenuItem copy = menu.findItem( R.id.copy );

        if ( id == -1 )
        {
            save.setVisible( true );
            edit.setVisible( false );
            delete.setVisible( false );
            copy.setVisible( false );
        }
        else
        {
            save.setVisible( false );
            edit.setVisible( true );
            delete.setVisible( true );
            copy.setVisible( MainActivity.showCopy );
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        String model = "", color = "",date = "", desc = "", image = "";
        double prise = 0.0;

        final Ware ware;

        switch ( item.getItemId( ) )
        {
            case R.id.save:

                Date dateTime = new Date( );
                SimpleDateFormat sdf = new SimpleDateFormat( "a hh:mm yyyy/M/dd", java.util.Locale.ENGLISH );

                model = et_model.getText( ).toString( );
                color = et_color.getText( ).toString( );
                desc = et_desc.getText( ).toString( );
                date = sdf.format( dateTime );
                prise = Double.parseDouble( et_prise.getText( ).toString( ) );

                if ( desc.isEmpty( ) )
                {
                    desc = et_desc.getHint( ).toString( );
                }

                if ( color.isEmpty( ) )
                {
                    color = et_color.getHint( ).toString( );
                }

                if ( date.isEmpty( ) )
                {
                    date = et_date.getHint( ).toString( );
                }

                database.open( );
                Ware tempWare =  database.getWare( id );
                database.close( );

                if ( uri == null )
                {
                    image = tempWare.getImage( );
                }
                else
                {
                    image = uri.toString( );
                }

                ware = new Ware( id, image, model, color, date, desc, prise );

                database.open( );

                if ( id == -1 )
                {
                    boolean ok = database.insertData( ware );

                    if ( ok )
                    {
                        Toast.makeText( this, getResources( ).getString( R.string.wareView_save_toast ), Toast.LENGTH_SHORT ).show( );
                    }
                }
                else
                {
                    if ( !ware.getColor( ).equals( tempWare.getColor( ) ) || !ware.getDesc( ).equals( tempWare.getDesc( ) ) || ware.getPrice( ) != tempWare.getPrice( ) || !ware.getModel( ).equals( tempWare.getModel( ) ) || !ware.getImage( ).equals( tempWare.getImage( ) ) )
                    {
                        boolean ok = database.updateData( ware );

                        if ( ok )
                        {
                            Toast.makeText( this, getResources( ).getString( R.string.wareView_edit_toast ), Toast.LENGTH_SHORT ).show( );
                        }
                    }
                }

                database.close( );     

                setResult( RESULT_OK );
                finish( );

                return true;
            case R.id.delete:
                ware = new Ware( id, null, null, null, null, null, 0 );

                AlertDialog dialog = new AlertDialog.Builder( this )
                    .setTitle( getResources( ).getString( R.string.wareView_dialog_title_delete ) )
                    .setMessage( getResources( ).getString( R.string.wareView_dialog_msg_delete ) )
                    .setIcon( R.drawable.dialog_delete )
                    .setPositiveButton( getResources( ).getString( R.string.wareView_dialog_btn_yes_delete ), new DialogInterface.OnClickListener( ) 
                    {
                        @Override 
                        public void onClick( DialogInterface dia, int which )
                        {
                            database.open( );
                            boolean ok = database.deleteData( ware );
                            database.close( );

                            if ( ok )
                            {
                                Toast.makeText( getBaseContext( ), getResources( ).getString( R.string.wareView_delete_toast ), Toast.LENGTH_SHORT ).show( );
                            }                 

                            setResult( RESULT_OK );
                            finish( );

                        }
                    } )
                    .setNegativeButton( getResources( ).getString( R.string.wareView_dialog_btn_no_delete ), null )
                    .create( );
                dialog.show( );

                return true;
            case R.id.edit:
                setEnabaled( true );

                MenuItem save = toolbar.getMenu( ).findItem( R.id.save );
                MenuItem edit = toolbar.getMenu( ).findItem( R.id.edit );
                MenuItem delete = toolbar.getMenu( ).findItem( R.id.delete );
                MenuItem copy = toolbar.getMenu( ).findItem( R.id.copy );

                save.setVisible( true );
                delete.setVisible( false );
                edit.setVisible( false );
                copy.setVisible( false );

                return true;
            case R.id.copy:
                database.open( );
                ware = database.getWare( id );
                database.close( );

                String textCopy = editTextCopy( ware );

                ClipboardManager clipboard = (ClipboardManager)getSystemService( CLIPBOARD_SERVICE );
                ClipData clip = ClipData.newPlainText( "Copy Text", textCopy );
                clipboard.setPrimaryClip( clip );

                Toast.makeText( getBaseContext( ), getResources( ).getString( R.string.wareView_copy_toast ), Toast.LENGTH_LONG ).show( );
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        if ( requestCode == REQ_CODE && resultCode == RESULT_OK )
        {
            if ( data != null )
            {
                uri = data.getData( );
                iv.setImageURI( uri );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult( int requestCode, String[] permissions, int[] grantResults )
    {
        if ( requestCode == REQ_PERMISSION )
        {
            if ( grantResults.length > 0 && grantResults[ 0 ] == PackageManager.PERMISSION_GRANTED )
            {

            }
        }
    }

    private String editTextCopy( Ware ware )
    {
        String result = "";
        String chPrise = "$";
        String model = "#";
        String text = MainActivity.getTextCopy;

        String strPrise = String.valueOf( ware.getPrice( ) );

        int end = strPrise.length( ) - 2;

        String prise = strPrise.substring( 0, end );

        result += text.replace( chPrise, prise ).replace( model, ware.getModel( ) );

        return result ;
    }

}
