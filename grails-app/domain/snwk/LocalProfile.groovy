package snwk

import grails.converters.JSON
import util.DateUtil

class LocalProfile {

    static transients = ['checkMap']

    String profileName
    String profileSettings

    java.sql.Date dateCreated = DateUtil.currentDate()

    static constraints = {
        profileName blank: false, size: 2..254, unique: true
        profileSettings nullable: true, size: 2..16777215
    }

    def getCheckMap() {
        if (!this.profileSettings) {
            this.profileSettings = ''
        }
        if (this.profileSettings == '') {
            this.profileSettings = '{}'
        }
        return JSON.parse(this.profileSettings)
    }
}
