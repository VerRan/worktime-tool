package com.chinasoft.wt.repository;

import com.chinasoft.wt.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by VerRan.Liu on 2017/9/22.
 */
public interface StaffRepository extends JpaRepository<Staff,Long> {
    Staff findByStaffCode(String staffCode);
}
