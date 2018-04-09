package com.niiwoo.shield.manage.sys.service.dubbo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.niiwoo.shield.manage.sys.SysManageServiceApplication;
import com.niiwoo.shield.manage.sys.dto.DepartmentDTO;
import com.niiwoo.shield.manage.sys.dto.UserDTO;
import com.niiwoo.shield.manage.sys.service.DepartmentDubboService;
import com.niiwoo.shield.manage.sys.service.UserDubboService;
import com.niiwoo.tripod.service.component.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dell on 2017/12/19.
 * Description：shield-parent
 */
@Slf4j
@SpringBootTest(classes = SysManageServiceApplication.class)
@RunWith(SpringRunner.class)
public class DepartmentDubboServiceTest {

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Reference(version = "1.0.0")
    private UserDubboService userDubboService;
    @Reference(version = "1.0.0")
    private DepartmentDubboService departmentDubboService;


    @Test
    public void addDepartmentTest(){
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setDepartmentCode("66666");
        departmentDTO.setDepartmentName("测试666部门");
        departmentDTO.setCreatedUser("测试小王");
        departmentDTO.setUpdatedUser("测试小王");

        departmentDubboService.addDepartment(departmentDTO);
        System.out.println("添加部门------------");
    }

    @Test
    public void queryUsers(){
        List<Long> userids = Arrays.asList();
        List<UserDTO> userDTOS = userDubboService.queryUsersByuserIds(userids);

        for (UserDTO userDTO : userDTOS) {
            System.out.println(JSON.toJSONString(userDTO));
        }
        System.out.println("=====================================");
    }



}
