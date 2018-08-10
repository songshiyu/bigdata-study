package com.bigdata.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;
import java.util.List;


/**
 * 案例 ES 实体对象
 *
 * @date 2018/1/22 16:06
 */
@Document(indexName = "test", type = "test", shards = 5, replicas = 1)
public class PPTTestEntity implements Serializable {
    @Id
    private Long id;
    /**
     * 用户id
     */
    @Field(index = FieldIndex.not_analyzed, store = true, type = FieldType.Long)
    private Long userId;
    /**
     * 用户名
     */
    @Field(index = FieldIndex.not_analyzed, store = true, type = FieldType.String)
    private String userName;
    /**
     * 是否置顶
     */
    @Field(index = FieldIndex.not_analyzed, store = true, type = FieldType.String)
    private String isTop;
    /**
     * 数据来源
     */
    @Field(index = FieldIndex.analyzed, analyzer = "ik", searchAnalyzer = "ik", store = true, type = FieldType.String)
    private String dataSource;

    /**
     * 关联行业
     */
    @Field(index = FieldIndex.not_analyzed, store = true, type = FieldType.String)
    private String relatedIndustries;

    @Field(index = FieldIndex.not_analyzed, store = true, type = FieldType.String)
    private String relatedPeople;

    /**
     * 标题
     */
    @Field(index = FieldIndex.analyzed, analyzer = "ik", searchAnalyzer = "ik", store = true, type = FieldType.String)
    private String titleName;
    /**
     * 标签
     */
    @Field(index = FieldIndex.analyzed, analyzer = "ik", searchAnalyzer = "ik", store = true, type = FieldType.String)
    private String titleLableName;

    /**
     * 摘要
     */
    @Field(index = FieldIndex.analyzed, store = true, analyzer = "ik", searchAnalyzer = "ik", type = FieldType.String)
    private String summaryName;

    /**
     * 内容
     */
    @Field(index = FieldIndex.analyzed, analyzer = "ik", searchAnalyzer = "ik",store = true, type = FieldType.String)
    private String contentName;

    /**
     * 报告时间
     */
    @Field(index = FieldIndex.not_analyzed, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss", store = true, type = FieldType.Date)
    private String reportTime;

    /**
     * 创建时间
     */
    @Field(index = FieldIndex.not_analyzed, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss", store = true)
    private String createTime;

    /**
     * 图片地址
     */
    @Field(index = FieldIndex.not_analyzed, store = true, type = FieldType.String)
    private String uploadImgsId;
    /**
     * 附件地址
     */
    @Field(index = FieldIndex.not_analyzed, store = true, type = FieldType.String)
    private String uploadFilesId;
    private List uploadImgsRoot;
    private List uploadFilesRoot;

    public PPTTestEntity() {
    }

    public PPTTestEntity(Long id, Long userId, String userName, String isTop, String dataSource,
                         String relatedIndustries, String relatedPeople, String titleName, String titleLableName,
                         String summaryName, String contentName, String reportTime, String createTime,
                         String uploadImgsId, String uploadFilesId, List uploadImgsRoot, List uploadFilesRoot) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.isTop = isTop;
        this.dataSource = dataSource;
        this.relatedIndustries = relatedIndustries;
        this.relatedPeople = relatedPeople;
        this.titleName = titleName;
        this.titleLableName = titleLableName;
        this.summaryName = summaryName;
        this.contentName = contentName;
        this.reportTime = reportTime;
        this.createTime = createTime;
        this.uploadImgsId = uploadImgsId;
        this.uploadFilesId = uploadFilesId;
        this.uploadImgsRoot = uploadImgsRoot;
        this.uploadFilesRoot = uploadFilesRoot;
    }
    public PPTTestEntity(Long id, Long userId, String userName, String isTop, String dataSource, String relatedIndustries, String relatedPeople, String titleName, String titleLableName, String summaryName, String contentName, String reportTime, String createTime, String uploadImgsId, String uploadFilesId) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.isTop = isTop;
        this.dataSource = dataSource;
        this.relatedIndustries = relatedIndustries;
        this.relatedPeople = relatedPeople;
        this.titleName = titleName;
        this.titleLableName = titleLableName;
        this.summaryName = summaryName;
        this.contentName = contentName;
        this.reportTime = reportTime;
        this.createTime = createTime;
        this.uploadImgsId = uploadImgsId;
        this.uploadFilesId = uploadFilesId;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getRelatedIndustries() {
        return relatedIndustries;
    }

    public void setRelatedIndustries(String relatedIndustries) {
        this.relatedIndustries = relatedIndustries;
    }

    public String getRelatedPeople() {
        return relatedPeople;
    }

    public void setRelatedPeople(String relatedPeople) {
        this.relatedPeople = relatedPeople;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getTitleLableName() {
        return titleLableName;
    }

    public void setTitleLableName(String titleLableName) {
        this.titleLableName = titleLableName;
    }

    public String getSummaryName() {
        return summaryName;
    }

    public void setSummaryName(String summaryName) {
        this.summaryName = summaryName;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUploadImgsId() {
        return uploadImgsId;
    }

    public void setUploadImgsId(String uploadImgsId) {
        this.uploadImgsId = uploadImgsId;
    }

    public String getUploadFilesId() {
        return uploadFilesId;
    }

    public void setUploadFilesId(String uploadFilesId) {
        this.uploadFilesId = uploadFilesId;
    }

    public List getUploadImgsRoot() {
        return uploadImgsRoot;
    }

    public void setUploadImgsRoot(List uploadImgsRoot) {
        this.uploadImgsRoot = uploadImgsRoot;
    }

    public List getUploadFilesRoot() {
        return uploadFilesRoot;
    }

    public void setUploadFilesRoot(List uploadFilesRoot) {
        this.uploadFilesRoot = uploadFilesRoot;
    }
}