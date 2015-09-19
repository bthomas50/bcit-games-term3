/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars;

import sketchwars.graphics.Graphics;
import sketchwars.graphics.map.TestMapClass;

/**
 * The SketchWars main class
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class SketchWars {
    private OpenGL openGL;
    private Graphics graphics;
    
    private void init() {
       graphics = new Graphics();
       graphics.init();
       
       openGL = new OpenGL(this, graphics);
       openGL.init();
               
       TestMapClass test = new TestMapClass();
       graphics.AddDrwableObject(test);
    }
    
    private void start() {
        openGL.run();
    }
    
    public void update(double elapsed) {
        
    }
    
    public static void main(String[] args) {
        SketchWars sketchWars = new SketchWars();        
        sketchWars.init();
        sketchWars.start();
    }

    
}
