package com.niiwoo.shield.manage.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.niiwoo.shield.manage.base.entity.Page;
import com.niiwoo.shield.manage.base.utils.StringUtil;
import com.niiwoo.shield.manage.base.utils.SysImageUtil;
import com.niiwoo.shield.manage.sys.dto.*;
import com.niiwoo.shield.manage.sys.enums.UserStatusEnum;
import com.niiwoo.shield.manage.sys.service.DepartmentDubboService;
import com.niiwoo.shield.manage.sys.service.RoleDubboService;
import com.niiwoo.shield.manage.sys.service.UserDubboService;
import com.niiwoo.shield.manage.sys.service.UserRoleDubboService;
import com.niiwoo.shield.manage.web.util.ErrorsDisposal;
import com.niiwoo.shield.manage.web.vo.*;
import com.niiwoo.tripod.consumer.component.FileUploadHandler;
import com.niiwoo.tripod.consumer.properties.FileUploadProperties;
import com.niiwoo.tripod.provider.exception.BizException;
import com.niiwoo.tripod.web.annotation.AuthIgnore;
import com.niiwoo.tripod.web.shiro.AuthCacheCleanHandler;
import com.niiwoo.tripod.web.vo.Empty;
import com.niiwoo.tripod.web.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;


/**
 * Created by dell on 2017/12/20.
 * Description：shield-parent
 */
@Slf4j
@RestController
@RequestMapping("/users")
@Api(tags = "用户管理", description = "/users", position = 1)
public class UserManageController extends BaseController{

    @Autowired
    private FileUploadHandler fileUploadHandler;
    @Reference(version = "1.0.0")
    private UserDubboService userDubboService;
    @Reference(version = "1.0.0")
    private UserRoleDubboService userRoleDubboService;
    @Reference(version = "1.0.0")
    private RoleDubboService roleDubboService;
    @Reference(version = "1.0.0")
    private DepartmentDubboService departmentDubboService;
    @Autowired
    private AuthCacheCleanHandler authCacheCleanHandler;


    /**
     * 头像上传
     * @param imageFile
     * @return
     * @throws IOException
     */
    private FileUploadProperties.FileUploadResult uploadImage(String imageFile){
        log.info("头像上传：" + imageFile);
        FileUploadProperties.FileUploadResult fileUploadResult = null;
        if(StringUtils.isNotEmpty(imageFile)) {
            imageFile = imageFile.substring(imageFile.indexOf(";base64,") + 8);
            byte[] imageBytes = Base64.decodeBase64(imageFile);

            String extName = null;
            try {
                //获取文件类型
                extName = SysImageUtil.getImageExtType(imageBytes);
            } catch (IOException e) {
                log.error("获取文件类型异常{}", e.getMessage(), e);
                throw new BizException("US_200002");
            }

            // 图片类型判断
            if (!fileUploadHandler.checkAttachmentExtNames(extName))
                throw new BizException("US_200002");

            //上传头像
            if (imageBytes != null) {
                fileUploadResult = fileUploadHandler.saveAttachment(imageBytes, extName);
                return fileUploadResult;
            }else{
                throw new BizException("US_200003");
            }
        }
        log.info("文件上传结果：" + JSON.toJSONString(fileUploadResult));
        return fileUploadResult;
    }


    @ApiOperation("上传用户头像")
    @PostMapping("/uploadHeadImage")
//    @RequiresPermissions({"user:uploadHeadImage"})
    public Empty uploadHeadImage(@RequestBody UploadImageVO requestVO){
        log.info("上传用户头像");
        String imageFile = requestVO.getImageFile();
        FileUploadProperties.FileUploadResult fileUploadResult = uploadImage(imageFile);

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(getCurrentUserId());
        userDTO.setImageFile(fileUploadResult.getImageUrl());
        userDTO.setCreatedUser(getCurrentUser().getPrincipal());
        userDTO.setUpdatedUser(getCurrentUser().getPrincipal());

        //更新用户信息
        log.info("更新用户信息：" + JSON.toJSONString(userDTO));
        userDubboService.updateUser(userDTO);
        return Empty.getInstance();
    }


