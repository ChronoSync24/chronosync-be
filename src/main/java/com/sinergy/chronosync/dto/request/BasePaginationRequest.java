package com.sinergy.chronosync.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BasePaginationRequest {

	private int page = 0;
	private int pageSize = 10;
}