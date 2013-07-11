package com.javafx.experiments.shape3d;

import javafx.collections.FXCollections;
import javafx.collections.ObservableFloatArray;
import javafx.collections.ObservableIntegerArray;

/**
 * A Mesh where each face can be a Polygon
 *
 * TODO convert to using ObservableIntegerArray
 */
public class PolygonMesh {
    private final ObservableFloatArray points = FXCollections.observableFloatArray();
    private final ObservableFloatArray texCoords = FXCollections.observableFloatArray();
    public int[][] faces = new int[0][0];
    private final ObservableIntegerArray faceSmoothingGroups = FXCollections.observableIntegerArray();
    protected int numEdgesInFaces = -1; // TODO invalidate automatically by listening to faces (whenever it is an observable)

    public PolygonMesh() {}

    public PolygonMesh(float[] points, float[] texCoords, int[][] faces) {
        this.points.addAll(points);
        this.texCoords.addAll(texCoords);
        this.faces = faces;
    }

    public ObservableFloatArray getPoints() {
        return points;
    }
    
    public ObservableFloatArray getTexCoords() {
        return texCoords;
    }
    
    public ObservableIntegerArray getFaceSmoothingGroups() {
        return faceSmoothingGroups;
    }
     
    public int getNumEdgesInFaces() {
        if (numEdgesInFaces == -1) {
            numEdgesInFaces = 0;
            for(int[] face : faces) {
                numEdgesInFaces += face.length;
            }
           numEdgesInFaces /= 2;
        }
        return numEdgesInFaces;
    }
}