if (window.console) {
  console.log("Welcome to your Play application's JavaScript!");
}

$(function() {

    $('#formChat').submit(function(event){
        event.preventDefault();
        var msg = $('#message');
        send(msg.val());
        msg.val("");
    });

    var mysocket = new WebSocket("ws://localhost:9000/socket");

    mysocket.onopen = function (evt){
       console.log("\tWebsocket abierto");
    };

    mysocket.onmessage = function (evt){
      console.log("\tRecibido: " + evt.data);
      var msg = JSON.parse(evt.data).msg;
      $.amaran({
          content:{
              bgcolor:'#2d3037',
              color:'#fff',
              message: msg
             },
          theme:'colorful'
      });
    };

    mysocket.onclose = function (evt){
      console.log("\tWebsocket cerrado");
    };

    mysocket.onerror = function (evt) {
      console.log("\tError Websocket: " + evt.data);
    }

    function send(texto) {
      mysocket.send(JSON.stringify({msg: texto}));
      console.log("\tEnviado: " + texto);
    }


    function disconnect(){
      mysocket.close();
    }

});



