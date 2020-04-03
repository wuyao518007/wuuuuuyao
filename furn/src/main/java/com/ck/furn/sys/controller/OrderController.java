package com.ck.furn.sys.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ck.furn.sys.bean.GoodLike;
import com.ck.furn.sys.bean.Img;
import com.ck.furn.sys.service.OrderService;
import com.ck.furn.util.ConstValues;
import com.ck.furn.util.DateUtils;
import com.ck.furn.util.ReturnStatus;
import com.github.pagehelper.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/order")
@Api(value = "定制的操作类")
public class OrderController {
	@Autowired
	private OrderService commentService;
	/**
	 * 查询
	 * @param GoodLike
	 * @return
	 */
	@RequestMapping(value = "/findList", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "根据图片id查询定制信息")
    public Map<String, Object> findList(GoodLike goodLike){
		Page<GoodLike> page = commentService.findAllList(goodLike);
    	Map<String, Object> map = new HashMap<String, Object>();
        map.put("pagedata", page); 
        map.put("total", page.getTotal()); 
        return map;
	}	
	
	/**
	 * 统计查询
	 * @param GoodLike
	 * @return
	 */
	@RequestMapping(value = "/findOrderList", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "统计查询")
    public Map<String, Object> findOrderList(Img goodLike){
		Page<Img> page = commentService.findOrderList(goodLike);
    	Map<String, Object> map = new HashMap<String, Object>();
        map.put("pagedata", page); 
        map.put("total", page.getTotal()); 
        return map;
	}
	/**
	 * 添加新
	 * @param GoodLike
	 * @return
	 */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
	@ApiOperation(value = "提交定制")
	public ReturnStatus insert(GoodLike goodLike){
    	ReturnStatus ret = new ReturnStatus(ConstValues.FAILURE,"提交失败!");
    	goodLike.setCreateTime(DateUtils.getDateTime());
    	goodLike.setUpdateTime(DateUtils.getDateTime());
		int row=commentService.insert(goodLike);
		if(row == 1) {
			ret.setReturnValue(ConstValues.SUCCESS);
			ret.setMessage("提交成功!");
		}
		return ret;	
	}
    /**
	 * 修改
	 * @param GoodLike
	 * @return
	 */
    @RequestMapping(value = "/updateState", method = RequestMethod.POST)
    @ResponseBody
	@ApiOperation(value = "修改定制")
	public ReturnStatus updateState(GoodLike goodLike) {
		ReturnStatus ret = new ReturnStatus(ConstValues.FAILURE,"修改失败!");
    	goodLike.setCreateTime(DateUtils.getDateTime());
    	goodLike.setUpdateTime(DateUtils.getDateTime());
		int row=commentService.updateState(goodLike);
		if(row == 1) {
			ret.setReturnValue(ConstValues.SUCCESS);
			ret.setMessage("修改成功!");
		}
		return ret;	
	}
	/**
	 * 付款
	 * @param ids
	 * @return
	 */	
	  @RequestMapping(value = "/update/{ids}", method=RequestMethod.DELETE)  
	  @ResponseBody	  
	  @ApiOperation(value = "付款定制") 
	  public ReturnStatus update(@PathVariable String ids){
		  ReturnStatus ret = new ReturnStatus(ConstValues.FAILURE,"删除失败!"); 
	  	  try {
			  commentService.update(ids); 
			  ret.setReturnValue(ConstValues.SUCCESS); 
			  ret.setMessage("删除成功!");
			} catch (Exception e) {
				// TODO: handle exception
			}
		  return ret; 
	  }
	/**
	 * 删除
	 * @param ids
	 * @return
	 */	
	  @RequestMapping(value = "/delete/{ids}", method=RequestMethod.DELETE)  
	  @ResponseBody	  
	  @ApiOperation(value = "删除定制") 
	  public ReturnStatus delete(@PathVariable String ids){
		  ReturnStatus ret = new ReturnStatus(ConstValues.FAILURE,"删除失败!"); 
	  	  try {
			  commentService.delete(ids); 
			  ret.setReturnValue(ConstValues.SUCCESS); 
			  ret.setMessage("删除成功!");
			} catch (Exception e) {
				// TODO: handle exception
			}
		  return ret; 
	  }
	 
}
