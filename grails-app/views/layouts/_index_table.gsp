<%@ page import="snwk.SnwkEvent" %>
<%@ page import="util.StringUtil" %>
<table class="listTable table table-sm">
    <thead>
    <tr class="table-active">
        <th style="border-bottom-width: 0 !important;">Datum</th>
        <th style="border-bottom-width: 0 !important;">Intresserad</th>
        <th style="border-bottom-width: 0 !important;">Anmäld</th>
        <th style="border-bottom-width: 0 !important;">Plats</th>
        <th style="border-bottom-width: 0 !important;" class="d-none d-sm-table-cell">Arrangör</th>
        <th style="border-bottom-width: 0 !important;" class="d-none d-sm-table-cell">Klass</th>
        <th style="border-bottom-width: 0 !important;">Moment</th>
        <th style="border-bottom-width: 0 !important;">Anmälan</th>
    </tr>
    </thead>
    <tbody>

    <g:each in="${allList}" status="i" var="row">
        <tr class="${row.selected ? 'table-success' : ''}">

            <td><span class="d-none d-sm-table-cell">${row.datum}</span>
                <span class="d-sm-none">${row.datum.substring(5)}</span></td>
            <td><g:checkBox class="showCheck" name="show_${row.token}" id="show_${row.token}"
                            value="${row.show}"/></td>
            <td><g:checkBox class="selectedCheck" name="selected_${row.token}" id="selected_${row.token}"
                            value="${row.selected}"/></td>
            <td>
                <span class="d-none d-sm-table-cell">${row.lanPlatsText}</span>
                <span class="d-sm-none">${StringUtil.firstPartThenDots(row.plats, 12)}</span>
            </td>
            <td class="d-none d-sm-table-cell">${row.organisation} - ${row.domare}</td>
            <td class="d-none d-sm-table-cell">${row.klass}</td>
            <td>
                <span class="d-none d-sm-table-cell">${row.moment.toUpperCase()}</span>
                <span class="d-sm-none">${row.moment.toUpperCase() + '/' + row.klass}</span>
            </td>
            <td class="${row.selected ? '' : (row.anmalanTyp == SnwkEvent.ANMALAN_TYP_TURORDNING ? 'table-danger' : (row.isAnmalanOpen ? 'table-primary' : ''))}">
                <g:if test="${row.isAnmalanOpen}"><a target="snwktavling" href="${row.anmalanLink}"></g:if>
                <span class="d-none d-sm-table-cell">${row.anmalanText}</span>
                <span class="d-sm-none">${row.anmalanTextShort}</span>
                <g:if test="${row.isAnmalanOpen}"></a></g:if>
            </td>
        </tr>

    </g:each>
    <g:if test="${allList.size() > 0}">
        <tr>
            <td colspan="8" class="d-none d-sm-table-cell">${allList.size()} st</td>
            <td colspan="6" class="d-sm-none">${allList.size()} st</td>
        </tr>
    </g:if>

    </tbody>
</table>