package pogo

import util.DateUtil

class SnwkEvent implements Cloneable {

    static final ANMALAN_TYP_LOTTNING= "Lottning"
    static final ANMALAN_TYP_TURORDNING= "Turordning"

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
    boolean lanIsGreen

    String getAnmalanText() {
        return "${this.anmalanStart} till ${this.anmalanSlut} (${this.anmalanTyp})"
    }

    String getAnmalanTextShort() {
        return "${this.anmalanStart.substring(5,10)} - ${this.anmalanSlut.substring(5,10)}"
    }

    String getLanPlatsText() {
        return "${this.lan} - ${this.plats}"
    }

    boolean isAnmalanOpen() {
        if (DateUtil.aDateFromYMD(this.anmalanStart)<= DateUtil.currentJavaUtilDate() &&
                DateUtil.aDateFromYMD(this.anmalanSlut)>= DateUtil.currentJavaUtilDate()) {
            return true
        }
        return false
    }

}

