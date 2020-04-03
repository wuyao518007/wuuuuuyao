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

import com.ck.furn.sys.bean.Role;
import com.ck.furn.sys.service.RoleService;
import com.ck.furn.util.ConstValues;
import com.ck.furn.util.ReturnStatus;
import com.github.pagehelper.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/role")
@Api(value = "角色类型的操作类")
public class RoleController {
	@Autowired
	private RoleService roleService;

	/**
	 * 查询列表
	 * @param Role
	 * @return
	 */
	@RequestMapping(value = "/findList", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "分页查询角色列表")
	public Map<String,Object>  findList(Role role) {
		Page<Role> page = roleService.findList(role);
    	Map<String, Object> map = new HashMap<String, Object>();
        map.put("pagedata", page); 
        map.put("total", page.getTotal());
        return map;
	}	
	/**
	 * 查询列表
	 * @param Role
	 * @return
	 */
	@RequestMapping(value = "/findAllList", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "查询全部角色列表")
	public List<Role>  findAllList(Role role) {
		List<Role> page = roleService.findAllList(role);
        return page;
	}	

	 /**
     *  添加
     * @param Role 信息
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
	@ApiOperation(value = "添加角色信息")
    public ReturnStatus insert(Role role){
    	ReturnStatus ret = new ReturnStatus(ConstValues.FAILURE,"添加失败!");  
    	try {
    		roleService.insert(role);
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
     * @param Role 信息
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	@ApiOperation(value = "更新角色信息")
    public ReturnStatus update(Role role){
    	ReturnStatus ret = new ReturnStatus(ConstValues.FAILURE,"更新失败!"); 
    	try {
    		roleService.update(role);
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
	 * @param Role
	 */
	@RequestMapping(value = "/delete/{ids}", method=RequestMethod.DELETE)
    @ResponseBody
	@ApiOperation(value = "通过id删除角色信息")
	public ReturnStatus delete(@PathVariable String ids){
		ReturnStatus ret = new ReturnStatus(ConstValues.FAILURE,"删除失败!");
		try {
			roleService.delete(ids);
		} catch (Exception e) {
			// TODO: handle exception
			return ret;	
		}
		ret.setReturnValue(ConstValues.SUCCESS);
		ret.setMessage("删除成功!");
		return ret;	
	}
}
