package com.cd.tech.report.controller;

import com.cd.tech.report.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * Created by zc on 2017/4/19.
 */
@Controller
@RequestMapping("test")
public class ExcelController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "import", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
//    @ResponseBody
    public ModelAndView importFile(MultipartFile file) {
        String result;
        try {
            result = userService.handleFile(file);
        } catch (IOException e) {
            result = e.getMessage();
        }
        return new ModelAndView("result").addObject("result", result);
    }

    @RequestMapping("importPage")
    public ModelAndView page() {
        return new ModelAndView("test");
    }

}
