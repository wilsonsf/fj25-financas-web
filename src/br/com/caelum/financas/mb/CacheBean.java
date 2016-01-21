package br.com.caelum.financas.mb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.financas.dao.ContaDao;
import br.com.caelum.financas.modelo.Conta;

@Named
@RequestScoped
public class CacheBean {
	
	@Inject
	private ContaDao dao; 
	
	private Integer id;
	private Conta conta;
	
	public void pesquisar() {
		this.conta = this.dao.busca(id);
		this.conta = this.dao.busca(id);
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