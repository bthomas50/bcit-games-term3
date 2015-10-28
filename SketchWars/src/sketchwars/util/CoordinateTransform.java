package sketchwars.util;

public interface CoordinateTransform
{
    float transformX(float x);
    float transformY(float y);
    float transformWidth(float width);
    float transformHeight(float height);
    float invTransformX(float x);
    float invTransformY(float y);
    float invTransformWidth(float width);
    float invTransformHeight(float height);
}