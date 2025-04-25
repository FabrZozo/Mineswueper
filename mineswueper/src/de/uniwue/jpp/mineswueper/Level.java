package de.uniwue.jpp.mineswueper;

public class Level {
    private int width; private int height;private int mineCount;private String label;
    public Level(int width, int height, int mineCount, String label) {
        this.height = height;
        this.width = width;
        this.mineCount = mineCount;
        this.label = label;
    }
    public Level(int width, int height, int mineCount) {
        this(width, height, mineCount, "Custom");
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getMineCount() {
        return mineCount;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        // <label>:(<width>x<height>) <mineCount> mines

        return label+":("+width+"x"+height+") "+mineCount+" mines";
    }

    public static Level getBeginner(){
        return new Level(9,9,10,"Beginner");
    }

    public static Level getIntermediate(){
        return new Level(16,16,40,"Intermediate");
    }

    public static Level getExpert(){
        return new Level(30,16,99,"Expert");
    }
}
