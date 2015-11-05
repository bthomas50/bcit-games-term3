package sketchwars.util;

import java.awt.image.BufferedImage;
import sketchwars.SketchWars;

public class PhysicsToImage implements CoordinateTransform
{
    private static final int PHYSICS_TOP = SketchWars.PHYSICS_TOP;
    private static final int PHYSICS_LEFT = SketchWars.PHYSICS_LEFT;
    private static final int PHYSICS_WIDTH = SketchWars.PHYSICS_WIDTH;
    private static final int PHYSICS_HEIGHT = SketchWars.PHYSICS_HEIGHT;

    private float widthRatio;
    private float heightRatio;

    public PhysicsToImage(BufferedImage image)
    {
        widthRatio = (float)image.getWidth() / PHYSICS_WIDTH;
        heightRatio = (float)image.getHeight() / PHYSICS_HEIGHT;
    }

    @Override
    public float transformX(float x)
    {
        return (x - PHYSICS_LEFT) * widthRatio;// = xx -> xx / wi
    }
    @Override
    public float transformY(float y)
    {
        return -(y - PHYSICS_TOP) * heightRatio;
    }
    @Override
    public float transformWidth(float width)
    {
        return width * widthRatio;
    }
    @Override
    public float transformHeight(float height)
    {
        return height * heightRatio;
    }
    @Override
    public float invTransformX(float x)
    {
        return x / widthRatio + PHYSICS_LEFT;
    }
    @Override
    public float invTransformY(float y)
    {
        return -y / heightRatio + PHYSICS_TOP;
    }
    @Override
    public float invTransformWidth(float width)
    {
        return width / widthRatio;
    }
    @Override
    public float invTransformHeight(float height)
    {
        return height / heightRatio;
    }
}