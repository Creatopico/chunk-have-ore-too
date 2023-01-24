package ru.creatopico;

import net.minecraft.util.math.ChunkPos;

class Vec2I {
    public int x, z;

    public Vec2I(){}

    public Vec2I(ChunkPos pos) {
        x = pos.x;
        z = pos.z;
    }

    public Vec2I(double x, double z) {
        this.x = (int) (x / 16);
        this.z = (int) (z / 16);
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

}