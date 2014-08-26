package javaapplication1;

import java.applet.*;
import java.awt.*;
//import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import javax.imageio.ImageIO;

public class Mymain extends Applet {
    
   private class Rect{
       double x,y,h,w;
       private Rect(){
           x=y=h=w=0;
       }
       private Rect(double xx, double yy, double hh, double ww){
           x=xx;
           y=yy;
           h=hh;
           w=ww;
       }
       private void print(){
           System.out.println("" + x + " " + y+ " " + w + " " + h);
       }
   }
   private class Rects{
       Rect [] rects;
       int len;
       Rect boundingRect;
       private Rects(int n){
           len = 0;
           rects = new Rect[n];
           boundingRect = new Rect(1,1,-1,-1);
       }
       private void addRect(Rect r){
           ++len;
           
           //Inc size if necessary
           if(len > rects.length){
               Rect [] rcts = new Rect[len + 10];
               //copy
               for(int i = 0; i < rects.length; ++i){
                   rcts[i] = rects[i];
               }
               rects = rcts;//start using new array
           }
           rects[len-1] = r;
           
           //update the bounding rect;
           if(r.x<boundingRect.x){
               boundingRect.w += boundingRect.x - r.x;
               boundingRect.x = r.x;
           }
           if(r.x+r.w>boundingRect.x+boundingRect.w)//right edge
                boundingRect.w += (r.x+r.w) - (boundingRect.x+boundingRect.w);
           if(r.y<boundingRect.y){
               boundingRect.h += boundingRect.y - r.y;
               boundingRect.y = r.y;
           }
           if(r.y+r.h>boundingRect.y+boundingRect.h)
                boundingRect.h += (r.y+r.h) - (boundingRect.y+boundingRect.h);
       }
       private void clear(){
           len = 0;
           boundingRect.x = boundingRect.y = 1;
           boundingRect.h = boundingRect.w = -1;
       }
       private Rect getBoundingRect(){
           return boundingRect;
       }
   }
   
   class DepthComparator implements Comparator{

        @Override
        public int compare(Object o1, Object o2) {
            if(dimensions<3)
                return 0;
            if(o1==o2)
                return 0;
            
            Body b1 = (Body)o1;
            Body b2 = (Body)o2;
            if (b1.position[2]==b2.position[2])
                return 0;
            return (b1.position[2]-b2.position[2])>0?-1:1;
        }
       
   }
   
   Rects displayedRects;
   int size = 800;
   int dimensions = 3;
   BufferedImage off_Image;
   ArrayList<BufferedImage> bkImages;
   BufferedImage bubbleImage, threeImage,  bkImage, babyImage, momImage, dadImage, heartImage;
   Graphics2D g2;
   double xo, yo, zo; //observer location
   CollisionSimulator sim;
   int bkImageIndex, bubbleCount, heartCount, threeCount;
   public Mymain() throws IOException
   {
       
       sim = new CollisionSimulator(dimensions);
       
       off_Image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        g2 = off_Image.createGraphics();
        
        //observer location
        xo = 0.3;
        yo = 0.9;
        zo = -1.5; 
        //screen is the front facing side of the unit box
        
        String [] bkImageFiles = {
            "birthdaycard.jpg",
            "birthdaycandles.png",
            "chery.jpg",
            "since1984.png",
            "ohno.jpg",
            "worried.jpg",
            "anothercandle.gif",
            "anothercandle2.gif",//for more time to read the contents
            "lookgood.png",
            "babybk.jpg",
            "rosesarered.jpg"
        };
        
        bkImages = new ArrayList<>();
        for( String name : bkImageFiles){
            File bkFile = new File("javaapplication1\\" + name);
            bkImages.add(ImageIO.read(bkFile));
        }
        
        bkImageIndex = 0;
        bkImage = bkImages.get(bkImageIndex);
        
        File bubbleImageFile = new File("javaapplication1\\bubble.png");
        bubbleImage = ImageIO.read(bubbleImageFile);
        bubbleCount = 0;
        
        File threeImageFile = new File("javaapplication1\\three.png");
        threeImage = ImageIO.read(threeImageFile);
        threeCount = 0;
       
        File heartImageFile = new File("javaapplication1\\heart.png");
        heartImage = ImageIO.read(heartImageFile);
        heartCount = 0;
        
        File momImageFile = new File("javaapplication1\\mom.png");
        momImage = ImageIO.read(momImageFile);
        
        momImage = babyImage = dadImage = heartImage;
        
        for (int i = 0; i<1; ++i){
            addBubble();
        }
   }
   
