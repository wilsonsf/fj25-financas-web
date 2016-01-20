package br.com.caelum.financas.mb;

import java.math.BigDecimal;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.financas.dao.MovimentacaoDao;
import br.com.caelum.financas.modelo.Movimentacao;
import br.com.caelum.financas.modelo.TipoMovimentacao;

@Named
@RequestScoped
public class MovimentacoesPorValorETipoBean {
	
	@Inject
	MovimentacaoDao dao;
	
	private List<Movimentacao> movimentacoes;
	private BigDecimal valor;
	private TipoMovimentacao tipoMovimentacao;

	public void lista() {
		this.movimentacoes = dao.buscaPor(this.valor, this.tipoMovimentacao);
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public TipoMovimentacao getTipoMovimentacao() {
		return tipoMovimentacao;
	}

	public void setTipoMovimentacao(TipoMovimentacao tipoMovimentacao) {
		this.tipoMovimentacao = tipoMovimentacao;
	}

	public List<Movimentacao> getMovimentacoes() {
		return movimentacoes;
	}

}
