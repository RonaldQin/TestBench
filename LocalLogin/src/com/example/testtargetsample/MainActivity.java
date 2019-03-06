package com.example.testtargetsample;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;



public class MainActivity extends Activity {
	
	EditText et_username, et_password;
	Button btn_login, btn_register;
	CheckBox ck_validated;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_register = (Button) findViewById(R.id.btn_register);
		
		ck_validated = (CheckBox) findViewById(R.id.is_validate);
		
	}
	
	public void login(View view) {
		String username = et_username.getText().toString().trim(); // <-- source
		String password = et_password.getText().toString().trim();
		boolean isValidated = ck_validated.isChecked();
		
		boolean login_successfully = false;
		DBHelper db_helper = DBHelper.getInstance(this, DBHelper.DATABASE_NAME, null, DBHelper.DATABASE_VERSION);
		SQLiteDatabase db = db_helper.getWritableDatabase();
		
		String username_validated = DatabaseUtils.sqlEscapeString(username);
		if (isValidated) {
			String password_validated = DatabaseUtils.sqlEscapeString(password);
			String sql_validated = "select * from " + DBHelper.TABLE_USER + " where " + DBHelper.COLUMN_NAME + " = "
					+ username_validated + " and " + DBHelper.COLUMN_PASSWORD + " = " + password_validated + ";";
			Cursor cursor = db.rawQuery(sql_validated, null);
			if (cursor.moveToNext()) {
				login_successfully = true;
				int id = cursor.getInt(1);
				String user = cursor.getString(1);
				String pwd = cursor.getString(2);
				String log_text = new Date() + "|" + id + "|" + user + "|" + pwd + "\n";
				log_login_information(log_text);
			}
		} else {
			String sql = "select * from " + DBHelper.TABLE_USER + " where " 
					+ DBHelper.COLUMN_NAME + " = " + username_validated + " and " // TODO: username
					+ DBHelper.COLUMN_PASSWORD + " = '" + password + "';";
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				login_successfully = true;
				int id = cursor.getInt(1);
				String user = cursor.getString(1);
				String pwd = cursor.getString(2);
				String log_text = new Date() + "|" + id + "|" + user + "|" + pwd + "\n";
				log_login_information(log_text);
			}
		}
		if (username_validated.equals("'administrator'") && password.equals("administrator")) { // TODO: username
			String sql = "select * from " + DBHelper.TABLE_USER + ";";
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				int id = cursor.getInt(1);
				String user = cursor.getString(1);
				String pwd = cursor.getString(2);
				Log.i("All user information: ", id + " - " + user + " - " + pwd + ".");
			}
		}
		
		if (login_successfully) {
			Log.i("Login information: ", "Login successfully!");
			Toast.makeText(this, "Welcome " + username_validated + "!", 3).show(); // <-- sink
		} else {
			Log.i("Login information: ", "Login fairly!");
			Toast.makeText(this, "Login faired!", 3).show();
		}
	}
	
	public void login(View view, int version) {
		String username = et_username.getText().toString().trim(); // source
		String password = et_password.getText().toString().trim();
		boolean isValidated = ck_validated.isChecked();
		
		boolean login_successfully = false;
		DBHelper db_helper = DBHelper.getInstance(this, DBHelper.DATABASE_NAME, null, DBHelper.DATABASE_VERSION);
		SQLiteDatabase db = db_helper.getWritableDatabase();
		
		String username_validated = DatabaseUtils.sqlEscapeString(username);
		if (isValidated) {
			String password_validated = DatabaseUtils.sqlEscapeString(password);
			String sql_validated = "select * from " + DBHelper.TABLE_USER + " where " + DBHelper.COLUMN_NAME + " = "
					+ username_validated + " and " + DBHelper.COLUMN_PASSWORD + " = " + password_validated + ";";
			Cursor cursor = db.rawQuery(sql_validated, null);
			if (cursor.moveToNext()) {
				login_successfully = true;
				int id = cursor.getInt(1);
				String user = cursor.getString(1);
				String pwd = cursor.getString(2);
				String log_text = new Date() + "|" + id + "|" + user + "|" + pwd + "\n";
				log_login_information(log_text);
			}
		} else {
			String sql = "select * from " + DBHelper.TABLE_USER + " where " 
					+ DBHelper.COLUMN_NAME + " = '" + username + "' and "
					+ DBHelper.COLUMN_PASSWORD + " = '" + password + "';";
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				login_successfully = true;
				int id = cursor.getInt(1);
				String user = cursor.getString(1);
				String pwd = cursor.getString(2);
				String log_text = new Date() + "|" + id + "|" + user + "|" + pwd + "\n";
				log_login_information(log_text);
			}
		}
		
		if (username.equals("administrator") && password.equals("administrator")) {
			String sql = "select * from " + DBHelper.TABLE_USER + ";";
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				int id = cursor.getInt(1);
				String user = cursor.getString(1);
				String pwd = cursor.getString(2);
				Log.i("All user information: ", id + " - " + user + " - " + pwd + "."); // sink
			}
		}
		
		if (login_successfully) {
			Log.i("Login information: ", "Login successfully!");
			Toast.makeText(this, "Welcome " + username_validated + "!", 3).show();
		} else {
			Log.i("Login information: ", "Login fairly!");
			Toast.makeText(this, "Login faired!", 3).show();
		}
	}
	
	public void log_login_information(String log_text) {
		FileOutputStream fos = null;
		BufferedWriter bw = null;
		try {
			fos = openFileOutput("log.txt", MODE_APPEND);
			bw = new BufferedWriter(new OutputStreamWriter(fos));
			bw.write(log_text);
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
				bw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void register(View view) {
		new Intent().getIntExtra("", 0);
		startActivity(new Intent(MainActivity.this, RegisterActivity.class));
	}
	
}
