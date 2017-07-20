package com.github.alkurop.streetviewmarker;

/**
 * Created by alkurop on 01.06.16.
 */
public class MarkerDrawData {
    final public float left;
    final public float top;
    final public float right;
    final public float bottom;
    final public  MarkerMatrixData matrixData;

    public MarkerDrawData(MarkerMatrixData matrixData, float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.matrixData = matrixData;
    }

    @Override public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof MarkerDrawData)) return false;
        MarkerDrawData that = (MarkerDrawData) o;
        if (Float.compare(that.left, left) != 0) return false;
        if (Float.compare(that.top, top) != 0) return false;
        if (Float.compare(that.right, right) != 0) return false;
        if (Float.compare(that.bottom, bottom) != 0) return false;
        return matrixData != null ? matrixData.equals(that.matrixData) : that.matrixData == null;
    }

    @Override public int hashCode () {
        int result = (left != +0.0f ? Float.floatToIntBits(left) : 0);
        result = 41 * result + (top != +0.0f ? Float.floatToIntBits(top) : 0);
        result = 41 * result + (right != +0.0f ? Float.floatToIntBits(right) : 0);
        result = 41 * result + (bottom != +0.0f ? Float.floatToIntBits(bottom) : 0);
        result = 41 * result + (matrixData != null ? matrixData.hashCode() : 0);
        return result;
    }

    @Override public String toString () {
        return "MarkerDrawData{" +
                  "left=" + left +
                  ", top=" + top +
                  ", right=" + right +
                  ", bottom=" + bottom +
                  '}';
    }
}
