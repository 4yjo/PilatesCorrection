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
float xCoord;



void setup(){
  size(400,400);
   background(200);
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
 // input = someClient.readBytes();
 // println("Message received" + input);
}

void draw(){
  background(c);
  
  //receive data from client (Android App)
 myClient = myServer.available();
 if (myClient != null){
    println("reading string from client "+ myClient.readString());
    println(myClient.readString());
    //String[] dataArray = split(myClient.readString(), ';');
    String sensordata = myClient.readString();
     println("sensordata: " + sensordata);
   //while (sensordata != null){ //checks if there is data from client
   //println("ENTERED WHILE");
   // xCoord = Float.valueOf(dataArray[0]);
   
   // AAH sensordata is always null >> check for exeptions return null 
   //https://stackoverflow.com/questions/5714102/java-inputstream-nullpointerexception-with-inputstream
   
   
  // xCoord = Float.valueOf(dataArray[0]);
  // println("xCoord: " + xCoord);
  //TODO: make float global and position circle
   
   //TODO: get floats from string
   // like here: https://processing.org/tutorials/network/
   // maybe read https://processing.org/tutorials/data/
   
 }
 else{
  // println("No Client found");
   //TODO: Buffer + show text "waiting for client to connect"
 
    //draw circle
    fill(200,0,200);
    //float turn = map(xCoord, -5,5, 0, width);
    float turn = 200;
   // println("circle Position: " + turn);
    circle(turn, height/2, 50);

}   
}
  


// stop server and disconnect all clients
void mousePressed(){
  myServer.stop();
}
