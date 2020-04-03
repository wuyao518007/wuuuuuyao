package com.ck.furn.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ck.furn.sys.bean.Address;
import com.ck.furn.sys.dao.AddressDao;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
@Transactional
public class AddressService {
	@Autowired
	private AddressDao addressDao;
	
	/**
	 * 查询列表
	 * @param address
	 * @return 
	 */
	public Page<Address> findList(Address address){
		int pageNum = address.getPageNum();
		int pageAmount = address.getPageAmount();
		Page<Address> page = PageHelper.startPage(pageNum, pageAmount);
		addressDao.findList(address);
		return page;
	}

	/**
	 * 不分页查询列表
	 * @param address
	 * @return 
	 */
	public List<Address> findAllList(Address address){
		List<Address> page = addressDao.findList(address);
		return page;
	}
	/**
	 * 添加
	 * @param address
	 * @return
	 */
	public int insert(Address address) {
		return addressDao.insert(address);
	}
	
	/**
	 * 修改
	 * @param address
	 * @return
	 */
	public int update(Address address) {
		return addressDao.update(address);
	}
	
	/**
	 * 删除
	 */
	public boolean delete(String ids){
		String[] arr = ids.split(",");
		int row=addressDao.delete(arr);
		boolean tru = false;
		if(ids.length() == row) {
			tru = true;
		}
		return tru;
	}
	
}
