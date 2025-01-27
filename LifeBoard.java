// package <default>

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import javax.swing.*; /* provides "JPanel" class */
import java.awt.*;
//import java.awt.event.*;
import java.util.*; /* provides "Random( )" method */
import java.io.*; /* provides "File" class */


/**
 * Write a description of class LifeBoard here.
 * 
 * @author (Mike Roam) 
 * @version (2011 Oct 14, revised to use ints instead of booleans on the lifeboard)
 */
class LifeBoard extends JPanel implements MouseListener { 
/* Note: there's no such thing as JCanvas in Java 1.6 API 
   and advice online is to use JPanels and override their paintComponent( ) method,
   NOT overriding paint( ) like we used to do in awt.
   
   see <a href="http://www.oracle.com/technetwork/java/painting-140037.html">http://www.oracle.com/technetwork/java/painting-140037.html</a>
   */

    private static final long serialVersionUID = 42L;  // makes serializable happy
    int cellsAcross = 20;
    int cellsDown = 20;

    int cellWidth = 8;
    int cellHeight = 8;

    int topMargin = 0;
    int leftMargin = 0;

    int wholePictureWidth = 300;
    int wholePictureHeight = 300;
    Dimension minSize = new Dimension(wholePictureWidth, wholePictureHeight);
  
    final int /*boolean*/ ALIVE2 = 2/*true*/;
    final int /*boolean*/ ALIVE1 = 1/*true*/;
    final int /*boolean*/ DEAD = 0/*false*/;

    final Color alive2Color = Color.black;
    final Color alive1Color = Color.red;
    final Color deadColor = Color.white;
    
    /* two-dimensional array of ALIVE1 or ALIVE2 or DEAD*/ 
    int/*boolean*/[][] theData = null;
    /** holds data as we calculate new board */
    int/*boolean*/[][] newData = null;
	Point badPoint =  new Point( -1, -1);  /* used in badLoc */


    /**
    * Constructor for specified width and height
    * Creates a new board, loads it with random dots
    * ??( perhaps could get dots from a file!?)
    */
    LifeBoard( int newCellsAcross, int newCellsDown ) {
        super();
        setBackground( Color.GRAY );
        setOpaque(true);
        setBorder(BorderFactory.createLineBorder(Color.black));

        cellsAcross = newCellsAcross;
        cellsDown = newCellsDown;
        Random myNRG = new Random();
        theData = new int /*boolean*/ [ cellsAcross ] [ cellsDown ];
        newData = new int /*boolean*/ [ cellsAcross ] [ cellsDown ];
        for ( int x = 0; x < cellsAcross; ++x ) {
            for ( int y = 0; y < cellsDown; ++y ) {
                if ( (myNRG.nextInt() % 3) == 1 ) {
                    theData[x][y] = ALIVE1;
                } else if ( (myNRG.nextInt() % 3) == 2 ) {
                    theData[x][y] = ALIVE2;
                } else {
                    theData[x][y] = DEAD;
                } 
            } // for y
        } // for x
        addMouseListener(this);
        this.repaint( );
        //System.out.println( this.toString() );
    } // Lifeboard constructor



    /**
    * Constructor (for reading from a file?)
    */
    LifeBoard( Component theFrame ) {
        super();
        setBackground( Color.GRAY );
        getBoardFromFile( theFrame ); // Filename is found by theFrame somehow
        this.repaint( );
        // System.out.println( this.toString() );
    } // Lifeboard constructor

   

    public void mousePressed(MouseEvent e) {
       // e.getClickCount(), e);
    }

    public void mouseReleased(MouseEvent e) {
       // e.getClickCount(), e);
    }

    public void mouseEntered(MouseEvent e) {
       // saySomething("Mouse entered", e);
    }

    public void mouseExited(MouseEvent e) {
       // saySomething("Mouse exited", e);
    }

    public void mouseClicked(MouseEvent e) {
       // e.getClickCount() + ")", e);
       Point whereClicked = e.getPoint( );
       System.out.println("Click at (local):" + whereClicked);
    } /* mouseClicked( ) */


	/**
	* Given a Point representing screenloc in pixels (over,down),
	* return a Point representing the [x],[y] loc of the cell in the grid.
	* What do we return if input gives us bad coords??
	*/
	Point whichCell( Point clickLoc ) {
		/* Here's the equation for drawing cells:
		myg.fillOval( /* left edge.. leftMargin + (x * cellWidth),
                              /* top edge..  topMargin + (y * cellHeight), 
                                cellWidth, cellHeight ); */
		if ( badLoc( clickLoc) ) {
			return badPoint;
		}
		
	} /* whichCell( ) */


