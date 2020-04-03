package com.ck.furn.sys.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ck.furn.sys.bean.GoodLike;
import com.ck.furn.sys.service.CommentService;
import com.ck.furn.util.ConstValues;
import com.ck.furn.util.DateUtils;
import com.ck.furn.util.ReturnStatus;
import com.github.pagehelper.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/comment")
@Api(value = "评论的操作类")
public class CommentController {
	@Autowired
	private CommentService commentService;
	/**
	 * 查询
	 * @param GoodLike
	 * @return
	 */
	@RequestMapping(value = "/findList", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "根据图片id查询评论信息")
    public Map<String, Object> findList(GoodLike goodLike){
		Page<GoodLike> page = commentService.findAllList(goodLike);
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
	@ApiOperation(value = "提交评论")
	public ReturnStatus insert(GoodLike goodLike){
    	ReturnStatus ret = new ReturnStatus(ConstValues.FAILURE,"提交失败!");
    	goodLike.setCreateTime(DateUtils.getDateTime());
		int row=commentService.insert(goodLike);
		if(row == 1) {
			ret.setReturnValue(ConstValues.SUCCESS);
			ret.setMessage("提交成功!");
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
	  @ApiOperation(value = "删除评论") 
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
