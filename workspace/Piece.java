import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Piece {
    private final boolean color;    
    private BufferedImage img;

    public Piece(boolean isWhite, String img_file) {
        this.color = isWhite;

        try {
            if (this.img == null) {
                this.img = ImageIO.read(getClass().getResource(img_file));
            }
        } catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    public boolean getColor() {
        return color;
    }

    public Image getImage() {
        return img;
    }

    public void draw(Graphics g, Square currentSquare) {
        int x = currentSquare.getX();
        int y = currentSquare.getY();
        g.drawImage(this.img, x, y, null);
    }

    public ArrayList<Square> getControlledSquares(Square[][] board, Square start) {
        ArrayList<Square> controlledSquares = new ArrayList<>();

        int row = start.getRow();
        int col = start.getCol();

        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                if (r == row && c == col) continue; 
                if (r >= 0 && r < 8 && c >= 0 && c < 8) {
                    Square targetSquare = board[r][c];
                    if (targetSquare.isOccupied() && targetSquare.getOccupyingPiece().getColor() != this.color) {
                        controlledSquares.add(targetSquare);
                    }
                }
            }
        }
        return controlledSquares;
    }

    public ArrayList<Square> getLegalMoves(Board b, Square start) {
        ArrayList<Square> legalMoves = new ArrayList<>();
        Square[][] board = b.getSquareArray(); 

        int row = start.getRow();
        int col = start.getCol();

        System.out.println("Checking legal moves for piece at: " + row + ", " + col);

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board[r][c].isOccupied()) {
                    Piece occupyingPiece = board[r][c].getOccupyingPiece();
                    System.out.println("Piece found at (" + r + ", " + c + "): " + 
                        (occupyingPiece.getColor() ? "White" : "Black"));

                    if (occupyingPiece.getColor() != this.color) {
                        System.out.println("Enemy piece detected at: " + r + ", " + c);

                        int newRow = this.color ? r + 1 : r - 1;
                        if (newRow >= 0 && newRow < 8) {
                            Square destination = board[newRow][c];
                            if (!destination.isOccupied()) { 
                                legalMoves.add(destination);
                                System.out.println("Teleport move added to: " + newRow + ", " + c);
                            }
                        }
                    }
                }
            }
        }

        if (legalMoves.isEmpty()) {
            System.out.println("No legal moves available.");
        } else {
            System.out.println("Legal moves found: " + legalMoves.size());
        }

        return legalMoves;
    }
}
