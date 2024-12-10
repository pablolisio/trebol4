package com.proit.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Esta clase representa una entidad bancaria 
 */
@Entity
public class Config extends EntidadGenerica implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="only_desarrollador_can_use_app", nullable = false)
	private boolean onlyDesarrolladorCanUseApp;

	public boolean isOnlyDesarrolladorCanUseApp() {
		return onlyDesarrolladorCanUseApp;
	}

	public void setOnlyDesarrolladorCanUseApp(boolean onlyDesarrolladorCanUseApp) {
		this.onlyDesarrolladorCanUseApp = onlyDesarrolladorCanUseApp;
	}	
	
}

