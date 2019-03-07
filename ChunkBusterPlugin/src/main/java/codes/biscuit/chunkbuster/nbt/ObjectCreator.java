package codes.biscuit.chunkbuster.nbt;

import java.lang.reflect.Constructor;

// Credits to tr7zw
public enum ObjectCreator {

    NMS_NBTTAGCOMPOUND(ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz());

    private Constructor<?> construct;

    ObjectCreator(Class<?> clazz, Class<?>... args){
        try{
            construct = clazz.getConstructor(args);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public Object getInstance(Object... args){
        try{
            return construct.newInstance(args);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    
}
