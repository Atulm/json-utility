package com.example.demo.controller;

import com.example.demo.utils.ExternalAPIUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.example.demo.utils.JSONUtils.filterJSON;
import static com.example.demo.utils.ObjectsUtils.convertStringToList;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Controller
public class JSONTransformationController {

    @RequestMapping("/transform")
    @ResponseBody
    public String transform(@RequestBody String requestBody) {
        String filteredJSON = "";
        try {
            Response externalAPIResponse = ExternalAPIUtils
                    .execute("https://test-api.skulibrary.dev/getTestProductData");
            String inputJSON = externalAPIResponse.body().string();
            if (nonNull(externalAPIResponse) && isNotBlank(requestBody))
            {
                String attributesToFilter = new Gson().fromJson(requestBody, JsonObject.class).get("filters").toString();
                return filterJSON(inputJSON,
                        convertStringToList(attributesToFilter));
            }
        } catch (Exception ex) {
            filteredJSON.concat("Encountered some issues during transforming JSON "+ ex.getMessage());
        } return filteredJSON;
    }
}
