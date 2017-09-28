package com.chinasoft.wt.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import com.chinasoft.wt.service.WorkTimeRecordService;
import com.chinasoft.wt.storage.StorageFileNotFoundException;
import com.chinasoft.wt.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;


@Controller
public class FileUploadController {

    Logger logger= LoggerFactory.getLogger(FileUploadController.class);
    private final StorageService storageService;

    @Autowired
    WorkTimeRecordService workTimeRecordService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam("staffCode") String staffCode,HttpSession session){
        session.setAttribute("staffCode",staffCode);
        logger.info("session.getId() is "+session.getId());
        return "redirect:/list";
    }



    @GetMapping("/list")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));
        model.addAttribute("summary",workTimeRecordService.summary());
        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes,HttpSession session) throws Exception {

        storageService.store(file);
        logger.info("session id is upload method "+session.getId());
        //将上传文件的数据存入数据库
        workTimeRecordService.importWTRfromXLS(file.getOriginalFilename(),(String) session.getAttribute("staffCode"));

        redirectAttributes.addFlashAttribute("message",
                "您的考勤数据上传成功：" + file.getOriginalFilename() + "!");

        return "redirect:/list";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
