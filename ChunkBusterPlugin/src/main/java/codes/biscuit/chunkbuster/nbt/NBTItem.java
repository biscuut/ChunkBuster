package codes.biscuit.chunkbuster.nbt;

import org.bukkit.inventory.ItemStack;

// Credits to tr7zw
public class NBTItem extends NBTCompound {

    private ItemStack bukkitItem;

    public NBTItem(ItemStack item) {
        super(null, null);
        if(item == null){
            throw new NullPointerException("ItemStack can't be null!");
        }
        bukkitItem = item.clone();
    }

    @SuppressWarnings("ConstantConditions")
    protected Object getCompound() {
        return NBTReflectionUtil.getItemRootNBTTagCompound(ReflectionMethod.ITEMSTACK_NMSCOPY.run(null, bukkitItem));
    }

    protected void setCompound(Object compound) {
        Object stack = ReflectionMethod.ITEMSTACK_NMSCOPY.run(null, bukkitItem);
        ReflectionMethod.ITEMSTACK_SET_TAG.run(stack, compound);
        bukkitItem = (ItemStack) ReflectionMethod.ITEMSTACK_BUKKITMIRROR.run(null, stack);
    }

    public ItemStack getItem() {
        return bukkitItem;
    }
}
