import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Maxwellino extends PApplet {

ArrayList<float[]> cargas= new ArrayList<float[]>();
ArrayList<float[]> cargasaux= new ArrayList<float[]>();
float q=10000.0f,pi=3.141592654f,e=8.854187817f,m=0.1f,aux;
int run=0;
public void setup(){
  background(0xffFFFCFC);
  
  noStroke();
  frameRate(40);
}
public void drawArrow(float cx,float cy,float len,float angle){
  pushMatrix();
  translate(cx,cy);
  rotate(radians(angle));
  line(0,0,len,0);
  line(len,0,len-8,-8);
  line(len,0,len-8,8);
  popMatrix();
}
public void mousePressed(){
  if(mouseButton == LEFT)cargas.add(new float[] {q,mouseX,mouseY,0,0,m});
  else cargas.add(new float[] {-q,mouseX,mouseY,0,0,m});
}
public void keyPressed(){
  if(key=='\n'){
    cargas= new ArrayList<float[]>();
    run=0;
  }
  if(key=='z'&&cargas.size()!=0)cargas.remove(cargas.size()-1);
  if(key=='r'){
    if(run==0)run=1;
    else run=0;
  }
}  
public void draw(){
  clear();
  background(0xffFFFCFC);
  int j,k,i;
  float Ex=0,Ey=0,ang=0;
  fill(0xff080000);
  for(k=1;k<=9;k++)for(j=1;j<=18;j++)ellipse(j*72,k*72, 10,10);
  if(cargas!=null)
  for(k=1;k<=9;k++)for(j=1;j<=18;j++){
    Ex=0;Ey=0;
    for(i=0;i<cargas.size();i++){
       Ex+=(1.0f/(4*pi*e))*cargas.get(i)[0]*(j*72-cargas.get(i)[1])/pow((cargas.get(i)[1]-j*72)*(cargas.get(i)[1]-j*72)+(cargas.get(i)[2]-k*72)*(cargas.get(i)[2]-k*72),1.5f);
       Ey+=(1.0f/(4*pi*e))*cargas.get(i)[0]*(k*72-cargas.get(i)[2])/pow((cargas.get(i)[1]-j*72)*(cargas.get(i)[1]-j*72)+(cargas.get(i)[2]-k*72)*(cargas.get(i)[2]-k*72),1.5f);
    }
    if(!(Ey==Ex&&Ey==0))ang=atan2(Ey,Ex);
    else ang=0;
    if(!(Ey==Ex&&Ey==0)){
      stroke(0);
      strokeWeight(5);
      drawArrow(j*72,k*72,30,180*(ang)/pi+random(-5,5));
      noStroke();
    }
  }
  if(cargas!=null){
  cargasaux=new ArrayList<float[]>();
  for(i=0;i<cargas.size();i++){
    cargasaux.add(new float[6]);
    for(j=0;j<6;j++){
    cargasaux.get(i)[j]=cargas.get(i)[j];
    }
  }
  for(i=0; i<cargas.size();i++){
    Ex=0;Ey=0;
    for(j=0;j<cargas.size();j++){
      if(j!=i){
       Ex+=(1.0f/(4*pi*e))*cargas.get(j)[0]*(cargas.get(i)[1]-cargas.get(j)[1])/pow((cargas.get(j)[1]-cargas.get(i)[1])*(cargas.get(j)[1]-cargas.get(i)[1])+(cargas.get(j)[2]-cargas.get(i)[2])*(cargas.get(j)[2]-cargas.get(i)[2]),1.5f);
       Ey+=(1.0f/(4*pi*e))*cargas.get(j)[0]*(cargas.get(i)[2]-cargas.get(j)[2])/pow((cargas.get(j)[1]-cargas.get(i)[1])*(cargas.get(j)[1]-cargas.get(i)[1])+(cargas.get(j)[2]-cargas.get(i)[2])*(cargas.get(j)[2]-cargas.get(i)[2]),1.5f);
      }
    }
   
    if(cargas.get(i)[0]>0)fill(random(100,255),random(50,70),random(50,70));
    else if(cargas.get(i)[0]==0){
      aux=random(200,255);
      fill(aux,aux,aux);
    }else fill(random(50,70),random(50,70),random(100,255));
    ellipse(cargas.get(i)[1]+random(-5,5),cargas.get(i)[2]+random(-5,5), 100,100);
    if(cargas.get(i)[0]>0)stroke(random(100,255),random(50,70),random(50,70));
    else if(cargas.get(i)[0]==0){
      aux=random(200,255);
      stroke(aux,aux,aux);
    }else stroke(random(50,70),random(50,70),random(100,255));
    if(pow(pow(cargas.get(i)[3],2)+pow(cargas.get(i)[4],2),0.5f)!=0)drawArrow(cargas.get(i)[1],cargas.get(i)[2],100,random(-3,3)+degrees(atan2(cargas.get(i)[4],cargas.get(i)[3])));
    noStroke();
    if(cargas.get(i)[0]==0)fill(0,0,0);
    else fill(255,255,255);
    textSize(13);
    text("v="+String.format("%.1f",pow(pow(cargas.get(i)[3],2)+pow(cargas.get(i)[4],2),0.5f))+"m/s\nq="+String.format("%.1f",cargas.get(i)[0]/1000.0f)+"kC\nm="+String.format("%.1f",cargas.get(i)[5]*1000)+"g",cargas.get(i)[1]-40,cargas.get(i)[2]-20,cargas.get(i)[1]+10,cargas.get(i)[2]+10);
    if(run==1){
      cargasaux.get(i)[1]+=cargas.get(i)[3]*1.0f/20.0f+0.5f*Ex*cargas.get(i)[0]/(400.0f*cargas.get(i)[5]);
      cargasaux.get(i)[2]+=cargas.get(i)[4]*1.0f/20.0f+0.5f*Ey*cargas.get(i)[0]/(400.0f*cargas.get(i)[5]);
      cargasaux.get(i)[3]+=cargas.get(i)[0]*Ex*1.0f/(20.0f*cargas.get(i)[5]);
      cargasaux.get(i)[4]+=cargas.get(i)[0]*Ey*1.0f/(20.0f*cargas.get(i)[5]);
       for(j=0;j<i;j++){
        if(pow(pow(cargas.get(i)[1]-cargas.get(j)[1],2)+pow(cargas.get(i)[2]-cargas.get(j)[2],2),0.5f)<50.0f){
         cargasaux.get(i)[0]=cargas.get(i)[0]+cargas.get(j)[0];
         cargas.get(i)[0]=cargas.get(i)[0]+cargas.get(j)[0];
         cargasaux.get(i)[1]=(cargas.get(i)[1]+cargas.get(j)[1])*0.5f;
         cargasaux.get(i)[2]=(cargas.get(i)[2]+cargas.get(j)[2])*0.5f;
         cargasaux.get(i)[3]=(cargas.get(i)[5]*cargas.get(i)[3]+cargas.get(j)[5]*cargas.get(j)[3])/(cargas.get(i)[5]+cargas.get(j)[5]);
         cargasaux.get(i)[4]=(cargas.get(i)[5]*cargas.get(i)[4]+cargas.get(j)[5]*cargas.get(j)[4])/(cargas.get(i)[5]+cargas.get(j)[5]);
         cargasaux.get(i)[5]=cargas.get(i)[5]+cargas.get(j)[5];
         i--;
         cargasaux.remove(j);
         cargas.remove(j);
         break;
        }
      }
    }
  }
   cargas=new ArrayList<float[]>();
   for(i=0;i<cargasaux.size();i++){
    cargas.add(new float[6]);
    for(j=0;j<6;j++){
    cargas.get(i)[j]=cargasaux.get(i)[j];
    }
  }
  }
}
  public void settings() {  size(displayWidth,displayHeight); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Maxwellino" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
