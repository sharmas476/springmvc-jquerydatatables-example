<!doctype html>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<meta name="_date_format" content="yyyy/mm/dd" />
<title>JQuery DataTables spring-mvc example</title>
<tags:css />
<style>
div.dt-buttons {
	float: right;
	padding: 3px 5px 3px 0;
}

tfoot.datatables-filters input, tfoot.datatables-filters select {
	width: 100%;
	padding: 3px;
	vertical-align: middle;
}

tfoot.datatables-filters select {
	height: 100%;
}

tfoot.datatables-filters {
	display: table-header-group;
}

.number {
	text-align: right;
}

.search-filter {
	width: 100%;
}

.sliderwrapper, .daterangewrapper {
	position: absolute;
	border-radius: 10px;
	box-shadow: 5px 5px rgba(0, 0, 0, .6);
	padding: 10px;
	z-index: 100;
}

.sliderwrapper {
	width: 150px;
	height: 80px;
}

.daterangewrapper {
	width: 250px;
	height: 100px;
}
</style>
</head>
<body>
	<div class="container">
		<h1>JQuery DataTables spring-mvc example</h1>
		<div class="row">
			<div class="form-inline">
				<label for="language"><spring:message code="caption.language" /></label> <select name="language" id="language">
					<c:forEach var="l" items="${langs}">
						<option value="${l[0]}">${l[1]}</option>
					</c:forEach>
				</select> <label for="dateformat"><spring:message code="caption.dateformat" /></label> <select name="dateformat" id="dateformat">
					<c:forEach var="d" items="${dateFormats}">
						<option value="${d}">${d}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="row">
			<div class="table-responsive">
				<table id="dt" class="table table-striped table-bordered text-nowrap">
					<thead>
						<tr class="bg-info">
							<th class="col-md-2" data-col="uname"><spring:message code="caption.name" /></th>
							<th class="col-md-2" data-col="urole">Position</th>
							<th class="col-md-2" data-col="city" data-order="false">Office</th>
							<th class="col-md-1" data-col="age" data-cls="number" data-render="number">Age</th>
							<th class="col-md-2" data-col="hdate" data-render="date">Hire date</th>
							<th class="col-md-2" data-col="salary" data-cls="number" data-render="number">Salary</th>
						</tr>
					</thead>
					<tfoot class="datatables-filters">
						<tr>
							<th><input type="text" name="uname" class="search-filter" data-col="uname"></th>
							<th><select name="urole" class="search-filter" data-col="urole">
									<option />
									<c:forEach var="r" items="${roles}">
										<option value="${r}">${r}</option>
									</c:forEach>
							</select></th>
							<th><select name="city" class="search-filter" data-col="city">
									<option />
									<c:forEach var="c" items="${cities}">
										<option value="${c}">${c}</option>
									</c:forEach>
							</select></th>
							<th style="text-align: left;" id="filter-age-th"><button type="button" id="filter-age-btn" class="text-left btn-md btn-link">
									<span class="glyphicon glyphicon-modal-window"></span><span class="glyphicon glyphicon-ok" style="display: none;"></span>Filter
								</button> <input type="hidden" name="age" id="filter-age-filter" class="search-filter" data-col="age"></th>
							<th style="text-align: left;" id="filter-hdate-th">
								<button type="button" id="filter-hdate-btn" class="text-left btn-md btn-link">
									<span class="glyphicon glyphicon-modal-window"></span><span class="glyphicon glyphicon-ok" style="display: none;"></span>Filter
								</button> <input type="hidden" name="hdate" id="filter-hdate-filter" class="search-filter" data-col="hdate">
							</th>
							<th></th>
						</tr>
					</tfoot>
					<tbody />
				</table>
			</div>
		</div>

	</div>

	<tags:js />

	<script type="text/template" id="slider-template">
        <div id="slider_{id}" class="sliderwrapper bg-info" style="display:none;">
            <p class="slider-fromto">from: <span>{min}</span> - to: <span>{max}</span></p>
            <div class="slider"></div>
        </div>
    </script>

	<script type="text/template" id="date-range-template">
        <div id="dates_{id}" class="daterangewrapper bg-info form-horizontal" style="display:none;">
            <div class="row">
                <label for="dates_{id}_from" class="control-label col-xs-4">From</label>
                <input type="text" name="date_{id}_from" id="date_{id}_from" class="datepicker form-control col-xs-4">
            </div>
            <div class="row">
                <label for="dates_{id}_to"  class="control-label col-xs-4">To</label>
                <input type="text" name="date_{id}_to" id="date_{id}_to" class="datepicker form-control col-xs-4">
            </div>
        </div>
    </script>

	<script>
        $(function() {
            'use strict'
            var tid = 'dt';
            var $tid = '#' + tid;
            var loadUrl = '${pageContext.request.contextPath}/dt/stateLoad/' + tid;
            var saveUrl = '${pageContext.request.contextPath}/dt/stateSave/' + tid;
            var langUrl = '${pageContext.request.contextPath}/dt/lang';
            var dataUrl = '${pageContext.request.contextPath}/dt/tbody';
            var topUrl = '${pageContext.request.contextPath}/dt';

            var d = {
                id : tid
            };

            $('#filter-hdate-th').append($('#date-range-template').text().replace(/\{(\w+)\}/g, function(m, k) {
                return d[k] === undefined ? '' : d[k];
            }));

            var $pickers = $('#dates_' + tid + ' .datepicker').datepicker({
                "dateFormat" : $("meta[name='_date_format']").attr("content").replace('yyyy', 'yy')
            });
            $('#filter-hdate-btn').on('click', function(event) {
                $('#dates_' + tid).toggle();
                var $spans = $('span', this);
                if ($spans.eq(1).is(':visible')) {
                    var dates = {
                        from : $pickers.eq(0).val(),
                        to : $pickers.eq(1).val(),
                        dateFormat : $pickers.datepicker("option", "dateFormat")
                    };
                    $('#filter-hdate-filter').val(encodeURIComponent(JSON.stringify(dates)));
                    $('#filter-hdate-filter').trigger('change');
                }
                $spans.toggle();
            });

            var minMaxAge = {
                min : +'${minage}',
                max : +'${maxage}'
            };

            var p = {
                id : tid,
                min : minMaxAge.min,
                max : minMaxAge.max
            };
            $("#filter-age-th").append($('#slider-template').text().replace(/\{(\w+)\}/g, function(m, k) {
                return p[k] === undefined ? '' : p[k];
            }));

            $('#filter-age-btn').on('click', function(event) {
                $('#slider_' + tid).toggle();
                var $spans = $('span', this);
                if ($spans.eq(1).is(':visible')) {
                    $('#filter-age-filter').val(encodeURIComponent(JSON.stringify(minMaxAge)));
                    $('#filter-age-filter').trigger('change');
                }
                $spans.toggle();
            });

            var onChangeSlider = function() {
                var $spans = $('#slider_' + tid + ' .slider-fromto span');
                $spans.eq(0).text(minMaxAge.min);
                $spans.eq(1).text(minMaxAge.max);
            };

            var $slider = $('#slider_' + tid + ' .slider').slider({
                range : true,
                step : 1,
                min : minMaxAge.min,
                max : minMaxAge.max,
                values : [ minMaxAge.min, minMaxAge.max ],
                slide : function(event, ui) {
                    minMaxAge.min = ui.values[0];
                    minMaxAge.max = ui.values[1];
                    onChangeSlider();
                }
            });

            // csrf header for ajax post request
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");
            $(document).ajaxSend(function(e, xhr, options) {
                if (token && header) {
                    xhr.setRequestHeader(header, token);
                }
            });

            var columns = [];
            var $ths = $($tid + ' thead th');
            var renderers = {
                number : function(data, type, full, meta) {
                    return data.toString().replace(/(\d)(?=(\d{3})+$)/g, '$1,');
                },
                date : function(data, type, full, meta) {
                    var dateFormat = $("meta[name='_date_format']").attr("content");
                    var aDate = data.split('-');
                    return dateFormat.replace('yyyy', aDate[0]).replace('mm', aDate[1]).replace('dd', aDate[2]);
                }
            };
            $.each($ths, function(i, th) {
                var $th = $(th);
                var colDef = {
                    data : $th.attr('data-col'),
                    name : this.data
                };
                if ($th.attr('data-render')) {
                    colDef.render = renderers[$th.attr('data-render')];
                }
                if ($th.attr('data-cls')) {
                    colDef.className = $th.attr('data-cls');
                }
                if ($th.attr('data-order') === 'false') {
                    colDef.orderable = false;
                }
                columns.push(colDef);
            });

            function convertDateFormat(date, ffmt, tfmt) {
                if (ffmt === tfmt) {
                    return date;
                }
                if (!date || date.length !== 10) {
                    return date;
                }
                var aDate = date.split(/[-/]/);
                var aFfmt = ffmt.split(/[-/]/);

                var ymd = {};
                for (var i = 0; i < 3; i++) {
                    switch (aFfmt[i]) {
                    case 'yy':
                        ymd.y = aDate[i];
                        break;
                    case 'mm':
                        ymd.m = aDate[i];
                        break;
                    case 'dd':
                        ymd.d = aDate[i];
                        break;
                    }
                }
                return tfmt.replace('yy', ymd.y).replace('mm', ymd.m).replace('dd', ymd.d);
            }

            var table = $($tid).on('preXhr.dt', function(e, settings, data) {
                $.each(data.columns, function(i, col) {
                    var $filter = $($tid + '_wrapper .search-filter[data-col="' + col.data + '"]');
                    if ($filter.length) {
                        col.search.value = $filter.val();
                    }
                });
            }).DataTable({
                searching : false,
                stateSave : true,
                responsive : true,
                processing : true,
                columns : columns,
                serverSide : true,
                ajax : {
                    url : dataUrl,
                    type : 'POST'
                },
                dom : 'Bfrtip',
                colReorder : true,
                lengthMenu : [ [ 10, 25, 50, -1 ], [ '10 rows', '25 rows', '50 rows', 'Show all' ] ],
                buttons : [ 'colvis', 'pageLength' ],
                language : {
                    url : langUrl
                },
                stateLoadCallback : function(settings) {
                    var state;
                    $.ajax({
                        type : 'GET',
                        url : loadUrl,
                        async : false,
                        success : function(data) {
                            state = data;
                            $.each(settings.aoColumns, function(i, col) {
                                var $filter = $($tid + ' .search-filter[data-col="' + col.data + '"]');
                                if ($filter.length) {
                                    $filter.val(data.columns[col.idx].search.search);
                                    if ($filter.val() !== '') {
                                        if (col.data === 'age') {
                                            minMaxAge = JSON.parse(decodeURIComponent($filter.val()));
                                            $slider.slider("values", [ minMaxAge.min, minMaxAge.max ]);
                                            onChangeSlider();
                                        } else if (col.data === 'hdate') {
                                            var dates = JSON.parse(decodeURIComponent($filter.val()));
                                            var currDateFormat = $("meta[name='_date_format']").attr("content").replace('yyyy', 'yy');
                                            $pickers.eq(0).val(convertDateFormat(dates.from, dates.dateFormat, currDateFormat));
                                            $pickers.eq(1).val(convertDateFormat(dates.to, dates.dateFormat, currDateFormat));
                                            $pickers.datepicker("option", "dateFormat", currDateFormat);
                                        }
                                    }
                                }
                            });
                        }
                    });
                    return state;
                },
                stateSaveCallback : function(settings, data) {
                    $.each(settings.aoColumns, function(i, col) {
                        var $filter = $($tid + '_wrapper .search-filter[data-col="' + col.col + '"]');
                        if ($filter.length) {
                            var idx = data.ColReorder ? data.ColReorder[col.idx] : col.idx;
                            data.columns[idx].search.search = $filter.val();
                        }
                    });
                    $.post(saveUrl, data);
                }
            });

            $($tid + ' .search-filter').on('change', function(event) {
                table.draw();
            });

            $('#language').on('change', function(event) {
                location.href = topUrl + '?language=' + $(this).val();
            });

            $('#dateformat').on('change', function(event) {
                var dfmt = $(this).val();
                //update meta
                $("meta[name='_date_format']").attr("content", dfmt);

                //update datepicker
                var shortDfmt = dfmt.replace('yyyy', 'yy');
                var currPfmt = $pickers.eq(0).datepicker('option', 'dateFormat');
                var fdate = convertDateFormat($pickers.eq(0).val(), currPfmt, shortDfmt);
                var tdate = convertDateFormat($pickers.eq(1).val(), currPfmt, shortDfmt);
                $pickers.datepicker("option", "dateFormat", shortDfmt);
                $pickers.eq(0).datepicker('setDate', fdate);
                $pickers.eq(1).datepicker('setDate', tdate);

                //reload table
                table.draw();
            });

            //load time language selection from cookie data
            var lang = /_LOCALE=(\w+)/.exec(document.cookie);
            if (lang) {
                $('#language').val(lang[1]);
            }

            // for debug
            $(document).ajaxError(function(event, jqxhr, settings, thrownError) {
                console.log("**** ajax error occurred ****");
                console.log(event);
                console.log(jqxhr);
                console.log(settings);
                console.log(thrownError);
                console.log("*****************************")
            });
        });
    </script>
</body>
</html>