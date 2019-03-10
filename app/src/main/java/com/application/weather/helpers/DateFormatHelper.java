package com.application.weather.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatHelper {

	public static String formatTime(String date)
	{
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
			Date newDate = null;
			newDate = format.parse(date);
			format = new SimpleDateFormat("yyyy-MM-dd");
			return format.format(newDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;

	}

	public static String datetoDay(String date)
	{
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
			Date newDate = null;
			newDate = format.parse(date);
			format = new SimpleDateFormat("EEEE");
			return format.format(newDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;

	}

}
