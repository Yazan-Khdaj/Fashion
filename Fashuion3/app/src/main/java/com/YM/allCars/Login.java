package com.YM.allCars;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import android.os.Handler;

public class Login extends AppCompatActivity
{
    private EditText email, password;
    private Button login;
    private TextView register;
    private TextInputLayout emailError, passError;

    private boolean isEmailValid, isPasswordValid;
    private final String EMAIL = "em";
    private final String PASS = "ps";
    private final String NAME = "na";
    private final String LOGIN = "logins";
    public static String name = "";
    public static String emails = "";
    public static String pass = "";

    private SharedPreferences save;
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        email = findViewById( R.id.email );
        password = findViewById( R.id.password );
        login = findViewById( R.id.login );
        register = findViewById( R.id.register );
        emailError = findViewById( R.id.emailError );
        passError = findViewById( R.id.passError );

        save = PreferenceManager.getDefaultSharedPreferences( this );
        edit = save.edit( );

        if ( getIntent( ).getBooleanExtra( Register.NO_RRGISTER, true ) )
        {
            if ( save.getBoolean( LOGIN, true ) )
            {
                name = save.getString( NAME, getResources( ).getString( R.string.defalt_user_name ) );
                emails = save.getString( EMAIL, "" );
                pass = save.getString( PASS, "" );

                Intent intent = new Intent( getApplicationContext( ), MainActivity.class );
                startActivity( intent );
                finish( );
            }
            else
            {
                edit.putString( EMAIL, Register.EMAIL );
                edit.putString( PASS, Register.PASS );
                edit.putString( NAME, Register.NAME );
                edit.putBoolean( LOGIN, Register.isRegister );
                edit.apply( );
            }
        }

        login.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick( View v )
                {
                    SetValidation( );
                }
            } );

        register.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick( View v )
                {
                    // redirect to RegisterActivity
                    Intent intent = new Intent( getApplicationContext( ), Register.class );
                    startActivity( intent );
                    finish( );
                }
            } );
    }

    public void SetValidation( )
    {
        // Check for a valid email address.
        if ( email.getText( ).toString( ).trim( ).isEmpty( ) )
        {
            emailError.setError( getResources( ).getString( R.string.email_error ) );
            isEmailValid = false;
        }
        else if ( email.getText( ).toString( ).trim( ).length( ) <= 10 || !Patterns.EMAIL_ADDRESS.matcher( email.getText( ).toString( ).trim( ) ).matches( ) )
        {
            emailError.setError( getResources( ).getString( R.string.error_invalid_email ) );
            isEmailValid = false;
        }
        else
        {
            isEmailValid = true;
            emailError.setErrorEnabled( false );
        }

        // Check for a valid password.
        if ( password.getText( ).toString( ).trim( ).isEmpty( ) )
        {
            passError.setError( getResources( ).getString( R.string.password_error ) );
            isPasswordValid = false;
        }
        else if ( password.getText( ).toString( ).trim( ).length( ) < 6 )
        {
            passError.setError( getResources( ).getString( R.string.error_invalid_password ) );
            isPasswordValid = false;
        }
        else
        {
            isPasswordValid = true;
            passError.setErrorEnabled( false );
        }

        if ( isEmailValid && isPasswordValid )
        {
            if ( save.getString( EMAIL, "" ).equals( email.getText( ).toString( ).trim( ) ) && save.getString( PASS, "" ).equals( password.getText( ).toString( ).trim( ) ) )
            {
                name = save.getString( NAME, getResources( ).getString( R.string.defalt_user_name ) );
                emails = save.getString( EMAIL, "" );
                pass = save.getString( PASS, "" );          

                Tutorials.loadingDialogShow( Login.this );

                Handler handler = new Handler( );
                handler.postDelayed( new Runnable( )
                    {
                        @Override
                        public void run( )
                        {
                            Toast.makeText( getApplicationContext( ), getResources( ).getString( R.string.login_toast ), Toast.LENGTH_SHORT ).show( );

                            Tutorials.loadingDialogDismiss( );

                            Intent intent = new Intent( Login.this, MainActivity.class );
                            startActivity( intent );
                            finish( );
                        }        
                    }, Tutorials.TIME_LOADING );
            }
            else
            {
                email.setSelectAllOnFocus( true );
                email.setSelection( 0, email.getText( ).toString( ).length( ) );
                password.setText( "" );

                Toast.makeText( this, getResources( ).getString( R.string.login_rong_toast ), Toast.LENGTH_SHORT ).show( );
            }
        }

    }

}
