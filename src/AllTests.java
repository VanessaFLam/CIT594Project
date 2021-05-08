import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ GraphLTest.class, NodeTest.class, WikiGameTest.class, DataCleanerTest.class })
public class AllTests {

}
