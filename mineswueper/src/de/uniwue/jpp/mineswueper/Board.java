package de.uniwue.jpp.mineswueper;

import java.util.*;
import java.util.stream.Collectors;

public class Board {
    private int width;
    private int height;
    private Collection<Coordinate> mines;

    private Collection<Field> fieldState;
    int[][] spielfeld;

    public Board(int width, int height, Collection<Coordinate> mines) {
        this.width = width;
        this.height = height;
        this.mines = mines;
        spielfeld = new int[width][height];
        fieldState = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Coordinate coordinate = new Coordinate(i, j);
                if (mines.contains(coordinate)) {
                    Field field = new Field(coordinate);
                    field.setNeighbourMineCount(neighboursMineCount(field.getCoordinate().getX(), field.getCoordinate().getY()));
                    field.setHasMine(true);
                    fieldState.add(field);
                } else {
                    Field field = new Field(coordinate);
                    field.setNeighbourMineCount(neighboursMineCount(field.getCoordinate().getX(), field.getCoordinate().getY()));
                    fieldState.add(field);
                }
            }
        }
    }

    public boolean hasWon() {
        if (fieldState.stream().filter(e -> !mines.contains(e.getCoordinate())).
                allMatch(Field::isOpened) &&
                fieldState.stream().filter(Field::hasMine).noneMatch(Field::isOpened))
            return true;
        else return false;
    }

    public void flagField(Coordinate coordinate) {
        if (fieldState.stream().filter(e -> e.getCoordinate().equals(coordinate)).anyMatch(Field::isOpened)) return;
        if (fieldState.stream().filter(e -> e.getCoordinate().equals(coordinate)).anyMatch(Field::hasFlag)) {
            fieldState.stream().filter(e -> e.getCoordinate().equals(coordinate)).forEach(e -> e.setHasFlag(false));
        } else {
            fieldState.stream().filter(e -> e.getCoordinate().equals(coordinate)).forEach(e -> e.setHasFlag(true));
        }
    }

    public int getRemainingMines() {
        int restMine = mines.size() - (int) fieldState.stream().filter(Field::hasFlag).count();
        return restMine;
    }

    public Collection<Field> getMines() {
        Collection<Field> fieldMine = new ArrayList<>();
        for (Coordinate coordinate : mines) {
            fieldMine.add(new Field(coordinate));
        }
        return fieldMine;
    }

    public Collection<Field> getFields() {
        return Collections.unmodifiableCollection(fieldState);
    }

    public RevealFieldsResult revealFields(Coordinate coord) {

        if (fieldState.stream().filter(e -> e.getCoordinate().equals(coord)).anyMatch(e -> e.hasFlag() ||
                e.isOpened())) {
            return new RevealFieldsResult(new ArrayList<>());
        } else {

            if (fieldState.stream().filter(e -> e.getCoordinate().equals(coord)).anyMatch(Field::hasMine)) {
                fieldState.stream().filter(e -> e.getCoordinate().equals(coord)).forEach(e -> e.setOpened(true));
                Collection<Field> abdeckungFields = new ArrayList<>();
                abdeckungFields.add(fieldState.stream().filter(e -> e.getCoordinate().equals(coord)).findAny().get());
                return new RevealFieldsResult(abdeckungFields);
            } else if (fieldState.stream().filter(e -> e.getCoordinate().equals(coord)).anyMatch(e -> !e.hasMine())) {
                fieldState.stream().filter(e -> e.getCoordinate().equals(coord)).forEach(e -> e.setOpened(true));
                Collection<Field> abdeckungFields = new ArrayList<>();
                if (fieldState.stream().filter(e -> e.getCoordinate().equals(coord)).anyMatch(e -> e.getNeighbourMineCount() != 0)) {
                    abdeckungFields.add(fieldState.stream().filter(e -> e.getCoordinate().equals(coord)).findAny().get());
                    return new RevealFieldsResult(abdeckungFields);
                } else if (fieldState.stream().filter(e -> e.getCoordinate().equals(coord)).anyMatch(e -> e.getNeighbourMineCount() == 0)) {
                    for (Field field : listOfbenachbarFeld(coord.getX(), coord.getY())) {
                     /*   fieldState.stream().filter(e -> e.getCoordinate().equals(field.getCoordinate())).filter(e ->
                                !e.isOpened() && !e.hasFlag()).forEach(e -> {
                            e.setOpened(true);
                            abdeckungFields.add(e);
                        });*/
                        abdeckungFields.addAll(revealFields(field.getCoordinate()).getRevealedFields());
                    }

                    abdeckungFields.add(fieldState.stream().filter(e -> e.getCoordinate().equals(coord)).findAny().get());
                    Set<Field> fieldSet= new HashSet<>(abdeckungFields);
                    return new RevealFieldsResult(new ArrayList<>(fieldSet));
                }
            }
        }
        return null;
    }

    public RevealFieldsResult revealMultiClickFields(Coordinate coord) {
        if (fieldState.stream().filter(e -> e.getCoordinate().equals(coord)).anyMatch(e -> !e.isOpened())) {
            return new RevealFieldsResult(new ArrayList<>());
        } else if (fieldState.stream().filter(e -> e.getCoordinate().equals(coord)).anyMatch(e -> e.isOpened() && neighboursMineCount(coord.getX(), coord.getY()) == 0)) {
            return new RevealFieldsResult(new ArrayList<>());
        } else if (fieldState.stream().filter(e -> e.getCoordinate().equals(coord)).anyMatch(e -> e.isOpened() && neighboursMineCount(coord.getX(), coord.getY()) != 0)) {
            if (neighboursMineCount(coord.getX(), coord.getY()) != flaggAnzahl(coord.getX(), coord.getY())) {
                return new RevealFieldsResult(new ArrayList<>());
            } else if (neighboursMineCount(coord.getX(), coord.getY()) == flaggAnzahl(coord.getX(), coord.getY())) {
                Collection<Field> fields = new ArrayList<>();
                for (Field field : listOfbenachbarFeld(coord.getX(), coord.getY())) {
                  /*  if (listOfbenachbarFeld(coord.getX(), coord.getY()).stream().anyMatch(Field::hasMine)) {
                        fieldState.stream().filter(e -> e.getCoordinate().equals(field.getCoordinate())).
                                filter(e -> !e.hasFlag() && !e.isOpened()).forEach(e -> {
                                    e.setOpened(true);
                                    fields.add(e);
                                });
                    } else {
                        fieldState.stream().filter(e -> e.getCoordinate().equals(field.getCoordinate())).
                                filter(e -> !e.hasFlag() && !e.isOpened()).forEach(e -> {
                                    e.setOpened(true);
                                    fields.add(e);
                                });
                    }*/

                    if(fieldState.stream().filter(e -> e.getCoordinate().equals(field.getCoordinate())).anyMatch(e -> !e.hasFlag() && !e.isOpened())){
                        fields.addAll(revealFields(new Coordinate(field.getCoordinate().getX(),field.getCoordinate().getY())).getRevealedFields());
                        //fieldState.stream().filter(e -> e.getCoordinate().equals(field.getCoordinate())).forEach(e->{e.setOpened(true);fields.add(e);});
                        /*if(neighboursMineCount(field.getCoordinate().getX(),field.getCoordinate().getY())==0){
                            for(Field field2 : listOfbenachbarFeld(field.getCoordinate().getX(),field.getCoordinate().getY())){
                                fieldState.stream().filter(e -> e.getCoordinate().equals(field2.getCoordinate())).filter(e->!e.isOpened()).forEach(e->{e.setOpened(true);fields.add(e);});
                            }
                        }*/
                    }
                }
                Set<Field> fieldSet= new HashSet<>(fields);
                return new RevealFieldsResult(new ArrayList<>(fieldSet));
            }
        } return null;
    }

    private int neighboursMineCount(int x, int y) {
        int count = 0;
        int zeile = spielfeld.length;
        int spalte = spielfeld[0].length;
        int[][] nachbarn = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        for (int[] l : nachbarn) {
            int nx = x + l[0];
            int ny = y + l[1];
            if (nx >= 0 && nx < zeile && ny >= 0 && ny < spalte && mines.contains(new Coordinate(nx, ny))) {
                count++;
            }
        }
        return count;
    }

    private int flaggAnzahl(int x, int y) {
        int count = 0;
        int zeile = spielfeld.length;
        int spalte = spielfeld[0].length;
        int[][] nachbarn = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        for (int[] l : nachbarn) {
            int nx = x + l[0];
            int ny = y + l[1];
            if (nx >= 0 && nx < zeile && ny >= 0 && ny < spalte && fieldState.stream().filter(e -> e.getCoordinate().equals(new Coordinate(nx, ny))).anyMatch(Field::hasFlag)) {
                count++;
            }
        }
        return count;
    }

    private List<Field> listOfbenachbarFeld(int x, int y) {
        return fieldState.stream()
                .filter(f -> Math.abs(f.getCoordinate().getX() - x) <= 1 &&
                        Math.abs(f.getCoordinate().getY() - y) <= 1 &&
                        !(f.getCoordinate().getX() == x && f.getCoordinate().getY() == y))
                .collect(Collectors.toList());
    }

    public String toString(List<Field> fieldList) {
        String s = "";
        for (Field field : fieldList) {
            s += "(" + field.getCoordinate().getX() + ", " + field.getCoordinate().getY() + ")";
        }
        return s;
    }


    public static void main(String[] args) {
        Collection<Coordinate> mines = new ArrayList<>();
        mines.add(new Coordinate(2, 2));
        mines.add(new Coordinate(2, 1));
        // mines.add(new Coordinate(3,3));
        Board board = new Board(3, 3, mines);
        board.flagField(new Coordinate(1, 1));

        board.flaggAnzahl(1, 1);

        System.out.println(board.revealMultiClickFields(new Coordinate(2, 1)).getRevealedFields().size());

    }

}
