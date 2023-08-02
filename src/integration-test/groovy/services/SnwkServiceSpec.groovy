package services

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import snwk.LocalProfile
import snwk.SnwkEvent
import snwk.SnwkService
import spock.lang.Specification
import util.StringUtil

@Integration
@Rollback
class SnwkServiceSpec extends Specification {

    SnwkService snwkService

    private static LocalProfile setupData() {
        new LocalProfile(profileName: 'user1', profileSettings: '{"tsm_elit": true}').save(failOnError: true)
        LocalProfile lp = new LocalProfile(profileName: 'user2', profileSettings: '{"tsm_elit": false}').save(failOnError: true, flush: true)
        new SnwkEvent(localProfile:lp, token: StringUtil.randomString(12),
                klass: 'NW3', moment: 'tsm',
                datum: '2023-01-01').save(failOnError: true)
        new SnwkEvent(localProfile:lp,token: StringUtil.randomString(12),
                klass: 'NW3', moment: 'tsm',
                datum: '2023-01-02').save(failOnError: true)
        new SnwkEvent(localProfile:lp,token: StringUtil.randomString(12),
                klass: 'NW3', moment: 'tsm',
                datum: '2023-01-03').save(failOnError: true)

        return lp
    }

    void "listLocalProfiles"() {
        given: 'some data'
        LocalProfile lp = setupData()

        when: 'getting a list of profiles'
        def list = snwkService.listLocalProfiles()

        then: 'the list contains all profiles'
        list.size() == 4 //2 are added in BootStrap
        list.contains(lp)
    }

    void "getLocalProfileByName"() {
        given: 'some data'
        LocalProfile lp = setupData()

        when: 'getting a profile'
        LocalProfile localProfile = snwkService.getLocalProfileByName(lp.profileName)

        then: 'the response is the correct profile'
        localProfile == lp
    }

    void "fail getLocalProfileByName"() {
        given: 'some data'
        LocalProfile lp = setupData()

        when: 'getting a profile'
        LocalProfile localProfile = snwkService.getLocalProfileByName(lp.profileName + 'doestNotExist')

        then: 'the response is not a profile'
        !localProfile
    }

    void "getSnwkEventByToken"() {
        given: 'some data'
        LocalProfile lp = setupData()
        SnwkEvent se = SnwkEvent.first()

        when: 'getting an event'
        SnwkEvent snwkEvent = snwkService.getSnwkEventByProfileAndToken(lp,se.token)

        then: 'the response is the correct event'
        snwkEvent == se
    }

    void "fail getSnwkEventByToken"() {
        given: 'some data'
        LocalProfile lp = setupData()
        SnwkEvent se = SnwkEvent.first()

        when: 'getting an event'
        SnwkEvent snwkEvent = snwkService.getSnwkEventByProfileAndToken(lp,se.token + 'doestNotExist')

        then: 'the response is not an event'
        !snwkEvent
    }

    void "test extraction of Plats"() {
        given: 'html text'
        String html = someHtml()

        when: 'extraction of Location is done'
        String location = SnwkService.extractBetween(html, '<span style="margin: 0 15px;">', '</span>')

        then: 'location is correct'
        location.equals("Bikini bottom")
    }

    void "test extraction of Domare"() {
        given: 'html text'
        String html = someHtml()

        when: 'extraction of Judges is done'
        String judges = SnwkService.extractDomare(html, 'Domare: ', '<br')

        then: 'judges are correct'
        judges.equals("SpongeBob SquarePants, Patrick Star, Sandy Cheeks")
    }

