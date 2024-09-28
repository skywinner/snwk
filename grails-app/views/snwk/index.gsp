<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>SNWK-tävlingar lista</title>
    <script>
        let setShowUrl = "${g.createLink(absolute: true, controller: 'snwk', action: 'updateShow')}"
        let setSelectedUrl = "${g.createLink(absolute: true, controller: 'snwk', action: 'updateSelected')}"
        let updateProfileUrl = "${g.createLink(absolute: true, controller: 'snwk', action: 'updateProfile')}"
    </script>
</head>

<body>

<div class="container-fluid">
    <div class="row">
        <div style="margin-bottom: 0 !important;" class="col input-container">
            <div>
                <span><g:select id="profile" name="profile" from="${profileList*.profileName}"
                                style="width: 100px;"
                                keys="${profileList*.profileName}"
                                value="${profileName}"
                                noSelection="${['null': 'Välj en profil...']}"/></span>
                <span><g:checkBox class="settingCheck" checked="${checkMap['showAll']}" name="showAll"/>&nbsp;<g:message
                        code="nose.work.showAll.label" default="showAll"/>
                </span>
                <span><g:checkBox class="settingCheck" checked="${checkMap['showScentTest']}"
                                  name="showScentTest"/>&nbsp;<g:message
                        code="nose.work.showScentTest.label" default="showScentTest"/></span>
            </div>

            <g:set var="moment" value="tsm"/>
            <g:render template="/layouts/inputRow" bean="checkMap"/>

            <g:set var="moment" value="behallare"/>
            <g:render template="/layouts/inputRow" bean="checkMap"/>

            <g:set var="moment" value="inomhus"/>
            <g:render template="/layouts/inputRow" bean="checkMap"/>

            <g:set var="moment" value="utomhus"/>
            <g:render template="/layouts/inputRow" bean="checkMap"/>

            <g:set var="moment" value="fordon"/>
            <g:render template="/layouts/inputRow" bean="checkMap"/>

            <g:set var="moment" value="inoff"/>
            <g:render template="/layouts/inputRow" bean="checkMap"/>

        </div>

        <div class="col-sm-5 d-none d-lg-block logo-container">
            <asset:image class="logo" src="Logotype-SNWK.jpg" alt="SNWK logotype"/>
        </div>
    </div>
</div>

<div class="d-none d-sm-block">
    <label class="layout-invisible" for="filter"></label>
    <input id="filter" type="search" autofocus class="light-table-filter form-control mousetrap"
           data-table="listTable"
           placeholder='<g:message code="placeholder.filter" args="[clubList?.size()]"/>'>
</div>

<div id="content" role="main">
    <g:render template="/layouts/index_table"/>
</div>

</body>
</html>
