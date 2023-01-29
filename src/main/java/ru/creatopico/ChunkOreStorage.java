package ru.creatopico;

public class ChunkOreStorage {
    public final static String ORE_LEFT_KEY = "ore_left";
    public int oreLeft = 133;

	public void getOre(int amount) {
		if (canGetOre(amount)) oreLeft -= amount;
	}

	public boolean canGetOre(int amount) {
		return oreLeft - amount >= 0;
	}

	public boolean checkAndGetOre(int amount) {
		boolean canGet = canGetOre(amount);
		if (canGet)
			getOre(amount);

		return canGet;
	}
}
