package snwk

import grails.testing.gorm.DomainUnitTest
import org.apache.tomcat.jni.Local
import spock.lang.Specification

class LocalProfileSpec extends Specification implements DomainUnitTest<LocalProfile> {

    def setup() {
        LocalProfile localProfile = new LocalProfile(profileName: 'test',
                profileSettings: '{"tsm_nw3":true,"tsm_nw2":true,"tsm_nw1":false,"behallare_nw2":true,"utomhus_nw3":true,"fordon_nw1":true}')
        return localProfile
    }

    void "checkMap can be extracted"() {
        given: 'a profile'
        LocalProfile localProfile = setup()

        when: 'checkMap is extracted'
        Map checkMap = localProfile.checkMap

        then: 'is contains the correct data'
        !checkMap['tsm_nw1']
        checkMap['tsm_nw2']
        checkMap['tsm_nw3']
        !checkMap['tsm_elit']
        checkMap['fordon_nw1']
        !checkMap['fordon_nw2']
        !checkMap['fordon_nw3']
        !checkMap['behallare']
        !checkMap['inomhus']
        !checkMap['utomhus_nw1']
        !checkMap['utomhus_nw2']
        checkMap['utomhus_nw3']
    }

}