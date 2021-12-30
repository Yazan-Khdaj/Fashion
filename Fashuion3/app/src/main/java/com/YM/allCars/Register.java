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
import java.util.Properties;
import javax.mail.Session;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.MessagingException;
import javax.mail.Transport;
import android.os.StrictMode;
import papaya.in.sendmail.SendMail;
import android.net.Uri;
import androidx.annotation.IntRange;

public class Register extends AppCompatActivity
{
    private EditText name, email, phone, password;
    private Button register;
    private TextView login;
    private TextInputLayout nameError, emailError, phoneError, passError;

    private boolean isNameValid, isEmailValid, isPhoneValid, isPasswordValid;
    public static boolean isRegister = false;
    public static String EMAIL = "";
    public static String PASS = "";
    public static String NAME = "";
    public static String NO_RRGISTER = "no";

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );

        name = findViewById( R.id.name );
        email = findViewById( R.id.email );
        phone = findViewById( R.id.phone );
        password = findViewById( R.id.password );
        login = findViewById( R.id.login );
        register = findViewById( R.id.register );
        nameError = findViewById( R.id.nameError );
        emailError = findViewById( R.id.emailError );
        phoneError = findViewById( R.id.phoneError );
        passError = findViewById( R.id.passError ); 

        //يجب اضافتها لارسال البريد
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder( ).permitAll( ).build( );
        StrictMode.setThreadPolicy( policy );

        register.setOnClickListener( new View.OnClickListener( ) 
            {
                @Override
                public void onClick( View v )
                {
                    SetValidation( );   
                }
            } );

        login.setOnClickListener( new View.OnClickListener( ) 
            {
                @Override
                public void onClick( View v )
                {
                    // redirect to LoginActivity
                    Intent intent = new Intent( getApplicationContext( ), Login.class );
                    intent.putExtra( NO_RRGISTER, false );
                    startActivity( intent );
                    finish( );
                }
            } );
    }

    public void SetValidation( ) 
    {
        // Check for a valid name.
        if ( name.getText( ).toString( ).trim( ).isEmpty( ) ) 
        {
            nameError.setError( getResources( ).getString( R.string.name_error ) );
            isNameValid = false;
        }
        else
        {
            isNameValid = true;
            nameError.setErrorEnabled( false );
        }

        // Check for a valid email address.
        if ( email.getText( ).toString( ).isEmpty( ) ) 
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

        // Check for a valid phone number.
        if ( phone.getText( ).toString( ).isEmpty( ) && phone.getText( ).toString( ).trim( ).length( ) == 10 ) 
        {
            phoneError.setError( getResources( ).getString( R.string.phone_error ) );
            isPhoneValid = false;
        }
        else
        {
            isPhoneValid = true;
            phoneError.setErrorEnabled( false );
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

        if ( isNameValid && isEmailValid /*&& isPhoneValid*/ && isPasswordValid )
        {
            EMAIL = email.getText( ).toString( ).trim( );
            PASS = password.getText( ).toString( ).trim( );
            NAME = name.getText( ).toString( ).trim( );

            if ( ( isRegister = sendData( ) ) )
            {
                Tutorials.loadingDialogShow( Register.this ) ;

                Handler handler = new Handler( );
                handler.postDelayed( new Runnable( )
                    {
                        @Override
                        public void run( )
                        {
                            Toast.makeText( getApplicationContext( ), getResources( ).getString( R.string.register_toast ) , Toast.LENGTH_SHORT ).show( );

                            Tutorials.loadingDialogDismiss( );

                            Intent intent = new Intent( Register.this, Login.class );
                            startActivity( intent );
                            finish( );                      
                        }        
                    }, Tutorials.TIME_LOADING );
            }
        }

    }

    private boolean sendData( )
    {
        try
        {
            Intent sendEmail = new Intent( Intent.ACTION_SENDTO );

            sendEmail.setData( Uri.parse( "mailto:" ) );
            sendEmail.putExtra( Intent.EXTRA_EMAIL, "fashuion3@gmail.com" );
            sendEmail.putExtra( Intent.EXTRA_SUBJECT, "مشترك جديد" );
            sendEmail.putExtra( Intent.EXTRA_TEXT, "hi" );

            startActivity( Intent.createChooser( sendEmail, "اختر طريقة" ) );
            
            return true;
        }
        catch (Exception e)
        {
            Toast.makeText( this, getResources( ).getString( R.string.register_notEmail_toast ), Toast.LENGTH_SHORT ).show( );
            
            return false;
        }
    }
//        Properties props = new Properties( );
//
//        props.put( "mail.smtp.auth", "true" );
//        props.put( "mail.smtp.starttls.enable", "true" );
//        props.put( "mail.smtp.host", "smtp.gmail.com" );
//        props.put( "mail.smtp.port", "587" );
//
//        Session session = Session.getInstance( props, new javax.mail.Authenticator( )
//            {
//                @Override
//                protected PasswordAuthentication getPasswordAuthentication( )
//                {
//                    return new PasswordAuthentication( email.getText( ).toString( ).trim( ), password.getText( ).toString( ).trim( ) );
//                }
//            } );
//
//        try
//        {
//            Message msg = new MimeMessage( session );
//
//            msg.setFrom( new InternetAddress( email.getText( ).toString( ).trim( ) ) );
//            msg.setRecipients( Message.RecipientType.TO, InternetAddress.parse(  "fashuion3@gmail.com" ) ) ;
//            msg.setSubject( "مشترك جديد" );
//            msg.setText( "Name: " + name.getText( ).toString( ) + "\nEmail: " + email.getText( ).toString( ).trim( ) + "\nPassword: " + password.getText( ).toString( ).trim( ) + "\nNumber: " + phone.getText( ).toString( ) );
//
//            Transport.send( msg );
//        }
//        catch (MessagingException e)
//        {
//            Toast.makeText( this, getResources( ).getString( R.string.register_notEmail_toast ), Toast.LENGTH_LONG ).show( );
//
//            return false;
//        }
//
//        return true;    
//    }


    private String hashPass( String pass )
    {
        String newPass = "";

        char[] arr = pass.toCharArray( );

        for ( int i = 0; i < arr.length; i++ )
        {
            switch ( arr[ i ] )
            {
                case 'a':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "1&" );
                    break;
                case 'b':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "2&" );
                    break;
                case 'c':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "3&" );
                    break;
                case 'd':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "4&" );
                    break;
                case 'e':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "5&" );
                    break;
                case 'f':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "6&" );
                    break;
                case 'g':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "7&" );
                    break;
                case 'h':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "8&" );
                    break;
                case 'i':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "9&" );
                    break;
                case 'j':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "10&" );
                    break;
                case 'k':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "11&" );
                    break;
                case 'l':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "12&" );
                    break;
                case 'm':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "13&" );
                    break;
                case 'n':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "14&" );
                    break;
                case 'o':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "15&" );
                    break;
                case 'p':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "16&" );
                    break;
                case 'q':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "17&" );
                    break;
                case 'r':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "18&" );
                    break;
                case 's':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "19&" );
                    break;
                case 't':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "20&" );
                    break;
                case 'u':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "21&" );
                    break;
                case 'v':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "22&" );
                    break;
                case 'w':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "23&" );
                    break;
                case 'x':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "24&" );
                    break;
                case 'y':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "25&" );
                    break;
                case 'z':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "26&" );
                    break;
                case 'A':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "1&&" );
                    break;
                case 'B':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "2&&" );
                    break;
                case 'C':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "3&&" );
                    break;
                case 'D':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "4&&" );
                    break;
                case 'E':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "5&&" );
                    break;
                case 'F':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "6&&" );
                    break;
                case 'G':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "7&&" );
                    break;
                case 'H':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "8&&" );
                    break;
                case 'I':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "9&&" );
                    break;
                case 'J':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "10&&" );
                    break;
                case 'K':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "11&&" );
                    break;
                case 'L':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "12&&" );
                    break;
                case 'M':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "13&&" );
                    break;
                case 'N':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "14&&" );
                    break;
                case 'O':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "15&&" );
                    break;
                case 'P':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "16&&" );
                    break;
                case 'Q':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "17&&" );
                    break;
                case 'R':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "18&&" );
                    break;
                case 'S':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "19&&" );
                    break;
                case 'T':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "20&&" );
                    break;
                case 'U':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "21&&" );
                    break;
                case 'V':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "22&&" );
                    break;
                case 'W':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "23&&" );
                    break;
                case 'X':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "24&&" );
                    break;
                case 'Y':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "25&&" );
                    break;
                case 'Z':
                    newPass += arr[ i ] + "".replace( arr[ i ] + "", "26&&" );
                    break;
                default:
                    newPass += arr[ i ] + "&";
                    break;

            }
        }

        return newPass;
    }

}
