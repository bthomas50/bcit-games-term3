package sketchwars.physics;

public class FrictionResult
{
    public long vAcceleration1, vAcceleration2;
    public long vTangent;
    public double impulse;

    public FrictionResult()
    {
        vAcceleration1 = Vectors.V_ZERO;
        vAcceleration2 = Vectors.V_ZERO;
        vTangent = Vectors.V_ZERO;
        impulse = 0.0;
    }
}