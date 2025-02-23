package br.com.uoutec.community.ediacaran.sales.entity;

public class ProductMetadataSearch {

	private String name;
	
	private Integer page;
	
	private Integer resultPerPage;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getResultPerPage() {
		return resultPerPage;
	}

	public void setResultPerPage(Integer resultPerPage) {
		this.resultPerPage = resultPerPage;
	}

}
