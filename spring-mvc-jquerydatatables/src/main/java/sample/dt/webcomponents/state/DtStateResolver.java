package sample.dt.webcomponents.state;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class DtStateResolver implements HandlerMethodArgumentResolver {
	@Value("${pagination.length.default?:10}")
	int defaultLengthPerPage;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(DtStateParam.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		Map<String, Object> params = webRequest.getParameterMap().entrySet().stream().map(e -> {
			return convert(e.getKey(), e.getValue());
		}).collect(Collectors.toMap(p -> p.getLeft(), p -> p.getRight()));

		return fill(params);
	}

	private Pair<String, Object> convert(String key, String[] val) {
		if (val != null && val.length > 1) {
			return Pair.of(key, val);
		}

		String temp = val == null || val.length == 0 ? null : val[0];

		switch (key) {
		case "time":
			return Pair.of(key, temp == null || "NaN".equals(temp) ? 0L : Long.parseLong(temp));
		case "start":
			return Pair.of(key, temp == null || "NaN".equals(temp) ? 0 : Integer.parseInt(temp));
		case "length":
			return Pair.of(key, temp == null || "NaN".equals(temp) || "0".equals(temp) ? defaultLengthPerPage : Integer.parseInt(temp));
		default: {
			if (key.indexOf("order[") == 0) {
				return Pair.of(key, val);
			} else if (key.indexOf("[smart]") > 0 || key.indexOf("[regex]") > 0 || key.indexOf("[caseInsensitive]") > 0 || key.indexOf("[visible]") > 0) {
				return Pair.of(key, Boolean.parseBoolean(temp));
			} else {
				return Pair.of(key, temp == null ? "" : temp);
			}

		}
		}
	}

	private DtStateParam fill(Map<String, Object> params) {

		DtStateParam state = new DtStateParam();
		state.setTime((Long) params.get("time"));
		state.setLength((Integer) params.get("length"));
		state.setStart((Integer) params.get("start"));

		// global search
		DtStateParam.DtStateSearch search = new DtStateParam.DtStateSearch();
		search.setSearch((String) params.get("search[search]"));
		search.setCaseInsensitive((Boolean) params.get("search[caseInsensitive]"));
		search.setSmart((Boolean) params.get("search[smart]"));
		search.setRegex((Boolean) params.get("search[regex]"));
		state.setSearch(search);

		// order
		Map<String, Object> orderParam = params.entrySet().stream().filter(me -> me.getKey().startsWith("order[")).collect(Collectors.toMap(me -> me.getKey(), me -> me.getValue()));

		int i = 0;
		while (orderParam.containsKey("order[" + i + "][]")) {
			String[] val = (String[]) orderParam.get("order[" + i + "][]");
			DtStateParam.DtStateOrder order = new DtStateParam.DtStateOrder(val[0], val[1]);
			state.getOrders().add(order);
			i++;
		}

		// column
		i = 0;
		Map<String, Object> colParam = params.entrySet().stream().filter(me -> me.getKey().startsWith("columns[")).collect(Collectors.toMap(me -> me.getKey(), me -> me.getValue()));
		while (colParam.containsKey("columns[" + i + "][visible]")) {
			DtStateParam.DtStateColumn column = new DtStateParam.DtStateColumn((Boolean) colParam.get("columns[" + i + "][visible]"),
					new DtStateParam.DtStateSearch((String) colParam.get("columns[" + i + "][search][search]"), (Boolean) colParam.get("columns[" + i + "][search][smart]"),
							(Boolean) colParam.get("columns[" + i + "][search][regex]"), (Boolean) colParam.get("columns[" + i + "][search][caseInsensitive]")));
			state.getSearches().add(column);

			i++;
		}

		if (params.containsKey("ColReorder[]")) {
			String[] crs = (String[]) params.get("ColReorder[]");
			state.setColReorder(Arrays.asList(crs));
		}

		return state;

	}
}
