<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Web Checkers | ${title}</title>
    <meta http-equiv="refresh" content="5">
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

  <h1>Web Checkers | ${title}</h1>

  <!-- Provide a navigation bar -->
  <#include "nav-bar.ftl" />

  <div class="body">

    <!-- Provide a message to the user, if supplied. -->
    <#include "message.ftl" />

    <#if currentUser??>
        <h2>${playersOnline}</h2>
        <form action="./game">
        <ol>

            <#list userList as user>
                <#if user == current>
<#--                    <button name = "name" type="submit" disabled="true" value=${user}>${user}</button>-->
                <#else>
                    <!--<a href="/game?${user}">${user}</a>-->
                    <button name = "opp_user" type="submit" value=${user}>${user}</button>
                </#if>
            </#list>
        </ol>
        </form>
    <#else>
        <h2>${playersOnline}</h2>
        <blockquote>
            <li> ${playerActive}</li>
        </blockquote>
    <#else>
    </#if>
      <h2>Spectate A Game</h2>
      <#if gameList?has_content>
          <#list game_list as id, game>
              <form action="/spectator/game" method="GET">
                  <input type="hidden" value="${id}" name="gameID">
                  <input type="submit" value="${game.redPlayer.name} vs ${game.whitePlayer.name}">
              </form>
          </#list>
      <#else>
          <body>No games are currently active.</body>
      </#if>
    <!-- TODO: future content on the Home:
            to start games,
            spectating active games,
            or replay archived games
    -->

    </p>
  </div>
</div>
</body>

</html>
