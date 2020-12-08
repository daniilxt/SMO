import Service.SMO
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Tab
import javafx.scene.control.TextField
import java.net.URL
import java.util.*


class Controller {
    @FXML
    private var resources: ResourceBundle? = null

    @FXML
    private var location: URL? = null

    @FXML
    private var tab_menu: Tab? = null

    @FXML
    private var count_app: TextField? = null

    @FXML
    private var devices_lambda: TextField? = null

    @FXML
    private var devices_count: TextField? = null

    @FXML
    private var sources_count: TextField? = null

    @FXML
    private var sources_lambda: TextField? = null

    @FXML
    private var buff_capacity: TextField? = null

    @FXML
    private var btn_start: Button? = null

    @FXML
    private var tab_step_mode: Tab? = null

    @FXML
    private var tab_result: Tab? = null
    private val fieldsList = mutableListOf<TextField?>()


    @FXML
    fun OnClickStart() {
        if (checkFields()) {
            try {
                val tmpArr = mutableListOf<Double>()
                fieldsList.forEach {
                    tmpArr.add(it!!.text.toDouble())
                }
                SMO(tmpArr[0].toInt(), tmpArr[1].toInt(), tmpArr[2].toInt(), tmpArr[3].toLong(),tmpArr[4]).runSMO()
            } catch (ex: Exception) {
                alert()
                println(ex.message)
            }
            //clearForms()
        } else {
            alert()
        }
    }


    private fun clearForms() {
        fieldsList.forEach {
            it?.clear()
        }
    }

    @FXML
    private fun alert() {
        var alert = Alert(Alert.AlertType.ERROR)
        alert.title = "Attention"
        alert.contentText = "Incorrect input"
        alert.showAndWait()
    }

    @FXML
    fun initialize() {
        createFieldsList()
    }

    fun createFieldsList() {
        fieldsList.add(buff_capacity)
        fieldsList.add(devices_count)
        fieldsList.add(sources_count)
        fieldsList.add(count_app)
        fieldsList.add(devices_lambda)
        fieldsList.add(sources_lambda)
    }

    private fun checkFields(): Boolean {
        return fieldsList.stream().allMatch { !it?.text.isNullOrEmpty() }
    }
}