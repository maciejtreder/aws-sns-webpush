package com.maciejtreder.aws.webpush.endpoints;

import com.maciejtreder.aws.webpush.PushService;
import com.maciejtreder.aws.webpush.dao.LogsStore;
import com.maciejtreder.aws.webpush.dao.SubscriptionStore;
import com.maciejtreder.aws.webpush.model.Log;
import com.maciejtreder.aws.webpush.model.Notification;
import com.maciejtreder.aws.webpush.model.SafariSubscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Profile("web")
@RestController
@EnableWebMvc
@RequestMapping("/safari/v1")
public class SafariSubscriptionEndpoint {

    @Autowired
    private LogsStore logsStore;

    @Autowired
    private SubscriptionStore subscriptionStore;

    @RequestMapping(path = "/pushPackages/{websitePushID:.+}", produces = { "application/zip" }, method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<byte[]> getFile(@PathVariable(value="websitePushID") String fileName) throws IOException {
        fileName += ".zip";
        ResponseEntity respEntity;
        ClassLoader classLoader = getClass().getClassLoader();
        File result = new File(classLoader.getResource(fileName).getFile());
        if(result.exists()){
            InputStream inputStream = new FileInputStream(result.getAbsolutePath());
            byte[] out=org.apache.commons.io.IOUtils.toByteArray(inputStream);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("content-disposition", "attachment; filename=" + fileName);

            respEntity = new ResponseEntity(out, responseHeaders, HttpStatus.OK);
        }else{
            respEntity = new ResponseEntity ("File Not Found", HttpStatus.NOT_FOUND);
        }
        return respEntity;
    }

    @RequestMapping(path ="/log", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity log(@RequestBody Log log) {
        this.logsStore.put(log);
        return new ResponseEntity("log saved", HttpStatus.ACCEPTED);
    }

    @RequestMapping(path ="devices/{deviceToken}/registrations/{websitePushID:.+}", method = RequestMethod.POST)
    public String saveSubscription(@PathVariable("deviceToken") String deviceToken, @PathVariable("websitePushID") String websitePushID) {
        SafariSubscription subscription = new SafariSubscription(deviceToken, websitePushID);
        this.subscriptionStore.put(subscription);
        return "Subscription stored";
    }

    @RequestMapping(path ="devices/{deviceToken}/registrations/{websitePushID:.+}", method = RequestMethod.DELETE)
    public String deleteSubscription(@PathVariable("deviceToken") String deviceToken, @PathVariable("websitePushID") String websitePushID) {
        SafariSubscription subscription = new SafariSubscription(deviceToken, websitePushID);
        this.subscriptionStore.delete(subscription);
        return "Subscription deleted";
    }
}