   private Body createNewBody(){
       Body b = new Body(dimensions);
        
        //Set body parameters
//        for(int dim = 0; dim < dimensions; ++dim){
//            if(dim<2)
//                b.position[dim] = Math.random();
//            else
//                b.position[dim] = 1.0;
//        }
        b.position[0] = b.position[1] = b.position[2] = b.getR();

        for(int dim = 0; dim < dimensions; ++dim){
            //adjust so that the body is inside unit box
            if ( b.position[dim] + b.getR() > 1)
                b.position[dim] = 1-b.getR();
            else if( b.position[dim] - b.getR() < 0)
                b.position[dim] = 0+b.getR();

            //Set initial velocity
            b.v[dim] = (Math.random())/400;
        }
        
        b.r = 0.04;
        return b;
   }
   private void addBubble(){
       Body b = createNewBody();
       b.setImage(bubbleImage);
       b.m /= 1.5;
       b.m /= 1.5;
       b.v[2] = 0;
       b.v[0] = -b.v[0];
       sim.addBody(b);
       bubbleCount++;
   }
   
   private void addThree(){
       Body b = createNewBody();
       b.setImage(threeImage);
       b.m /= 1.5;
       b.v[2] = 0;
       b.v[0] = -b.v[0];
       sim.addBody(b);
       threeCount++;
   }
   
   private void addHeart(){
       Body b = createNewBody();
       b.setImage(heartImage);
       b.position[0] = 1-b.getR();
       b.m *= 1.5;
       b.v[0] = -b.v[0];
       sim.addBody(b);
       heartCount++;
   }
   
   private void addBaby(){
       Body b = createNewBody();
       b.setImage(babyImage);
        sim.addBody(b);
   }
   
   private void addMom(){
       Body b = createNewBody();
       b.setImage(momImage);
        sim.addBody(b);
   }
   
   private void addDad(){
       Body b = createNewBody();
       b.setImage(dadImage);
       sim.addBody(b);
   }
   
