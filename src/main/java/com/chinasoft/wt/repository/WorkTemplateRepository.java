package com.chinasoft.wt.repository;

import com.chinasoft.wt.model.WorkTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by VerRan.Liu on 2017/9/22.
 */
@Repository
public interface WorkTemplateRepository extends JpaRepository<WorkTemplate,Long>{

}
