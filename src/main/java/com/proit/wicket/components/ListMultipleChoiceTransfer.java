package com.proit.wicket.components;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

public abstract class ListMultipleChoiceTransfer extends Panel {

	private static final long serialVersionUID = 1L;

	/** Component id of the form as used by the default wizard panel. */
	public static final String FORM_ID = "form";

	//The list choices.
	private ListMultipleChoice<String> originals;
	private ListMultipleChoice<String> destinations;

	// The Selected options in the list choices.
	private List<String> selectedOriginals;
	private List<String> selectedDestinations;

	// The options that where transfered to the destination list choice.
	private List<String> currentDestinations = new ArrayList<String>();

	// The Buttons that contain actions.
	private AjaxButton add;
	private AjaxButton remove;

	public ListMultipleChoiceTransfer(String id, List<String> actualRoles) {
		super(id);
		Form<?> form = new Form<Void>(FORM_ID);
		addOrReplace(form);


		originals = new ListMultipleChoice<String>("originals", new PropertyModel<List<String>>(this, "selectedOriginals"), new LoadableDetachableModel<List<String>>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected List<String> load() {
				return getOriginalChoices();
			}
		});
		// make sure we can update this wicket component.
		originals.setOutputMarkupId(true);
		form.add(originals);

		destinations = new ListMultipleChoice<String>("destinations", new PropertyModel<List<String>>(this, "selectedDestinations"), new LoadableDetachableModel<List<String>>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected List<String> load() {
				return currentDestinations;
			}
		});
		destinations.setOutputMarkupId(true);
		form.add(destinations);
		
		
		
		
		updateValues(actualRoles, originals, destinations);
		
		
		

		add = new AjaxButton("add") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				update(target,selectedOriginals, originals, destinations);
			}
		};
		form.add(add);

		remove = new AjaxButton("remove") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				update(target, selectedDestinations, destinations, originals);
			}
		};
		form.add(remove);
	}

	/**
	 * Updates the select boxes.
	 * @param target The {@link AjaxRequestTarget}.
	 */
	private void update(AjaxRequestTarget target, List<String> selections, ListMultipleChoice<String> from, ListMultipleChoice<String> to) {
		updateValues(selections, from, to);
		target.add(to);
		target.add(from);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void updateValues(List<String> selections, ListMultipleChoice<String> from, ListMultipleChoice to) {
		for (String destination : selections) {
			List<String> choices = getChoices(from);
			if (!to.getChoices().contains(destination)) {
				to.getChoices().add(destination);
				choices.remove(destination);
				from.setChoices(choices);
			}
		}
	}

	/**
	 * Retrieves a {@link List} of choices for the given {@link ListMultipleChoice}. Because the
	 * {@link AbstractList} method remove is a stub so we need to <br />
	 * create a new List so we can remove the item.
	 * 
	 * @return
	 */
	private List<String> getChoices(ListMultipleChoice<String> choice) {
		List<String> choices = new ArrayList<String>();
		choices.addAll(choice.getChoices());
		return choices;
	}

	public abstract List<String> getOriginalChoices();


	public ListMultipleChoice<String> getDestinations() {
		return destinations;
	}

	public void setDestinations(ListMultipleChoice<String> destinations) {
		this.destinations = destinations;
	}



}
