package com.proit.vista.compras.ordenes;

import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.proit.modelo.compras.FacturaCompraOrdenPago;
import com.proit.modelo.compras.OrdenPago;
import com.proit.modelo.compras.PlanCuenta;
import com.proit.servicios.compras.OrdenPagoService;
import com.proit.servicios.compras.PlanCuentaService;
import com.proit.vista.base.FacturarOnLineBasePage;

@AuthorizeInstantiation({"Administrador","Solo Lectura","Desarrollador"})
public class EditarPlanCuentaOPPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	public EditarPlanCuentaOPPage(OrdenPago ordenPago) {
		setearDefaultModel(ordenPago);
		
		crearForm(ordenPago);
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}
	
	private void setearDefaultModel(OrdenPago ordenPago) {
		this.setDefaultModel(Model.of(ordenPago));
	}
	
	private void crearForm(OrdenPago ordenPago) {
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		
		DropDownChoice<PlanCuenta> planCuentaDropDownChoice = crearPlanCuentaDropDownChoice();
				
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit() {
				OrdenPago ordenPago = (OrdenPago)EditarPlanCuentaOPPage.this.getDefaultModelObject();
				
				PlanCuentaService planCuentaService = new PlanCuentaService();
				planCuentaService.updatePlanCuentaOP(ordenPago.getId(), ordenPago.getPlanCuenta().getId());
				planCuentaService.flushSession();
				
				OrdenPagoService ordenPagoService = new OrdenPagoService();
				ordenPago = (OrdenPago) ordenPagoService.get(ordenPago.getId());
				FacturaCompraOrdenPago facturaOrdenPago = new FacturaCompraOrdenPago();
				facturaOrdenPago.setBorrado(false);
				facturaOrdenPago.setFacturaCompra(null);
				facturaOrdenPago.setOrdenPago(ordenPago);
				setResponsePage(new DetallesOrdenesPagoPage(facturaOrdenPago));
				
			}

		};
		
		this.add(form);
		form.add(feedback.setOutputMarkupId(true));
		form.add(planCuentaDropDownChoice);
	}
	
	private DropDownChoice<PlanCuenta> crearPlanCuentaDropDownChoice() {
		PlanCuentaService planCuentaService = new PlanCuentaService();
		List<PlanCuenta> planesCuenta = planCuentaService.getPlanesCuenta();
		
		IModel<PlanCuenta> planCuentaModel = new PropertyModel<PlanCuenta>(getDefaultModel(), "planCuenta");
		DropDownChoice<PlanCuenta> planCuentaDropDownChoice = new DropDownChoice<PlanCuenta>("planCuenta", planCuentaModel, planesCuenta, new ChoiceRenderer<PlanCuenta>("nombre"));
		planCuentaDropDownChoice.setRequired(true);
		
		planCuentaDropDownChoice.setNullValid(true);
		return planCuentaDropDownChoice;
	}

}