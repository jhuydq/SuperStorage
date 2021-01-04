package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	private User user = null;
	private SignUpPage signUpPage = null;
	private LoginPage loginPage = null;
	private HomePage homePage = null;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		user = new User(null, "david", "null", "pw1234", "David", "Dinh");
		signUpPage = new SignUpPage(driver);
		loginPage = new LoginPage(driver);
		homePage = new HomePage(driver);
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
		user = null;
		signUpPage = null;
		loginPage = null;
		homePage = null;
	}

	@Test
	public void testAccessUnauthorizedUser(){
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testAccessAuthorizedUser(){
		driver.get("http://localhost:" + this.port + "/signup");
		signUpPage.signup(user);

		driver.get("http://localhost:" + this.port + "/login");
		loginPage.login(user.getUsername(), user.getPassword());

		Assertions.assertEquals("Home", driver.getTitle());

		homePage.logout();
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());
	}

	private void signUpLogin(){
		driver.get("http://localhost:" + this.port + "/signup");
		signUpPage.signup(user);

		driver.get("http://localhost:" + this.port + "/login");
		loginPage.login(user.getUsername(), user.getPassword());
	}

	@Test
	public void testCreateNote(){
		signUpLogin();

		Note note = new Note(null, "To do list", "The first", null);
		homePage.createNote(note);

		Note result = homePage.getFirstNote();
		Assertions.assertEquals(note.getNoteTitle(), result.getNoteTitle());
		Assertions.assertEquals(note.getNoteDescription(), result.getNoteDescription());
	}

	@Test
	public void testEditNote(){
		signUpLogin();

		Note note = new Note(null, "To do list", "The first", null);
		homePage.createNote(note);

		note.setNoteTitle(note.getNoteTitle() + " edited");
		note.setNoteDescription(note.getNoteDescription() + " edited");
		homePage.editNote(note);

		Note result = homePage.getFirstNote();
		Assertions.assertEquals(note.getNoteTitle(), result.getNoteTitle());
		Assertions.assertEquals(note.getNoteDescription(), result.getNoteDescription());
	}

	@Test
	public void testDeleteNote(){
		signUpLogin();

		Note note = new Note(null, "To do list", "The first", null);
		homePage.createNote(note);
		homePage.deleteNote();

		Assertions.assertThrows(NoSuchElementException.class, ()->{
			homePage.getFirstNote();
		} );
	}

	@Test
	public void confusedCase1(){  //use Thread.sleep(1000);
		signUpLogin();

		Note note = new Note(null, "To do list", "The first", null);
		homePage.createNote(note);

		Note result = homePage.confusedCase();
		Assertions.assertEquals(note.getNoteTitle(), result.getNoteTitle());
		Assertions.assertEquals(note.getNoteDescription(), result.getNoteDescription());
	}

	@Test
	public void confusedCase2(){  //without Thread.sleep(1000);
		signUpLogin();

		Note note = new Note(null, "To do list", "The first", null);
		homePage.createNote(note);

		Note result = homePage.confusedCase2();
		Assertions.assertEquals(note.getNoteTitle(), result.getNoteTitle());
		Assertions.assertEquals(note.getNoteDescription(), result.getNoteDescription());
	}



}