	/**
	* 
	*/
	boolean badLoc( Point clickLoc ) {
		if ( (clickLoc.x < 0 ) || (clickLoc.x >  wholePictureWidth)) { 
			return false;
		}
		if ( (clickLoc.y < 0 ) || (clickLoc.y >  wholePictureHeight)) { 
			return false;
		}
	} /* badLoc( ) */


    /**
    * automatically called whenever the screen needs refresh
    */
    public void paintComponent( Graphics myg ) {
        // System.out.println("LifeBoard.paintComponent( )");
        // quickDraw( myg ); // doesn't seem to be hiding dead cells
        
        fullDraw( myg ); // only when flagged??
    } // paint( )


    public Dimension getMinimumSize() {
        return minSize;
    }

    
    public Dimension getPreferredSize() {
        return minSize;
    }


    /**
    * redraws every cell, unlike "quickDraw" which merely draws changed cells
    */
    public void fullDraw( Graphics myg ) {
        for ( int x = 0; x < cellsAcross; ++x ) {
            for ( int y = 0; y < cellsDown; ++y ) {
                if ( theData[x][y] == ALIVE2 ) {
                    myg.setColor( alive2Color );
                } else if ( theData[x][y] == ALIVE1 ) {
                    myg.setColor( alive1Color );
                } else{
                    myg.setColor( deadColor );
                };
                myg.fillOval( /* left edge */ leftMargin + (x * cellWidth),
                              /* top edge */  topMargin + (y * cellHeight), 
                                cellWidth, cellHeight );
            } // for y
        } // for x
    } // fullDraw( )


    /** 
    * I could call this if I knew how to get the current myg?
    * Or just let paint call this?
    */
    void quickDraw( Graphics myg ) {
        // This draws only the cells that have changed since last time...;
        for ( int x = 0; x < cellsAcross; ++x ) {
            for ( int y = 0; y < cellsDown; ++y ) {
                if ( theData[x][y] != newData[x][y] ) {
                    if ( theData[x][y] == ALIVE2 ) {
                        myg.setColor( alive2Color );
                    } else if ( theData[x][y] == ALIVE1 ) {
                        myg.setColor( alive1Color );
                    } else {
                        myg.setColor( deadColor );
                    };
                    myg.fillOval( /* left: */ leftMargin + (x * cellWidth),
                                /* top: */ topMargin + (y * cellHeight), 
                                /* width: */    cellWidth,
                                /* height: */   cellHeight );
                } // if new state
            } // for y
        } // for x
    } // quickDraw( )



    /**
    * Creates a new generation.
    * Currently hard-wired to use Conway rules.
    * plus living cell with 3 neighbors turns alive2
    */
    void step() {
        for ( int x = 0; x < cellsAcross; ++x ) {
            for ( int y = 0; y < cellsDown; ++y ) {
                int numOfNeighbors = neighborCount( x, y );
                if ( theData[x][y] == ALIVE1 ) {
                    if (numOfNeighbors == 2) /* || numOfNeighbors == 3)) */ {
                        newData[x][y] = ALIVE2;
                    } else if (numOfNeighbors == 3) {
                        newData[x][y] = ALIVE1;
                    } else {
                        newData[x][y] = DEAD;
                    }
                } else if ( theData[x][y] == ALIVE2 ) {
                    if (numOfNeighbors == 2)/* || (numOfNeighbors == 3) )*/ {
                        newData[x][y] = ALIVE1;
                    } else if (numOfNeighbors == 3) {
                        newData[x][y] = ALIVE2;
                    } else {
                        newData[x][y] = DEAD;
                    }
                } else {
                    if ( (numOfNeighbors == 3) ) {
                        newData[x][y] = ALIVE1;
                    } else {
                        newData[x][y] = DEAD;
                    }
                }
            } // for y
        } // for x 
        // now swap the pointers to the boards;;
        int /*boolean*/[][] tempPtr = theData;
        theData = newData;
        newData = tempPtr;
    } // step( )




