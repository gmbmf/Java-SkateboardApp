import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;  // Needed for formatting the totals
import javax.swing.*;

/**
 *  The SkateboardApp class creates the GUI for The Skate Shop application.
 */

public class SkateboardApp extends JFrame {
    private final double    SALES_TAX_RATE  = 0.06; // Sales tax rate
    private final int       WINDOW_WIDTH    = 420;
    private final int       WINDOW_HEIGHT   = 250;

    private TitlePanel      titlePanel;     // To display a title
    private DeckPanel       deckPanel;      // Deck panel
    private TrucksPanel     trucksPanel;    // Trucks panel
    private WheelsPanel     wheelsPanel;    // Wheels panel
    private AccessoryPanel  accessoryPanel; // Accessory panel
    private JPanel          buttonPanel;    // To hold the buttons
    private JButton         purchaseButton; // To calculate the cost
    private JButton         exitButton;     // To exit the application

    protected JFrame        thisFrame;
    int                     windLeft;
    int                     windTop;

    /**
        Constructor
    */

    public SkateboardApp() {
        thisFrame = this;

        // Display a title.
        setTitle("Order Taker");

        // Set the size of the window.
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

        // Position the window.
        positionWindow();

        // Specify an action for the close button.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a FlowLayout manager.
        setLayout(new FlowLayout(FlowLayout.CENTER));

        // Create the custom panels.
        titlePanel = new TitlePanel();
        deckPanel = new DeckPanel();
        trucksPanel = new TrucksPanel();
        wheelsPanel = new WheelsPanel();
        accessoryPanel = new AccessoryPanel();

        // Create the button panel.
        buildButtonPanel();

        add(titlePanel);
        add(deckPanel);
        add(trucksPanel);
        add(wheelsPanel);
        add(accessoryPanel);
        add(buttonPanel);

        new SplashDialog(this);

        // Pack the contents of the window and display it.
        pack();
        setVisible(true);
    }

    /**
     *  positionWindow method:
     *  This method determines the size of the screen and then positions the
     *  main window in the center of the screen.
     */
    private void positionWindow() {
        Dimension screenSize =
               new Dimension(Toolkit.getDefaultToolkit().getScreenSize());

        windLeft    = (screenSize.width - WINDOW_WIDTH) / 2;
        windTop     = (screenSize.height - WINDOW_HEIGHT) / 2;
        setLocation(windLeft, windTop);
    }

    /**
        The buildButtonPanel method builds the button panel.
    */

    private void buildButtonPanel() {
        // Create a panel for the buttons.
        buttonPanel = new JPanel();

        // Create the buttons.
        purchaseButton = new JButton("Purchase");
        purchaseButton.setForeground(Color.GREEN.darker());
        exitButton = new JButton("Exit");
        exitButton.setForeground(Color.RED);

        // Register the action listeners.
        purchaseButton.addActionListener(new PurchaseButtonListener());
        exitButton.addActionListener(new ExitButtonListener());

        // Add the buttons to the button panel.
        buttonPanel.add(purchaseButton);
        buttonPanel.add(exitButton);
    }

    /**
        Private inner class that handles the event when
        the user clicks the Purchase button.
    */

    private class PurchaseButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Variables to hold the subtotal, sales tax, and total
            double  salesTax;
            double  subtotal;
            double  total;

            // Calculate the subtotal.
            subtotal = deckPanel.getDeckCost() + 
                       trucksPanel.getTrucksCost() +
                       wheelsPanel.getWheelsCost() +
                       accessoryPanel.getAccessoryCost();

