/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javaapplication1;

import java.util.ArrayList;


/**
 *
 * @author harish
 */
public class CollisionSimulator {
    
    ArrayList<Body> bodies;
    int dimensions;
    
    public CollisionSimulator(int dim){    
        dimensions = dim;
        bodies = new ArrayList<>();
    }
    
    public int getNumBodies(){
        return bodies.size();
    }
    
    public void addBody(Body b){
        bodies.add(b);
    }
    
    private boolean bodiesCollide(Body b1, Body b2){
        double [] d2 = new double[dimensions];
        for(int dim = 0; dim < dimensions; ++dim){
            d2[dim] = (b1.position[dim]-b2.position[dim]) * (b1.position[dim]-b2.position[dim]);
        }
        double sigma = b1.r+b2.r;
        //calc distance square between 2 bodies
        double dist2 = 0;
        for ( int i = 0; i<dimensions; ++i){
            dist2 += d2[i];
        }
        
        //have the balls crossed each other's boundaries?
        if (sigma*sigma >= dist2){
            //if the balls are moving towards each other, then they are colliding
            //calc relative velocities for all dimensions
            double [] dv = new double[dimensions];
            for(int i = 0; i<dimensions; ++i){
                dv[i] = b2.v[i] - b1.v[i];
            }
            
            //calc distance bw 2 balls
            double [] d = new double[dimensions];
            for(int dim = 0; dim < dimensions; ++dim){
                d[dim] = b2.position[dim] - b1.position[dim];
            }
            //RateOfIncOfGap between 2 balls = (dvx2 + dvy2) + 2*(dvx*dx + dvy*dy)
            //extend this to multi-dimension
            double RateOfIncOfGap_part1 = 0;
            for(int dim =0; dim<dimensions; ++dim){
                RateOfIncOfGap_part1 += dv[dim]*dv[dim];
            }
            
            double RateOfIncOfGap_part2 = 0;
            for(int dim =0; dim<dimensions; ++dim){
                RateOfIncOfGap_part2 += dv[dim]*d[dim];
            }
            
            double rateOfIncOfGap = RateOfIncOfGap_part1 + 2*(RateOfIncOfGap_part2);
            if(rateOfIncOfGap < 0)
                return true;
        }
        return false;
    }
    private boolean detectAndHandleCollisionWithOtherParticles(Body b, boolean[] processed){
        boolean collided = false;
        
        for(int i = 0; i<bodies.size(); ++i){
            if (processed[i] == true)
                continue;
            
            boolean selfCollision = true;
            for(int j = 0; j<dimensions; ++j){
                if(b.position[j] != bodies.get(i).position[j])
                    selfCollision = false;
            }
            if(selfCollision)
                   continue;
            
            if(bodiesCollide(bodies.get(i),b))
            {
                double [] dr = new double[dimensions];
                for(int dim = 0; dim<dimensions; ++dim){
                    dr[dim] = bodies.get(i).position[dim] - b.position[dim];
                }
                
                double [] dv = new double[dimensions];
                for(int dim = 0; dim<dimensions; ++dim){
                    dv[dim] = bodies.get(i).v[dim] - b.v[dim];
                }
                
                double dvdr = 0;
                for(int dim = 0; dim<dimensions; ++dim){
                    dvdr += dv[dim]*dr[dim];
                }
                
                double sigma = 0;
                for(int dim = 0; dim<dimensions; ++dim){
                    sigma += dr[dim]*dr[dim];
                }
                sigma = Math.sqrt(sigma);
                
                double F = 2*b.m*bodies.get(i).m*dvdr/((b.m+bodies.get(i).m)*sigma);
                double [] f = new double[dimensions];
                for(int dim = 0; dim<dimensions; ++dim){
                    f[dim] = F * dr[dim] / sigma;
                }
                                
                //Calc new velocities 
                for(int dim = 0; dim<dimensions; ++dim){
                    b.v[dim] += f[dim]/b.m;
                    bodies.get(i).v[dim] += -f[dim]/bodies.get(i).m;
                }
                processed[i] = true; //mark this body as processed
                collided = true;
                
                //exchange color
                /*Color c = bodies.get(i).c;
                bodies.get(i).c = b.c;
                b.c = c;*/

                break;//simple case - no multiple collisions for a particle
            }
        }
        return collided;
    }
    private boolean detectAndHandleCollisionWithWalls(Body b){
        double x,y;
        double dx=0,dy=0;//distance travelled beyond the physical boundaries
        double d=0;
        boolean collided = false;
        x = b.position[0];
        y = b.position[1];
        
        for(int i =0; i<dimensions; ++i){
            //check lower bound
            if ( b.position[i] + b.getR() >= 1 ){
                b.v[i] = -1 * b.v[i];
                d = b.position[i] + b.getR() - 1;
                collided = true;
            }else if(b.position[i] - b.getR() <= 0){
                b.v[i] = -1 * b.v[i];
                d = b.position[i] - b.getR();
                collided = true;
            }
            if(collided){
                b.position[i] -= d;
                break;//collision with one wall only  - for simplicity
            }
        }
        return collided;
    }
    
    public ArrayList<Body> getBodies(){
        double dt = 1;
        double d;
        //Advance the positions
        for (int i = 0; i<bodies.size(); ++i){
            for(int j = 0; j < dimensions; ++j){
                d = bodies.get(i).position[j] + bodies.get(i).v[j] * dt;
                bodies.get(i).position[j] = d;
            }
        }
        //Now handle collisions
        boolean[] processed = new boolean[bodies.size()];
        for(int i = 0 ; i<processed.length; ++i)
            processed[i] = false;
        for (int i = 0; i<bodies.size(); ++i){
            if(!detectAndHandleCollisionWithWalls(bodies.get(i))){
                //detect and handle particle collisions
                if (detectAndHandleCollisionWithOtherParticles(bodies.get(i), processed) == true){
//                    System.out.println("collision");
                    
                }
                processed[i] = true;
            }
        }
        return bodies;
    }
}
