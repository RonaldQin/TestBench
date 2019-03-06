package com.example.knowweather;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public static final int SHOW_RESPONSE = 1;
	
	Button btn_search;
	EditText et_city;
	TextView tv_name, tv_weather, tv_update;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == SHOW_RESPONSE) {
				String result = (String) msg.obj;
				try {
					JSONObject jsonObject = new JSONObject(result);
					JSONArray jsonArray = jsonObject.getJSONArray("results");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject info = jsonArray.getJSONObject(i);
						JSONObject location = info.getJSONObject("location");
						String name = location.getString("name");
						String show_name = "Name: " + name;
						JSONObject now = info.getJSONObject("now");
						String text = now.getString("text");
						String temperature = now.getString("temperature");
						String show_weather = "Weather: " + text + ", " + temperature + " 摄氏度";
						String last_update = info.getString("last_update");
						String show_update = "Last update: " + last_update;
						tv_name.setText(show_name); // sink
						tv_weather.setText(show_weather); // sink
						tv_update.setText(show_update); // sink
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		et_city = (EditText) findViewById(R.id.et_city);
		
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_weather = (TextView) findViewById(R.id.tv_weather);
		tv_update = (TextView) findViewById(R.id.tv_update);
	}
	
	public void search_weather(View view) {
		final String city = et_city.getText().toString(); // source
		// 对city进行验证
		boolean validated = validate(city);
		if (validated) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					HttpClient httpClient = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet("https://api.seniverse.com/v3/weather/now.json?key=kjhssnjkh89qnxhy&location=" + city + "&language=zh-Hans&unit=c");
					try {
						HttpResponse httpResponse = httpClient.execute(httpGet); // sink
						if (httpResponse.getStatusLine().getStatusCode() == 200) {
							HttpEntity entity = httpResponse.getEntity();
							String response = EntityUtils.toString(entity, "utf-8"); // source
							if (response.startsWith("{\"") && response.endsWith("}")) {
								Message message = new Message();
								message.what = SHOW_RESPONSE;
								message.obj = response.toString();
								handler.sendMessage(message); // sink
							} else {
								Toast.makeText(getBaseContext(), "Get data error!", 3).show();
								return;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		} else {
			Toast.makeText(getBaseContext(), "Can not support your input city!", 3).show();
		}
	}
	
	public boolean validate(String c) {
		String[] cities = new String[]{
				"beijing", "shanghai", "nanchang", "shenzhen", "changsha",
		}; 
		for (String city : cities) {
			if (city.equals(c)) {
				return true;
			}
		}
		return false;
	}
}
