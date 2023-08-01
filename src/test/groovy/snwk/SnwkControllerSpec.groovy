package snwk

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class SnwkControllerSpec extends Specification implements ControllerUnitTest<SnwkController> {

    def populateValidParams(params) {
        assert params != null

        params["profile"] = "username"
    }

    void "Test the index action returns the correct model"() {
        given: 'service results'
        def aList = [new SnwkEvent(datum: '2023-01-01')]
        controller.snwkService = Mock(SnwkService) {
            1 * listLocalProfiles() >> ['user1', 'user2']
            1 * getLocalProfileByName(_) >> new LocalProfile(profileName: 'username', profileSettings: '{"tsm_elit": true}')
            1 * getEvents(_, _, _) >> aList
        }

        when: 'The index action is executed'
        populateValidParams(params)
        controller.index()

        then: 'The models are correct'
        model.allList
        model.checkMap
        model.profileList
        model.profileName
        model.checkMap.tsm_elit
        !model.checkMap.inomhus_nw1
    }

}
