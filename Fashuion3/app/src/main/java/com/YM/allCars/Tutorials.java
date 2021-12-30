package com.YM.allCars;

import android.content.Context;
import android.content.Intent;
import java.util.Date;
import java.text.SimpleDateFormat;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.Color;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.CompoundButton;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.Locale;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.material.textfield.TextInputLayout;
import android.widget.Toast;
import android.os.Handler;

public class Tutorials 
{
    public static boolean welcome = true;
    private static boolean camera = true;
    private static boolean copy = true;
    private static boolean isPasswordValid = false;
    public static boolean isOk = false;
    public static final int TIME_LOADING = 2000;
    private static final String CAMERA = "B" ;
    private static final String COPY = "A" ;
    private static int today;

    private static Dialog dialog, dialog1, dialog2;
    private static Date date;
    private static SimpleDateFormat sdf;
    private static SharedPreferences save;
    private static SharedPreferences.Editor edit; 

    private static CheckBox cb_show;
    private static ImageView image_close;
    private static TextView tv_title, tv_msg, tv_next, tv_cb_text;
    private static Button btn_check;
    private static EditText et_pass;
    private static TextInputLayout error_pass;

    public static void welcomeBast( Context context )
    {
        String title = context.getResources( ).getString( R.string.tutorials_dialog_title );
        String msg =  context.getResources( ).getString( R.string.tutorials_dialog_msg_welcome ) + " " + Login.name;

        dialog = getDialog( context );
        tv_title.setText( title );
        tv_msg.setText( msg );

        tv_next.setOnClickListener( new View.OnClickListener( )
            {
                @Override
                public void onClick( View p1 )
                {
                    dialog.cancel( );
                }
            } );

        cb_show.setVisibility( View.GONE );
        tv_cb_text.setVisibility( View.GONE );

        if ( welcome )
        {
            welcome = !welcome;
            dialog.show( );
        }
    }

    public static void useCamera( Context context )
    {
        String title = context.getResources( ).getString( R.string.tutorials_dialog_title );
        String msg = context.getResources( ).getString( R.string.tutorials_dialog_msg_camera );

        dialog = getDialog( context );
        tv_title.setText( title );
        tv_msg.setText( msg );

        tv_next.setOnClickListener( new View.OnClickListener( )
            {
                @Override
                public void onClick( View p1 )
                {
                    dialog.cancel( );
                    inputEdit( p1.getContext( ) );
                }
            } );

        cb_show.setVisibility( View.GONE );
        tv_cb_text.setVisibility( View.GONE );
        tv_next.setText( context.getResources( ).getString( R.string.dialog_tutorial_tv_next_text ) );

        camera = dontShow( CAMERA, camera );

        if ( camera )
        {
            dialog.show( );
        }
    }

    public static void inputEdit( Context context )
    {
        String title = context.getResources( ).getString( R.string.tutorials_dialog_title );
        String msg = context.getResources( ).getString( R.string.tutorials_dialog_msg_input );

        dialog = getDialog( context );
        tv_title.setText( title );
        tv_msg.setText( msg );

        tv_next.setOnClickListener( new View.OnClickListener( )
            {
                @Override
                public void onClick( View p1 )
                {
                    dialog.dismiss( );
                }
            } );

        camera = dontShow( CAMERA, camera );

        if ( camera )
        {
            camera = !camera;
            dialog.show( );
        }
    }

