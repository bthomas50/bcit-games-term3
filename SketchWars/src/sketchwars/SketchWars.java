/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars;

import sketchwars.graphics.Graphics;

/**
 * The SketchWars main class
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class SketchWars {
    private OpenGL openGL;
    private Graphics graphics;
    private World world;
    
    private void init() {
       graphics = new Graphics();
       graphics.init();
       
       openGL = new OpenGL(this, graphics);
       openGL.init();
               
       world = new World(graphics);
       world.init();
    }
    
    private void start() {
        openGL.run();
    }
    
    public void update(double elapsed) {
        world.update(elapsed);
    }
    
    public static void main(String[] args) {
        SketchWars sketchWars = new SketchWars();        
        sketchWars.init();
        sketchWars.start();
    }

    public void dispose() {
        world.dispose();
    }
}
