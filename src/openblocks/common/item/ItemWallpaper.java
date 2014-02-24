package openblocks.common.item;

import java.util.List;

import openblocks.Config;
import openblocks.OpenBlocks;
import openblocks.common.block.BlockCanvas;
import openblocks.common.tileentity.TileEntityCanvas;
import openmods.utils.render.PaintUtils;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemWallpaper extends Item {

	public ItemWallpaper() {
		super(Config.itemWallpaperId);
		setHasSubtypes(true);
		setCreativeTab(OpenBlocks.tabOpenBlocks);
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			if (PaintUtils.instance.isAllowedToReplace(world, x, y, z)) {
				if (stack.hasTagCompound()) {
					NBTTagCompound tag = stack.getTagCompound();
					if (tag.hasKey("blockId") && tag.hasKey("blockMeta") && tag.hasKey("side")) {
						if (PaintUtils.instance.isAllowedToReplace(world, x, y, z)) {
							BlockCanvas.replaceBlock(world, x, y, z);
						}
						TileEntity te = world.getBlockTileEntity(x, y, z);

						if (te instanceof TileEntityCanvas) {
							TileEntityCanvas canvas = (TileEntityCanvas)te;
							canvas.setWallpaper(side, tag.getInteger("blockId"), tag.getInteger("blockMeta"), tag.getInteger("side"));
						}
						stack.stackSize--;
					}
				} else {
					NBTTagCompound tag = new NBTTagCompound();
					tag.setInteger("blockId", world.getBlockId(x, y, z));
					tag.setInteger("blockMeta", world.getBlockMetadata(x, y, z));
					tag.setInteger("side", side);
					stack.setTagCompound(tag);
				}
			}
		}
		return true;
	}
}