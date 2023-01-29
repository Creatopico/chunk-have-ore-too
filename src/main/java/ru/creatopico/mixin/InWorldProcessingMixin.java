package ru.creatopico.mixin;

import com.simibubi.create.content.contraptions.processing.InWorldProcessing;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;

import net.minecraft.item.ItemStack;

import net.minecraft.world.World;

import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import ru.creatopico.ChunkOreStorage;
import ru.creatopico.ExampleMod;
import ru.creatopico.OreType;
import ru.creatopico.ServerStateManager;

import java.util.HashMap;
import java.util.List;

@Mixin(InWorldProcessing.class)
public class InWorldProcessingMixin {

	@Inject(method = "applyProcessing(Lnet/minecraft/entity/ItemEntity;Lcom/simibubi/create/content/contraptions/processing/InWorldProcessing$Type;)Z", at = @At("HEAD"))
	private static void applyProcessingMixin(ItemEntity entity, InWorldProcessing.Type type, CallbackInfoReturnable<Boolean> cir) {
		ExampleMod.LOGGER.info("applyProcessing MIXIN SAY THAT entity is " + entity + " stack is " + entity.getStack());
		ExampleMod.stackToEntityMap.put(entity.getStack(), entity);
	}

	@Inject(method = "process", at=@At("RETURN"))
	private static void processMixin(ItemStack stack, InWorldProcessing.Type type, World world, CallbackInfoReturnable<List<ItemStack>> cir) {
		if (type != InWorldProcessing.Type.SPLASHING)
			return;

		for (ItemStack resultStack : cir.getReturnValue()) {
			if (OreType.contains(resultStack.getTranslationKey())) {
				Entity entity = ExampleMod.stackToEntityMap.get(stack);
				if (entity != null) {
					ChunkOreStorage oreStorage = ServerStateManager.getChunkStorage(entity);

					int newCount = 0;
					for (int i = 0; i < resultStack.getCount(); i++)
						if (oreStorage.checkAndGetOre(1))
							newCount++;
						else break;
					ExampleMod.LOGGER.info("" + oreStorage.oreLeft + " ORE LEFT");

					resultStack.setCount(newCount);
				}
			}
		}
	}
}
