package br.com.caelum.financas.mb;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.financas.dao.ContaDao;
import br.com.caelum.financas.modelo.Conta;

@Named
@ViewScoped
public class ContasBean implements Serializable {
    
    private static final long serialVersionUID = 1L;

	private Conta conta = new Conta();
	private List<Conta> contas;
	
	@Inject
	private ContaDao contaDao;

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public void grava() {
		contaDao.adiciona(this.conta);
		this.contas = contaDao.lista();
		
		limpaFormularioDoJSF();
	}

	public List<Conta> getContas() {
		if (this.contas == null) {
			this.contas = contaDao.lista();
		}
		
		return contas;
	}

	public void remove() {
		contaDao.remove(this.conta);
		this.contas = contaDao.lista();
		
		limpaFormularioDoJSF();
	}

	/**
	 * Esse metodo apenas limpa o formulario da forma com que o JSF espera.
	 * Invoque-o no momento em que precisar do formulario vazio.
	 */
	private void limpaFormularioDoJSF() {
		this.conta = new Conta();
	}
}
