package com.proit.servicios;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.mail.MessagingException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.util.string.Strings;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proit.modelo.Rol;
import com.proit.modelo.Usuario;
import com.proit.modelo.UsuarioRol;
import com.proit.utils.Constantes;
import com.proit.utils.EmailSender;
import com.proit.wicket.FacturarOnLineAuthenticatedWebApplication;

public class UsuarioService extends GenericService<Usuario> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Logger logger;

	public UsuarioService() {
		super(Usuario.class);
		logger = LoggerFactory.getLogger(UsuarioService.class);
	}
	
	private Criteria definirCriteria(String nombre, String apellido, String email, String clave) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria usuarioCriteria = session.createCriteria(Usuario.class);
		if (!Strings.isEmpty(nombre)) {
			usuarioCriteria.add(Restrictions.ilike("nombreORazonSocial", "%" + nombre + "%"));
		}
		if (!Strings.isEmpty(apellido)) {
			usuarioCriteria.add(Restrictions.ilike("apellido", "%" + apellido + "%"));
		}
		if (!Strings.isEmpty(email)) {
			usuarioCriteria.add(Restrictions.ilike("email", "%" + email + "%"));
		}
		if (!Strings.isEmpty(clave)) {
			usuarioCriteria.add(Restrictions.eq("clave", clave));
		}
		usuarioCriteria.add(Restrictions.eq("borrado", false));
		return usuarioCriteria;
	}
	
	/**
	 * This method gets some info of {@link Usuario}s from database.
	 * @param primerResultado First result to obtain.
	 * @param cantidadResultados Total {@link Usuario}s to obtain.
	 * @return Returns name, surname, email and Rol of {@link Usuario}s from database.
	 */
	@SuppressWarnings("unchecked")
	public Iterator<Usuario> getUsuarios(long primerResultado, long cantidadResultados, String nombre, String apellido, String email, boolean isUsuarioLogueadoRolDesarrollador) {
		Criteria usuarioCriteria = definirCriteria(nombre, apellido, email, null);//TODO NO Ahora: usar isUsuarioLogueadoRolDesarrollador para que no devuelva usuarios desarrolladores
		usuarioCriteria.addOrder(Order.asc("nombreORazonSocial"));
		usuarioCriteria.setFirstResult((int) primerResultado);
		usuarioCriteria.setMaxResults((int) cantidadResultados);
		return usuarioCriteria.list().iterator();
	}
	
	public long getUsuariosSize(String nombre, String apellido, String email, boolean isUsuarioLogueadoRolDesarrollador) {
		Criteria usuarioCriteria = definirCriteria(nombre, apellido, email, null);//TODO NO Ahora: usar isUsuarioLogueadoRolDesarrollador para que no devuelva usuarios desarrolladores
		usuarioCriteria.setProjection(Projections.rowCount());
		return (Long)usuarioCriteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> getUsuariosByRol(Rol rol) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria usuarioCriteria = session.createCriteria(Usuario.class);
		usuarioCriteria.createAlias("listadoRoles", "listadoRoles");
		usuarioCriteria.add(Restrictions.eq("borrado", false));
		usuarioCriteria.add(Restrictions.eq("listadoRoles.id", rol.getId()));
		return usuarioCriteria.list();
	}
	
	/**
	 * Verifica si el email ya ha sido utilizado previamente en el sistema
	 * (sin importar si el usuario fue borrado o no previamente)
	 * @param email
	 * @param idUsuarioActual 
	 * @return
	 */
	public boolean existsByEmail(String email, int idUsuarioActual){	
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria usuarioCriteria = session.createCriteria(Usuario.class);
		usuarioCriteria.add(Restrictions.eq("email", email));
		if (idUsuarioActual!=0) {
			usuarioCriteria.add(Restrictions.ne("id", idUsuarioActual));
		}
		usuarioCriteria.setProjection(Projections.rowCount());
		return( (Long) usuarioCriteria.uniqueResult() ) > 0;
	}
	
	public Usuario getUsuarioByEmail(String email) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria usuarioCriteria = session.createCriteria(Usuario.class);
		usuarioCriteria.add(Restrictions.eq("email", email));
		usuarioCriteria.add(Restrictions.eq("borrado", false));
		return (Usuario)usuarioCriteria.uniqueResult();
	}
	
	public Usuario getUsuario(String email, String password) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();	
		String hashedPassword = DigestUtils.md5Hex(password);
		Criteria usuarioCriteria = session.createCriteria(Usuario.class);
		usuarioCriteria.add(Restrictions.eq("email", email));
		usuarioCriteria.add(Restrictions.eq("clave", hashedPassword));
		usuarioCriteria.add(Restrictions.eq("borrado", false));
		return (Usuario)usuarioCriteria.uniqueResult();
	}
	
	public boolean esClaveCorrecta(Usuario usuario, String clave){
		return getUsuario(usuario.getEmail(), clave) != null;
	}
	
	public void modificarClave(Usuario usuario, String claveNueva, RuntimeConfigurationType runtimeConfigurationType) throws MessagingException {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String hashedPassword = DigestUtils.md5Hex(claveNueva);
		usuario = (Usuario) get(usuario.getId()); 
		usuario.setClave(hashedPassword);
		session.update(usuario);
		sendEmailWithNewPass(usuario.getEmail(), claveNueva, runtimeConfigurationType);
	}
	
	public String resetPassword(String email, RuntimeConfigurationType runtimeConfigurationType) throws MessagingException {
		Usuario usuario = getUsuarioByEmail(email);
		if (usuario == null) {
			return null;
		}
		String nuevaClave = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
		modificarClave(usuario, nuevaClave, runtimeConfigurationType);
		return nuevaClave;
	}
	
	/*public Usuario prepararUsuarioWebConClaveAutogenerada(String email) throws MessagingException {
		Usuario usuario = new Usuario();
		usuario.setEmail(email);
		String claveAutogenerada = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
		String hashedPassword = DigestUtils.md5Hex(claveAutogenerada);
		usuario.setClave(hashedPassword);
		usuario.setRol(Rol.USUARIO_WEB);
		generarYAgregarTokenActivacion(usuario);
		sendEmailWithTokenAndPass(usuario.getEmail(), claveAutogenerada, usuario.getActivacion());
		return usuario;
	}*/
	
	/**
	 * El token es generado con el email + fecha actual (formato yyyyMMddHHmmss). Todo eso hasheado a MD5.
	 * @param usuario
	 */
	public void generarYAgregarTokenActivacion(Usuario usuario) {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String tokenActivacion = usuario.getEmail() + "-" +dateFormat.format(new Date());
		String hashedTokenActivacion = DigestUtils.md5Hex(tokenActivacion);
		usuario.setActivacion(hashedTokenActivacion);
	}
	
	public boolean activarUsuario(String token) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria usuarioCriteria = session.createCriteria(Usuario.class);
		usuarioCriteria.add(Restrictions.eq("activacion", token));
		usuarioCriteria.add(Restrictions.eq("borrado", false));
		Usuario usuario = (Usuario) usuarioCriteria.uniqueResult();
		if (usuario != null){
			usuario.setActivacion(Constantes.USUARIO_ACTIVADO);
			session.update(usuario);
			return true;
		}
		return false;
	}
	
	private void sendEmailWithNewPass(String email, String nuevaClave, RuntimeConfigurationType runtimeConfigurationType) throws MessagingException {
		if ( EmailSender.sendEmail(email, "Trebol4 SRL - Clave generada", "Estimado, su nueva clave de trebol4sistema.com.ar es: " + nuevaClave, EmailSender.TO, runtimeConfigurationType) ) {
			logger.info("Correo electrónico enviado");
		}
	}
	
	 //TODO PROD: testear q se envie el mail y q se cree el usuario en todos los lugares donde se llama este metodo. Tambien el link de validacion
	public void sendEmailWithTokenAndPass(String email, String nuevaClave, String tokenActivacion, RuntimeConfigurationType runtimeConfigurationType) throws MessagingException { 
		String cuerpo = "Para activar su cuenta en Trebol4 SRL debe acceder al siguiente link:\n"
				+ Constantes.URL_ACTIVACION + tokenActivacion + "\n\n"
				+ "Para acceder a Trebol4 SRL, utilice los siguientes datos:\n"
				+ "Usuario: " + email + "\n"
				+ "Clave: " + nuevaClave + "\n";
		logger.info(cuerpo);
		if ( EmailSender.sendEmail(email, "Trebol4 SRL - Activa tu cuenta", cuerpo, EmailSender.TO, runtimeConfigurationType) ) {
			logger.info("Correo electrónico enviado");
		}
	}
		
	@Override
	public void delete(int id) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		
		//primero elimino el usuario
		Usuario usuario = (Usuario) get(id);
		usuario.setBorrado(true);
		session.update(usuario);
		
		//luego elimino las entradas many-to-many
		for (Rol rol : usuario.getListadoRoles()) {
			UsuarioRol usuarioRol = new UsuarioRol();
			usuarioRol.setUsuario(usuario);
			usuarioRol.setRol(rol);
			usuarioRol.setBorrado(true);
			session.update(usuarioRol);
		}
	}
	
}
