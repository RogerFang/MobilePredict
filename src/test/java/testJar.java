import edu.whu.irlab.process.model.PreProcessProps;
import edu.whu.irlab.process.model.InputParam;
import edu.whu.irlab.process.service.PredictService;
import edu.whu.irlab.process.service.TrainService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roger on 2016/5/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class testJar {
    @Autowired
    PreProcessProps preProcessProps;

    // @Autowired
    // TrainService trainService;
    //
    // @Autowired
    // PredictService predictService;

    @Test
    public void test(){
        System.out.println(preProcessProps.getProp("MODE_TRAIN"));
    }

    @Test
    public void testTrain(){
        InputParam inputParam = new InputParam();
        inputParam.setMode("train");
        inputParam.setModel("cnn");
        inputParam.setModelPath("model_path");
        inputParam.setStdOut(false);
        List<String> months = new ArrayList<>();
        months.add("201408.txt");
        months.add("201409.txt");
        inputParam.setMonths(months);
        // trainService.doTrain(inputParam);
        // predictService.doPredict(inputParam);
    }
}
