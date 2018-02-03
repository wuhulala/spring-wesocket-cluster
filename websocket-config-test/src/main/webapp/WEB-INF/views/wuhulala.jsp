<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>wuhulala</title>
    <script src="plugins/js/sockjs.js"></script>
    <script src="plugins/js/stomp.js"></script>
</head>
<body>

<script type="text/javascript">
    var socket = new SockJS("/portfolio");
    var stompClient = Stomp.over(socket);

    var userId = "wuhulala";

    var headers = {
        login: 'wuhulala',
        passcode: '123456',
        // additional header
        'client-id': 'wuhulala-websocket'
    };
    stompClient.connect(headers, function(frame) {

        stompClient.subscribe('/user/' + userId + '/chat',function(message){
            console.log(message);
        });

        stompClient.subscribe("/topic/greetings", function(message){

            console.log(userId);

//            stompClient.send("/app/time", {priority: 9}, "i want");
//            stompClient.send("/app/time", {priority: 9}, "i want");
//            stompClient.send("/app/time", {priority: 9}, "i want");
//            stompClient.send("/app/time", {priority: 9}, "i want");

        });



        // 打招呼
//        stompClient.send("/app/greeting", {priority: 9}, "Hello, STOMP");


    });
</script>

</body>
</html>