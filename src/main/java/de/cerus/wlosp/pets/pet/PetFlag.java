package de.cerus.wlosp.pets.pet;

public enum PetFlag {

    PEACEFUL(0x1), FOLLOW_OWNER(0x2), TELEPORT_WHEN_AWAY(0x3);

    private final int flag;

    PetFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

}
