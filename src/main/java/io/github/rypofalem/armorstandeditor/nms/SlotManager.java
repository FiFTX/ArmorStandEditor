package io.github.rypofalem.armorstandeditor.nms;

import org.bukkit.entity.Entity;

import io.github.rypofalem.armorstandeditor.nms.EntityHandler.ReflectionUtil;

public class SlotManager {
	
	private static final int FLAG = 2039583;
	private static final String NAME = "DisabledSlots";
	
	public static boolean isDisabled(Entity entity) {
		try {
			return ReflectionUtil.getInt(EntityHandler.readTag(entity), NAME) == FLAG;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void setDisabled(Entity e, boolean disable) {
		if(disable) {
			try {
				Object nbt = EntityHandler.readTag(e);
				ReflectionUtil.setInt(nbt, NAME, FLAG);
				EntityHandler.setTag(nbt, e);
			} catch (InstantiationException | IllegalAccessException e1) {
				e1.printStackTrace();
			}
		} else {
			try {
				Object nbt = EntityHandler.readTag(e);
				ReflectionUtil.setInt(nbt, NAME, 0);
				EntityHandler.setTag(nbt, e);
			} catch (InstantiationException | IllegalAccessException e1) {
				e1.printStackTrace();
			}
		}
	}
	
}
