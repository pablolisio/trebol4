package com.proit.vista.caja;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.form.datepicker.AjaxDatePicker;
import com.proit.modelo.CajaChica;
import com.proit.modelo.Rol;
import com.proit.modelo.Usuario;
import com.proit.servicios.CajaChicaService;
import com.proit.servicios.UsuarioService;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.components.CustomTextFieldDouble;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class EditarCajaChicaPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(EditarCajaChicaPage.class.getName());
	
	private CajaChicaService cajaChicaService;
	
	public EditarCajaChicaPage() {
		this(new PageParameters(),null);
	}
	
	public EditarCajaChicaPage(final PageParameters parameters, Integer cajaChicaId) {
		super(parameters);
		cajaChicaService = new CajaChicaService();
		
		setearDefaultModel(cajaChicaId);
		
		boolean esEditar = verificarSiEsEditar(cajaChicaId);
		
		crearForm(esEditar);
		add(new FeedbackPanel("feedback"));
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
	}

	private void setearDefaultModel(Integer cajaChicaId) {
		CajaChica cajaChica;
		if (cajaChicaId != null) {
			cajaChica = (CajaChica) cajaChicaService.get(cajaChicaId);
		} else {
			cajaChica = new CajaChica();
		}
		this.setDefaultModel(Model.of(cajaChica));
	}

	private void crearForm(final boolean esEditar) {
		IModel<String> detalleModel = new PropertyModel<String>(getDefaultModel(), "detalle");
		final TextField<String> detalleTextField = new TextField<String>("detalle", detalleModel);
		detalleTextField.setRequired(true);
		
		AjaxDatePicker ajaxDatePicker = crearDatePicker();
		ajaxDatePicker.setOutputMarkupId(true);
		ajaxDatePicker.setRequired(true);
		
		IModel<Double> montoModel = new PropertyModel<Double>(getDefaultModel(), "monto");
		final CustomTextFieldDouble montoTextField = new CustomTextFieldDouble("monto", montoModel);
		montoTextField.setOutputMarkupId(true);
		montoTextField.setRequired(true);
		montoTextField.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(montoTextField);
            }
        });
		
//		DropDownChoice<Usuario> solicitanteDropDownChoice = crearSolicitanteDropDownChoice();
		
		Link<WebPage> cancelarLink = new Link<WebPage>("cancelarLink") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(DetalleCajaChicaPage.class);
			}
		};
				
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit() {
				submitForm(esEditar);
			}

		};
		Button guardarButton = new Button("guardar");
        
		add(form);
		form.add(detalleTextField);
		form.add(ajaxDatePicker);
		form.add(montoTextField);
