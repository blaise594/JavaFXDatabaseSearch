package JavaFXDatabaseSearch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ExploreProduct extends Application {
	
	// Statement for executing queries
	  private Statement stmt;
	//create a GridPane
	  GridPane gridPane = new GridPane();
	  //display by properties
	  private RadioButton vin = new RadioButton("VIN");
	  private RadioButton make = new RadioButton("Make");
	  private RadioButton model = new RadioButton("Model");
	  private RadioButton year = new RadioButton("Year");
	  private RadioButton price = new RadioButton("Price");
	  
	  private RadioButton vinOrder = new RadioButton("VIN");
	  private RadioButton makeOrder = new RadioButton("Make");
	  private RadioButton modelOrder = new RadioButton("Model");
	  private RadioButton yearOrder = new RadioButton("Year");
	  private RadioButton priceOrder = new RadioButton("Price");
	 
	  HBox orderButtons = new HBox(2);
	  HBox searchButtons = new HBox(2);
	  
	  
	  ToggleGroup searchGroup = new ToggleGroup();
	  ToggleGroup orderGroup = new ToggleGroup();
	  
	  
	  private TextField searchTerm = new TextField();
	  
	  private TextArea results = new TextArea();
	  HBox searchBox = new HBox(2);
	  private Button searchButton = new Button("Search");
	  private Button resetButton = new Button("Reset");
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// Initialize database connection and create a Statement object
	    initializeDB();
	    
	    gridPane.setAlignment(Pos.CENTER);		
		gridPane.setHgap(5); gridPane.setVgap(5);
		
		
		gridPane.add(new Label("Instructions: "), 1, 0);
		gridPane.add(new Label("Fill out form below to search product inventory."), 2, 0);
		
		//Search by
		gridPane.add(new Label("Search by:"), 1, 1);
		
		searchButtons.getChildren().add(vin);
		searchButtons.getChildren().add(make);
		searchButtons.getChildren().add(model);
		searchButtons.getChildren().add(year);
		searchButtons.getChildren().add(price);
		gridPane.add(searchButtons, 2, 1);
		
		 vin.setToggleGroup(searchGroup);
		 make.setToggleGroup(searchGroup);
		 model.setToggleGroup(searchGroup);
		 year.setToggleGroup(searchGroup);
		 price.setToggleGroup(searchGroup);
		 
		 //Oderby
		 gridPane.add(new Label("Order by: "), 1, 3);
		 orderButtons.getChildren().addAll(vinOrder, makeOrder, modelOrder, yearOrder, priceOrder);
		 vinOrder.setToggleGroup(orderGroup);
		 makeOrder.setToggleGroup(orderGroup);
		 modelOrder.setToggleGroup(orderGroup);
		 yearOrder.setToggleGroup(orderGroup);
		 priceOrder.setToggleGroup(orderGroup);
		 gridPane.add(orderButtons, 2, 3);
		 
		 //search term
		 gridPane.add(new Label("Enter search term:"), 1, 7);
		 
		 searchBox.getChildren().addAll(searchTerm, searchButton, resetButton);
		 gridPane.add(searchBox, 2, 7);
		 
		 
		 
		//results
		 gridPane.add(new Label("Results: "), 1, 8);
		 
		 gridPane.add(results, 2, 8);
		 searchButton.setOnAction(e -> showResults());
		 resetButton.setOnAction(e -> reset());
		// Create a scene and place it in the stage
	    Scene scene = new Scene(gridPane, 700, 500);
	    primaryStage.setTitle("Manage Inventory"); // Set the stage title
	    primaryStage.setScene(scene); // Place the scene in the stage
	    primaryStage.show(); // Display the stage
	}
	private void initializeDB() {
	    try {
	      // Load the JDBC driver
	      Class.forName("com.mysql.jdbc.Driver");
//	      Class.forName("oracle.jdbc.driver.OracleDriver");
	      System.out.println("Driver loaded");

	      // Establish a connection
	      Connection connection = DriverManager.getConnection
	        ("jdbc:mysql://localhost:3306/example","root","");

	      System.out.println("Database connected");

	      // Create a statement
	      stmt = connection.createStatement();
	    }
	    catch (Exception ex) {
	      ex.printStackTrace();
	    }
	  }
	private void showResults() {
		String searchFor = searchTerm.getText();
		
		int searchByStatus=0;
		if(vin.isSelected()) {
			searchByStatus+=0;
		}
		if(make.isSelected()){
			searchByStatus+=1;
		}	
		if(model.isSelected()) {
			searchByStatus+=2;
		}
		if(year.isSelected()){
			searchByStatus+=3;
		}
		if(price.isSelected()){
			searchByStatus+=4;
		}
		
		int orderByStatus=0;
		if(vinOrder.isSelected()) {
			orderByStatus+=0;
		}
		if(makeOrder.isSelected()){
			orderByStatus+=1;
		}	
		if(modelOrder.isSelected()) {
			orderByStatus+=2;
		}
		if(yearOrder.isSelected()){
			orderByStatus+=3;
		}
		if(priceOrder.isSelected()){
			orderByStatus+=4;
		}
		
		String[] searchBystr = {"vin", "make", "model", "year", "price"};
        
		
		String[] orderBystr = {"vin", "make", "model", "year", "price"};
		
		
		
		 String searchString = "select * from product where product." + searchBystr[searchByStatus] +" = '" + searchFor+"'" + " order by "+ orderBystr[orderByStatus];
	                    
	        System.out.println(searchString);
//	        
	        
	    try {
//	      

	      ResultSet rset = stmt.executeQuery(searchString);

	      ArrayList<String> returnArray = new ArrayList<String>();
	      
	      if (rset.next()) {
	    	while(rset.next()) {  
	        String returnValue = rset.getString(1)+" "+rset.getString(2)+" "+rset.getString(3)+" "+rset.getString(4)+" "+rset.getString(5)+" "+rset.getString(6);
	        
	        //results.setText(returnValue);
	        returnArray.add(returnValue);
	    	}
	    	final String concatString= returnArray.stream()
                    .collect(Collectors.joining("\n"));
	    	results.setText(concatString);
	      } else {
	        results.setText("Not found");
	      }
	    }
	    catch (SQLException ex) {
	      ex.printStackTrace();}
	    
	}
	private void reset() {
		
		searchTerm.setText("");
		results.setText("");
		vin.setSelected(false);
		make.setSelected(false);
		model.setSelected(false);
		year.setSelected(false);
		price.setSelected(false);
		
		vinOrder.setSelected(false);
		makeOrder.setSelected(false);
		modelOrder.setSelected(false);
		yearOrder.setSelected(false);
		priceOrder.setSelected(false);
		
	}
	
	public static void main(String[] args) {
	    launch(args);
	  }
}
