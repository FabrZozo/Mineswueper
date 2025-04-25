package de.uniwue.jpp.mineswueper;

import java.util.Objects;

public class Field {
    private Coordinate coord;
    private boolean mine;
    private boolean offen;
    private int anzahlMinesNeighbours;
    private boolean gibtFlagge;
    public Field(Coordinate coord) {
        this.coord = coord;
        gibtFlagge=false;
        mine=false;
        offen=false;
        anzahlMinesNeighbours=0;
    }

    public Coordinate getCoordinate() {
        return coord;
    }

    public boolean hasFlag() {
        return gibtFlagge;
    }

    public void setHasFlag(boolean flag) {
        this.gibtFlagge = flag;
    }

    public boolean hasMine() {
        return mine;
    }

    public void setHasMine(boolean mine) {
        this.mine = mine;
    }

    public boolean isOpened() {
        return offen;
    }

    public void setOpened(boolean opened) {
        this.offen = opened;
    }

    public int getNeighbourMineCount() {
        return anzahlMinesNeighbours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return Objects.equals(coord, field.coord);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(coord);
    }

    public void setNeighbourMineCount(int neighbourMineCount) {
        if(neighbourMineCount<0)throw new IllegalArgumentException("neighbourMineCount<0");
        this.anzahlMinesNeighbours = neighbourMineCount;
    }
}
