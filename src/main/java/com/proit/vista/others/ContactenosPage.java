package com.proit.vista.others;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.forms.ContactenosForm;

public class ContactenosPage extends FacturarOnLineBasePage{
	private static final long serialVersionUID = 1L;
	
	public ContactenosPage(final PageParameters parameters) {
		super(parameters);
		
		add(new ContactenosForm("form", getRuntimeConfigurationType()));
		add(new FeedbackPanel("feedback"));
		
		facturarOnLineMenu.setVisible(false);
	}
}
