import processing.net.*;
import processing.svg.*;

Server myServer;
String incoming;
String[] coord;
float xValue;
float yValue;
float angle = 0;
float alpha = 100;
float score; 
int time1 = 2000;
int time2 = 4000;
int time3 = 6000;
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
      text("score: "+ int(score), 50, 10);
    
      if (second()%10==0){
      //give instructions every 10 seconds
        if (second()%20==0){
        //lift left arm
        fill(200,0,200, 150);
        circle(100,150,100);
        fill(255,150);
        text("left arm", 100,150);
        }
  
        else {
        fill(200,0,200, 150);
        circle(width-100,150,100);
        fill(255,150);
        text("right arm", width-100,150);
        }
    }
    }
  
  Client myClient = myServer.available();
  if(myClient != null){
    fill(0,255,0);
    circle(width-10, 10, 10);
    incoming = myClient.readString();
    coord = incoming.split(";");
    xValue = abs(float(coord[0])); //abs() -> absolute numbers, set positive
    yValue = float(coord[1]);
  }
    
  
  if (xValue > 1.5){
    fill (255,0,0);
    //display text to warn about hollow or round back
    text("straighten your back!", width/2-100, height- 200);
  }
  else if (xValue <= 1.5){
    // rise score if backposition is stable
    score += 0.015; //equals approx 1 whole point per second as 60 fps
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
 

void keyPressed(){
    println ("keypressed");
    time1 += millis();
    time2 += millis();
    time3 += millis();
    looping = true;
    loop();
}

  
void gameOver(){
  if(crashing){
  shape(boom, width/2-150, height-160, 300, 200); 
  fill(255);
  text("score: "+ int(score), width/2, height/2-20);
  text("GAME OVER!", width/2, height/2-40);
  looping = false;
  noLoop(); 
  }
}
