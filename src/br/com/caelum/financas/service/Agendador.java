package br.com.caelum.financas.service;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.AccessTimeout;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

@Startup
@Singleton
@AccessTimeout(unit = TimeUnit.SECONDS, value = 5)
public class Agendador {

	private static int totalCriado;

	@Resource
	private TimerService timerService;
	
	@PostConstruct
	void posConstrucao() {
		System.out.println("criando agendador");
		totalCriado++;
	}
	
	@PreDestroy
	void preDestruicao() {
		System.out.println("destruindo agendador");
	}
	
	public void executa() {
		System.out.printf("%d instancias criadas %n", totalCriado);

		// simulando demora de 4s na execucao
		try {
			System.out.printf("Executando %s %n", this);
			Thread.sleep(4000);
		} catch (InterruptedException e) {
		}
	}
	
	public void agenda(String expressaoMinutos, String expressaoSegundos) {
		ScheduleExpression expression = new ScheduleExpression();
		expression.hour("*");
		expression.minute(expressaoMinutos);
		expression.second(expressaoSegundos);
		
		TimerConfig config = new TimerConfig();
		config.setInfo(expression.toString());
		config.setPersistent(false);
		
		this.timerService.createCalendarTimer(expression, config);
		
		System.out.println("Agendamento: " + expression);
	}
	
	@Timeout
	public void verificaNovasContas(Timer timer) {
		System.out.println(timer.getInfo());
		// Realizar acesso ao BD com JPA e verifica as contas
	}
	
	/*
	@Schedule(hour="10", minute="0/15", second="0", persistent=false)
	public void enviaEmailComUltimasMovimentacoes() {
		System.out.println("enviando email agora! : " + new SimpleDateFormat().format(new Date()));
	}
	
	@Schedule(dayOfWeek="Tue",hour="15", minute="0", second="0", persistent=false)
	public void enviaEmailSemanal() {
		System.out.println("enviando email semanal");
	}
	 */
}
