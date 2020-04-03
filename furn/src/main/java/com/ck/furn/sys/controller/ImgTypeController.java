package com.ck.furn.sys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ck.furn.sys.bean.ImgType;
import com.ck.furn.sys.service.ImgTypeService;
import com.ck.furn.util.ConstValues;
import com.ck.furn.util.ReturnStatus;
import com.github.pagehelper.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/imgType")
@Api(value = "图片类型的操作类")
public class ImgTypeController {

	@Autowired
	private ImgTypeService imgTypeService;
	/**
	 * 查询图片类型
	 * @param ImgType
	 * @return
	 */
	@RequestMapping(value = "/findList", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "分页查询图片类型")
	public Map<String,Object>  findList(ImgType imgType){
		Page<ImgType> page = imgTypeService.findList(imgType);
    	Map<String, Object> map = new HashMap<String, Object>();
        map.put("pagedata", page); 
        map.put("total", page.getTotal()); 
        return map;
	}


	/**
	 * 查询图片类型
	 * @param ImgType
	 * @return
	 */
	@RequestMapping(value = "/findAllList", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "查询全部图片类型")
	public List<ImgType> findAllList(){
		List<ImgType> list = imgTypeService.findList(new ImgType());
		return list;
	}
	/**
	 * 添加新图片类型
	 * @param ImgType
	 * @return
	 */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
	@ApiOperation(value = "添加新图片类型")
	public ReturnStatus insert(ImgType imgType){
    	ReturnStatus ret = new ReturnStatus(ConstValues.FAILURE,"添加失败!");
		int row=imgTypeService.insert(imgType);
		if(row == 1) {
			ret.setReturnValue(ConstValues.SUCCESS);
			ret.setMessage("添加成功!");
		}
		return ret;	
	}
	
	/**
	 * 修改图片类型
	 * @param ImgType
	 * @return
	 */
    @RequestMapping(value = "/update", method=RequestMethod.POST)
    @ResponseBody
	@ApiOperation(value = "修改图片类型")
	public ReturnStatus update(ImgType imgType){
    	ReturnStatus ret = new ReturnStatus(ConstValues.FAILURE,"更新失败!");
		int row=imgTypeService.update(imgType);
		if(row == 1) {
			ret.setReturnValue(ConstValues.SUCCESS);
			ret.setMessage("更新成功!");
		}
		return ret;	
	}
	
	/**
	 * 删除图片类型
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/delete/{ids}", method=RequestMethod.DELETE)
    @ResponseBody
	@ApiOperation(value = "删除图片类型")
	public ReturnStatus delete(@PathVariable String ids){
		ReturnStatus ret = new ReturnStatus(ConstValues.FAILURE,"删除失败!");
		boolean boo=imgTypeService.delete(ids);
		if(boo) {
			ret.setReturnValue(ConstValues.SUCCESS);
			ret.setMessage("删除成功!");
		}
		return ret;	
	}
}
