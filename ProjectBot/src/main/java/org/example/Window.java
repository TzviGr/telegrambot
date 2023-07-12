package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.util.Map;
import java.util.Set;
public class Window extends JFrame {
    public static final int WINDOW_HEIGHT = 1500;
    public static final int WINDOW_WIDTH = 1500;
    private JLabel requestsLabel;
    private JLabel chatIdLabel;
    private JLabel poplarChatIdLabel;
    private JLabel poplarLabel;
    private JTextArea interactions;
    private InformationBot informationBot;
    private List<String> apiService = new ArrayList<>();

    private List<String> chooseApi = new ArrayList<>();
    public static List<Boolean> booleans = new ArrayList<>();
    private int countApi = 0;
    private int countTrue = 0;
    private int windowWidth = 1000;
    private int WindowHeight = 600;
    private int buttonWidth = 100;
    private int buttonHeight = 50;
    private int chooseWidth = 3 * buttonWidth + 200;

    private int chooseHeight = 100;

    public Window() {
        try {
            informationBot = new InformationBot();
            this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.setLayout(null);
            requestsLabel = new JLabel();
            chatIdLabel = new JLabel();
            poplarChatIdLabel = new JLabel();
            poplarLabel = new JLabel();
            interactions = new JTextArea();
            updateLabels();


            requestsLabel.setBounds(20, 20, 300, 23);
            chatIdLabel.setBounds(20, 60, 300, 23);
            poplarChatIdLabel.setBounds(20, 100, 700, 23);
            poplarLabel.setBounds(20, 140, 600, 24);
            interactions.setBounds(20, 210, 440, 550);
            interactions.setEditable(false);
            interactions.setLineWrap(true);
            interactions.setWrapStyleWord(true);
            requestsLabel.setFont(new Font("Ariel", 1, 23));
            chatIdLabel.setFont(new Font("Ariel", 1, 23));
            poplarChatIdLabel.setFont(new Font("Ariel", 1, 23));
            poplarLabel.setFont(new Font("Ariel", 1, 23));

            this.add(requestsLabel);
            this.add(chatIdLabel);
            this.add(poplarChatIdLabel);
            this.add(poplarLabel);
            this.add(interactions);

            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(informationBot);
            this.setLocationRelativeTo(null);
            this.setResizable(false);
            this.setVisible(true);
            apiService.add("Jokes");
            apiService.add("country");
            apiService.add("Cat Facts");
            apiService.add("quotes");
            apiService.add("numbers");

            for (int i = 0; i < 5; i++) {
                booleans.add(false);
            }

            JButton buttonsApi = new JButton(apiService.get(0));
            this.add(buttonsApi);
            buttonsApi.setBounds(880, 100, buttonWidth, buttonHeight);
            buttonsApi.addActionListener((e -> {
                if (countApi == 4) {
                    countApi = 0;
                } else {
                    countApi++;
                }
                buttonsApi.setText(this.apiService.get(countApi));

            }));

            JButton addApi = new JButton("add api");
            this.add(addApi);
            addApi.setBounds(1000 , 100, buttonWidth, buttonHeight);

            JLabel choose = new JLabel("you choose:" + chooseApi);
            this.add(choose);
            choose.setForeground(Color.BLUE);
            choose.setBounds((1000 - chooseWidth) + 250, 7, chooseWidth, chooseHeight);
            choose.setFont(new Font("Ariel", 2, 22));
            addApi.addActionListener((e) -> {
                if (!booleans.get(countApi) && countTrue < 3) {
                    booleans.set(countApi, true);
                    chooseApi.add(apiService.get(countApi));
                    choose.setText("you choose:" + chooseApi);
                    countTrue++;
                }
            });


            JButton removeApi = new JButton("remove api");
            this.add(removeApi);
            removeApi.setBounds(750, 100, buttonWidth, buttonHeight);

            removeApi.addActionListener((e) -> {
                if (booleans.get(countApi)) {
                    booleans.set(countApi, false);
                    chooseApi.remove(apiService.get(countApi));
                    choose.setText("you choose:" + chooseApi);
                    countTrue--;
                }
            });
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateLabels() {
//        new Timer(0, e -> {
//            String graphURL = generateGraphURL();
//            if (graphURL != null) {
//                ImageIcon graphImage = null;
//                try {
//                    graphImage = new ImageIcon(new URL(graphURL));
//                } catch (MalformedURLException ex) {
//                    throw new RuntimeException(ex);
//                }
//                JLabel graphLabel = new JLabel(graphImage);
//                graphLabel.setBounds(450, 170, graphImage.getIconWidth(), graphImage.getIconHeight());
//                this.add(graphLabel);
//            }
//                requestsLabel.setText("Total Requests: " + informationBot.getCountOfRequests());
//                chatIdLabel.setText("Total Chat IDs: " + informationBot.getCountOfChatId());
//                poplarChatIdLabel.setText("Most Active User: " + informationBot.getPoplarChatId());
//                poplarLabel.setText("Most Popular: " + informationBot.getPoplar());
//                List<String> lastInteractions = informationBot.displayLastInteractions();
//                StringBuilder interactionsText = new StringBuilder("The list of the last 10 interactions of the bot:\n\n\n");
//                interactions.setFont(new Font("Ariel", 4, 19));
//                for (String interaction : lastInteractions) {
//                    interactionsText.append(interaction).append("\n\n");
//                }
//                this.interactions.setText(interactionsText.toString());
//        }).start();
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            private String graphURL;
            private String poplarChatId;
            private List<String> lastInteractions;


            @Override
            protected Void doInBackground() {
                graphURL = generateGraphURL();
                poplarChatId = informationBot.getPoplarChatId();
                lastInteractions = informationBot.displayLastInteractions();
                return null;
            }

            @Override
            protected void done() {
                try {
                    requestsLabel.setText("Total Requests: " + informationBot.getCountOfRequests());
                    chatIdLabel.setText("Total Chat IDs: " + informationBot.getCountOfChatId());
                    poplarChatIdLabel.setText(poplarChatId);
                    poplarLabel.setText("Most Popular: " + informationBot.getPoplar());

                    StringBuilder interactionsText = new StringBuilder("The list of the last 10 interactions of the bot:\n\n\n");
                    interactions.setFont(new Font("Arial", 4, 19));
                    for (String interaction : lastInteractions) {
                        interactionsText.append(interaction).append("\n\n");
                    }
                    interactions.setText(interactionsText.toString());

                    if (graphURL != null) {
                        try {
                            ImageIcon graphImage = new ImageIcon(new URL(graphURL));
                            JLabel graphLabel = new JLabel(graphImage);
                            graphLabel.setBounds(450, 170, graphImage.getIconWidth(), graphImage.getIconHeight());

                            SwingUtilities.invokeLater(() -> {
                                // Remove the previous graph label (if any) before adding the new one
                                Component[] components = getContentPane().getComponents();
                                for (Component component : components) {
                                    if (component instanceof JLabel && component != requestsLabel && component != chatIdLabel &&
                                            component != poplarChatIdLabel && component != poplarLabel && component != interactions) {
                                        getContentPane().remove(component);
                                    }
                                }

                                add(graphLabel);
                                revalidate();
                                repaint();
                            });
                        } catch (MalformedURLException ex) {
                            ex.printStackTrace();
                        }
                    }

                    SwingUtilities.invokeLater(() -> {
                        JLabel choose = new JLabel("You choose: " + chooseApi);
                        choose.setForeground(Color.BLUE);
                        choose.setBounds((1000 - chooseWidth) + 250, 7, chooseWidth, chooseHeight);
                        choose.setFont(new Font("Arial", Font.BOLD, 22));
                        getContentPane().add(choose);
                        revalidate();
                        repaint();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // Schedule the next update
                    Timer timer = new Timer(1000, e -> updateLabels());
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        };
        worker.execute();
    }

    private String generateGraphURL() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            int[] num = {informationBot.getCountOfJokes(),informationBot.getCountOfRestCountries() , informationBot.getCountOfCatFacts(),informationBot.getCountOfQuotes(),informationBot.getCountOfNumbers()};
            String requestData = objectMapper.writeValueAsString(num);
            HttpResponse<String> response = Unirest.post("https://quickchart.io/chart/create")
                    .header("Content-Type", "application/json")
                    .body("{\"chart\": {\"type\": \"bar\", \"data\": {\"labels\": [\"Jokes\", \"Rest Countries\", \"Cat Facts\", \"Quotes\", \"Numbers\"], \"datasets\": [{\"label\":\"Description of the requests\",\"data\": " + requestData + "  }]}}}")
                    .asString();
            if (response.getStatus() == 200) {
                String responseBody = response.getBody();
                Map<String, Object> responseJson = objectMapper.readValue(responseBody, new TypeReference<>() {});
                return (String) responseJson.get("url");
            }
        } catch (JsonProcessingException | UnirestException e) {
            e.printStackTrace();
        }

        return null;
    }
}
