package snwk

import grails.converters.JSON

class LocalProfile {

    static transients = ['checkMap']

    String profileName
    String profileSettings

    static constraints = {
        profileName blank: false, size: 2..254, unique: true
        profileSettings nullable: true, size: 2..16777215
    }

    def getCheckMap() {
        return JSON.parse(this.profileSettings)
    }
}
