/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.util;

import org.joml.Vector2d;
import sketchwars.SketchWars;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class Converter {
    public static final float WIDTH_RATIO = SketchWars.PHYSICS_WIDTH/SketchWars.OPENGL_WIDTH;
    public static final float HEIGHT_RATIO = SketchWars.PHYSICS_WIDTH/SketchWars.OPENGL_WIDTH;
    
    
    public static float PhysicsToGraphicsX(double x) {
        return (float) (x/WIDTH_RATIO);
    }
        
    public static float PhysicsToGraphicsY(double y) {
        return (float) (y/HEIGHT_RATIO);
    }
    
    public static Vector2d PhysicsToGraphics(double x, double y) {
        return new Vector2d(PhysicsToGraphicsX(x), PhysicsToGraphicsY(y));
    }
    
    public static int GraphicsToPhysicsX(float x) {
        return (int) (x * WIDTH_RATIO);
    }
    
    public static int GraphicsToPhysicsY(float y) {
        return (int) (y * HEIGHT_RATIO);
    }   
}
