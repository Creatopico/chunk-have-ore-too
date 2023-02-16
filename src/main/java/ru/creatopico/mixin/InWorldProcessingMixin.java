package ru.creatopico.mixin;

import com.simibubi.create.content.contraptions.processing.InWorldProcessing;

import net.minecraft.entity.ItemEntity;

import net.minecraft.item.ItemStack;

import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import ru.creatopico.chunk.ChunkPit;
import ru.creatopico.util.ChunkInfo;
import ru.creatopico.util.OreType;
import ru.creatopico.chunk.ServerState;
import ru.creatopico.chunk.StackPositionProvider;
import ru.creatopico.util.Vec2I;

import java.util.List;

@Mixin(InWorldProcessing.class)
public class InWorldProcessingMixin {

	@Inject(method = "applyProcessing(Lnet/minecraft/entity/ItemEntity;Lcom/simibubi/create/content/contraptions/processing/InWorldProcessing$Type;)Z", at = @At("HEAD"))
	private static void applyProcessingMixin(ItemEntity entity, InWorldProcessing.Type type, CallbackInfoReturnable<Boolean> cir) {
		if (type == InWorldProcessing.Type.SPLASHING) {
			Vec2I pos = new Vec2I(entity.getChunkPos());
			ChunkInfo info = new ChunkInfo(pos, entity.getWorld());
			StackPositionProvider.put(entity.getStack(), info);
		}
	}
	@Inject(method = "process", at=@At("RETURN"))
	private static void processMixin(ItemStack stack, InWorldProcessing.Type type, World world, CallbackInfoReturnable<List<ItemStack>> cir) {
		if (type != InWorldProcessing.Type.SPLASHING)
			return;

		for (ItemStack resultStack : cir.getReturnValue()) {
			if (!OreType.contains(resultStack.getTranslationKey())) {
				StackPositionProvider.get(resultStack);
				continue;
			}

			ChunkInfo info = StackPositionProvider.get(stack);
			if (info == null) continue;

			ServerState state = ServerState.getServerState(info);
			ChunkPit oreStorage = state.getChunkStorage(info.pos);
			resultStack.setCount(oreStorage.tryGetOre(resultStack.getCount()));
			state.markDirty();
		}
	}
}
