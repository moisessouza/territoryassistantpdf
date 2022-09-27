package com.territoryassistantpdf.report.vo;

public class TerritorioVO {

	private Long id;
	private String cod;
	
	public TerritorioVO (){}
	
	public TerritorioVO(Long id, String cod) {
		this.id = id;
		this.cod = cod;
	}
	
	public String getCod() {
		return cod;
	}
	
	public void setCod(String cod) {
		this.cod = cod;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
}
