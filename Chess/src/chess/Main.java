package chess;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pieces.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.HashMap;

/**
 * @author Ashish Kedia and Adarsh Mohata
 *
 */

/**
 * This is the Main Class of our project. All GUI Elements are declared,
 * initialized and used in this class itself. It is inherited from the JFrame
 * Class of Java's Swing Library.
 * 
 */

public class Main extends JFrame implements MouseListener {
    private static final long serialVersionUID = 1L;

    // Variable Declaration
    private static final int Height = 800;
    private static final int Width = 1200;
    private static List<Rook> whiteRooks, blackRooks;
    private static List<Knight> whiteKnights, blackKnights;
    private static List<Bishop> whiteBishops, blackBishops;
    private static List<Pawn> whitePawns, blackPawns;
    private static List<Queen> whiteQueens, blackQueens;
    private static King whiteKing;
    private static King blackKing;
    private Cell c, previous;
    private int chance = 0;
    private Cell boardState[][];
    private ArrayList<Cell> destinationlist = new ArrayList<Cell>();
    private Player White = null, Black = null;
    private JPanel board = new JPanel(new GridLayout(8, 8));
    private JPanel wdetails = new JPanel(new GridLayout(3, 3));
    private JPanel bdetails = new JPanel(new GridLayout(3, 3));
    private JPanel wcombopanel = new JPanel();
    private JPanel bcombopanel = new JPanel();
    private JPanel controlPanel, WhitePlayer, BlackPlayer, temp, displayTime, showPlayer, time;
    private JSplitPane split;
    private JLabel label, mov;
    private static JLabel CHNC;
    private Time timer;
    public static Main Mainboard;
    private boolean selected = false, end = false;
    private Container content;
    private ArrayList<Player> wplayer, bplayer;
    private ArrayList<String> Wnames = new ArrayList<String>();
    private ArrayList<String> Bnames = new ArrayList<String>();
    private JComboBox<String> wcombo, bcombo;
    private String wname = null, bname = null, winner = null;
    static String move;
    private Player tempPlayer;
    private JScrollPane wscroll, bscroll;
    private String[] WNames = {}, BNames = {};
    private JSlider timeSlider;
    private BufferedImage image;
    private JButton start, wselect, bselect, WNewPlayer, BNewPlayer, playVsAI;
    public static int timeRemaining = 60;
    private static HashMap<String, Integer> stateHash;
    private static int trivialMoveCounter = 0;
    private static String lastMoveNotation;
    private static ArrayList<String> movesHistoryNotation = new ArrayList<String>();

    public static void main(String[] args) {

        initializePieces();

        // Setting up the board
        Mainboard = new Main();
        Mainboard.setVisible(true);
        Mainboard.setResizable(false);
        stateHash = new HashMap<String, Integer>();
    }

    // Constructor
    private Main() {
        timeRemaining = 60;
        timeSlider = new JSlider();
        move = "White";
        wname = null;
        bname = null;
        winner = null;
        board = new JPanel(new GridLayout(8, 8));
        wdetails = new JPanel(new GridLayout(3, 3));
        bdetails = new JPanel(new GridLayout(3, 3));
        bcombopanel = new JPanel();
        wcombopanel = new JPanel();
        Wnames = new ArrayList<String>();
        Bnames = new ArrayList<String>();
        board.setMinimumSize(new Dimension(800, 700));
        ImageIcon img = new ImageIcon(this.getClass().getResource("icon.png"));
        this.setIconImage(img.getImage());

        // Time Slider Details
        timeSlider.setMinimum(1);
        timeSlider.setMaximum(15);
        timeSlider.setValue(1);
        timeSlider.setMajorTickSpacing(2);
        timeSlider.setPaintLabels(true);
        timeSlider.setPaintTicks(true);
        timeSlider.addChangeListener(new TimeChange());

        // Fetching Details of all Players
        wplayer = Player.fetch_players();
        Iterator<Player> witr = wplayer.iterator();
        while (witr.hasNext())
            Wnames.add(witr.next().name());

        bplayer = Player.fetch_players();
        Iterator<Player> bitr = bplayer.iterator();
        while (bitr.hasNext())
            Bnames.add(bitr.next().name());
        WNames = Wnames.toArray(WNames);
        BNames = Bnames.toArray(BNames);

        Cell cell;
        board.setBorder(BorderFactory.createLoweredBevelBorder());
        pieces.Piece P;
        content = getContentPane();
        setSize(Width, Height);
        setTitle("Chess");
        content.setBackground(Color.black);
        controlPanel = new JPanel();
        content.setLayout(new BorderLayout());
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createTitledBorder(null, "Statistics", TitledBorder.TOP,
                TitledBorder.CENTER, new Font("Lucida Calligraphy", Font.PLAIN, 20), Color.ORANGE));

        // Defining the Player Box in Control Panel
        WhitePlayer = new JPanel();
        WhitePlayer.setBorder(BorderFactory.createTitledBorder(null, "White Player", TitledBorder.TOP,
                TitledBorder.CENTER, new Font("times new roman", Font.BOLD, 18), Color.RED));
        WhitePlayer.setLayout(new BorderLayout());

