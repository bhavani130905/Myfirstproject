package project;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class ATMApp extends Application {
    private Map<String, User> users = new HashMap<>();
    private User currentUser;
    private Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Sample users
        users.put("1234", new User("1234", 1000.0));
        users.put("5678", new User("5678", 5000.0));
        users.put("0000", new User("0000", 2000.0));

        window = primaryStage;
        window.setTitle("ATMify");

        showSplashScreen();
    }

    private void showSplashScreen() {
        // Load your logo image (adjust the path if needed)
    	Image logo = new Image("file:C:\\Users\\Bhavani\\OneDrive\\사진\\Screenshots\\ATMify.png");
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(180);
        logoView.setPreserveRatio(true);
        logoView.setEffect(new DropShadow(20, Color.DARKCYAN));

        Label welcomeLabel = new Label("ATMify");
        welcomeLabel.setFont(Font.font("Elephant", 30));
        welcomeLabel.setTextFill(Color.web("#006064"));

        VBox layout = new VBox(30, logoView, welcomeLabel);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #e0f7fa, #b2ebf2);");
        Scene splashScene = new Scene(layout, 420, 320);

        window.setScene(splashScene);
        window.show();

        // Fade in effect
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), layout);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        // Pause for 3 seconds, then show login
        PauseTransition pause = new PauseTransition(Duration.seconds(10));
        pause.setOnFinished(e -> showLoginScreen());
        pause.play();
    }

    private void showLoginScreen() {
        Label pinLabel = new Label("Enter PIN:");
        pinLabel.setFont(Font.font("Centaur", 20));
        PasswordField pinField = new PasswordField();
        pinField.setFont(Font.font("Arial", 16));
        Button loginButton = createStyledButton("Login");
        Label message = new Label();

        VBox inputBox = new VBox(12, pinLabel, pinField, loginButton, message);
        inputBox.setAlignment(Pos.CENTER);

        BorderPane layout = new BorderPane();
        layout.setCenter(inputBox);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #f1f8e9, #c8e6c9);"
                + "-fx-background-radius: 18; -fx-border-radius: 18;");

        loginButton.setOnAction(e -> {
            String pin = pinField.getText();
            if (users.containsKey(pin)) {
                currentUser = users.get(pin);
                showMainMenu();
            } else {
                message.setText("❌ Incorrect PIN. Try again.");
                message.setTextFill(Color.RED);
            }
        });

        window.setScene(new Scene(layout, 400, 250));
        window.show();
    }

    private void showMainMenu() {
        Label welcome = new Label("Welcome! Choose an option:");
        welcome.setFont(Font.font("Palatino Linotype",25));
        welcome.setTextFill(Color.web("#01579b"));

        Button checkBalance = createStyledButton("Check Balance");
        Button deposit = createStyledButton("Deposit");
        Button withdraw = createStyledButton("Withdraw");
        Button history = createStyledButton("Transaction History");
        Button changePin = createStyledButton("Change PIN");
        Button logout = createStyledButton("Logout");
        Button exit = createStyledButton("Exit");

        VBox menuBox = new VBox(14, checkBalance, deposit, withdraw, history, changePin, logout, exit);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setPadding(new Insets(20));
        menuBox.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 16; -fx-background-radius: 16; "
                + "-fx-effect: dropshadow(gaussian, #b2ebf2, 10, 0.5, 0, 2);");

        BorderPane layout = new BorderPane();
        layout.setTop(welcome);
        BorderPane.setAlignment(welcome, Pos.CENTER);
        layout.setCenter(menuBox);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #e3f2fd, #b3e5fc);");

        checkBalance.setOnAction(e -> showBalanceScreen());
        deposit.setOnAction(e -> showDepositScreen());
        withdraw.setOnAction(e -> showWithdrawScreen());
        history.setOnAction(e -> showHistoryScreen());
        changePin.setOnAction(e -> showChangePinScreen());
        logout.setOnAction(e -> {
            currentUser = null;
            showLoginScreen();
        });
        exit.setOnAction(e -> window.close());

        window.setScene(new Scene(layout, 420, 420));
    }

    private void showBalanceScreen() {
        Label balanceLabel = new Label("Current Balance:  ₹" + String.format("%.2f", currentUser.getBalance()));
        balanceLabel.setFont(Font.font("Arial", 18));
        balanceLabel.setTextFill(Color.web("#388e3c"));
        Button back = createStyledButton("Back");
        back.setOnAction(e -> showMainMenu());

        VBox layout = new VBox(30, balanceLabel, back);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #fffde7; -fx-background-radius: 14;");
        window.setScene(new Scene(layout, 350, 180));
    }

    private void showDepositScreen() {
        Label label = new Label("Enter amount to deposit:");
        label.setFont(Font.font("Arial", 15));
        TextField amountField = new TextField();
        amountField.setFont(Font.font("Arial", 15));
        Button depositButton = createStyledButton("Deposit");
        Button back = createStyledButton("Back");
        Label message = new Label();

        depositButton.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (amount > 0) {
                    currentUser.deposit(amount);
                    message.setText("✅ Deposited ₹" + String.format("%.2f", amount));
                    message.setTextFill(Color.web("#388e3c"));
                } else {
                    message.setText("Enter a positive amount₹.");
                    message.setTextFill(Color.RED);
                }
            } catch (NumberFormatException ex) {
                message.setText("Invalid amount.");
                message.setTextFill(Color.RED);
            }
        });

        back.setOnAction(e -> showMainMenu());

        VBox layout = new VBox(14, label, amountField, depositButton, message, back);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #e8f5e9; -fx-background-radius: 14;");
        window.setScene(new Scene(layout, 350, 220));
    }

    private void showWithdrawScreen() {
        Label label = new Label("Enter amount to withdraw:");
        label.setFont(Font.font("Arial", 15));
        TextField amountField = new TextField();
        amountField.setFont(Font.font("Arial", 15));
        Button withdrawButton = createStyledButton("Withdraw");
        Button back = createStyledButton("Back");
        Label message = new Label();

        withdrawButton.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (amount > 0 && amount <= currentUser.getBalance()) {
                    currentUser.withdraw(amount);
                    message.setText("✅ Withdrew ₹" + String.format("%.2f", amount));
                    message.setTextFill(Color.web("#388e3c"));
                } else if (amount > currentUser.getBalance()) {
                    message.setText("Insufficient funds.");
                    message.setTextFill(Color.RED);
                } else {
                    message.setText("Enter a positive amount.");
                    message.setTextFill(Color.RED);
                }
            } catch (NumberFormatException ex) {
                message.setText("Invalid amount.");
                message.setTextFill(Color.RED);
            }
        });

        back.setOnAction(e -> showMainMenu());

        VBox layout = new VBox(14, label, amountField, withdrawButton, message, back);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #ffebee; -fx-background-radius: 14;");
        window.setScene(new Scene(layout, 350, 220));
    }

    private void showHistoryScreen() {
        Label label = new Label("Transaction History:");
        label.setFont(Font.font("Arial", 15));
        ListView<String> historyList = new ListView<>();
        historyList.getItems().addAll(currentUser.getHistory());
        Button back = createStyledButton("Back");
        back.setOnAction(e -> showMainMenu());

        VBox layout = new VBox(10, label, historyList, back);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f3e5f5; -fx-background-radius: 14;");
        window.setScene(new Scene(layout, 400, 300));
    }

    private void showChangePinScreen() {
        Label label = new Label("Enter new PIN:");
        label.setFont(Font.font("Arial", 15));
        PasswordField newPinField = new PasswordField();
        newPinField.setFont(Font.font("Arial", 15));
        Button changeButton = createStyledButton("Change PIN");
        Button back = createStyledButton("Back");
        Label message = new Label();

        changeButton.setOnAction(e -> {
            String newPin = newPinField.getText();
            if (newPin.length() == 4 && newPin.matches("\\d+")) {
                users.remove(currentUser.getPin());
                currentUser.setPin(newPin);
                users.put(newPin, currentUser);
                message.setText("PIN changed successfully!");
                message.setTextFill(Color.web("#388e3c"));
            } else {
                message.setText("PIN must be 4 digits.");
                message.setTextFill(Color.RED);
            }
        });

        back.setOnAction(e -> showMainMenu());

        VBox layout = new VBox(14, label, newPinField, changeButton, message, back);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #e1f5fe; -fx-background-radius: 14;");
        window.setScene(new Scene(layout, 350, 200));
    }

    // Helper to create styled buttons
    private Button createStyledButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Arial", 15));
        btn.setStyle("-fx-background-color: #4dd0e1; -fx-text-fill: #01579b; -fx-background-radius: 8; "
                + "-fx-border-radius: 8; -fx-border-color: #00bcd4; -fx-border-width: 1.5;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #00bcd4; -fx-text-fill: white; -fx-background-radius: 8; "
                + "-fx-border-radius: 8; -fx-border-color: #00bcd4; -fx-border-width: 1.5;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #4dd0e1; -fx-text-fill: #01579b; -fx-background-radius: 8; "
                + "-fx-border-radius: 8; -fx-border-color: #00bcd4; -fx-border-width: 1.5;"));
        btn.setPrefWidth(200);
        return btn;
    }

    // User class for account info and history
    static class User {
        private String pin;
        private double balance;
        private List<String> history = new ArrayList<>();

        public User(String pin, double balance) {
            this.pin = pin;
            this.balance = balance;
        }

        public String getPin() { return pin; }
        public void setPin(String newPin) { this.pin = newPin; }
        public double getBalance() { return balance; }
        public List<String> getHistory() { return history; }

        public void deposit(double amount) {
            balance += amount;
            history.add("Deposited ₹" + String.format("%.2f", amount));
        }

        public void withdraw(double amount) {
            balance -= amount;
            history.add("Withdrew ₹" + String.format("%.2f", amount));
        }
    }
}

