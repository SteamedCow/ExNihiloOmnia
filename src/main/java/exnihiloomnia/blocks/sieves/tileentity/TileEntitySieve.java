package exnihiloomnia.blocks.sieves.tileentity;

import exnihiloomnia.ENOConfig;
import exnihiloomnia.client.particles.ParticleSieve;
import exnihiloomnia.items.meshs.ISieveMesh;
import exnihiloomnia.items.sieveassist.ISieveFaster;
import exnihiloomnia.registries.sifting.SieveRegistry;
import exnihiloomnia.util.helpers.InventoryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.HashMap;

@SuppressWarnings("deprecation")
public class TileEntitySieve extends TileEntity implements ITickable {
	protected final HashMap<ItemStack, EntityPlayer> sifters = new HashMap<>();
    protected ItemStack currentSifter;

	protected ItemStack mesh;
	protected ItemStack contents;
	protected IBlockState contentsState;

	protected boolean workQueued = false;
	protected int work = 0;
	protected final int workMax = 1000;
	protected static final int workPerSecondBase = 200;

	protected int workPerSecond = 200;

	protected final int ticksPerCycle = 4;
	protected int ticksThisCycle = 0;
	
	protected boolean spawningParticles = false;
	protected int spawnParticlesTimer = 0;
	protected final int spawnParticlesTimerMax = 5;

	protected int updateTimer = 0;
	protected final int updateTimerMax = 4; //Sync if an update is required.
	protected boolean updateQueued = false;
	protected boolean updateTimerRunning = false;

	private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            ItemStack copy = stack.copy();
            copy.setCount(1);

