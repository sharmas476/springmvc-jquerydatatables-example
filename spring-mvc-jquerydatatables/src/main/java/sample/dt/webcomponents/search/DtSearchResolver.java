package sample.dt.webcomponents.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import sample.dt.webcomponents.DtPageable;

public class DtSearchResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(DtPageable.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		int start = Integer.parseInt(webRequest.getParameter("start"));
		int length = Integer.parseInt(webRequest.getParameter("length"));
		int draw = Integer.parseInt(webRequest.getParameter("draw"));
		
		// column
		int idx = 0;
		Map<String, String[]> parameterMap = webRequest.getParameterMap();
		List<DtSearchColumn> columns = new ArrayList<>();
		while (parameterMap.containsKey("columns[" + idx + "][data]")) {
			// search condition
			DtSearchColumn.DtColSearchCondition cond = new DtSearchColumn.DtColSearchCondition(parameterMap.get("columns[" + idx + "][search][value]")[0]);
			DtSearchColumn col = new DtSearchColumn(idx, parameterMap.get("columns[" + idx + "][data]")[0], cond);
			columns.add(col);
			idx++;
		}

		// order
		idx = 0;
		List<DtSearchColumn.DtSearchOrder> orders = new ArrayList<>();
		while (parameterMap.containsKey("order[" + idx + "][column]")) {
			int colIdx = Integer.parseInt(parameterMap.get("order[" + idx + "][column]")[0]);
			DtSearchColumn col = columns.stream().filter(e -> e.getIndex() == colIdx).findFirst().orElseThrow(RuntimeException::new);
			Direction direction = "asc".equals(parameterMap.get("order[" + idx + "][dir]")[0]) ? Direction.ASC : Direction.DESC;
			DtSearchColumn.DtSearchOrder order = new DtSearchColumn.DtSearchOrder(idx, direction, col);
			orders.add(order);
			idx++;
		}

		// global search
		DtSearchColumn.DtColSearchCondition globalSearch = new DtSearchColumn.DtColSearchCondition(parameterMap.get("search[value]")[0], Boolean.parseBoolean(parameterMap.get("search[value]")[0]));

		return new DtPageable(start, length, draw, columns, orders, globalSearch);
	}

}
