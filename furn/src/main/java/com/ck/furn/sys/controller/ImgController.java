package com.ck.furn.sys.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ck.furn.sys.bean.Img;
import com.ck.furn.sys.bean.ImgList;
import com.ck.furn.sys.service.ImgListService;
import com.ck.furn.sys.service.ImgService;
import com.ck.furn.util.ConstValues;
import com.ck.furn.util.IdGen;
import com.ck.furn.util.ReturnStatus;
import com.github.pagehelper.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/img")
@Api(value = "家具的操作类")
public class ImgController {

	@Autowired
	private ImgService imgService;
	@Autowired
	private ImgListService imgListService;
	/**
	 * 查询家具
	 * @param Img
	 * @return
	 */
	@RequestMapping(value = "/findList", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "分页查询家具")
	public Map<String,Object>  findList(Img img){
		Page<Img> page = imgService.findList(img);
    	Map<String, Object> map = new HashMap<String, Object>();
        map.put("pagedata", page); 
        map.put("total", page.getTotal()); 
        return map;
	}
	
	/**
	 * 查询家具
	 * @param Img
	 * @return
	 */
	@RequestMapping(value = "/findUserList", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "用户分页查询家具")
	public Map<String,Object>  findUserList(Img img){
		Page<Img> page = imgService.findUserList(img);
    	Map<String, Object> map = new HashMap<String, Object>();
        map.put("pagedata", page); 
        map.put("total", page.getTotal()); 
        return map;
	}
	/**
	 * 查询家具
	 * @param Img
	 * @return
	 */
	@RequestMapping(value = "/findListById", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "根据家具id查询家具信息")
	public List<Img>  findListById(Img img){
		List<Img> page = imgService.findListById(img);
        return page;
	}
	
	/**
	 * 查询家具
	 * @param Img
	 * @return
	 */
	@RequestMapping(value = "/findListImg", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "根据家具id查询家具图片")
	public Img  findListImg(ImgList imgList){
		Img img = new Img();
		img.setId(imgList.getImgId());
		List<Img> list = imgService.findListById(img);
		img = list.get(0);
		List<ImgList> page = imgListService.findList(imgList);
		img.setPicture(page);
        return img;
	} 
	/**
	 * 从数据库获取家具的byte数组
	 * @param img
	 * @return
	 */
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    @ResponseBody
	@ApiOperation(value = "家具面显示字节流")
    public void findById(@RequestParam(value = "imgid") String imgid,HttpServletResponse response) {
    	response.setContentType("image/jpeg");
        try{
            byte[] imgbytes= imgService.findById(imgid).getImage();
            OutputStream  out= response.getOutputStream();
            out.write(imgbytes);
        }catch (Exception e){
           
        }
    }
	
	/**
	 * 添加新家具
	 * @param Img
	 * @return
	 */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
	@ApiOperation(value = "添加新家具")
	public ReturnStatus insert(Img img,
    		@RequestParam(value = "files", required = false) MultipartFile[] files){
    	ReturnStatus ret = new ReturnStatus(ConstValues.FAILURE,"添加失败!");
        try {
        	String id = IdGen.uuid();
        	img.setId(id);
			int row=imgService.insert(img);
			//循环存储图片
			List<ImgList> list = new ArrayList<ImgList>();
			for(int i=0;i < files.length; i ++){
	      		MultipartFile file = files[i];
				byte[] data = file.getBytes();
				ImgList il= new ImgList();
				il.setImgId(id);	
				il.setPicture(data);
				list.add(il);
			}
			imgListService.inserts(list);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ret.setReturnValue(ConstValues.SUCCESS);
		ret.setMessage("添加成功!");
		return ret;	
	}
	
	/**
	 * 修改家具
	 * @param Img
	 * @return
	 */
    @RequestMapping(value = "/update", method=RequestMethod.POST)
    @ResponseBody
	@ApiOperation(value = "修改家具")
	public ReturnStatus update(Img img,
    		@RequestParam(value = "files", required = false) MultipartFile[] files){
    	ReturnStatus ret = new ReturnStatus(ConstValues.FAILURE,"更新失败!");
    	try {
//			if(file != null) {
//				byte[] data = file.getBytes();
//				img.setPicture(data);				
//			}
			int row=imgService.update(img);
			if(row == 1) {
				ret.setReturnValue(ConstValues.SUCCESS);
				ret.setMessage("更新成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;	
	}
	
	/**
	 * 更新库存
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/updatekc/{ids}", method=RequestMethod.DELETE)
    @ResponseBody
	@ApiOperation(value = "更新库存")
	public ReturnStatus updatekc(@PathVariable String ids){
		ReturnStatus ret = new ReturnStatus(ConstValues.FAILURE,"更新失败!");
		boolean boo=imgService.updatekc(ids);
		if(boo) {
			ret.setReturnValue(ConstValues.SUCCESS);
			ret.setMessage("更新成功!");
		}
		return ret;	
	}
	/**
	 * 删除家具
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/delete/{ids}", method=RequestMethod.DELETE)
    @ResponseBody
	@ApiOperation(value = "删除家具")
	public ReturnStatus delete(@PathVariable String ids){
		ReturnStatus ret = new ReturnStatus(ConstValues.FAILURE,"删除失败!");
		boolean boo=imgService.delete(ids);
		if(boo) {
			ret.setReturnValue(ConstValues.SUCCESS);
			ret.setMessage("删除成功!");
		}
		return ret;	
	}
}
