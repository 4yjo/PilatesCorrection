import processing.net.*;
import processing.svg.*;

Server myServer;
String incoming;
String[] coord;
float xValue;
float yValue;
int score = 0; 
int time1 = 2000;
int time2 = 4000;
int time3 = 6000;
PShape rocket;


void setup(){
  //fullScreen();
  size(500,500);
  textAlign(CENTER,CENTER);
  textSize(20);
  //set Window Title
  surface.setTitle("Pilates Correction!");
  surface.setResizable(true);
  rocket = loadShape("rocket.svg");
  
  incoming = "";
  
  myServer = new Server(this, 12345);
}

void draw(){
    background(0);
    fill(255);    
    
    if (millis()<time3){
      countdown();
    }
    else{
      //display score in upper left corner
      text("score: "+score, 50, 10);
    }
      
    
    //display xValues
    text(xValue*100, width-20, height-20);
  
  
  setScore();
  
  Client myClient = myServer.available();
  if(myClient != null){
    incoming = myClient.readString();
    coord = incoming.split(";");
    xValue = abs(float(coord[0])); //abs() -> absolute numbers, set positive
  }
  
  //calc angle for rotation
  float angle= map(mouseX, 0, width, -HALF_PI, HALF_PI); // circle = 2*PI, half circle = 1*PI
  
  //draw rocket
  translate(width/2, height-100); //shift coordinate system for rotation
  rotate(angle);
  shape(rocket, -30, -30, 60,60); //position shape with center at 0,0 to rotate around itself
}

void mousePressed(){
  //myServer.write("HELLO!");
}

void setScore(){
  //calculate Score from time - hollow back
  score = -6 + int(millis()*0.001)-int(xValue); // -6 for countdown time
}

void countdown(){
  //display countdown 3-2-1 on start
  if (millis() < time1){
    text("3", width/2, height/2);
  }
  else if(millis() > time1 && millis()  < time2){
    text("2", width/2, height/2);
  }
  else if(millis() > time2 && millis()  < time3){
    text("1", width/2, height/2);
  }
}
