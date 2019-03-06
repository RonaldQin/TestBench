package com.example.sendmail;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetServerActivity extends Activity {

    EditText et_host, et_fromAddr, et_authPwd, et_port, et_protocols;
//    Spinner sp_protocols;
    Button btn_save;
    String protocol = "smtp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_server);

        et_host = (EditText) findViewById(R.id.et_host);
        et_fromAddr = (EditText) findViewById(R.id.et_fromAddr);
        et_authPwd = (EditText) findViewById(R.id.et_authPwd);
        et_port = (EditText) findViewById(R.id.et_port);
        et_protocols = (EditText) findViewById(R.id.et_protocol);

        /*sp_protocols = (Spinner) findViewById(R.id.sp_protocol);
        sp_protocols.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               String selected = parent.getItemAtPosition(position).toString();
               if (selected.equals("POP3/SMTP")) {
                    protocol = "smtp";
               } else if (selected.equals("IMAP/SMTP")) {
                    protocol = "imap";
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });*/

        btn_save = (Button) findViewById(R.id.btn_save);
    }

    public void save(View view) {
        String host = et_host.getText().toString();
        String fromAddr = et_fromAddr.getText().toString();
        String authPwd = et_authPwd.getText().toString();
        String port = et_port.getText().toString();
        
        if (!Tool.checkEmail(fromAddr)) {
        	Toast.makeText(getBaseContext(), "输入格式有错误！", Toast.LENGTH_SHORT).show();
        	return;
        }
        
        String input_protocols = et_protocols.getText().toString();
        if (input_protocols.equals("smtp")) {
        	protocol = "smtp";
        } else if (input_protocols.equals("imap")) {
        	protocol = "imap";
        } else {
        	Toast.makeText(getBaseContext(), "对不起，您输入的邮件协议暂不支持！", Toast.LENGTH_SHORT).show();
        	return;
        }

        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("host", host);
        editor.putString("fromAddr", fromAddr);
        editor.putString("authPwd", authPwd);
        editor.putString("port", port);
        editor.apply();

        finish();
    }
}
