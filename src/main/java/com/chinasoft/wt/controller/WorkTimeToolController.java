package com.chinasoft.wt.controller;

import com.chinasoft.wt.service.WorkTimeRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;


@Controller
public class WorkTimeToolController {

    Logger logger= LoggerFactory.getLogger(WorkTimeToolController.class);

    @Autowired
    WorkTimeRecordService workTimeRecordService;

    /***
     * 跳转首页
     * */
    @GetMapping("/")
    public String index(){
        return "index";
    }

    /***
     * 登陆功能，不需要密码，这里主要是为了区分不同用户的上传和计算
     * */
    @PostMapping("/login")
    public String login(@RequestParam("staffCode") String staffCode,HttpSession session){
        session.setAttribute("staffCode",staffCode);
        return "redirect:/caculatePage";
    }


    /**
     *
     * 计算工时主页面
     *
     * **/
    @GetMapping("caculatePage")
    public  String caculatePage(HttpSession session){
        if(StringUtils.isEmpty((String)session.getAttribute("staffCode"))){
            return index();
        }
        return "caculatePage";//跳转到工时计算页面
    }

    /**
     * 导入excel数据到数据库
     * 同时汇总计算，将结果返回页面
     * */
    @PostMapping("/caculate")
    public String caculate(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes,HttpSession session) throws Exception {

        //1.将上传文件存储到文件服务器,返回磁盘中的路径
       String diskPath= workTimeRecordService.uploadFileToDisk(file);

        //2.将上传文件的数据存入数据库
        workTimeRecordService.importWTRfromXLS(diskPath
                ,(String) session.getAttribute("staffCode"));
        redirectAttributes.addFlashAttribute("message",
                "您的考勤数据上传成功：" + file.getOriginalFilename() + "!");
        redirectAttributes.addFlashAttribute("summary",
                workTimeRecordService.summary((String)session.getAttribute("staffCode")));
        return "redirect:/caculatePage";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = workTimeRecordService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

}
