package com.pploder.fxwrap;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import org.junit.Test;
import static org.junit.Assert.*;

import com.pploder.fxwrap.TestPaneFXML;

import java.io.IOException;

public class TestPaneTest {

    @Test
    public void testControllerExists() throws ClassNotFoundException {
        Class.forName("com.pploder.fxwrap.TestPaneController");
    }

    @Test
    public void testGeneratedExists() throws ClassNotFoundException {
        Class.forName("com.pploder.fxwrap.TestPaneFXML");
    }

    @Test
    public void testLoad() throws IOException {
        com.sun.javafx.application.PlatformImpl.startup(() -> {});

        FXMLLoader loader = TestPaneFXML.getInstance().load();
        TestPaneController controller = loader.getController();
        AnchorPane pane = loader.getRoot();

        assertNotNull(controller);
        assertNotNull(pane);
    }

}
