package snwk

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification
import util.DateUtil

class SnwkEventSpec extends Specification implements DomainUnitTest<SnwkEvent> {

    def setup() {
        SnwkEvent snwkEvent = new SnwkEvent(lan: 'Gavle',
                plats: 'Valbo',
                anmalanStart: '2023-01-01',
                anmalanSlut: '2023-04-01',
                anmalanTyp: SnwkEvent.ANMALAN_TYP_TURORDNING)
        return snwkEvent
    }

    void "anmalanText is generated"() {
        given: 'an event'
        SnwkEvent snwkEvent = setup()

        when: 'text is calculated'
        String anmalanText = snwkEvent.anmalanText

        then: 'the text is correct'
        anmalanText.equals((String) "2023-01-01 till 2023-04-01 (${SnwkEvent.ANMALAN_TYP_TURORDNING})")
    }

    void "shorter anmalanText is generated"() {
        given: 'an event'
        SnwkEvent snwkEvent = setup()

        when: 'shorter text is calculated'
        String anmalanText = snwkEvent.anmalanTextShort

        then: 'the shorter text is correct'
        anmalanText.equals((String) "01-01 - 04-01")
    }

    void "lan and plats combined is generated"() {
        given: 'an event'
        SnwkEvent snwkEvent = setup()

        when: 'lan and plats are combined'
        String lanPlatsText = snwkEvent.lanPlatsText

        then: 'the text is correct'
        lanPlatsText.equals((String) "Gavle - Valbo")
    }

    void "anmalan is closed"() {
        given: 'an event'
        SnwkEvent snwkEvent = setup()

        when: 'check that anmalan is closed'
        boolean isAnmalanOpen = snwkEvent.isAnmalanOpen

        then: 'the event indicates closed'
        !isAnmalanOpen
    }

    void "anmalan can be open"() {
        given: 'an event with dates surrounding today'
        SnwkEvent snwkEvent = setup()
        snwkEvent.anmalanStart = DateUtil.toYmdString()
        snwkEvent.anmalanSlut = DateUtil.toYmdString()

        when: 'check that anmalan is open'
        boolean isAnmalanOpen = snwkEvent.isAnmalanOpen

        then: 'the event indicates open'
        isAnmalanOpen
    }

    void "lan can be OK (close enough to Gävle)"() {
        given: 'an event'
        SnwkEvent snwkEvent = setup()
        snwkEvent.lan = 'Gävleborg'
        assert SnwkEvent.okLan.contains(snwkEvent.lan)

        when: 'check that lan is OK'
        boolean isLanGreen = snwkEvent.isLanGreen

        then: 'the event indicates lan is OK to travel to'
        isLanGreen
    }

    void "lan can be too far away (from Gävle)"() {
        given: 'an event'
        SnwkEvent snwkEvent = setup()
        snwkEvent.lan = 'Skåne'
        assert !SnwkEvent.okLan.contains(snwkEvent.lan)

        when: 'check that lan is not OK'
        boolean isLanGreen = snwkEvent.isLanGreen

        then: 'the event indicates lan is NOT OK to travel to'
        !isLanGreen
    }

}