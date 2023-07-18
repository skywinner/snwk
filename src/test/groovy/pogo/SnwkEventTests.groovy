package pogo

import org.junit.Before
import org.junit.Test
import util.DateUtil

class SnwkEventTests {
    SnwkEvent snwkEvent

    @Before
    void setup() {
        snwkEvent = new SnwkEvent(lan: 'Gavle',
                plats: 'Valbo',
                anmalanStart: '2023-01-01',
                anmalanSlut: '2023-04-01',
                anmalanTyp: SnwkEvent.ANMALAN_TYP_TURORDNING)
    }

    @Test
    void testAnmalanText() {
        assert snwkEvent.getAnmalanText().equals((String) "2023-01-01 till 2023-04-01 (${SnwkEvent.ANMALAN_TYP_TURORDNING})")
    }

    @Test
    void testAnmalanTextShort() {
        assert snwkEvent.getAnmalanTextShort().equals((String) "01-01 - 04-01")
    }

    @Test
    void testLanPlatsText() {
        assert snwkEvent.getLanPlatsText().equals((String) "Gavle - Valbo")
    }

    @Test
    void testAnmalanIsClosed() {
        assert !snwkEvent.anmalanOpen
    }

    @Test
    void testAnmalanIsOpen() {
        snwkEvent.anmalanStart = DateUtil.toYmdString()
        snwkEvent.anmalanSlut = DateUtil.toYmdString()
        assert snwkEvent.anmalanOpen
    }
}
