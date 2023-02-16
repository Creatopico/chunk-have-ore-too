package ru.creatopico.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;

public class Vec2I {
    public int x, z;

    public Vec2I(ChunkPos pos) {
        x = pos.x;
        z = pos.z;
    }

	public Vec2I(int x, int z) {
		this.x = x;
		this.z = z;
	}

	public Vec2I(Vec3d pos) {
		this.x = (int) Math.floor(pos.x / 16);
		this.z = (int) Math.floor(pos.z / 16);
	}

	@Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;

        Vec2I vec = (Vec2I) obj;
        return x == vec.x && z == vec.z;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + this.x;
        hash = 31 * hash + this.z;
        return hash;
    }

	@Override
	public String toString() {
		return "(" + x + ", " + z + ")";
	}
}
