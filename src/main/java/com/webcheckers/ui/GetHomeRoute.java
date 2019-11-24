package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;


/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</>
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella</>
 */
public class GetHomeRoute implements Route {

    // Values used in the view-model map for rendering the home view.
    static final String WELCOME_ATTR = "title";
    static final String WELCOME_ATTR_MSG = "Welcome!";
    static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");
    static final String CURRENT_USER = "currentUser";
    static final String USERS_LIST = "userList";
    static final String GAME_LIST = "gameList";
    static final String MESSAGE = "message";
    static final String PLAYERS_COUNT = "playerActive";
    static final String PLAYERS = "There are %d other players available to play at this time.";
    static final String PLAYER = "There are %d other player available to play at this time.";
    static final String NO_PLAYERS = "There are no other players available to play at this time";
    static final String PLAYERS_ON = "playersOnline";
    static final String PLAYERS_ONLINE = "Players Online";
    static final String SPECTATOR = "spectator";

    // View name
    static final String VIEW_NAME = "home.ftl";

    static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    //
    // Attributes
    //

    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;
    private final GameManager gameManager;

    //
    // Constructor
    //

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine the HTML template rendering engine
     */
    public GetHomeRoute(final TemplateEngine templateEngine, PlayerLobby playerLobby, GameManager gameManager) {
        // validation
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        this.gameManager = Objects.requireNonNull(gameManager, "gameManager is required");
        LOG.config("GetHomeRoute is initialized.");
    }

  /**
   * {@inheritDoc}
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
    vm.put(WELCOME_ATTR, WELCOME_ATTR_MSG);

    // display a user message in the Home page
    vm.put(MESSAGE, WELCOME_MSG);

    final Session httpSession = request.session();
    Message messageError = httpSession.attribute(GetGameRoute.MESSAGE_ERR);
      if (playerLobby != null) {
          Player currentPlayer = httpSession.attribute("Player");
          if(messageError != null){
              ModelAndView mv = error(vm,messageError, currentPlayer);
              httpSession.removeAttribute(GetGameRoute.MESSAGE_ERR);
          }
          if (currentPlayer != null){
              if (currentPlayer.isInGame()){
                  response.redirect(WebServer.GAME_URL);
                  halt();
                  return null;
              } else if (currentPlayer.isSpectating()) {
                  response.redirect((WebServer.SPECTATOR_URL));
                  halt();
                  return null;
              }
              ModelAndView mv = currentUser(vm, request);
              return templateEngine.render(mv);
          }
          ModelAndView mv = playerActive(vm);
          return templateEngine.render(mv);
      }

    // render the View
    return templateEngine.render(new ModelAndView(vm , VIEW_NAME));
  }


    //
    // Private Methods
    //

    /**
     * active player of the model view
     * @param vm
     * @return
     */
    private ModelAndView playerActive(Map<String, Object> vm) {
        vm.put(PLAYERS_ON, PLAYERS_ONLINE);
        Integer playerCount = playerLobby.getPlayers().size();
        if (playerCount == 0) {
            vm.put(PLAYERS_COUNT, NO_PLAYERS);
        } else if (playerCount == 1) {
            String count = String.format(PLAYER, playerCount);
            vm.put(PLAYERS_COUNT, count);
        } else {
            String count = String.format(PLAYERS, playerCount);
            vm.put(PLAYERS_COUNT, count);
        }
        return new ModelAndView(vm, VIEW_NAME);
    }

    /**
     * current user of the model view
     * @param vm
     * @param request
     * @return
     */
    public ModelAndView currentUser(Map<String, Object> vm, Request request) {
        final Session session = request.session();
        List<String> userList = playerLobby.getUsernames();
        HashMap<CheckersGame, String> gameList = gameManager.activeGames();
        Player player = session.attribute("Player");
        // Displays the welcome message

        if (gameManager.spectators != null && GetSpectatorRoute.specEndGame != null) {
            if (GetSpectatorStopWatching.visited) {
                GetSpectatorStopWatching.visited = false;
                vm.put(MESSAGE, Message.info(GetSpectatorStopWatching.SPECTATOR_END));
            }
            if (GetSpectatorRoute.specEndGame) {
                GetSpectatorRoute.specEndGame = false;
                vm.put(MESSAGE, Message.info("The game has ended."));
            }
        }

        // Sets the current user to the current player
        vm.put(GAME_LIST, gameManager.activeGames());

        vm.put(GetHomeRoute.SPECTATOR, player.isSpectating());


        vm.put(CURRENT_USER, player);
        vm.put(PLAYERS_ON, PLAYERS_ONLINE);
        vm.put(PostSignInRoute.CURRENT, player.getName());
        vm.put(USERS_LIST, userList);
        vm.put(GAME_LIST, gameList);
        return new ModelAndView(vm, VIEW_NAME);
    }

    /**
     * this handles errors in the model view
     * @param vm
     * @param message
     * @param currentPlayer
     * @return
     */
    private ModelAndView error(final Map<String, Object> vm, final Message message, final Player currentPlayer) {
        vm.put("title", GetHomeRoute.WELCOME_ATTR_MSG);
        //vm.put(GetHomeRoute.CURRENT_USER, playerLobby.getPlayers().get(playerLobby.getPlayers().size()-1));
        //final Session session = request.session();
        //Player currentPlayer = session.attribute("Player");
        vm.put(CURRENT_USER, currentPlayer);
        vm.put(PostSignInRoute.CURRENT, currentPlayer.getName());
        vm.put(PLAYERS_ON, PLAYERS_ONLINE);
        vm.put(USERS_LIST, playerLobby.getUsernames());
        vm.put(MESSAGE, message);
        return new ModelAndView(vm, VIEW_NAME);
    }

}
