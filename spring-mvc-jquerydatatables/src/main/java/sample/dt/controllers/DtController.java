package sample.dt.controllers;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;

import sample.dt.entity.CompanyUser;
import sample.dt.entity.DtState;
import sample.dt.entity.QCompanyUser;
import sample.dt.repository.CompanyUserRepository;
import sample.dt.repository.DtStateRepository;
import sample.dt.repository.querydsl.QueryDslExpressionHelper;
import sample.dt.repository.querydsl.BooleanExpressionTemplate;
import sample.dt.webcomponents.DtPageable;
import sample.dt.webcomponents.DtResponse;
import sample.dt.webcomponents.search.DtSearchColumn;
import sample.dt.webcomponents.state.DtStateParam;

@Controller
@RequestMapping(value = "/dt")
public class DtController {

    @Autowired
    VelocityEngine velocityEngine;

    @Autowired
    DtStateRepository dtStateRepository;

    @Autowired
    CompanyUserRepository companyUserRepository;

    @Autowired
    MessageSource messageSource;

    @Autowired
    QueryDslExpressionHelper queryDslExpressionHelper;

    @Value("#{'${dt.lang.key}'.split(',')}")
    List<String> lngKeys;

    @Value("#{'${lang.key}'.split(',')}")
    List<String> langs;

    @Value("#{'${dateformat.key}'.split(',')}")
    List<String> dateFormats;

    ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model, Locale locale) {

        List<String> roles = companyUserRepository.findDistinctUserRole();
        List<String> cities = companyUserRepository.findDistinctUserCity();
        Pair<Integer, Integer> minmaxAge = companyUserRepository.findMinMaxAge();

        List<String[]> langvls = langs.stream().map(l -> {
            return new String[]{l, messageSource.getMessage("lang.key." + l, new Object[0], l, locale)};
        }).collect(Collectors.toList());

        model.addAttribute("roles", roles);
        model.addAttribute("cities", cities);
        model.addAttribute("dateFormats", dateFormats);
        model.addAttribute("langs", langvls);
        model.addAttribute("minage", minmaxAge.getLeft());
        model.addAttribute("maxage", minmaxAge.getRight());

        return "dt";
    }

    @RequestMapping(value = "/lang", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
    public
    @ResponseBody
    String datatablesLanguage(Locale locale) {
        final Object[] param = new Object[0];
        Map<String, Object> langs = lngKeys.stream().collect(Collectors.toMap(e -> e, e -> messageSource.getMessage("dt.lang." + e, param, locale)));
        final String str = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "vm/dtlang.vm", "UTF-8", langs);
        return str;
    }

    @RequestMapping(value = "/stateSave/{tableId}", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    public
    @ResponseBody
    String stateSave(@PathVariable String tableId, DtStateParam state) throws IOException {

        Map<String, Object> map = new HashMap<>();
        map.put("dt", state);

        // to json string using velocity
        final String str = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "vm/dtstate.vm", "UTF-8", map);

        DtState data = dtStateRepository.findByTableId(tableId);
        if (data == null) {
            data = new DtState();
            data.setTableId(tableId);
        }

        data.setTableSettings(str.getBytes(Charset.forName("UTF-8")));
        dtStateRepository.save(data);

        return null;
    }

    @RequestMapping(value = "/stateLoad/{tableId}", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
    public
    @ResponseBody
    String stateLoad(@PathVariable String tableId, HttpServletRequest request) {
        DtState data = dtStateRepository.findByTableId(tableId);
        if (data == null) {
            return null;
        }
        return new String(data.getTableSettings(), Charset.forName("UTF-8"));
    }

    @RequestMapping(value = "/tbody", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    public
    @ResponseBody
    DtResponse<CompanyUser> body(DtPageable pageable) {
        Predicate predicate = queryDslExpressionHelper.express(new BooleanExpressionTemplate() {
            @Override
            public BooleanExpression cretePredicate(BooleanExpression lastPredicate, DtSearchColumn column, Map<String, DtSearchColumn> allSearchColumns) {
                final String colName = column.getName();
                BooleanExpression be = null;
                JsonNode node = null;
                String encoded = null;
                String decorded = null;
                switch (colName) {
                    case "uname":
                        be = QCompanyUser.companyUser.uname.upper().like("%" + column.getCondition().getValue().toUpperCase() + "%");
                        break;
                    case "urole":
                        be = QCompanyUser.companyUser.urole.eq(column.getCondition().getValue());
                        break;
                    case "city":
                        be = QCompanyUser.companyUser.city.eq(column.getCondition().getValue());
                        break;
                    case "hdate":
                        try {
                            // url encode for save to database
                            encoded = column.getCondition().getValue();
                            decorded = URLDecoder.decode(encoded, "UTF-8");
                            node = mapper.readTree(decorded);
                            String from = node.findPath("from").asText();
                            String to = node.findPath("to").asText();
                            String format = node.findPath("dateFormat").asText();
                            Date fdate = asDate(from, format);
                            Date tdate = asDate(to, format);

                            if (fdate != null) {
                                be = QCompanyUser.companyUser.hdate.goe(fdate);
                            }
                            if (tdate != null) {
                                if (be != null) {
                                    be = be.and(QCompanyUser.companyUser.hdate.loe(tdate));
                                } else {
                                    be = QCompanyUser.companyUser.hdate.loe(tdate);
                                }
                            }

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case "age":
                        try {
                            encoded = column.getCondition().getValue();
                            decorded = URLDecoder.decode(encoded, "UTF-8");
                            node = mapper.readTree(decorded);
                            Integer min = node.findPath("min").asInt();
                            Integer max = node.findPath("max").asInt();
                            be = QCompanyUser.companyUser.age.goe(min).and(QCompanyUser.companyUser.age.loe(max));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    default:
                        return lastPredicate;
                }
                return lastPredicate == null ? be : lastPredicate.and(be);
            }
        }, pageable);

        Page<CompanyUser> users = companyUserRepository.findAll(predicate, pageable);
        return DtResponse.of(users, pageable);
    }

    protected Date asDate(String sdate, String format) {
        if (sdate.length() != 10) {
            return null;
        }
        try {
            return new Date(new SimpleDateFormat(format.replace("mm", "MM")).parse(sdate).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
