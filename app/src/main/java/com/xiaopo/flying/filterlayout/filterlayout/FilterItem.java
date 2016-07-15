package com.xiaopo.flying.filterlayout.filterlayout;


/**
 * Created by snowbean on 16-7-15.
 */

public class FilterItem {
    private int row;
    private int column;
    private boolean isSelected;
    private int position;

    public FilterItem(int row, int column, boolean isSelected, int position) {
        this.row = row;
        this.column = column;
        this.isSelected = isSelected;
        this.position = position;
    }


    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return "row is "
                + row
                + ", column is "
                + column
                + ", the selected state is "
                + isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + row;
        result = 31 * result + column;
        result = result + (isSelected ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FilterItem)) {
            return false;
        }

        FilterItem position = (FilterItem) o;

        return row == position.row && column == position.column && isSelected == position.isSelected;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
