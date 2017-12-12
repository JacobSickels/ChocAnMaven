package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.User;
import util.Regex;

import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.sql.ResultSet;

/**
 * Created by Jacob on 11/6/17.
 */
public class LoginController implements FxmlController {

    public Label errorLabel;
    private StateController sc;
    private DatabaseController db;

    public JFXTextField userField;

    private User u;

    public LoginController() {
        sc = StateController.getInstance();
        userField = new JFXTextField();
        db = DatabaseController.getInstance();
        u = User.getInstance();
    }

    //Validates id user enters in LoginView
    public boolean validate(int id){

        boolean notEmpty = false;
        ResultSet rs = db.executeSql(db.getChocAnProviderValidation(id));

        String name = "";

        try{
            notEmpty = rs.next();
            /*if(notEmpty){
                rs.first();
                createUser(id);

            }*/

            //Get data for createUser() from returned Result Set
            //this is not how you get things from result sets.
            name = rs.getString(2);

        }
        catch(Exception e){
            e.printStackTrace();
        }


        createUser(id, name);


        return notEmpty;
    }

    //Queries DB to get role corresponding with User
    //Returns string (subject to change) with role, to populate GUI with proper View
    //@TODO: Populate role_table with values
    public String getRole(){
        return u.getPermissionLevel();
    }

    //@TODO regex validation for passing values
    public void submit(ActionEvent actionEvent) throws IOException {

        System.out.println(userField.getText() + " length:" + userField.getText().length());

        if(!Regex.characterLength(userField.getText(), 4)){
            showError("Invalid ID length.");
            return;
        }

        boolean valid = validate(Integer.parseInt(userField.getText()));

        if(!valid) {
            showError("Invalid Provider number.");
        }
        else{
            sc.setView(View.CHOICE);
        }

    }

    private void createUser(int id, String name){
        System.out.println("Creating User");
        u.setName(name);
        u.setID(id);
    }



    private void showError(String error) {
        errorLabel.setText(error);
    }

    @Override
    public void updateUser() {
        return;
    }
}
