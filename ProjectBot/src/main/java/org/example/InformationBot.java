package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class InformationBot extends TelegramLongPollingBot {
    private Map<Long,Integer> levelsMap;
    private Map<String, Integer> chatIdCounts = new HashMap<>();
    private int countOfJokes=0;
    private int countOfNumbers=0;
    private int countOfCatFacts=0;
    private int countOfRestCountries=0;
    private int countOfQuotes=0;
    private long countOfChatId=0;


    public InformationBot(){
        this.levelsMap=new HashMap<>();
    }
    public String getBotUsername() {
        return "tzviRemanderBot";
    }


    public String getBotToken() {
        return "6154812753:AAENZ_15VpMwm4AGVnPJX2rjpBMEv7hmTJk";
    }
    private String whichButton;
    private long chatId;
    private String name;
    private String massage;
    private List<String> interactions = new ArrayList<>();
    private String interaction1;
    private List<InlineKeyboardButton> topRow = new ArrayList<>();
    private InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

    public  void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            name=update.getCallbackQuery().getFrom().getFirstName();
        } else {
            chatId = update.getMessage().getChatId();
            name=update.getMessage().getFrom().getFirstName();

        }
        Integer level = this.levelsMap.get(chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        InlineKeyboardButton Numbers = new InlineKeyboardButton();
        Numbers.setText("Numbers");
        Numbers.setCallbackData("Numbers");

        InlineKeyboardButton CatFacts = new InlineKeyboardButton();
        CatFacts.setText("Cat Facts");
        CatFacts.setCallbackData("Cat Facts");

        InlineKeyboardButton jokes = new InlineKeyboardButton();
        jokes.setText("jokes");
        jokes.setCallbackData("jokes");

        InlineKeyboardButton RestCountries = new InlineKeyboardButton();
        RestCountries.setText("Rest Countries ");
        RestCountries.setCallbackData("Rest Countries");

        InlineKeyboardButton Quotes = new InlineKeyboardButton();
        Quotes.setText("Quotes");
        Quotes.setCallbackData("Quotes");


        if (level == null) {
            sendMessage.setText("What would you like to know from what I offer?");
            topRow.clear();
            if (Window.booleans.get(0)) {
                topRow.add(jokes);
            }
            if (Window.booleans.get(1)) {
                topRow.add(RestCountries);
            }
            if (Window.booleans.get(2)) {
                topRow.add(CatFacts);
            }
            if (Window.booleans.get(3)) {
                topRow.add(Quotes);
            }
            if (Window.booleans.get(4)) {
                topRow.add(Numbers);
            }
            List<List<InlineKeyboardButton>> keyboard = List.of(this.topRow);
            inlineKeyboardMarkup.setKeyboard(keyboard);

            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            send(sendMessage);
            countOfChatId++;
            this.levelsMap.put(chatId, 1);
        } else {
            new Thread(() -> {
                if (level == 1) {
                    massage = update.getCallbackQuery().getData();
                    ObjectMapper objectMapper;
                    switch (massage) {
                        case "jokes" -> {
                            try {
                                HttpResponse<String> result =
                                        Unirest.get("https://v2.jokeapi.dev/joke/Any").asString();
                                objectMapper = new ObjectMapper();
                                Jokes jokes1 = null;
                                try {
                                    jokes1 = objectMapper.readValue(result.getBody(), Jokes.class);
                                } catch (JsonProcessingException e) {
                                    throw new RuntimeException(e);
                                }
                                if (jokes1.getDelivery() != null && jokes1.getSetup() != null) {
                                    sendMessage.setText("Setup: " + jokes1.getSetup() + "\n\n" +
                                            "  Delivery: " + jokes1.getDelivery());
                                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                                    this.levelsMap.put(chatId, 1);
                                } else {
                                    sendMessage.setText(jokes1.getJoke());
                                    this.levelsMap.put(chatId, 1);
                                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                                }
                                send(sendMessage);
                                countOfJokes++;
                                String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                                interaction1 = "User: " + name + ", Activity: " + massage + ", Time: " + time;
                                interactions.add(interaction1);
                            } catch (UnirestException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        case "Cat Facts" -> {
                            HttpResponse<String> response1 =
                                    null;
                            try {
                                response1 = Unirest.get("https://catfact.ninja/fact").asString();
                            } catch (UnirestException e) {
                                throw new RuntimeException(e);
                            }
                            objectMapper = new ObjectMapper();
                            CatModel catModel = null;
                            try {
                                catModel = objectMapper.readValue(response1.getBody(), CatModel.class);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                            sendMessage.setText("The information I have about your request is:    " + catModel.getFact());
                            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                            this.levelsMap.put(chatId, 1);
                            send(sendMessage);
                            countOfCatFacts++;
                            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                            interaction1 = "User: " + name + ", Activity: " + massage + ", Time: " + time;
                            interactions.add(interaction1);
                        }
                        case "Numbers" -> {
                            sendMessage.setText("Which number would you like to know information?");
                            this.levelsMap.put(chatId, 2);
                            whichButton=massage;
                           send(sendMessage);
                        }
                        case "Quotes" -> {
                            HttpResponse<String> response =
                                    null;
                            try {
                                response = Unirest.get("https://api.quotable.io/quotes/random?limit=1").asString();
                            } catch (UnirestException e) {
                                throw new RuntimeException(e);
                            }
                            objectMapper = new ObjectMapper();
                            List<Quotes> quotes = null;
                            try {
                                quotes = objectMapper.readValue(response.getBody(), new TypeReference<>() {
                                });
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                            sendMessage.setText("The name of author is: " + quotes.get(0).getAuthor() + "\n\n" +
                                    "content: " + quotes.get(0).getContent());
                            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                            this.levelsMap.put(chatId, 1);
                            send(sendMessage);
                            countOfQuotes++;
                            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                            interaction1 = "User: " + name + ", Activity: " + massage + ", Time: " + time;
                            interactions.add(interaction1);
                        }
                        case "Rest Countries" -> {
                            sendMessage.setText("Which country would you like to know information about? Enter country code: ");
                            this.levelsMap.put(chatId, 2);
                            whichButton=massage;
                            send(sendMessage);
                        }
                    }
                }
            }).start();
            new  Thread(()->{
                if (level == 2) {
                    if (whichButton.equals("Numbers")) {
                        int number = Integer.parseInt(update.getMessage().getText());
                        String response =
                                null;
                        try {
                            response = Unirest.get("http://numbersapi.com/" + number).asString().getBody();
                        } catch (UnirestException e) {
                            throw new RuntimeException(e);
                        }
                        sendMessage.setText("The information I have about your number is:      " + response);
                        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                        this.levelsMap.put(chatId, 1);
                        send(sendMessage);
                        countOfNumbers++;
                        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                        interaction1 = "User: " + name + ", Activity: " + massage + ", Time: " + time;
                        interactions.add(interaction1);
                    } else if (whichButton.equals("Rest Countries")){
                        String code = update.getMessage().getText();
                        HttpResponse<String> response1 =
                                null;
                        try {
                            response1 = Unirest.get("https://restcountries.com/v2/alpha/" + code).asString();
                        } catch (UnirestException e) {
                            throw new RuntimeException(e);
                        }
                        ObjectMapper objectMapper = new ObjectMapper();
                        RestCountries restCountries = null;
                        try {
                            restCountries = objectMapper.readValue(response1.getBody(), RestCountries.class);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                        if (restCountries.getName() == null) {
                            sendMessage.setText("cannot find try again");
                            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                            this.levelsMap.put(chatId, 1);
                            send(sendMessage);
                        } else {
                            sendMessage.setText("name : " + restCountries.getName() + "\n\n" +
                                    "Population : " + restCountries.getPopulation() + "\n\n " +
                                    "capital: " + restCountries.getCapital() + "\n\n" +
                                    "borders: " + restCountries.getBorders());
                            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                            this.levelsMap.put(chatId,1);
                            send(sendMessage);
                            countOfRestCountries++;
                            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                            interaction1 = "User: " + name + ", Activity: " + massage + ", Time: " + time;
                            interactions.add(interaction1);
                        }
                    }
                }
            }).start();
        }
    }

    public String getPoplar() {
        int poplar = countOfJokes;
        String result = "";
        if (poplar > countOfQuotes && poplar > countOfNumbers && poplar > countOfCatFacts && poplar > countOfRestCountries) {
            result = "jokes is the poplar";
        } else if (countOfQuotes > poplar && countOfQuotes > countOfNumbers && countOfQuotes > countOfCatFacts && countOfQuotes > countOfRestCountries) {
            result = "Quotes is the poplar";
        } else if (countOfNumbers > poplar && countOfNumbers > countOfQuotes && countOfNumbers > countOfCatFacts && countOfNumbers > countOfRestCountries) {
            result = "Numbers is the poplar";
        }else if (countOfCatFacts > poplar && countOfCatFacts > countOfNumbers && countOfCatFacts > countOfRestCountries&&countOfCatFacts>countOfQuotes) {
            result = "CatFacts is the poplar";
        } else if (countOfRestCountries > poplar && countOfRestCountries > countOfQuotes && countOfRestCountries > countOfCatFacts && countOfRestCountries >countOfNumbers ) {
            result = "RestCountries is the poplar";
        }else{
            result="No popularity yet ";
        }
        return result;
    }
    public int  getCountOfRequests() {

        return countOfJokes+countOfNumbers+countOfRestCountries+countOfCatFacts+countOfQuotes;
    }

    public long getCountOfChatId() {
        return countOfChatId;
    }
    public String getPoplarChatId() {
        int maxCount = 0;
        String mostActiveChatId = "";
        chatIdCounts.put(name,getCountOfRequests());
        for (Map.Entry<String, Integer> entry : chatIdCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostActiveChatId = entry.getKey();
            }
        }
        return "Most active user is: "+mostActiveChatId+" with  "+maxCount+"  requests";
    }
    public List<String> displayLastInteractions() {
        List<String> result = new ArrayList<>();
        for (int i = 0;  i < interactions.size(); i++) {
            result.add(interactions.get(i));
            if (result.size()>10){
                result.remove(0);
            }
        }
        return  result;
    }

    public int getCountOfJokes() {
        return countOfJokes;
    }

    public int getCountOfNumbers() {
        return countOfNumbers;
    }

    public int getCountOfCatFacts() {
        return countOfCatFacts;
    }

    public int getCountOfRestCountries() {
        return countOfRestCountries;
    }

    public int getCountOfQuotes() {
        return countOfQuotes;
    }

    private void send(SendMessage sendMessage){
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
