<!DOCTYPE html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <meta http-equiv="refresh" content="10">
    <title>Web Checkers | ${title}</title>
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

            <form action="./signin" method="POST">

                <label for="myUsername"><b>Username</b></label>
                <input type="text" placeholder="Enter Username" name="myUserName" required>

                <label for="psw"><b>Password</b></label>
                <input type="password" placeholder="Enter Password" name="psw" required>

                <button type="submit">Login</button>

            </form>

        </div>

    </div>
</body>

</html>