    @ApiOperation("添加新用户")
    @PostMapping("/addUser")
    @RequiresPermissions({"user:add"})
    public Result addUser(@RequestBody UserVO requestVO){
        checkNewUserParam(requestVO);       //参数校验

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(requestVO, userDTO);

        //头像上传
        if (StringUtils.isNotEmpty(requestVO.getImageFile())){
            FileUploadProperties.FileUploadResult fileUploadResult = uploadImage(requestVO.getImageFile());
            userDTO.setImageFile(fileUploadResult.getImageUrl());
        }

        String salt = StringUtil.genRandomStr(4); //加密因子
        userDTO.setSalt(salt);
        userDTO.setPassword(getMd5HashPwd(userDTO.getPassword(),salt));
        userDTO.setCreatedUser(getCurrentUser().getPrincipal());
        userDTO.setUpdatedUser(getCurrentUser().getPrincipal());

        //创建新员工
        userDTO = userDubboService.addUser(userDTO);

        //用户角色授权
        try {
            Boolean enableFlag = UserStatusEnum.STOPED.getStatusCode().equals(userDTO.getStatus()) ? false : true;
            String principal = getCurrentUser().getPrincipal();
            userRoleDubboService.addUserRoles(userDTO.getUserId(), userDTO.getRoleIds(), principal, enableFlag);
        }catch(Exception e){
            log.error("用户角色授权失败{}", e.getMessage(), e);
            throw new BizException("US_200004");
        }
        return Result.with(userDTO);
    }


    /**
     * 新用户参数校验
     * @param requestVO
     * @return
     */
    public void checkNewUserParam(UserVO requestVO){
        Byte status = requestVO.getStatus();
        if(status==null || (!status.equals(UserStatusEnum.USED.getStatusCode())
                && !status.equals(UserStatusEnum.STOPED.getStatusCode())
                && !status.equals(UserStatusEnum.LOCKED.getStatusCode()))){
            throw new BizException("MS_100004", "用户状态值");
        }
        //校验用户名是否已存在
        UserDTO userDTO1 = userDubboService.queryUserByUserName(requestVO.getUserName());
        if(userDTO1!=null){
            throw new BizException("MS_100003", "用户名");
        }
        UserDTO userDTO2 = userDubboService.queryUserByUserCode(requestVO.getUserCode());
        if(userDTO2!=null){
            throw new BizException("MS_100003", "用户编号");
        }
        //校验部门ID
        DepartmentDTO department = departmentDubboService.findSysDepartmentById(requestVO.getDepartmentId());
        if(department == null){
            throw new BizException("MS_100002", "部门");
        }

        //校验角色ID
        checkRoleIds(requestVO.getRoleIds());
    }


    /**
     * 校验角色ID
     * @param roleIds
     * @return
     */
    public void checkRoleIds(Long[] roleIds){
        //角色校验
        if(roleIds==null || roleIds.length ==0) {
            throw new BizException("US_200006");
        }
        for (Long roleId : roleIds) {
            RoleDTO roleDTO = userRoleDubboService.findSysRoleById(roleId);
            if (roleDTO == null) {
                throw new BizException("US_200005");
            }
        }
    }


    @ApiOperation("编辑用户信息")
    @PostMapping("/editUser")
    @RequiresPermissions({"user:edit"})
    public Empty editUser(@RequestBody UserVO requestVO){
        checkUpdateUserParam(requestVO);    //参数校验

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(requestVO, userDTO);
        userDTO.setUpdatedUser(getCurrentUser().getPrincipal());

        //头像上传
        if (StringUtils.isNotEmpty(requestVO.getImageFile())){
            FileUploadProperties.FileUploadResult fileUploadResult = uploadImage(requestVO.getImageFile());
            userDTO.setImageFile(fileUploadResult.getImageUrl());
        } else {
            userDTO.setImageFile(null);
        }

        //检查用户是否存在
        UserDTO oldUser = userDubboService.queryUserByUserId(requestVO.getUserId());
        if(oldUser==null){
            throw new BizException("US_200007");
        }

        //若修改了用户编号，判断修改后的用户编号是否已存在
        String userCode = requestVO.getUserCode();
        if(!userCode.equals(oldUser.getUserCode())) {
            UserDTO tmpUser1 = userDubboService.queryUserByUserCode(userCode);
            if (tmpUser1 != null) {
                throw new BizException("US_200008");
            }
        }

        //更新员工信息
        userDTO.setPassword(null);  //密码置空
        userDubboService.updateUser(userDTO);
        try {
            //关联用户与角色
            Boolean newEnableFlag = UserStatusEnum.STOPED.getStatusCode().equals(requestVO.getStatus()) ? false : true;
            Boolean oldEnableFlag = UserStatusEnum.STOPED.getStatusCode().equals(oldUser.getStatus()) ? false : true;
            String principal = getCurrentUser().getPrincipal();
            userRoleDubboService.updateUserRoles(requestVO.getUserId(), requestVO.getRoleIds(), principal, oldEnableFlag, newEnableFlag);
            // 刷新用户资源权限
            authCacheCleanHandler.cleanAuthorizationCache(oldUser.getUserName());
        } catch (Exception e) {
            log.error("用户角色授权失败{}", e.getMessage(), e);
            throw new BizException("US_200009");
        }
        return Empty.getInstance();
    }


