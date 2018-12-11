/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;

/**
 * FXML Controller class
 *
 * @author fernandondin
 */
public class JuegoController implements Initializable,EventHandler<ActionEvent> {

    //Usamos herencia para extender a Button
    private class ButtonCoord extends Button{
        private int x,y;
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
    public static Mine mine;
    private ButtonCoord[][] button;
   @Override
    public void handle(ActionEvent event) {
        ButtonCoord btn = (ButtonCoord)event.getSource();
        if(mine.getCell()[btn.x][btn.y].getElement() == -1)
            button[btn.x][btn.y].setText("✸");
        else if(mine.getCell()[btn.x][btn.y].getElement() > 0)
            button[btn.x][btn.y].setText(""+mine.getCell()[btn.x][btn.y].getElement());
        else{
            for (Mine.Cell n : mine.getCell()[btn.x][btn.y].getNeighbors()) {

            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //int spa = 64;
        //int spa = 57; //works with 9x9
        //int spa = 50; //works with 10x10
        //int spa = 43; //works with 11x11
        int spa = 36; //works with 12x12
        //int spa = 39;
        button = new ButtonCoord[mine.getX()][mine.getY()];
        GridPane gp = new GridPane();
        sp.setContent(gp);
        RowConstraints rowConstraints = new RowConstraints(spa, spa, spa);
        ColumnConstraints columnConstraints = new ColumnConstraints(spa, spa, spa);
        System.out.println(mine.getX());
        System.out.println(mine.getY());
        for(int i=0;i<mine.getX();i++){
            gp.getRowConstraints().add(i, rowConstraints);
            for(int j=0;j<mine.getY();j++){
              gp.getColumnConstraints().add(j, columnConstraints);
              ButtonCoord btn = new ButtonCoord(" ",i,j);
              btn.setPrefSize(spa, spa);
              btn.setOnAction(this);
              button[i][j] = btn;
              /*if(mine.getCell()[i][j].getElement() == -1)
                  btn.setText("*");
              else
                btn.setText(""+mine.getCell()[i][j].getElement());*/
              gp.add(btn, i, j);
                             
            }
        }
      /*ColumnConstraints col1 = new ColumnConstraints();
    col1.setHgrow( Priority.ALWAYS );

    ColumnConstraints col2 = new ColumnConstraints();
    col2.setHgrow( Priority.ALWAYS );
        //gp.getColumnConstraints().addAll( new ColumnConstraints( 6 ), col1, new ColumnConstraints( 6 ), col2 );
        gp.setMinSize(0, 0);
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
              Button btn = new Button(" ");
              btn.setPrefSize(70, 70);
              gp.add(btn, i, j);            
            }
        }*/
    }    
    
}
