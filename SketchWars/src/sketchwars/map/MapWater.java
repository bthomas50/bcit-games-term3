/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.map;

import org.joml.Matrix3d;
import org.lwjgl.opengl.GL20;
import sketchwars.character.SketchCharacter;
import sketchwars.game.GameObject;
import sketchwars.graphics.GraphicsObject;
import sketchwars.graphics.Shader;
import sketchwars.graphics.Texture;
import sketchwars.physics.BoundingBox;
import sketchwars.physics.Collider;
import sketchwars.physics.CollisionListener;
import sketchwars.physics.Vectors;
import sketchwars.util.Converter;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class MapWater implements GameObject, GraphicsObject, CollisionListener {
    private static final float RISING_SPEED = 0.0001f;
    private static final float RISING_STEP_DIST = 2f;
    
    private static final float ANGLE_MAX = (float)Math.PI*2;
    private static final float Y_OFFSET = -0.01f;
    private static final float WAVE_SPEED = 0.01f;
    private static final float WAVE_AMPLITUDE = 0.005f;
    private static final float WAVE_LENGTH = 80f;
    
    private final Collider collider;
    private final Texture waterTexture;
    private final Shader shader;
    
    private float waveAngle;
    
    private boolean riseWaterLevel;
    private float distanceRisen;
    
    public MapWater(Texture waterTexture, Shader shader, Collider collider) {
        this.collider = collider;
        this.waterTexture = waterTexture;
        this.shader = shader;
    }
    
    @Override
    public void update(double delta) {
        if (shader != null) { 
            waveAngle += WAVE_SPEED * delta;
            
            if (waveAngle > ANGLE_MAX) {
                waveAngle -= ANGLE_MAX;
            }
        }
        
        handleRisingWaterLevel(delta);
    }
    
    public void riseWaterLevel() {
        if (!riseWaterLevel) {
            distanceRisen = 0;
        }
        riseWaterLevel = true;
    }
    
    public boolean isWaterLevelRising() {
        return riseWaterLevel;
    }
    

    @Override
    public boolean hasExpired() {
        return false;
    }

    @Override
    public void render() {
        if (waterTexture != null && shader != null && shader.isValid()) {
            Matrix3d matrix = new Matrix3d();

            BoundingBox bounds = collider.getBounds();
            long center = bounds.getCenterVector();
            float x = Converter.PhysicsToGraphicsX(Vectors.xComp(center));
            float y = Converter.PhysicsToGraphicsY(Vectors.yComp(center));
            float w = Converter.PhysicsToGraphicsX(bounds.getWidth());
            float h = Converter.PhysicsToGraphicsY(bounds.getHeight());
            
            matrix.translation(x, y + 0.2);
            matrix.scale(w, h, 1);            
            
            shader.begin();
            setShaderVariables();
            waterTexture.draw(matrix);
            shader.end();
            
            
        }
    }

    private void setShaderVariables() {
        int waveAngleLoc = GL20.glGetUniformLocation(shader.getProgram(), "wave_angle");
        GL20.glUniform1f(waveAngleLoc, waveAngle); 

        int waveLengthLoc = GL20.glGetUniformLocation(shader.getProgram(), "wave_length");
        GL20.glUniform1f(waveLengthLoc, WAVE_LENGTH); 
        
        int ampliLoc = GL20.glGetUniformLocation(shader.getProgram(), "amplitude");
        GL20.glUniform1f(ampliLoc, WAVE_AMPLITUDE);
        
        int yOffsetLoc = GL20.glGetUniformLocation(shader.getProgram(), "y_offset");
        GL20.glUniform1f(yOffsetLoc, Y_OFFSET); 
    }

    private void handleRisingWaterLevel(double delta) {
        if (riseWaterLevel) {
            float distStep = (float) (RISING_SPEED * delta);
            distanceRisen += distStep;
            
            int physStep = Converter.GraphicsToPhysicsY(distStep);
            long pos = collider.getPosition();
            collider.setPosition(Vectors.create(Vectors.xComp(pos), Vectors.yComp(pos) + physStep));
        }
        
        if (distanceRisen > RISING_STEP_DIST) {
            riseWaterLevel = false;
        }
    }

    
    @Override
    public void collided(Collider thisColl, Collider otherColl) {
        if (otherColl.hasAttachedGameObject()) {
            GameObject other = otherColl.getAttachedGameObject();
            
            if (other instanceof SketchCharacter) {
                SketchCharacter sc = (SketchCharacter) other;
                sc.takeDamage(sc.getMaxHealth());
            }
        }
    }
    
}
