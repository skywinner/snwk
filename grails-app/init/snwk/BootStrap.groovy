package snwk

class BootStrap {

    def init = { servletContext ->
        log.info('Adding profiles')
        LocalProfile lp = new LocalProfile()
        lp.profileName = 'Jesper'
        lp.save failOnError: true

        lp = new LocalProfile()
        lp.profileName = 'Anna'
        lp.save failOnError: true
    }
}
