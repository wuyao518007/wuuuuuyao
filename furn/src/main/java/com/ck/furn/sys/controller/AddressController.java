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

import com.ck.furn.sys.bean.Address;
import com.ck.furn.sys.service.AddressService;
import com.ck.furn.util.ConstValues;
import com.ck.furn.util.DateUtils;
import com.ck.furn.util.ReturnStatus;
import com.github.pagehelper.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/address")
@Api(value = "收货地址操作类")
public class AddressController {
	@Autowired
	private AddressService addressService;

	/**
	 * 查询列表
	 * @param address
	 * @return
	 */
	@RequestMapping(value = "/findList", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "分页查询列表")
	public Map<String,Object>  findList(Address address) {
		Page<Address> page = addressService.findList(address);
    	Map<String, Object> map = new HashMap<String, Object>();
        map.put("pagedata", page); 
        map.put("total", page.getTotal());
        return map;
	}	
	/**
	 * 查询列表
	 * @param address
	 * @return
	 */
	@RequestMapping(value = "/findAllList", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "查询全部列表")
	public List<Address>  findAllList(Address address) {
		List<Address> page = addressService.findAllList(address);
        return page;
	}	

	 /**
     *  添加
     * @param address 信息
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
	@ApiOperation(value = "添加信息")
    public ReturnStatus insert(Address address){
    	ReturnStatus ret = new ReturnStatus(ConstValues.FAILURE,"添加失败!");  
    	try {
    		address.setCreateTime(DateUtils.getDateTime());
    		address.setUpdateTime(DateUtils.getDateTime());
    		addressService.insert(address);
		} catch (Exception e) {
			// TODO: handle exception
			return ret;	
		}
		ret.setReturnValue(ConstValues.SUCCESS);
		ret.setMessage("添加成功!");
		return ret;		
    }
  
    /**
     *  更新信息
     * @param address 信息
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	@ApiOperation(value = "更新信息")
    public ReturnStatus update(Address address){
    	ReturnStatus ret = new ReturnStatus(ConstValues.FAILURE,"更新失败!"); 
    	try {
    		address.setUpdateTime(DateUtils.getDateTime());
    		addressService.update(address);
		} catch (Exception e) {
			// TODO: handle exception
			return ret;	
		}
		ret.setReturnValue(ConstValues.SUCCESS);
		ret.setMessage("更新成功!");
		return ret;	
    	
    }
    

    /**
	 * 通过id删除信息
	 * @param address
	 */
	@RequestMapping(value = "/delete/{ids}", method=RequestMethod.DELETE)
    @ResponseBody
	@ApiOperation(value = "通过id删除信息")
	public ReturnStatus delete(@PathVariable String ids){
		ReturnStatus ret = new ReturnStatus(ConstValues.FAILURE,"删除失败!");
		try {
			addressService.delete(ids);
		} catch (Exception e) {
			// TODO: handle exception
			return ret;	
		}
		ret.setReturnValue(ConstValues.SUCCESS);
		ret.setMessage("删除成功!");
		return ret;	
	}
}
