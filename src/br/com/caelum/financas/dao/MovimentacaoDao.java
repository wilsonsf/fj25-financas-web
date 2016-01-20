package br.com.caelum.financas.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.caelum.financas.exception.ValorInvalidoException;
import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Movimentacao;
import br.com.caelum.financas.modelo.TipoMovimentacao;
import br.com.caelum.financas.modelo.ValorPorMesEAno;

@Stateless
public class MovimentacaoDao {

	@PersistenceContext
	EntityManager manager;

	public void adiciona(Movimentacao movimentacao) {
		this.manager.persist(movimentacao);
		
		if (movimentacao.getValor().compareTo(BigDecimal.ZERO) < 0)
			throw new ValorInvalidoException("Movimentação Negativa");
	}

	public Movimentacao busca(Integer id) {
		return this.manager.find(Movimentacao.class, id);
	}

	public List<Movimentacao> lista() {
		return this.manager.createQuery("select m from Movimentacao m", Movimentacao.class).getResultList();
	}
	
	public List<Movimentacao> buscaPor(Conta conta) {
		String jpql = "select m from Movimentacao m "
					+ "where m.conta = :conta "
					+ "order by m.valor desc";
		
		Query query = manager.createQuery(jpql);
		query.setParameter("conta", conta);
		
		@SuppressWarnings("unchecked")
		List<Movimentacao> movimentacoes = query.getResultList();
		
		return movimentacoes;
	}

	public List<Movimentacao> buscaPor(BigDecimal valor, TipoMovimentacao tipo) {
		String jpql = "select m from Movimentacao m "
					+ "where m.valor <= :valor "
					+ (tipo != null ? "and m.tipoMovimentacao = :tipo " : "");
		
		Query query = manager.createQuery(jpql);
		query.setParameter("valor", valor);
		
		if (tipo != null)
			query.setParameter("tipo", tipo);
		
		@SuppressWarnings("unchecked")
		List<Movimentacao> movimentacoes = query.getResultList();
		
		return movimentacoes;
	}
	
	public BigDecimal calculaTotalMovimentado(Conta conta, TipoMovimentacao tipo) {
		String jpql = "select sum(m.valor) from Movimentacao m "
					+ "where m.conta = :conta "
					+ (tipo != null ? "and m.tipoMovimentacao = :tipo " : "");
		
		TypedQuery<BigDecimal> query = manager.createQuery(jpql,BigDecimal.class);
		query.setParameter("conta", conta);
		
		if (tipo != null)
			query.setParameter("tipo", tipo);
		
		return query.getSingleResult();
	}
	
	public List<Movimentacao> buscaTodasMovimentacoesDo(String titular) {
		String jpql = "select m from Movimentacao m "
					+ "where m.conta.titular like :titular";
		
		TypedQuery<Movimentacao> query = manager.createQuery(jpql, Movimentacao.class);
		query.setParameter("titular", "%" + titular + "%");
		
		return query.getResultList();
	}
	
	public List<ValorPorMesEAno> listaMesesComMovimentacoes(Conta conta, TipoMovimentacao tipo) {
		String classFQN = ValorPorMesEAno.class.getName();
		
		String jpql = "select new " + classFQN + "(sum(m.valor), month(m.data), year(m.data)) "
					+ "from Movimentacao m "
					+ "where m.conta = :conta and m.tipoMovimentacao = :tipo "	
					+ "group by year(m.data), month(m.data) "
					+ "order by sum(m.valor) desc ";
		
		TypedQuery<ValorPorMesEAno> query = manager.createQuery(jpql, ValorPorMesEAno.class);
		query.setParameter("conta", conta);
		query.setParameter("tipo", tipo);
		
		return query.getResultList();
	}
	
	public void remove(Movimentacao movimentacao) {
		Movimentacao movimentacaoParaRemover = this.manager.find(Movimentacao.class, movimentacao.getId());
		this.manager.remove(movimentacaoParaRemover);
	}

}
