package snwk

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class SnwkControllerSpec extends Specification implements ControllerUnitTest<SnwkController> {

    def populateValidParams(params) {
        assert params != null

        params["tsm"] = "3"
        params["fordon"] = "1"

    }

    void "Test the index action returns the correct model"() {
        given: 'service results'
        def event = [:]
        event.key = 'value'
        controller.snwkService = Mock(SnwkService) {
            2 * getEvents(_, _) >> [event]
        }

        when: 'The index action is executed'
        populateValidParams(params)
        controller.index()

        then: 'The models are correct'
        model.allList
        model.checkMap
        model.checkMap.tsm
        model.checkMap.fordon
        !model.checkMap.inomhus
        !model.checkMap.utomhus
        !model.checkMap.behallare
    }

}
