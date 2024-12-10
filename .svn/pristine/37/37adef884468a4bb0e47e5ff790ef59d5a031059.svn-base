package com.proit.wicket.components;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.wicket.markup.html.form.ChoiceRenderer;

public class CalendarChoiceRenderer extends ChoiceRenderer<Calendar> {
	private static final long serialVersionUID = 1L;
	
	private String dateFormatStr;
	
	public CalendarChoiceRenderer(String dateFormatStr){
		this.dateFormatStr = dateFormatStr;
	}

	@Override
	public Object getDisplayValue(Calendar object){
		DateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
		return dateFormat.format(object.getTime());
	}
}