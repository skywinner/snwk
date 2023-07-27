<div class="row">
    <div class="col-4">
        <div class="checkBox">
            <g:checkBox disabled="true" class="momentCheck" checked="${checkMap[moment]}" name="${moment}"/>&nbsp;<g:message
                    code="nose.work.class.${moment}.label" default="${moment}"/>
        </div>
    </div>

    <div class="col">
        <g:checkBox class="klassCheck" checked="${checkMap[moment+'_nw1']}" name="${moment}_nw1"/>&nbsp;<g:message
                code="nose.work.moment.one.label" default="one"/>
        <g:checkBox class="klassCheck" checked="${checkMap[moment+'_nw2']}" name="${moment}_nw2"/>&nbsp;<g:message
                code="nose.work.moment.two.label" default="two"/>
        <g:checkBox class="klassCheck" checked="${checkMap[moment+'_nw3']}" name="${moment}_nw3"/>&nbsp;<g:message
                code="nose.work.moment.three.label" default="three"/>
        <g:checkBox class="klassCheck" checked="${checkMap[moment+'_elit']}" name="${moment}_elit"/>&nbsp;<g:message
                code="nose.work.moment.elit.label" default="elit"/>
    </div>
</div>
