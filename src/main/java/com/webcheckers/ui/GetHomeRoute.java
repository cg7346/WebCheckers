package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import com.webcheckers.util.Message;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella</>
 */
public class GetHomeRoute implements Route {

  // Values used in the view-model map for rendering the home view.
  static final String WELCOME_ATTR = "title";
  static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");

  // View name
  static final String VIEW_NAME = "home.ftl";

  // Key in the session attribute map for the player who started the session
  static final String PLAYERLOBBY_KEY = "playerLobby";
  static final String TIMEOUT_SESSION_KEY = "timeoutWatchdog";

  // The length of the session timeout in seconds
  static final int SESSION_TIMEOUT_PERIOD = 120;

  static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

  //
  // Attributes
  //

  private final TemplateEngine templateEngine;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetHomeRoute(final TemplateEngine templateEngine) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    //
    LOG.config("GetHomeRoute is initialized.");
  }

  /**
   * Render the WebCheckers Home page.
   *
   * @param request
   *   the HTTP request
   * @param response
   *   the HTTP response
   *
   * @return
   *   the rendered HTML for the Home page
   */
  @Override
  public Object handle(Request request, Response response) {
    LOG.finer("GetHomeRoute is invoked.");
    //
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "Welcome!");

    // display a user message in the Home page
    vm.put("message", WELCOME_MSG);

    // render the View
    return templateEngine.render(new ModelAndView(vm , VIEW_NAME));
  }
}