   @Override
   public void paint(Graphics g) 
   {
          
      ArrayList<Body> bodies;
      displayedRects = new Rects(sim.getNumBodies());
      int count = 0;
      //observer speed;
      double vox, voy;
      vox = 0.000;
      voy = 0.000;
      
       while(true){
//        try{Thread.sleep(1);}
//        catch(InterruptedException e){break;}//Doesnt work???
           
        switch(count){
            case 1000:
                //addBaby();
                addHeart();
                break;
            case 2000:
                //addMom();
                break;
            case 4000:
                //addDad();
                break;
        }
        ++count;
        
        if(bubbleCount<30 && (count%39)==0)
            addBubble();
//        if(threeCount<30 && (count%39)==0)
//            addThree();
        
        if(heartCount<30 && ((count+107)%41)==0)
            addHeart();
        if(count == 100)
        {
            try{
                JavaAudioPlayer.playInLoop("javaapplication1\\Happy_Birthday.mp3");
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        
        if((count % 1000) == 0){
            if(bkImageIndex==bkImages.size()-1)
                bkImageIndex = 0;
            else
                ++bkImageIndex;
            
            bkImage = bkImages.get(bkImageIndex);
        }
        bodies = sim.getBodies();
          
        //move observer
        xo += vox;
        if(xo<0 || xo>1){
            vox = -vox;
            xo += 2*vox;
        }      
        yo += voy;
        if(yo<0||yo>1){
            voy = -voy;
            yo += 2*voy;
        }
              
        displayBodies(g,bodies);
       }
   }
   
   private void clean(Graphics g){
       
       if(true){
           g.setColor(Color.PINK);
            g.fillRect((int)(0), (int)(0), (int)(size), (int)(size));
       }
       //Clean the bounding box 
       else if(true) //This one is faster
       {
           Rect r = displayedRects.getBoundingRect();
            //r.print();
           g.setColor(Color.WHITE);
            g.fillRect((int)(size*r.x), (int)(size*r.y), (int)(size*(r.w)), (int)(size*(r.h)));
            //g.drawRect((int)(size*r.x), (int)(size*r.y), (int)(size*(r.x+r.w)), (int)(size*(r.y+r.h)));
       }
       //clean individual bodyboxes
       else{
           for(int i = 0; i<displayedRects.len; ++i){
               Rect r = displayedRects.rects[i];
               g.clearRect((int)(size*r.x), (int)(size*r.y), (int)(size*(r.w)), (int)(size*(r.h)));
           }
       }
       displayedRects.clear();
   }
   
   private void translatePosition(double x, double y, double z, double [] pos)
   {
       double dist_factor;
       if(dimensions>2){
            dist_factor = (zo)/(zo-z/*unit box*/);
            
        }else{
           dist_factor = 1;
        }
        pos[0] = dist_factor * (x-xo) + xo;
        pos[1] = dist_factor * (y-yo) + yo;
   }
   
   private double translateSize(double x, double y, double z, double s)
   {
       double dist_factor;
       if(dimensions>2){
            dist_factor = (zo)/(zo-z/*unit box*/);
            
        }else{
           dist_factor = 1;
        }
        return dist_factor * (s);
   }
   
   public void drawBodies(Graphics g, ArrayList<Body> bodies){
       clean(g);
       
       //draw image on the backside of the unit box
       //get the perceived dimensions of the back wall
       double [] bk0 = new double[2];//x0=0, bkx1=1, bky0=0, bky1=1;
       double [] bk1 = new double[2];
       translatePosition(0.0, 0.0, 1.0, bk0);
       translatePosition(1.0, 1.0, 1.0, bk1);
       
       g.drawImage(bkImage, (int)(bk0[0]*size), (int)(bk0[1]*size), (int)(bk1[0]*size), (int)(bk1[1]*size), 
               0,0,bkImage.getWidth(), bkImage.getHeight(),
               null);
       
       //draw lines to indicate wall boundaries
       g.setColor(Color.red);
       g.drawLine(0, 0, (int)(bk0[0]*size), (int)(bk0[1]*size));
       g.drawLine(0, size, (int)(bk0[0]*size), (int)(bk1[1]*size));
       g.drawLine(size, 0, (int)(bk1[0]*size), (int)(bk0[1]*size));
       g.drawLine(size, size, (int)(bk1[0]*size), (int)(bk1[1]*size));
       g.drawRect(0, 0, size-1, size-1);
       
       //sort according the z axis
       Arrays.sort(bodies.toArray(), new DepthComparator());
       for (Body b : bodies){
           displayBody(g,b);
       }
           
   }
   
   public void displayBody(Graphics g, Body b){
        double visibleRadius = b.r;
        double [] pos = new double[2];
        translatePosition(b.position[0], b.position[1], b.position[2], pos);
        visibleRadius = translateSize(b.position[0], b.position[1], b.position[2], b.r);
        g.drawImage(b.getImage(), 
                (int)((pos[0]-visibleRadius)*size), (int)((pos[1]-visibleRadius)*size), (int)((pos[0]+visibleRadius)*size), (int)((pos[1]+visibleRadius)*size), 
                0,0,b.getImage().getWidth(), b.getImage().getHeight(), null);
        displayedRects.addRect(new Rect(pos[0]-visibleRadius,
                            pos[1]-visibleRadius,
                            pos[0]+visibleRadius,
                            pos[1]+visibleRadius));
       
   }
   public void displayBodies(Graphics g, ArrayList<Body> bodies){
 
        drawBodies(g2, bodies);
        g.drawImage(off_Image, 0, 0, this);
   }
}