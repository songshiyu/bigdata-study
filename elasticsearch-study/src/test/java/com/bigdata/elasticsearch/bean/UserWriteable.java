package com.bigdata.elasticsearch.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;
import java.util.Date;


/**
 * Created by songshiyu on 2018/7/17.
 */
@Document(indexName = "songshiyu",type = "songshiyu", shards = 5, replicas = 1)
public class UserWriteable implements Serializable{

        @Id
        @Field(index = FieldIndex.not_analyzed,type = FieldType.String)
        private String userid;

        @Field(type = FieldType.Integer)
        private Integer agerange;

        @Field(type = FieldType.String)
        private String birthday;

        @Field(type = FieldType.Integer)
        private int cntFollowers;

        @Field(type = FieldType.Integer)
        private int cntFriends;

        @Field(type = FieldType.Integer)
        private int cntPosts;

        @Field(index = FieldIndex.analyzed,analyzer = "ik",type = FieldType.String)
        private String gender;

        @Field(type = FieldType.String)
        private String modifiedtime;

        @Field(type = FieldType.Integer)
        private int levelcode;

        @Field(type = FieldType.Long)
        private Long loccountry;

        @Field(type = FieldType.Long)
        private Long locProvince;

        @Field(type = FieldType.Long)
        private Long locCity;

        @Field(index = FieldIndex.analyzed,analyzer = "ik",type = FieldType.String)
        private String location;

        @Field(index = FieldIndex.analyzed,analyzer = "ik",type = FieldType.String)
        private String screenName;

        @Field(index = FieldIndex.not_analyzed,type = FieldType.String)
        private String profileImageUrl;

        @Field(type = FieldType.String)
        private String profileurl;

        @Field(type = FieldType.String)
        private String regDate;

        @Field(type = FieldType.String)
        private String tagset;

        @Field(type = FieldType.String)
        private String weibotags;

        @Field(type = FieldType.String)
        private String friends;


        @Field(index = FieldIndex.not_analyzed,type = FieldType.String)
        private String userUrn;

        @Field(type = FieldType.Long)
        private Long entitySectionUrn;

        @Field(type = FieldType.String)
        private String cntEssence;

        @Field(index = FieldIndex.not_analyzed,type = FieldType.String)
        private String infoWork;

        @Field(index = FieldIndex.no,type = FieldType.Long)
        private Long suppliers;

        @Field(type = FieldType.Integer)
        private int cntLikes;

        @Field(type = FieldType.Long)
        private Long serviceTypeURN;

        @Field(type = FieldType.Double)
        private Double liveness;

        @Field(type = FieldType.Integer)
        private int searchFlag;

        @Field(index = FieldIndex.analyzed,analyzer = "ik",type = FieldType.String)
        private String verifiedStr;

        @Field(type = FieldType.Integer)
        private int cntRead;

        @Field(type = FieldType.Integer)
        private int focusCars;

        @Field(index = FieldIndex.not_analyzed,type = FieldType.String)
        private String infoEdu;

        @Field(index = FieldIndex.not_analyzed,type = FieldType.String)
        private String billTypes;

        @Field(index = FieldIndex.analyzed,analyzer = "ik",type = FieldType.String)
        private String txtDescription;

        @Field(type = FieldType.Integer)
        private int accountType;

        @Field(type = FieldType.Double)
        private Double health;

        @Field(type = FieldType.Integer)
        private int successTrades;

        @Field(type = FieldType.Double)
        private Double idxActiveness;

        @Field(type = FieldType.Double)
        private  Double bwi;

        @Field(format = DateFormat.date_optional_time,type = FieldType.Date)
        private Date dwCreatedAt;

        @Field(type = FieldType.Integer)
        private int items;




    public Long getEntitySectionUrn() {
        return entitySectionUrn;
    }

