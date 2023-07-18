package snwk

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class SnwkControllerSpec extends Specification implements ControllerUnitTest<SnwkController> {

    def setup() {
    }

    def cleanup() {
    }

    void "Test the index action returns the correct model"() {
        given: 'service results'
        def event = [:]
        event.key = 'value'
        controller.snwkService = Mock(SnwkService) {
            5 * getEvents(_, _) >> [event]
        }

        when: 'The index action is executed'
        controller.index()

        then: 'The model is correct'
        model.allList
    }

}
