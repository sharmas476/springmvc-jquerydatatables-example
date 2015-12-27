package sample.dt.repository.querydsl;

import java.util.List;
import java.util.Map;
//import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;

import sample.dt.webcomponents.DtPageable;
import sample.dt.webcomponents.search.DtSearchColumn;

@Component
public class QueryDslExpressionHelper {

	public Predicate express(BooleanExpressionTemplate helper, DtPageable pageable) {

		List<DtSearchColumn> columns = pageable.getColumns();
		final Map<String, DtSearchColumn> colMap = columns.stream().collect(Collectors.toMap(e -> e.getName(), e -> e));

		BooleanExpression predicate = null;
		for (DtSearchColumn column : columns) {
			if(StringUtils.isNotEmpty(column.getCondition().getValue())){
				predicate = helper.cretePredicate(predicate, column, colMap);
			}
		}

		return predicate;


	}

}
