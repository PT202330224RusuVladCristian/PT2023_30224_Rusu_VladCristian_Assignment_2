package GUI;

import BussinesLogic.SimulationManager;
import Models.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SimulationGUI extends JFrame implements ActionListener, Runnable {

    private JButton startButton;
    private JLabel nrClientsLabel, nrQueuesLabel, maxSimulationTimeLabel;
    private JTextField nrClientsTextField, nrQueuesTextField, maxSimulationTimeTextField;
    private JTextArea simulationOutputTextArea;
    private JScrollPane simulationOutputScrollPane;

    private SimulationManager simulationManager;

    public SimulationGUI() {
        super("Queue Simulation");

        startButton = new JButton("Start Simulation");
        startButton.addActionListener(this);

        nrClientsLabel = new JLabel("Number of clients:");
        nrClientsTextField = new JTextField("5", 10);

        nrQueuesLabel = new JLabel("Number of queues:");
        nrQueuesTextField = new JTextField("3", 10);

        maxSimulationTimeLabel = new JLabel("Max simulation time:");
        maxSimulationTimeTextField = new JTextField("30", 10);

        simulationOutputTextArea = new JTextArea();
        simulationOutputScrollPane = new JScrollPane(simulationOutputTextArea);


        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(nrClientsLabel);
        inputPanel.add(nrClientsTextField);
        inputPanel.add(nrQueuesLabel);
        inputPanel.add(nrQueuesTextField);
        inputPanel.add(maxSimulationTimeLabel);
        inputPanel.add(maxSimulationTimeTextField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(simulationOutputScrollPane, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(outputPanel, BorderLayout.SOUTH);

        add(mainPanel);


        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int nrClients = Integer.parseInt(nrClientsTextField.getText());
        int nrQueues = Integer.parseInt(nrQueuesTextField.getText());
        int maxSimulationTime = Integer.parseInt(maxSimulationTimeTextField.getText());

        simulationManager = new SimulationManager(nrClients, nrQueues, maxSimulationTime);
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        List<Client> generatedClients = simulationManager.getGeneratedClients();
        simulationOutputTextArea.append("Generated clients:\n");
        for (Client client : generatedClients) {
            simulationOutputTextArea.append(client.toString() + "\n");
        }

        simulationOutputTextArea.append("\nStarting simulation...\n");

        simulationManager.run();

        simulationOutputTextArea.append("\nSimulation finished.\n");
    }

    public static void main(String[] args) {
        SimulationGUI simulationGUI = new SimulationGUI();

    }
}

