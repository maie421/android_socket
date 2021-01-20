const express = require('express'),
http = require('http'),
app = express(),
server = http.createServer(app),
io = require('socket.io').listen(server);

app.get('/', (req, res) => {
res.send('Chat Server is running on port 3000')
});

let numUsers = 0;

io.on('connection', (socket) => {

    console.log('user connected')

    socket.on('add user', (username) =>{
        console.log(username +" : has joined the chat");

        socket.username = username;
        ++numUsers;

        socket.emit('login', {
            numUsers: numUsers
        });

        socket.broadcast.emit('user joined', {
            username: socket.username,
            numUsers: numUsers
          });
    });


    socket.on('messagedetection', (senderNickname,messageContent) => {
       
       //log the message in console 
       console.log(senderNickname+" :" +messageContent)
        //create a message object 
       let  message = {"message":messageContent, "senderNickname":senderNickname}
          // send the message to the client side  
       io.emit('message', message );
     
      });
      
  
    socket.on('disconnect', function() {
        console.log( 'user has left ')
        socket.broadcast.emit("userdisconnect"," user has left ") 
    });

});





server.listen(3000,()=>{

console.log('Node app is running on port 3000');

});
