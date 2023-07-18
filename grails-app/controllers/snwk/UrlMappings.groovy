package snwk

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }


        "/"(controller: "snwk", action: [GET: "index"])

        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
