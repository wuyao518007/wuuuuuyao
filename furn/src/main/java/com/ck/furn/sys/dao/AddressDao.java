package com.ck.furn.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ck.furn.sys.bean.Address;

@Mapper
public interface AddressDao {
	/**
	 * 查询
	 * @param address
	 * @return
	 */
	public List<Address> findList(Address address);
	/**
	 * 添加
	 * @param address
	 * @return
	 */
	public int insert(Address address);
	
	/**
	 * 修改
	 * @param address
	 * @return
	 */
	public int update(Address address);
	
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	public int delete(@Param("ids") String[] ids);
}
