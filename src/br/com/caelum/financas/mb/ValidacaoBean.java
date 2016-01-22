package br.com.caelum.financas.mb;

import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import br.com.caelum.financas.modelo.Conta;

@Named
@RequestScoped
public class ValidacaoBean {

	@Inject
	private Validator validator;
	
	private Conta conta = new Conta();

	public void validar() {
		System.out.println("Validando a conta");
		Set<ConstraintViolation<Conta>> erros = validator.validate(this.conta);
		for (ConstraintViolation<Conta> erro : erros) {
			geraMensagemJsf(erro);
		}
	}
	
	public Conta getConta() {
		return conta;
	}

	/**
	 * Esse metodo disponibiliza uma mensagem para a tela JSF.
	 */
	private void geraMensagemJsf(ConstraintViolation<Conta> erro) {
		FacesContext.getCurrentInstance().addMessage("", new FacesMessage(erro.getPropertyPath().toString() + "  " + erro.getMessage()));
	}
	

	
}
