package io.devtio.dummy.service.setups;

import io.devtio.dummy.service.setups.model.AppModel;
import io.devtio.dummy.service.setups.model.NamespaceModel;
import io.devtio.dummy.service.setups.model.RoutingModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;

@Component
public class ScenarioLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScenarioLoader.class);

    @Autowired
    private ResourceLoader resourceLoader;

    public NamespaceModel load(String scenarioFileName) {
        try {
            Resource resource = resourceLoader.getResource("classpath:/" + scenarioFileName);
            Yaml yaml = new Yaml();
            Map<String, Object> scenarioAsMap = yaml.load(resource.getInputStream());

            String namespaceName = (String) scenarioAsMap.get("namespace");
            NamespaceModel namespaceModel = new NamespaceModel(namespaceName);

            List<Map<String, Object>> apps = (List<Map<String, Object>>) scenarioAsMap.get("apps");
            apps.forEach(app -> {
                String appName = (String) app.get("name");
                String version = (String) app.get("version");

                Map<String, Object> routingMap = (Map<String, Object>) app.get("routing");
                RoutingModel routingModel = new RoutingModel(routingMap);

                AppModel appModel = new AppModel(appName, version, routingModel);

                namespaceModel.addApp(appModel);
            });

            return namespaceModel;
        } catch (Exception e) {
            LOGGER.error("Unable to load scenario file: " + scenarioFileName);
            throw new RuntimeException(e);
        }
    }
}
