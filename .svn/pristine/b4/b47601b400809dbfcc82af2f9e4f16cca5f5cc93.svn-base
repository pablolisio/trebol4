package com.proit.wicket.components;

import java.io.Serializable;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class ColoredLabel extends Label{
	private static final long serialVersionUID = 1L;
	
	private String color;
	
	public ColoredLabel(final String id, String color){
		super(id);
		this.color = color;
	}

	public ColoredLabel(final String id, String label, String color){
		super(id, new Model<String>(label));
		this.color = color;
	}

	public ColoredLabel(final String id, Serializable label, String color){
		super(id, Model.of(label));
		this.color = color;
	}

	public ColoredLabel(final String id, IModel<?> model, String color){
		super(id, model);
		this.color = color;
	}
	
	@Override
	protected void onComponentTag(final ComponentTag tag){
	    super.onComponentTag(tag);
	    tag.put("class", color);
	}	

}
