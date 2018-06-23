package io.devtio.dummy.service.setups;

import io.devtio.dummy.service.setups.model.NamespaceModel;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.apis.ExtensionsV1beta1Api;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class SetupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetupService.class);

    private CoreV1Api api;
    private ExtensionsV1beta1Api extensionsApi;

    public SetupService(ApiClient client) {
         api = new CoreV1Api(client);
         extensionsApi = new ExtensionsV1beta1Api(client);
    }

    public void setup(NamespaceModel namespaceModel) throws ApiException {
        String namespace = namespaceModel.getName();
        createNamespace(namespace);
        namespaceModel.getApps().forEach(app -> {
            String appName = app.getName();
            app.getVersions().forEach(version -> {
                createDeployment(namespace, appName, version);
                createService(namespace, appName, version);
            });
        });
    }

    private void createNamespace(String namespaceName) {
        try {
            V1Namespace namespace;
            try {
                namespace = api.readNamespace(namespaceName, "true", false, false);
                if (namespace != null) return;
            } catch (ApiException e) {
            }
            namespace = new V1Namespace();

            V1ObjectMeta meta = new V1ObjectMeta();
            meta.setName(namespaceName);
            namespace.setMetadata(meta);
            api.createNamespace(namespace, "true");

            LOGGER.info("Created Namespace: {}", namespaceName);

        } catch (Exception e) {
            LOGGER.error("Error creating a namespace: ", e);
            throw new RuntimeException(e);
        }
    }

    private void createService(String namespace, String targetApp, String targetVersion) {
        try {
            String name = targetApp + "-" + targetVersion;

            V1Service service;
            try {
                service = api.readNamespacedService(name, namespace, "true", false, false);
                if (service != null) return;
            } catch (ApiException exception) {
            }
            service = new V1Service();

            V1ObjectMeta meta = new V1ObjectMeta();
            service.setMetadata(meta);
            meta.setName(name);
            meta.setNamespace(namespace);
            service.setKind("Service");
            service.setApiVersion("v1");

            V1ServiceSpec spec = new V1ServiceSpec();
            service.setSpec(spec);

            V1ServicePort port = new V1ServicePort();
            port.setName("http");
            port.setPort(8090);
            port.setTargetPort(new IntOrString(8090));
            port.setProtocol("TCP");
            spec.setPorts(Arrays.asList(port));

            Map<String, String> selector = new HashMap<String, String>();
            selector.put("app", targetApp);
            selector.put("version", targetVersion);

            spec.setSelector(selector);

            spec.setSessionAffinity("None");
            spec.setType("ClusterIP");

            api.createNamespacedService(namespace, service, "true");

            LOGGER.info("Created Service: {}", name);

        } catch (Exception e) {
            LOGGER.error("Error creating a service: ", e);
            throw new RuntimeException(e);
        }
    }

    private void createDeployment(String namespace, String app, String version) {
        try {
            String name = app + "-" + version;
            ExtensionsV1beta1Deployment deployment;
            try {
                deployment = extensionsApi.readNamespacedDeployment(name, namespace, "true", false, false);
                if (deployment != null) return;
            } catch (ApiException exception) {
            }
            deployment = new ExtensionsV1beta1Deployment();

            V1ObjectMeta meta = new V1ObjectMeta();
            meta.setName(name);

            deployment.setMetadata(meta);

            ExtensionsV1beta1DeploymentSpec spec = new ExtensionsV1beta1DeploymentSpec();
            deployment.setSpec(spec);

            V1LabelSelector matchLabels = new V1LabelSelector();
            matchLabels.putMatchLabelsItem("app", app);
            matchLabels.putMatchLabelsItem("version", version.toString());
            spec.setSelector(matchLabels);

            spec.setReplicas(1);

            V1PodTemplateSpec podTemplateSpec = new V1PodTemplateSpec();
            V1ObjectMeta podMeta = new V1ObjectMeta();
            podMeta.putLabelsItem("app", app);
            podMeta.putLabelsItem("version", version.toString());
            podTemplateSpec.setMetadata(podMeta);

            V1PodSpec podSpec = new V1PodSpec();

            V1Container container = new V1Container();
            container.setName("app");
            container.setImage("devtio/dummy:latest");
            container.setImagePullPolicy("IfNotPresent");

            V1Probe readyLivenessProbe = new V1Probe();
            readyLivenessProbe.setInitialDelaySeconds(50);
            readyLivenessProbe.setPeriodSeconds(30);
            readyLivenessProbe.setFailureThreshold(20);
            readyLivenessProbe.setSuccessThreshold(1);
            readyLivenessProbe.setTimeoutSeconds(3);

            V1HTTPGetAction get = new V1HTTPGetAction();
            get.setPath("/actuator/health");
            get.setPort(new IntOrString(8090));
            readyLivenessProbe.setHttpGet(get);

            container.setReadinessProbe(readyLivenessProbe);
            container.setLivenessProbe(readyLivenessProbe);

            V1ContainerPort port = new V1ContainerPort();
            port.setContainerPort(8090);
            port.setProtocol("TCP");

            container.setPorts(Arrays.asList(port));

            podSpec.setContainers(Arrays.asList(container));

            podTemplateSpec.setSpec(podSpec);

            spec.setTemplate(podTemplateSpec);

            extensionsApi.createNamespacedDeployment(namespace, deployment, "true");

            LOGGER.info("Created Deployment: {}", name);

        } catch (Exception e) {
            LOGGER.error("Error creating a deployment: ", e);
            throw new RuntimeException(e);
        }
    }
}
