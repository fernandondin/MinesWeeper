/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javax.swing.Timer;

/**
 * FXML Controller class
 *
 * @author fernandondin
 */
public class JuegoController implements Initializable,EventHandler<ActionEvent>{
    class UpdateTask extends Task<String> {

    @Override
    protected String call() throws Exception {
        while(!dead) {
            updateValue(Long.toString((System.currentTimeMillis()-tiempo)/1000));
        }
        return timer.getText();
    }

}
    //Usamos herencia para extender a Button
    private class ButtonCoord extends Button implements Serializable{
        private int x,y;
        private boolean isMarked,hasFlag;
        public ButtonCoord(String t,int x,int y){
            this.setText(t);
            this.x=x;
            this.y=y;
        }
    }
    /**
     * Initializes the controller class.
     */
    @FXML
    private GridPane gp;
    @FXML
    private ScrollPane sp;
    @FXML
    private Label timer,flags,record;
    
    @FXML
    private CheckBox checkBox;
    private int nFlags;
    public static Mine mine;
    private boolean dead;
    private ButtonCoord[][] button;
    public static String nombre;
    long tiempo;
    private boolean inGame;
    public static boolean esCargado=false;
    private Datos d = new Datos();
    private TreeMap<String,Object> partida;
    @FXML
    public void save(ActionEvent ev) throws ClassNotFoundException, IOException{
        for(int i=0;i<button.length;i++){
            for(int j=0;j<button[i].length;j++){
                if(button[i][j].isMarked){
                    mine.getCell()[i][j].marked = true;
                }
            }
        }
        TreeMap<String,Object> tm = new TreeMap<>();
        tm.put("tiempo", tiempo);
        tm.put("mine", mine);
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nombre");
        dialog.setHeaderText("Ingrese el nombre de la partida:");
        Optional<String> respuesta = dialog.showAndWait();
        Date date = new Date();
        DateFormat formatH = new SimpleDateFormat("HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = "unknown"+".mine";
        if(respuesta == null || !respuesta.isPresent()){
            try{
                d.escribe(tm,"unknown.mine");
            }catch(Exception e){
                System.err.println(e);
            }
        }else{
            if(respuesta.get().trim().equals("")){
                try{
                d.escribe(tm, fecha);
                }catch(IOException | ClassNotFoundException e){
                    System.err.println("error al escribir");
                    System.err.println(e);
                }
            }else{
                try{
                    d.escribe(tm, respuesta.get().toLowerCase().trim()+".mine");
                }catch(IOException | ClassNotFoundException e){System.err.println("error al escribir");}
            }
        }
   
    }
    private void menu(){
        try{
            FXMLLoader fxmlLoader= new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
            Parent root1= (Parent)fxmlLoader.load();
            Stage stage= new Stage();
            //stage.getScene().getStylesheets().add(getClass().getResource("esilo.css").toExternalForm());           
            stage.setResizable(false);
            stage.setScene(new Scene(root1));
            stage.show();
            stage.setOnCloseRequest(evt -> {
                System.exit(0);
            });
            stage.getScene().getStylesheets().add(getClass().getResource("esilo.css").toExternalForm());           
        }catch(Exception e){
        } 
        Stage stage = (Stage) this.timer.getScene().getWindow();
        stage.close();
    }
    @FXML
    public void menu(ActionEvent ev){
        System.exit(1);
    }
    @FXML
    public void reiniciar(ActionEvent ev){
        JuegoController.mine = new Mine(mine.getX(),mine.getY(),mine.getMinas(),mine.getNombre());
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
        Stage stage = (Stage) this.timer.getScene().getWindow();
        stage.close();
    }
    private void lose(){
        for(int i=0;i<button.length;i++){
            for(int j=0;j<button[i].length;j++){
                if(mine.getCell()[i][j].getElement() == -1){
                    button[i][j].setText("✸");
                    button[i][j].styleProperty().set(" -fx-background-color: #e34040;\n" +
        "    -fx-border-color: #010101;\n" +
        "    -fx-border-radius: 10;\n" +
        "    -fx-background-radius: 10;\n"+"-fx-font-size:20;");
                }
            }
        }
    }
    private void win(){
        int c=0;
        for(int i=0;i<button.length;i++){
            for(int j=0;j<button[i].length;j++){
                if(button[i][j].isMarked){
                    c++;
                }
            }
        }
        System.out.println(Integer.parseInt(flags.getText().substring(0, flags.getText().indexOf("/"))));
        System.out.println(c);
        if(c == mine.getX()*mine.getY()- mine.getMinas()){
            dead=true;
            for(int i=0;i<button.length;i++){
                for(int j=0;j<button[i].length;j++){
                    //if(mine.getCell()[i][j].getElement() == -1){
                        button[i][j].setText("✔");
                        button[i][j].styleProperty().set(" -fx-background-color: #5ce04e;\n" +
            "    -fx-border-color: #010101;\n" +
            "    -fx-border-radius: 10;\n" +
            "    -fx-background-radius: 10;\n"+"-fx-font-size:20;");
                    //}
                }
            } 
        
        
        }
    }
   @Override
    public void handle(ActionEvent event) {      
        win();
        if(!inGame){
            if(!esCargado){
               tiempo=System.currentTimeMillis();
            }
            this.flags.setText(nFlags+"/"+mine.getMinas());
            UpdateTask updTask = new UpdateTask();
            timer.textProperty().bind(updTask.valueProperty());
            Thread changes = new Thread(updTask);
            changes.start();
            inGame=true;
        }
        ButtonCoord btn = (ButtonCoord)event.getSource();
        Mine.Cell pre = mine.getCell()[btn.x][btn.y];
        if(btn.hasFlag && !checkBox.selectedProperty().get()){
            return;
        }
        if(checkBox.selectedProperty().get()){
            if(btn.hasFlag){
                win();
                btn.setText("");
                nFlags--;
                btn.hasFlag = false;
                this.flags.setText(nFlags+"/"+mine.getMinas());
            }else if(!btn.isMarked){
                win();
                btn.hasFlag = true;
                btn.setText("🚩🚩🚩");
                nFlags++;
                this.flags.setText(nFlags+"/"+mine.getMinas());
            }
        }else{
            if(mine.getCell()[btn.x][btn.y].getElement() == -1){
                button[btn.x][btn.y].setText("✸");
                button[btn.x][btn.y].styleProperty().set(" -fx-background-color: #66b3aa;\n" +
    "    -fx-border-color: #010101;\n" +
    "    -fx-border-radius: 10;\n" +
    "    -fx-background-radius: 10;\n"+"-fx-font-size:20;");
                button[btn.x][btn.y].isMarked = true;
                dead = true;
                lose();
            }
            else if(mine.getCell()[btn.x][btn.y].getElement() > 0){
                button[btn.x][btn.y].setText(""+mine.getCell()[btn.x][btn.y].getElement());
                button[btn.x][btn.y].isMarked = true;
            }
            else if(!mine.getCell()[btn.x][btn.y].isMarked()){
                button[btn.x][btn.y].setText(mine.getCell()[btn.x][btn.y].getElement()+"");
                for(Mine.Cell vecino:mine.getCell()[btn.x][btn.y].getNeighbors()){
                     button[vecino.getX()][vecino.getY()].setText(vecino.getElement()+"");
                     updateN(mine.getCell()[vecino.getX()][vecino.getY()]);
                     button[vecino.getX()][vecino.getY()].isMarked = true;

                }
             }
        }
        updateStyle();
                win();
    }
    private void updateStyle(){
        for(int i=0;i<button.length;i++){
            for(int j=0;j<button[i].length;j++){
                if(button[i][j].getText().equals("0")){
                    button[i][j].setText("");
                    button[i][j].styleProperty().set("-fx-background-color:#fefefe;");
                    button[i][j].isMarked = true;
                }
                 if(button[i][j].hasFlag){
                    button[i][j].setText("⚐");
                    button[i][j].styleProperty().set(" -fx-background-color: #66b3aa;\n" +
"    -fx-border-color: #010101;\n" +
"    -fx-border-radius: 10;\n" +
"    -fx-background-radius: 10;\n"+"-fx-font-size:20;");
                }
            }
        }
    }
    private void updateN(Mine.Cell c){
        for(int i=0;i<button.length;i++){
            for(int j=0;j<button[i].length;j++){
                if(button[i][j].getText().equals("0") || mine.getCell()[i][j].getElement() == 0 ){
                    for(Mine.Cell vecino:mine.getCell()[i][j].getNeighbors()){
                        if(vecino.getElement() != -1){                           
                            button[vecino.getX()][vecino.getY()].setText(vecino.getElement()+"");
                            button[vecino.getX()][vecino.getY()].isMarked = true;
                        }
                    }
                }
            }
        }
    }
    private void updateN1(Mine.Cell c){
        for(Mine.Cell vecino:c.getNeighbors()){
            if(/*!button[c.getX()][c.getY()].isMarked &&*/ vecino.getElement() != -1){
                button[vecino.getX()][vecino.getY()].setText(vecino.getElement()+"");
                button[vecino.getX()][vecino.getY()].isMarked = true;
            }
        }
    }
    private void noCargado(){
        int base = 64;
        int spa = base - ((mine.getX() - 8)*7);
        if(spa < 43){
            spa = 43;
        }
        //int spa = 39;
        button = new ButtonCoord[mine.getX()][mine.getY()];
        record.setText(mine.getNombre());
        GridPane gp = new GridPane();
        sp.setContent(gp);
        RowConstraints rowConstraints = new RowConstraints(spa, spa, spa);
        ColumnConstraints columnConstraints = new ColumnConstraints(spa, spa, spa);
        for(int i=0;i<mine.getX();i++){
            gp.getRowConstraints().add(i, rowConstraints);
            for(int j=0;j<mine.getY();j++){
              gp.getColumnConstraints().add(j, columnConstraints);
              ButtonCoord btn = new ButtonCoord(" ",i,j);
              btn.setPrefSize(spa, spa);
              btn.setOnAction(this);
              button[i][j] = btn;
              gp.add(btn, i, j);
                             
            }
        }
        this.flags.setText(nFlags+"/"+mine.getMinas());
    }
    private void cargado(){
        TreeMap<String,Object> ts = null;
        try {
            ts = (TreeMap<String,Object>)d.lee(nombre);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JuegoController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JuegoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(ts == null){
            menu();
            return;
        }
        mine = (Mine)ts.get("mine");
        button = new ButtonCoord[mine.getX()][mine.getY()];
        tiempo = (long) ts.get("tiempo");
        this.flags.setText(nFlags+"/"+mine.getMinas());
            UpdateTask updTask = new UpdateTask();
            timer.textProperty().bind(updTask.valueProperty());
            Thread changes = new Thread(updTask);
            changes.start();
            inGame=true;
        record.setText(mine.getNombre());
        int base = 64;
        int spa = base - ((mine.getX() - 8)*7);
        if(spa < 43){
            spa = 43;
        }
        GridPane gp = new GridPane();
        sp.setContent(gp);
        RowConstraints rowConstraints = new RowConstraints(spa, spa, spa);
        ColumnConstraints columnConstraints = new ColumnConstraints(spa, spa, spa);
        for(int i=0;i<mine.getX();i++){
            gp.getRowConstraints().add(i, rowConstraints);
            for(int j=0;j<mine.getY();j++){
              gp.getColumnConstraints().add(j, columnConstraints);
              ButtonCoord btn = new ButtonCoord(" ",i,j);
              btn.setPrefSize(spa, spa);
              btn.setOnAction(this);
              button[i][j] = btn;
              if(mine.getCell()[i][j].isMarked()){
                  button[i][j].isMarked =true;
                  if(mine.getCell()[i][j].getElement() == -1){
                    button[i][j].setText("✸");
                    button[i][j].styleProperty().set(" -fx-background-color: #66b3aa;\n" +
        "    -fx-border-color: #010101;\n" +
        "    -fx-border-radius: 10;\n" +
        "    -fx-background-radius: 10;\n"+"-fx-font-size:20;");
                    button[i][j].isMarked = true;
            }
            else if(mine.getCell()[i][j].getElement() > 0){
                button[i][j].setText(""+mine.getCell()[btn.x][btn.y].getElement());
            }
            else {
                    button[i][j].setText("");
                    button[i][j].styleProperty().set("-fx-background-color:#fefefe;");
                    button[i][j].isMarked = true;
             }
              }
              gp.add(btn, i, j);                     
            }
        }
        this.flags.setText(nFlags+"/"+mine.getMinas());

    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //int spa = 64;
        //int spa = 57; //works with 9x9
        //int spa = 50; //works with 10x10
        //int spa = 43; //works with 11x11
        
        if(!esCargado){
            noCargado();            
        }else{
            cargado();
        }

    }    
    
}
