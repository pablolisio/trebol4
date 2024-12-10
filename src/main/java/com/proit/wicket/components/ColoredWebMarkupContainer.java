package com.proit.wicket.components;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public class ColoredWebMarkupContainer extends WebMarkupContainer{
	private static final long serialVersionUID = 1L;
	
	private String color;

	public ColoredWebMarkupContainer(String id, String color){
		super(id);
		this.color = color;
	}
	
	public ColoredWebMarkupContainer(String id, IModel<?> model, String color){
		super(id, model);
		this.color = color;
	}
	
	@Override
	protected void onComponentTag(final ComponentTag tag){
	    super.onComponentTag(tag);
	    tag.put("class", color);
	}	

}
