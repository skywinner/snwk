package snwk

import grails.converters.JSON
import grails.gorm.transactions.Transactional

class SnwkController {

    SnwkService snwkService

    static allowedMethods = [index: "GET", updateShow: "PUT", updateSelected: "PUT", updateProfile: "PUT"]

    def index() {

        def checkMap = [:]
        def profileList = snwkService.listLocalProfiles()
        String profileName = null
        LocalProfile localProfile = null

        if (params.profile) {
            profileName = params.profile.toString()
            localProfile = snwkService.getLocalProfileByName(profileName)
            if (localProfile) {
                profileName = localProfile.profileName
                checkMap = localProfile.checkMap
            }
        }

        def allList = mapToList(localProfile)

        respond allList, model: [allList    : allList,
                                 checkMap   : checkMap,
                                 profileList: profileList,
                                 profileName: profileName]

    }

    private ArrayList mapToList(LocalProfile localProfile) {
        ArrayList allList = []

        if (localProfile) {
            boolean showScentTest = localProfile.checkMap['showScentTest'] ?: false
            if (showScentTest) {
                String moment = 'doftprov'
                String klass = 'NW1'
                allList += snwkService.getEvents(localProfile, klass, moment)
                klass = 'NW2'
                allList += snwkService.getEvents(localProfile, klass, moment)
                klass = 'NW3'
                allList += snwkService.getEvents(localProfile, klass, moment)
            }
            localProfile.checkMap.each {
                if (!['tsm', 'inomhus', 'utomhus', 'behallare', 'fordon', 'doftprov', 'inoff', 'showAll', 'showScentTest'].contains(it.key)) {
                    if (it.value) {
                        String moment = it.key.toString().split('_')[0]
                        String klass = it.key.toString().split('_')[1].toUpperCase()
                        allList += snwkService.getEvents(localProfile, klass, moment)
                    }
                }
            }

            allList = allList.sort { it.datum }
        }

        return allList
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

            def allList = mapToList(localProfile)
            render(template: '/layouts/index_table', model: [allList: allList])

        } else {
            // Respond with error
            render(status: 422, text: 'Could not update profile ' + profileName)
        }
    }

    // AJAX action, set selection of SHOW
    @Transactional
    def updateShow() {
        String profileName = params.profileName
        LocalProfile localProfile = snwkService.getLocalProfileByName(profileName)
        String token = params.token
        SnwkEvent snwkEvent = snwkService.getSnwkEventByProfileAndToken(localProfile, token)

        boolean show = Boolean.parseBoolean(params.selected)
        if (snwkEvent) {
            snwkEvent.show = false
            snwkEvent.show = show
            snwkEvent.save failOnError: true, flush: true

            if (localProfile) {
                def allList = mapToList(localProfile)
                render(template: '/layouts/index_table', model: [allList: allList])
            }

        } else {
            // Respond with error
            render(status: 422, text: 'Could not find event')
        }
    }

    // AJAX action, set selection of SHOW
    @Transactional
    def updateSelected() {
        String profileName = params.profileName
        LocalProfile localProfile = snwkService.getLocalProfileByName(profileName)
        String token = params.token
        SnwkEvent snwkEvent = snwkService.getSnwkEventByProfileAndToken(localProfile, token)

        boolean selected = Boolean.parseBoolean(params.selected)
        if (snwkEvent) {
            snwkEvent.selected = false
            snwkEvent.selected = selected
            snwkEvent.save failOnError: true, flush: true

            if (localProfile) {
                def allList = mapToList(localProfile)
                render(template: '/layouts/index_table', model: [allList: allList])
            }

        } else {
            // Respond with error
            render(status: 422, text: 'Could not find event')
        }
    }

}