    /**
     * 更新用户参数校验
     * @param requestVO
     * @return
     */
    public void checkUpdateUserParam(UserVO requestVO){
        Byte status = requestVO.getStatus();
        if(status==null || (!status.equals(UserStatusEnum.USED.getStatusCode())
                && !status.equals(UserStatusEnum.STOPED.getStatusCode())
                && !status.equals(UserStatusEnum.LOCKED.getStatusCode()))){
            throw new BizException("MS_100004", "用户状态值");
        }

        //校验部门ID
        DepartmentDTO department = departmentDubboService.findSysDepartmentById(requestVO.getDepartmentId());
        if(department == null){
            throw new BizException("MS_100002", "部门");
        }

        checkRoleIds(requestVO.getRoleIds());
    }


    @ApiOperation("修改用户密码")
    @PostMapping("/updatePassword")
    @RequiresPermissions({"user:updatePassword"})
    public Empty updatePassword(@RequestBody @Valid UpdatePasswordVO requestVO, BindingResult resultErrors){
        //参数校验
        if(resultErrors.hasErrors()){
            String errorMsg = ErrorsDisposal.getErrorsMessage(resultErrors);
            throw new BizException("US_200010", errorMsg);
        }
        //参数校验
        checkUpadatePwdParam(requestVO);

        //更新用户密码
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(requestVO.getUserId());
        String salt = StringUtil.genRandomStr(4);   //加密因子
        userDTO.setSalt(salt);
        userDTO.setPassword(getMd5HashPwd(requestVO.getPassword(),salt));//密码
        String principal = getCurrentUser().getPrincipal();
        userDTO.setUpdatedUser(principal);

        userDubboService.resetPassword(userDTO);
        return Empty.getInstance();
    }


    /**
     * 校验用户ID
     * @param requestVO
     * @return
     */
    private void checkUpadatePwdParam(UpdatePasswordVO requestVO){
        UserDTO tmpUser = userDubboService.queryUserByUserId(requestVO.getUserId());
        if(tmpUser == null){
            throw new BizException("US_200007");
        }
        String oldPassword = requestVO.getOldPassword();
        if(StringUtils.isEmpty(oldPassword)){
            throw new BizException("US_200010", "旧密码");
        }
        if(oldPassword.equals(requestVO.getPassword())){
            throw new BizException("US_200011");
        }
        String oldPaswordTmp = getMd5HashPwd(oldPassword,tmpUser.getSalt());
        if(!oldPaswordTmp.equals(tmpUser.getPassword())){
            throw new BizException("US_200012");
        }
    }


    @ApiOperation("重置用户密码")
    @PostMapping("/resetPassword")
    @RequiresPermissions({"user:resetPassword"})
    public Empty resetPassword(@RequestBody @Valid UpdatePasswordVO requestVO, BindingResult resultErrors){
        //参数校验
        if(resultErrors.hasErrors()){
            String errorMsg = ErrorsDisposal.getErrorsMessage(resultErrors);
            throw new BizException("US_200010", errorMsg);
        }
        //用户ID校验
        UserDTO tmpUser = userDubboService.queryUserByUserId(requestVO.getUserId());
        if(tmpUser==null){
            throw new BizException("US_200007");
        }

        //更新用户密码
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(requestVO.getUserId());
        String salt = StringUtil.genRandomStr(4);   //加密因子
        userDTO.setSalt(salt);
        userDTO.setPassword(getMd5HashPwd(requestVO.getPassword(),salt));//密码
        String principal = getCurrentUser().getPrincipal();
        userDTO.setUpdatedUser(principal);

        userDubboService.resetPassword(userDTO);
        return Empty.getInstance();
    }