    public static boolean welcome( Context context )
    {
        dialog = getDialog( context );

        if ( getDay( context ) != 0 && getDay( context ) > 0 )
        {
            String title = context.getResources( ).getString( R.string.tutorials_dialog_title );
            String msg =  context.getResources( ).getString( R.string.tutorials_dialog_msg_welcome ) + " " + Login.name + "\n" + context.getResources( ).getString( R.string.tutorials_dialog_msg_day ) + " [ " + getDay( context ) + " ] " + context.getResources( ).getString( R.string.tutorials_dialog_msg_day2 );

            tv_title.setText( title );
            tv_msg.setText( msg );

            tv_next.setOnClickListener( new View.OnClickListener( )
                {
                    @Override
                    public void onClick( View p1 )
                    {
                        dialog.cancel( );
                        welcome( p1.getContext( ) );
                    }
                } );

            cb_show.setVisibility( View.GONE );
            tv_cb_text.setVisibility( View.GONE );

            if ( welcome )
            {
                welcome = !welcome;
                dialog.show( );
            }

            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean addPass( Context context )
    {
        dialog1 = new Dialog( context );

        dialog1.setContentView( R.layout.custom_dialog_pass );
        dialog1.getWindow( ).setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );

        btn_check = dialog1.findViewById( R.id.custom_dialog_pass_btn_check );
        et_pass = dialog1.findViewById( R.id.custom_dialog_pass_et_pass );
        error_pass = dialog1.findViewById( R.id.custom_dialog_pass_til_errorPass );
        image_close = dialog1.findViewById( R.id.custom_dialog_pass_iv_close );

        image_close.setOnClickListener( new View.OnClickListener( )
            {
                @Override
                public void onClick( View p1 )
                {
                    dialog1.dismiss( );
                }
            } );

        btn_check.setOnClickListener( new View.OnClickListener( ) 
            {
                @Override 
                public void onClick( View view )
                {
                    if ( et_pass.getText( ).toString( ).trim( ).isEmpty( ) )
                    {
                        error_pass.setError( view.getResources( ).getString( R.string.password_error ) );
                        isPasswordValid = false;
                    }
                    else if ( et_pass.getText( ).toString( ).trim( ).length( ) < 6 )
                    {
                        error_pass.setError( view.getResources( ).getString( R.string.error_invalid_password ) );
                        isPasswordValid = false;
                    }
                    else
                    {
                        isPasswordValid = true;
                        error_pass.setErrorEnabled( false );
                    }

                    if ( isPasswordValid && et_pass.getText( ).toString( ).trim( ).equals( Login.pass ) )
                    {
                        isOk = true;
                        dialog1.dismiss( );
                    }
                    else
                    {
                        Toast.makeText( view.getContext( ), view.getResources( ).getString( R.string.tutorials_dialog_rongPass_toast ), Toast.LENGTH_SHORT ).show( );
                        et_pass.setSelection( 0, et_pass.getText( ).toString( ).length( ) );
                    }
                }
            } );

        dialog1.show( );

        return isOk;
    }

    public static void loadingDialogShow( Context context )
    {
        dialog2 = new Dialog( context );

        dialog2.setContentView( R.layout.custom_loading_dialog );
        dialog2.getWindow( ).setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
        dialog2.setCancelable( false );

        dialog2.show( );
    }

    public static void loadingDialogDismiss( )
    {
        dialog2.dismiss( );
    }

    public static void useCopy( Context context )
    {
        String title = context.getResources( ).getString( R.string.tutorials_dialog_title );
        String msg = context.getResources( ).getString( R.string.tutorials_dialog_msg1_copy ) + "\n" + context.getResources( ).getString( R.string.tutorials_dialog_msg2_copy ) + "\n" + context.getResources( ).getString( R.string.tutorials_dialog_msg3_copy );

        dialog = getDialog( context );
        tv_title.setText( title );
        tv_msg.setText( msg );

        tv_next.setOnClickListener( new View.OnClickListener( )
            {
                @Override
                public void onClick( View p1 )
                {
                    dialog.dismiss( );
                }
            } );

        copy = dontShow( COPY, copy );

        if ( copy )
        {
            copy = !copy;
            dialog.show( );
        }
    }

    private static Dialog getDialog( Context context )
    {
        save = PreferenceManager.getDefaultSharedPreferences( context );
        edit = save.edit( );

        final Dialog dialog = new Dialog( context );

        dialog.setContentView( R.layout.custom_dialog );
        dialog.getWindow( ).setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );

        image_close = dialog.findViewById( R.id.dialog_iv_close );
        cb_show = dialog.findViewById( R.id.dialog_cb_show  );
        tv_next = dialog.findViewById( R.id.dialog_tv_ok );
        tv_title = dialog.findViewById( R.id.dialog_tv_title );
        tv_msg = dialog.findViewById( R.id.dialog_tv_msg );
        tv_cb_text = dialog.findViewById( R.id.dialog_tv_cb_text );

        if ( MainActivity.theme )
        {
            tv_next.setTextColor( Color.parseColor( "#00cccc" ) );
        }
        else
        {
            tv_next.setTextColor( Color.parseColor( "#FF5722" ) );
        }

        image_close.setOnClickListener( new View.OnClickListener( )
            {
                @Override
                public void onClick( View p1 )
                {
                    dialog.dismiss( );
                }
            } );

        return dialog;
    }

    private static boolean dontShow( final String name , boolean show )
    {
        cb_show.setOnCheckedChangeListener( new CheckBox.OnCheckedChangeListener( )
            {
                @Override
                public void onCheckedChanged( CompoundButton p1, boolean p2 )
                {
                    if ( p2 )
                    {
                        edit.putBoolean( name, false );
                        edit.apply( );
                    }
                }       
            } );

        if ( show )
        {
            show =  save.getBoolean( name, true );
        }

        return show;
    }

    private static int getDay( Context context )
    {
        date = new Date( );
        sdf = new SimpleDateFormat( "D", Locale.ENGLISH );

        boolean one = save.getBoolean( "one", true );
        int tomuro = Integer.parseInt( sdf.format( date ) );

        int day = AddCode.getDay( context );

        if ( one )
        {
            today = Integer.parseInt( sdf.format( date ) );
            edit.putInt( "today", today );
            edit.putBoolean( "one", false );
            edit.apply( );
        }

        today = save.getInt( "today", -1 );

        if ( tomuro != today && today != -1 )
        {
            day--;

            edit.putInt( "reee", day ); 
            edit.putInt( "today", tomuro );
            edit.apply( );
        }

        return day;
    }

}
