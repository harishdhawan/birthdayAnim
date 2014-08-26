/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javaapplication1;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.*;
import java.net.MalformedURLException;
import sun.audio.*;


 
/**
 * A simple Java sound file example (i.e., Java code to play a sound file).
 * AudioStream and JavaAudioPlayer code comes from a javaworld.com example.
 * @author alvin alexander, devdaily.com.
 */
public class JavaAudioPlayer
{
  
  public static void playInLoop(String file){
      AudioClip ac;
      System.out.println("playingcc...");
      try{
        ac = Applet.newAudioClip(new File(file).toURI().toURL());
      }
      catch(MalformedURLException e){
          System.out.println("" + e.getMessage());
          return;
      }
      
      try{
          System.out.println("playing...");
        ac.loop();
        System.out.println("done.");
      }
      catch(Exception e){
          System.out.println("" + e.getMessage());
          throw e;
      }
  }
}