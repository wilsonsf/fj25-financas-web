package br.com.caelum.financas.mb;

import java.util.List;

import br.com.caelum.financas.dao.MovimentacaoDao;
import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Movimentacao;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class MovimentacoesDaContaBean {

	@Inject
	MovimentacaoDao dao;
	
	private List<Movimentacao> movimentacoes;
	private Conta conta = new Conta();
	
	public void lista() {
		this.movimentacoes = this.dao.buscaPor(conta);
	}

	public List<Movimentacao> getMovimentacoes() {
		return movimentacoes;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}
}
