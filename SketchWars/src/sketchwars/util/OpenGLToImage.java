package sketchwars.util;

import java.awt.image.BufferedImage;
import sketchwars.OpenGL;

/**
 * OpenGL coordinates to image coordinates
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class OpenGLToImage implements CoordinateTransform
{
    private static final float OPENGL_TOP = OpenGL.OPENGL_TOP;
    private static final float OPENGL_LEFT = OpenGL.OPENGL_LEFT;
    private static final float OPENGL_WIDTH = OpenGL.OPENGL_WIDTH;
    private static final float OPENGL_HEIGHT = OpenGL.OPENGL_HEIGHT;

    private final float widthRatio;
    private final float heightRatio;

    public OpenGLToImage(BufferedImage image) {
        widthRatio = (float)image.getWidth() / OPENGL_WIDTH;
        heightRatio = (float)image.getHeight() / OPENGL_HEIGHT;
    }

    @Override
    public float transformX(float x) {
        return (x - OPENGL_LEFT) * widthRatio;
    }
    
    @Override
    public float transformY(float y) {
        return -(y - OPENGL_TOP) * heightRatio;
    }
    
    @Override
    public float transformWidth(float width) {
        return width * widthRatio;
    }
    
    @Override
    public float transformHeight(float height) {
        return height * heightRatio;
    }
    
    @Override
    public float invTransformX(float x) {
        return x / widthRatio + OPENGL_LEFT;
    }
    
    @Override
    public float invTransformY(float y) {
        return -y / heightRatio + OPENGL_TOP;
    }
    
    @Override
    public float invTransformWidth(float width) {
        return width / widthRatio;
    }
    
    @Override
    public float invTransformHeight(float height) {
        return height / heightRatio;
    }
}