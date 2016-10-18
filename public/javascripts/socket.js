
$(function() {

    var user = ManagerCookies.getCookie("userSocket");
    if (!user) {
        user = prompt("Usuario:");
        if (!user === "") {
            Math.floor((Math.random() * 100) + 1);
            ManagerCookies.setCookie("userSocket", user, 365);
        }
    }

    /**
      * Inicio socket
      */
    var myChat = chat( user );

    myChat.on("chat", function(msg){
        console.log(msg);
        $.amaran({
          content:{
              bgcolor:'#2d3037',
              color:'#fff',
              message: msg.message
             },
          theme:'colorful'
        });
    });


    $('#formChat').submit(function(event){
        event.preventDefault();
        var msg = $('#message');
        var text = msg.val()
        msg.val("");

        var json = {
           event: "chat",
           message: {
               message: text,
               lorem: "Lorem ipsum"
           }
        }
        myChat.emit(JSON.stringify(json));

    });

    function disconnect(){
      myChat.close();
    }

});





/**
  * Chat
  */

(function( global ) {

    function Chat( user ) {
        this.user = user;
        this.socket = null;
        this.events = [];
        this.init();
        return this;
    }

    Chat.prototype.init = function() {
    	var url = "ws://localhost:9000/socket?id=" + this.user
        this.socket = new WebSocket(url);
        var self = this;

        this.socket.onopen = function (evt){
	       console.log("\tWebsocket abierto");
	    };

	    this.socket.onclose = function (evt){
	      console.log("\tWebsocket cerrado");
	    };

	    this.socket.onerror = function (evt) {
	      console.log("\tError Websocket: " + evt.data);
	    }

	    this.socket.onmessage = function (evt){
            var msg = JSON.parse(evt.data)
            var event = msg.event;

            for (e in self.events) {
            	if(self.events[e].name === event)
            	   self.events[e].fn(msg.message)
            }

	    };

    };

    Chat.prototype.getSocket = function( val ) {
        return this.socket;
    };

    Chat.prototype.setSocket = function( val ) {
        return ( this.socket = val );
    };

    Chat.prototype.emit = function( msg ) {
        this.socket.send(msg);
    };

    Chat.prototype.close = function() {
        this.socket.close();
    };

    Chat.prototype.on = function(event, fn) {
        var obj = {
            name: event,
            fn: fn
        }
        this.events.push(obj);
    };

    /**
      * Constructor
      */
    var chat = function( user ) {
        return new Chat( user );
    };

    global.chat = chat;

})( this );





/**
  * Módulo manejo de cookies
  */

(function( global ) {
    var ManagerCookies = (function() {
        return {
			getCookie: function(cname) {
			    var name = cname + "=";
			    var ca = document.cookie.split(';');
			    for(var i = 0; i < ca.length; i++) {
			        var c = ca[i];
			        while (c.charAt(0) == ' ') {
			            c = c.substring(1);
			        }
			        if (c.indexOf(name) == 0) {
			            return c.substring(name.length, c.length);
			        }
			    }
			    return "";
			},
			setCookie: function(cname,cvalue,exdays) {
			    var d = new Date();
			    d.setTime(d.getTime() + (exdays*24*60*60*1000));
			    var expires = "expires=" + d.toGMTString();
			    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
			}
        };
    })();

    global.ManagerCookies = ManagerCookies;

})( this );