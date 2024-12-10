package com.proit.wicket.components;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.Strings;

import com.googlecode.wicket.jquery.ui.form.autocomplete.AutoCompleteTextField;

/**
 * Creada para ser utilizada en cualquier SearchAutocompleteTextField
 * @author plisio
 *
 */
public abstract class GenericSearchAutoCompleteTextField extends AutoCompleteTextField<String> {
	private static final long serialVersionUID = 1L;
	
	public GenericSearchAutoCompleteTextField(String id, IModel<String> model) {
		super(id, model);
	}
	
	@Override
	protected List<String> getChoices(String textToSearch) {
		List<String> selectableFacturas = new ArrayList<String>();
		
		if ( ! Strings.isEmpty(textToSearch) && textToSearch.length() >= getMinimumAmountOfDigits() ) {
			List<String> facturas = getAllStringChoices(textToSearch);
			
			int contador = 0;
			for (String factura : facturas) {
				selectableFacturas.add(factura);
				if (++contador == 20) { break; } // limits the number of results
			}
		}
		
		return selectableFacturas;
	}
	
	protected abstract List<String> getAllStringChoices(String textToSearch);
	
	protected abstract int getMinimumAmountOfDigits();

}
