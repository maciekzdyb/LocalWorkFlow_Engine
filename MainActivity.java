package inz.lwe_v103;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    EditText email;
    EditText psd;
    Button login;

    readEmail receiver = new readEmail();

    public void newAct(){
        Intent intent = new Intent(this, ToDo.class);
        startActivity(intent);
    }

    public void newAct1() {
        Intent intent = new Intent(this, MergeActivity.class);
        startActivity(intent);

    }

    public void showMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    public void cloceKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            TextView exception = (TextView) findViewById(R.id.textViewException);
            exception.setText(receiver.getException());
            String attachment = receiver.getAtttachment_filename();
            if(attachment!=null) {
                if (attachment.startsWith("MIND")) {
                    newAct();
                    //System.exit(0);
                }
            }
            else{
                showMessage("No attachment to download...");
                email = (EditText) findViewById(R.id.editEmail);
                psd  = (EditText) findViewById(R.id.editPass);
                email.setText("");
                psd.setText("");
            }
        }
    };

    public static Activity ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //newAct1();
        ma = this;
        email = (EditText) findViewById(R.id.editEmail);
        psd  = (EditText) findViewById(R.id.editPass);
        login = (Button) findViewById(R.id.btnLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cloceKeyboard();
                final String host="smtp.gmail.com";
                final String port="465";
                //final String user ="pg2016test1@gmail.com";
                final String user = String.valueOf(email.getText());
                //final String pass = "Bowisz2016";
                final String pass = String.valueOf(psd.getText());
                String Dir;
                File file;
                file = Environment.getExternalStoragePublicDirectory("Documents");
                Dir = file.getPath();
                Dir+="/LWE";
                email = (EditText) findViewById(R.id.editEmail);
                psd  = (EditText) findViewById(R.id.editPass);
                SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("HOST","smtp.gmail.com");
                editor.putString("PORT","465");
                editor.putString("DIR",Dir);
                editor.putString("EMAIL",email.getText().toString());
                editor.putString("PASS",psd.getText().toString());

                editor.apply();
                zip.mkDir(Dir);
                zip.mkDir(Dir+"/ZIP");
                receiver.setSaveDirectory(Dir+"/ZIP");
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        receiver.downloadEmailAttachments(host,port,user,pass);
                        handler.sendEmptyMessage(0);
                    }
                };
                Thread thread = new Thread(r);
                thread.start();
            }
        });
    }
}
