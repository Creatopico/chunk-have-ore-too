package ru.creatopico.util;

public enum OreType {
	iron("item.minecraft.iron_nugget"),
	gold("item.minecraft.gold_nugget");

	public final String ore;

	OreType(String ore) {
		this.ore = ore;
	}

	public static boolean contains(String value) {
		for(OreType type: OreType.values())
			if (type.ore.equals(value))
				return true;

		return false;
	}

}
