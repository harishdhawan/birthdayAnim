/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javaapplication1;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author harish
 */
public class Body {
        Color c;
        double r; //dimensions are in unit square
        double[] v;
        int dimensions;
        double [] position;
        double m;
        
        BufferedImage img;
        
        public Body(int dim){
            dimensions = dim;
            position = new double[dimensions];
            
            double cc = Math.random();
            if(cc<0.25)
                c = Color.BLUE;
            else if(cc<0.5)
                c = Color.GREEN;
            else if(cc<0.75)
                c = Color.GRAY;
            else
                c = Color.RED;
//            c = Color.WHITE;
            //c = new Color(192, 200, 182);
            r = 0.06;
            v = new double[dimensions];
            for(int i = 0; i < v.length; ++i)
                v[i] = 0;
            m = 1;
        }
        
        
        public double getX(){
            return position[0];
        }
        public double getY(){
            return position[1];
        }
        
        public void setX(double xx){
            position[0] = xx;
        }
        public void setY(double yy){
            position[1] = yy;
        }
        public double getVX(){
            return v[0];
        }
        public void setVX(double vv){
            v[0] = vv;
        }
        public double getVY(){
            return v[1];
        }
        public void setVY(double vv){
            v[1] = vv;
        }
        public double getR(){
            return r;
        }
        public void setR(double rr){
            r = rr;
        }
        public void setImage(BufferedImage i){
            img = i;
        }
        public BufferedImage getImage(){
            return img;
        }
    }
