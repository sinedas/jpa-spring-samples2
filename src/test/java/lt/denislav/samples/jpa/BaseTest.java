package lt.denislav.samples.jpa;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.annotation.Rollback;

@Rollback(value = false)
public class BaseTest {
    protected static final Log log = LogFactory.getLog("lt.denislav.samples.jpa");

    protected void logDashes(String name) {
        log.info("----------" + name + "----------");
        log.info("----------");
        log.info("----------");
        log.info("----------");
        log.info("----------");
    }
}
