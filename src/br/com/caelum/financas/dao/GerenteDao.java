package br.com.caelum.financas.dao;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.caelum.financas.modelo.Gerente;

public class GerenteDao implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;

	public void adiciona(Gerente g) {
		manager.joinTransaction();
		manager.persist(g);
	}
	
	public void busca(Integer id) {
		manager.find(Gerente.class, id);
	}

}