    public void setEntitySectionUrn(Long entitySectionUrn) {
        this.entitySectionUrn = entitySectionUrn;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Integer getAgerange() {
        return agerange;
    }

    public void setAgerange(Integer agerange) {
        this.agerange = agerange;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getCntFollowers() {
        return cntFollowers;
    }

    public void setCntFollowers(int cntFollowers) {
        this.cntFollowers = cntFollowers;
    }

    public int getCntFriends() {
        return cntFriends;
    }

    public void setCntFriends(int cntFriends) {
        this.cntFriends = cntFriends;
    }

    public int getCntPosts() {
        return cntPosts;
    }

    public void setCntPosts(int cntPosts) {
        this.cntPosts = cntPosts;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getModifiedtime() {
        return modifiedtime;
    }

    public void setModifiedtime(String modifiedtime) {
        this.modifiedtime = modifiedtime;
    }

    public int getLevelcode() {
        return levelcode;
    }

    public void setLevelcode(int levelcode) {
        this.levelcode = levelcode;
    }

    public Long getLoccountry() {
        return loccountry;
    }

    public void setLoccountry(Long loccountry) {
        this.loccountry = loccountry;
    }

    public Long getLocProvince() {
        return locProvince;
    }

    public void setLocProvince(Long locProvince) {
        this.locProvince = locProvince;
    }

    public Long getLocCity() {
        return locCity;
    }

    public void setLocCity(Long locCity) {
        this.locCity = locCity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getProfileurl() {
        return profileurl;
    }

    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getTagset() {
        return tagset;
    }

    public void setTagset(String tagset) {
        this.tagset = tagset;
    }

    public String getWeibotags() {
        return weibotags;
    }

    public void setWeibotags(String weibotags) {
        this.weibotags = weibotags;
    }

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }

    public String getUserUrn() {
        return userUrn;
    }

    public void setUserUrn(String userUrn) {
        this.userUrn = userUrn;
    }

    public String getCntEssence() {
        return cntEssence;
    }

    public void setCntEssence(String cntEssence) {
        this.cntEssence = cntEssence;
    }

    public String getInfoWork() {
        return infoWork;
    }

    public void setInfoWork(String infoWork) {
        this.infoWork = infoWork;
    }

    public Long getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(Long suppliers) {
        this.suppliers = suppliers;
    }

    public int getCntLikes() {
        return cntLikes;
    }

    public void setCntLikes(int cntLikes) {
        this.cntLikes = cntLikes;
    }

    public Long getServiceTypeURN() {
        return serviceTypeURN;
    }

    public void setServiceTypeURN(Long serviceTypeURN) {
        this.serviceTypeURN = serviceTypeURN;
    }

    public Double getLiveness() {
        return liveness;
    }

    public void setLiveness(Double liveness) {
        this.liveness = liveness;
    }

    public int getSearchFlag() {
        return searchFlag;
    }

    public void setSearchFlag(int searchFlag) {
        this.searchFlag = searchFlag;
    }

    public String getVerifiedStr() {
        return verifiedStr;
    }

    public void setVerifiedStr(String verifiedStr) {
        this.verifiedStr = verifiedStr;
    }

    public int getCntRead() {
        return cntRead;
    }

    public void setCntRead(int cntRead) {
        this.cntRead = cntRead;
    }

    public int getFocusCars() {
        return focusCars;
    }

    public void setFocusCars(int focusCars) {
        this.focusCars = focusCars;
    }

    public String getInfoEdu() {
        return infoEdu;
    }

    public void setInfoEdu(String infoEdu) {
        this.infoEdu = infoEdu;
    }

    public String getBillTypes() {
        return billTypes;
    }

    public void setBillTypes(String billTypes) {
        this.billTypes = billTypes;
    }

    public String getTxtDescription() {
        return txtDescription;
    }

    public void setTxtDescription(String txtDescription) {
        this.txtDescription = txtDescription;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public Double getHealth() {
        return health;
    }

    public void setHealth(Double health) {
        this.health = health;
    }

    public int getSuccessTrades() {
        return successTrades;
    }

    public void setSuccessTrades(int successTrades) {
        this.successTrades = successTrades;
    }

    public Double getIdxActiveness() {
        return idxActiveness;
    }

    public void setIdxActiveness(Double idxActiveness) {
        this.idxActiveness = idxActiveness;
    }

    public Double getBwi() {
        return bwi;
    }

    public void setBwi(Double bwi) {
        this.bwi = bwi;
    }

    public Date getDwCreatedAt() {
        return dwCreatedAt;
    }

    public void setDwCreatedAt(Date dwCreatedAt) {
        this.dwCreatedAt = dwCreatedAt;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "mapWriteable{" +
                "userid=" + userid +
                ", agerange=" + agerange +
                ", birthday=" + birthday +
                ", cntFollowers=" + cntFollowers +
                ", cntFriends=" + cntFriends +
                ", cntPosts=" + cntPosts +
                ", gender='" + gender + '\'' +
                ", modifiedtime='" + modifiedtime + '\'' +
                ", levelcode=" + levelcode +
                ", loccountry=" + loccountry +
                ", locProvince=" + locProvince +
                ", locCity=" + locCity +
                ", location='" + location + '\'' +
                ", screenName='" + screenName + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", profileurl='" + profileurl + '\'' +
                ", regDate=" + regDate +
                ", tagset='" + tagset + '\'' +
                ", weibotags='" + weibotags + '\'' +
                ", friends='" + friends + '\'' +
                ", userUrn='" + userUrn + '\'' +
                ", entitySectionUrn=" + entitySectionUrn +
                '}';
    }
}
