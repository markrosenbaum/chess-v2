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

    // Return a list of every square that is "controlled" by this piece.
    public ArrayList<Square> getControlledSquares(Square[][] board, Square start) {
        ArrayList<Square> controlledSquares = new ArrayList<>();

        int row = start.getRow();
        int col = start.getCol();

        // Check for a piece directly adjacent (1 square in any direction)
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

        // Moving backwards to any square behind an enemy piece
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board[r][c].isOccupied() && board[r][c].getOccupyingPiece().getColor() != this.color) {
                    if (isBehindEnemy(start, board[r][c], r, c)) {
                        legalMoves.add(board[r][c]);
                    }
                }
            }
        }
        return legalMoves;
    }

  
    private boolean isBehindEnemy(Square start, Square enemySquare, int eRow, int eCol) {
        int startRow = start.getRow();
        int startCol = start.getCol();
        
        // For white pieces
        if (this.color) {
            // Move up the board for white (lower row numbers)
            return (eRow > startRow) && (eCol == startCol);
        } else {  // For black pieces
            // Move down the board for black (higher row numbers)
            return (eRow < startRow) && (eCol == startCol);
        }
    }
}
