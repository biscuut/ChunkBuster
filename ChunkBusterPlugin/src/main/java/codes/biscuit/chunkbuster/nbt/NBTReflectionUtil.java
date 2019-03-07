package codes.biscuit.chunkbuster.nbt;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.Stack;

// Credits to tr7zw
class NBTReflectionUtil {

    @SuppressWarnings({"unchecked"})
    static Object getItemRootNBTTagCompound(Object nmsitem) {
        @SuppressWarnings("rawtypes")
        Class clazz = nmsitem.getClass();
        Method method;
        try {
            method = clazz.getMethod("getTag");
            return method.invoke(nmsitem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @SuppressWarnings("unchecked")
    private static Object getSubNBTTagCompound(Object compound, String name) {
        @SuppressWarnings("rawtypes")
        Class c = compound.getClass();
        Method method;
        try {
            method = c.getMethod("getCompound", String.class);
            return method.invoke(compound, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static Boolean valideCompound(NBTCompound comp) {
        Object root = comp.getCompound();
        if (root == null) {
            root = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance();
        }
        return (gettoCompount(root, comp)) != null;
    }

    private static Object gettoCompount(Object nbttag, NBTCompound comp) {
        Stack<String> structure = new Stack<>();
        while (comp.getParent() != null) {
            structure.add(comp.getName());
            comp = comp.getParent();
        }
        while (!structure.isEmpty()) {
            nbttag = getSubNBTTagCompound(nbttag, structure.pop());
            if (nbttag == null) {
                return null;
            }
        }
        return nbttag;
    }

    @SuppressWarnings("ConstantConditions")
    static String getContent(NBTCompound comp, String key) {
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null) {
            rootnbttag = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance();
        }
        if (!valideCompound(comp)) return null;
        Object workingtag = gettoCompount(rootnbttag, comp);
        Method method;
        try {
            method = workingtag.getClass().getMethod("get", String.class);
            return method.invoke(workingtag, key).toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static void remove(NBTCompound comp, String key) {
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null) {
            rootnbttag = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance();
        }
        if (!valideCompound(comp)) return;
        Object workingtag = gettoCompount(rootnbttag, comp);
        ReflectionMethod.COMPOUND_REMOVE_KEY.run(workingtag, key);
        comp.setCompound(rootnbttag);
    }

    @SuppressWarnings("unchecked")
    static Set<String> getKeys(NBTCompound comp) {
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null) {
            rootnbttag = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance();
        }
        if (!valideCompound(comp)) return null;
        Object workingtag = gettoCompount(rootnbttag, comp);
        return (Set<String>) ReflectionMethod.COMPOUND_GET_KEYS.run(workingtag);
    }

    @SuppressWarnings("SameParameterValue")
    static void setData(NBTCompound comp, ReflectionMethod type, String key, Object data) {
        if (data == null) {
            remove(comp, key);
            return;
        }
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null) {
            rootnbttag = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance();
        }
        if (!valideCompound(comp)) return;
        Object workingtag = gettoCompount(rootnbttag, comp);
        type.run(workingtag, key, data);
        comp.setCompound(rootnbttag);
    }

    static Object getData(NBTCompound comp, ReflectionMethod type, String key) {
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null) {
            return null;
        }
        if (!valideCompound(comp)) return null;
        Object workingtag = gettoCompount(rootnbttag, comp);
        return type.run(workingtag, key);
    }

}
