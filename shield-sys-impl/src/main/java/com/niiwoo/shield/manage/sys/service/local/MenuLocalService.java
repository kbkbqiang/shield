package com.niiwoo.shield.manage.sys.service.local;


import com.niiwoo.shield.manage.base.constants.ManageSysConstants;
import com.niiwoo.shield.manage.sys.dao.entity.SysMenu;
import com.niiwoo.shield.manage.sys.dao.mapper.SysMenuMapperExt;
import com.niiwoo.shield.manage.sys.dto.MenuDTO;
import com.niiwoo.tripod.provider.exception.BizException;
import com.niiwoo.tripod.service.component.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;


/**
 * @author  zhuhecheng
 * @date 2017-06-13
 */
@Slf4j
@Service
public class MenuLocalService {

    @Autowired
    private SysMenuMapperExt sysMenuMapperExt;

    @Autowired
    private RoleLocalService roleLocalService;

    @Autowired
    private RoleMenuLocalService roleMenuLocalService;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;


    @Transactional
    public void addMenu(MenuDTO dto) {
        //参数校验
        checkMenuName(dto.getMenuName());

        if(dto.getIsLeaf()==0){
            dto.setUrl("");
        }else{
            if(dto.getIsLeaf()==1 && StringUtils.isEmpty(dto.getUrl())) {
               throw  new BizException("MS_100001","叶子菜单的URL不能为空");
            }
            Integer urlCount = sysMenuMapperExt.selectCountByUrl(dto.getUrl());
            if(urlCount>0){
                throw  new BizException("MS_100003","栏目路径");
            }
        }
        //组装参数
        Long parentId = dto.getParentId();
        if(parentId==null || parentId ==0){
            dto.setLevel(new Byte("1"));
            dto.setParentId(Long.valueOf(0));  //parentId为空时，默认设为0
        }else{
            if(parentId != null && parentId != 0){
                //判断parentId有效性
                MenuDTO parentMenu = findSysMenuById(parentId);
                if(parentMenu==null) {
                    throw  new BizException("MS_100004","上级菜单ID");
                }else{
                    if(parentMenu.getLevel() > ManageSysConstants.MAX_MENU_LEVEL-1){
                        throw  new BizException("MS_100008",ManageSysConstants.MAX_MENU_LEVEL+"级菜单不能作为上级菜单");
                    }
                    if(parentMenu.getIsLeaf().equals(new Byte("1"))){
                        throw  new BizException("MS_100016");
                    }
                }

                dto.setLevel(new Byte(""+(parentMenu.getLevel().intValue()+1)));
            }
        }
        if(dto.getSort()==null){
            dto.setSort(1);  //菜单位置，默认为1
        }

        try {
            Long id = snowflakeIdWorker.nextId();
            dto.setMenuId(id);
            SysMenu menu = new SysMenu();
            BeanUtils.copyProperties(dto, menu);
            menu.setCreatedUser(dto.getCreatedUser());
            menu.setUpdatedUser(dto.getUpdatedUser());
            //创建新菜单
            Integer count = sysMenuMapperExt.insertSysMenu(menu);
            if (count == 0) {
                throw  new BizException("MS_100005");
            }
        }catch(Exception e){
            log.error("操作失败",e);
            throw  new BizException("MS_100005");
        }

    }



    /**
     * 菜单ID校验
     * @param menuId
     * @return
     */
    public void checkMenuId(Long menuId){
        if(menuId==null || menuId ==0){
            throw new BizException("MS_100004", "菜单ID");
        }

        //角色ID校验
        MenuDTO tmpMenu = findSysMenuById(menuId);
        if(tmpMenu == null){
            throw new BizException("MS_100002", "菜单");
        }
    }

    /**
     * 菜单参数校验
     * @param menuName  菜单名称
     * @return
     */
    public void checkMenuName(String menuName){
        if(StringUtils.isEmpty(menuName)){
            throw new BizException("MS_100001", "菜单名称");
        }
    }

