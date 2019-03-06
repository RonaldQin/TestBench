package com.example.sendmail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private EditText et_toAddr, et_subject, et_content, et_phoneNum;
    Spinner sp_isEncrypt;
    Button btn_send;
    boolean is_encrypt = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_toAddr = (EditText) findViewById(R.id.et_toAddr);
        et_subject = (EditText) findViewById(R.id.et_subject);
        et_content = (EditText) findViewById(R.id.et_content);
        et_phoneNum = (EditText) findViewById(R.id.et_phoneNum);

        sp_isEncrypt = (Spinner) findViewById(R.id.sp_isEncrypt);

        sp_isEncrypt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected_item = parent.getItemAtPosition(position).toString();
                if (selected_item.equals("不加密邮件")) {
                    is_encrypt = false;
                } else {
                    is_encrypt = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btn_send = (Button) findViewById(R.id.btn_send);
    }

    public void send(View view) {
        String toAddr = et_toAddr.getText().toString();
        if (!Tool.checkEmail(toAddr)) {
        	Toast.makeText(getBaseContext(), "输入格式有错误！", Toast.LENGTH_SHORT).show();
        	return;
        }
        String subject = et_subject.getText().toString();
        String content = et_content.getText().toString();
        if (subject == null || subject.equals("") || content == null || content.equals("")) {
        	Toast.makeText(getBaseContext(), "对不起，您的邮件未填写完整！", Toast.LENGTH_SHORT).show();
        	return;
        }
        String phone = et_phoneNum.getText().toString();
        if (is_encrypt) {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "content.txt");
            OutputStream os = null;
            try {
            	os = new FileOutputStream(file);
            	byte[] data = content.getBytes();
            	os.write(data);
            } catch (Exception e) {
            	e.printStackTrace();
            } finally {
            	try {
            		if (os != null) {
            			os.close();
            		}
            	} catch (Exception e) {
            		e.printStackTrace();
            	}
            }
            SendMailUtil.send(toAddr, Tool.secret(subject), Tool.secret(content), file);
        } else {
            SendMailUtil.send(toAddr, subject, content);
        }
        if (phone != null || !phone.equals("")) {
        	TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        	String tel = tm.getLine1Number();
        	SmsManager sms = SmsManager.getDefault();
        	sms.sendTextMessage(phone, null, "账号为：" + fromAddr + "的用户给您地址为：" + toAddr + "的邮箱发送了一封邮件，请注意查收！", null, null);
        }
        et_subject.setText("");
        et_content.setText("");
        et_phoneNum.setText("");
        sp_isEncrypt.setSelection(0);
        Toast.makeText(this, "邮件已发送！", Toast.LENGTH_SHORT).show();
    }

    String host, fromAddr, authPwd, port;

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        host = sp.getString("host", null);
        fromAddr = sp.getString("fromAddr", null);
        authPwd = sp.getString("authPwd", null);
        port = sp.getString("port", null);

        if (host != null && fromAddr != null && authPwd != null && port != null) {
            SendMailUtil.HOST = host;
            SendMailUtil.FROM_ADD = fromAddr;
            SendMailUtil.AUTH_PWD = authPwd;
            SendMailUtil.PORT = port;
        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String selected = item.getTitle().toString();
        if (selected.equals("设置")) {
            startActivity(new Intent(MainActivity.this, SetServerActivity.class));
        } else if (selected.equals("退出")) {
            finish();
        }
        return true;
    }
	
}