        BlackPlayer = new JPanel();
        BlackPlayer.setBorder(BorderFactory.createTitledBorder(null, "Black Player", TitledBorder.TOP,
                TitledBorder.CENTER, new Font("times new roman", Font.BOLD, 18), Color.BLUE));
        BlackPlayer.setLayout(new BorderLayout());

        JPanel whitestats = new JPanel(new GridLayout(3, 3));
        JPanel blackstats = new JPanel(new GridLayout(3, 3));
        wcombo = new JComboBox<String>(WNames);
        bcombo = new JComboBox<String>(BNames);
        wscroll = new JScrollPane(wcombo);
        bscroll = new JScrollPane(bcombo);
        wcombopanel.setLayout(new FlowLayout());
        bcombopanel.setLayout(new FlowLayout());
        wselect = new JButton("Select");
        bselect = new JButton("Select");
        wselect.addActionListener(new SelectHandler(0));
        bselect.addActionListener(new SelectHandler(1));
        WNewPlayer = new JButton("New Player");
        BNewPlayer = new JButton("New Player");
        WNewPlayer.addActionListener(new Handler(0));
        BNewPlayer.addActionListener(new Handler(1));
        wcombopanel.add(wscroll);
        wcombopanel.add(wselect);
        wcombopanel.add(WNewPlayer);
        bcombopanel.add(bscroll);
        bcombopanel.add(bselect);
        bcombopanel.add(BNewPlayer);
        WhitePlayer.add(wcombopanel, BorderLayout.NORTH);
        BlackPlayer.add(bcombopanel, BorderLayout.NORTH);
        whitestats.add(new JLabel("Name   :"));
        whitestats.add(new JLabel("Played :"));
        whitestats.add(new JLabel("Won    :"));
        blackstats.add(new JLabel("Name   :"));
        blackstats.add(new JLabel("Played :"));
        blackstats.add(new JLabel("Won    :"));
        playVsAI = new JButton("Play vs AI");
        playVsAI.setBackground(Color.black);
        playVsAI.setForeground(Color.white);
        playVsAI.addActionListener(new PlayAIHandler());
        playVsAI.setPreferredSize(new Dimension(120, 40));
        playVsAI.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPanel.add(playVsAI);        

        WhitePlayer.add(whitestats, BorderLayout.WEST);
        BlackPlayer.add(blackstats, BorderLayout.WEST);
        controlPanel.add(WhitePlayer);
        controlPanel.add(BlackPlayer);

        // Defining all the Cells
        boardState = new Cell[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                P = null;
                if (i == 0 && j == 0)
                    P = blackRooks.get(0);
                else if (i == 0 && j == 7)
                    P = blackRooks.get(1);
                else if (i == 7 && j == 0)
                    P = whiteRooks.get(0);
                else if (i == 7 && j == 7)
                    P = whiteRooks.get(1);
                else if (i == 0 && j == 1)
                    P = blackKnights.get(0);
                else if (i == 0 && j == 6)
                    P = blackKnights.get(1);
                else if (i == 7 && j == 1)
                    P = whiteKnights.get(0);
                else if (i == 7 && j == 6)
                    P = whiteKnights.get(1);
                else if (i == 0 && j == 2)
                    P = blackBishops.get(0);
                else if (i == 0 && j == 5)
                    P = blackBishops.get(1);
                else if (i == 7 && j == 2)
                    P = whiteBishops.get(0);
                else if (i == 7 && j == 5)
                    P = whiteBishops.get(1);
                else if (i == 0 && j == 3)
                    P = blackQueens.get(0);
                else if (i == 0 && j == 4)
                    P = blackKing;
                else if (i == 7 && j == 3)
                    P = whiteQueens.get(0);
                else if (i == 7 && j == 4)
                    P = whiteKing;
                else if (i == 1)
                    P = blackPawns.get(j);
                else if (i == 6)
                    P = whitePawns.get(j);
                cell = new Cell(i, j, P);
                cell.addMouseListener(this);
                board.add(cell);
                boardState[i][j] = cell;
            }
        showPlayer = new JPanel(new FlowLayout());
        showPlayer.add(timeSlider);
        JLabel setTime = new JLabel("Set Timer(in mins):");
        start = new JButton("Start");
        start.setBackground(Color.black);
        start.setForeground(Color.white);
        start.addActionListener(new START());
        start.setPreferredSize(new Dimension(120, 40));
        setTime.setFont(new Font("Arial", Font.BOLD, 16));
        label = new JLabel("Time Starts now", JLabel.CENTER);
        label.setFont(new Font("SERIF", Font.BOLD, 30));
        displayTime = new JPanel(new FlowLayout());
        time = new JPanel(new GridLayout(3, 3));
        time.add(setTime);
        time.add(showPlayer);
        displayTime.add(start);
        time.add(displayTime);
        controlPanel.add(time);
        board.setMinimumSize(new Dimension(800, 700));

