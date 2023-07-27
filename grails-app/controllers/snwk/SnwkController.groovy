package snwk

import grails.converters.JSON
import grails.gorm.transactions.Transactional

class SnwkController {

    SnwkService snwkService

    static allowedMethods = [index: "GET", updateShow: "PUT", updateSelected: "PUT", updateProfile: "PUT"]

    def index() {

        def allList = []
        def checkMap = [:]
        def profileList = snwkService.listLocalProfiles()
        String profileName = null

        if (params.profile) {
            profileName = params.profile.toString()
            LocalProfile localProfile = snwkService.getLocalProfileByName(profileName)
            if (localProfile) {
                profileName = localProfile.profileName
                checkMap = localProfile.checkMap
            }
        }
        checkMap.each {
            if (!['tsm', 'inomhus', 'utomhus', 'behallare', 'fordon'].contains(it.key)) {
                if (it.value) {
                    String moment = it.key.toString().split('_')[0]
                    String klass = it.key.toString().split('_')[1].toUpperCase()
                    allList += snwkService.getEvents(klass, moment)
                }
            }
        }

        allList = allList.sort { it.datum }

        respond allList, model: [allList    : allList,
                                 checkMap   : checkMap,
                                 profileList: profileList,
                                 profileName: profileName]

    }

    // AJAX action, set new parameters for profile
    @Transactional
    def updateProfile() {
        String profileName = params.profileName
        LocalProfile localProfile = snwkService.getLocalProfileByName(profileName)

        if (localProfile) {
            String klass = params.klass
            boolean selected = Boolean.parseBoolean(params.selected)

            def checkMap = localProfile.checkMap
            checkMap[klass] = selected

            localProfile.profileSettings = (checkMap as JSON).toString()
            localProfile.save failOnError: true, flush: true

            //TODO: render partial page

        } else {
            // Respond with error
            render(status: 422, text: 'Could not update profile ' + profileName)
        }
    }

    // AJAX action, set selection of SHOW
    @Transactional
    def updateShow() {
        String token = params.token
        SnwkEvent snwkEvent = snwkService.getSnwkEventByToken(token)

        boolean show = Boolean.parseBoolean(params.selected)
        if (snwkEvent) {
            snwkEvent.show = false
            snwkEvent.show = show
            snwkEvent.save failOnError: true, flush: true
        } else {
            // Respond with error
            render(status: 422, text: 'Could not find event')
        }
    }

    // AJAX action, set selection of SHOW
    @Transactional
    def updateSelected() {
        String token = params.token
        SnwkEvent snwkEvent = snwkService.getSnwkEventByToken(token)

        boolean selected = Boolean.parseBoolean(params.selected)
        if (snwkEvent) {
            snwkEvent.selected = false
            snwkEvent.selected = selected
            snwkEvent.save failOnError: true, flush: true
        } else {
            // Respond with error
            render(status: 422, text: 'Could not find event')
        }
    }

}
