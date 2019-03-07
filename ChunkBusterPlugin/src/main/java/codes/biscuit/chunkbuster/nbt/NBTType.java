package codes.biscuit.chunkbuster.nbt;

// Credits to tr7zw
public enum NBTType {

    NBTTagEnd(0),
    NBTTagCompound(10);

    NBTType(int i) {
        id = i;
    }

    private final int id;

    public int getId() {
        return id;
    }

    public static NBTType valueOf(int id) {
        for (NBTType t : values())
            if (t.getId() == id)
                return t;
        return NBTType.NBTTagEnd;
    }

}
