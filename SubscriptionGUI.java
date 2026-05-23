import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;


/**
 * SubscriptionGUI class - Main GUI class for the AI Subscription Management System.
 * Stores an ArrayList of AIModel plans and provides a graphical interface
 * to add, display, manage, and export subscription plans.
 *
 * @author Suvam Gurung
 * @studentID 25029199
 */
public class SubscriptionGUI extends JFrame implements ActionListener {

    // =====================================================================
    //  DATA STORE - single ArrayList of type AIModel (required by spec)
    // =====================================================================
    private ArrayList<AIModel> plans = new ArrayList<>();

    // =====================================================================
    //  TEXT FIELDS  (10 required by spec + 1 for purchase prompts)
    // =====================================================================
    private JTextField tfModelName      = new JTextField(14);
    private JTextField tfPrice          = new JTextField(10);
    private JTextField tfParams         = new JTextField(10);
    private JTextField tfWindowSize     = new JTextField(8);   // parsed as int
    private JTextField tfPromptQuota    = new JTextField(8);
    private JTextField tfTeamSlots      = new JTextField(8);
    private JTextField tfPromptText     = new JTextField(20);
    private JTextField tfResponseLength = new JTextField(8);
    private JTextField tfTeamMemberName = new JTextField(12);
    private JTextField tfIndex          = new JTextField(6);
    private JTextField tfPurchasePrompts = new JTextField(8);  // for purchasing additional prompts

    // =====================================================================
    //  BUTTONS
    // =====================================================================
    private JButton btnAddPersonal     = new JButton("Add Personal Plan");
    private JButton btnAddPro          = new JButton("Add Pro Plan");
    private JButton btnDisplayAll      = new JButton("Display All");
    private JButton btnClear           = new JButton("Clear Fields");
    private JButton btnGivePrompt      = new JButton("Give a Prompt");
    private JButton btnPurchasePrompts = new JButton("Purchase Prompts");   // NEW (Changes slide)
    private JButton btnAddMember       = new JButton("Add Team Member");
    private JButton btnRemoveMember    = new JButton("Remove Team Member");
    private JButton btnCheckPlan       = new JButton("Check Plan Type");
    private JButton btnExport          = new JButton("Export to File");
    private JButton btnLoad            = new JButton("Load From File");

    // =====================================================================
    //  DISPLAY AREA
    // =====================================================================
    private JTextArea displayArea = new JTextArea(14, 50);

    // =====================================================================
    //  CONSTRUCTOR
    // =====================================================================
    public SubscriptionGUI() {
        setTitle("AI Subscription Management System - 25029199");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        add(buildInputPanel(),  BorderLayout.NORTH);
        add(buildDisplayPanel(), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);

        registerListeners();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // =====================================================================
    //  PANEL BUILDERS
    // =====================================================================
    private JPanel buildInputPanel() {
        JPanel wrapper = new JPanel(new GridLayout(1, 3, 8, 0));
        wrapper.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));

        // ---- Model Info ----
        JPanel modelPanel = new JPanel(new GridLayout(4, 2, 4, 4));
        modelPanel.setBorder(BorderFactory.createTitledBorder("Model Info"));
        modelPanel.add(new JLabel("Model Name:"));
        modelPanel.add(tfModelName);
        modelPanel.add(new JLabel("Price (Rs/1L tokens):"));
        modelPanel.add(tfPrice);
        modelPanel.add(new JLabel("Parameters (Billion):"));
        modelPanel.add(tfParams);
        modelPanel.add(new JLabel("Context Window (K tokens):"));
        modelPanel.add(tfWindowSize);

        // ---- Plan Options ----
        JPanel planPanel = new JPanel(new GridLayout(5, 2, 4, 4));
        planPanel.setBorder(BorderFactory.createTitledBorder("Plan Options"));
        planPanel.add(new JLabel("Prompt Quota (Personal):"));
        planPanel.add(tfPromptQuota);
        planPanel.add(new JLabel("Team Slots (Pro):"));
        planPanel.add(tfTeamSlots);
        planPanel.add(new JLabel("Purchase Prompts (qty):"));
        planPanel.add(tfPurchasePrompts);
        planPanel.add(new JLabel("Index No:"));
        planPanel.add(tfIndex);
        planPanel.add(new JLabel("Team Member Name:"));
        planPanel.add(tfTeamMemberName);

