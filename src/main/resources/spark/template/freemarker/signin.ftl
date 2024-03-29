<!DOCTYPE html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
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
            <input type="text" placeholder="Enter Username" name="myUsername" required>

            <button type="submit" >Login</button>

        </form>


    </div>

</div>
</body>

</html>
