package ganymedes01.headcrumbs.blocks;

import java.util.ArrayList;
import java.util.List;

import ganymedes01.headcrumbs.Headcrumbs;
import ganymedes01.headcrumbs.ModBlocks.IHasCustomItem;
import ganymedes01.headcrumbs.items.ItemHeadcrumbsSkull;
import ganymedes01.headcrumbs.libs.SkullTypes;
import ganymedes01.headcrumbs.tileentities.TileEntityBlockSkull;
import ganymedes01.headcrumbs.utils.HeadUtils;
import ganymedes01.headcrumbs.utils.Utils;
import ganymedes01.headcrumbs.utils.helpers.LycanitesHelperClient;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockHeadcrumbsSkull extends BlockSkull implements IHasCustomItem {

	public BlockHeadcrumbsSkull() {
		setHardness(1.0F);
		setStepSound(soundTypePiston);
		setCreativeTab(Headcrumbs.tab);
		setUnlocalizedName(Utils.getUnlocalisedName("skull"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
		for (SkullTypes skull : SkullTypes.values())
			if (skull.canShow() && skull != SkullTypes.lycanites)
				list.add(skull.getStack());
		if (SkullTypes.lycanites.canShow())
			list.addAll(LycanitesHelperClient.getStacks());
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityBlockSkull();
	}

	@Override
	public boolean canDispenserPlace(World world, BlockPos pos, ItemStack stack) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos) {
		return Item.getItemFromBlock(this);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> list = new ArrayList<ItemStack>();
		if (!state.getValue(NODROP).booleanValue()) {
			TileEntityBlockSkull tile = Utils.getTileEntity(world, pos, TileEntityBlockSkull.class);

			if (tile != null) {
				SkullTypes model = tile.getModel();
				ItemStack stack = model.getStack();

				if (model.usesProfile()) {
					NBTTagCompound nbt = NBTUtil.writeGameProfile(new NBTTagCompound(), tile.getPlayerProfile());
					stack.getTagCompound().setTag(HeadUtils.OWNER_TAG, nbt);
				}

				list.add(stack);
			}
		}
		return list;
	}

	@Override
	public Class<? extends ItemBlock> getItemBlockClass() {
		return ItemHeadcrumbsSkull.class;
	}
}