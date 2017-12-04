package com.nonobank.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Repository;
@Repository
public class Order {
    private String id;

    private String userId;

    private String userName;

    private String mobile;

    private String userMerchantId;

    private String applyDate;

    private String applyTime;

    private String workDate;

    private Long productId;

    private String productType;

    private String productName;

    private Long refProductId;

    private String refProductName;

    private String cardIdxNo;

    private Long amount;

    private String currency;

    private Long quty;

    private Integer qutyScale;

    private Long confirmQuty;

    private Integer confirmQutyScale;

    private Long confirmAmount;

    private String confirmCurrency;

    private String nav;

    private String settleType;

    private String feeType;

    private Long fee;

    private String refFeeType;

    private Long refFee;

    private BigDecimal discount;

    private String outBizNo;

    private String melonType;

    private String merchantId;

    private String transCode;

    private String subTransCode;

    private String source;

    private String assetLastConfirm;

    private String flag;

    private String status;

    private String note;

    private Date createTime;

    private Date modifyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getUserMerchantId() {
        return userMerchantId;
    }

    public void setUserMerchantId(String userMerchantId) {
        this.userMerchantId = userMerchantId == null ? null : userMerchantId.trim();
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate == null ? null : applyDate.trim();
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime == null ? null : applyTime.trim();
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate == null ? null : workDate.trim();
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType == null ? null : productType.trim();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public Long getRefProductId() {
        return refProductId;
    }

    public void setRefProductId(Long refProductId) {
        this.refProductId = refProductId;
    }

    public String getRefProductName() {
        return refProductName;
    }

    public void setRefProductName(String refProductName) {
        this.refProductName = refProductName == null ? null : refProductName.trim();
    }

    public String getCardIdxNo() {
        return cardIdxNo;
    }

    public void setCardIdxNo(String cardIdxNo) {
        this.cardIdxNo = cardIdxNo == null ? null : cardIdxNo.trim();
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    public Long getQuty() {
        return quty;
    }

    public void setQuty(Long quty) {
        this.quty = quty;
    }

    public Integer getQutyScale() {
        return qutyScale;
    }

    public void setQutyScale(Integer qutyScale) {
        this.qutyScale = qutyScale;
    }

    public Long getConfirmQuty() {
        return confirmQuty;
    }

    public void setConfirmQuty(Long confirmQuty) {
        this.confirmQuty = confirmQuty;
    }

    public Integer getConfirmQutyScale() {
        return confirmQutyScale;
    }

    public void setConfirmQutyScale(Integer confirmQutyScale) {
        this.confirmQutyScale = confirmQutyScale;
    }

    public Long getConfirmAmount() {
        return confirmAmount;
    }

    public void setConfirmAmount(Long confirmAmount) {
        this.confirmAmount = confirmAmount;
    }

    public String getConfirmCurrency() {
        return confirmCurrency;
    }

    public void setConfirmCurrency(String confirmCurrency) {
        this.confirmCurrency = confirmCurrency == null ? null : confirmCurrency.trim();
    }

    public String getNav() {
        return nav;
    }

    public void setNav(String nav) {
        this.nav = nav == null ? null : nav.trim();
    }

    public String getSettleType() {
        return settleType;
    }

    public void setSettleType(String settleType) {
        this.settleType = settleType == null ? null : settleType.trim();
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType == null ? null : feeType.trim();
    }

    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public String getRefFeeType() {
        return refFeeType;
    }

    public void setRefFeeType(String refFeeType) {
        this.refFeeType = refFeeType == null ? null : refFeeType.trim();
    }

    public Long getRefFee() {
        return refFee;
    }

    public void setRefFee(Long refFee) {
        this.refFee = refFee;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getOutBizNo() {
        return outBizNo;
    }

    public void setOutBizNo(String outBizNo) {
        this.outBizNo = outBizNo == null ? null : outBizNo.trim();
    }

    public String getMelonType() {
        return melonType;
    }

    public void setMelonType(String melonType) {
        this.melonType = melonType == null ? null : melonType.trim();
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId == null ? null : merchantId.trim();
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode == null ? null : transCode.trim();
    }

    public String getSubTransCode() {
        return subTransCode;
    }

    public void setSubTransCode(String subTransCode) {
        this.subTransCode = subTransCode == null ? null : subTransCode.trim();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public String getAssetLastConfirm() {
        return assetLastConfirm;
    }

    public void setAssetLastConfirm(String assetLastConfirm) {
        this.assetLastConfirm = assetLastConfirm == null ? null : assetLastConfirm.trim();
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag == null ? null : flag.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}