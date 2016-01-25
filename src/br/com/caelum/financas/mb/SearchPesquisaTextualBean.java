package br.com.caelum.financas.mb;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.financas.dao.MovimentacaoDao;
import br.com.caelum.financas.modelo.Movimentacao;

@Named
@RequestScoped
public class SearchPesquisaTextualBean {

	@Inject
	private MovimentacaoDao da;

	private String descricao;
	private List<Movimentacao> movimentacoes;


	public void pesquisar() {
		this.movimentacoes = this.da.buscaPorDescricao(this.descricao);
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Movimentacao> getMovimentacoes() {
		return this.movimentacoes;
	}

}
