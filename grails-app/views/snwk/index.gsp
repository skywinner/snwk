<%@ page import="pogo.SnwkEvent" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Welcome to SNWK Jesper</title>
</head>

<body>

<div class="svg" role="presentation">
    <div class="logo-container">
        <asset:image class="logo" src="snwk_logo.png"/>
    </div>
</div>

<div id="content" role="main">
    <div>
        <label class="layout-invisible" for="filter"></label>
        <input id="filter" type="search" autofocus class="light-table-filter form-control mousetrap"
               data-table="listTable"
               placeholder='<g:message code="placeholder.filter" args="[clubList?.size()]"/>'>
    </div>

    <table class="listTable table table-sm">
        <tr>
            <th>Datum</th>
            <th>Plats</th>
            <th class="d-none d-sm-block">Arrangör</th>
            <th>Klass</th>
            <th>Moment</th>
            <th>Anmälan</th>
        </tr>

        <g:each in="${allList}" status="i" var="row">
            <tr>

                <td>${row.datum}</td>
                <td class="${row.lanIsGreen ? 'table-danger' : ''}">${row.getLanPlatsText()}</td>
                <td class="d-none d-sm-block">${row.organisation} - ${row.domare}</td>
                <td>${row.klass}</td>
                <td>${row.moment.toUpperCase()}</td>
                <td class="${row.anmalanTyp == SnwkEvent.ANMALAN_TYP_TURORDNING ? 'table-danger' : (row.isAnmalanOpen() ? 'table-primary' : '')}">
                    <g:if test="${row.isAnmalanOpen()}"><a target="snwktavling" href="${row.anmalanLink}"></g:if>
                    <span class="d-none d-sm-block">${row.getAnmalanText()}</span>
                    <span class="d-sm-none">${row.getAnmalanTextShort()}</span>
                    <g:if test="${row.isAnmalanOpen()}"></a></g:if>
                </td>
            </tr>

        </g:each>
    </table>

</div>

</body>
</html>