package com.niiwoo.shield.manage.sys.service.local;

import com.niiwoo.shield.manage.sys.dao.entity.SysPermission;
import com.niiwoo.shield.manage.sys.dao.mapper.SysPermissionMapperExt;
import com.niiwoo.shield.manage.sys.dao.mapper.SysRolePermissionMapperExt;
import com.niiwoo.shield.manage.sys.dto.PermissionDTO;
import com.niiwoo.tripod.provider.exception.BizException;
import com.niiwoo.tripod.service.component.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by luosiwen on 2017/12/22.
 */
@Slf4j
@Service
public class PermissionLocalService {


    @Autowired
    private SysPermissionMapperExt sysPermissionMapper;

    @Autowired
    private SysRolePermissionMapperExt sysRolePermissionMapper;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

//    @Autowired
//    private CacheCleaner cacheCleaner;


    @Transactional
    public void addPermission(PermissionDTO dto) {
        SysPermission permission = new SysPermission();
        permission.setPermissionName(dto.getPermissionName());
        permission.setPermissionValue(dto.getPermissionValue());
        permission.setMenuId(dto.getMenuId());
        permission.setStatus(dto.getStatus());
        permission.setDescription(dto.getDescription());
        permission.setCreatedUser(dto.getCreatedUser());
        permission.setUpdatedUser(dto.getUpdatedUser());

        //创建新资源
        try {
            Long id = snowflakeIdWorker.nextId();
            dto.setPermissionId(id);
            permission.setPermissionId(id);
            Integer count = sysPermissionMapper.insertSysPermission(permission);
            if (count==0) {
                throw new BizException("MS_100005");
            }
        }catch(Exception e){
            log.error("操作失败",e);
            throw new BizException("MS_100005");
        }
    }

    @Transactional
    public void updatePermission(PermissionDTO dto) {
        Long permissionId = dto.getPermissionId();
        SysPermission tmpPerm = findSysPermissionById(permissionId);
        if(tmpPerm==null){
            throw new BizException("MS_100002","资源");
        }
        //若permissionValue被修改，校验修改后的资源是否已存在
        String permissionValue = dto.getPermissionValue();
        if(!tmpPerm.getPermissionValue().equals(permissionValue)){
            //校验权限是否已存在
            Integer permCount = findSysPermCountByCondition(dto);
            if(permCount > 0){
                throw new BizException("MS_100003","修改后的资源路径");
            }
        }

        SysPermission permission = new SysPermission();
        permission.setPermissionId(dto.getPermissionId());
        permission.setPermissionName(dto.getPermissionName());
        permission.setPermissionValue(dto.getPermissionValue());
        permission.setDescription(dto.getDescription());
        permission.setStatus(dto.getStatus());
        permission.setUpdatedUser(dto.getUpdatedUser());
        permission.setUpdatedTime(new Date());
        //更新权限
        try {
            Integer count = sysPermissionMapper.updateSysPermission(permission);
            if (count == 0) {
                throw new BizException("MS_100005");
            }
        }catch(Exception e){
            log.error("操作失败",e);
            throw new BizException("MS_100005");
        }
    }

    public SysPermission findSysPermissionById(Long permissionId){
        return sysPermissionMapper.findSysPermissionById(permissionId);
    }

    public Integer findSysPermCountByCondition(PermissionDTO permissionDTO){
        return sysPermissionMapper.findSysPermCountByCondition(permissionDTO);
    }

    public List<PermissionDTO> queryPermissionsByMenuId(Long menuId){
        return sysPermissionMapper.queryPermissionsByMenuId(menuId);
    }

    public Integer queryRolePermissionCountByPermId(Long permissionId){
        return sysRolePermissionMapper.queryRolePermissionCountByPermId(permissionId);
    }

    @Transactional
    public Integer deletePermission(Long permissionId){
        return sysPermissionMapper.deleteSysPermission(permissionId);
    }


    /**
     * 查询用户资源列表
     * @param userId
     * @return
     */
    public List<String> queryPermissions(Long userId) {
        return sysPermissionMapper.queryPermissionsByUserid(userId);
    }
}
