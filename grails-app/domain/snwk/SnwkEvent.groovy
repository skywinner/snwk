package snwk

import util.DateUtil

class SnwkEvent {

    static transients = ['anmalanText', 'anmalanTextShort', 'lanPlatsText', 'isAnmalanOpen']

    static final ANMALAN_TYP_LOTTNING = "Lottning"
    static final ANMALAN_TYP_TURORDNING = "Turordning"
    static final okLan = ['Värmland',
                          'Västernorrland',
                          'Dalarna',
                          'Västmanland',
                          'Uppsala',
                          'Stockholm',
                          'Gävleborg',
                          'Örebro']

    String token
    boolean show = true
    boolean selected = false

    String datum
    String plats
    String organisation
    String domare
    String klass
    String moment
    String anmalanStart
    String anmalanSlut
    String anmalanTyp
    String anmalanLink
    String lan

    java.sql.Date dateCreated = DateUtil.currentDate()
    java.sql.Date lastUpdated

    static belongsTo = [localProfile: LocalProfile]

    String getAnmalanText() {
        return "${this.anmalanStart} till ${this.anmalanSlut} (${this.anmalanTyp})"
    }

    String getAnmalanTextShort() {
        return "${this.anmalanStart.substring(5, 10)} - ${this.anmalanSlut.substring(5, 10)}"
    }

    String getLanPlatsText() {
        return "${this.lan} - ${this.plats}"
    }

    boolean getIsAnmalanOpen() {
        if (DateUtil.aDateFromYMD(this.anmalanStart) <= DateUtil.currentJavaUtilDate() &&
                DateUtil.aDateFromYMD(this.anmalanSlut) >= DateUtil.currentJavaUtilDate()) {
            return true
        }
        return false
    }

    boolean getIsLanGreen() {
        return okLan.contains(this.lan)
    }

    static constraints = {
        token blank: false, size: 1..254, unique: ['localProfile']
        datum nullable: true, blank: false
        plats nullable: true, blank: false
        organisation nullable: true, blank: false
        domare nullable: true, blank: false
        klass inList: ['NW1', 'NW2', 'NW3', 'ELIT']
        moment inList: ['tsm', 'inomhus', 'utomhus', 'behallare', 'fordon']
        anmalanStart nullable: true, blank: false
        anmalanSlut nullable: true, blank: false
        anmalanTyp nullable: true, blank: false
        anmalanLink nullable: true, blank: false
        lan nullable: true, blank: false
    }

    static mapping = {
        show defaultValue: true, column: 'is_shown'
        selected defaultValue: false, column: 'is_selected'
    }

    def beforeUpdate() {
        lastUpdated = DateUtil.currentDate()
    }

}
