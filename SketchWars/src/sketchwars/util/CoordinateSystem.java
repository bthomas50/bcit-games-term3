/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.util;

import org.joml.Vector2d;
import sketchwars.OpenGL;
import sketchwars.physics.Vectors;

/**
 * Used to convert between the physics and OpenGL coordinate system
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class CoordinateSystem {
    /**
     * Convert physics position to OpenGL draw position
     * @param vector
     * @return 
     */
    public static Vector2d physicsToOpenGL(long vector) {
        double xComp = ((Vectors.xComp(vector) / 1024) * OpenGL.WIDTH/2);
        double yComp = ((Vectors.yComp(vector) / 1024) * OpenGL.HEIGHT/2);
        
        return new Vector2d(xComp, yComp);
    }
}
