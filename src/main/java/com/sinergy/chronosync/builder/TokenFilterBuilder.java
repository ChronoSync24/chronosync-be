package com.sinergy.chronosync.builder;

import com.sinergy.chronosync.model.Token;
import com.sinergy.chronosync.model.user.User;
import jakarta.persistence.criteria.Predicate;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Builder
public class TokenFilterBuilder extends BaseFilterBuilder<Token> {

	private static final String JWT_STRING = "jwt_string";
	private static final String USER_ID = "user_id";

	private User user;
	private String jwtString;

	@Override
	public Specification<Token> toSpecification() {
		return ((root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			addEqualPredicate(predicates, root, criteriaBuilder, JWT_STRING, jwtString);
			addEqualPredicate(predicates, root, criteriaBuilder, "user", user);

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		});
	}
}
