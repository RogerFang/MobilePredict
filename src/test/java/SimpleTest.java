import edu.whu.irlab.model.SystemProps;
import edu.whu.irlab.util.CalendarUtil;
import org.junit.Test;

/**
 * Created by Roger on 2016/5/18.
 */
public class SimpleTest {

    @Test
    public void test(){
        SystemProps systemProps = new SystemProps();
        System.out.println(systemProps.getBatchSize());
    }

    @Test
    public void testCalendar(){
        System.out.println(CalendarUtil.getLastMonth("201302"));
    }
}
