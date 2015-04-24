package com.coolweather.app.util;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

	public static String ObjectToArray(String json){
		try {
			JSONObject object = new JSONObject(json);
			String success = object.getString("success");
			System.out.println("success «£∫"+success);
			if(success.equals("1")){
				JSONObject result = object.optJSONObject("result");
				return parseString(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}

	private static String parseString(JSONObject result) throws JSONException {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{result:[");
		Iterator keys = result.keys();
		while (keys.hasNext()) {
			buffer.append(result.getString(keys.next().toString())+",");
		}
		buffer.deleteCharAt(buffer.length()-1);
		buffer.append("]}");
		return buffer.toString();
	}
}
