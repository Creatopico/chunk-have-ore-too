package ru.creatopico.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import org.jetbrains.annotations.Nullable;

import ru.creatopico.chunk.ChunkPit;
import ru.creatopico.chunk.ServerState;
import ru.creatopico.util.Vec2I;

public class OreViewerBlock extends RedstoneBlock{
	public static final BooleanProperty FIND_ORE = BooleanProperty.of("ore");

	public OreViewerBlock(AbstractBlock.Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(FIND_ORE, false));
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		world.createAndScheduleBlockTick(pos, this, 20);
		super.onPlaced(world, pos, state, placer, itemStack);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		ChunkPit storage = ServerState.getChunkStorage(world, new Vec2I(world.getChunk(pos).getPos()));
		if (state.get(FIND_ORE) != (storage != null && storage.canGetOre(1)))
			world.setBlockState(pos, state.cycle(FIND_ORE));
		world.createAndScheduleBlockTick(pos, this, 20);
		super.scheduledTick(state, world, pos, random);
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		if (state.get(FIND_ORE)) return 15;
		else return 0;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FIND_ORE);
	}
}