    @Transactional
    public void updateMenu(MenuDTO dto) {
        Long menuId = dto.getMenuId();
        //菜单ID校验
        if(menuId==null || menuId ==0){
            throw new BizException("MS_100004", "菜单ID");
        }

        //角色ID校验
        MenuDTO tmpMenu = findSysMenuById(menuId);
        if(tmpMenu == null){
            throw new BizException("MS_100002", "菜单");
        }
        //菜单名称校验
        checkMenuName(dto.getMenuName());

        if(dto.getIsLeaf()==0){
            dto.setUrl("");
        }else{
            if(StringUtils.isEmpty(dto.getUrl())) {
                throw new BizException("MS_100001", "叶子菜单的URL");
            }

            if(!dto.getUrl().equals(tmpMenu.getUrl())) {
                Integer urlCount = sysMenuMapperExt.selectCountByUrl(dto.getUrl());
                if (urlCount > 0) {
                    throw new BizException("MS_100003", "栏目路径");
                }
            }
        }
        //组装参数
        Long parentId = dto.getParentId();
        if(parentId==null || parentId ==0){
            dto.setLevel(new Byte("1"));
            dto.setParentId(Long.valueOf(0));  //parentId为空时，默认设为0
        }else{
            if(parentId != null && parentId != 0){
                //判断parentId有效性
                MenuDTO parentMenu = findSysMenuById(parentId);
                if(parentMenu==null) {
                    throw new BizException("MS_100004", "上级菜单ID");
                }else{
                    if(parentMenu.getLevel() > ManageSysConstants.MAX_MENU_LEVEL-1){
                        throw new BizException("MS_100018", ManageSysConstants.MAX_MENU_LEVEL);
                    }
                    if(parentMenu.getIsLeaf().equals(new Byte("1"))){
                        throw  new BizException("MS_100016");
                    }
                }

                dto.setLevel(new Byte(""+(parentMenu.getLevel().intValue()+1)));
            }
        }



        //更新角色
        try {
            SysMenu menu = new SysMenu();
            BeanUtils.copyProperties(dto,menu);
            menu.setUpdatedUser(dto.getUpdatedUser());
            menu.setUpdatedTime(new Date());
            Integer count = sysMenuMapperExt.updateSysMenu(menu);
            if (count == 0) {
                throw new BizException("MS_100005");
            }


        }catch(Exception e){
            log.error("操作失败",e);
            throw new BizException("MS_100005");
        }
    }

    public MenuDTO findSysMenuById(Long menuId){
        return sysMenuMapperExt.findSysMenuById(menuId);
    }

    /**
     * 查询上级菜单列表-树状结构
     * @return
     */
    public List<MenuDTO> queryParentMenuDownList() {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("maxLevel", ManageSysConstants.MAX_MENU_LEVEL);
        //params.put("isLeaf", 0);  //父菜单
        List<MenuDTO> menuList = queryMenuTreeList(params);

        return menuList;
    }

    /**
     * 查询树状菜单列表
     * @return
     */
    public List<MenuDTO> queryMenuTreeList(Map<String,Object> params){
        //全量列表，顶级节点查询
        List<MenuDTO> menuList = sysMenuMapperExt.queryMenuTreeTopList(params);
        if(menuList==null || menuList.isEmpty()){
            return new ArrayList<MenuDTO>();
        }

        List<MenuDTO> resultList = new ArrayList<MenuDTO>();
        for(MenuDTO menu : menuList){
            params.put("menuId",menu.getMenuId());
            //子节点查询
            List<MenuDTO> childrenMenList = getChildrenMenuList(params);
            if(childrenMenList!=null && !childrenMenList.isEmpty()){
                menu.setChildren(childrenMenList);
            }
            resultList.add(menu);
        }

        return resultList;
    }

    /**
     * 下级菜单查询
     * @param params
     * @return
     */
    public List<MenuDTO> getChildrenMenuList(Map<String,Object> params){
        List<MenuDTO> resultList = new ArrayList<MenuDTO>();
        //子菜单列表，树结构
        List<MenuDTO> childrenMenuList = sysMenuMapperExt.queryMenuTreeChildrenList(params);
        if(childrenMenuList!=null && !childrenMenuList.isEmpty()){
            for(MenuDTO menu : childrenMenuList){
                //递归
                params.put("menuId",menu.getMenuId());
                List<MenuDTO> childList = getChildrenMenuList(params);
                menu.setChildren(childList);
                resultList.add(menu);
            }
        }

        return resultList;
    }

    public Set<Long> findChildrenMenusById(Long menuId){
        Set<Long> childMenuIds = new HashSet<Long>();
        Set<Long> menuIds = sysMenuMapperExt.findChildrenByMenuId(menuId);
        if(menuIds != null && !menuIds.isEmpty()){
            childMenuIds.addAll(menuIds);
            for(Long mid : menuIds){
                Set<Long> children = findChildrenMenusById(mid);
                childMenuIds.addAll(children);
            }
        }

        return childMenuIds;
    }

    @Transactional
    public void dropMenu(Long menuId){
        //参数校验
        if(menuId==null || menuId ==0){
            throw new BizException("MS_100004","菜单ID");
        }

        try {
            //删除菜单信息
            Set<Long> menuIds = findChildrenMenusById(menuId);
            menuIds.add(menuId);
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("menuIds",menuIds);
            //删除菜单
            sysMenuMapperExt.dropMenuByMenuIds(params);
            //删除角色与菜单关联关系
            roleMenuLocalService.dropRoleMenuByMenuIds(params);

        }catch(Exception e){
            log.error("操作失败",e);
            throw new BizException("MS_100005");

        }


    }
}
