import DAO.Application
import DAO.Event
import DAO.Statistics
import Service.SMO
import Service.Source
import Service.Stat
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.text.Text
import java.net.URL
import java.util.*


class Controller {

    @FXML
    private var tab_menu: Tab? = null

    @FXML
    private var tab_pane: TabPane? = null

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
    private var time_step: Text? = null

    @FXML
    private var tab_step_mode: Tab? = null

    @FXML
    private var table_view_calendar: TableView<Application>? = null

    @FXML
    private var table_view_buffer: TableView<Application>? = null

    @FXML
    private var table_view_result: TableView<Statistics>? = null

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

    @FXML
    private var tab_result: Tab? = null

    @FXML
    private var id_text: Text? = null


    @FXML
    private var source_num_rs: TableColumn<Statistics, Long>? = null

    @FXML
    private var app_num_rs: TableColumn<Statistics, Long>? = null

    @FXML
    private var p_otk_rs: TableColumn<Statistics, Double>? = null

    @FXML
    private var t_wait_rs: TableColumn<Statistics, Double>? = null

    @FXML
    private var t_handle_rs: TableColumn<Statistics, Double>? = null

    @FXML
    private var t_sys_rs: TableColumn<Statistics, Double>? = null

    @FXML
    private var d_wait_rs: TableColumn<Statistics, Double>? = null

    @FXML
    private var d_handle_rs: TableColumn<Statistics, Double>? = null

    private var fieldsList = mutableListOf<TextField?>()
    private var appList = mutableListOf<Application>()
    private var bufferList = mutableListOf<Application>()
    private var statList = mutableListOf<Statistics>()

    private var isStep = false
    private var smoObj: SMO? = null
    private var smoThread: Thread? = null
    //private var  obList:ObservableList<Application> = FXCollections.observableArrayList()

    @FXML
    fun OnSelectShange() {
    }

    @FXML
    fun OnClickStart() {

        if (checkFields()) {
            try {
                val tmpArr = mutableListOf<Double>()
                fieldsList.forEach {
                    tmpArr.add(it!!.text.toDouble())
                }
                smoThread = Thread {
                    smoObj = SMO(
                        tmpArr[0].toInt(),
                        tmpArr[1].toInt(),
                        tmpArr[2].toInt(),
                        tmpArr[3].toLong(),
                        tmpArr[4],
                        isStep
                    )
                    if (isStep) {
                        smoObj?.runStepSMO() {}
                    } else {
                        smoObj?.runSMO()
                        printStatistics(smoObj?.getStatistics())
                    }
                }
                smoThread!!.start()

            } catch (ex: Exception) {
                println(ex.message)
                alert()
            }
            //clearForms()
        } else {
            alert()
        }
    }

    private fun printStatistics(statistics: MutableList<Statistics>?) {
        try {
            statList = statistics!!
            table_view_result?.items?.clear()
            table_view_result?.items?.addAll(statList)
        } catch (ex: java.lang.Exception) {

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

        createFieldsList()
        initColumns()
    }

    @FXML
    fun OnNextStep() {
        nextStep(1)
    }

    private fun nextStep(it: Any) {
        smoObj?.runStepSMO {
        }
        try {
            appList = smoObj!!.getApps()
            table_view_calendar?.items?.clear()
            table_view_calendar?.items?.addAll(appList)

            bufferList = smoObj!!.getBuffer()
            table_view_buffer?.items?.clear()
            table_view_buffer?.items?.addAll(bufferList)
            time_step!!.text = (smoObj?.getCurrentTime().toString())

        } catch (e: Exception) {

        }
    }

    private fun initColumns() {

        app_num_sm?.cellValueFactory = PropertyValueFactory("applicationNumber")
        source_sm?.cellValueFactory = PropertyValueFactory("src")
        time_sm?.cellValueFactory = PropertyValueFactory("time")
        event_sm?.cellValueFactory = PropertyValueFactory("event")

        app_num_buff?.cellValueFactory = PropertyValueFactory("applicationNumber")
        source_buff?.cellValueFactory = PropertyValueFactory("src")
        time_buff?.cellValueFactory = PropertyValueFactory("time")

        app_num_rs?.cellValueFactory = PropertyValueFactory("src")
        source_num_rs?.cellValueFactory = PropertyValueFactory("countApp")
        p_otk_rs?.cellValueFactory = PropertyValueFactory("pDenied")
        t_wait_rs?.cellValueFactory = PropertyValueFactory("timeWait")
        t_handle_rs?.cellValueFactory = PropertyValueFactory("timeHandle")
        t_sys_rs?.cellValueFactory = PropertyValueFactory("timeSystem")
        d_wait_rs?.cellValueFactory = PropertyValueFactory("dispWait")
        d_handle_rs?.cellValueFactory = PropertyValueFactory("dispHandled")

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