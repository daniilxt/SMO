import DAO.Application
import DAO.Event
import Service.SMO
import javafx.fxml.FXML
import javafx.scene.control.*
import java.net.URL
import java.util.*
import javafx.scene.control.cell.PropertyValueFactory


class Controller {

    @FXML
    private var tab_menu: Tab? = null

    @FXML
    private var tab_pane: Tab? = null

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
    private var count_app: TextField? = null

    @FXML
    private var is_step: CheckBox? = null

    @FXML
    private var tab_step_mode: Tab? = null

    @FXML
    private var app_num_sm: TableColumn<Application, Int>? = null

    @FXML
    private var source_sm: TableColumn<Application, Long>? = null

    @FXML
    private var time_sm: TableColumn<Application, Long>? = null

    @FXML
    private var event_sm: TableColumn<Application, Event>? = null

    @FXML
    private var btn_next: Button? = null

    @FXML
    private var app_num_buff: TableColumn<Application, Long>? = null

    @FXML
    private var source_buff: TableColumn<Application, Long>? = null

    @FXML
    private var time_buff: TableColumn<Application, Long>? = null

    @FXML
    private var resources: ResourceBundle? = null

    @FXML
    private var location: URL? = null

    private var tab_result: Tab? = null
    private var fieldsList = mutableListOf<TextField?>()
    private var isStep = false
    private var smoObj: SMO? = null


    @FXML
    fun OnSelectShange() {
        smoObj?.runSMO()
    }
    @FXML
    fun OnClickStart() {

        if (checkFields()) {
            try {
                val tmpArr = mutableListOf<Double>()
                fieldsList.forEach {
                    tmpArr.add(it!!.text.toDouble())
                }
                smoObj = SMO(
                    tmpArr[0].toInt(),
                    tmpArr[1].toInt(),
                    tmpArr[2].toInt(),
                    tmpArr[3].toLong(),
                    tmpArr[4],
                    isStep
                )
            } catch (ex: Exception) {
                println(ex.message)
                alert()
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
        val alert = Alert(Alert.AlertType.ERROR)
        alert.title = "Attention"
        alert.contentText = "Incorrect input"
        alert.showAndWait()
    }

    @FXML
    fun OnStepModeClick() {
        isStep = true
    }

    @FXML
    fun initialize() {

/*        val tabPane = TabPane()
        val tab = Tab()
        tab.text = "new tab"
        tab.content = Rectangle(200.0, 200.0, Color.LIGHTSTEELBLUE)
        tabPane.tabs.add(tab)*/
        createFieldsList()
       // initColumns()
    }

    @FXML
    fun OnNextStep() {
        smoObj?.nextStep()
    }

    private fun initColumns() {

        app_num_sm?.cellValueFactory = PropertyValueFactory("App number")
        source_sm?.cellValueFactory = PropertyValueFactory("Source")
        time_sm?.cellValueFactory = PropertyValueFactory("Time")
        event_sm?.cellValueFactory = PropertyValueFactory("Event")

        app_num_buff?.cellValueFactory = PropertyValueFactory("App number")
        source_buff?.cellValueFactory = PropertyValueFactory("Source")
        time_buff?.cellValueFactory = PropertyValueFactory("Time")
    }

    fun createFieldsList() {
        fieldsList.add(buff_capacity)
        fieldsList.add(sources_count)
        fieldsList.add(devices_count)
        fieldsList.add(count_app)
        fieldsList.add(devices_lambda)
        fieldsList.add(sources_lambda)
    }

    private fun checkFields(): Boolean {
        return fieldsList.stream().allMatch { !it?.text.isNullOrEmpty() }
    }
}