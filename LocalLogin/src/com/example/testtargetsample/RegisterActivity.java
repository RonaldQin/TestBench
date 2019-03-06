package com.example.testtargetsample;

import android.app.Activity;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	
	public static EditText et_username, et_password, et_repassword;
	Button btn_submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		et_repassword = (EditText) findViewById(R.id.et_repassword);
		
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(new HandlerSubmit(this));
	}
	
	static class HandlerSubmit implements OnClickListener {
		
		Activity activity;
		
		public HandlerSubmit(Activity activity) {
			this.activity = activity;
		}

		@Override
		public void onClick(View v) {
			String password = et_password.getText().toString().trim();
			String repassword = et_repassword.getText().toString().trim();
			String username = DatabaseUtils.sqlEscapeString(et_username.getText().toString().trim());
			
			if (username.equals("") || password.equals("") || repassword.equals("")) {
				Toast.makeText(activity.getBaseContext(), "Can not have an empty item in this form!", 3).show();
				return;
			}
			
			if (!password.equals(repassword)) {
				Toast.makeText(activity.getBaseContext(), "Repassword doesn't equals password, please re-try!", 3).show();;
				et_password.setText("");
				et_repassword.setText("");
				return;
			}
			
			String sql = "select * from " + DBHelper.TABLE_USER + " where " + DBHelper.COLUMN_NAME + " = " + username + ";";
			DBHelper db_helper = DBHelper.getInstance(activity.getBaseContext(), DBHelper.DATABASE_NAME, null, DBHelper.DATABASE_VERSION);
			SQLiteDatabase db = db_helper.getWritableDatabase();
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor.getCount() != 0) {
				Toast.makeText(activity.getBaseContext(), "This username already contains in database!", 3).show();
				et_username.setText("");
				et_password.setText("");
				et_repassword.setText("");
				activity.finish();
				return;
			} else {
				sql = "insert into " + DBHelper.TABLE_USER + " (" + DBHelper.COLUMN_NAME + ", " + DBHelper.COLUMN_PASSWORD
						+ ") values (" + username + ", " + DatabaseUtils.sqlEscapeString(password) + ");";
				db.execSQL(sql);
				Toast.makeText(activity.getBaseContext(), "Register successfully!", 3).show();
				activity.finish();
			}
		}
		
	}
	
}