    private String someHtml() {
        return "<li style=\"box-shadow: 0 0 5px 5px gray; border: 0; margin: 20px auto;\" id=\"5277\">\n" +
                "\t\n" +
                "<div style=\"display: flex; flex-wrap: wrap;\">\n" +
                "\t<div style=\"width: 65px;\">\n" +
                "\t\t<div style=\"background: white; border: 1px solid gray; border-radius: 4px; width: 55px; text-align: center;font-family: 'Open Sans', sans-serif;\">\n" +
                "\t\t\t<div style=\"background: #ADA1D4; height: 20px; padding-top: 1px; margin: 0 0 6px;\">\n" +
                "\t\t\t\t<strong style=\"color: white; font-weight: 800; font-size: 13pt; font-family: ARIAL; text-shadow: none;\">AUG</strong>\n" +
                "\t\t\t</div>\n" +
                "\t\t\t<div style=\"height: 20px; margin-top: 8px; color: black; font-weight: 900; font-size: 20pt;\">\n" +
                "\t\t\t\t27\n" +
                "\t\t\t</div>\n" +
                "\t\t\t<div style=\"margin: 0 0 3px;\">\n" +
                "\t\t\t\t<p style=\"margin: 0 0 10px; color: black; height: 10px; font-size: 10pt; font-weight: 400;\">Söndag</p>\n" +
                "\t\t\t</div>\t\t\t\n" +
                "\t\t</div>\n" +
                "\t\t<div style=\"text-align: center; margin-top: 5px; width: 57px;\">\n" +
                "\t \t\t\n" +
                "\t\t</div>\n" +
                "\t</div>\t\n" +
                "\t<div style=\"margin-left: 20px; margin-top: -10px; width: calc(100% - 85px);\">\n" +
                "\t\t<div style=\"width: 100%;\">\n" +
                "\t\t\t\n" +
                "\t\t\t<div style=\"float: left;\"><p class=\"style2\"><strong><span>2023-08-27</span><span style=\"margin: 0 15px;\">Bikini bottom</span></strong></div>\n" +
                "\t\t\t\n" +
                "\t\n" +
                "\t\t\t<div style=\"float: left;\"><p class=\"style2\">\n" +
                "\t\n" +
                "\t\t\t\t<span style=\"margin-right: 15px;\">Tävling i Samtliga Moment i klass:  NW3 </span>\n" +
                "\t\t\t\n" +
                "\t\t\t\t</p>\n" +
                "\t\t\t</div>\n" +
                "\t\n" +
                "\t\t\t<div style=\"clear: both; float: left; width: 100%;\"><p>Västerbottens län</p></div>\n" +
                "\t\t\n" +
                "\t\t\t<div style=\"clear: both; float: left; width: 100%;\">\n" +
                "\t\t\t<p class=style1>Arrangerande klubb: \n" +
                "\tBB Brukshundklubb\n" +
                "\t\t\t</div>\n" +
                "\t\t\t<div style=\"clear: both; float: left; width: 100%;\">\n" +
                "\t\t\t\t<p class=style1>Arrangör: <a href=\"http://www.bbbhk.com\" target=_blank>BB BHK</a>\n" +
                "\t\t\t\t</p>\n" +
                "\t\t\t\t<p class=style1>\n" +
                "\t\t\t\tKontaktmail: <a href=\"mailto:info@bbbhk.se\" target=_top>info@bbbhk.se</a>\n" +
                "\t\t\t\t</p>\n" +
                "\t\t\t</div>\n" +
                "\t\t\t<div style=\"clear: both; float: left;\"><p>\n" +
                "\tAnmälan är öppen från 2023-07-12 klockan 18:00 till och med 2023-08-05</p></div><div style=\"clear: both; float: left\"><p class=\"style4\">Lottad (Slumpmässig) platsantagning</p></div>\n" +
                "\t\t\t<div style=\"clear: both; float: left;\"><p>Pris: 500:-</p></div>\n" +
                "\t\t\n" +
                "\t\t\t<div style=\"float: left; margin-left: 15px;\"><p>Antal platser NW3: 12st</p></div>\n" +
                "\t\t\t\t\t\t\n" +
                "\t\t</div>\n" +
                "\t\t<div style=\"clear: both; margin-left: 1px; width: 100%; display: inline-block; text-align: center; margin-top: 5px;\">\n" +
                "\t\t\t<input id=\"list-1.5277\" name=\"accordion-1\" type=\"checkbox\" hidden >\n" +
                "\n" +
                "\t\t\t<label id=\"label-1.5277\" for=\"list-1.5277\" style=\"padding: 5px 0; margin: 0 0 0 -85px;\"></label>\n" +
                "\n" +
                "\t\t\t<article class=\"list-large\" style=\"text-align: left;\">\n" +
                "\t\t\t\t<div style=\"margin: 0 40px 0 0; float: left;\">\n" +
                "\t\n" +
                "\t\t\t\t\t<p>Tid kl: 08.00</p>\n" +
                "\t\t\n" +
                "\t\t\t\t\t<p>Plats:<br />BB Brukshundklubb<br />\n" +
                "Bikini bottom 12<br />\n" +
                "1234 DEEP</p>\n" +
                "\t\t\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t\t<div style=\"float: left;\">\n" +
                "\t\t\t\t\t<p>\n" +
                "\t\t\n" +
                "\t\t\t\t\tDomare: SpongeBob SquarePants<br>\n" +
                "\t\t\t\n" +
                "\t\t\t\t\tDomare: Patrick Star<br>\n" +
                "\t\t\t\t\t\t\n" +
                "\t\t\t\t\tDomare: Sandy Cheeks<br>\n" +
                "\t\t\t\n" +
                "\t\t\t\t\t</p>\n" +
                "\n" +
                "\n" +
                "\t\t\t\t</div>\n" +
                "\t\n" +
                "\t\t\t\t<div style=\"clear: both; width: 100%; margin-top: 10px; display: inline-block\">\n" +
                "\t\t\t\t\t<a href=\"http://www.bikinibottom.com\" target=_blank>Länk till mer information</a>\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\n" +
                "\t\t\t</article>\n" +
                "\t\t</div>\n" +
                "\t\t<div style=\"width: 100%; text-align: center; display: inline-block; margin-left: -45px;\">\n" +
                "\t<button class=\"anmalbutton\" onclick=\"location='?page=ekipagesida&anmalan=y&token=invalid_token'\">Klicka här för komma till anmälan</button>\n" +
                "\t\t</div>\n" +
                "\t</div>\n" +
                "</div>\n" +
                "\t\n" +
                "</li>\n" +
                "\t\n"
    }
}
