package io.devtio.dummy.controller;

import io.devtio.dummy.model.Hop;
import io.devtio.dummy.service.HopsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class DummyController {

    @Autowired
    private HopsService demoService;

    @RequestMapping(method = RequestMethod.GET, path = "/hops")
    public List<Hop> hops(@RequestParam("path") String pathsCommaSeparated, @RequestHeader MultiValueMap headers) {
        String[] paths = pathsCommaSeparated.split(",");

        return demoService.getHops(Arrays.asList(paths), headers);
    }

}
