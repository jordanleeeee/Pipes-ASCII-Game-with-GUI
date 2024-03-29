package controllers;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;
import models.Config;
import models.map.cells.Cell;
import models.map.cells.FillableCell;
import models.map.cells.TerminationCell;
import models.pipes.Pipe;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Helper class for render operations on a {@link Canvas}.
 */
public class Renderer {

    /**
     * Padding between two tiles in a queue.
     */
    private static final int QUEUE_TILE_PADDING = 8;

    /**
     * An image of a cell, with support for rotated images.
     */
    public static class CellImage {

        /**
         * Image of the cell.
         */
        @NotNull
        final Image image;
        /**
         * Rotation of the image.
         */
        final float rotation;

        /**
         * @param image    Image of the cell.
         * @param rotation Rotation of the image.
         */
        public CellImage(@NotNull Image image, float rotation) {
            this.image = image;
            this.rotation = rotation;
        }
    }

    /**
     * Sets the current rotation of a {@link GraphicsContext}.
     *
     * @param gc     Target Graphics Context.
     * @param angle  Angle to rotate the context by.
     * @param pivotX X-coordinate of the pivot point.
     * @param pivotY Y-coordinate of the pivot point.
     */
    private static void rotate(@NotNull GraphicsContext gc, double angle, double pivotX, double pivotY) {
        final var r = new Rotate(angle, pivotX, pivotY);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

    /**
     * Draws a rotated image onto a {@link GraphicsContext}.
     *
     * @param gc    Target Graphics Context.
     * @param image Image to draw.
     * @param angle Angle to rotate the image by.
     * @param x     X-coordinate relative to the graphics context to draw the top-left of the image.
     * @param y     Y-coordinate relative to the graphics context to draw the top-left of the image.
     */
    private static void drawRotatedImage(@NotNull GraphicsContext gc, @NotNull Image image, double angle, double x, double y) {
        // TODO
        //System.out.println("draw rotated image");
        rotate(gc, angle, x+Config.TILE_SIZE/2, y+Config.TILE_SIZE/2);
        gc.drawImage(image, x, y);
    }

    /**
     * Renders a map into a {@link Canvas}.
     *
     * @param canvas Canvas to render to.
     * @param map    Map to render.
     */
    public static void renderMap(@NotNull Canvas canvas, @NotNull Cell[][] map) {
        // TODO
        //System.out.println("map");
        canvas.setWidth(Config.TILE_SIZE*map[0].length);
        canvas.setHeight(Config.TILE_SIZE*map.length);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for(int i=0; i<map.length; i++){
            for(int j=0; j<map[0].length; j++) {
                //System.out.print(map[i][j].toSingleChar());
                CellImage temp = map[i][j].getImageRep();
                if(map[i][j] instanceof FillableCell){
                    FillableCell fillableCell = (FillableCell)map[i][j];
                    if(!fillableCell.getPipe().isEmpty()){
                        temp = fillableCell.getPipe().get().getImageRep();
                    }
                }
                drawRotatedImage(gc, temp.image, temp.rotation,j*Config.TILE_SIZE,i*Config.TILE_SIZE);
            }
            //System.out.println();
        }
    }

    /**
     * Renders a pipe queue into a {@link Canvas}.
     *
     * @param canvas    Canvas to render to.
     * @param pipeQueue Pipe queue to render.
     */
    public static void renderQueue(@NotNull Canvas canvas, @NotNull List<Pipe> pipeQueue) {
        // TODO
        canvas.setWidth((Config.TILE_SIZE + QUEUE_TILE_PADDING)*5 - QUEUE_TILE_PADDING*2);
        canvas.setHeight(Config.TILE_SIZE + QUEUE_TILE_PADDING*2);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for(int i=0; i<pipeQueue.size(); i++){
            CellImage temp = pipeQueue.get(i).getImageRep();
            drawRotatedImage(gc, temp.image, temp.rotation,i*(Config.LIST_CELL_HEIGHT + QUEUE_TILE_PADDING), QUEUE_TILE_PADDING);
        }
    }
}
