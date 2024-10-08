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

    const profileName = $('#profile').val();
    enableDisableSelections(profileName);

});

function addClickListeners() {
    $('.klassCheck').on('click', function () {
        const klass = this.name.toString();
        const selected = $('#' + klass).prop('checked');
        const moment = klass.split('_')[0]

        /* if (selected) {
            let momentSel = $('#' + moment);
            if (!momentSel.prop('checked')) {
                momentSel.prop('checked', true);
            }
        } */
        //console.log('clicked on klass: ' + this.name + ' is selected: ' + selected.toString());

        let ajaxUrl = updateProfileUrl; //Set in the head of GSP
        const profileName = $('#profile').val();
        updateProfile(ajaxUrl, profileName, moment, klass, selected);
    });

    $('.settingCheck').on('click', function () {
        const name = this.name;
        const selected = $('#' + name).prop('checked');
        const profileName = $('#profile').val();

        let ajaxUrl = updateProfileUrl; //Set in the head of GSP
        updateProfile(ajaxUrl, profileName, '', name, selected);
    });

    $('#profile').on('change', function () {
        const profileName = $('#profile').val();
        reLoadMeWithProfile(profileName);
    });

    addClickListenersTable();
}

function addClickListenersTable() {
    $('.showCheck').on('click', function () {
        const name = this.name;
        const selected = $('#' + name).prop('checked');
        const token = name.substring(5)
        //console.log('clicked on show: ' + token + ' is selected: ' + selected);

        let ajaxUrl = setShowUrl; //Set in the head of GSP
        setSelected(ajaxUrl, token, selected);
    });

    $('.selectedCheck').on('click', function () {
        const name = this.name;
        const selected = $('#' + name).prop('checked');
        const token = name.substring(9)
        //console.log('clicked on selected: ' + token + ' is selected: ' + selected);

        let ajaxUrl = setSelectedUrl; //Set in the head of GSP
        setSelected(ajaxUrl, token, selected);
    });
}

function enableDisableSelections(profileName) {
    if (profileName.toString() === 'null') {
        //console.log('DISABLING selections');
        setDisabled(true);
    } else {
        //console.log('enabling selections for ' + profileName);
        setDisabled(false);
    }
}

function setDisabled(trueFalse) {
    $('.klassCheck').prop('disabled', trueFalse);
    $('.settingCheck').prop('disabled', trueFalse);
    $('.showCheck').prop('disabled', trueFalse);
    $('.selectedCheck').prop('disabled', trueFalse);
}

function reLoadMeWithProfile(profileName) {
    const url = window.location.href.split('?')[0];

    let urlParams = '';
    if (profileName) {
        urlParams = '?profile=' + profileName;
    }

    setDisabled(true);
    window.location.href = (url + urlParams).trim();
}

function setSelected(ajaxUrl, token, selected) {
    if (ajaxUrl) {
        setDisabled(true);
        const profileName = $('#profile').val();
        $.ajax({
            url: ajaxUrl,
            type: "PUT",
            data: {
                token: token,
                selected: selected,
                profileName: profileName
            },
            success: function (resp) {
                // Only act if something has been updated
                if (resp.length > 0) {
                    $("#content").html(resp);
                    $("#filter").val('');
                }
                setDisabled(false);
                addClickListenersTable();
            },
            error: function (xhr, ajaxOptions, thrownError) {
                console.log('Could not set Selected ' + xhr.status.toString());
            }
        });
    }
}

function updateProfile(ajaxUrl, profileName, moment, klass, selected) {
    if (ajaxUrl) {
        setDisabled(true);
        $.ajax({
            url: ajaxUrl,
            type: "PUT",
            data: {
                profileName: profileName,
                moment: moment,
                klass: klass,
                selected: selected
            },
            success: function (resp) {
                // Only act if something has been updated
                if (resp.length > 0) {
                    $("#content").html(resp);
                    $("#filter").val('');
                }
                setDisabled(false);
                addClickListenersTable();
            },
            error: function (xhr, ajaxOptions, thrownError) {
                console.log('Could not update Profile ' + xhr.status.toString());
            }
        });
    }
}