            if (subtotal == 0) {
                // Tell the user to select something.
                JOptionPane.showMessageDialog(null, "No items have been " +
                                              "selected.");
            }
            else {
                // Calculate the sales tax.
                salesTax = subtotal * SALES_TAX_RATE;

                // Calculate the total.
                total = subtotal + salesTax;

                // Display the costs.
                new CostDialog(thisFrame, windLeft, windTop, subtotal,
                               salesTax, total);

                // Clear all of the selections back to None.
                deckPanel.clearSelection();
                trucksPanel.clearSelection();
                wheelsPanel.clearSelection();
                accessoryPanel.clearSelection();
            }
        }
    }

    /**
     *  ExitButtonListener class:
     *  Private inner class that handles the event when the user clicks
     *  the Exit button.
     */

    private class ExitButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
             System.exit(0);
        }
    }

    /**
     *  SplashDialog class:
     *  Display a splash screen for the app.
     */

    private class SplashDialog extends JDialog {
        private final int       WINDOW_WIDTH        = 400;
        private final int       WINDOW_HEIGHT       = 200;

        // Description used to create splash labels.
        private final int       LABEL_ALIGNMENT     = JLabel.CENTER;
        private final Color     LABEL_COLOR         = Color.GREEN.darker();
        private final String    LABEL_FONT_NAME     = "Serif";
        private final int       LABEL_FONT_SIZE     = 48;
        private final int       LABEL_FONT_WEIGHT   = Font.BOLD | Font.ITALIC;

        // Panel elements.
        private String[]        SPLASH_STRS         = {
            "Welcome to", "The Skate Shop"
        };
        private JButton         okButton;

        // Panels to create for the dialog.
        JPanel[]                splashPanels;

        /**
         *  SplashDialog constructor:
         */

        public SplashDialog(JFrame parentFrame) {
            // Call the JDialog constructor to create the dialog.  The dialog
            // will be set up so that it can be displayed for a while and
            // then closed programmatically.
            super(parentFrame, "Welcome!", false);

            // Set the dimensions of the dialog.
            setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

            // Position the window.
            positionWindow();

            // Use the flow layout.
            setLayout(new FlowLayout());

            // Create the panels.
            splashPanels = new JPanel[SPLASH_STRS.length];
            for (int index = 0; index < SPLASH_STRS.length; index++) {
                String  text    = SPLASH_STRS[index];

                splashPanels[index] = new SplashPanel(text);
            }

            // Add the panels to the content pane.
            for (JPanel panel : splashPanels)
                getContentPane().add(panel);

            // Make the dialog visible.
            setVisible(true);

            // Splash the dialog for a while
            try {
                Thread.sleep(2500);
            }
            catch (InterruptedException ex) {
            }

            // Make the dialog invisible and close it.
            setVisible(false);
            dispose();
        }

        /**
         *  positionWindow method:
         *  This method determines the size of the screen and then positions
         *  this dialog in the center of the screen.
         */
        private void positionWindow() {
            Dimension screenSize =
                   new Dimension(Toolkit.getDefaultToolkit().getScreenSize());

            // Determine the northwest corner of the dialog and set it.
            int windLeft    = (screenSize.width - WINDOW_WIDTH) / 2;
            int windTop     = (screenSize.height - WINDOW_HEIGHT) / 2;
            setLocation(windLeft, windTop);
        }

        /**
         *  SplashPanel class:
         *  This class creates a simple panel that contains a label which is
         *  centered, colored medium green, and has a large fancy font.
         */

        private class SplashPanel extends JPanel {
            public SplashPanel(String splashText) {
                JLabel  splashLabel;

                // Use the flow layout.
                setLayout(new FlowLayout());

                // Create the label and make it fancy.
                splashLabel = new JLabel(splashText, LABEL_ALIGNMENT);
                splashLabel.setForeground(LABEL_COLOR);
                splashLabel.setFont(
                        new Font(LABEL_FONT_NAME, LABEL_FONT_WEIGHT,
                                 LABEL_FONT_SIZE));

                // Add the label to the panel.
                add(splashLabel);
            }
        }
    }

    /**
     *  CostDialog class:
     *  Display a modal dialog with the total cost including the breakdown.
     */

    private class CostDialog extends JDialog {
        // Window dimensions and location offsets.
        private final int   WINDOW_WIDTH    = 170;
        private final int   WINDOW_HEIGHT   = 150;
        private final int   WIDTH_OFFSET    = 130;
        private final int   HEIGHT_OFFSET   = 80;

        // Cost panels.
        JPanel                  buttonPanel;
        private JPanel          costTitlePanel;
        private JPanel          textsPanel;
        private JPanel          valuesPanel;

        // Panel elements.
        private String          salesTaxStr;
        private String          subtotalStr;
        private String          totalStr;
        private JButton         okButton;

        // Used to format the total amounts.
        private DecimalFormat   dollar;

        /**
         *  CostDialog constructor:
         */

        public CostDialog(JFrame parentFrame, int windLeft, int windTop,
                          double subtotal, double salesTax, double total) {
            super(parentFrame, "Total Purchase", true);

            // Set the dimensions of the dialog.
            setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

            // Position the dialog southeast of the northwest corner of the
            // main window.
            setLocation(windLeft + WIDTH_OFFSET, windTop + HEIGHT_OFFSET);

            // Use the border layout manager.
            setLayout(new BorderLayout());

            // Format the money variables as strings to use as labels.
            dollar = new DecimalFormat("0.00");
            subtotalStr = "$" + dollar.format(subtotal);
            salesTaxStr = "$" + dollar.format(salesTax);
            totalStr = "$" + dollar.format(total);

            costTitlePanel = new CostTitlePanel("Your Total Purchase");

            String[] texts = { "Subtotal", "Sales Tax", "Total" };
            textsPanel = new CostPanel(texts);

            String[] values = { subtotalStr, salesTaxStr, totalStr };
            valuesPanel = new CostPanel(values);

            buttonPanel = new ButtonPanel();

            getContentPane().add(costTitlePanel, BorderLayout.NORTH);
            getContentPane().add(textsPanel, BorderLayout.WEST);
            getContentPane().add(valuesPanel, BorderLayout.EAST);
            getContentPane().add(buttonPanel, BorderLayout.SOUTH);

            setVisible(true);
        }

        private class CostTitlePanel extends JPanel {
            public CostTitlePanel(String title) {
                add(new JLabel(title, JLabel.CENTER));
            }
        }

        private class CostPanel extends JPanel {
            public CostPanel(String[] texts) {
                setLayout(new GridLayout(3, 1));

                for (String text : texts) {
                    add(new JLabel(text, JLabel.RIGHT));
                }
            }
        }

        private class ButtonPanel extends JPanel {
            public ButtonPanel() {
                setLayout(new FlowLayout());

                okButton = new JButton("OK");
                okButton.addActionListener(new ButtonListener());
                add(okButton);
            }
        }

        private class ButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                // Make this dialog invisible and close it.
                setVisible(false);
                dispose();
            }
        }
    }

    /**
     *  TitlePanel class:
     *  This class is a specialized JPanel class that displays a title at the
     *  top of the application window.
     */

    private class TitlePanel extends JPanel {
        // The text to display at the top of the content pane.
        private final String    LABEL_TEXT      = "Welcome to The Skate Shop";
        // Label characteristics
        private final int       LABEL_ALIGNMENT = JLabel.CENTER;
        private final Color     LABEL_COLOR     = Color.BLUE;
        private final int       LABEL_HEIGHT    = 20;
        private final int       LABEL_WIDTH     = 500;

        /**
         *  TitlePanel constructor:
         */

        public TitlePanel() {
            // Create the title label and center it.
            JLabel titleLabel = new JLabel(LABEL_TEXT, LABEL_ALIGNMENT);

            // The title is window-sized to force the flow layout to keep it
            // on top of the panels for the choices.
            titleLabel.setPreferredSize(
                    new Dimension(LABEL_WIDTH, LABEL_HEIGHT));

            // Color the title blue.
            titleLabel.setForeground(LABEL_COLOR);

            // Add the label to this panel.
            add(titleLabel);
        }
    }

    /**
     *  DeckPanel class:
     *  This class allows the user to select one of three decks.
     */

    private class DeckPanel extends JPanel {
        // The title of the panel.
        private final String    PANEL_TITLE             = "Decks";
        // These are the names and prices of each deck.
        private final String[]  PART_NAMES              = {
            "None", "Master Thrasher", "Dictator", "Street King"
        };
        private final double[]  PART_PRICES             = {
            0.00, 60.00, 45.00, 50.00
        };

        private Debug           debug;

        // The deck list to be added to the panel.
        private JList<String>   deckList;

        /**
         *  DeckPanel constructor:
         */

        public DeckPanel() {
            // FIXME: DEBUG
            debug = new Debug();

            setPreferredSize(new Dimension(120, 120));

            // KEEP THIS FOR REFERENCE!
            // JList issue - use the latter vs. the former:
            //JList deckList = new JList(PART_NAMES);
            //JList<String> deckList = new JList<String>(PART_NAMES);

            deckList = new JList<String>(PART_NAMES);

            deckList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            deckList.setSelectedIndex(0);

            // Add a 'Decks' border around the panel.
            setBorder(BorderFactory.createTitledBorder(PANEL_TITLE));

            // Add the deck list to the panel.
            add(deckList);
        }

        public void clearSelection() {
            deckList.setSelectedIndex(0);
        }

        /**
         *  getDeckCost method:
         *  This method returns the cost of the selected deck.
         */

        public double getDeckCost() {
            // The following variable will hold the cost
            // of the selected deck.
            double  deckCost        = 0.0;

            // Protect against crashing due to the user clearing all
            // selections.
            if (deckList.isSelectionEmpty()) {
                //System.out.println("DEBUG: deck: NONE");
                debug.println("deck: NONE");
                deckList.setSelectedIndex(0);
            }

            // Determine which deck was selected.
            Object  selection       = deckList.getSelectedValue();
            String  selectionStr    = selection.toString();

            //System.out.println("DEBUG: deck: " + selection.toString());
            debug.println("deck: " + selection.toString());

            // Determine the price of this deck.
            for (int index = 0; index < PART_NAMES.length; index++) {
                if (PART_NAMES[index] == selectionStr) {
                    deckCost = PART_PRICES[index];
                    //System.out.println("DEBUG: price: $" + price);
                    debug.println("price: $" + deckCost);
                    break;
                }
            }

            // Return the cost of the selected deck.
            return deckCost;
        }
    }

    /**
     *  TrucksPanel class:
     *  This class allows the user to select one of three types of truck
     *  assemblies.
     */

    private class TrucksPanel extends JPanel {
        // The title of the panel.
        private final String    PANEL_TITLE             = "Trucks";
        // These are the names and prices of each trucks assembly.
        private final String[]  PART_NAMES              = {
            "None", "7.75\" axle", "8.00\" axle", "8.50\" axle"
        };
        private final double[]  PART_PRICES             = {
            0.00, 35.00, 40.00, 45.00
        };

        // FIXME: DEBUG
        private Debug           debug;

        // The trucks list to be added to the panel.
        private JList<String>   trucksList;
        
        /**
         *  Constructor
         */

        public TrucksPanel() {
            debug = new Debug();

            setPreferredSize(new Dimension(80, 120));

            trucksList = new JList<String>(PART_NAMES);

            trucksList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            trucksList.setSelectedIndex(0);

            // Add a 'Trucks' border around the panel.
            setBorder(BorderFactory.createTitledBorder("Trucks"));

            // Add the trucks list to the panel.
            add(trucksList);
        }

        public void clearSelection() {
            trucksList.setSelectedIndex(0);
        }

        /**
         *  getTrucksCost method:
         *  This method returns the cost of the selected trucks assembly.
         */

        public double getTrucksCost() {
            // The following variable will hold the cost
            // of the selected trucks.
            double  trucksCost      = 0.0;

            // Protect against crashing due to the user clearing all
            // selections.
            if (trucksList.isSelectionEmpty()) {
                debug.println("trucks: NONE");
                trucksList.setSelectedIndex(0);
            }

            // Determine which trucks assembly was selected.
            Object  selection       = trucksList.getSelectedValue();
            String  selectionStr    = selection.toString();

            debug.println("trucks: " + selection.toString());

            // Determine the price of this deck.
            for (int index = 0; index < PART_NAMES.length; index++) {
                if (PART_NAMES[index] == selectionStr) {
                    trucksCost = PART_PRICES[index];
                    debug.println("price: $" + trucksCost);
                    break;
                }
            }

            // Return the cost of the selected trucks assembly.
            return trucksCost;
        }
    }

    /**
     *  WheelsPanel class:
     *  This class allows the user to select one of four types of wheel sets.
     */

    private class WheelsPanel extends JPanel {
        // These are the names and prices of each set of wheels.
        private final String[]  PART_NAMES  = {
            "None", "51 mm", "55 mm", "58 mm", "61 mm"
        };
        private final double[]  PART_PRICES = {
            0.00, 20.00, 22.00, 24.00, 28.00
        };

        // FIXME: DEBUG
        private Debug           debug;

        // The wheels list to be added to the panel.
        private JList<String>   wheelsList;
        
        /**
         *  Constructor
         */

        public WheelsPanel() {
            debug = new Debug();

            setPreferredSize(new Dimension(60, 120));

            wheelsList = new JList<String>(PART_NAMES);

            wheelsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            wheelsList.setSelectedIndex(0);

            // Add a border around the panel.
            setBorder(BorderFactory.createTitledBorder("Wheels"));

            // Add the wheels list to the panel.
            add(wheelsList);
        }

        public void clearSelection() {
            wheelsList.setSelectedIndex(0);
        }

        /**
         *  getWheelsCost method:
         *  This method returns the cost of the selected set of wheels.
         */

        public double getWheelsCost() {
            // The following variable will hold the cost of the selected set
            // of wheels.
            double  wheelsCost      = 0.0;

            // Protect against crashing due to the user clearing all
            // selections.
            if (wheelsList.isSelectionEmpty()) {
                debug.println("wheels: NONE");
                wheelsList.setSelectedIndex(0);
            }

            // Determine which set of wheels was selected.
            Object  selection       = wheelsList.getSelectedValue();
            String  selectionStr    = selection.toString();

            debug.println("wheels: " + selection.toString());

            // Determine the price of this wheels set.
            for (int index = 0; index < PART_NAMES.length; index++) {
                if (PART_NAMES[index] == selectionStr) {
                    wheelsCost = PART_PRICES[index];
                    debug.println("price: $" + wheelsCost);
                    break;
                }
            }

            // Return the cost of the selected set of wheels.
            return wheelsCost;
        }
    }

    /**
     *  AccessoryPanel class:
     *  This class allows the user to select accessories.
     */

    public class AccessoryPanel extends JPanel {
        // The title of the panel.
        private final String    PANEL_TITLE = "Accessories";
        // These are the names and prices of each accessory.
        private final String[]  PART_NAMES  = {
            "Grip tape", "Bearings", "Riser pads", "Nuts & bolts kit"
        };
        private final double[]  PART_PRICES = {
            10.00, 30.00, 2.00, 3.00
        };

        // FIXME: DEBUG
        Debug                   debug;

        // The accessory list to be added to the panel.
        private JList<String>   accessoryList;

        /**
         * Constructor
         */

        public AccessoryPanel() {
            // FIXME: temp
            debug = new Debug();

            setPreferredSize(new Dimension(110, 120));  // FIXME

            accessoryList = new JList<String>(PART_NAMES);

            accessoryList.setSelectionMode(
                    ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            // Add an 'Accessories' border around the panel.
            setBorder(BorderFactory.createTitledBorder(PANEL_TITLE));

            // Add the accessory list to the panel.
            add(accessoryList);
        }

        public void clearSelection() {
            // Clear all selected items.
            accessoryList.clearSelection();
        }

        /**
         *  getAccessoryCost method:
         * This method returns the cost of the selected accessories.
         */

        public double getAccessoryCost() {
            // The following variable will hold the cost of the selected
            // accessories.
            double accessoryCost = 0.0;

            if (accessoryList.isSelectionEmpty())
                debug.println("accessory: NONE");

            // Determine which accessories were selected, if any.
            java.util.List selections = accessoryList.getSelectedValuesList();

            // Process all of the accessories selected, if any.
            for (Object selection : selections) {
                double  price           = 0.00;
                String  selectionStr    = selection.toString();

                debug.println("accessory: " + selectionStr);

                // Determine the price of this accessory.
                for (int index = 0; index < PART_NAMES.length; index++) {
                    if (PART_NAMES[index] == selectionStr) {
                        price = PART_PRICES[index];
                        debug.println("price: $" + price);
                        break;
                    }
                }

                // Add the price to the accessory cost total.
                accessoryCost += price;
            }

            // Return the accessory cost.
            return accessoryCost;
        }
    }

    /**
     *  main method:
     *  This creates a SkateboardApp object which runs the application.
     */
    
    public static void main(String[] args) {
        new SkateboardApp();
    }
}
