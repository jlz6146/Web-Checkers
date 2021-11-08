<!DOCTYPE html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Web Checkers sign in</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

    <h1>Web Checkers Sign in</h1>

    <!-- Provide a navigation bar -->
    <#include "nav-bar.ftl" />

    <div class="body">
        <#if message??>
            <div id="message" >
                <#include "message.ftl" />
            </div>
        </#if>
        <form id="name" action="/signin" method="POST">
            <label for="name">Enter a name:</label><br>
            <input type="text" id="name" name="name" ><br>
            <input type="submit" value="Submit">
        </form>
    </div>

</div>
</body>
