<%@ page import="snwk.SnwkEvent" %>
<div>
    <label class="layout-invisible" for="filter"></label>
    <input id="filter" type="search" autofocus class="light-table-filter form-control mousetrap"
           data-table="listTable"
           placeholder='<g:message code="placeholder.filter" args="[clubList?.size()]"/>'>
</div>

<table class="listTable table table-sm">
    <thead>
    <tr class="table-active">
        <th style="border-bottom-width: 0 !important;">Datum</th>
        <th style="border-bottom-width: 0 !important;">Visa</th>
        <th style="border-bottom-width: 0 !important;">Anm</th>
        <th style="border-bottom-width: 0 !important;">Plats</th>
        <th style="border-bottom-width: 0 !important;" class="d-none d-sm-block">Arrangör</th>
        <th style="border-bottom-width: 0 !important;>" Klass</th>
        <th style="border-bottom-width: 0 !important;">Moment</th>
        <th style="border-bottom-width: 0 !important;">Anmälan</th>
    </tr>
    </thead>
    <tbody>

    <g:each in="${allList}" status="i" var="row">
        <tr class="${row.selected ? 'table-success' : ''}">

            <td>${row.datum}</td>
            <td><g:checkBox class="showCheck" name="show_${row.token}" id="show_${row.token}"
                            value="${row.show}"/></td>
            <td><g:checkBox class="selectedCheck" name="selected_${row.token}" id="selected_${row.token}"
                            value="${row.selected}"/></td>
            <td class="${row.selected ? '' : (row.isLanGreen ? 'table-danger' : '')}">${row.lanPlatsText}</td>
            <td class="d-none d-sm-block">${row.organisation} - ${row.domare}</td>
            <td>${row.klass}</td>
            <td>${row.moment.toUpperCase()}</td>
            <td class="${row.selected ? '' : (row.anmalanTyp == SnwkEvent.ANMALAN_TYP_TURORDNING ? 'table-danger' : (row.isAnmalanOpen ? 'table-primary' : ''))}">
                <g:if test="${row.isAnmalanOpen}"><a target="snwktavling" href="${row.anmalanLink}"></g:if>
                <span class="d-none d-sm-block">${row.anmalanText}</span>
                <span class="d-sm-none">${row.anmalanTextShort}</span>
                <g:if test="${row.isAnmalanOpen}"></a></g:if>
            </td>
        </tr>

    </g:each>

    </tbody>
</table>