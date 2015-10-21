/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class LobbySystem {
 
   private JFrame mainFrame;
   private JLabel headerLabel;
   private JLabel statusLabel;
   private JPanel controlPanel;
   private JPanel myStatus;
   
   //pet stats
   private boolean gameStart = false;
   private boolean petAlive = true;
   
//state
   private boolean petHasToy = false;
   private boolean petSleeping = false;
   
   private int currentFood = 50;
   private int currentEngery = 100;
   private String petStatus = "Waiting";
   Random rand = new Random();
   //private enum petStatusIcon {sleeping,  hungry, waiting};
   // playing,  eating , dead 
    //resources folder should be inside SWING folder.
    final JLabel petStatusIcon = new JLabel();
    ImageIcon iconDead = createImageIcon("/resources/dead.png","Java");
    final ImageIcon iconEat = createImageIcon("/resources/eating.png","Java");
    final ImageIcon iconHungry = createImageIcon("/resources/hungry.png","Java");
    final ImageIcon iconPlay = createImageIcon("/resources/playing.png","Java");
    final ImageIcon iconSleep = createImageIcon("/resources/sleep.png","Java");
    final ImageIcon iconWait = createImageIcon("/resources/waiting.png","Java");

   public LobbySystem(){
      prepareGUI();
   }

   public static void main(String[] args){
      LobbySystem  swingControlDemo = new LobbySystem();      
      swingControlDemo.showButtonDemo();
   }
   
   private void prepareGUI(){
      mainFrame = new JFrame("Java Swing Examples");
      mainFrame.setSize(400,400);
      mainFrame.setLayout(new GridLayout(3, 1));
      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }        
      });
	  
      headerLabel = new JLabel("", JLabel.CENTER);        
      statusLabel = new JLabel("",JLabel.CENTER);    

      statusLabel.setSize(350,100);

      controlPanel = new JPanel();
      myStatus = new JPanel();
      controlPanel.setLayout(new FlowLayout());

      mainFrame.add(headerLabel);
      mainFrame.add(controlPanel);
      mainFrame.add(statusLabel);
      mainFrame.setVisible(true);  
   }
    
   private static ImageIcon createImageIcon(String path, 
      String description) {
      java.net.URL imgURL = LobbySystem.class.getResource(path);
      if (imgURL != null) {
         return new ImageIcon(imgURL, description);
      } else {            
         System.err.println("Couldn't find file: " + path);
         return null;
      }
   }
   /*
    LOGIC !!!

   */
   public void update() throws InterruptedException
   {
       Thread.sleep(3000); 
       int  n = rand.nextInt(3) + 1;
       if(isGameStart() && isPetAlive()== true) // pet alive and game is start
       {
            //Check Engery
            checkEnery(n);
            //Check Health
            checkHealth(n);
            
            //Update Face
            updateFace();
            // Update screen values
            statusLabel.setText("Energy :"+ getCurrentEngery() +"|| Food: "+ getCurrentFood()+ "|| Has Toy: "+isPetHasToy());
            update();
       }
       System.out.println("Game finished: " + getPetStatus()+n);
   }
   
   private void checkHealth(int n)
   {
       if( isPetAlive() && n > 1) // pet alive and game is start
       {
           //
           if(getCurrentFood() > 0)
           {
               setPetStatus("Eating");
               setCurrentFood(getCurrentFood()-5);
           }
           else
           {
               setPetStatus("Dead");
               // close the game pet died
               setGameStart(false);
           }
       }
       else
       {
            setPetStatus("Sleeping");
            setCurrentEngery(100);
       }
       headerLabel.setText("Pet Status: "+ getPetStatus()); 
       updateFace();
   }
   /*
    Non-deterministic, sometimes it will play with toy not always
   */
   private void checkEnery(int n)
   {
       // random chance pet plays with toy
   
        // pet plays with toy sometimes
       if( isPetHasToy() == true && n > 1 && getPetStatus() !="Sleeping")
       {
           if(getCurrentEngery() > 0)
           {
               // playing
               setCurrentEngery(getCurrentEngery()-10);
               setPetStatus("Playing");
           }
           else
           {
               // pet dies
               setPetStatus("Dead");
               // close the game pet died
               setGameStart(false);
           }
       } else {
           setPetStatus("Waiting");
       }
       headerLabel.setText("Pet Status: "+ getPetStatus()); 
       updateFace();
   }
   /*
    Update faces
   */
   public void updateFace()
   {
       if(getPetStatus() == "Sleeping"){petStatusIcon.setIcon(iconSleep);}
       else if(getPetStatus() == "Eating"){ petStatusIcon.setIcon(iconEat);}
       else if(getPetStatus() == "Hungry"){ petStatusIcon.setIcon(iconHungry);}
       else if(getPetStatus() == "Waiting"){ petStatusIcon.setIcon(iconWait);}
       else if(getPetStatus() == "Dead"){ petStatusIcon.setIcon(iconDead);}
       else // playing
       {
           petStatusIcon.setIcon(iconPlay);
       }
   }
   

   private void showButtonDemo(){

      headerLabel.setText("Pet Status: "+ getPetStatus()); 


      
      
      JButton okButton = new JButton("Start");        
      JButton javaButton = new JButton("Feed");
      JButton cancelButton = new JButton("Give toy");
      
      
      cancelButton.setHorizontalTextPosition(SwingConstants.LEFT);   

      //start
      okButton.addActionListener(new ActionListener() 
      {
         public void actionPerformed(ActionEvent e) 
         {
           if(!isGameStart())
           {
             petStatusIcon.setIcon(iconEat);
             setGameStart(true);
             setPetAlive(true);
             //
                Thread thread = new Thread("update Thread created") {
                     public void run(){
                         try {
                             update();
                         } catch (InterruptedException ex) {
                             Logger.getLogger(LobbySystem.class.getName()).log(Level.SEVERE, null, ex);
                         }
                     }
                  };
                thread.start();
                System.out.println(thread.getName());
            //
           }
         }          
      });

      //feed
      javaButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            petStatusIcon.setIcon(iconEat);
            setCurrentFood(getCurrentFood()+20);
            System.out.println("pet Eating..+20 food");
         }
      });

      //give toy
      cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
             if(isPetHasToy() == true)
             {
                 setPetHasToy(false);
                 System.out.println("pet lost the toy");
             }
             else
             {
                 setPetHasToy(true);
                 System.out.println("pet got a toy");
             }
           
           
         }
      });

      controlPanel.add(okButton);
      controlPanel.add(javaButton);
      controlPanel.add(cancelButton);
      //status
      controlPanel.add(petStatusIcon);

      mainFrame.setVisible(true);  
   }

    /**
     * @return the gameStart
     */
    public boolean isGameStart() {
        return gameStart;
    }

    /**
     * @param gameStart the gameStart to set
     */
    public void setGameStart(boolean gameStart) {
        this.gameStart = gameStart;
    }

    /**
     * @return the petAlive
     */
    public boolean isPetAlive() {
        return petAlive;
    }

    /**
     * @param petAlive the petAlive to set
     */
    public void setPetAlive(boolean petAlive) {
        this.petAlive = petAlive;
    }

    /**
     * @return the petHasToy
     */
    public boolean isPetHasToy() {
        return petHasToy;
    }

    /**
     * @param petHasToy the petHasToy to set
     */
    public void setPetHasToy(boolean petHasToy) {
        this.petHasToy = petHasToy;
    }

    /**
     * @return the currentFood
     */
    public int getCurrentFood() {
        return currentFood;
    }

    /**
     * @param currentFood the currentFood to set
     */
    public void setCurrentFood(int currentFood) {
        this.currentFood = currentFood;
    }


    /**
     * @return the petStatusIcon
     */
    public String getPetStatus() {
        return petStatus;
    }

    /**
     * @param petStatus the petStatusIcon to set
     */
    public void setPetStatus(String petStatus) {
        this.petStatus = petStatus;
    }

    /**
     * @return the currentEngery
     */
    public int getCurrentEngery() {
        return currentEngery;
    }

    /**
     * @param currentEngery the currentEngery to set
     */
    public void setCurrentEngery(int currentEngery) {
        this.currentEngery = currentEngery;
    }
}
