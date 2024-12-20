package com.proit.vista.caja;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
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
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.components.CalendarChoiceRenderer;
import com.proit.wicket.components.CustomTextFieldDouble;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class CajaChicaPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(CajaChicaPage.class.getName());
	
	private CajaChicaService cajaChicaService;
	
	private Calendar mesSeleccionado;
	
	public CajaChicaPage(PageParameters parameters) {
		super(parameters);
		
		cajaChicaService = new CajaChicaService();
		
		mesSeleccionado = Utils.firstMillisecondOfDay(Calendar.getInstance());
				
		setearDefaultModel();
		
		crearForm();
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
				
	}
	
	private void setearDefaultModel() {
		List<CajaChica> listaCajaChica = new ArrayList<CajaChica>();
		
		//Agrego un detalle por defecto
		CajaChica cajaChica = new CajaChica();
		listaCajaChica.add(cajaChica);
		
		this.setDefaultModel(Model.of(listaCajaChica));
	}
	
	@SuppressWarnings("unchecked")
	private void crearForm() {
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit() {
				List<CajaChica> listaCajaChica = obtenerListadoDetallesPreparado(((List<CajaChica>)CajaChicaPage.this.getDefaultModelObject()));
				if (listaCajaChica.isEmpty()){
					error("No se ingresaron detalles de caja chica con monto mayor a cero para el mes seleccionado.");
				}else if ( cajaChicaValidadaOK(listaCajaChica) ){
					boolean error = false;
					// Se crea cada detalle de caja chica por separado para el mes seleccionado. Una vez que los guardo, cagó.					
					for (CajaChica cajaChica : listaCajaChica){
						cajaChica.setMes(mesSeleccionado);
						
						//Si selecciono opcion: Otros o Todos
						if (cajaChica.getUsuarioSolicitante()!=null && cajaChica.getUsuarioSolicitante().getId() == -98) {
							cajaChica.setUsuarioSolicitante(null);
							cajaChica.setSolicitadoPorOtros(true);
							cajaChica.setSolicitadoPorTodos(false);
						} else if (cajaChica.getUsuarioSolicitante()!=null && cajaChica.getUsuarioSolicitante().getId() == -99) {
							cajaChica.setUsuarioSolicitante(null);
							cajaChica.setSolicitadoPorOtros(false);
							cajaChica.setSolicitadoPorTodos(true);
						} else {
							cajaChica.setSolicitadoPorOtros(false);
							cajaChica.setSolicitadoPorTodos(false);
						}
						
						try {
							cajaChicaService.createOrUpdate(cajaChica);
						} catch(Exception e) {
							log.error(e.getMessage(), e);
							error = true;
						}
					}
					String textoPorPantalla = "Los detalles ingresados fueron cargados correctamente.";
					String resultado = "OK";
					if (error){
						textoPorPantalla = "Hubo un problema al intentar cargar alguno de los detalles ingresados. Por favor, vuelva a intentarlo.";
						resultado = "ERROR";
					}
					PageParameters pageParameters = new PageParameters();
					pageParameters.add("Resultado", resultado);
					pageParameters.add("TextoPantalla", textoPorPantalla);
					setResponsePage(CajaChicaPage.class, pageParameters);
				}
			}

			/**
			 * Elimino los detalles de caja chica que fueron eliminados desde el boton eliminar de la grilla.
			 * Solo tomo los que tienen monto mayor a cero
			 * @param list
			 * @return
			 */
			private List<CajaChica> obtenerListadoDetallesPreparado(List<CajaChica> list) {
				List<CajaChica> nuevoListadoCajaChica = new ArrayList<CajaChica>();
				for (CajaChica cajaChica : list) {
					if (!cajaChica.isBorrado() && cajaChica.getMonto()!=0) {
						nuevoListadoCajaChica.add(cajaChica);
					}
				}
				return nuevoListadoCajaChica;
			}

			private boolean cajaChicaValidadaOK(List<CajaChica> listaCajaChica) {
				boolean cajaChicaValidadaOK = true;
//				if ( ! cajaChicaService.todoMontoMayorACero(listaCajaChica) ){
//					error("Todo monto ingresado debe ser mayor a cero.");
//					cajaChicaValidadaOK = false;
//				}
				if ( ! cajaChicaService.todaLineaTieneDetalle(listaCajaChica) ){
					error("Toda linea ingresada debe contener un detalle.");
					cajaChicaValidadaOK = false;
				}
				
				if ( ! cajaChicaService.todaLineaTieneFecha(listaCajaChica) ){
					error("Toda linea ingresada debe contener una fecha.");
					cajaChicaValidadaOK = false;
				}
				
				return cajaChicaValidadaOK;
			}			
		};
				
		final WebMarkupContainer container = new WebMarkupContainer("dataContainer");
		container.setOutputMarkupPlaceholderTag(true);
		container.add(new AjaxFallbackLink<CajaChica>("agregar") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				((List<CajaChica>)CajaChicaPage.this.getDefaultModelObject()).add(new CajaChica());
				target.add(container);
			}
		});
		
		DropDownChoice<Calendar> mesDropDownChoice = crearMesDropDownChoice(container);
		
		setearDefaultModel();
		
		addDetallesList(container);
		
		add(form);
		form.add(mesDropDownChoice);
		form.add(container);
		form.add(new FeedbackPanel("feedback"));
	}

	private DropDownChoice<Calendar> crearMesDropDownChoice(final WebMarkupContainer container) {
		
		List<Calendar> listaMeses = Utils.getListaMeses(-3,1);

		final IModel<Calendar> mesModel = Model.of(Calendar.getInstance()); 
		mesSeleccionado = listaMeses.get(3); //Por defecto el mes actual
		mesModel.setObject(mesSeleccionado);
		
		DropDownChoice<Calendar> mesDropDownChoice = new DropDownChoice<Calendar>("mes", mesModel, listaMeses, new CalendarChoiceRenderer("MM / yyyy"));
		mesDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba "onchange"
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				mesSeleccionado = (Calendar)mesModel.getObject();
				setearDefaultModel();
				target.add(container);
            }
        });
		return mesDropDownChoice;
	}
	
	private void addDetallesList(final WebMarkupContainer container) {
		IDataProvider<CajaChica> cajaChicaDataProvider = getCajaChicaProvider();
		
		DataView<CajaChica> dataView = new DataView<CajaChica>("listaCajaChica", cajaChicaDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<CajaChica> item) {
				AjaxDatePicker ajaxDatePicker = crearDatePicker(item);
				ajaxDatePicker.setOutputMarkupId(true);
				
				TextField<String> detalleTextField = new TextField<String>("detalle", new PropertyModel<String>(item.getModelObject(), "detalle"));
				
				IModel<Double> montoModel = new PropertyModel<Double>(item.getModelObject(), "monto");
				final CustomTextFieldDouble montoTextField = new CustomTextFieldDouble("monto", montoModel);
				montoTextField.setOutputMarkupId(true);
				montoTextField.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
					private static final long serialVersionUID = 1L;
					protected void onUpdate(AjaxRequestTarget target) {
						target.add(montoTextField);
		            }
		        });				
				
				DropDownChoice<Usuario> solicitanteDropDownChoice = crearSolicitanteDropDownChoice(item.getModelObject());
				
				item.add(ajaxDatePicker);
				item.add(detalleTextField);
				item.add(montoTextField);
				item.add(solicitanteDropDownChoice);
				
				//Utilizo lo siguiente para que todos los campos ingresados en la grilla de la pagina no sean eliminados al intentar agregar o quitar una fila.
				ajaxDatePicker.add(createNewAjaxFormComponentUpdatingBehavior());
				detalleTextField.add(createNewAjaxFormComponentUpdatingBehavior());
				montoTextField.add(createNewAjaxFormComponentUpdatingBehavior());
				solicitanteDropDownChoice.add(createNewAjaxFormComponentUpdatingBehavior());
				
				item.add(new AjaxFallbackLink<CajaChica>("eliminar", item.getModel()) {					
					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick(AjaxRequestTarget target) {
						CajaChica detalleSeleccionado = (CajaChica) getModelObject();
						detalleSeleccionado.setBorrado(true);
						target.add(container);
					}
				});
				
				if (item.getModelObject().isBorrado())	//se debe poner invisible
					item.setVisible(false);
			}

			/**
			 * Utilizado para que al eliminar o agregar una fila de la tabla los campos de las demas filas no se eliminen
			 * @return
			 */
			private AjaxFormComponentUpdatingBehavior createNewAjaxFormComponentUpdatingBehavior() {
				return new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
					private static final long serialVersionUID = 1L;
					protected void onUpdate(AjaxRequestTarget target) {
						
					}
				};
			}
		};
		
		container.add(dataView);
	}
	
	private IDataProvider<CajaChica> getCajaChicaProvider() {
		IDataProvider<CajaChica> cajaChicaDataProvider = new IDataProvider<CajaChica>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@SuppressWarnings("unchecked")
			@Override
			public Iterator<CajaChica> iterator(long first, long count) {
				return ((List<CajaChica>)CajaChicaPage.this.getDefaultModelObject()).iterator();
			}

			@SuppressWarnings("unchecked")
			@Override
			public long size() {
				return ((List<CajaChica>)CajaChicaPage.this.getDefaultModelObject()).size();
			}

			@Override
			public IModel<CajaChica> model(CajaChica cajaChica) {
				return new Model<CajaChica>(cajaChica);
			}
        	
        };
		return cajaChicaDataProvider;
	}
	
	private AjaxDatePicker crearDatePicker(Item<CajaChica> item) {
		IModel<Date> fechaModel = new PropertyModel<Date>(item.getModelObject(), "fechaAsDate");
		AjaxDatePicker ajaxDatePicker = new AjaxDatePicker("datepicker", fechaModel, "dd/MM/yyyy", new Options());
		//ajaxDatePicker.setRequired(true);
		return ajaxDatePicker;
	}
	
	private DropDownChoice<Usuario> crearSolicitanteDropDownChoice(CajaChica cajaChica) {
		UsuarioService usuarioService = new UsuarioService();
		List<Usuario> solicitantes = usuarioService.getUsuariosByRol(Rol.SOLICITANTE_PAGOS);
		agregarUsuarioFicticio(solicitantes, -98, "Otros");
		agregarUsuarioFicticio(solicitantes, -99, "Todos");

		IModel<Usuario> solicitanteModel = new PropertyModel<Usuario>(cajaChica, "usuarioSolicitante"); 
		DropDownChoice<Usuario> solicitanteDropDownChoice = new DropDownChoice<Usuario>("solicitante", solicitanteModel, solicitantes, new ChoiceRenderer<Usuario>("nombreCompleto"));
		//solicitanteDropDownChoice.setRequired(true);
		
		solicitanteDropDownChoice.setNullValid(true);
		return solicitanteDropDownChoice;
	}

	private void agregarUsuarioFicticio(List<Usuario> solicitantes, int id, String nombre) {
		Usuario usuario = new Usuario();
		usuario.setId(id);
		usuario.setNombreORazonSocial(nombre);
		usuario.setApellido("");
		usuario.setEmail("");
		solicitantes.add(usuario);
	}	

}
