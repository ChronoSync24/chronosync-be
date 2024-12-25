package com.sinergy.chronosync.dto.request;

public class BasePaginationRequest {

	private int page = 0; // Default page number
	private int pageSize = 10; // Default page size

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}