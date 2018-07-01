package io.devtio.dummy.service;

import io.devtio.dummy.model.Hop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;

@Component
public class HopsService {

    private Logger LOGGER = LoggerFactory.getLogger(HopsService.class);

    @Value("${app.id}")
    String appId;

    @Value("${app.version}")
    String appVersion;

    @Value("${server.port:8080}")
    Integer port;

    @Autowired
    private RestTemplate template;

    public List<Hop> getHops(List<String> path, MultiValueMap headers) {

        LOGGER.info("type=fetching-hops, app_id={}, app_version={}, action=getHops, paths={}, headers={}", appId, appVersion, path, headers);

        List<Hop> returnList = new LinkedList<>();


        // Validations
        if (path.isEmpty()) {
            returnList.add(new Hop(appId, appVersion, null, null, "'path' is empty"));
            return returnList;
        }

        Hop thisHop = new Hop(appId, appVersion, path.get(0));
        returnList.add(thisHop);

        // Call downstream with the remaining paths
        if (path.size() > 1) {
            List<String> nextPath = path.subList(1, path.size());

            String nextHop = nextPath.get(0);
            thisHop.setNextHop(nextHop);

            HttpEntity entity = new HttpEntity(headers);

            String url = "http://" + nextHop + ":" + port + "/hops?path=" + String.join(",", nextPath);
            try {
                ResponseEntity<List<Hop>> response =
                        template.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Hop>>() {
                        });

                if (response.getStatusCode().is2xxSuccessful()) {
                    returnList.addAll(response.getBody());
                } else {
                    LOGGER.warn("url={}. type=error-fetching-downstream, app_id={}, app_version={}, action=getHops, paths={}, headers={}, target={}, error_response={}", url, appId, appVersion, path, headers, nextHop, response.getBody());
                    thisHop.setError("Unsuccessful response " + response.getStatusCodeValue() + "calling downstream to: " + nextHop);
                }
            } catch (Exception e) {
                LOGGER.warn("url={}, type=exception-fetching-downstream, app_id={}, app_version={}, action=getHops, paths={}, headers={}, target={}", url, appId, appVersion, path, headers, nextHop);
                thisHop.setError("Unsuccessful response " + e.getMessage() + " calling downstream to: " + nextHop);
            }
        }

        return returnList;
    }


}
