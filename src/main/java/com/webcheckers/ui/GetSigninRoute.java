package com.webcheckers.ui;

import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * The UI Controller to GET the Signin page.
 *
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
public class GetSigninRoute implements Route {
    //The log for this object
    private static final Logger LOG = Logger.getLogger(GetSigninRoute.class.getName());

    //Variables used to hold the objects used by this route
    private final TemplateEngine templateEngine;

    //Attributes in the view used when displaying the signin page
    public static final String PLAYER_NAME_ATTR = "name";

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /signin} HTTP requests.
     *
     * @param templateEngine the HTML template rendering engine
     */
    public GetSigninRoute(final TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        LOG.config("GetSigninRoute is initialized.");
    }

    /**
     * Render the WebCheckers Signin page.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for the Home page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetSigninRoute is invoked.");
        Map<String, Object> vm = new HashMap<>();

        if(request.session().attribute(PLAYER_NAME_ATTR) != null) {
            response.redirect(WebServer.HOME_URL);
            return null;
        }

        // render the View
        return templateEngine.render(new ModelAndView(vm , "signin.ftl"));
    }
}
