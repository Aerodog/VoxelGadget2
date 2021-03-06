package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 * @author CalDaBeast
 */
public class BlockToggleMode extends AbstractModeModifier {

	@Override
	public int modify(Processor p, Block currentBlock, Block nextBlock) {
		if (p.getDispensed().getTypeId() == 387) { //handle for blueprints
			try {
				BlueprintHandler blueprint = new BlueprintHandler(p.getDispensed());
				if (blueprint.checkIfExists(p.getTargetLocation())) blueprint.remove(p.getTargetLocation());
				else blueprint.paste(p.getTargetLocation());
			} catch (Exception e) {
				BlueprintHandler.handleException(e, p.getDispensed());
			}
			return 0;
		}
		Block existing = p.getDispenser().getRelative(p.getTail().getOppositeFace(), (p.isLineEnabled()
				|| p.isAreaEnabled() ? p.getSize() + (p.isLineEnabled() ? 2 : 1) : p.getOffset()));
		if (p.getOffset3D() != null) existing = p.getOffset3D().getBlock();
		//System.out.println(existing);
		if (p.isOverrideAbsolute()) {
			toggleBetween(existing, p.getDispensed().getTypeId(), p.getDispensed().getData().getData(), p.getOverride().getTypeId(), p.getOverride().getData(), p);
		} else {
			int placeID = p.getDispensed().getTypeId();
			byte placeData = p.getDispensed().getData().getData();
			if (p.getOverride() != null) {
				placeID = p.getOverride().getTypeId();
				placeData = p.getOverride().getData();
			}
			toggleBetween(existing, 0, (byte) 0, placeID, placeData, p);
		}
		return 0;
	}

	private void toggleBetween(Block existing, int idA, byte dataA, int idB, byte dataB, Processor p) {
		//System.out.println("Toggling between " + idA + ":" + dataA + " and " + idB + ":" + dataB);
		//System.out.println("Current = " + existing.getTypeId() + ":" + existing.getData());
		int placeID;
		byte placeData;
		if (existing.getTypeId() == idB && existing.getData() == dataB) {
			placeID = idA;
			placeData = dataA;
		} else {
			placeID = idB;
			placeData = dataB;
		}
		//System.out.println("Placing " + placeID + ":" + placeData);
		this.setBlock(existing, new ItemStack(placeID, p.getDispensed().getAmount(), placeData), p.applyPhysics(), p);
	}

}
