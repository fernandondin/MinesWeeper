/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author fernandondin
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label label;
    @FXML
    private Spinner spinnerX,spinnerY,nMinas;
    @FXML
    private Button play;
    
    private int mult;
    @FXML
    private void handleButtonAction(ActionEvent event){
        int x = Integer.parseInt(spinnerX.getValue().toString());
        int y = Integer.parseInt(spinnerY.getValue().toString());
        int nm = Integer.parseInt(nMinas.getValue().toString());
        JuegoController.mine = new Mine(x,y,nm);
        try{
            FXMLLoader fxmlLoader= new FXMLLoader(getClass().getResource("Juego.fxml"));
            Parent root1= (Parent)fxmlLoader.load();
            Stage stage= new Stage();
            stage.setResizable(false);
            stage.setScene(new Scene(root1));
            stage.show();
            stage.setOnCloseRequest(evt -> {
                System.exit(0);
            });
        }catch(Exception e){
        }
        System.out.println(x);
        System.out.println(y);
        Stage stage = (Stage) this.play.getScene().getWindow();
    // do what you have to do
    stage.close();
    }
    private void numeroMinas(){
        // un hilo para checar siempre el numero celdas que habra y que pueda haber n-1 minas 
        //para que no todas sean minas y exista al menos una casilla sin mina
        Runnable r = new Runnable() {
            public void run() {
                while(true){
                    int x = Integer.parseInt(spinnerX.getValue().toString());
                    int y = Integer.parseInt(spinnerY.getValue().toString());
                    SpinnerValueFactory<Integer> nm = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,(y*x)-1,1);
                    if(mult != (x*y)-1){
                        nMinas.setValueFactory(nm);
                        mult = (x*y)-1;
                    }
                }
            }
        };
        Thread th = new Thread(r);
        th.start();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb){
        //mínimo valor 8 y maximo el número maximo de un int
        SpinnerValueFactory<Integer> nm = new SpinnerValueFactory.IntegerSpinnerValueFactory(8,Integer.MAX_VALUE,8);
        SpinnerValueFactory<Integer> nm1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(8,Integer.MAX_VALUE,8);
        this.spinnerX.setValueFactory(nm);
        this.spinnerY.setValueFactory(nm1);
        numeroMinas();
        //Utilizamos animaciones de javafx
        ScaleTransition st = new ScaleTransition(Duration.millis(1000),label);
        st.setByX(0.5f);
        st.setByY(0.5f);
        st.setCycleCount(Timeline.INDEFINITE);
        st.setAutoReverse(true);
        st.play();
    }    
    
}
