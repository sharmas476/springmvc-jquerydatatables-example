package sample.dt.repository.querydsl;

import java.util.Map;

import com.mysema.query.types.expr.BooleanExpression;

import sample.dt.webcomponents.search.DtSearchColumn;

public interface BooleanExpressionTemplate {

	BooleanExpression cretePredicate(BooleanExpression lastPredicate, DtSearchColumn column, Map<String, DtSearchColumn> allSearchColumns);

}
