package com.chinasoft.wt.ui;

import com.chinasoft.wt.model.Staff;
import com.chinasoft.wt.service.StaffService;
import com.chinasoft.wt.service.WorkTimeRecordService;
import com.chinasoft.wt.vo.SummaryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.util.StringUtils;

/**
 * Created by VerRan.Liu on 2017/9/22.
 *
 * ShellUI 用于实现工时计算工具的使用shell工具
 *
 * 1. 登录系统，只要输入自己的工号即可，当前不做验证
 * 2. 进入后可以直接输入help查看可用命令
 * 3. importXLSX  excel文件路径（已将工时信息拷贝到此excel的第一个sheet中）
 * 4. querywtl query work time length 查看实际的工作时长
 * 5. queryawtl query available work time length  查看可用的工作时长
 * 6. summay 汇总工时
 *
 * list 列出已导入工时信息【暂不支持】
 */
@ShellComponent
public class ShellUI {
    @Autowired
    private WorkTimeRecordService workTimeRecordService;

    @ShellMethod("import the worktime data from  xlsx file,the param is abs path of xlsx file")
    public String imp(String  xlsxPath) throws Exception{
        int i= 0;
        try {
            if(xlsxPath.contains(".xls")||xlsxPath.contains("txt")){
                return "当前只支持.xlsx格式";
            }
            if(!xlsxPath.contains(".xlsx")){//如果没有加后缀，自动添加上
                xlsxPath=xlsxPath+".xlsx";
            }
            i = workTimeRecordService.importWTRfromXLS(xlsxPath,"107207");
        } catch (Exception e) {
            return new String("import error ,please check your file fomate and content！".getBytes());
        }
        return "import success ,you have import "+i+" data ，now you can use show command to display summary";
    }

    @ShellMethod("show the summary data of work time list ")
    public String show(){
        StringBuffer buffer =new StringBuffer();
        SummaryVO summaryVO = workTimeRecordService.summary();
        //buffer.append("--company_work_time_length--------my_work_time_length--------------i_can_use_length---------------");
        buffer.append("should time length:"+summaryVO.getExpectWTL()+"\n");
        buffer.append("actual time length:"+summaryVO.getActWTL()+"\n");
        buffer.append("can use time length:"+summaryVO.getAlvTWL()+"\n");
        buffer.append("shoud add time length:"+summaryVO.getShouldApendTWL()+"\n");
        return buffer.toString();
    }


    //    @Autowired
//    private StaffService staffService;
//
//
//    @ShellMethod("注册,register staffCode password 格式 ")
//    public String register(String staffCode,String password ){
//        if(StringUtils.isEmpty(staffCode)){
//            return "register 必须输入工号";
//        }
//
//        if(StringUtils.isEmpty(password)){
//            return "register 必须输入密码";
//        }
//        Staff staff =new Staff();
//        staff.setStaffCode(staffCode);
//        staff.setPassword(password);
//        staffService.add(staff);
//        return "恭喜您,注册成功！，如需登录请使用 login staffcode password 登录";
//    }
//
//    @ShellMethod("登录,login staffCode password 格式 ")
//    public String login(String staffCode,String password){
//        if(StringUtils.isEmpty(staffCode)){
//            return "login 必须有工号信息";
//        }
//        Staff staff =new Staff();
//        staff.setStaffCode(staffCode);
//        staff.setPassword(password);
//        staffService.login(staff);
//        return staffCode+"欢迎您!"+" 现在您可以进行工时excel的导入，使用  importXLSX path 命令";
//    }


}
