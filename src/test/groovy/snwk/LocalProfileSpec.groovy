package snwk

import grails.testing.gorm.DomainUnitTest
import org.apache.tomcat.jni.Local
import spock.lang.Specification

class LocalProfileSpec extends Specification implements DomainUnitTest<LocalProfile> {

    def setup() {
        LocalProfile localProfile = new LocalProfile(profileName: 'test',
                checkMap: "{JSON-map for check boxes}")
    }

    void "checkMap can be extracted"() {
        given: 'a profile'
        LocalProfile localProfile = setup()

        when: 'checkMap is extracted'
        String checkMap = localProfile.checkMap

        then: 'is contains the correct data'
        checkMap.contains('{')
    }

}