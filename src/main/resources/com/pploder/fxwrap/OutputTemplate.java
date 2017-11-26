package $package;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class $className {

    private static $className instance;

    public static $className getInstance() {
        return instance == null ? instance = new $className() : instance;
    }

    public String getRelativePath() {
        return "$relativePath";
    }

    public FXMLLoader load() throws IOException {
        FXMLLoader loader = new FXMLLoader($className.class.getResource(getRelativePath()));
        loader.load();
        return loader;
    }

}
