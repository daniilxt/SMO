import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.stage.Stage

class MainApplication : javafx.application.Application() {
    @FXML
    lateinit var input: TextField

    @FXML
    lateinit var output: Label

    override fun start(stage: Stage) {
/*        stage.scene = Scene(FXMLLoader.load(javaClass.getResource("MainApplication.fxml")))
        val controller: Controller =loader.getController()
        stage.show()*/
        try {
            val loader = FXMLLoader(javaClass.getResource("MainApplication.fxml"))

            stage.scene = Scene(loader.load())
            stage.title = "SMO"
            stage.show()
            val controller: Controller = loader.getController()
            controller.createFieldsList()
        } catch (ex: Exception) {

        }
    }
}

fun main() {
    javafx.application.Application.launch(MainApplication::class.java)
}