package org.linlinjava.litemall.db.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.linlinjava.litemall.db.domain.Source;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseResp<T extends Object> implements Serializable {

    public static int SUCCESS = 0;
    public static int FAIlURE = -1;
    private int code;//状态码
    private T data;//数据
    private int displayStatus = 0;// 0 冒泡  1 弹框
    private String rtnInfo;
    private Map<String, Object> expandData = new HashMap<>();

    public BaseResp() {
    		this.code = Constant.STATUS_SYS_01;
    		this.rtnInfo = Constant.RTNINFO_SYS_01;
    };

    public int getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(int displayStatus) {
        this.displayStatus = displayStatus;
    }

    //成功直接返回 BaseResp.ok()
    public static BaseResp ok() {
        return new BaseResp(BaseResp.SUCCESS, "操作成功!");
    }

    //成功直接返回 BaseResp.ok(msg)
    public static BaseResp ok(String msg) {
        return new BaseResp(BaseResp.SUCCESS, msg);
    }

    //失败直接返回 BaseResp.fail()
    public static BaseResp fail() {
        return new BaseResp(BaseResp.FAIlURE, "服务器异常!");
    }

    //失败直接返回 BaseResp.fail(msg)
    public static BaseResp fail(String msg) {
        return new BaseResp(BaseResp.FAIlURE, msg);
    }
    
    public BaseResp(int code, String rtnInfo) {
        this.code = code;
        this.rtnInfo = rtnInfo;
    }
    
    public BaseResp(int code, String rtnInfo, T data){
    		this.code = code;
    		this.rtnInfo = rtnInfo;
    		this.data = data;
    }

    public BaseResp(T data) {
        this.code = SUCCESS;
        this.data = data;
    }
    @JsonInclude(Include.ALWAYS)
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    
    public BaseResp initCodeAndDesp(int code,String rtnInfo){
        this.code = code;
        this.rtnInfo = rtnInfo;
        return this;
    }
    public BaseResp initCodeAndDesp(){
        this.code = 	Constant.STATUS_SYS_00;
        this.rtnInfo = Constant.RTNINFO_SYS_00;
        return this;
    }

    public BaseResp initParamErrorDesp(){
        this.code = Constant.STATUS_SYS_02;
        this.rtnInfo = Constant.RTNINFO_SYS_02;
        return this;
    }

	public Map<String, Object> getExpandData() {
		return expandData;
	}

	public void setExpandData(Map<String, Object> expandData) {
		this.expandData = expandData;
	}

	public String getRtnInfo() {
		return rtnInfo;
	}

	public void setRtnInfo(String rtnInfo) {
		this.rtnInfo = rtnInfo;
	}

	@Override
	public String toString(){
       return JSONObject.fromObject(this).toString();
    }


    public Object okList(PageInfo page) {
        Map<String, Object> data = new HashMap<String, Object>();
//        if (pageList instanceof PageInfo) {
//            PageInfo page = (PageInfo) pageList;
            data.put("list", page.getList());
            data.put("total", page.getTotal());
            data.put("page", page.getPageNum());
            data.put("limit", page.getPageSize());
            data.put("pages", page.getPages());
//        } else {
//            data.put("total", pageList.size());
//            data.put("page", 1);
//            data.put("limit", pageList.size());
//            data.put("pages", 1);
//        }
        return ok(data);
    }

    public Object ok(Object data) {
        Map<String, Object> obj = new HashMap<String, Object>();
        obj.put("errno", 0);
        obj.put("errmsg", "成功");
        obj.put("data", data);
        return obj;
    }
}
