package com.example.encrypt;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

	public final String TAG = "com.example.encrypt";
	
	EditText et_password;
	Spinner sp_algorithms, sp_length;
	TextView tv_result;
	String selected_algorithm, selected_length;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		et_password = (EditText) findViewById(R.id.et_password);
		sp_algorithms = (Spinner) findViewById(R.id.sp_algorithm);
		sp_length = (Spinner) findViewById(R.id.sp_length);
		tv_result = (TextView) findViewById(R.id.tv_result);
		
		sp_algorithms.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				selected_algorithm = parent.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		sp_length.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				selected_length = parent.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}
	
	public void encrypt(View view) {
		boolean flag = false;
		String password = et_password.getText().toString(); // source
		if (selected_algorithm.equals("None")) {
			flag = true;
		} else if (selected_algorithm.equals("MD5")) {
			password = encrypt_with_algorithm(password, "MD5");
		} else if (selected_algorithm.equals("Hash")) {
			password = encrypt_with_algorithm(password, "SHA-1");
		}
		if (selected_length.equals("16")) {
			password = password.substring(0, 16);
		} else if (selected_length.equals("24")) {
			password = password.substring(0, 24);
		} else if (selected_length.equals("32")) {
			password = password.substring(0, 32);
		}
		tv_result.setText(flag ? "None" : password); // sink
	}
	
	public void unencrypt(View view) {
		String input = et_password.getText().toString(); // source
		tv_result.setText(input); // sink
	}
	
	private String encrypt_with_algorithm(String plainText, String algorithm) {
		byte[] secretBytes = null;
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(plainText.getBytes());
			secretBytes = md.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("No this algorithm!");
		}
		String md5code = new BigInteger(1, secretBytes).toString(16);
		for (int i = 0; i < 32 - md5code.length(); i++) {
			md5code = "0" + md5code;
		}
		return md5code;
	}
}
