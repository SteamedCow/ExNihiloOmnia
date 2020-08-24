package exnihiloomnia;

import exnihiloomnia.client.textures.ENOTextures;
import exnihiloomnia.compatibility.ENOCompatibility;
import exnihiloomnia.crafting.ENOCrafting;
import exnihiloomnia.crafting.recipes.MobDrops;
import exnihiloomnia.items.hammers.ItemHammer;
import exnihiloomnia.proxy.Proxy;
import exnihiloomnia.registries.CommandRegistry;
import exnihiloomnia.registries.ENORegistries;
import exnihiloomnia.registries.hammering.HammerRegistry;
import exnihiloomnia.world.ENOWorld;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(name = ENO.NAME, modid = ENO.MODID, version = ENO.VERSION, dependencies = ENO.DEPENDENCIES, guiFactory = ENO.GUI_FACTORY)
public class ENO {

	static {
		FluidRegistry.enableUniversalBucket();
	}

	@Instance(ENO.MODID)
	public static ENO INSTANCE;
	
	public static final String NAME = "Ex Nihilo Omnia";
	public static final String MODID = "exnihiloomnia";
	public static final String VERSION = "0.0.1";
	public static final String DEPENDENCIES = "after:tconstruct;after:Mekanism;after:IC2;after:appliedenergistics2;after:FunOres;after:forestry;after:morebees;after:immersiveengineering;after:bigreactors;after:substratum";
	public static final String GUI_FACTORY = "exnihiloomnia.config.ENOGuiFactory";

	@SidedProxy(serverSide = "exnihiloomnia.proxy.ServerProxy", clientSide = "exnihiloomnia.proxy.ClientProxy")
	public static Proxy proxy;

	public static final Logger log = LogManager.getLogger(ENO.NAME);
	public static String path;
	public static Configuration config;

	@EventHandler
	public void preInitialize(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@EventHandler
	public void doInitialize(FMLInitializationEvent event) {
        proxy.init(event);
	}

	@EventHandler
	public void postInitialize(FMLPostInitializationEvent event) {
		proxy.postInit(event);
    }

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTextureStitchEvent(TextureStitchEvent.Pre event) {
		ENOTextures.registerCustomTextures(event.getMap());
		ENOTextures.setMeshTextures();
	}

	@EventHandler
	public void onServerLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandRegistry());
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		if (!event.getWorld().isRemote && event.getWorld() instanceof WorldServer) {
			ENOWorld.load(event.getWorld());
		}
	}

	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		if (event.side == Side.SERVER && event.phase == TickEvent.Phase.START) {
			event.world.profiler.startSection("exNihiloWorld");

			ENOWorld.tick(event.world);

			event.world.profiler.endSection();
		}
	}

	@SubscribeEvent
	public void onEntityDrop(LivingDropsEvent event) {
		MobDrops.onMobDeath(event);
	}

	@SubscribeEvent
	public void onFish(LootTableLoadEvent event) {
		if (event.getName().equals(LootTableList.GAMEPLAY_FISHING_TREASURE)) {
			LootPool main = event.getTable().getPool("main");
			main.addEntry(new LootEntryItem(Items.REEDS, 1, 2, new LootFunction[0], new LootCondition[0], MODID + ":sugarcane"));
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onHammer(BlockEvent.HarvestDropsEvent event) {
		if (event.getHarvester() != null) {
			ItemStack stack = event.getHarvester().getHeldItemMainhand();

			if (stack != null && stack.getItem() instanceof ItemHammer) {
				IBlockState block = event.getState();

				if (!event.isSilkTouching() && HammerRegistry.isHammerable(block)) {
					event.getDrops().clear();

					event.getDrops().addAll(HammerRegistry.getEntryForBlockState(block).rollRewards(event.getHarvester()));
				}
			}
		}
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if(MODID.equals(event.getModID()))
			syncConfig();
	}

	public static void syncConfig() {
		ENOConfig.configure(config);

		ENOCompatibility.configure(config);
		ENOCrafting.configure(config);
		ENOWorld.configure(config);

		ENORegistries.configure(config);
		ENORegistries.initialize();

		if(config.hasChanged())
			config.save();
	}
}
