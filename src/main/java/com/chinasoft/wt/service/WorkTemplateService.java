package com.chinasoft.wt.service;

import com.chinasoft.wt.model.WorkTemplate;
import com.chinasoft.wt.repository.WorkTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by VerRan.Liu on 2017/9/22.
 */
@Service
public class WorkTemplateService {
    @Autowired
    private WorkTemplateRepository workTemplateRepository;
    public void add(WorkTemplate workTemplat) {
        workTemplateRepository.save(workTemplat);
    }

    public java.util.List<WorkTemplate> findAll() {
       return  workTemplateRepository.findAll();
    }
}
