package com.chinasoft.wt.repository;

import com.chinasoft.wt.model.WorkTimeRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by VerRan.Liu on 2017/9/22.
 */
public interface WorkTimeRecordRepository extends JpaRepository<WorkTimeRecord,Long> {
    public List<WorkTimeRecord> findByStaffCode(String staffCode);
}
