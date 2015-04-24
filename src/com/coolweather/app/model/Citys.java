package com.coolweather.app.model;

import java.util.ArrayList;


public class Citys{
	public ArrayList<City> result;	

	public static class City{
		public int id;
		public String cityno;/*城市拼音*/
		public String weaid;/*天气编号*/
		public String citynm;/*城市的名字*/
		public String cityid;/*城市的编号*/
		
	}
}

