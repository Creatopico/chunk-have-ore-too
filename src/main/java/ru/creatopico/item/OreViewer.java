package ru.creatopico.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import ru.creatopico.ChunkHaveOreToo;
import ru.creatopico.chunk.ChunkPit;
import ru.creatopico.chunk.ServerState;
import ru.creatopico.util.OreViewerScheduler;

public class OreViewer extends Item {

	protected static OreViewerScheduler scheduler = new OreViewerScheduler();
	public OreViewer(Settings settings) {
		super(settings
				.maxCount(1)
				.group(ItemGroup.TOOLS)
				.rarity(Rarity.EPIC)
		);

	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		user.getItemCooldownManager().set(this, 20);
		if (world.isClient)
			user.playSound(SoundEvents.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, 1, 1);
		else {
			ChunkPit storage = ServerState.getChunkStorage(user.getServer().getWorld(world.getRegistryKey()), user.getChunkPos());

			user.sendMessage(Text.literal(
							ChunkHaveOreToo.CONFIG.playerMessageMask().replace("$value", "" + storage.getOreLeft())
					), true);
		}

		return TypedActionResult.success(user.getStackInHand(hand));
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);

		if(world.isClient || !entity.isPlayer())
			return;

		scheduler.query((ServerPlayerEntity) entity, world, selected);
	}
}