//		form.add(solicitanteDropDownChoice);
		form.add(cancelarLink);
		form.add(guardarButton);
	}
	
	private boolean validacionOK(boolean esEditar){		
		boolean validacionOK = true;
		//CajaChica cajaChica = (CajaChica) EditarCajaChicaPage.this.getDefaultModelObject();
		
		//de momento ninguna validacion extra mas que campos requeridos
		
		return validacionOK;
	}
	
	private void submitForm(boolean esEditar) {
		if ( ! validacionOK(esEditar) ) {
			return;
		}
		CajaChica cajaChica = (CajaChica) EditarCajaChicaPage.this.getDefaultModelObject();
		String textoPorPantalla = "El detalle '" + cajaChica.getDetalle() + "' ha sido " + (esEditar ? "editado." : "creado.");
		String resultado = "OK";
		
		try {
			CajaChicaService cajaChicaService = new CajaChicaService();
			
//			//Si selecciono opcion: Otros o Todos
//			if (cajaChica.getUsuarioSolicitante()!=null && cajaChica.getUsuarioSolicitante().getId() == -98) {
//				cajaChica.setUsuarioSolicitante(null);
//				cajaChica.setSolicitadoPorOtros(true);
//				cajaChica.setSolicitadoPorTodos(false);
//			} else if (cajaChica.getUsuarioSolicitante()!=null && cajaChica.getUsuarioSolicitante().getId() == -99) {
//				cajaChica.setUsuarioSolicitante(null);
//				cajaChica.setSolicitadoPorOtros(false);
//				cajaChica.setSolicitadoPorTodos(true);
//			} else {
//				cajaChica.setSolicitadoPorOtros(false);
//				cajaChica.setSolicitadoPorTodos(false);
//			}
			
			cajaChicaService.createOrUpdate(cajaChica);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			textoPorPantalla = "El detalle '" + cajaChica.getDetalle() + "' no pudo ser " + (esEditar ? "editado " : "creado ") + "correctamente. Por favor, vuelva a intentarlo.";
			resultado = "ERROR";
		}
		
		PageParameters pageParameters = new PageParameters();
		pageParameters.add("Resultado", resultado);
		pageParameters.add("TextoPantalla", textoPorPantalla);
		setResponsePage(DetalleCajaChicaPage.class, pageParameters);
	}
	
	private AjaxDatePicker crearDatePicker() {
		IModel<Date> fechaModel = new PropertyModel<Date>(getDefaultModel(), "fechaAsDate");
		AjaxDatePicker ajaxDatePicker = new AjaxDatePicker("datepicker", fechaModel, "dd/MM/yyyy", new Options());
		//ajaxDatePicker.setRequired(true);
		return ajaxDatePicker;
	}
	
	
	//Esto falla porque no me deja setearle un -98 o -99 como id, porque dice que viola la llave foranea, aunque aun no se le da click en guardar, sino q ya lo tira al intentar cargar la pag
//	private DropDownChoice<Usuario> crearSolicitanteDropDownChoice() {
//		UsuarioService usuarioService = new UsuarioService();
//		List<Usuario> solicitantes = usuarioService.getUsuariosByRol(Rol.SOLICITANTE_PAGOS);
//		Usuario usuarioFicticioOtros = agregarUsuarioFicticio(solicitantes, -98, "Otros");
//		Usuario usuarioFicticioTodos = agregarUsuarioFicticio(solicitantes, -99, "Todos");
//		
//		CajaChica cajaChica = (CajaChica) EditarCajaChicaPage.this.getDefaultModelObject();
//
//		IModel<Usuario> solicitanteModel = new PropertyModel<Usuario>(getDefaultModel(), "usuarioSolicitante"); 
//		if (cajaChica.isSolicitadoPorOtros() || cajaChica.isSolicitadoPorTodos()) {
//			Usuario seleccionado = null;
//			for (Usuario solicitante : solicitantes) {
//				if (solicitante.getId()==-98 && cajaChica.isSolicitadoPorOtros()) {
//					seleccionado = solicitante;
//					break;
//				} else if (solicitante.getId()==-99 && cajaChica.isSolicitadoPorTodos()) {
//					seleccionado = solicitante;
//					break;
//				}
//			}
//			solicitanteModel.setObject(seleccionado);
//		}
//		//if (cajaChica.isSolicitadoPorOtros()) solicitanteModel.setObject(usuarioFicticioOtros);
//		//if (cajaChica.isSolicitadoPorTodos()) solicitanteModel.setObject(usuarioFicticioTodos);
//		DropDownChoice<Usuario> solicitanteDropDownChoice = new DropDownChoice<Usuario>("solicitante", solicitanteModel, solicitantes, new ChoiceRenderer<Usuario>("nombreCompleto"));
//		//solicitanteDropDownChoice.setRequired(true);
//		
//		solicitanteDropDownChoice.setNullValid(true);
//		return solicitanteDropDownChoice;
//	}
//	
//	private Usuario agregarUsuarioFicticio(List<Usuario> solicitantes, int id, String nombre) {
//		Usuario usuario = new Usuario();
//		usuario.setId(id);
//		usuario.setNombreORazonSocial(nombre);
//		usuario.setApellido("");
//		usuario.setEmail("");
//		solicitantes.add(usuario);
//		return usuario;
//	}	

}
