package snwk

class SnwkController {

    SnwkService snwkService

    static allowedMethods = [index: "GET"]

    def index() {

        def allList = []
        def checkMap = [:]

        if (params.tsm) {
            addToCheckMap(checkMap, 'tsm')
            allList += getList(params.tsm.toString(), 'tsm')
        }
        if (params.behallare) {
            addToCheckMap(checkMap, 'behallare')
            allList += getList(params.behallare.toString(), 'behallare')
        }
        if (params.inomhus) {
            addToCheckMap(checkMap, 'inomhus')
            allList += getList(params.inomhus.toString(), 'inomhus')
        }
        if (params.utomhus) {
            addToCheckMap(checkMap, 'utomhus')
            allList += getList(params.utomhus.toString(), 'utomhus')
        }
        if (params.fordon) {
            addToCheckMap(checkMap, 'fordon')
            allList += getList(params.fordon.toString(), 'fordon')
        }

        allList = allList.sort { it.datum }

        respond allList, model: [allList : allList,
                                 checkMap: checkMap]

    }

    private ArrayList getList(String paramString, String moment) {
        ArrayList responseList = []

        def classList = paramString.split(',')
        classList.each { String klass ->
            if (['1', '2', '3'].contains(klass)) {
                responseList += snwkService.getEvents('NW' + klass, moment)
            }
        }
        return responseList
    }

    private addToCheckMap(Map checkMap, String moment) {
        checkMap[moment] = true
        checkMap[moment + '_nw1'] = params[moment].toString().contains('1')
        checkMap[moment + '_nw2'] = params[moment].toString().contains('2')
        checkMap[moment + '_nw3'] = params[moment].toString().contains('3')
    }

}
