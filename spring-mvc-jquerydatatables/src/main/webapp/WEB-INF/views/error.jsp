<%@ page isErrorPage="true" contentType="text/html; charset=utf-8"%>
<!doctype html>
<html>
<head>
<meta content="text/html; charset=utf-8">
<tags:css />
<style>
#error {
	width: 80%;
	max-height: 70%;
	overflow: scroll;
}
</style>
</head>
<body>
	<h1>Error</h1>
	<div>
		<c:if test=${not empty exception }>
            ${exception.printStackTrace(new java.io.PrintWriter(out)); }
        </c:if>
	</div>
</body>
</html>