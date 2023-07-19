// This is a manifest file that'll be compiled into application.js.
//
// Any JavaScript file within this directory can be referenced here using a relative path.
//
// You're free to add application-wide JavaScript to this file, but it's generally better
// to create separate JavaScript files as needed.
//
//= require jquery-3.7.0.min
//= require bootstrap
//= require popper.min
//= require tableFilter
//= require_self
$(document).ready(function () {
    addClickListeners();
});

function addClickListeners() {
    $('.momentCheck').on('click', function () {
        //const moment = this.name;
        //const selected = $('#' + moment).prop('checked');
        //console.log('clicked on moment: ' + this.name + ' is selected: ' + selected.toString());
        reLoadMe();
    });
    $('.klassCheck').on('click', function () {
        const klass = this.name.toString();
        const selected = $('#' + klass).prop('checked');
        const moment = klass.split('_')[0]
        if (selected) {
            let momentSel = $('#' + moment);
            if (!momentSel.prop('checked')) {
                momentSel.prop('checked', true);
            }
        }
        //console.log('clicked on klass: ' + this.name + ' is selected: ' + selected.toString());
        reLoadMe();
    });
}

function reLoadMe() {
    const url = window.location.href.split('?')[0];

    let urlParams = '';
    urlParams = addParams(urlParams, 'tsm');
    urlParams = addParams(urlParams, 'behallare');
    urlParams = addParams(urlParams, 'inomhus');
    urlParams = addParams(urlParams, 'utomhus');
    urlParams = addParams(urlParams, 'fordon');

    window.location.href = url + '?' + urlParams;
}

function addParams(params, moment) {
    const momentIsChecked = $('#' + moment).prop('checked');
    let momentParams = '';
    if (momentIsChecked) {
        momentParams += $('#' + moment + '_nw1').prop('checked') ? (momentParams.length > 0 ? ',1' : '1') : '';
        momentParams += $('#' + moment + '_nw2').prop('checked') ? (momentParams.length > 0 ? ',2' : '2') : '';
        momentParams += $('#' + moment + '_nw3').prop('checked') ? (momentParams.length > 0 ? ',3' : '3') : '';
        if (momentParams.length === 0) {
            momentParams = '1'
        }
        return (params.length > 0 ? params + '&' : '') + moment + '=' + momentParams;
    }
    return params;
}