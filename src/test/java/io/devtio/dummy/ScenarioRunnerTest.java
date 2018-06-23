package io.devtio.dummy;

import io.devtio.dummy.service.setups.ScenarioLoader;
import io.devtio.dummy.service.setups.SetupService;
import io.devtio.dummy.service.setups.model.NamespaceModel;
import io.kubernetes.client.ApiException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScenarioRunnerTest {

    @Autowired
    private SetupService setupService;

    @Autowired
    private ScenarioLoader loader;

//    @Ignore
    @Test
    public void testCreateStuff() throws ApiException {
        NamespaceModel namespace = loader.load("scenario1.yml");
        setupService.setup(namespace);
    }

}
