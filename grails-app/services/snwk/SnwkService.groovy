package snwk

import grails.gorm.transactions.Transactional
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.uri.UriBuilder
import pogo.SnwkEvent

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

    def getEvents(String klass, String moment) {
        def list = []

        try {
            HttpRequest request = HttpRequest.GET(UriBuilder.of('/?page=kalendarium')
                    .queryParam('klass', klass)
                    .queryParam('typ', moment)
                    .build())

            HttpResponse<String> response = httpClient.toBlocking().exchange(request, String)
            String html = response.body()
            def eventList = html.split('<li ')
            eventList.eachWithIndex { it, idx ->
                if (idx != 0) {
                    SnwkEvent oneEvent = new SnwkEvent()
                    oneEvent.klass = klass
                    oneEvent.moment = moment

                    // Datum
                    oneEvent.datum = extractBetween(it, '<div style="float: left;"><p class="style2"><strong><span>', '</span>')

                    // Plats
                    oneEvent.plats = extractBetween(it, '<span style="margin: 0 15px;">', '</span>')

                    // Arrangör
                    oneEvent.organisation = extractBetween(it, 'Arrangerande klubb:', '</div>')

                    // Domare
                    oneEvent.domare = extractDomare(it, 'Domare: ', '<br')

                    // Anmälan öppnar
                    oneEvent.anmalanStart = extractBetween(it, 'Anmälan är öppen från ', ' till ')

                    // Anmälan stänger
                    oneEvent.anmalanSlut = extractBetween(it, ' till och med ', '</p>')

                    // Typ av anmälan
                    oneEvent.anmalanTyp = extractBetween(it, '<p class="style4">', '</p>')
                    if (oneEvent.anmalanTyp.startsWith('Lottad')) {
                        oneEvent.anmalanTyp = SnwkEvent.ANMALAN_TYP_LOTTNING
                    } else {
                        oneEvent.anmalanTyp = SnwkEvent.ANMALAN_TYP_TURORDNING
                    }

                    // Länk till anmälan
                    oneEvent.anmalanLink = ''
                    if (oneEvent.anmalanOpen) {
                        oneEvent.anmalanLink = BASE_URL + '/' + extractBetween(it, '<button class="anmalbutton" onclick="location=\'', "'")
                    }

                    // Län
                    oneEvent.lan = extractBetween(it, '<div style="clear: both; float: left; width: 100%;"><p>', '</p>')
                    if (oneEvent.lan.length() > 4) {
                        oneEvent.lan = oneEvent.lan.split('s län')[0]
                        oneEvent.lan = oneEvent.lan.split(' län')[0]
                    }
                    oneEvent.lanIsGreen = okLan.contains(oneEvent.lan)


                    list.push(oneEvent)
                }
            }


        }
        catch (Throwable e) {
            log.error('', e)
        }


        return list

    }

    public static String extractBetween(it, def from, def to) {
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

    public static String extractDomare(it, def from, def to) {
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
