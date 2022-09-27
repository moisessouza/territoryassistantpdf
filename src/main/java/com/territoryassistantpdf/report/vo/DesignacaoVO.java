package com.territoryassistantpdf.report.vo;

public class DesignacaoVO {

	private String tipo;
	private String nomeDirigente;
	private String dataInicio;
	private String dataFim;
	
	public DesignacaoVO () {}
	
	public DesignacaoVO( String tipo, String nomeDirigente, String dataInicio, String dataFim){
		this.tipo=tipo;
		this.nomeDirigente = nomeDirigente;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNomeDirigente() {
		return nomeDirigente;
	}

	public void setNomeDirigente(String nomeDirigente) {
		this.nomeDirigente = nomeDirigente;
	}

	public String getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}

	public String getDataFim() {
		return dataFim;
	}

	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}
		
}
