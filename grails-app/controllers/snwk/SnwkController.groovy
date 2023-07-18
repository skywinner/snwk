package snwk

class SnwkController {

    SnwkService snwkService

    static allowedMethods = [index: "GET"]

    def index() {

        def tsmList = snwkService.getEvents('NW3', 'tsm')
        def behallareList = snwkService.getEvents('NW1', 'behallare')
        def inomhusList = snwkService.getEvents('NW2', 'inomhus')
        def utomhusList = snwkService.getEvents('NW1', 'utomhus')
        def fordonList = snwkService.getEvents('NW1', 'fordon')

        def allList = tsmList + behallareList+inomhusList+utomhusList+ fordonList
        allList = allList.sort { it.datum }

        respond allList, model: [allList: allList]

    }
}
