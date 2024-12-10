package com.proit.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.proit.modelo.Rol;
import com.proit.modelo.Usuario;
import com.proit.utils.Constantes;

public class ResetData {
	
	private static Rol rol1;
	private static Rol rol2;
	private static Rol rol3;
	private static Rol rol4;

	private static Usuario usuario1;
	private static Usuario usuario2;
	private static Usuario usuario3;
	private static Usuario usuario4;
	private static Usuario usuario5;
	private static Usuario usuario6;
	
	public static void initScript(boolean execute) {
		if (!execute) {
			return;
		}
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		
		try {
			loadRoles(session);
			loadUsers(session);
            // Committing the change in the database.
            session.flush();
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            // Rolling back the changes to make the data consistent in case of any failure
            // in between multiple database write operations.
            tx.rollback();
        } finally{
            if(session != null) {
                session.close();
            }
        }
	}
	
	private static void loadRoles(Session session) {
		rol1 = Rol.ADMINISTRADOR;
		session.save(rol1);
		
		rol2 = Rol.ADMINISTRADOR;
		session.save(rol2);
		
		rol3 = Rol.SOLO_LECTURA;
		session.save(rol3);
		
		rol4 = Rol.DESARROLLADOR;
		session.save(rol4);
	}
	
	private static void loadUsers(Session session) {
		usuario1 = new Usuario();
		usuario1.setNombreORazonSocial("Pablo");
		usuario1.setApellido("Lacioka");
		usuario1.setClave("202cb962ac59075b964b07152d234b70");
		usuario1.setEmail("pablo@facturaronline.com.ar");
		//usuario1.setRol(rol4);
		usuario1.setTelefono("4416-3626");
		usuario1.setBorrado(false);
		usuario1.setActivacion(Constantes.USUARIO_ACTIVADO);
		session.save(usuario1);
		
		usuario2 = new Usuario();
		usuario2.setNombreORazonSocial("Adrian");
		usuario2.setApellido("Fernandez");
		usuario2.setClave("202cb962ac59075b964b07152d234b70");
		usuario2.setEmail("ariel@facturaronline.com.ar");
		//usuario2.setRol(rol4);
		usuario2.setTelefono("4444-8888");
		usuario2.setBorrado(false);
		usuario2.setActivacion(Constantes.USUARIO_ACTIVADO);
		session.save(usuario2);
		
		usuario3 = new Usuario();
		usuario3.setNombreORazonSocial("Juan");
		usuario3.setApellido("Garcia");
		usuario3.setClave("202cb962ac59075b964b07152d234b70");
		usuario3.setEmail("lucho@facturaronline.com.ar");
		//usuario3.setRol(rol1);
		usuario3.setTelefono("4444-1111");
		usuario3.setBorrado(false);
		usuario3.setActivacion(Constantes.USUARIO_ACTIVADO);
		session.save(usuario3);
		
		usuario4 = new Usuario();
		usuario4.setNombreORazonSocial("Carlos");
		usuario4.setApellido("Jimenez");
		usuario4.setClave("202cb962ac59075b964b07152d234b70");
		usuario4.setEmail("carlos@facturaronline.com.ar");
		//usuario4.setRol(rol3);
		usuario4.setTelefono("4444-2222");
		usuario4.setBorrado(false);
		usuario4.setActivacion(Constantes.USUARIO_ACTIVADO);
		session.save(usuario4);
		
		usuario5 = new Usuario();
		usuario5.setNombreORazonSocial("Guillermo");
		usuario5.setApellido("Sanchez");
		usuario5.setClave("202cb962ac59075b964b07152d234b70");
		usuario5.setEmail("guillermo@facturaronline.com.ar");
		//usuario5.setRol(rol2);
		usuario5.setTelefono("4444-3333");
		usuario5.setBorrado(false);
		usuario5.setActivacion(Constantes.USUARIO_ACTIVADO);
		session.save(usuario5);
		
		usuario6 = new Usuario();
		usuario6.setNombreORazonSocial("Test");
		usuario6.setApellido("Test");
		usuario6.setClave("098f6bcd4621d373cade4e832627b4f6");	//pass: test
		usuario6.setEmail("test@facturaronline.com.ar");
		//usuario6.setRol(rol1);
		usuario6.setTelefono("4216-3698");
		usuario6.setBorrado(false);
		usuario6.setActivacion(Constantes.USUARIO_ACTIVADO);
		session.save(usuario6);
	}

}
