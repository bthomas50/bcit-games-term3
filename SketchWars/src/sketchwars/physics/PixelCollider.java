package sketchwars.physics;

import sketchwars.game.GameObject;

import java.util.List;
import java.util.ArrayList;
/**
 * Class of physics objects that collides based on the intersection of pixel masks
 * @author Brian
 */
public class PixelCollider extends AbstractCollider
{
    private BitMask bitMask;
    
    public PixelCollider(BitMask bm)
    {
        bitMask = bm;
        bm.trim();
		this.vPosition = bm.getPosition();
    }

    public PixelCollider(BitMask bm, CollisionBehaviour behaviour)
    {
        super(behaviour);
        bitMask = bm;
        bm.trim();
        this.vPosition = bm.getPosition();
    }

    public BitMask getPixels()
    {
        return bitMask;
    }

	@Override
	public void setPosition(long vPos)
	{
		super.setPosition(vPos);
		bitMask.setPosition(vPos);
	}
	

    @Override
    public BoundingBox getBounds()
    {
        return bitMask.getBounds();
    }

    @Override
    public long getCenterOfMass()
    {
        return bitMask.getCenterOfMass();
    }
}