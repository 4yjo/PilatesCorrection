import processing.net.*;

// for more documentation see https://processing.org/reference/libraries/net/serverEvent_.html

int port = 12345;
Server myServer;
int c; 

void setup(){
  size(400,400);
  c = 0;
  myServer = new Server(this, port);
}

void serverEvent(Server someServer, Client someClient) {
  println("We have a new client: " + someClient.ip());
  c = 255; 
}

void draw(){
  background(c);
}
