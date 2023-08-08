<!doctype html>
<html lang="en" class="no-js">
<head>
    <title>
    <g:layoutTitle default="SNWK"/>
    </title>
    <meta name="description" content="SNWK Tävlingslista by Jesper">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <asset:link rel="icon" href="favicon.png" type="image/png"/>
    <asset:link rel="shortcut icon" href="favicon.png" type="image/png"/>
    <link rel="apple-touch-icon" href="${assetPath(src: 'favicon.png')}">
    <link rel="apple-touch-icon" sizes="114x114" href="${assetPath(src: 'favicon.png')}">

    <asset:stylesheet src="application.css"/>
    <g:layoutHead/>
</head>

<body>

<g:layoutBody/>

<div id="spinner" class="spinner" style="display:none;">
    <g:message code="spinner.alt" default="Loading&hellip;"/>
</div>

<asset:javascript src="application.js"/>

</body>
</html>
