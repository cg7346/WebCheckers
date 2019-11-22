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

    <#include "message.ftl" />

    <#if currentUser??>
        <h2>${playersOnline}</h2>
        <form action="./game">
        <ol>
            <button name = "opp_user" type="submit" value="AI">AI</button>
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

        <h2>Spectate A Game</h2>
        <#if gameList?has_content>
            <form action="./spectator/game">
                <blockquote>
                    <#list gameList?values as v>
                    <#--          <#if spectator??>-->
                    <#--              <form action="/spectator/game" method="GET">-->
                    <#--                  <input type="hidden" value="${id}" name="gameID">-->
                    <#--                  <input type="submit" value="${game.redPlayer.name} vs ${game.whitePlayer.name}">-->
                    <#--              </form>-->
                    <#--          <#else>-->
                        <button name="spec_user" type="submit" value=${v}>Game ${v}</button>
                    <#--          </#if>-->
                    </#list>
                </blockquote>
            </form>
        <#else>
            <body>No games are currently active.</body>
        </#if>

    <#else>
        <h2>${playersOnline}</h2>
        <blockquote>
            <li> ${playerActive}</li>
        </blockquote>
    </#if>

      <#--<h2>Spectate A Game</h2>-->
      <#--<#if gameList?has_content>-->
          <#--<form action="./spectator/game">-->
              <#--<blockquote>-->
                  <#--<#list gameList?values as v>-->
                  <#--&lt;#&ndash;          <#if spectator??>&ndash;&gt;-->
                  <#--&lt;#&ndash;              <form action="/spectator/game" method="GET">&ndash;&gt;-->
                  <#--&lt;#&ndash;                  <input type="hidden" value="${id}" name="gameID">&ndash;&gt;-->
                  <#--&lt;#&ndash;                  <input type="submit" value="${game.redPlayer.name} vs ${game.whitePlayer.name}">&ndash;&gt;-->
                  <#--&lt;#&ndash;              </form>&ndash;&gt;-->
                  <#--&lt;#&ndash;          <#else>&ndash;&gt;-->
                      <#--<button name="spec_user" type="submit" value=${v}>Game ${v}</button>-->
                  <#--&lt;#&ndash;          </#if>&ndash;&gt;-->
                  <#--</#list>-->
              <#--</blockquote>-->
          <#--</form>-->
      <#--<#else>-->
          <#--<body>No games are currently active.</body>-->
      <#--</#if>-->

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
