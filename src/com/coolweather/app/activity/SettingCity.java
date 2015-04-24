package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.coolweather.app.R;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.Citys.City;
import com.coolweather.app.util.SharedPreferencesUtil;

public class SettingCity extends Activity{
	private EditText resarch;
	
	private ListView city_list;
	private List<City> list ;
	private MyAdapter myAdapter;
	private ImageView resarchText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		list=new ArrayList<City>();
		for(int i=0;i<3;i++){
			City city = new City();
			city.citynm=i+"";
			list.add(city);
		};
		

		resarch=(EditText)findViewById(R.id.resarch);
		
		city_list=(ListView)findViewById(R.id.city_item);
		
		resarchText=(ImageView)findViewById(R.id.resarch_btn);
		
		resarchText.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						list=null;
						list=CoolWeatherDB.getInstance(SettingCity.this).loadByConfidition(resarch.getText().toString());
						System.out.println(list.size()+"查询结果的所有大小是：");
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if(myAdapter==null){
									initListView();
								};
								myAdapter.notifyDataSetChanged();
							}
						});
					}
				}).start();	
			
			}
		});	
		city_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Builder builder = new AlertDialog.Builder(SettingCity.this);
				final TextView textView=(TextView) arg1;
				builder.setTitle("提示信息");
				builder.setMessage("你确定将"+textView.getText()+"设为本地信息吗？");
				builder.setPositiveButton("确定",new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SharedPreferencesUtil.putValue(SettingCity.this, "location",(String)textView.getTag());
					    Intent intent = new Intent(SettingCity.this,MainActivity.class);
					    startActivity(intent);
					}
					
				});
				builder.setNegativeButton("取消",null);
				builder.setCancelable(false);
				builder.show();
			}
		
		
		});
	};
	public void initListView() {
		myAdapter=new MyAdapter();
		city_list.setAdapter(myAdapter);
	}
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView text;
			if(convertView!=null){
				text=(TextView) convertView;
			}else {
				text=new TextView(SettingCity.this);
			}
			text.setText(list.get(position).citynm);
			text.setTag(list.get(position).weaid);
			return text;
		}
		
	}
}
