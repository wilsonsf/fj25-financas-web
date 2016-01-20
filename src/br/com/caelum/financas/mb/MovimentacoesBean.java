package br.com.caelum.financas.mb;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.financas.dao.CategoriaDao;
import br.com.caelum.financas.dao.ContaDao;
import br.com.caelum.financas.dao.MovimentacaoDao;
import br.com.caelum.financas.modelo.Categoria;
import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Movimentacao;
import br.com.caelum.financas.modelo.TipoMovimentacao;

@Named @ViewScoped
public class MovimentacoesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject 
	private ContaDao contaDao;
	@Inject 
	private CategoriaDao categoriaDao;
	@Inject
	private MovimentacaoDao movimentacaoDao;
	
	private List<Movimentacao> movimentacoes;
	private Movimentacao movimentacao = new Movimentacao();
	private Integer contaId;
	private Integer categoriaId;
	private List<Categoria> categorias;
	
	public void grava() {
		Conta contaVinculada = contaDao.busca(this.contaId);
		this.movimentacao.setConta(contaVinculada);
		
		this.movimentacaoDao.adiciona(this.movimentacao);
		
		this.movimentacoes = this.movimentacaoDao.lista();
		
		limpaFormularioDoJSF();
	}
	

	public void remove() {
		this.movimentacaoDao.remove(this.movimentacao);
		
		this.movimentacoes = this.movimentacaoDao.lista();
		
		limpaFormularioDoJSF();
	}

	public void adicionaCategoria() {
		if (this.categoriaId != null && this.categoriaId > 0) {
			Categoria categoria = categoriaDao.busca(this.categoriaId);
			this.movimentacao.getCategorias().add(categoria);
		}
	}
	
	public List<Movimentacao> getMovimentacoes() {
		if (this.movimentacoes == null) {
			this.movimentacoes = this.movimentacaoDao.lista();
		}
		
		return movimentacoes;
	}
	
	public Movimentacao getMovimentacao() {
		if(movimentacao.getData()==null) {
			movimentacao.setData(Calendar.getInstance());
		}
		return movimentacao;
	}

	public void setMovimentacao(Movimentacao movimentacao) {
		this.movimentacao = movimentacao;
	}

	public Integer getContaId() {
		return contaId;
	}

	public void setContaId(Integer contaId) {
		this.contaId = contaId;
	}
	

	public Integer getCategoriaId() {
		return categoriaId;
	}

	public void setCategoriaId(Integer categoriaId) {
		this.categoriaId = categoriaId;
	}

	/**
	 * Esse metodo apenas limpa o formulario da forma com que o JSF espera.
	 * Invoque-o no momento manager que precisar do formulario vazio.
	 */
	private void limpaFormularioDoJSF() {
		this.movimentacao = new Movimentacao();
	}

	public TipoMovimentacao[] getTiposDeMovimentacao() {
		return TipoMovimentacao.values();
	}


	public List<Categoria> getCategorias() {
		if (this.categorias == null)
			this.categorias = categoriaDao.lista();
			
		return categorias;
	}
}