    @ApiOperation("用户解锁")
    @PostMapping("/unlock")
    @RequiresPermissions({"user:unlock"})
    public Empty unlockUser(@RequestBody UserRequestVO requestVO){
        userDubboService.unlockUser(requestVO.getUserId(), getCurrentUser().getPrincipal());
        return Empty.getInstance();
    }


    @ApiOperation("删除用户")
    @PostMapping("/dropUser")
    @RequiresPermissions({"user:drop"})
    public Empty dropUser(@RequestBody DropUserRequestVO requestVO){
        String principal = getCurrentUser().getPrincipal();
        try {
            //删除用户角色权限
            userRoleDubboService.clearUserRoles(requestVO.getUserIds(), principal, true, false);
        } catch (Exception e) {
            log.error("删除用户角色权限失败",e);
            throw new BizException("US_200013");
        }

        userDubboService.dropUser(requestVO.getUserIds(), principal);
        return Empty.getInstance();
    }


    @ApiOperation("根据ID根据查询用户信息")
    @PostMapping("/queryUserDetail")
    @RequiresPermissions({"user:queryDetail"})
    public Result queryUserDetail(@RequestBody UserRequestVO requestVO){
        Long userId = requestVO.getUserId();

        Map<String,Object> dataMap = new HashMap<String,Object>();
        try {
            //查询用户信息
            UserDTO userDTO = userDubboService.queryUserByUserId(userId);

            if(Objects.isNull(userDTO)){
                throw new BizException("US_200007");
            }

            //查询角色集合
            List<RoleDTO> roles = roleDubboService.queryRolesByUserId(userId);
            List<Long> roleIds = new ArrayList<Long>();
            List<String> roleNames = new ArrayList<>();
            for(RoleDTO roleDTO : roles){
                roleIds.add(roleDTO.getRoleId());
                roleNames.add(roleDTO.getRoleName());
            }

            //清空密码加密因子再返回
            userDTO.setSalt("");
            dataMap.put("user",userDTO);
            dataMap.put("roleIds",roleIds);
            dataMap.put("roleNames",roleNames);
            return Result.with(dataMap);
        }catch(Exception e){
            log.error("根据ID根据查询用户信息操作失败{}", e.getMessage(), e);
            throw new BizException("US_200014");
        }
    }


    @ApiOperation("分页查询用户列表，精准匹配")
    @PostMapping("/queryUserList")
    @RequiresPermissions({"user:query"})
    public Result queryUserList(@RequestBody QueryUsersRequestVO requestVO){
        try {
            QueryUserListDTO queryUserListDTO = new QueryUserListDTO();
            BeanUtils.copyProperties(requestVO, queryUserListDTO);

            //分页查询部门信息
            Page<UserDTO> page = userDubboService.queryUserList(queryUserListDTO);
            return Result.with(page);
        }catch(Exception e){
            log.error("分页查询用户列表，精准匹配操作失败{}", e.getMessage(), e);
            throw new BizException("US_200015");
        }
    }


    @ApiOperation("查询当前登录用户的菜单列表")
    @PostMapping("/queryMenuTree")
    //@RequiresPermissions({"user:queryMenus"})
    public Result queryMenusByUserId(){
        try {
            Long currentUserId = getCurrentUserId();
            //查询用户的菜单列表,树状结构
            List<RoleMenuDataDTO> menuList = userDubboService.queryMenusByUserId(currentUserId);

            Map<String,Object> dataMap = new HashMap<String,Object>();
            dataMap.put("list",menuList);
            return Result.with(dataMap);
        }catch(Exception e){
            log.error("查询当前登录用户的菜单列表操作失败{}", e.getMessage(), e);
            throw new BizException("US_200016");
        }
    }


    @ApiOperation("查询当前登录用户的按钮权限列表")
    @PostMapping("/queryButtons")
    //@RequiresPermissions({"user:queryButtons"})
    public Result queryButtonsByUserId(@RequestBody QueryButtonsRequestVO requestVO){
        //参数校验
        Long menuId = requestVO.getMenuId();
        if(menuId==null || menuId ==0){
            throw new BizException("US_200017");
        }

        try {
            //查询用户按钮权限列表
            Set<String> buttonList = userDubboService.getButtonsByUserId(getCurrentUserId(), menuId);

            Map<String,Object> dataMap = new HashMap<String,Object>();
            dataMap.put("list",buttonList);
            return Result.with(dataMap);
        }catch(Exception e){
            log.error("查询当前登录用户的按钮权限列表操作失败{}", e.getMessage(), e);
            throw new BizException("US_200018");
        }
    }
}