            if (isItemValidForSlot(slot, copy)) {

				if (slot == 0 && hasMesh()) {
                    setContents(copy);
                    return new ItemStack(stack.getItem(), stack.getCount() - 1);
                }

				if (slot == 1) {
                    setMesh(copy);
                    return new ItemStack(stack.getItem(), stack.getCount() - 1);
                }
            }
			return stack;
		}
	};

    private boolean isItemValidForSlot(int index, ItemStack stack) {
        Block block = Block.getBlockFromItem(stack.getItem());

		return block != null ? index == 0 && contents == null && SieveRegistry.isSiftable(block.getStateFromMeta(stack.getMetadata())) : index == 1 && stack.getItem() instanceof ISieveMesh && mesh == null && !ENOConfig.classic_sieve;
    }

	public void setWorkPerSecond(int workPerSecond, ItemStack sifter, EntityPlayer player) {
		this.workPerSecond = workPerSecond;
		if (sifter != null) {
            currentSifter = sifter;
            sifters.put(sifter, player);
        }
	}

	public int getBaseSpeed() {
		return workPerSecondBase;
	}

	@Override
	public void update() {
		if (workQueued) {
			ticksThisCycle++;

			if (work < workMax) {
                work += workPerSecond / 20;
				if (currentSifter != null)
                	((ISieveFaster)currentSifter.getItem()).addSiftTime(currentSifter, workPerSecond/20);
            }

			if (ticksThisCycle >= ticksPerCycle) {
				ticksThisCycle = 0;
				workQueued = false;
			}
		}
		if (!this.getWorld().isRemote) {
			if (updateTimerRunning) {
				updateTimer++;

				if (updateTimer > updateTimerMax) {
					updateTimer = 0;

					if (updateQueued) {
						updateQueued = false;
						getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);

					}
					else {
						updateTimerRunning = false;
					}
				}
			}
			if (work >= workMax) {
				if (contentsState != null) {
					for (ItemStack i : SieveRegistry.generateRewards(contentsState)) {
						InventoryHelper.dropItemInWorld(getWorld(), pos, 1, i);

						for (int j = 0; j < getWorld().rand.nextInt(2); j++)
							getWorld().spawnEntity(new EntityXPOrb(getWorld(), pos.getX() + .5, pos.getY() + 1, pos.getZ() + .5, 1));
					}
				}

				work = 0;
				workQueued = false;
				ticksThisCycle = 0;
				contents = null;

				if (this.mesh != null) {
					if (mesh.attemptDamageItem(1, getWorld().rand, null)) {
						getWorld().playSound(null, pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 0.5f, 2.5f);
						setMesh(null);
					}
				}
				if (sifters != null) {
					for (ItemStack i : sifters.keySet()) {
						ISieveFaster sifter = ((ISieveFaster) i.getItem());

						if (sifter.getSiftTime(i) >= workMax / 2) {
							sifter.setSiftTime(i, 0);
							EntityPlayer player = sifters.get(i);

							i.damageItem(1, player);

							if (i.getCount() <= 0) {
								player.renderBrokenItemStack(i);
								if (i.equals(player.inventory.offHandInventory.get(0)))
									player.inventory.offHandInventory.set(0, ItemStack.EMPTY);
							}
						}
					}

					sifters.clear();
				}

				sync();
				markDirty();
			}
		}
		else {
			if (spawningParticles) {
				generateParticles(contentsState);
				
				spawnParticlesTimer++;
				
				if (spawnParticlesTimer > spawnParticlesTimerMax) {
					spawningParticles = false;
				}
			}
		}
	}

	//Send update packets to each client.
	public void sync() {
		if (getWorld() != null && !getWorld().isRemote) {
			if (!updateTimerRunning) {
				updateTimerRunning = true;
				getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
			}
			else {
				updateQueued = true;
			}
		}
	}

	public boolean hasMesh() {
		return this.mesh != null;
	}
	
	public ItemStack getMesh() {
		return this.mesh;
	}
	
	public void setMesh(ItemStack mesh) {
		this.mesh = mesh;
		sync();
	}
	
	public ItemStack getContents() {
		return contents;
	}
	
	public void setContents(ItemStack input) {
		this.contents = input;
		
		if (contents != null) {
			Block block = Block.getBlockFromItem(contents.getItem());

			if (block != null)
				contentsState = block.getStateFromMeta(input.getMetadata());
		}
		else {
			contentsState = null;
		}
		
		sync();
	}
	
	public boolean canWork() {
		return contents != null;
	}
	
	public void doWork() {
        this.spawningParticles = true;
        this.workQueued = true;
        
		if (!this.getWorld().isRemote) {
			sync();
			markDirty();
		}
	}
	
	public float getProgress() {
		return (float) work / (float) workMax;
	}
	
	//Subclasses which don't want to use the replacable meshes can override this directly.
	public TextureAtlasSprite getMeshTexture() {
		if (mesh != null)
			return ((ISieveMesh) mesh.getItem()).getMeshTexture();
		else
			return null;
	}
	
	public void startSpawningParticles() {
		this.spawningParticles = true;
		this.spawnParticlesTimer = 0;
	}
	
	@SideOnly(Side.CLIENT)
	private void generateParticles(IBlockState block) {
		if (block != null) {
			TextureAtlasSprite texture = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(block);

			for (int x = 0; x < 6; x++) {	
				ParticleSieve dust = new ParticleSieve(getWorld(),
						pos.getX() + 0.8d * getWorld().rand.nextFloat() + 0.15d,
						pos.getY() + 0.585d, 
						pos.getZ() + 0.8d * getWorld().rand.nextFloat() + 0.15d,
						0.0d, 0.0d, 0.0d, texture);
				
				Minecraft.getMinecraft().effectRenderer.addEffect(dust);
			}
		}
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)  {
		return !oldState.getBlock().equals(newState.getBlock());
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && ENOConfig.sieve_automation) || super.hasCapability(capability, facing);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && ENOConfig.sieve_automation) {
			return (T) itemHandler;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		work = compound.getInteger("work");
		
		if(compound.getBoolean("particles"))
			startSpawningParticles();
		
		NBTTagList items = compound.getTagList("items", Constants.NBT.TAG_COMPOUND);
		
		NBTTagCompound meshTag = items.getCompoundTagAt(0);
		setMesh(new ItemStack(meshTag));
		
		NBTTagCompound contentsTag = items.getCompoundTagAt(1);
		setContents(new ItemStack(contentsTag));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		compound.setInteger("work", work);
		compound.setBoolean("particles", spawningParticles);
		
		NBTTagList items = new NBTTagList();
		
		NBTTagCompound meshTag = new NBTTagCompound();
		
		if (mesh != null) {
			mesh.writeToNBT(meshTag);
		}
		
		items.appendTag(meshTag);
		
		NBTTagCompound contentsTag = new NBTTagCompound();
		
		if (contents != null) {
			contents.writeToNBT(contentsTag);
		}
		
		items.appendTag(contentsTag);

		/*
		NBTTagList sifters = new NBTTagList();

		if (this.sifters.size() > 0) {
			for (ItemStack i : this.sifters) {
				NBTTagCompound sift = new NBTTagCompound();

				i.writeToNBT(sift);

				sifters.appendTag(sift);
			}
		}

		items.appendTag(sifters);
		*/
		
		compound.setTag("items", items);
		return compound;
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		this.readFromNBT(tag);
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);

		return new SPacketUpdateTileEntity(this.getPos(), this.getBlockMetadata(), tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		NBTTagCompound tag = pkt.getNbtCompound();
		this.readFromNBT(tag);
	}
}
