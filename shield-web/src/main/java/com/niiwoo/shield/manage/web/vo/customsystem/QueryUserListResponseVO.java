package com.niiwoo.shield.manage.web.vo.customsystem;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 客服系统查询用户列表
 * @author TomXue
 * @since 2018-02-06
 */
@Getter
@Setter
@ApiModel(value = "查询用户列表结果列表")
public class QueryUserListResponseVO {
    @ApiModelProperty("注册时间")
    private Long registerDate;

    @ApiModelProperty("注册渠道")
    private String registerChannel;

    @ApiModelProperty("用户来源")
    private String userSource;

    @ApiModelProperty("是否借款人:0.否;1:是")
    private Integer IsBorrower;

    @ApiModelProperty("是否投资人:0.否;1:是")
    private Integer IsInvestor;

    @ApiModelProperty("帐户余额")
    private BigDecimal accountBalance;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("姓名")
    private String realName;

    @ApiModelProperty("性别")
    private Integer sex;

    @ApiModelProperty("手机号码")
    private String mobileNo;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("居住地址")
    private String Address;

    @ApiModelProperty("极速借授信反欺诈结果")
    private Integer speedLoanFraudLibra;

    @ApiModelProperty("最近登录时间")
    private String lastLoginDate;

    @ApiModelProperty("芝麻分")
    private String sesameScore;

    @ApiModelProperty("社保数据验证状态")
    private String socialSecurityVerifyState;

    @ApiModelProperty("待收金额")
    private String dueInAmount;

    @ApiModelProperty("首次充值时间")
    private String firstRechargeTime;

    @ApiModelProperty("首次充值金额")
    private String firstRechargeAmount;

    @ApiModelProperty("累计充值金额")
    private BigDecimal totalRechargeAmount;

    @ApiModelProperty("最近提现时间")
    private String lastWithdrawTime;

    @ApiModelProperty("最近提现金额")
    private BigDecimal lastWithdrawAmount;

    @ApiModelProperty("审核中的借款笔数")
    private Integer auditingLoanTimes;

    @ApiModelProperty("预发布的借款笔数")
    private Integer prePublishLoanTimes;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("极速借授信额度")
    private BigDecimal fastBorrowCreditAmount;

    @ApiModelProperty("学历")
    private String studentLeve;

    @ApiModelProperty("黑名单类型")
    private Integer listType;

    @ApiModelProperty("极速借借款次数")
    private Integer speedBorrowCount;

    @ApiModelProperty("用户引流渠道")
    private String drainageChannel;

    @ApiModelProperty("用户下载渠道")
    private String downloadChannel;

    @ApiModelProperty("借款反欺诈结果")
    private String loanAgainstFakeResult;

    @ApiModelProperty("用户在你我金融最近借款状态")
    private String lastestBorrowStatus;

    @ApiModelProperty("用户在你我金融最近借款类型")
    private String lastestBorrowType;

    @ApiModelProperty("最近一次借款申请时间")
    private String lastLoanApplyTime;

    @ApiModelProperty("最近一次借款申请金额")
    private String lastLoanApplyAmount;

    @ApiModelProperty("退回原因")
    private String sendBackReason;

    @ApiModelProperty("累计投资金额")
    private BigDecimal totalInvestAmount;

    @ApiModelProperty("最近一次投资时间")
    private String lastInvestTime;

    @ApiModelProperty("最近一次投资金额")
    private String lastInvestAmount;
}