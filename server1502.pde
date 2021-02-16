import processing.net.*;

Server myServer;
String incoming;
String[] coord;
float xValue;

void setup(){
  size(300,300);
  textAlign(CENTER,CENTER);
  textSize(20);
  
  incoming = "";
  
  myServer = new Server(this, 12345);
}

void draw(){
  background(0);
  fill(255);
  text(xValue*100, width-20, height-20);
  circle(width/2, height/2, xValue*10);
  
  Client myClient = myServer.available();
  if(myClient != null){
    incoming = myClient.readString();
    coord = incoming.split(";");
    xValue = float(coord[0]);
  }
}

void mousePressed(){
  myServer.write("HELLO!");
}