        // The Left Layout When Game is inactive
        temp = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                try {
                    image = ImageIO.read(this.getClass().getResource("clash.jpg"));
                } catch (IOException ex) {
                    System.out.println("not found");
                }

                g.drawImage(image, 0, 0, null);
            }
        };

        temp.setMinimumSize(new Dimension(800, 700));
        controlPanel.setMinimumSize(new Dimension(285, 700));
        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, temp, controlPanel);

        content.add(split);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    class PlayAIHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            switchToAIMode();
        }
    }

    private void switchToAIMode() {
        content.removeAll();
    
        JPanel aiModePanel = new JPanel(new BorderLayout());
    
        JLabel statusDisplay = new JLabel("Play vs AI Mode", JLabel.CENTER);
        statusDisplay.setFont(new Font("Arial", Font.BOLD, 28));
        statusDisplay.setForeground(Color.BLUE);
        statusDisplay.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); 

        JPanel playerDetailsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        playerDetailsPanel.setBorder(BorderFactory.createTitledBorder(null, "Statistics", TitledBorder.TOP, TitledBorder.CENTER, new Font("Lucida Calligraphy", Font.PLAIN, 20), Color.ORANGE));
    
        JPanel whitePlayerPanel = new JPanel(new BorderLayout());
        whitePlayerPanel.setBorder(BorderFactory.createTitledBorder(null, "Player", TitledBorder.TOP, TitledBorder.CENTER, new Font("times new roman", Font.BOLD, 18), Color.RED));
        JPanel whiteStats = new JPanel(new GridLayout(3, 1));
        whiteStats.add(new JLabel("Name   : Player"));
        whiteStats.add(new JLabel("Played : 1"));
        whiteStats.add(new JLabel("Won    : 0"));
        whitePlayerPanel.add(whiteStats, BorderLayout.CENTER);
    
        JPanel blackPlayerPanel = new JPanel(new BorderLayout());
        blackPlayerPanel.setBorder(BorderFactory.createTitledBorder(null, "AI", TitledBorder.TOP, TitledBorder.CENTER, new Font("times new roman", Font.BOLD, 18), Color.BLUE));
        JPanel blackStats = new JPanel(new GridLayout(3, 1));
        blackStats.add(new JLabel("Name   : AI"));
        blackStats.add(new JLabel("Played : 1"));
        blackStats.add(new JLabel("Won    : 0"));
        blackPlayerPanel.add(blackStats, BorderLayout.CENTER);
    
        playerDetailsPanel.add(whitePlayerPanel);
        playerDetailsPanel.add(blackPlayerPanel);
    
        aiModePanel.add(statusDisplay, BorderLayout.NORTH);
        aiModePanel.add(playerDetailsPanel, BorderLayout.WEST);
    
        JPanel boardWrapper = new JPanel(new BorderLayout());
        boardWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 
        boardWrapper.add(board, BorderLayout.CENTER);
        aiModePanel.add(boardWrapper, BorderLayout.CENTER);
    
        // Remove the button from normal mode completely --->>> Failed for now
        if (playVsAI != null && playVsAI.getParent() == controlPanel) {
            controlPanel.remove(playVsAI);  
            playVsAI = null;  
            controlPanel.revalidate();  
            controlPanel.repaint();    
        }
    
        content.add(aiModePanel);
        content.validate();
        content.repaint();
    }
    
    // A function to change the chance from White Player to Black Player or vice
    // verse
    // It is made public because it is to be accessed in the Time Class
    public void changechance() {
        if (boardState[getKing(chance).getx()][getKing(chance).gety()].ischeck()) {
            chance ^= 1;
            triggerMate();
        }
        if (destinationlist.isEmpty() == false)
            cleandestinations(destinationlist);
        if (previous != null)
            previous.deselect();
        previous = null;
        chance ^= 1;
        if (isDrawByInsufficientMaterial()) {
            System.out.println("Insufficient material found.");
            triggerDraw("Draw by insufficient material");
            return; 
        } else {
            System.out.println("Insufficient material not found.");
        }
        if (!end && timer != null) {
            timer.reset();
            timer.start();
            showPlayer.remove(CHNC);
            if (Main.move == "White")
                Main.move = "Black";
            else
                Main.move = "White";
            CHNC.setText(Main.move);
            showPlayer.add(CHNC);
        }
        BoardState boardStateClass = new BoardState(boardState);
        String stateString = boardStateClass.buildString();
        if (stateHash.get(stateString) == null) {
        	stateHash.put(stateString, 1);
        } else {
        	stateHash.replace(stateString, stateHash.get(stateString) + 1);
        }
        if (stateHash.get(stateString).intValue() == 3) {
        	triggerDraw("Threefold Repetition");
        }
    }

    // A function to retrieve the Black King or White King
    private King getKing(int color) {
        if (color == 0)
            return whiteKing;
        else
            return blackKing;
    }

    private int[] findKing(Cell[][] position, int color){
        for (Cell[] row : position) {
            for (Cell cell : row){
                Piece temp = cell.getpiece();

                if (temp != null && temp instanceof King && temp.getcolor() == color){
                    int[] result = {cell.x, cell.y};
                    return result;
                }
            }
        }

        return null;
    }
    
    private boolean isStalemate(int color, Cell[][] position) {
    	boolean isStaleMate = true;
    	List<Piece> pieces = getPieces(color, position);
    	
    	int i = 0;
    	while (isStaleMate && i < pieces.size()) {
    		Piece piece = pieces.get(i);
    		ArrayList<Cell> moves = piece.move(position);
    		
    		if (piece instanceof King) { 
                moves = willkingbeindanger(position[piece.getx()][piece.gety()], moves);
            }
    		
    		isStaleMate = moves.size() == 0;
    		i++;
    	}
    	
    	return isStaleMate;
    }
    
    private List<Piece> getPieces(int color, Cell[][] position){
    	List<Piece> pieces = new ArrayList<Piece>();
    	
    	for (int i = 0; i < 8; i++) {
    		for (int j = 0; j < 8; j++) {
    			Piece piece = position[i][j].getpiece();
    			
    			if (piece != null && color == piece.getcolor()) pieces.add(piece);
    		}
    	}
    	
    	return pieces;
    }

    // A function to clean the highlights of possible destination cells
    private void cleandestinations(ArrayList<Cell> destlist) // Function to clear the last move's destinations
    {
        ListIterator<Cell> it = destlist.listIterator();
        while (it.hasNext())
            it.next().removepossibledestination();
    }

    // A function that indicates the possible moves by highlighting the Cells
    private void highlightdestinations(ArrayList<Cell> destlist) {
        ListIterator<Cell> it = destlist.listIterator();
        while (it.hasNext())
            it.next().setpossibledestination();
    }

    // Function to check if the king will be in danger if the given move is made
    private boolean willkingbeindanger(Cell fromcell, Cell tocell) {
        Cell newboardstate[][] = new Cell[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                try {
                    newboardstate[i][j] = new Cell(boardState[i][j]);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                    System.out.println("There is a problem with cloning !!");
                }
            }

        if (newboardstate[tocell.x][tocell.y].getpiece() != null)
            newboardstate[tocell.x][tocell.y].removePiece();

        Piece movingPiece = newboardstate[fromcell.x][fromcell.y].getpiece();
        newboardstate[tocell.x][tocell.y].setPiece(movingPiece);
        newboardstate[fromcell.x][fromcell.y].removePiece();
        
        int[] position = findKing(newboardstate, movingPiece.getcolor());
        King kingInNewBoard = (King) newboardstate[position[0]][position[1]].getpiece();
        if (kingInNewBoard.isindanger(newboardstate, position[0], position[1]) == true)
            return true;
        else
            return false;
    }
    
    private ArrayList<Cell> willkingbeindanger(Cell fromCell,List<Cell> destinations) {
    	ArrayList<Cell> filteredMoves = new ArrayList<Cell>();
    	
    	for (int i = 0; i < destinations.size(); i++) {
    		Cell toCell = destinations.get(i);
    		if (!willkingbeindanger(fromCell, toCell)) filteredMoves.add(toCell);
    	}
    	
    	return filteredMoves;
    }

    // A function to eliminate the possible moves that will put the King in danger
    private ArrayList<Cell> filterdestination(ArrayList<Cell> destlist, Cell fromcell) {
        ArrayList<Cell> newlist = new ArrayList<Cell>();
        Cell newboardstate[][] = new Cell[8][8];
        ListIterator<Cell> it = destlist.listIterator();
        int x, y;
        while (it.hasNext()) {
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++) {
                    try {
                        newboardstate[i][j] = new Cell(boardState[i][j]);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }

            Cell tempc = it.next();
            if (newboardstate[tempc.x][tempc.y].getpiece() != null)
                newboardstate[tempc.x][tempc.y].removePiece();
            newboardstate[tempc.x][tempc.y].setPiece(newboardstate[fromcell.x][fromcell.y].getpiece());
            x = getKing(chance).getx();
            y = getKing(chance).gety();
            if (newboardstate[fromcell.x][fromcell.y].getpiece() instanceof King) {
                ((King) (newboardstate[tempc.x][tempc.y].getpiece())).setx(tempc.x);
                ((King) (newboardstate[tempc.x][tempc.y].getpiece())).sety(tempc.y);
                x = tempc.x;
                y = tempc.y;
            }
            newboardstate[fromcell.x][fromcell.y].removePiece();
            if ((((King) (newboardstate[x][y].getpiece())).isindanger(newboardstate) == false))
                newlist.add(tempc);
        }
        return newlist;
    }

    // A Function to filter the possible moves when the king of the current player
    // is under Check
    private ArrayList<Cell> incheckfilter(ArrayList<Cell> destlist, Cell fromcell, int color) {
        ArrayList<Cell> newlist = new ArrayList<Cell>();
        Cell newboardstate[][] = new Cell[8][8];
        ListIterator<Cell> it = destlist.listIterator();
        int x, y;
        while (it.hasNext()) {
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++) {
                    try {
                        newboardstate[i][j] = new Cell(boardState[i][j]);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            Cell tempc = it.next();
            if (newboardstate[tempc.x][tempc.y].getpiece() != null)
                newboardstate[tempc.x][tempc.y].removePiece();
            newboardstate[tempc.x][tempc.y].setPiece(newboardstate[fromcell.x][fromcell.y].getpiece());
            x = getKing(color).getx();
            y = getKing(color).gety();
            if (newboardstate[tempc.x][tempc.y].getpiece() instanceof King) {
                ((King) (newboardstate[tempc.x][tempc.y].getpiece())).setx(tempc.x);
                ((King) (newboardstate[tempc.x][tempc.y].getpiece())).sety(tempc.y);
                x = tempc.x;
                y = tempc.y;
            }
            newboardstate[fromcell.x][fromcell.y].removePiece();
            if ((((King) (newboardstate[x][y].getpiece())).isindanger(newboardstate) == false))
                newlist.add(tempc);
        }
        return newlist;
    }

    // A function to check if the King is check-mate. The Game Ends if this function
    // returns true.
    public boolean checkmate(int color) {
        ArrayList<Cell> dlist = new ArrayList<Cell>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (boardState[i][j].getpiece() != null && boardState[i][j].getpiece().getcolor() == color) {
                    dlist.clear();
                    dlist = boardState[i][j].getpiece().move(boardState, i, j);
                    dlist = incheckfilter(dlist, boardState[i][j], color);
                    if (dlist.size() != 0)
                        return false;
                }
            }
        }
        return true;
    }
    
    private void triggerMate() {
        gameend(false, null);
    }
    
    private void triggerDraw(String message) {
        gameend(true, message);
    }
  
    private boolean isDrawByInsufficientMaterial() {

        List<Piece> whitePieces = getPieces(0, boardState);
        List<Piece> blackPieces = getPieces(1, boardState);
    
       
        if (whitePieces.size() == 1 && blackPieces.size() == 1) {
            return true; // King vs King
        }
    
        if (whitePieces.size() == 1 && blackPieces.size() == 2) {
            Piece otherPiece = blackPieces.stream().filter(p -> !(p instanceof King)).findFirst().orElse(null);
            if (otherPiece instanceof Knight || otherPiece instanceof Bishop) {
                return true; // King vs King and Knight/Bishop
            }
        } else if (whitePieces.size() == 2 && blackPieces.size() == 1) {
            Piece otherPiece = whitePieces.stream().filter(p -> !(p instanceof King)).findFirst().orElse(null);
            if (otherPiece instanceof Knight || otherPiece instanceof Bishop) {
                return true; // King and Knight/Bishop vs King
            }
        } else if (whitePieces.size() == 2 && blackPieces.size() == 2) {
            Piece whiteBishop = whitePieces.stream().filter(p -> p instanceof Bishop).findFirst().orElse(null);
            Piece blackBishop = blackPieces.stream().filter(p -> p instanceof Bishop).findFirst().orElse(null);
    
            if (whiteBishop != null && blackBishop != null &&
                ((whiteBishop.getx() + whiteBishop.gety()) % 2 == (blackBishop.getx() + blackBishop.gety()) % 2)) {
                return true; // King and Bishop (same color) vs King and Bishop (same color)
            }
        }
    
        return false;
    }
  
    @SuppressWarnings("deprecation")
    private void gameend(boolean isDraw, String message) {
        cleandestinations(destinationlist);
        displayTime.disable();
        timer.countdownTimer.stop();
        if (previous != null)
            previous.removePiece();
        if (!isDraw) {
            if (chance == 0) {
                White.updateGamesWon();
                White.Update_Player();
                winner = White.name();
            } else {
                Black.updateGamesWon();
                Black.Update_Player();
                winner = Black.name();
            }
            JOptionPane.showMessageDialog(board, "Checkmate!!!\n" + winner + " wins");
        } else {
            JOptionPane.showMessageDialog(board, "Draw.\n" + message);
        }
        
        WhitePlayer.remove(wdetails);
        BlackPlayer.remove(bdetails);
        displayTime.remove(label);

        displayTime.add(start);
        showPlayer.remove(mov);
        showPlayer.remove(CHNC);
        showPlayer.revalidate();
        showPlayer.add(timeSlider);

        split.remove(board);
        split.add(temp);
        WNewPlayer.enable();
        BNewPlayer.enable();
        wselect.enable();
        bselect.enable();
        end = true;
        Mainboard.disable();
        Mainboard.dispose();

        clearPieces();
        initializePieces();

        resetTrivialMoveCounter();

        Mainboard = new Main();
        Mainboard.setVisible(true);
        Mainboard.setResizable(false);
        stateHash.clear();
    }

    // These are the abstract function of the parent class. Only relevant method
    // here is the On-Click Fuction
    // which is called when the user clicks on a particular cell
    @Override
    public void mouseClicked(MouseEvent arg0) {
        // TODO Auto-generated method stub
        c = (Cell) arg0.getSource();
        if (previous == null) {
            if (c.getpiece() != null) {
                if (c.getpiece().getcolor() != chance)
                    return;
                c.select();
                previous = c;
                destinationlist.clear();
                destinationlist = c.getpiece().move(boardState, c.x, c.y);
                if (c.getpiece() instanceof King)
                    destinationlist = filterdestination(destinationlist, c);
                else {
                    if (boardState[getKing(chance).getx()][getKing(chance).gety()].ischeck())
                        destinationlist = new ArrayList<Cell>(filterdestination(destinationlist, c));
                    else if (destinationlist.isEmpty() == false && willkingbeindanger(c, destinationlist.get(0)))
                        destinationlist.clear();
                }
                highlightdestinations(destinationlist);
            }
        } else {
            if (c.x == previous.x && c.y == previous.y) {
                c.deselect();
                cleandestinations(destinationlist);
                destinationlist.clear();
                previous = null;
            } else if (c.getpiece() == null || previous.getpiece().getcolor() != c.getpiece().getcolor()) {
                // This increments move count for rook and king when moved
            	if (c.ispossibledestination()) {
            	    // Check for pawn promotion if moved to final row
                    if (previous.getpiece() instanceof Pawn && (c.x == 0 || c.x == 7)) {
                        Piece promotedPiece;
                        do {
                            promotedPiece = ((Pawn) previous.getpiece()).promote();
                        } while (promotedPiece == null);
                        previous.setPiece(promotedPiece);  // Set the promoted piece in the final cell
                        lastMoveNotation = generateNotation(previous.x, previous.y, c.x, c.y, getCharacterFromPiece(promotedPiece));
                    } else {
                        lastMoveNotation = generateNotation(previous.x, previous.y, c.x, c.y, null);
                    }
                    // Temporarily print the notation of the last move
                    System.out.println(lastMoveNotation);
                    movesHistoryNotation.add(lastMoveNotation);
                	if (previous.getpiece() instanceof Rook || previous.getpiece() instanceof King) {
                    	previous.getpiece().incrementMoveCount();
                    }
                	// This does queen side castle below
                	if ((previous.getpiece() instanceof King) && (c.y - previous.y == 2)) {
                		c.setPiece(previous.getpiece());
                		boardState[c.x][c.y-1].setPiece(boardState[c.x][7].getpiece());
                		if (boardState[c.x][7].ischeck()) {
                			boardState[c.x][7].removecheck();
                		}
                		boardState[c.x][7].removePiece();
                		boardState[c.x][7].invalidate();
                		boardState[c.x][7].validate();
                		boardState[c.x][7].repaint();
                	// This does king side castle below
                	} else if ((previous.getpiece() instanceof King) && (c.y - previous.y == -2)) {
                		c.setPiece(previous.getpiece());
                		boardState[c.x][c.y+1].setPiece(boardState[c.x][0].getpiece());
                		if (boardState[c.x][0].ischeck()) {
                			boardState[c.x][0].removecheck();
                		}
                		boardState[c.x][0].removePiece();
                		boardState[c.x][0].invalidate();
                		boardState[c.x][0].validate();
                		boardState[c.x][0].repaint();
                	// This is the original code for the move function
                	} else {
	                    if (c.getpiece() != null) {
                            //A piece was captured
                            resetTrivialMoveCounter();
	                        c.removePiece();
	                    } else if (previous.getpiece() instanceof Pawn) {
	                        if (Math.abs(previous.y - c.y) > 0) {
	                            // The pawn moved diagonally onto a vacant square
	                            // En Passant, act accordingly
	                            boardState[previous.x][c.y].removePiece();
	                            boardState[previous.x][c.y].invalidate();
	                            boardState[previous.x][c.y].validate();
	                            boardState[previous.x][c.y].repaint();
	                        }

                            //reset counter if pawn moves
                            resetTrivialMoveCounter();
	                    }
	                    c.setPiece(previous.getpiece());
                	}
                    if (previous.ischeck())
                        previous.removecheck();
                    previous.removePiece();
                    for (Pawn p : whitePawns) {
                        p.setJustSkipped(false);
                    }
                    for (Pawn p : blackPawns) {
                        p.setJustSkipped(false);
                    }
                    // Check if pawn just skipped
                    if (c.getpiece() instanceof Pawn) {
                        if (Math.abs(previous.x - c.x) == 2) {
                            ((Pawn)c.getpiece()).setJustSkipped(true);
                        }
                    }
                    if (getKing(chance ^ 1).isindanger(boardState)) {
                        boardState[getKing(chance ^ 1).getx()][getKing(chance ^ 1).gety()].setcheck();
                        if (checkmate(getKing(chance ^ 1).getcolor())) {
                            previous.deselect();
                            if (previous.getpiece() != null)
                                previous.removePiece();
                            triggerMate();
                        }
                    }
                    if (getKing(chance).isindanger(boardState) == false)
                        boardState[getKing(chance).getx()][getKing(chance).gety()].removecheck();
                    c.getpiece().setx(c.x);
                    c.getpiece().sety(c.y);
                    changechance();
                    
                    //Check for stalemate
                    if (isStalemate(chance, boardState)) {
                    	triggerDraw("Stalemate");
                    }

                    increaseTrivialMoveCounter();
                    if (trivialMoveCounter >= 50){
                        triggerDraw("50 Move Rule");
                    }
                    
                    if (!end) {
                        timer.reset();
                        timer.start();
                    }
                }
                if (previous != null) {
                    previous.deselect();
                    previous = null;
                }
                cleandestinations(destinationlist);
                destinationlist.clear();
            } else if (previous.getpiece().getcolor() == c.getpiece().getcolor()) {
                previous.deselect();
                cleandestinations(destinationlist);
                destinationlist.clear();
                c.select();
                previous = c;
                destinationlist = c.getpiece().move(boardState, c.x, c.y);
                if (c.getpiece() instanceof King)
                    destinationlist = filterdestination(destinationlist, c);
                else {
                    if (boardState[getKing(chance).getx()][getKing(chance).gety()].ischeck())
                        destinationlist = new ArrayList<Cell>(filterdestination(destinationlist, c));
                    else if (destinationlist.isEmpty() == false && willkingbeindanger(c, destinationlist.get(0)))
                        destinationlist.clear();
                }
                highlightdestinations(destinationlist);
            }
        }
        if (c.getpiece() != null && c.getpiece() instanceof King) {
            ((King) c.getpiece()).setx(c.x);
            ((King) c.getpiece()).sety(c.y);
        }
        
    }

    // Other Irrelevant abstract function. Only the Click Event is captured.
    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

    class START implements ActionListener {

        @SuppressWarnings("deprecation")
        @Override
        public void actionPerformed(ActionEvent arg0) {
            // TODO Auto-generated method stub

            if (White == null || Black == null) {
                JOptionPane.showMessageDialog(controlPanel, "Fill in the details");
                return;
            }
            White.updateGamesPlayed();
            White.Update_Player();
            Black.updateGamesPlayed();
            Black.Update_Player();
            WNewPlayer.disable();
            BNewPlayer.disable();
            wselect.disable();
            bselect.disable();
            split.remove(temp);
            split.add(board);
            showPlayer.remove(timeSlider);
            mov = new JLabel("Move:");
            mov.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
            mov.setForeground(Color.red);
            showPlayer.add(mov);
            CHNC = new JLabel(move);
            CHNC.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
            CHNC.setForeground(Color.blue);
            showPlayer.add(CHNC);
            displayTime.remove(start);
            displayTime.add(label);
            timer = new Time(label);
            timer.start();
            whiteKing.setMoveCount(0);
            blackKing.setMoveCount(0);
            blackRooks.get(0).setMoveCount(0);
            blackRooks.get(1).setMoveCount(0);
            whiteRooks.get(0).setMoveCount(0);
            whiteRooks.get(1).setMoveCount(0);
            BoardState startingState = new BoardState(boardState);
            stateHash.put(startingState.buildString(), 1);
        }
    }

    class TimeChange implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent arg0) {
            timeRemaining = timeSlider.getValue() * 60;
        }
    }

    class SelectHandler implements ActionListener {
        private int color;

        SelectHandler(int i) {
            color = i;
        }

        @Override
        public void actionPerformed(ActionEvent arg0) {
            // TODO Auto-generated method stub
            tempPlayer = null;
            String n = (color == 0) ? wname : bname;
            JComboBox<String> jc = (color == 0) ? wcombo : bcombo;
            JComboBox<String> ojc = (color == 0) ? bcombo : wcombo;
            ArrayList<Player> pl = (color == 0) ? wplayer : bplayer;
            // ArrayList<Player> otherPlayer=(color==0)?bplayer:wplayer;
            ArrayList<Player> opl = Player.fetch_players();
            if (opl.isEmpty())
                return;
            JPanel det = (color == 0) ? wdetails : bdetails;
            JPanel PL = (color == 0) ? WhitePlayer : BlackPlayer;
            if (selected == true)
                det.removeAll();
            n = (String) jc.getSelectedItem();
            Iterator<Player> it = pl.iterator();
            Iterator<Player> oit = opl.iterator();
            while (it.hasNext()) {
                Player p = it.next();
                if (p.name().equals(n)) {
                    tempPlayer = p;
                    break;
                }
            }
            while (oit.hasNext()) {
                Player p = oit.next();
                if (p.name().equals(n)) {
                    opl.remove(p);
                    break;
                }
            }

            if (tempPlayer == null)
                return;
            if (color == 0)
                White = tempPlayer;
            else
                Black = tempPlayer;
            bplayer = opl;
            ojc.removeAllItems();
            for (Player s : opl)
                ojc.addItem(s.name());
            det.add(new JLabel(" " + tempPlayer.name()));
            det.add(new JLabel(" " + tempPlayer.gamesplayed()));
            det.add(new JLabel(" " + tempPlayer.gameswon()));

            PL.revalidate();
            PL.repaint();
            PL.add(det);
            selected = true;
        }

    }

    class Handler implements ActionListener {
        private int color;

        Handler(int i) {
            color = i;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            String n = (color == 0) ? wname : bname;
            JPanel j = (color == 0) ? WhitePlayer : BlackPlayer;
            ArrayList<Player> N = Player.fetch_players();
            Iterator<Player> it = N.iterator();
            JPanel det = (color == 0) ? wdetails : bdetails;
            n = JOptionPane.showInputDialog(j, "Enter your name");

            if (n != null) {

                while (it.hasNext()) {
                    if (it.next().name().equals(n)) {
                        JOptionPane.showMessageDialog(j, "Player exists");
                        return;
                    }
                }

                if (n.length() != 0) {
                    Player tem = new Player(n);
                    tem.Update_Player();
                    if (color == 0)
                        White = tem;
                    else
                        Black = tem;
                } else
                    return;
            } else
                return;
            det.removeAll();
            det.add(new JLabel(" " + n));
            det.add(new JLabel(" 0"));
            det.add(new JLabel(" 0"));
            j.revalidate();
            j.repaint();
            j.add(det);
            selected = true;
        }
    }

    private static void clearPieces(){
        whiteBishops = new ArrayList<Bishop>();
        whiteKing = null;
        whiteKnights = new ArrayList<Knight>();
        whitePawns = new ArrayList<Pawn>();
        whiteQueens = new ArrayList<Queen>();
        whiteRooks = new ArrayList<Rook>();

        blackBishops = new ArrayList<Bishop>();
        blackKing = null;
        blackKnights = new ArrayList<Knight>();
        blackPawns = new ArrayList<Pawn>();
        blackQueens = new ArrayList<Queen>();
        blackRooks = new ArrayList<Rook>();
        
    }

    private static void initializePieces(){
        whiteRooks = Arrays.asList(new Rook("WR01", "White_Rook.png", 0, 7, 0), new Rook("WR02", "White_Rook.png", 0, 7, 7));
        blackRooks = Arrays.asList(new Rook("BR01", "Black_Rook.png", 1, 0, 0), new Rook("BR02", "Black_Rook.png", 1, 0, 7));
        whiteKnights = Arrays.asList(new Knight("WN01", "White_Knight.png", 0, 7, 1),
                new Knight("WN02", "White_Knight.png", 0, 7, 6));
        blackKnights = Arrays.asList(new Knight("BN01", "Black_Knight.png", 1, 0, 1),
                new Knight("BN02", "Black_Knight.png", 1, 0, 6));
        whiteBishops = Arrays.asList(new Bishop("WB01", "White_Bishop.png", 0, 7, 2),
                new Bishop("WB02", "White_Bishop.png", 0, 7, 5));
        blackBishops = Arrays.asList(new Bishop("BB01", "Black_Bishop.png", 1, 0, 2),
                new Bishop("BB02", "Black_Bishop.png", 1, 0, 5));
        whiteQueens = Arrays.asList(new Queen("WQ", "White_Queen.png", 0, 7, 3));
        blackQueens = Arrays.asList(new Queen("BQ", "Black_Queen.png", 1, 0, 3));
        whiteKing = new King("WK", "White_King.png", 0, 7, 4);
        blackKing = new King("BK", "Black_King.png", 1, 0, 4);
        whitePawns = new ArrayList<Pawn>();
        for (int i = 0; i < 8; i++) {
            whitePawns.add(new Pawn("WP0" + (i + 1), "White_Pawn.png", 0, 6, i));
        }
        blackPawns = new ArrayList<Pawn>();
        for (int i = 0; i < 8; i++) {
            blackPawns.add(new Pawn("BP0" + (i + 1), "Black_Pawn.png", 1, 1, i));
        }
    }

    private void resetTrivialMoveCounter(){
        trivialMoveCounter = 0;
    } 

    private void increaseTrivialMoveCounter(){
        trivialMoveCounter++;
    }
    
    private String generateNotation(int fromX, int fromY, int toX, int toY, Character promo) {
        StringBuilder sb = new StringBuilder();
        sb.append((char)('a' + fromY));
        sb.append(8 - fromX);
        
        sb.append((char)('a' + toY));
        sb.append(8 - toX);
        
        if (promo != null) {
            sb.append(Character.toLowerCase(promo.charValue()));
        }
        
        return sb.toString();
    }
    
    private Character getCharacterFromPiece(Piece piece) {
        if (piece instanceof Queen) return 'q';
        if (piece instanceof Rook) return 'r';
        if (piece instanceof Bishop) return 'b';
        if (piece instanceof Knight) return 'n';
        return null;
    }
}
