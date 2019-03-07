package codes.biscuit.chunkbuster.nbt;

import codes.biscuit.chunkbuster.nbt.utils.MinecraftVersion;

import java.util.Set;

// Credits to tr7zw
public class NBTCompound {

    private String compundName;
    private NBTCompound parent;

    NBTCompound(NBTCompound owner, String name) {
        this.compundName = name;
        this.parent = owner;
    }

    String getName() {
        return compundName;
    }

    protected Object getCompound() {
        return parent.getCompound();
    }

    protected void setCompound(Object compound) {
        parent.setCompound(compound);
    }

    NBTCompound getParent() {
        return parent;
    }

    private String getContent(String key) {
        return NBTReflectionUtil.getContent(this, key);
    }

    public void setInteger(String key, Integer value) {
        NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_INT, key, value);
    }

    public Integer getInteger(String key) {
        return (Integer) NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_INT, key);
    }

    private Set<String> getKeys() {
        return NBTReflectionUtil.getKeys(this);
    }

    private NBTCompound getCompound(String name) {
        NBTCompound next = new NBTCompound(this, name);
        if (NBTReflectionUtil.valideCompound(next)) return next;
        return null;
    }

    private NBTType getType(String name) {
        if (MinecraftVersion.getVersion() == MinecraftVersion.MC1_7_R4) return null;
        Object o = NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_TYPE, name);
        if(o == null)return null;
        return NBTType.valueOf((byte) o);
    }

    public Boolean hasKey(String key) {
        Boolean b =  (Boolean) NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_HAS_KEY, key);
        if(b == null)return false;
        return b;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (String key : getKeys()) {
            result.append(toString(key));
        }
        return result.toString();
    }

    @SuppressWarnings("ConstantConditions")
    private String toString(String key) {
        StringBuilder result = new StringBuilder();
        NBTCompound compound = this;
        while (compound.getParent() != null) {
            result.append("   ");
            compound = compound.getParent();
        }
        if (this.getType(key) == NBTType.NBTTagCompound) {
            return this.getCompound(key).toString();
        } else {
            return result + "-" + key + ": " + getContent(key) + System.lineSeparator();
        }
    }
}
