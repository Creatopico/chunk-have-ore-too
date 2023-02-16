package ru.creatopico.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.simibubi.create.content.contraptions.components.fan.AirCurrent;
import com.simibubi.create.content.contraptions.processing.InWorldProcessing;
import com.simibubi.create.content.contraptions.relays.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.tileEntity.behaviour.belt.TransportedItemStackHandlerBehaviour;

import net.minecraft.world.World;
import ru.creatopico.util.ChunkInfo;
import ru.creatopico.chunk.StackPositionProvider;
import ru.creatopico.util.Vec2I;

@Mixin(AirCurrent.class)
public class AirCurrentMixin {
	@Inject(method = "lambda$tickAffectedHandlers$2", at = @At("HEAD"))
	private void tickAffectedHandlersMixin(World world, InWorldProcessing.Type processingType, TransportedItemStackHandlerBehaviour handler, TransportedItemStack transported, CallbackInfoReturnable<TransportedItemStackHandlerBehaviour.TransportedResult> cir) {
		if (processingType == InWorldProcessing.Type.SPLASHING){
			Vec2I pos = new Vec2I(handler.getWorldPositionOf(transported));
			ChunkInfo info = new ChunkInfo(pos, world);
			StackPositionProvider.put(transported.stack, info);
		}
	}
}
