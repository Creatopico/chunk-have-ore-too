package ru.creatopico.chunk;

public class ChunkPit {
    private int oreLeft = 0;

	public boolean canGetOre(int amount) {
		return oreLeft - amount >= 0;
	}

	public int tryGetOre(int amount) {
		int newAmount;
		if (oreLeft >= amount) {
			oreLeft -= amount;
			newAmount = amount;
		}
		else {
			newAmount = oreLeft;
			oreLeft = 0;
		}

		return newAmount;
	}

	public int getOreLeft() {
		return oreLeft;
	}

	public void setOreLeft(int oreLeft) {
		this.oreLeft = oreLeft;
	}

	public void addOre(int ore) {
		this.oreLeft += ore;
	}
}
