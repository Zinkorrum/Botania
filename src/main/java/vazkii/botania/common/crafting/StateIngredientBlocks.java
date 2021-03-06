package vazkii.botania.common.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.registries.ForgeRegistries;

import vazkii.botania.api.recipe.StateIngredient;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StateIngredientBlocks implements StateIngredient {
	private final Set<Block> blocks;

	public StateIngredientBlocks(Set<Block> blocks) {
		this.blocks = blocks;
	}

	@Override
	public boolean test(BlockState state) {
		return blocks.contains(state.getBlock());
	}

	@Override
	public JsonObject serialize() {
		JsonObject object = new JsonObject();
		object.addProperty("type", "blocks");
		JsonArray array = new JsonArray();
		for (Block block : blocks) {
			array.add(block.getRegistryName().toString());
		}
		object.add("blocks", array);
		return object;
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeVarInt(0);
		buffer.writeVarInt(blocks.size());
		for (Block block : blocks) {
			buffer.writeRegistryIdUnsafe(ForgeRegistries.BLOCKS, block);
		}
	}

	@Override
	public List<BlockState> getDisplayed() {
		return blocks.stream().map(Block::getDefaultState).collect(Collectors.toList());
	}
}
