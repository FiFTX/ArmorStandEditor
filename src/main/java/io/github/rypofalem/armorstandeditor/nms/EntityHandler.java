package io.github.rypofalem.armorstandeditor.nms;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

public class EntityHandler {
	
	private static Method READTAG;
	private static Method SETTAG;
	
	static {
		for(Method m : ReflectionUtil.NMSENTITY.getMethods()) {
			if(m.getReturnType().equals(ReflectionUtil.NBTTAGCOMPOUND)) {
				if(m.getParameterTypes().length == 1 && m.getParameterTypes()[0].equals(ReflectionUtil.NBTTAGCOMPOUND)) {
					READTAG = m;
					if(READTAG != null && SETTAG != null) break;
				}
			} else if(m.getReturnType().equals(Void.TYPE)) {
				if(m.getParameterTypes().length == 1 && m.getParameterTypes()[0].equals(ReflectionUtil.NBTTAGCOMPOUND)) {
					SETTAG = m;
					if(READTAG != null && SETTAG != null) break;
				}
			}
		}
	}
	
	public static void setTag(Object tag, Entity e) {
		try {
			SETTAG.invoke(ReflectionUtil.getEntity(e), tag);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			e1.printStackTrace();
		}
	}
	
	public static Object readTag(Entity e) throws InstantiationException, IllegalAccessException {
		Object tag = ReflectionUtil.NBTTAGCOMPOUND.newInstance();
		
		try {
			READTAG.invoke(ReflectionUtil.getEntity(e), tag);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			e1.printStackTrace();
		}
		
		return tag;
	}
	
	public static class ReflectionUtil {
		
		private static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().replace(".",  ",").split(",")[3];
		
		private static Class<?> NBTTAGCOMPOUND;
		private static Class<?> CRAFTENTITY;
		private static Class<?> NMSENTITY;
		
		static {
			try {
				NBTTAGCOMPOUND = Class.forName("net.minecraft.server." + VERSION + ".NBTTagCompound");
				CRAFTENTITY = Class.forName("org.bukkit.craftbukkit." + VERSION + ".entity.CraftEntity");
				NMSENTITY = Class.forName("net.minecraft.server." + VERSION + ".Entity");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		public static boolean hasTag(Object nbt, String name) {
			try {
				return (boolean) NBTTAGCOMPOUND.getMethod("hasKey", String.class).invoke(nbt, name);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		public static int getInt(Object nbt, String name) {
			try {
				return (int) NBTTAGCOMPOUND.getMethod("getInt", String.class).invoke(nbt, name);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
				return 0;
			}
		}
		
		public static Object getTag() {
			try {
				return NBTTAGCOMPOUND.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public static void setInt(Object nbt, String name, int i) {
			try {
				NBTTAGCOMPOUND.getMethod("setInt", String.class, int.class).invoke(nbt, name, i);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
		
		public static Object getEntity(Entity entity) {
			Object craft = CRAFTENTITY.cast(entity);
			try {
				return CRAFTENTITY.getMethod("getHandle").invoke(craft);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
}
