package com.sinergy.chronosync.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Model class for base pagination request.
 */
@Setter
@Getter
public class BasePaginationRequest {

	private int page = 0;
	private int pageSize = 10;
}