package com.example.android.weather;


import android.graphics.Bitmap;

public class RowItem {

	double TEMPERATURE, HUMIDITY, WINDSPEED, MINTEMP, MAXTEMP;
	String CONDITION, date;
    Bitmap image;

	public RowItem(double TEMPERATURE, double MAXTEMP, double MINTEMP, double HUMIDITY, double WINDSPEED, String CONDITION,
                   Bitmap image, String date) {

		this.TEMPERATURE = TEMPERATURE;
		this.MAXTEMP = MAXTEMP;
		this.MINTEMP = MINTEMP;
		this.CONDITION = CONDITION;
		this.HUMIDITY=HUMIDITY;
		this.WINDSPEED=WINDSPEED;
		this.image = image;
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getTemp() {
		return TEMPERATURE;
	}

	public void setTemp(double TEMPERATURE) {
		this.TEMPERATURE = TEMPERATURE;
	}

	public double getTemp_max() {
		return MAXTEMP;
	}

	public void setTemp_max(double MAXTEMP) {
		this.MAXTEMP = MAXTEMP;
	}

	public double getTemp_min() {
		return MINTEMP;
	}

	public void setTemp_min(double MINTEMP) {
		this.MINTEMP = MINTEMP;
	}

	public String getCondition() {
		return CONDITION;
	}

	public void setCondition(String CONDITION) {
		this.CONDITION = CONDITION;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setIcon(String icon) {
		this.image = image;
	}

	public double getWindspeed(){return WINDSPEED; }

	public void setWindspeed(double WINDSPEED){this.WINDSPEED=WINDSPEED;}

	public double getHumidity(){return HUMIDITY;}

	public void setHumidity(double HUMIDITY){this.HUMIDITY=HUMIDITY;}

}
