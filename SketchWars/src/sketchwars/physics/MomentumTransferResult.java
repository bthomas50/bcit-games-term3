package sketchwars.physics;

public class MomentumTransferResult
{
    public long vAcceleration1, vAcceleration2;
    public long vNormal;
    public double impulse;

    public MomentumTransferResult()
    {
        vAcceleration1 = Vectors.V_ZERO;
        vAcceleration2 = Vectors.V_ZERO;
        vNormal = Vectors.V_ZERO;
        impulse = 0.0;
    }
}