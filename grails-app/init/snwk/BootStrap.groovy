package snwk

class BootStrap {

    def init = { servletContext ->
        if (LocalProfile.count() == 0) {
            log.info('Adding profiles')
            LocalProfile lp = new LocalProfile()
            lp.profileName = 'Jesper'
            lp.profileSettings = '{"tsm_nw3":true,"behallare_nw1":true,"tsm_elit":true,"utomhus_nw1":true,"inomhus_nw2":true,"fordon_nw1":true}'
            lp.save failOnError: true

            lp = new LocalProfile()
            lp.profileName = 'Anna'
            lp.profileSettings = '{"tsm_nw3":false,"tsm_nw2":true,"behallare_nw2":true,"tsm_elit":true}'
            lp.save failOnError: true
        }
    }
}
