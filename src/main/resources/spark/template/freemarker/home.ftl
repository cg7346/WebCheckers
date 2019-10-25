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
        <form action="./game" method="POST">
        <ol>
            <#list userList as user>
                <button name = "name" type="submit" value=${user}>${user}</button>
            </#list>
        </ol>
        </form>
    <#else>
        <h2>${playersOnline}</h2>
        <blockquote>
            <li> ${playerActive}</li>
        </blockquote>
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