    /* This does toroidal wrap? Perhaps be optional with boolean flag "torus" */
    int neighborCount( int x, int y ) {
        int totalNeighbors = 0;
        /* count all the surroundings AND ourself, since it's easier to set up the loops that way */;
        
        for ( int xi = - 1; xi < 2; ++xi ) {
            for ( int yi = - 1; yi < 2; ++yi ) {
                // if (torus) {
                /* use "MOD" to control wrap around */;
                /* have to add cellsAcross to force wrap around 
                since -1 % 6 = -1 !!!!! */;
                int xiloc = (x + cellsAcross + xi) % cellsAcross;
                int yiloc = (y + cellsDown + yi) % cellsDown;
                if (( theData[xiloc][yiloc] == ALIVE1 ) ||  ( theData[xiloc][yiloc] == ALIVE2 )) {
                    ++totalNeighbors;
                }
            }
        }
        // don't count yourself!;
        if (( theData[x][y] == ALIVE1) || ( theData[x][y] == ALIVE2 )) {
            --totalNeighbors;
        };
        return totalNeighbors;
    } // neighborCount( )



    /** 
    * This should use some java kind of split or parse to get the ints!
    */
    void getBoardFromFile( Component theFrame ) {
            /* get board from a file! 
        File Format: life 8 8 \n":...AAA..\n:...AAA..."
        info: starts with word "life" and space and width and space and 
        height AND SPACE
        then the data, line by line, each line starting with colon,
        with periods for dead cells and 'A's for live ones
        AND '2' for alive2
            */;
        File myF = ComponentUtil.getSomeOldFile( theFrame );
        String myString = ComponentUtil.readStringFromFile( myF );
        /* file should start with word "life\n"...*/;
        try {
            /* going to try to read "life <int> <int>" from start of file */;
            int whereFirstBlankIs = myString.indexOf( ' ' );
            String firstWord = myString.substring( 0, whereFirstBlankIs );
            if ( ! "life".equals( firstWord ) ) {
                System.out.println( "first chars in life file should be 'life' but is '" + myString.substring(0, 4) + "'" );
                throw new FileNotFoundException();
            };
            int whereSecondBlankIs = myString.indexOf( ' ', whereFirstBlankIs + 1 );
            String secondWord = myString.substring( whereFirstBlankIs + 1, whereSecondBlankIs );
            int newCellsAcross = Integer.parseInt( secondWord );
            int whereThirdBlankIs = myString.indexOf( ' ', whereSecondBlankIs + 1 );
            String thirdWord = myString.substring( whereSecondBlankIs + 1, whereThirdBlankIs );
            int newCellsDown = Integer.parseInt( thirdWord );
            cellsAcross = newCellsAcross;
            cellsDown = newCellsDown;
            wholePictureWidth = leftMargin + (cellsAcross * cellWidth) + leftMargin;
            wholePictureHeight = topMargin + (cellsDown * cellHeight) + topMargin;
            theData = new int /*boolean*/ [ cellsAcross ] [ cellsDown ];
            newData = new int /*boolean*/ [ cellsAcross ] [ cellsDown ];
            int whereDataIs = whereThirdBlankIs + 1;
            for ( int y = 0; y < cellsDown; ++y ) {
                /* each line starts with : */;
                whereDataIs = 1 + myString.indexOf( ':', whereDataIs );
                for ( int x = 0; x < cellsAcross; ++x ) {
                    if ( (myString.charAt( whereDataIs )) == '.' ) {
                        theData[x][y] = DEAD;
                    } else if ( (myString.charAt( whereDataIs )) == 'A' ) {
                        theData[x][y] = ALIVE1;
                    } else {
                        theData[x][y] = ALIVE2;
                    }
                    ++whereDataIs;
                }
            }
        } catch( Exception e ) {
            System.out.println( "Error in getBoardFromFile: " + e.toString() );
            //this.LifeBoard( 16, 16 ) ?? have to fill in a dud board, perhaps random?;
        }
    } // getBoardFromFile

    
    /**
     * return a text image of the <em>current</em> board
     */
    public String toString() {
        StringBuffer mySB = new StringBuffer( (cellsAcross + 1) * cellsDown );
        for ( int y = 0; y < cellsDown; ++y ) {
            for ( int x = 0; x < cellsAcross; ++x ) {
                if ( theData[x][y] == ALIVE1 ) {
                    mySB.append( 'A' );
                } else if ( theData[x][y] == ALIVE2 ) {
                    mySB.append( '2' );
                } else {
                    mySB.append( '.' );
                };
            };
            mySB.append( '\n' );
        };
        String tempStr = new String( mySB );
        return tempStr;
    } // toString( )


} // Class Lifeboard