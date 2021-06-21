
package API.Graphics;

import java.awt.event.*;

/**
 * This class handles the mouse input for the GUI.
 *
 * @author Dan Parii, Lorenzo Pompigna, Nikola Prianikov, Axel Rozental, Konstantin Sandfort, Abhinandan Vasudevan​
 * @version 1.0
 * @since 19 /02/2021
 */
public class MouseInput extends MouseMotionAdapter implements MouseListener, MouseWheelListener {

    // Variables
    private int mouseX = -1;
    private int mouseY = -1;
    private int mouseB = -1;
    private int scroll = 0;
    private final int scene;

    public MouseInput(int scene) {
        this.scene = scene;
    }


    /**
     * Returns the X position of the mouse.
     *
     * @return x x
     */
    public int getX() {
        return this.mouseX;
    }

    /**
     * Returns the Y position of the mouse.
     *
     * @return y y
     */
    public int getY() {
        return this.mouseY;
    }

    /**
     * Returns the state, whether the user is currently scrolling up or not.
     *
     * @return boolean boolean
     */
    public boolean isScrollingUp() {
        return this.scroll == -1;
    }

    /**
     * Returns the state, whether the user is currently scrolling down or not.
     *
     * @return boolean boolean
     */
    public boolean isScrollingDown() {
        return this.scroll == 1;
    }

    public int getScene() {
        return scene;
    }

    /**
     * Resets the scroll of the mouse.
     */
    public void resetScroll() {
        this.scroll = 0;
    }

    /**
     * Returns the type of the button, which is pressed.
     *
     * @return button button
     */
    public ClickType getButton() {
        return switch (this.mouseB) {
            case 1 -> ClickType.LeftClick;
            case 2 -> ClickType.ScrollClick;
            case 3 -> ClickType.RightClick;
            case 4 -> ClickType.BackPage;
            case 5 -> ClickType.ForwardPage;
            default -> ClickType.Unknown;
        };
    }

    /**
     * Resets the mouse button.
     */
    public void resetButton() {
        this.mouseB = -1;
    }


    /**
     * Handles movement of the mouse wheel.
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
        scroll = event.getWheelRotation();
    }


    /**
     * Handles dragged movement of the mouse.
     */
    @Override
    public void mouseDragged(MouseEvent event) {
        this.mouseX = event.getX();
        this.mouseY = event.getY();
    }


    /**
     * Handles the general movement of the mouse.
     */
    @Override
    public void mouseMoved(MouseEvent event) {
        this.mouseX = event.getX();
        this.mouseY = event.getY();
    }


    /**
     * Handles a mouse click.
     */
    @Override
    public void mouseClicked(MouseEvent arg0) {

    }


    /**
     * Handles the cursor entering the frame.
     */
    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }


    /**
     * Handles the cursor exiting the frame.
     */
    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }


    /**
     * Handles a pressed mouse button.
     */
    @Override
    public void mousePressed(MouseEvent event) {
        this.mouseB = event.getButton();
    }


    /**
     * Handles a released mouse button.
     */
    @Override
    public void mouseReleased(MouseEvent arg0) {
        this.mouseB = -1;
    }


    /**
     * Enumerates the click type of the mouse.
     */
    public enum ClickType {
        /**
         * Unknown click type.
         */
        Unknown,
        /**
         * Left click click type.
         */
        LeftClick,
        /**
         * Scroll click click type.
         */
        ScrollClick,
        /**
         * Right click click type.
         */
        RightClick,
        /**
         * Forward page click type.
         */
        ForwardPage,
        /**
         * Back page click type.
         */
        BackPage
    }
}
