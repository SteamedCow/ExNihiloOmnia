package exnihiloomnia.client.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;

public class ParticleSieve extends Particle {
	protected float minU = 0.0f;
	protected float minV = 0.0f;
	protected float maxU = 0.0f;
	protected float maxV = 0.0f;
	
	public ParticleSieve(World par1World, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, TextureAtlasSprite texture) {
        super(par1World, posX, posY, posZ, speedX, speedY, speedZ);
        this.particleTexture = texture;
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
        this.setSize(0.04F, 0.04F);
        this.particleScale = 0.07f + (this.rand.nextFloat() * 0.1F);
        this.motionX *= 0.019999999552965164D;
        this.motionY *= -0.4;
        this.motionZ *= 0.019999999552965164D;
        this.particleMaxAge = (int) (20.0D / (Math.random() * 0.2D + 0.2D));
        
        //Select a single pixel in the texture to draw.
        float width = (this.particleTexture.getMaxU() - this.particleTexture.getMinU()) / particleTexture.getIconWidth();
        float height = (this.particleTexture.getMaxV() - this.particleTexture.getMinV()) / particleTexture.getIconHeight();
        
        this.minU = this.particleTexture.getMinU() + (rand.nextInt(particleTexture.getIconWidth()) * width);
        this.minV = this.particleTexture.getMinV() + (rand.nextInt(particleTexture.getIconHeight()) * height);
        this.maxU = this.minU + width;
        this.maxV = this.minV + height;
    }

	@Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.99D;
        this.motionY *= 0.99D;
        this.motionZ *= 0.99D;
        
        this.nextTextureIndexX();

        if (this.particleMaxAge-- <= 0) {
            this.setExpired();
        }
    }
    
    @Override
    public int getFXLayer() {
        return 1;
    }
}
