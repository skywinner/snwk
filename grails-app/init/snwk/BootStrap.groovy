package snwk

class BootStrap {

    def init = { servletContext ->
        if (LocalProfile.count() == 0) {
            log.info('Adding profiles')
            LocalProfile lp = new LocalProfile()
            lp.profileName = 'Anna'
            lp.profileSettings = '{"tsm_nw3":true,"tsm_nw2":false,"behallare_nw2":true,"tsm_elit":true,"nw1_doftprov":true}'
            lp.save failOnError: true

            lp = new LocalProfile()
            lp.profileName = 'Jesper'
            lp.profileSettings = '{"tsm_elit":true}'
            lp.save failOnError: true
        }
    }
}
