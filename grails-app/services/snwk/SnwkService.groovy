package snwk

import grails.gorm.transactions.Transactional
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.uri.UriBuilder

import javax.annotation.PostConstruct

@Transactional
class SnwkService {

    static final BASE_URL = 'https://snwktavling.se'
    static final okLan = ['Värmland',
                          'Västernorrland',
                          'Dalarna',
                          'Västmanland',
                          'Uppsala',
                          'Stockholm',
                          'Gävleborg',
                          'Örebro']

    HttpClient httpClient

    @PostConstruct
    def init() {
        String baseUrl = BASE_URL
        httpClient = HttpClient.create(baseUrl.toURL())
    }

    @Transactional(readOnly = true)
    LocalProfile getLocalProfileByName(String profileName) {
        return LocalProfile.findByProfileName(profileName)
    }

    @Transactional(readOnly = true)
    ArrayList<LocalProfile> listLocalProfiles() {
        return LocalProfile.list().sort { it.profileName }
    }

    @Transactional(readOnly = true)
    SnwkEvent getSnwkEventByProfileAndToken(LocalProfile localProfile, String token) {
        return SnwkEvent.findByLocalProfileAndToken(localProfile, token)
    }

    def getEvents(LocalProfile localProfile, String klass, String moment) {
        boolean showAll = localProfile.checkMap['showAll'] ?: false
        def list = []

        try {
            HttpRequest request = HttpRequest.GET(UriBuilder.of('/?page=kalendarium')
                    .queryParam('klass', klass)
                    .queryParam('typ', moment)
                    .build())

            HttpResponse<String> response = httpClient.toBlocking().exchange(request, String)
            String html = response.body()
            def eventList = html.split('<div class="infolist cal list" ')
            if (eventList.size() < 1) {
                log.warn('No events found')
            }
            eventList.eachWithIndex { it, idx ->
                if (idx != 0) {

                    // Token
                    String token = extractBetween(it, 'id="', '"')

                    // Store or update values - id is PROFILE and TOKEN
                    SnwkEvent se = getSnwkEventByProfileAndToken(localProfile, token)
                    if (!se) {
                        se = new SnwkEvent(localProfile: localProfile,
                                token: token)
                    }

                    // Klass/Moment (from input)
                    se.klass = klass
                    se.moment = moment

                    // Datum
                    se.datum = extractBetween(it, '<div style="float: left;"><p class="style2"><strong><span>', '</span>')

                    // Plats
                    se.plats = extractBetween(it, '<span style="margin: 0 15px;">', '</span>')

                    // Arrangör
                    se.organisation = extractBetween(it, 'Arrangerande klubb:', '</div>')

                    // Domare
                    se.domare = extractDomare(it, 'Domare: ', '<br')

                    // Anmälan öppnar
                    se.anmalanStart = extractBetween(it, 'Anmälan är öppen från ', ' till ')

                    // Anmälan stänger
                    if (it.contains(' förlängd till ')) {
                        se.anmalanSlut = extractBetween(it, ' förlängd till ', '</p>')
                    } else {
                        se.anmalanSlut = extractBetween(it, ' till och med ', '</p>')
                    }

                    // Typ av anmälan
                    String anmalanTyp = extractBetween(it, '<p class="style4">', '</p>')
                    if (anmalanTyp.startsWith('Lottad')) {
                        anmalanTyp = SnwkEvent.ANMALAN_TYP_LOTTNING
                    } else {
                        anmalanTyp = SnwkEvent.ANMALAN_TYP_TURORDNING
                    }
                    se.anmalanTyp = anmalanTyp

                    // Länk till anmälan
                    se.anmalanLink = null
                    if (se.isAnmalanOpen) {
                        se.anmalanLink = BASE_URL + '/' + extractBetween(it, '<button class="anmalbutton anmal_button" onclick="location=\'', "'")
                    }

                    // Län
                    se.lan = extractBetween(it, '<div style="clear: both; float: left; width: 100%;"><p>', '</p>')
                    if (se.lan.length() > 4) {
                        se.lan = se.lan.split('s län')[0]
                        se.lan = se.lan.split(' län')[0]
                    } else {
                        se.lan = '????'
                    }

                    se.save failOnError: true

                    if (se.show || showAll) {
                        list.push(se)
                    }
                }
            }


        } catch (Throwable e) {
            log.error('', e)
        }


        return list

    }

    static String extractBetween(it, def from, def to) {
        String result = ''

        def a = it.split(from)
        if (a.size() > 1) {
            def b = a[1].split(to)
            if (b.size() > 0) {
                result = b[0].trim()
                result = result.replaceAll('<s style="color:red;">', '')
                result = result.replaceAll('</s>', '')
            }
        }

        return result
    }

    static String extractDomare(it, def from, def to) {
        String result = ''

        def a = it.split(from)
        a.eachWithIndex { def entry, int i ->
            if (i > 0) {
                def b = entry.split(to)
                if (b.size() > 0) {
                    result += (result.length() > 0 ? ', ' : '') + b[0].trim()
                }
            }
        }

        return result
    }
}
