import processing.net.*;
import java.io.*;
import java.net.*;
import java.util.*;

// for more documentation see https://processing.org/reference/libraries/net/serverEvent_.html
// using tutorial https://processing.org/tutorials/network/

int port = 12345;
Server myServer;
int c; 
//byte[] input;
Client myClient; //Android App



void setup(){
  size(400,400);
  c = 0;
   myServer = new Server(this, port); //processing.net has class "Server" to create ServerSockets
   
  //provide information about server state
  // if server closes, socket exeption is thrown
  if (myServer.active())
  {
    println("server is active"); 
  }
  else {
    println("server is not active");
  }
  
  myServer.run();
  println("server is running");
  
}

void serverEvent(Server someServer, Client someClient) {
  println("We have a new client: " + someClient.ip());
  c = 255; 
 // input = someClient.readBytes();
 // println("Message received" + input);
}

void draw(){
  background(c);
  
  //receive data from client (Android App)
 myClient = myServer.available();
 if (myClient != null){
   println("reading string from client "+ myClient.readString());
 }
 
    //draw circle
    fill(200,0,200);
    circle(width/2, height/2, 10);
  
    
   //unpack data/slice substrings
   //input = input.substring(0, input.indexOf("\n"));  // Only up to the newline
   //data = int(split(input, ' '));  // Split values into an array
   // Draw line using received coords
   // stroke(0);
   // line(data[0], data[1], data[2], data[3]); 
   
}
  


// stop server and disconnect all clients
void mousePressed(){
  myServer.stop();
}