        // ---- Prompt / API Call ----
        JPanel promptPanel = new JPanel(new GridLayout(4, 2, 4, 4));
        promptPanel.setBorder(BorderFactory.createTitledBorder("Prompt / API Call"));
        promptPanel.add(new JLabel("Prompt Text:"));
        promptPanel.add(tfPromptText);
        promptPanel.add(new JLabel("Response Length (tokens):"));
        promptPanel.add(tfResponseLength);
        promptPanel.add(new JLabel(""));
        promptPanel.add(new JLabel(""));
        promptPanel.add(new JLabel(""));
        promptPanel.add(new JLabel(""));

        wrapper.add(modelPanel);
        wrapper.add(planPanel);
        wrapper.add(promptPanel);
        return wrapper;
    }

    private JScrollPane buildDisplayPanel() {
        displayArea.setEditable(false);
        displayArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        displayArea.setText("Welcome to AI Subscription Management System\n"
                + "---------------------------------------------\n"
                + "Use the buttons below to manage subscription plans.\n");

        JScrollPane scroll = new JScrollPane(displayArea);
        scroll.setBorder(BorderFactory.createTitledBorder("Output"));
        scroll.setPreferredSize(new Dimension(800, 260));
        return scroll;
    }

    private JPanel buildButtonPanel() {
        JPanel wrapper = new JPanel(new GridLayout(1, 4, 8, 0));
        wrapper.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));

        JPanel grp1 = new JPanel(new GridLayout(2, 1, 4, 4));
        grp1.setBorder(BorderFactory.createTitledBorder("Add Plans"));
        grp1.add(btnAddPersonal);
        grp1.add(btnAddPro);

        JPanel grp2 = new JPanel(new GridLayout(2, 1, 4, 4));
        grp2.setBorder(BorderFactory.createTitledBorder("View"));
        grp2.add(btnDisplayAll);
        grp2.add(btnClear);

        JPanel grp3 = new JPanel(new GridLayout(4, 1, 4, 4));
        grp3.setBorder(BorderFactory.createTitledBorder("Operations"));
        grp3.add(btnGivePrompt);
        grp3.add(btnPurchasePrompts);   // NEW button added here
        grp3.add(btnAddMember);
        grp3.add(btnRemoveMember);

        JPanel grp4 = new JPanel(new GridLayout(3, 1, 4, 4));
        grp4.setBorder(BorderFactory.createTitledBorder("Utilities"));
        grp4.add(btnCheckPlan);
        grp4.add(btnExport);
        grp4.add(btnLoad);

        wrapper.add(grp1);
        wrapper.add(grp2);
        wrapper.add(grp3);
        wrapper.add(grp4);
        return wrapper;
    }

    private void registerListeners() {
        btnAddPersonal.addActionListener(this);
        btnAddPro.addActionListener(this);
        btnDisplayAll.addActionListener(this);
        btnClear.addActionListener(this);
        btnGivePrompt.addActionListener(this);
        btnPurchasePrompts.addActionListener(this);   // NEW
        btnAddMember.addActionListener(this);
        btnRemoveMember.addActionListener(this);
        btnCheckPlan.addActionListener(this);
        btnExport.addActionListener(this);
        btnLoad.addActionListener(this);
    }

    // =====================================================================
    //  ACTION PERFORMED
    // =====================================================================
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if      (src == btnAddPersonal)     addPersonalPlan();
        else if (src == btnAddPro)          addProPlan();
        else if (src == btnDisplayAll)      displayAll();
        else if (src == btnClear)           clearFields();
        else if (src == btnGivePrompt)      givePrompt();
        else if (src == btnPurchasePrompts) purchaseAdditionalPrompts();   // NEW
        else if (src == btnAddMember)       addTeamMember();
        else if (src == btnRemoveMember)    removeTeamMember();
        else if (src == btnCheckPlan)       checkPlanType(getDisplayNumber());
        else if (src == btnExport)          exportToFile();
        else if (src == btnLoad)            loadFromFile();
    }

    // =====================================================================
    //  BUTTON HANDLER METHODS
    // =====================================================================

    /**
     * Reads model info + prompt quota from text fields,
     * creates a PersonalPlan and adds it to the plans ArrayList.
     * FIX: contextWindow is now correctly parsed as int (was String before).
     */
    private void addPersonalPlan() {
        try {
            String modelName = tfModelName.getText();
            double price     = Double.parseDouble(tfPrice.getText());
            int    params    = Integer.parseInt(tfParams.getText());
            int    window    = Integer.parseInt(tfWindowSize.getText());  // FIX: was String
            int    quota     = Integer.parseInt(tfPromptQuota.getText());

            PersonalPlan p = new PersonalPlan(modelName, price, params, window, quota);
            plans.add(p);

            displayArea.append("\nPersonal Plan added at index: " + (plans.size() - 1) + "\n");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid input! Price must be decimal; Params, Window and Quota must be whole numbers.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Reads model info + team slots from text fields,
     * creates a ProPlan and adds it to the plans ArrayList.
     * FIX: contextWindow is now correctly parsed as int (was String before).
     */
    private void addProPlan() {
        try {
            String modelName  = tfModelName.getText();
            double price      = Double.parseDouble(tfPrice.getText());
            int    params     = Integer.parseInt(tfParams.getText());
            int    window     = Integer.parseInt(tfWindowSize.getText());  // FIX: was String
            int    teamSlots  = Integer.parseInt(tfTeamSlots.getText());

            ProPlan p = new ProPlan(modelName, price, params, window, teamSlots);
            plans.add(p);

            displayArea.append("\nPro Plan added at index: " + (plans.size() - 1) + "\n");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid input! Price must be decimal; Params, Window and Team Slots must be whole numbers.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Iterates over the plans ArrayList and displays each plan's
     * index number and full details in the displayArea.
     */
    private void displayAll() {
        if (plans.isEmpty()) {
            displayArea.append("\nNo plans added yet.\n");
            return;
        }
        displayArea.append("\n===== All Plans =====\n");
        for (int i = 0; i < plans.size(); i++) {
            displayArea.append("\nIndex " + i + ":\n");
            displayArea.append(plans.get(i).display() + "\n");
            displayArea.append("---------------------\n");
        }
    }

    /**
     * Clears all text fields.
     */
    private void clearFields() {
        tfModelName.setText("");
        tfPrice.setText("");
        tfParams.setText("");
        tfWindowSize.setText("");
        tfPromptQuota.setText("");
        tfTeamSlots.setText("");
        tfPromptText.setText("");
        tfResponseLength.setText("");
        tfTeamMemberName.setText("");
        tfIndex.setText("");
        tfPurchasePrompts.setText("");
        displayArea.append("\nFields cleared.\n");
    }

    /**
     * Gets the index, checks instanceof PersonalPlan,
     * then calls enterPrompt() with prompt text and response length.
     */
    private void givePrompt() {
        int index = getDisplayNumber();
        if (index == -1) return;

        AIModel model = plans.get(index);

        if (model instanceof PersonalPlan) {
            try {
                PersonalPlan pp = (PersonalPlan) model;
                String promptText    = tfPromptText.getText();
                int    responseLength = Integer.parseInt(tfResponseLength.getText());
                String result        = pp.enterPrompt(promptText, responseLength);
                displayArea.append("\n" + result + "\n");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Response length must be a whole number.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Give a Prompt is only available for Personal Plan subscriptions.",
                    "Wrong Plan Type", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * NEW - Purchases additional prompts for a PersonalPlan.
     * Gets the index, checks instanceof PersonalPlan, then calls purchasePrompts()
     * with the quantity entered in tfPurchasePrompts.
     */
    private void purchaseAdditionalPrompts() {
        int index = getDisplayNumber();
        if (index == -1) return;

        AIModel model = plans.get(index);

        if (model instanceof PersonalPlan) {
            try {
                PersonalPlan pp = (PersonalPlan) model;
                int qty = Integer.parseInt(tfPurchasePrompts.getText().trim());
                String result = pp.purchasePrompts(qty);
                displayArea.append("\n" + result + "\n");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid whole number for the prompt quantity.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Purchase Prompts is only available for Personal Plan subscriptions.",
                    "Wrong Plan Type", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Gets the index, checks instanceof ProPlan,
     * then calls addTeamMember() with the provided name.
     */
    private void addTeamMember() {
        int index = getDisplayNumber();
        if (index == -1) return;

        AIModel model = plans.get(index);

        if (model instanceof ProPlan) {
            ProPlan  pp         = (ProPlan) model;
            String   memberName = tfTeamMemberName.getText();
            String   result     = pp.addTeamMember(memberName);
            displayArea.append("\n" + result + "\n");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Team collaboration is only available for Pro Plan subscriptions.",
                    "Wrong Plan Type", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Gets the index, checks instanceof ProPlan,
     * then calls removeTeamMember() with the provided name.
     */
    private void removeTeamMember() {
        int index = getDisplayNumber();
        if (index == -1) return;

        AIModel model = plans.get(index);

        if (model instanceof ProPlan) {
            ProPlan  pp         = (ProPlan) model;
            String   memberName = tfTeamMemberName.getText();
            String   result     = pp.removeTeamMember(memberName);
            displayArea.append("\n" + result + "\n");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Team collaboration is only available for Pro Plan subscriptions.",
                    "Wrong Plan Type", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Uses instanceof to determine whether the plan at the given index
     * is a PersonalPlan, ProPlan, or neither, and displays the result.
     *
     * @param index The valid ArrayList index to check.
     */
    private void checkPlanType(int index) {
        if (index == -1) return;

        AIModel model = plans.get(index);

        if (model instanceof PersonalPlan) {
            JOptionPane.showMessageDialog(this,
                    "Plan at index " + index + " is a: Personal Plan",
                    "Plan Type", JOptionPane.INFORMATION_MESSAGE);
        } else if (model instanceof ProPlan) {
            JOptionPane.showMessageDialog(this,
                    "Plan at index " + index + " is a: Pro Plan",
                    "Plan Type", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Plan type at index " + index + " is unknown.",
                    "Plan Type", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Exports all plan details to subscriptions.txt using FileWriter (character stream).
     */
    private void exportToFile() {
        try (FileWriter fw = new FileWriter("subscriptions.txt")) {
            if (plans.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No plans to export. Please add plans first.",
                        "Nothing to Export", JOptionPane.WARNING_MESSAGE);
                return;
            }
            for (int i = 0; i < plans.size(); i++) {
                AIModel m = plans.get(i);
                fw.write("Index: " + i + "\n");
                fw.write(m.display() + "\n");

                if (m instanceof PersonalPlan) {
                    PersonalPlan pp = (PersonalPlan) m;
                    fw.write("Plan Type: Personal Plan\n");
                    fw.write("Prompts Remaining: " + pp.getPromptsRemaining() + "\n");
                } else if (m instanceof ProPlan) {
                    ProPlan pp = (ProPlan) m;
                    fw.write("Plan Type: Pro Plan\n");
                    fw.write("Team Slots Remaining: " + pp.getTeamSlot() + "\n");
                }
                fw.write("---------------------\n");
            }
            JOptionPane.showMessageDialog(this,
                    "Plans exported successfully to subscriptions.txt",
                    "Export Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Export failed: " + e.getMessage(),
                    "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Loads subscriptions.txt and displays its contents in the displayArea.
     */
    private void loadFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("subscriptions.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            displayArea.append("\n===== Loaded From File =====\n");
            displayArea.append(sb.toString());
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this,
                    "File not found. Please export first.",
                    "Load Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Load failed: " + e.getMessage(),
                    "Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =====================================================================
    //  INDEX VALIDATION
    // =====================================================================

    /**
     * Reads the index from tfIndex, validates it is an integer within
     * the valid range of the plans ArrayList.
     *
     * @return A valid index, or -1 if input was invalid.
     */
    private int getDisplayNumber() {
        int displayNumber = -1;

        if (plans.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No plans available. Please add a plan first.",
                    "Empty List", JOptionPane.ERROR_MESSAGE);
            return -1;
        }

        try {
            int input = Integer.parseInt(tfIndex.getText().trim());
            if (input >= 0 && input < plans.size()) {
                displayNumber = input;
            } else {
                JOptionPane.showMessageDialog(this,
                        "Index " + input + " is out of range.\n"
                                + "Valid range: 0 to " + (plans.size() - 1),
                        "Invalid Index", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a whole number for the index.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
        return displayNumber;
    }

    // =====================================================================
    //  MAIN METHOD
    // =====================================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SubscriptionGUI::new);
    }
}