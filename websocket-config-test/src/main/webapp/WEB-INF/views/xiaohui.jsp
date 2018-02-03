<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>xiaohui</title>
    <script src="plugins/js/sockjs.js"></script>
    <script src="plugins/js/stomp.js"></script>
</head>
<body>

<script type="text/javascript">
    var socket = new SockJS("/portfolio");
    var stompClient = Stomp.over(socket);


    var userId = "xiaohui";

    var headers = {
        login: userId,
        passcode: '123456',
        // additional header
        'client-id': 'xiaohui-websocket'
    };
    stompClient.connect(headers, function (frame) {

        stompClient.send("/app/chat", {priority: 9},
            '{' +
            '    "name" : "wuhulala",' +
            '    "destination" : "/chat",' +
            '    "messageBody" : "hello, wuhulala !!!"' +
            '}'
        );

        stompClient.subscribe("/topic/greetings", function (message) {

            console.log(userId);
            stompClient.subscribe('/user/' + userId + '/time', function (message) {
                console.log(message);
            });
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