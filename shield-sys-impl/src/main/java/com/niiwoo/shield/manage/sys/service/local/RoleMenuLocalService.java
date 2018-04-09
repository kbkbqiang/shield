package com.niiwoo.shield.manage.sys.service.local;

import com.niiwoo.shield.manage.sys.dao.mapper.SysRoleMenuMapperExt;
import com.niiwoo.shield.manage.sys.dto.RoleMenuDTO;
import com.niiwoo.shield.manage.sys.dto.RoleMenuDataDTO;
import com.niiwoo.tripod.service.component.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by luosiwen on 2017/12/19.
 */
@Service
public class RoleMenuLocalService {

    @Autowired
    private SysRoleMenuMapperExt sysRoleMenuMapper;
    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    //添加新的菜单权限
    @Transactional
    public void addRoleMenu(Long roleId,long menuId,String principal){
        RoleMenuDTO roleMenu = new RoleMenuDTO();
        Long id = snowflakeIdWorker.nextId();
        roleMenu.setId(id);
        roleMenu.setRoleId(roleId);
        roleMenu.setMenuId(menuId);
        roleMenu.setCreatedUser(principal);
        roleMenu.setUpdatedUser(principal);
        sysRoleMenuMapper.addRoleMenu(roleMenu);
    }

    /**
     * 删除角色的菜单权限
     * @param roleId
     * @param menuId
     */
    @Transactional
    public void deleteRoleMenu(Long roleId,Long menuId){
        RoleMenuDTO roleMenu = new RoleMenuDTO();
        roleMenu.setRoleId(roleId);
        roleMenu.setMenuId(menuId);
        sysRoleMenuMapper.deleteRoleMenu(roleMenu);
    }

    /**
     * 根据菜单列表删除角色的菜单权限
     * @param params
     */
    @Transactional
    public void dropRoleMenuByMenuIds(Map<String,Object> params){
        sysRoleMenuMapper.deleteRoleMenuByMenuIds(params);
    }

    /**
     * 根据角色ID批量删除菜单权限
     * @param roleId
     */
    @Transactional
    public void deleteRoleMenusByRoleId(Long roleId){
        sysRoleMenuMapper.deleteRoleMenusByRoleId(roleId);
    }

    /**
     * 根据角色ID查询菜单ID列表
     * @param roleId
     * @return
     */
    public List<Long> queryMenuIdsByRoleId(Long roleId){
        return sysRoleMenuMapper.queryMenuIdsByRoleId(roleId);
    }

    /**
     * 查询角色的菜单列表(树状结构)
     * @param roleId
     * @return
     */
    public List<RoleMenuDataDTO> queryMenuListByRoleId(Long roleId){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("roleId",roleId);

        //全量菜单列表，顶级节点查询
        List<RoleMenuDataDTO> menuList = sysRoleMenuMapper.queryMenuTreeTopList(params);
        if (menuList == null || menuList.isEmpty()) {
            return new ArrayList<RoleMenuDataDTO>();
        }

        List<RoleMenuDataDTO> resultList = new ArrayList<RoleMenuDataDTO>();
        for (RoleMenuDataDTO menu : menuList) {
            params.put("menuId", menu.getMenuId());
            //子节点查询
            List<RoleMenuDataDTO> childrenMenuList = getChildrenMenuList(params);
            if (childrenMenuList != null && !childrenMenuList.isEmpty()) {
                menu.setChildren(childrenMenuList);
            }
            resultList.add(menu);
        }

        return resultList;
    }

    /**
     * 查询角色的菜单列表(树状结构)-子菜单
     * @param params
     * @return
     */
    public List<RoleMenuDataDTO> getChildrenMenuList(Map<String,Object> params){
        List<RoleMenuDataDTO> resultList = new ArrayList<RoleMenuDataDTO>();
        //子菜单列表，树结构
        List<RoleMenuDataDTO> childrenMenuList = sysRoleMenuMapper.queryMenuTreeChildrenList(params);
        if(childrenMenuList!=null && !childrenMenuList.isEmpty()){
            for(RoleMenuDataDTO menu : childrenMenuList){
                //非叶子节点，递归
                if(menu.getParentId()!=null && menu.getParentId()!=0){
                    params.put("menuId",menu.getMenuId());
                    List<RoleMenuDataDTO> childList = getChildrenMenuList(params);
                    menu.setChildren(childList);
                }

                resultList.add(menu);
            }
        }

        return resultList;
    }


    /**
     * 根据角色集合查询有权限的菜单列表(树状结构)
     * @param roleIds
     * @return
     */
    public List<RoleMenuDataDTO> queryMenuListByRoles(Set<Long> roleIds){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("roleIds",roleIds);
        //全量菜单列表，顶级节点查询
        List<RoleMenuDataDTO> menuList = sysRoleMenuMapper.queryMenuTreeTopListByRoles(params);
        if(menuList==null || menuList.isEmpty()){
            return new ArrayList<RoleMenuDataDTO>();
        }

        List<RoleMenuDataDTO> resultList = new ArrayList<RoleMenuDataDTO>();
        for(RoleMenuDataDTO menu : menuList){
            params.put("menuId",menu.getMenuId());
            //子节点查询
            List<RoleMenuDataDTO> childrenMenuList = getChildrenMenuListByRoles(params);
            if(childrenMenuList!=null && !childrenMenuList.isEmpty()){
                menu.setChildren(childrenMenuList);
            }
            resultList.add(menu);
        }

        return resultList;
    }

    /**
     * 根据角色集合查询有权限的菜单列表(树状结构)-子菜单
     * @param params
     * @return
     */
    public List<RoleMenuDataDTO> getChildrenMenuListByRoles(Map<String,Object> params){
        List<RoleMenuDataDTO> resultList = new ArrayList<RoleMenuDataDTO>();
        //子菜单列表，树结构
        List<RoleMenuDataDTO> childrenMenuList = sysRoleMenuMapper.queryMenuTreeChildrenListByRoles(params);
        if(childrenMenuList!=null && !childrenMenuList.isEmpty()){
            for(RoleMenuDataDTO menu : childrenMenuList){
                //递归
                params.put("menuId",menu.getMenuId());
                List<RoleMenuDataDTO> childList = getChildrenMenuListByRoles(params);
                menu.setChildren(childList);
                resultList.add(menu);
            }
        }

        return resultList;
    }
}


