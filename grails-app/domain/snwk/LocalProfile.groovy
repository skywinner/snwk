package snwk

class LocalProfile {

    String profileName
    String checkMap

    static constraints = {
        profileName blank: false, size: 2..254
        checkMap nullable: true, size: 2..16777215
    }
}
