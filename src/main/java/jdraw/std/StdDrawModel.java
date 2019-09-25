/*
 * Copyright (c) 2018 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved.
 */

package jdraw.std;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import jdraw.framework.*;

/**
 * Provide a standard behavior for the drawing model. This class initially does not implement the methods
 * in a proper way.
 * It is part of the course assignments to do so.
 *
 * @author TODO add your name here
 */
public class StdDrawModel implements DrawModel {

    private LinkedList<Figure> figures = new LinkedList<>();
    private List<DrawModelListener> listeners = new ArrayList<>();
    private FigureListener figureListener = e -> notifyListener(e.getFigure(), DrawModelEvent.Type.DRAWING_CHANGED);


    /**
     * Notify all Listeners about a change
     *
     * @param f figure
     * @param type eventtype
     */
    private void notifyListener(Figure f, DrawModelEvent.Type type) {
        listeners.forEach(e -> e.modelChanged(new DrawModelEvent(this, f, type)));
    }


    @Override
    public void addFigure(Figure f) {
        if (figures.contains(f)) return;
        figures.add(f);
        f.addFigureListener(figureListener);
        notifyListener(f, DrawModelEvent.Type.FIGURE_ADDED);
    }

    @Override
    public Stream<Figure> getFigures() {
        return figures.stream();
    }

    @Override
    public void removeFigure(Figure f) {
        if(!figures.contains(f)) {
            throw new IllegalArgumentException("This figure does not exists in the list");
        }
        figures.remove(f);
        notifyListener(f, DrawModelEvent.Type.FIGURE_REMOVED);
    }

    @Override
    public void addModelChangeListener(DrawModelListener listener) {
        // TODO to be implemented
        //System.out.println("StdDrawModel.addModelChangeListener has to be implemented");
        listeners.add(listener);
    }

    @Override
    public void removeModelChangeListener(DrawModelListener listener) {
        if(!listeners.contains(listener)){
            throw new IllegalArgumentException("This listener does not exist");
        }
        listeners.remove(listener);
    }

    /**
     * The draw command handler. Initialized here with a dummy implementation.
     */
    // TODO initialize with your implementation of the undo/redo-assignment.
    private DrawCommandHandler handler = new EmptyDrawCommandHandler();

    /**
     * Retrieve the draw command handler in use.
     *
     * @return the draw command handler.
     */
    @Override
    public DrawCommandHandler getDrawCommandHandler() {
        return handler;
    }


    @Override
    public void setFigureIndex(Figure f, int index) {
        if(!figures.contains(f)){
            throw new IllegalArgumentException("This figure does not exist");
        }
        figures.remove(f);
        figures.add(index, f);
        notifyListener(f, DrawModelEvent.Type.FIGURE_CHANGED);
    }

    //FIXME
    @Override
    public void removeAllFigures() {
        figures.forEach(f -> removeFigure(f));
    }

}