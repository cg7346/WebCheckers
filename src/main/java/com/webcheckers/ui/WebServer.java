package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import spark.TemplateEngine;

import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.*;

//import com.webcheckers.model.CheckersGame;


/**
 * The server that initializes the set of HTTP request handlers.
 * This defines the <em>web application interface</em> for this
 * WebCheckers application.
 *
 * <p>
 * There are multiple ways in which you can have the client issue a
 * request and the application generate responses to requests. If your team is
 * not careful when designing your approach, you can quickly create a mess
 * where no one can remember how a particular request is issued or the response
 * gets generated. Aim for consistency in your approach for similar
 * activities or requests.
 * </p>
 *
 * <p>Design choices for how the client makes a request include:
 * <ul>
 *     <li>Request URL</li>
 *     <li>HTTP verb for request (GET, POST, PUT, DELETE and so on)</li>
 *     <li><em>Optional:</em> Inclusion of request parameters</li>
 * </ul>
 * </p>
 *
 * <p>Design choices for generating a response to a request include:
 * <ul>
 *     <li>View templates with conditional elements</li>
 *     <li>Use different view templates based on results of executing the client request</li>
 *     <li>Redirecting to a different application URL</li>
 * </ul>
 * </p>
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class WebServer {
  private static final Logger LOG = Logger.getLogger(WebServer.class.getName());

  //
  // Constants
  //

  /**
   * The URL pattern to request the Home page.
   */
  public static final String HOME_URL = "/";
  /**
   * The URL pattern to request the Sign In page.
   */
  public static final String SIGNIN_URL = "/signin";
  /**
   * The URL pattern to request the Sign Out page.
   */
  public static final String SIGNOUT_URL = "/signout";
  /**
   * The URL pattern to request the game page
   */
  public static final String GAME_URL = "/game";
  /**
   * the URL pattern to check if it is player's turn
   */
  public static final String CHECKTURN_URL = "/checkTurn";
  /**
   * the URL pattern to check if it is player's turn
   */
  public static final String VALIDATEMOVE_URL = "/validateMove";
  /**
   * the URL pattern to to submit someone's turn
   */
  public static final String SUBMITTURN_URL = "/submitTurn";
  /**
   * the URL pattern to backup someone's turn
   */
  public static final String BACKUPTURN_URL = "/backupMove";
  /**
   * the URL pattern to resign game
   */
  public static final String RESIGN_URL = "/resignGame";
    /**
     * the URL pattern to spectate the a game
     */
    public static final String SPECTATOR_URL = "/spectator/game";
    /**
     * the URL pattern to end spectator mode
     */
    public static final String END_SPECTATOR_URL = "/spectator/stopWatching";


  //
  // Attributes
  //

  private final TemplateEngine templateEngine;
  private final Gson gson;
  private final GameManager gameManager;
  private final PlayerLobby playerLobby;
  //
  // Constructor
  //

  /**
   * The constructor for the Web Server.
   *
   * @param templateEngine
   *    The default {@link TemplateEngine} to render page-level HTML views.
   * @param gson
   *    The Google JSON parser object used to render Ajax responses.
   *
   * @throws NullPointerException
   *    If any of the parameters are {@code null}.
   */
  public WebServer(final TemplateEngine templateEngine, final Gson gson, final GameManager gameManager,
                   final PlayerLobby playerLobby){
    // validation
    Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    Objects.requireNonNull(gson, "gson must not be null");
    //
    this.templateEngine = templateEngine;
    this.gson = gson;
    this.gameManager = gameManager;
    this.playerLobby = playerLobby;
  }

  //
  // Public methods
  //

  /**
   * Initialize all of the HTTP routes that make up this web application.
   *
   * <p>
   * Initialization of the web server includes defining the location for static
   * files, and defining all routes for processing client requests. The method
   * returns after the web server finishes its initialization.
   * </p>
   */
  public void initialize() {

    // Configuration to serve static files
    staticFileLocation("/public");

    //// Setting any route (or filter) in Spark triggers initialization of the
    //// embedded Jetty web server.

    //// A route is set for a request verb by specifying the path for the
    //// request, and the function callback (request, response) -> {} to
    //// process the request. The order that the routes are defined is
    //// important. The first route (request-path combination) that matches
    //// is the one which is invoked. Additional documentation is at
    //// http://sparkjava.com/documentation.html and in Spark tutorials.

    //// Each route (processing function) will check if the request is valid
    //// from the client that made the request. If it is valid, the route
    //// will extract the relevant data from the request and pass it to the
    //// application object delegated with executing the request. When the
    //// delegate completes execution of the request, the route will create
    //// the parameter map that the response template needs. The data will
    //// either be in the value the delegate returns to the route after
    //// executing the request, or the route will query other application
    //// objects for the data needed.

    //// FreeMarker defines the HTML response using templates. Additional
    //// documentation is at
    //// http://freemarker.org/docs/dgui_quickstart_template.html.
    //// The Spark FreeMarkerEngine lets you pass variable values to the
    //// template via a map. Additional information is in online
    //// tutorials such as
    //// http://benjamindparrish.azurewebsites.net/adding-freemarker-to-java-spark/.

    //// These route definitions are examples. You will define the routes
    //// that are appropriate for the HTTP client interface that you define.
    //// Create separate Route classes to handle each route; this keeps your
    //// code clean; using small classes.

    // Shows the Checkers game Home page.
    get(HOME_URL, new GetHomeRoute(templateEngine, playerLobby, gameManager));

    // Shows the Checkers game Sign In page
    get(SIGNIN_URL, new GetSignInRoute(templateEngine));

    // Posts the Checkers game Sign In page
    post(SIGNIN_URL, new PostSignInRoute(templateEngine, playerLobby));

    // Posts the Checkers game Sign Out page
    post(SIGNOUT_URL, new PostSignOutRoute(gameManager, templateEngine, playerLobby));

    // Shows the Checkers game board
    get(GAME_URL, new GetGameRoute(templateEngine, playerLobby, gameManager, gson));

    // Validates the move for current player
    post(VALIDATEMOVE_URL, new PostValidateMove(playerLobby, gameManager, gson));

    // Backs up the move for current player
    post(BACKUPTURN_URL, new PostBackupRoute(gameManager, gson));

    // Submits the turn of the current player
    post(SUBMITTURN_URL, new PostSubmitTurn(playerLobby, gameManager, gson));

    // Posts after the resign button is clicked
    post(RESIGN_URL, new PostResignGame(gameManager, gson));

    // Checks to see if the game is ready for next turn
    post(CHECKTURN_URL, new PostCheckTurn(gameManager, gson));

    // Shows the Checkers game as a Spectator
      get(SPECTATOR_URL, new GetSpectatorRoute(templateEngine, playerLobby, gameManager, gson));

      // Posts the Checkers game when there isn't a spectator in a game
//    post(END_SPECTATOR_URL, new PostEndSpectatorRoute());
    //
    LOG.config("WebServer is initialized.");
  }

}