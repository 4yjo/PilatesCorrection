import processing.net.*;
import processing.svg.*;

Server myServer;
String incoming;
String[] coord;
float xValue;
float yValue;
float angle = 0;
float alpha = 100;
int score; 
int time1 = 2000;
int time2 = 4000;
int time3 = 6000;
int timeRefresh = 0;
boolean looping = false;
boolean crashing;
PShape rocket;
PShape boom;

void setup(){
  //fullScreen();
  size(500,500);
  textAlign(CENTER,CENTER);
  textSize(20);
  //set Window Title
  surface.setTitle("Pilates Correction!");
  surface.setResizable(true);
  rocket = loadShape("rocket.svg"); //make sure graphics are in same folder as this file
  rocket.disableStyle();
  
  boom = loadShape("boom.svg"); //maybe choose other file as "filters are not supported"
  
  incoming = "";
  
  myServer = new Server(this, 12345);

}

void draw(){
  //draw background image
  background(0,0,130);
  noStroke();
  fill(0,0,160);
  triangle(50,height,width/2, -30,width-50,height);
  fill(0,0,190);
  triangle(100,height,width/2, -30,width-100,height);
   
  
    fill(255);    
    
    if (millis()<time3){
      //display countdown
      crashing = false;
      countdown();
    }
    else{
      // set crashing
      crashing = true;
      //display score in upper left corner
      text("score: "+ score, 50, 10);
    }
      
    
    //display xValues or y Values for easier evaluation
    // text(yValue*100, width-20, height-20);
  
  
  setScore();
  
  Client myClient = myServer.available();
  if(myClient != null){
    fill(0,255,0);
    circle(width-10, 10, 10);
    incoming = myClient.readString();
    coord = incoming.split(";");
    xValue = abs(float(coord[0])); //abs() -> absolute numbers, set positive
    yValue = float(coord[1]);
  }
    
  //calc angle for rotation
  // check for NaN first, to omit blinking of the rocket
  if (! Float.isNaN(yValue)){
    angle= map(yValue, -3, 3, -HALF_PI, HALF_PI); // circle = 2*PI, half circle = 1*PI 
    alpha = map(abs(yValue), 0,3, 50,150);
    if ((yValue < -3.1) || (yValue > 3.1)){
      gameOver();
  }
  }
  //draw rocket
  translate(width/2, height-100); //shift coordinate system for rotation
  rotate(angle);
  fill(200,200,0);
  shape(rocket, -50, -50, 100,100); //position shape with center at 0,0 to rotate around itself
  fill(255,0,0,alpha);
  shape(rocket, -50, -50, 100,100); //position shape with center at 0,0 to rotate around itself
}

void setScore(){
  //calculate Score from time - hollow back
  score = - timeRefresh + int(millis()*0.001)-int(xValue); // -6 for countdown time
}

void countdown(){
   if (!looping){
    fill(255);
    text("connect your phone & hit space to start the fun", width/2, height/2);
    noLoop();
   }
  //display countdown 3-2-1 on start
  if ((millis() < time1) && looping){
    text("3", width/2, height/2);
  }
  else if(millis() > time1 && millis()  < time2){
    text("2", width/2, height/2);
  }
  else if(millis() > time2 && millis()  < time3){
    text("1", width/2, height/2);
  }
}
 
void mousePressed(){

  if (dist(mouseX, mouseY, width/2, height/2)<=100){
    time1 += millis();
    time2 += millis();
    time3 += millis();
    timeRefresh = int(millis()*0.001)-6; // 6 for countdown
    looping = true;
    loop();
  }
}

void keyPressed(){
    println ("keypressed");
    time1 += millis();
    time2 += millis();
    time3 += millis();
    timeRefresh = int(millis()*0.001)+6; // 6 for countdown
    looping = true;
    loop();
}

  
void gameOver(){
  if(crashing){
  shape(boom, width/2-150, height-160, 300, 200);
  //create start again button
  fill(255);
  text("GAME OVER!", width/2, height/2-40);
  fill(200,0,0);
  rect(width/2-60,height/2, 120, 30);
  fill(255);
  text("Play again", width/2, height/2+10);
  looping = false;
  noLoop(); 
  }
  // more on starting again: http://processing.flosscience.com/processing-for-android/macul-2012/simple-game-code
}
