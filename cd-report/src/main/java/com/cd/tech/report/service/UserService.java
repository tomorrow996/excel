package com.cd.tech.report.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by zc on 2017/4/20.
 */
public interface UserService {

    String handleFile(MultipartFile file) throws IOException;

}
