package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
    @FindBy(id = "logoutButton")
    WebElement logoutButton;

    @FindBy(id = "nav-notes-tab")
    WebElement noteTabBtn;

    @FindBy(id = "addNoteBtn")
    WebElement addNoteBtn;

    @FindBy(id = "note-id")
    WebElement noteId;

    @FindBy(id = "note-title")
    WebElement noteTitle;

    @FindBy(id = "note-description")
    WebElement noteDescription;

    @FindBy(id = "noteSubmit")
    WebElement noteSubmit;

    @FindBy(id = "saveNoteBtn")
    WebElement saveNoteBtn;

    @FindBy(className = "classNoteTitle")
    WebElement classNoteTitle;
    @FindBy(className = "classNoteDescription")
    WebElement classNoteDescription;

    @FindBy(id = "editNoteBtn")
    WebElement editNoteBtn;
    @FindBy(id = "deleteNoteBtn")
    WebElement deleteNoteBtn;

    WebDriver driver = null;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void logout(){
        logoutButton.submit();
    }

    public void createNote(Note note){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.noteTabBtn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.addNoteBtn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + note.getNoteTitle() + "';", this.noteTitle);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + note.getNoteDescription() + "';", this.noteDescription);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.saveNoteBtn);
    }

    public void editNote(Note note){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.noteTabBtn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.editNoteBtn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + note.getNoteTitle() + "';", this.noteTitle);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + note.getNoteDescription() + "';", this.noteDescription);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.saveNoteBtn);
    }

    public void deleteNote(){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.noteTabBtn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.deleteNoteBtn);
    }


    public Note getFirstNote() {
        Note result = new Note(null, null, null, null);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.noteTabBtn);

        result.setNoteTitle(classNoteTitle.getAttribute("innerHTML"));
        result.setNoteDescription(classNoteDescription.getAttribute("innerHTML"));
        return result;
    }

    public Note confusedCase(){
        Note result = new Note(null, null, null, null);
//        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.noteTabBtn);
        this.noteTabBtn.click();

        //Use Thread.sleep(1000); --> the result is ok.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String title = classNoteTitle.getText();
        String title2 = classNoteTitle.getAttribute("innerHTML");
        System.out.println("...... title = "+title + ", title2 = " +title2);

        result.setNoteTitle(classNoteTitle.getText());
        result.setNoteDescription(classNoteDescription.getText());
        return result;
    }

    public Note confusedCase2(){
        Note result = new Note(null, null, null, null);
//        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.noteTabBtn);
        this.noteTabBtn.click();

        //without Thread.sleep(1000); --> the result is not as expectation

        String title = classNoteTitle.getText();
        String title2 = classNoteTitle.getAttribute("innerHTML");
        System.out.println("...... title = "+title + ", title2 = " +title2);

        result.setNoteTitle(classNoteTitle.getText());
        result.setNoteDescription(classNoteDescription.getText());
        return result;
    }
}
