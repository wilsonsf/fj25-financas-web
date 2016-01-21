package br.com.caelum.financas.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CompoundSelection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.caelum.financas.exception.ValorInvalidoException;
import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Movimentacao;
import br.com.caelum.financas.modelo.TipoMovimentacao;
import br.com.caelum.financas.modelo.ValorPorMesEAno;

@Stateless
public class MovimentacaoDao implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	EntityManager manager;

	public void adiciona(Movimentacao movimentacao) {
		if (movimentacao.getValor().compareTo(BigDecimal.ZERO) < 0)
			throw new ValorInvalidoException("Movimentação Negativa");
		
		this.manager.joinTransaction();
		this.manager.persist(movimentacao);
	}

	public Movimentacao busca(Integer id) {
		return this.manager.find(Movimentacao.class, id);
	}

	public List<Movimentacao> lista() {
		return this.manager.createQuery("select m from Movimentacao m", Movimentacao.class).getResultList();
	}
	
	public List<Movimentacao> listaComCategorias() {
		return this.manager.createQuery("select distinct m from Movimentacao m "
									+	"left join fetch m.categorias "
					, Movimentacao.class).getResultList();
	}
	
	public List<Movimentacao> buscaPor(Conta conta) {
		String jpql = "select m from Movimentacao m "
					+ "where m.conta = :conta "
					+ "order by m.valor desc";
		
		TypedQuery<Movimentacao> query = manager.createQuery(jpql, Movimentacao.class);
		query.setParameter("conta", conta);
		
		return query.getResultList();
	}

	public List<Movimentacao> buscaPor(BigDecimal valor, TipoMovimentacao tipo) {
		String jpql = "select m from Movimentacao m "
					+ "where m.valor <= :valor "
					+ "and m.tipoMovimentacao = :tipo ";
		
		TypedQuery<Movimentacao> query = this.manager.createQuery(jpql,Movimentacao.class);
		query.setParameter("valor", valor);
		query.setParameter("tipo", tipo);
		query.setHint("org.hibernate.cacheable", true);
		
		return query.getResultList();
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
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ValorPorMesEAno> criteria = builder.createQuery(ValorPorMesEAno.class);
		Root<Movimentacao> root = criteria.from(Movimentacao.class);

		Path<Calendar> movData = root.<Calendar>get("data");
		Expression<Calendar> movMonth = builder.function("month", Calendar.class, movData);
		Expression<Calendar> movYear = builder.function("year", Calendar.class, movData);

		CompoundSelection<ValorPorMesEAno> selection = builder.construct(ValorPorMesEAno.class,
				builder.sum(root.get("valor").as(BigDecimal.class)),
				movMonth,
				movYear);

		criteria = criteria.select(selection).groupBy(movYear,movMonth);

		Predicate predicate = builder.conjunction();
		if (conta != null && conta.getId() != null) {
			predicate = builder.and(predicate, 
					builder.equal(root.<Conta>get("conta"), conta));
		}
		if (tipo != null) {
			predicate = builder.and(predicate, 
					builder.equal(root.<TipoMovimentacao>get("tipoMovimentacao"), tipo));
		}

		criteria.where(predicate);
		return manager.createQuery(criteria).getResultList();
	}
	
	public List<Movimentacao> pesquisa(Conta conta, TipoMovimentacao tipo, Integer mes) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Movimentacao> criteria = builder.createQuery(Movimentacao.class);
		Root<Movimentacao> root = criteria.from(Movimentacao.class);
		root.fetch("conta");

		Predicate predicate = builder.conjunction();
		if (conta != null && conta.getId() != null) {
			predicate =
				builder.and(predicate,
					builder.equal(root.<Conta>get("conta"), conta));
		}

		if (mes != null && mes > 0 && mes <= 12) {
			Expression<Integer> expression = 
				builder.function("month", Integer.class, root.<Calendar>get("data"));
			predicate = builder.and(predicate, builder.equal(expression, mes));
		}

		if (tipo != null) {
			predicate = builder.and(predicate, 
				builder.equal(root.<TipoMovimentacao>get("tipoMovimentacao"), tipo));
		}

		criteria.where(predicate);
		return manager.createQuery(criteria).getResultList();
	}

	public void remove(Movimentacao movimentacao) {
		this.manager.joinTransaction();
		Movimentacao movimentacaoParaRemover = this.manager.find(Movimentacao.class, movimentacao.getId());
		this.manager.remove(movimentacaoParaRemover);
	}

}
