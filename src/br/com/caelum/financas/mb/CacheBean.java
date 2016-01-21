package br.com.caelum.financas.mb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import br.com.caelum.financas.modelo.Conta;

@Named
@RequestScoped
public class CacheBean {
	
	@Inject
	private EntityManager manager;
	
	private Integer id;
	private Conta conta;
	
	public void pesquisar() {
		this.conta = this.manager.find(Conta.class, id);
		this.conta = this.manager.find(Conta.class, id);
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Conta getConta() {
		return conta;
	}
}